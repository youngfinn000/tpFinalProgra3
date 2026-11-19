import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Empleado, EmpleadoRequest } from '../models/empleado.model';

@Injectable({
  providedIn: 'root',
})
export class EmpleadoService {
  private http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/internalUser`;

  listar(): Observable<Empleado[]> {
    return this.http.get<Empleado[]>(this.baseUrl);
  }

  obtenerPorId(id: number): Observable<Empleado> {
    return this.http.get<Empleado>(`${this.baseUrl}/${id}`);
  }

  crear(empleado: EmpleadoRequest): Observable<Empleado> {
    return this.http.post<Empleado>(this.baseUrl, empleado);
  }

  actualizar(id: number, empleado: EmpleadoRequest): Observable<Empleado> {
    return this.http.put<Empleado>(`${this.baseUrl}/${id}`, empleado);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  activar(id: number): Observable<Empleado> {
    return this.http.patch<Empleado>(`${this.baseUrl}/${id}/activate`, {});
  }
}

