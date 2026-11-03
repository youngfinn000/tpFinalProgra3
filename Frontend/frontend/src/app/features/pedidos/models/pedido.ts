export type OrderStatus = 'PENDING' | 'IN_PREPARATION' | 'READY' | 'DELIVERED' | 'CANCELLED';
export type SalesChannel = 'LOCAL' | 'RAPPI' | 'PEDIDOS_YA' | 'WHOLESALE';
export type PaymentMethod = 'CASH' | 'TRANSFER' | 'DEBIT' | 'CREDIT';

export interface OrderDetail {
  id: number;
  productId: number;
  productName: string;
  flavorNames: string[];
  quantity: number;
  unitPrice: number;
  subtotal: number;          
}

export interface Order {
  id: number;
  createdAt: string;
  status: OrderStatus;
  channel: SalesChannel;
  paymentMethod: PaymentMethod;
  total: number;
  userName: string;
  details: OrderDetail[];
}

export interface CreateOrderRequest {
  channel: SalesChannel;
  paymentMethod: PaymentMethod;
  items: { productId: number; flavorIds: number[]; quantity: number }[];
}

export interface Product { id: number; name: string; price: number; maxFlavors: number; stock: number; }
export interface Flavor { id: number; name: string; }

// ---- Labels y helpers ----
export const STATUS_LABEL: Record<OrderStatus, string> = {
  PENDING: 'Pendiente',
  IN_PREPARATION: 'En preparación',
  READY: 'Listo',
  DELIVERED: 'Entregado',
  CANCELLED: 'Cancelado',
};

export const NEXT_STATUS: Partial<Record<OrderStatus, OrderStatus>> = {
  PENDING: 'IN_PREPARATION',
  IN_PREPARATION: 'READY',
  READY: 'DELIVERED',
};

export const NEXT_STATUS_LABEL: Partial<Record<OrderStatus, string>> = {
  PENDING: 'Pasar a En preparación',
  IN_PREPARATION: 'Marcar como Listo',
  READY: 'Marcar como Entregado',
};

export const CHANNEL_LABEL: Record<SalesChannel, string> = {
  LOCAL: 'Local',
  RAPPI: 'Rappi',
  PEDIDOS_YA: 'PedidosYa',
  WHOLESALE: 'Mayorista',
};

export const PAYMENT_LABEL: Record<PaymentMethod, string> = {
  CASH: 'Efectivo',
  TRANSFER: 'Transferencia',
  DEBIT: 'Débito',
  CREDIT: 'Crédito',
};

export const STATUSES = Object.keys(STATUS_LABEL) as OrderStatus[];
export const CHANNELS = Object.keys(CHANNEL_LABEL) as SalesChannel[];
export const PAYMENT_METHODS = Object.keys(PAYMENT_LABEL) as PaymentMethod[];

export const canCancel = (s: OrderStatus) => s === 'PENDING' || s === 'IN_PREPARATION';
export const canAdvance = (s: OrderStatus) => s in NEXT_STATUS;