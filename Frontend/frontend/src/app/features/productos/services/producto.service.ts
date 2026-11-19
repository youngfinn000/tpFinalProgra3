import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {
  CategoriaProducto,
  FiltrosProducto,
  Producto,
  ProductoDTORequest,
  ProductoDTOResponse,
} from '../models/producto.model';

function inferirCategoria(nombre: string): {
  categoria: CategoriaProducto;
  label: string;
  icono: string;
} {
  const n = (nombre || '').toLowerCase();
  if (n.includes('kilo + medio') || n.includes('promo')) {
    return { categoria: 'PROMO', label: 'Promociones', icono: 'promo' };
  }
  if (n.includes('milkshake') || n.includes('frapuccino') || n.includes('batido')) {
    return { categoria: 'BATIDOS', label: 'Batidos', icono: 'batidos' };
  }
  if (
    n.includes('torta') ||
    n.includes('palito') ||
    n.includes('postre') ||
    n.includes('bombón')
  ) {
    return { categoria: 'POSTRES_HELADOS', label: 'Postres Helados', icono: 'postres' };
  }
  return { categoria: 'GELATO', label: 'Gelato', icono: 'gelato' };
}

function mapDtoToProducto(dto: ProductoDTOResponse): Producto {
  const cat = inferirCategoria(dto.name);
  return {
    id: dto.id,
    nombre: dto.name,
    precio: Number(dto.price) || 0,
    activo: dto.active ?? true,
    stock: dto.stock ?? 0,
    maxSabores: dto.maxFlavors,
    categoria: cat.categoria,
    categoriaLabel: cat.label,
    categoriaIcono: cat.icono,
    descripcion: `Producto artesanal STRETTO: ${dto.name}.`,
  };
}

@Injectable({ providedIn: 'root' })
export class ProductoService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/products`;

  /**
   * Obtiene la lista completa de productos desde el backend.
   */
  listar(filtros?: Partial<FiltrosProducto>): Observable<Producto[]> {
    return this.http.get<ProductoDTOResponse[]>(this.url).pipe(
      map((dtos) => (dtos || []).map(mapDtoToProducto)),
      catchError((err) => {
        console.warn('Backend /products no disponible:', err.message);
        return of([]);
      }),
      map((lista) => this.aplicarFiltros(lista, filtros))
    );
  }

  /**
   * Obtiene un producto por ID
   */
  obtener(id: number): Observable<Producto | null> {
    return this.http.get<ProductoDTOResponse>(`${this.url}/${id}`).pipe(
      map((dto) => mapDtoToProducto(dto)),
      catchError(() => of(null))
    );
  }

  /**
   * Búsqueda por nombre
   */
  buscarPorNombre(nombre: string): Observable<Producto[]> {
    const texto = nombre.trim();
    if (!texto) {
      return this.listar();
    }

    const params = new HttpParams().set('name', texto);
    return this.http.get<ProductoDTOResponse[]>(`${this.url}/search`, { params }).pipe(
      map((dtos) => (dtos || []).map(mapDtoToProducto)),
      catchError(() => of([]))
    );
  }

  /**
   * Cambiar estado activo/inactivo
   */
  cambiarEstado(id: number, activar: boolean): Observable<void> {
    const req$: Observable<unknown> = activar
      ? this.http.patch(`${this.url}/${id}/activate`, {})
      : this.http.delete(`${this.url}/${id}`);

    return req$.pipe(
      map((): void => {})
    );
  }

  /**
   * Crea un nuevo producto en el backend
   */
  createProducto(datos: any): Observable<Producto> {
    const body: ProductoDTORequest = {
      name: datos.nombre,
      price: Number(datos.precio),
      stock: Number(datos.stock),
      maxFlavors: (datos.saboresMax != null && datos.saboresMax !== '') ? Number(datos.saboresMax) : 0,
      active: datos.activo ?? true
    };

    return this.http.post<ProductoDTOResponse>(this.url, body).pipe(
      map((dto) => mapDtoToProducto(dto))
    );
  }

  /**
   * Actualiza un producto existente
   */
  actualizarProducto(id: number, datos: any): Observable<Producto> {
    const body: ProductoDTORequest = {
      name: datos.nombre,
      price: Number(datos.precio),
      stock: Number(datos.stock),
      maxFlavors: (datos.saboresMax != null && datos.saboresMax !== '') ? Number(datos.saboresMax) : 0,
      active: datos.activo ?? true
    };

    return this.http.put<ProductoDTOResponse>(`${this.url}/${id}`, body).pipe(
      map((dto) => mapDtoToProducto(dto))
    );
  }

  /**
   * Actualización rápida de solo el precio
   */
  actualizarPrecio(id: number, nuevoPrecio: number): Observable<Producto> {
    return this.http.patch<ProductoDTOResponse>(`${this.url}/${id}/price`, { price: nuevoPrecio }).pipe(
      map((dto) => mapDtoToProducto(dto))
    );
  }

  private aplicarFiltros(lista: Producto[], filtros?: Partial<FiltrosProducto>): Producto[] {
    if (!filtros) return lista;

    let res = [...lista];

    if (filtros.texto) {
      const q = filtros.texto.toLowerCase().trim();
      res = res.filter(
        (p) =>
          p.nombre.toLowerCase().includes(q) ||
          p.categoriaLabel.toLowerCase().includes(q) ||
          p.precio.toString().includes(q)
      );
    }

    if (filtros.categoria && filtros.categoria !== 'todas') {
      res = res.filter((p) => p.categoria === filtros.categoria);
    }

    if (filtros.estado === 'activos') {
      res = res.filter((p) => p.activo);
    } else if (filtros.estado === 'inactivos') {
      res = res.filter((p) => !p.activo);
    }

    return res;
  }
}
