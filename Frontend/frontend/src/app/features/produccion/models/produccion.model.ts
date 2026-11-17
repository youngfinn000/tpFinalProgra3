export type EstadoLote = 'PLANNED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';

export interface LoteProduccion {
  id: number;
  recetaId: number;
  recetaNombre: string;
  saborId: number | null;
  saborNombre: string;
  usuarioId: number | null;
  usuarioNombre: string;
  cantidadProducida: number;
  rendimientoPct: number;
  estado: EstadoLote;
  fechaProduccion: string;
}

export interface CrearLoteRequest {
  recipeId: number;
  amountProduced: number;
  productionDate: string;
  status?: EstadoLote;
}

export interface FiltrosHistorialProduccion {
  fecha?: string;
  recetaId?: number | null;
  saborId?: number | null;
}
