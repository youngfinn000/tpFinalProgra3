import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, forkJoin, map, of, throwError } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Sabor, SaborForm } from '../models/sabor.model';

// Ajustá estos nombres a tu FlavorsDTOResponse
interface FlavorDTO {
  id: number;
  name: string;
  activeInactive?: boolean;  
 
}

const toSabor = (d: FlavorDTO): Sabor => ({
  id: d.id,
  nombre: d.name,
  activo: d.activeInactive ?? d.activeInactive ?? false,
});

// Ajustá estos nombres a tu FlavorsDTORequest
const toRequest = (r: SaborForm) => ({
  name: r.nombre,
});

export interface FiltrosSabor {
  estado: 'todos' | 'activos' | 'inactivos';
  nombre: string;
  modo: 'parcial' | 'exacta';
}

@Injectable({ providedIn: 'root' })

export class SaborService {

  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/flavors`;

  listar(f: FiltrosSabor): Observable<Sabor[]> {
    const texto = f.nombre.trim();
    let fuente$: Observable<FlavorDTO[]>;

    if (texto) {
      fuente$ =
        f.modo === 'exacta'
          ? this.http.get<FlavorDTO>(`${this.url}/name/${encodeURIComponent(texto)}`).pipe(
              map((d) => [d]),
              catchError((err) =>
                err.status === 404 ? of([] as FlavorDTO[]) : throwError(() => err),
              ),
            )
          : this.http.get<FlavorDTO[]>(`${this.url}/search`, {
              params: new HttpParams().set('name', texto),
            });
    } else if (f.estado === 'inactivos') {
      fuente$ = this.http.get<FlavorDTO[]>(`${this.url}/inactive`);
    } else if (f.estado === 'activos') {
      fuente$ = this.http.get<FlavorDTO[]>(this.url);
    } else {
      fuente$ = forkJoin([
        this.http.get<FlavorDTO[]>(this.url),
        this.http.get<FlavorDTO[]>(`${this.url}/inactive`),
      ]).pipe(map(([a, i]) => [...a, ...i]));
    }

    return fuente$.pipe(
      map((lista) => [...new Map(lista.map((d) => [d.id, d])).values()]), // sin duplicados
      map((lista) => lista.map(toSabor)),
      map((lista) =>
        lista.filter((s) => f.estado === 'todos' || (f.estado === 'activos') === s.activo),
      ),
      map((lista) => lista.sort((a, b) => a.nombre.localeCompare(b.nombre))),
    );
  }

  obtener(id: number) {
    return this.http.get<FlavorDTO>(`${this.url}/${id}`).pipe(map(toSabor));
  }

  crear(body: SaborForm) {
    return this.http.post<FlavorDTO>(this.url, toRequest(body)).pipe(map(toSabor));
  }

  modificar(id: number, body: SaborForm) {
    return this.http.put<FlavorDTO>(`${this.url}/${id}`, toRequest(body)).pipe(map(toSabor));
  }

  desactivar(id: number) {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  reactivar(id: number) {
    return this.http
      .patch<FlavorDTO>(`${this.url}/${id}/activate`, {})
      .pipe(map(() => undefined));
}
}
