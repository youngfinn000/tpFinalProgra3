export type EstadoPresupuesto = 'PENDING' | 'CONFIRMED' | 'REJECTED' | 'CANCELLED';

export interface SolicitudPresupuestoRequest {
  quantity: number;
  requestDatetime: string; // ISO 8601 (yyyy-MM-ddTHH:mm:ss)
  advancePayment: boolean;
  customerId: number;
  flavorsId: number[];
  eventDate: string; // yyyy-MM-dd
}

export interface SolicitudPresupuestoResponse {
  id: number;
  quantity: number;
  requestDateTime: string;
  eventDate: string;
  budget: number;
  advancePayment: boolean;
  stateRequestEnum: EstadoPresupuesto;
  customerName: string;
  customerId: number;
  flavors: {
    id: number;
    name: string;
    activeInactive?: boolean;
  }[];
}

