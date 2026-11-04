import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, switchMap } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { CreateOrderRequest, Flavor, NEXT_STATUS, Order, OrderDetail, Product } from '../models/pedido';

@Injectable({ providedIn: 'root' })
export class PedidoService {
  private http = inject(HttpClient);
  private api = environment.apiUrl;         
  private url = `${this.api}/orders`;

  findAll() {
    return this.http.get<any[]>(this.url).pipe(map((l) => l.map((r) => this.toOrder(r))));
  }

  get(id: number) {
    return this.http.get<any>(`${this.url}/${id}`).pipe(map((r) => this.toOrder(r)));
  }

  create(body: CreateOrderRequest) {
    const request = {
      saleChannelEnum: body.channel,
      paymentMethodEnum: body.paymentMethod,
      userID: this.currentUserId(),
      details: body.items.map((i) => ({
        productId: i.productId,
        flavorsId: i.flavorIds,
        quantity: i.quantity,
      })),
    };
    return this.http.post<any>(this.url, request).pipe(map((r) => this.toOrder(r)));
  }

  advance(order: Order) {
    const next = NEXT_STATUS[order.status];
    if (!next) throw new Error('El pedido no puede avanzar');
    return this.http
      .patch<any>(`${this.url}/${order.id}/state`, {}, { params: { state: next } })
      .pipe(map((r) => this.toOrder(r)));
  }

  cancel(id: number) {
    return this.http.patch<void>(`${this.url}/${id}/cancel`, {}).pipe(switchMap(() => this.get(id)));
  }

  getProducts() {
    return this.http.get<Product[]>(`${this.api}/products/available`);
  }

  getFlavors() {
    return this.http.get<any[]>(`${this.api}/flavors`).pipe(
      map((l) => l
        .filter((f) => f.activeInactive)          // true = activo (si es al revés, sacá el "!" o invertilo)
        .map((f): Flavor => ({ id: f.id, name: f.name }))),
    );
  }

  private currentUserId(): number {
    return Number(localStorage.getItem('userId'));
  }

  private toOrder(r: any): Order {
    return {
      id: r.id,
      createdAt: r.date,
      status: r.stateOrderEnum,
      channel: r.saleChannelEnum,
      paymentMethod: r.paymentMethodEnum,
      total: r.total,
      userName: r.username,
      details: (r.details ?? []).map((d: any): OrderDetail => ({
        id: d.id,
        productId: d.productId,
        productName: d.productName,
        flavorNames: d.flavorNames ?? [],
        quantity: d.quantity,
        unitPrice: d.unitPrice,
        subtotal: d.unitPrice * d.quantity,
      })),
    };
  }
}