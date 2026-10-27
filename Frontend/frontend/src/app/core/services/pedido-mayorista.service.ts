import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PedidoMayoristaResponse } from '../models/pedido-mayorista.model';

@Injectable({
  providedIn: 'root',
})
export class PedidoMayoristaService {
  private http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl.replace('/v1', '')}/wholesale-orders`;

  obtenerPorId(id: number): Observable<PedidoMayoristaResponse> {
    return this.http.get<PedidoMayoristaResponse>(`${this.baseUrl}/${id}`);
  }

  obtenerPorPresupuestoId(requestBudgetId: number): Observable<PedidoMayoristaResponse> {
    return this.http.get<PedidoMayoristaResponse>(`${this.baseUrl}/by-budget/${requestBudgetId}`);
  }

  obtenerPorCliente(customerId: number): Observable<PedidoMayoristaResponse[]> {
    return this.http.get<PedidoMayoristaResponse[]>(`${this.baseUrl}/customer/${customerId}`);
  }

  crearDesdePresupuesto(requestBudgetId: number): Observable<PedidoMayoristaResponse> {
    return this.http.post<PedidoMayoristaResponse>(
      `${this.baseUrl}/request-budget/${requestBudgetId}`,
      null
    );
  }

  listarTodos(): Observable<PedidoMayoristaResponse[]> {
    return this.http.get<PedidoMayoristaResponse[]>(this.baseUrl);
  }

  registrarAnticipo(orderId: number, customerId: number, amount: number): Observable<PedidoMayoristaResponse> {
    const params = new HttpParams()
      .set('customerId', customerId.toString())
      .set('amount', amount.toString());

    return this.http.patch<PedidoMayoristaResponse>(
      `${this.baseUrl}/${orderId}/advance-payment`,
      null,
      { params }
    );
  }
}
