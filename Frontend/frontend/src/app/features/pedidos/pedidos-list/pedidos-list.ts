import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { PedidoService } from '../services/pedido';
import { canAdvance, canCancel, CHANNEL_LABEL, CHANNELS, NEXT_STATUS_LABEL, Order, PAYMENT_LABEL, STATUS_LABEL, STATUSES } from '../models/pedido';


@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './pedidos-list.html',
    styleUrl: './pedidos-list.css',
})
export class OrderList implements OnInit {
  private fb = inject(FormBuilder);
  private service = inject(PedidoService);

  all = signal<Order[]>([]);
  loading = signal(false);
  message = signal<{ type: 'ok' | 'error'; text: string } | null>(null);

  statuses = STATUSES;
  channels = CHANNELS;
  statusLabel = STATUS_LABEL;
  channelLabel = CHANNEL_LABEL;
  paymentLabel = PAYMENT_LABEL;
  nextLabel = NEXT_STATUS_LABEL;
  canAdvance = canAdvance;
  canCancel = canCancel;

  filters = this.fb.group({ status: [''], channel: [''], from: [''], to: [''], user: [''] });
  private fv = toSignal(this.filters.valueChanges, { initialValue: this.filters.getRawValue() });

  orders = computed(() => {
    const f = this.fv() as any;
    const user = (f.user ?? '').trim().toLowerCase();
    return this.all()
      .filter((o) => {
        const day = (o.createdAt ?? '').slice(0, 10);
        return (!f.status || o.status === f.status)
          && (!f.channel || o.channel === f.channel)
          && (!f.from || day >= f.from)
          && (!f.to || day <= f.to)
          && (!user || (o.userName ?? '').toLowerCase().includes(user));
      })
      .sort((a, b) => (b.createdAt ?? '').localeCompare(a.createdAt ?? ''));
  });

  ngOnInit() { this.load(); }

  load() {
    this.loading.set(true);
    this.service.findAll().subscribe({
      next: (data) => { this.all.set(data); this.loading.set(false); },
      error: (e) => { this.loading.set(false); this.showError(e); },
    });
  }

  clearFilters() {
    this.filters.reset({ status: '', channel: '', from: '', to: '', user: '' });
  }

  advance(o: Order) {
    this.service.advance(o).subscribe({
      next: (updated) => {
        this.replace(updated);
        this.message.set({ type: 'ok', text: `Pedido #${updated.id} pasó a ${STATUS_LABEL[updated.status]}` });
      },
      error: (e) => this.showError(e),
    });
  }

  cancel(o: Order) {
    if (!confirm(`¿Cancelar el pedido #${o.id}? Esta acción no se puede deshacer.`)) return;
    this.service.cancel(o.id).subscribe({
      next: (updated) => {
        this.replace(updated);
        this.message.set({ type: 'ok', text: `Pedido #${updated.id} cancelado` });
      },
      error: (e) => this.showError(e),
    });
  }

  private replace(updated: Order) {
    this.all.update((list) => list.map((o) => (o.id === updated.id ? updated : o)));
  }

  private showError(e: any) {
    this.message.set({ type: 'error', text: e?.error?.message ?? 'Ocurrió un error. Intentá de nuevo.' });
  }
}