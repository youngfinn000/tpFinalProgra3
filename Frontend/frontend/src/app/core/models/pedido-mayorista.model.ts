export interface PedidoMayoristaResponse {
  id: number;
  advancePayment: boolean;
  advancePaymentAmount: number | null;
  deliveryDate: string | null;
  discount: number | null;
  requestBudgetId: number;
  wholesaleCustomerId: number;
  wholesaleCustomerName: string;
  budget?: number;
  quantity?: number;
  eventDate?: string;
  stateRequestEnum?: string;
}

export type MetodoPago = 'TRANSFERENCIA' | 'TARJETA' | 'ECHEQ';

export interface RegistrarAnticipoRequest {
  orderId: number;
  customerId: number;
  amount: number;
  metodoPago: MetodoPago;
  comprobante: string;
  titular: string;
  fechaOperacion: string;
  observaciones?: string;
}

