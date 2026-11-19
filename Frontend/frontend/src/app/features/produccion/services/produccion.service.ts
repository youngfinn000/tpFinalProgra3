import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {
  CrearLoteRequest,
  EstadoLote,
  FiltrosHistorialProduccion,
  LoteProduccion,
} from '../models/produccion.model';

interface ProductionLotDTO {
  id: number;
  amountProduced: number;
  status: EstadoLote;
  performancePCT: number;
  productionDate: string;
  recipe?: {
    id: number;
    name: string;
    flavorId?: number | null;
    flavorName?: string | null;
  } | null;
  internalUser?: {
    id: number;
    name: string;
  } | null;
  user?: {
    id: number;
    name: string;
  } | null;
}

const toLote = (d: ProductionLotDTO): LoteProduccion => {
  const usr = d.internalUser ?? d.user;
  return {
    id: d.id,
    recetaId: d.recipe?.id ?? 0,
    recetaNombre: d.recipe?.name ?? 'Receta',
    saborId: d.recipe?.flavorId ?? null,
    saborNombre: d.recipe?.flavorName ?? d.recipe?.name ?? 'Sin sabor',
    usuarioId: usr?.id ?? null,
    usuarioNombre: usr?.name ?? 'Operador',
    cantidadProducida: d.amountProduced ?? 0,
    rendimientoPct: d.performancePCT ?? 100,
    estado: d.status ?? 'COMPLETED',
    fechaProduccion: d.productionDate ?? '',
  };
};

@Injectable({
  providedIn: 'root',
})
export class ProduccionService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/productionLot`;

  crear(body: CrearLoteRequest): Observable<LoteProduccion> {
    return this.http
      .post<ProductionLotDTO>(this.url, {
        recipeId: body.recipeId,
        amountProduced: body.amountProduced,
        productionDate: body.productionDate,
        status: body.status ?? 'COMPLETED',
      })
      .pipe(map(toLote));
  }

  listar(): Observable<LoteProduccion[]> {
    return this.http
      .get<ProductionLotDTO[]>(this.url)
      .pipe(map((lista) => lista.map(toLote)));
  }

  obtener(id: number): Observable<LoteProduccion> {
    return this.http.get<ProductionLotDTO>(`${this.url}/${id}`).pipe(map(toLote));
  }

  historial(filtros?: FiltrosHistorialProduccion): Observable<LoteProduccion[]> {
    let params = new HttpParams();
    if (filtros?.fecha) {
      params = params.set('date', filtros.fecha);
    }
    if (filtros?.recetaId) {
      params = params.set('recipeId', filtros.recetaId);
    }
    if (filtros?.saborId) {
      params = params.set('flavorId', filtros.saborId);
    }

    return this.http
      .get<ProductionLotDTO[]>(`${this.url}/history`, { params })
      .pipe(map((lista) => lista.map(toLote)));
  }
}
