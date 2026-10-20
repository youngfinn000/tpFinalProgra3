import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { map } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Insumo , InsumoRequest, ESTADO_ACTIVO} from '../models/insumo';

interface StockDTO {
  id: number;
  name: string;
  currentStock: number;
  minimumStock: number;
  unit: string;
  status: string;
}

const toInsumo = (d: StockDTO): Insumo => ({
  id: d.id,
  nombre: d.name,
  unidad: d.unit,
  stockActual: d.currentStock,
  puntoReposicion: d.minimumStock,
  estado: d.status,
});

const toRequest = (r: InsumoRequest) => ({
  name: r.nombre,
  currentStock: r.stockActual,
  minimumStock: r.puntoReposicion,
  unitMeasurement: r.unidad,
  status: r.estado ?? ESTADO_ACTIVO,
});

@Injectable({
  providedIn: 'root',
})
export class InsumoService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/stock`;

  listar() {
    return this.http.get<StockDTO[]>(this.url).pipe(
      map((lista) => lista.map(toInsumo)),
      map((lista) =>
        lista.sort((a, b) => a.nombre.localeCompare(b.nombre)),
      ),
    );
  }

  obtener(id: number) {
    return this.http
      .get<StockDTO>(`${this.url}/${id}`)
      .pipe(map(toInsumo));
  }

  crear(body: InsumoRequest) {
    return this.http
      .post<StockDTO>(this.url, toRequest(body))
      .pipe(map(toInsumo));
  }

  modificar(id: number, body: InsumoRequest) {
    return this.http
      .put<StockDTO>(`${this.url}/${id}`, toRequest(body))
      .pipe(map(toInsumo));
  }

  eliminar(id: number) {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}