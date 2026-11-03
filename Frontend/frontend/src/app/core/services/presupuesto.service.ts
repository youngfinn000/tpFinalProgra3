import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  SolicitudPresupuestoRequest,
  SolicitudPresupuestoResponse,
} from '../models/presupuesto.model';

@Injectable({
  providedIn: 'root',
})
export class PresupuestoService {
  private http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl.replace('/v1', '')}/request_budget`;

  crear(solicitud: SolicitudPresupuestoRequest): Observable<SolicitudPresupuestoResponse> {
    return this.http.post<SolicitudPresupuestoResponse>(this.baseUrl, solicitud);
  }

  obtenerPorId(id: number): Observable<SolicitudPresupuestoResponse> {
    return this.http.get<SolicitudPresupuestoResponse>(`${this.baseUrl}/${id}`);
  }

  obtenerPorCliente(customerId: number): Observable<SolicitudPresupuestoResponse[]> {
    return this.http.get<SolicitudPresupuestoResponse[]>(`${this.baseUrl}/customer/${customerId}`);
  }

  listarTodos(state?: string): Observable<SolicitudPresupuestoResponse[]> {
    const options = state ? { params: { state } } : {};
    return this.http.get<SolicitudPresupuestoResponse[]>(this.baseUrl, options);
  }

  actualizarEstado(
    id: number,
    state: string,
    budget: number = 0,
  ): Observable<SolicitudPresupuestoResponse> {
    return this.http.patch<SolicitudPresupuestoResponse>(`${this.baseUrl}/${id}/state`, {
      state,
      budget,
    });
  }
}
