import { Injectable, computed, signal } from '@angular/core';
import { Rol, Usuario } from '../models/usuario.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'token';

  // TODO (HU01): quitar el mock cuando el login esté conectado al backend
  private _usuario = signal<Usuario | null>({
    id: 1,
    username: 'admin.mock',
    rol: 'ROLE_ADMIN',
  });

  readonly usuario = this._usuario.asReadonly();
  readonly estaLogueado = computed(() => !!this._usuario());
  readonly rol = computed(() => this._usuario()?.rol ?? null);

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  setSesion(token: string, usuario: Usuario): void {
    localStorage.setItem(this.TOKEN_KEY, token);
    this._usuario.set(usuario);
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this._usuario.set(null);
  }

  tieneRol(...roles: Rol[]): boolean {
    const r = this.rol();
    return !!r && roles.includes(r);
  }
}