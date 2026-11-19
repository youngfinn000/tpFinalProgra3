import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { PedidoService } from '../services/pedido';
import {
  canAdvance, canCancel, CHANNEL_LABEL, NEXT_STATUS_LABEL,
  Order, PAYMENT_LABEL, STATUS_LABEL,
} from '../models/pedido';

@Component({
  selector: 'app-order-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './pedidos-detail.html',
  styleUrl: './pedidos-detail.css',
})
export class OrderDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private service = inject(PedidoService);

  order = signal<Order | null>(null);
  loading = signal(true);
  message = signal<{ type: 'ok' | 'error'; text: string } | null>(null);

  statusLabel = STATUS_LABEL;
  channelLabel = CHANNEL_LABEL;
  paymentLabel = PAYMENT_LABEL;
  nextLabel = NEXT_STATUS_LABEL;
  canAdvance = canAdvance;
  canCancel = canCancel;

  totalUnits = computed(() =>
    (this.order()?.details ?? []).reduce((sum, d) => sum + d.quantity, 0),
  );

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) {
      this.loading.set(false);
      this.message.set({ type: 'error', text: 'Id de pedido inválido' });
      return;
    }
    this.load(id);
  }

  private load(id: number) {
    this.loading.set(true);
    this.service.get(id).subscribe({
      next: (o) => { this.order.set(o); this.loading.set(false); },
      error: (e) => {
        this.loading.set(false);
        this.showError(e, 'No se pudo cargar el pedido');
      },
    });
  }

  advance() {
    const o = this.order();
    if (!o) return;
    this.service.advance(o).subscribe({
      next: (updated) => {
        this.order.set(updated);
        this.message.set({ type: 'ok', text: `El pedido pasó a ${STATUS_LABEL[updated.status]}` });
      },
      error: (e) => this.showError(e),
    });
  }

  cancel() {
    const o = this.order();
    if (!o) return;
    if (!confirm(`¿Cancelar el pedido #${o.id}? Esta acción no se puede deshacer.`)) return;
    this.service.cancel(o.id).subscribe({
      next: (updated) => {
        this.order.set(updated);
        this.message.set({ type: 'ok', text: 'Pedido cancelado' });
      },
      error: (e) => this.showError(e),
    });
  }

  private showError(e: any, fallback = 'Ocurrió un error. Intentá de nuevo.') {
    this.message.set({ type: 'error', text: e?.error?.message ?? `${fallback} (HTTP ${e?.status})` });
  }
}