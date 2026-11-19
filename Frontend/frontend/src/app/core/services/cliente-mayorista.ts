import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ClienteMayorista as IClienteMayorista, ClienteMayoristaRequest } from '../models/cliente-mayorista.model';

@Injectable({
  providedIn: 'root',
})
export class ClienteMayoristaService {
  private http = inject(HttpClient);
  // El backend mapea wholesale_customers directamente bajo /api (sin /v1)
  private readonly baseUrl = `${environment.apiUrl.replace('/v1', '')}/wholesale_customers`;

  listar(): Observable<IClienteMayorista[]> {
    return this.http.get<IClienteMayorista[]>(this.baseUrl);
  }

  obtenerPorId(id: number): Observable<IClienteMayorista> {
    return this.http.get<IClienteMayorista>(`${this.baseUrl}/${id}`);
  }

  crear(cliente: ClienteMayoristaRequest): Observable<IClienteMayorista> {
    return this.http.post<IClienteMayorista>(this.baseUrl, cliente);
  }

  actualizar(id: number, cliente: ClienteMayoristaRequest): Observable<IClienteMayorista> {
    return this.http.put<IClienteMayorista>(`${this.baseUrl}/${id}`, cliente);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  buscarPorEmail(email: string): Observable<IClienteMayorista> {
    return this.http.get<IClienteMayorista>(`${this.baseUrl}/search`, {
      params: { email },
    });
  }

  obtenerPresupuestos(id: number, state?: string): Observable<any[]> {
    const options = state ? { params: { state } } : {};
    return this.http.get<any[]>(`${this.baseUrl}/${id}/requests`, options);
  }
}

// Alias para mantener compatibilidad con el spec original
export { ClienteMayoristaService as ClienteMayorista };
