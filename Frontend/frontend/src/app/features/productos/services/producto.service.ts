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

const PRODUCTOS_CATALOGO_INICIAL: Producto[] = [
  // GELATO
  {
    id: 1,
    nombre: 'Cucurucho',
    precio: 8000,
    activo: true,
    stock: 25,
    categoria: 'GELATO',
    categoriaLabel: 'Gelato',
    categoriaIcono: 'gelato',
    maxSabores: 2,
    descripcion: 'Cucurucho artesanal crocante con hasta 2 sabores a elección.',
  },
  {
    id: 2,
    nombre: 'Vaso Grande',
    precio: 7000,
    activo: true,
    stock: 32,
    categoria: 'GELATO',
    categoriaLabel: 'Gelato',
    categoriaIcono: 'gelato',
    maxSabores: 2,
    descripcion: 'Vaso térmico mediano/grande para disfrutar de 2 gustos.',
  },
  {
    id: 3,
    nombre: 'Vaso Chico',
    precio: 5000,
    activo: true,
    stock: 40,
    categoria: 'GELATO',
    categoriaLabel: 'Gelato',
    categoriaIcono: 'gelato',
    maxSabores: 1,
    descripcion: 'Vaso individual ideal para una porción justa de 1 gusto.',
  },
  {
    id: 4,
    nombre: 'Cuarto KG',
    precio: 9000,
    activo: true,
    stock: 18,
    categoria: 'GELATO',
    categoriaLabel: 'Gelato',
    categoriaIcono: 'gelato',
    maxSabores: 3,
    descripcion: 'Pote térmico de 250 gramos, admite hasta 3 sabores.',
  },
  {
    id: 5,
    nombre: 'Medio KG',
    precio: 14000,
    activo: true,
    stock: 15,
    categoria: 'GELATO',
    categoriaLabel: 'Gelato',
    categoriaIcono: 'gelato',
    maxSabores: 3,
    descripcion: 'Pote térmico de 500 gramos para compartir, hasta 3 sabores.',
  },
  {
    id: 6,
    nombre: 'Kilo',
    precio: 22000,
    activo: true,
    stock: 12,
    categoria: 'GELATO',
    categoriaLabel: 'Gelato',
    categoriaIcono: 'gelato',
    maxSabores: 4,
    descripcion: 'Pote térmico de 1000 gramos familiar, hasta 4 sabores.',
  },

  // POSTRES HELADOS
  {
    id: 7,
    nombre: 'Torta',
    precio: 32000,
    activo: true,
    stock: 6,
    categoria: 'POSTRES_HELADOS',
    categoriaLabel: 'Postres Helados',
    categoriaIcono: 'postres',
    maxSabores: 0,
    descripcion: 'Torta helada artesanal con base crocante y baño de chocolate.',
  },
  {
    id: 8,
    nombre: 'Palito Especial',
    precio: 7000,
    activo: true,
    stock: 22,
    categoria: 'POSTRES_HELADOS',
    categoriaLabel: 'Postres Helados',
    categoriaIcono: 'postres',
    maxSabores: 0,
    descripcion: 'Palito bañado en chocolate belga con cobertura de crocante.',
  },
  {
    id: 9,
    nombre: 'Palito',
    precio: 5000,
    activo: true,
    stock: 30,
    categoria: 'POSTRES_HELADOS',
    categoriaLabel: 'Postres Helados',
    categoriaIcono: 'postres',
    maxSabores: 0,
    descripcion: 'Palito de agua o crema tradicional.',
  },

  // BATIDOS
  {
    id: 10,
    nombre: 'Milkshake',
    precio: 8000,
    activo: true,
    stock: 20,
    categoria: 'BATIDOS',
    categoriaLabel: 'Batidos',
    categoriaIcono: 'batidos',
    maxSabores: 1,
    descripcion: 'Batido cremoso elaborado en el momento con helado artesanal.',
  },
  {
    id: 11,
    nombre: 'Frapuccino',
    precio: 8000,
    activo: true,
    stock: 16,
    categoria: 'BATIDOS',
    categoriaLabel: 'Batidos',
    categoriaIcono: 'batidos',
    maxSabores: 1,
    descripcion: 'Café espresso batido con helado de crema y hielo picado.',
  },

  // PROMO
  {
    id: 12,
    nombre: 'Kilo + Medio Kilo',
    precio: 28000,
    activo: true,
    stock: 8,
    categoria: 'PROMO',
    categoriaLabel: 'Promociones',
    categoriaIcono: 'promo',
    maxSabores: 4,
    descripcion: 'Promo especial familiar: 1 Kilo + 1/2 Kilo a precio promocional.',
  },
];

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

  // Copia local en memoria para persistencia simulada si backend no está activo
  private mockProductos: Producto[] = [...PRODUCTOS_CATALOGO_INICIAL];

  /**
   * Obtiene la lista completa de productos desde el backend,
   * con fallback automático a la lista de catálogo si el backend no responde.
   */
  listar(filtros?: Partial<FiltrosProducto>): Observable<Producto[]> {
    return this.http.get<ProductoDTOResponse[]>(this.url).pipe(
      map((dtos) => {
        if (!dtos || dtos.length === 0) {
          return this.mockProductos;
        }
        return dtos.map(mapDtoToProducto);
      }),
      catchError((err) => {
        console.warn('Backend /products no disponible. Usando catálogo local:', err.message);
        return of(this.mockProductos);
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
      catchError(() => {
        const prod = this.mockProductos.find((p) => p.id === id) || null;
        return of(prod);
      })
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
      map((dtos) => dtos.map(mapDtoToProducto)),
      catchError(() => {
        const filtrados = this.mockProductos.filter((p) =>
          p.nombre.toLowerCase().includes(texto.toLowerCase())
        );
        return of(filtrados);
      })
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
      map((): void => {
        this.actualizarEstadoLocal(id, activar);
      }),
      catchError(() => {
        this.actualizarEstadoLocal(id, activar);
        return of(undefined);
      })
    );
  }

  
  createProducto(datos: any): Observable<Producto> {
    // Armamos el DTO que espera la API
    const body: ProductoDTORequest = {
      name: datos.nombre,
      price: Number(datos.precio),
      stock: Number(datos.stock),
      maxFlavors: datos.saboresMax ? Number(datos.saboresMax) : undefined,
      active: datos.activo ?? true
    };

    return this.http.post<ProductoDTOResponse>(this.url, body).pipe(
      map((dto) => {
        const nuevoProd = mapDtoToProducto(dto);
        this.mockProductos.unshift(nuevoProd);
        return nuevoProd;
      }),
      catchError((err) => {
        console.warn('Backend /products no disponible para crear. Guardando localmente:', err.message);

        const cat = inferirCategoria(datos.nombre);
        const nuevoProdLocal: Producto = {
          id: this.mockProductos.length + 1,
          nombre: datos.nombre,
          precio: Number(datos.precio) || 0,
          activo: datos.activo ?? true,
          stock: Number(datos.stock) || 0,
          maxSabores: datos.saboresMax ? Number(datos.saboresMax) : 0,
          categoria: cat.categoria,
          categoriaLabel: cat.label,
          categoriaIcono: cat.icono,
          descripcion: datos.descripcion || `Producto artesanal STRETTO: ${datos.nombre}.`
        };

        this.mockProductos.unshift(nuevoProdLocal);
        return of(nuevoProdLocal);
      })
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
      maxFlavors: datos.saboresMax ? Number(datos.saboresMax) : undefined,
      active: datos.activo ?? true
    };

    return this.http.put<ProductoDTOResponse>(`${this.url}/${id}`, body).pipe(
      map((dto) => {
        const actualizado = mapDtoToProducto(dto);
        this.actualizarLocal(id, actualizado);
        return actualizado;
      }),
      catchError((err) => {
        console.warn('Backend no disponible para actualizar. Modificando localmente:', err.message);
        const cat = inferirCategoria(datos.nombre);
        const prodActualizado: Producto = {
          id,
          nombre: datos.nombre,
          precio: Number(datos.precio),
          activo: datos.activo ?? true,
          stock: Number(datos.stock) || 0,
          maxSabores: datos.saboresMax ? Number(datos.saboresMax) : 0,
          categoria: cat.categoria,
          categoriaLabel: cat.label,
          categoriaIcono: cat.icono,
          descripcion: datos.descripcion || ''
        };
        this.actualizarLocal(id, prodActualizado);
        return of(prodActualizado);
      })
    );
  }

  /**
   * Actualización rápida de solo el precio
   */
  actualizarPrecio(id: number, nuevoPrecio: number): Observable<Producto> {
    return this.http.patch<ProductoDTOResponse>(`${this.url}/${id}/precio`, { price: nuevoPrecio }).pipe(
      map((dto) => {
        const actualizado = mapDtoToProducto(dto);
        this.actualizarPrecioLocal(id, nuevoPrecio);
        return actualizado;
      }),
      catchError(() => {
        this.actualizarPrecioLocal(id, nuevoPrecio);
        const prod = this.mockProductos.find((p) => p.id === id);
        return of(prod!);
      })
    );
  }

  private actualizarLocal(id: number, prod: Producto): void {
    const idx = this.mockProductos.findIndex((p) => p.id === id);
    if (idx !== -1) {
      this.mockProductos[idx] = prod;
    }
  }

  private actualizarPrecioLocal(id: number, precio: number): void {
    const idx = this.mockProductos.findIndex((p) => p.id === id);
    if (idx !== -1) {
      this.mockProductos[idx] = { ...this.mockProductos[idx], precio };
    }
  }

  private actualizarEstadoLocal(id: number, activo: boolean): void {
    const idx = this.mockProductos.findIndex((p) => p.id === id);
    if (idx !== -1) {
      this.mockProductos[idx] = { ...this.mockProductos[idx], activo };
    }
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
