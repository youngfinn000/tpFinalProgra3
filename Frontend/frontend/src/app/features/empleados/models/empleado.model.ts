export type RolEmpleado = 'ADMIN' | 'EMPLOYEE';

export interface Empleado {
  id: number;
  name: string;
  email: string;
  rol: RolEmpleado;
  active: boolean;
}

export interface EmpleadoRequest {
  name: string;
  email: string;
  passwordHash: string;
  rol: RolEmpleado;
}

