export interface RecetaIngrediente {
  id?: number;
  stockId: number;
  nombreInsumo: string;
  cantidadRequerida: number;
  unidad: string;
}

export interface Receta {
  id: number;
  nombre: string;
  cantidadBase: number;
  unidadBase: string;
  saborId: number | null;
  saborNombre: string;
  ingredientes: RecetaIngrediente[];
}

export interface RecetaIngredienteRequest {
  stockId: number;
  requiredAmount: number;
  unit: string;
}

export interface RecetaRequest {
  name: string;
  baseQuantity: number;
  unitBase: string;
  flavorId: number | null;
  ingredients: RecetaIngredienteRequest[];
}

export interface InsumoCalculado {
  id: number;
  stockId: number | null;
  nombreInsumo: string;
  cantidadBase: number;
  cantidadCalculada: number;
  unidad: string;
}
