export interface Insumo {
  id: number;
  nombre: string;
  unidad: string;
  stockActual: number;
  puntoReposicion: number;
  estado: string;
}

export interface InsumoRequest {
  nombre: string;
  unidad: string;
  stockActual: number;
  puntoReposicion: number;
  estado?: string;
}

export const UNIDADES: { valor: string; etiqueta: string }[] = [
  { valor: 'KG', etiqueta: 'Kilogramos (kg)' },
  { valor: 'GRAM', etiqueta: 'Gramos (g)' },
  { valor: 'LITER', etiqueta: 'Litros (L)' },
  { valor: 'ML', etiqueta: 'Mililitros (ml)' },
  { valor: 'UNIT', etiqueta: 'Unidades' },
];

export const ESTADO_ACTIVO = 'ACTIVE';

export const etiquetaUnidad = (valor: string): string =>
  UNIDADES.find((u) => u.valor === valor)?.etiqueta ?? valor;