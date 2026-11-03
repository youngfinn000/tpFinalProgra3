export interface ClienteMayorista {
  id: number;
  companyName: string;
  email: string;
  contactName: string;
  cuit?: string;
  active: boolean;
}

export interface ClienteMayoristaRequest {
  companyName: string;
  email: string;
  contactName: string;
  cuit?: string;
  password?: string;
}

