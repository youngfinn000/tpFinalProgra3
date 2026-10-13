
import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthRequest, AuthResponse } from '../models/auth.model';
import { Rol, Usuario } from '../models/usuario.model';

const ROLES_VALIDOS: Rol[] = [
  'ROLE_ADMIN',
  'ROLE_USER',
  'ROLE_WHOLESALE'
];

function decodificarJwt(token: string): Record<string, any> | null {
  try {
    const parte = token.split('.')[1];
    if (!parte) return null;

    const base64 = parte
      .replace(/-/g, '+')
      .replace(/_/g, '/');

    const json = decodeURIComponent(
      atob(base64)
        .split('')
        .map(
          (c) =>
            '%' + c.charCodeAt(0).toString(16).padStart(2, '0')
        )
        .join('')
    );

    return JSON.parse(json);
  } catch {
    return null;
  }
}

function extraerRol(claims: Record<string, any>): Rol | null {
  const raw =
    claims['role'] ??
    claims['roles'] ??
    claims['authorities'];

  const lista = Array.isArray(raw) ? raw : [raw];

  for (const item of lista) {
    const valor: unknown =
      typeof item === 'string' ? item : item?.authority;

    if (typeof valor !== 'string') continue;

    const rol = (
      valor.startsWith('ROLE_') ? valor : `ROLE_${valor}`
    ) as Rol;

    if (ROLES_VALIDOS.includes(rol)) return rol;
  }

  return null;
}

function usuarioDesdeToken(token: string): Usuario | null {
  const claims = decodificarJwt(token);

  if (!claims) return null;

  const rol = extraerRol(claims);
  if (!rol) return null;

  const rawId = claims['id'] ?? claims['userId'];
  const username = claims['sub'] ?? claims['username'];

  if (typeof username !== 'string' || !username.trim()) {
    console.warn('El JWT no contiene el username esperado:', claims);
    return null;
  }

  const id =
    rawId !== undefined && rawId !== null && !isNaN(Number(rawId))
      ? Number(rawId)
      : undefined;

  return {
    id,
    username,
    rol,
  };
}

function tokenVencido(token: string): boolean {
  const exp = decodificarJwt(token)?.['exp'];

  return typeof exp === 'number' && exp * 1000 <= Date.now();
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private readonly TOKEN_KEY = 'token';

  private _usuario = signal<Usuario | null>(this.cargarSesion());

  readonly usuario = this._usuario.asReadonly();
  readonly estaLogueado = computed(() => !!this._usuario());
  readonly rol = computed(() => this._usuario()?.rol ?? null);

  login(credenciales: AuthRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(
        `${environment.apiUrl}/auth/login`,
        credenciales
      )
      .pipe(
        tap((res) => this.guardarSesion(res.token))
      );
  }

  logout(): void {
    this.limpiarSesion();
    this.router.navigate(['/login']);
  }

  sesionExpirada(): void {
    this.limpiarSesion();
    this.router.navigate(['/login'], {
      queryParams: { expirada: true }
    });
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  tokenVigente(): boolean {
    const token = this.getToken();
    return !!token && !tokenVencido(token);
  }

  tieneRol(...roles: Rol[]): boolean {
    const rol = this.rol();
    return !!rol && roles.includes(rol);
  }

  rutaInicial(): string {
    return this.rol() === 'ROLE_ADMIN'
      ? '/empleados'
      : '/clientes-mayoristas';
  }

  private guardarSesion(token: string): void {
    const usuario = usuarioDesdeToken(token);

    if (!usuario) {
      throw new Error(
        'El token no contiene los datos de usuario esperados'
      );
    }

    localStorage.setItem(this.TOKEN_KEY, token);
    this._usuario.set(usuario);
  }

  private limpiarSesion(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this._usuario.set(null);
  }

  private cargarSesion(): Usuario | null {
    const token = localStorage.getItem(this.TOKEN_KEY);

    if (!token || tokenVencido(token)) {
      localStorage.removeItem(this.TOKEN_KEY);
      return null;
    }

    return usuarioDesdeToken(token);
  }
}
