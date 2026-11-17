import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {
  InsumoCalculado,
  Receta,
  RecetaIngrediente,
  RecetaRequest,
} from '../models/receta.model';

interface IngredientSummaryDTO {
  id: number;
  stockId: number;
  stockName?: string;
  ingredientName?: string;
  requiredAmount: number;
  unit: string;
}

interface RecipeDTO {
  id: number;
  name: string;
  baseQuantity: number;
  unitBase: string;
  flavor?: { id: number; name: string } | null;
  product?: { id: number; name: string } | null;
  ingredients?: IngredientSummaryDTO[];
}

interface RecipeSuppliesDTO {
  id: number;
  stockId?: number | null;
  ingredientName: string;
  baseQuantity?: number;
  requiredQuantity: number;
  unit?: string;
}

const toIngrediente = (i: IngredientSummaryDTO): RecetaIngrediente => ({
  id: i.id,
  stockId: i.stockId,
  nombreInsumo: i.stockName ?? i.ingredientName ?? `Insumo #${i.stockId}`,
  cantidadRequerida: i.requiredAmount,
  unidad: i.unit ?? 'KG',
});

const toReceta = (d: RecipeDTO): Receta => ({
  id: d.id,
  nombre: d.name,
  cantidadBase: d.baseQuantity,
  unidadBase: d.unitBase ?? 'KG',
  saborId: d.flavor?.id ?? null,
  saborNombre: d.flavor?.name ?? d.product?.name ?? d.name,
  ingredientes: (d.ingredients ?? []).map(toIngrediente),
});

@Injectable({
  providedIn: 'root',
})
export class RecetaService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/recipe`;

  listar(): Observable<Receta[]> {
    return this.http.get<RecipeDTO[]>(this.url).pipe(
      map((lista) => lista.map(toReceta)),
      map((lista) => lista.sort((a, b) => a.nombre.localeCompare(b.nombre))),
    );
  }

  obtener(id: number): Observable<Receta> {
    return this.http.get<RecipeDTO>(`${this.url}/${id}`).pipe(map(toReceta));
  }

  crear(body: RecetaRequest): Observable<Receta> {
    return this.http.post<RecipeDTO>(this.url, body).pipe(map(toReceta));
  }

  modificar(id: number, body: RecetaRequest): Observable<Receta> {
    return this.http.put<RecipeDTO>(`${this.url}/${id}`, body).pipe(map(toReceta));
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  calcularInsumos(id: number, kg: number): Observable<InsumoCalculado[]> {
    const params = new HttpParams().set('kg', kg);
    return this.http
      .get<RecipeSuppliesDTO[]>(`${this.url}/${id}/calculate-inputs`, { params })
      .pipe(
        map((lista) =>
          lista.map((item) => ({
            id: item.id,
            stockId: item.stockId ?? null,
            nombreInsumo: item.ingredientName,
            cantidadBase: item.baseQuantity ?? 0,
            cantidadCalculada: item.requiredQuantity,
            unidad: item.unit ?? 'KG',
          })),
        ),
      );
  }
}
