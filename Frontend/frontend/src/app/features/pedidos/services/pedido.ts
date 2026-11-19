import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, switchMap, throwError } from 'rxjs';
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
    const userId = this.currentUserId();
    if (!userId) {
      return throwError(() => ({ error: { message: 'No hay usuario logueado (falta userId en localStorage)' } }));
    }

    const request = {
      saleChannelEnum: body.channel,
      paymentMethodEnum: body.paymentMethod,
      userID: userId,
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
    return this.http.get<any[]>(`${this.api}/products/available`).pipe(
      map((l) => l.map((p): Product => ({
        id: p.id,
        name: p.name,
        price: Number(p.price),
        maxFlavors: p.maxFlavors,  
        stock: p.stock,           
      }))),
    );
  }

  getFlavors() {
    return this.http.get<any[]>(`${this.api}/flavors`).pipe(
      map((l) => l
        .filter((f) => f.activeInactive)        
        .map((f): Flavor => ({ id: f.id, name: f.name }))),
    );
  }

  private currentUserId(): number {
  const token = localStorage.getItem('token');
  if (!token) return 0;
  try {
    const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
    const n = Number(payload?.userId ?? payload?.id);
    return Number.isFinite(n) && n > 0 ? n : 0;
  } catch {
    return 0;
  }
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