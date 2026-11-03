import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  CHANNELS, CHANNEL_LABEL, PAYMENT_LABEL, PAYMENT_METHODS, Flavor, Order, Product,
} from '../models/pedido';
import { OrderService } from '../services/pedido';

@Component({
  selector: 'app-order-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './pedido-form.html',
})
export class OrderForm implements OnInit {
  private fb = inject(FormBuilder);
  private service = inject(OrderService);
  private router = inject(Router);

  products = signal<Product[]>([]);
  flavors = signal<Flavor[]>([]);
  channels = CHANNELS;
  channelLabel = CHANNEL_LABEL;
  paymentMethods = PAYMENT_METHODS;
  paymentLabel = PAYMENT_LABEL;

  saving = signal(false);
  error = signal<string | null>(null);
  created = signal<Order | null>(null);

  form = this.fb.group({
    channel: ['', Validators.required],
    paymentMethod: ['', Validators.required],
    items: this.fb.array([this.newItem()]),
  });

  private formValue = toSignal(this.form.valueChanges, { initialValue: this.form.value });

  total = computed(() => {
    const items = (this.formValue().items ?? []) as any[];
    return items.reduce((sum, it) => {
      const p = this.products().find((x) => x.id === Number(it?.productId));
      return sum + (p ? p.price * (Number(it?.quantity) || 0) : 0);
    }, 0);
  });

  get items() { return this.form.get('items') as FormArray; }

  ngOnInit() {
    this.service.getProducts().subscribe({
      next: (p) => this.products.set(p),
      error: () => this.error.set('No se pudieron cargar los productos'),
    });
    this.service.getFlavors().subscribe({
      next: (f) => this.flavors.set(f),
      error: () => this.error.set('No se pudieron cargar los sabores'),
    });
  }

  private newItem() {
    const g = this.fb.group({
      productId: [null as number | null, Validators.required],
      flavorIds: [[] as number[], Validators.required],   // al menos 1 sabor
      quantity: [1, [Validators.required, Validators.min(1)]],
    });
    g.get('productId')!.valueChanges.subscribe(() => g.get('flavorIds')!.setValue([]));
    return g;
  }

  addItem() { this.items.push(this.newItem()); }
  removeItem(i: number) { if (this.items.length > 1) this.items.removeAt(i); }

  // ---- helpers por línea ----
  productOf(i: number) {
    const id = Number(this.items.at(i).value.productId);
    return this.products().find((p) => p.id === id);
  }
  maxFlavors(i: number) { return this.productOf(i)?.maxFlavors ?? 0; }
  stockOf(i: number) { return this.productOf(i)?.stock; }
  selected(i: number): number[] { return this.items.at(i).value.flavorIds ?? []; }
  isChecked(i: number, id: number) { return this.selected(i).includes(id); }
  isDisabled(i: number, id: number) {
    return !this.isChecked(i, id) && this.selected(i).length >= this.maxFlavors(i);
  }

  toggleFlavor(i: number, id: number) {
    const ctrl = this.items.at(i).get('flavorIds')!;
    const cur = ctrl.value as number[];
    ctrl.setValue(cur.includes(id) ? cur.filter((x) => x !== id) : [...cur, id]);
    ctrl.markAsTouched();
  }

  subtotal(i: number) {
    const p = this.productOf(i);
    return p ? p.price * (Number(this.items.at(i).value.quantity) || 0) : 0;
  }

  submit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }

    for (let i = 0; i < this.items.length; i++) {
      const max = this.maxFlavors(i);
      const n = this.selected(i).length;
      if (n < 1 || n > max) {
        this.error.set(`Producto ${i + 1}: elegí entre 1 y ${max} sabores`);
        return;
      }
      const stock = this.stockOf(i);
      if (stock != null && Number(this.items.at(i).value.quantity) > stock) {
        this.error.set(`Producto ${i + 1}: stock disponible ${stock}`);
        return;
      }
    }

    const v = this.form.getRawValue();
    this.saving.set(true);
    this.error.set(null);
    this.service.create({
      channel: v.channel as any,
      paymentMethod: v.paymentMethod as any,
      items: v.items.map((it: any) => ({
        productId: Number(it.productId),
        flavorIds: it.flavorIds as number[],
        quantity: Number(it.quantity),
      })),
    }).subscribe({
      next: (order) => { this.saving.set(false); this.created.set(order); },
      error: (e) => {
        this.saving.set(false);
        this.error.set(e?.error?.message ?? 'No se pudo registrar el pedido');
      },
    });
  }

  newOrder() {
    this.created.set(null);
    this.error.set(null);
    this.form.reset({ channel: '', paymentMethod: '' });
    this.items.clear();
    this.addItem();
  }

  goToDetail() { this.router.navigate(['/orders', this.created()!.id]); }
}