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

/**
 * Formatea una fecha ISO 'YYYY-MM-DD' a 'DD/MM/YYYY' sin desfase de zona horaria UTC-3.
 */
export function formatearFechaLocal(fechaIso: string | null | undefined): string {
  if (!fechaIso) return '-';
  const limpia = fechaIso.split('T')[0];
  const partes = limpia.split('-');
  if (partes.length !== 3) return fechaIso;
  return `${partes[2]}/${partes[1]}/${partes[0]}`;
}

/**
 * Devuelve la fecha local de hoy en formato 'YYYY-MM-DD'.
 */
export function obtenerFechaHoyLocal(): string {
  const hoy = new Date();
  const anio = hoy.getFullYear();
  const mes = String(hoy.getMonth() + 1).padStart(2, '0');
  const dia = String(hoy.getDate()).padStart(2, '0');
  return `${anio}-${mes}-${dia}`;
}
