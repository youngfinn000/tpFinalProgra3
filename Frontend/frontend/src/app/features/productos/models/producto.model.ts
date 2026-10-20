export type CategoriaProducto = 'GELATO' | 'POSTRES_HELADOS' | 'BATIDOS' | 'PROMO';

export interface Producto {
  id: number;
  nombre: string;
  precio: number;
  activo: boolean;
  stock: number;
  categoria: CategoriaProducto;
  categoriaLabel: string;
  categoriaIcono: string;
  maxSabores?: number;
  descripcion?: string;
}

export interface ProductoDTORequest {
  name: string;
  price: number;
  maxFlavors?: number;
  stock?: number;
}

export interface ProductoDTOResponse {
  id: number;
  name: string;
  price: number;
  active: boolean;
  maxFlavors?: number;
  stock?: number;
}

export interface FiltrosProducto {
  texto: string;
  categoria: string;
  estado: 'todos' | 'activos' | 'inactivos';
}

