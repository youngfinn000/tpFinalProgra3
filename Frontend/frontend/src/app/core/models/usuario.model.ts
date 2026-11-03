export type Rol = 'ROLE_ADMIN' | 'ROLE_WHOLESALE' | 'ROLE_USER';

export interface Usuario {
  id?: number;
  username: string;
  rol: Rol;
}