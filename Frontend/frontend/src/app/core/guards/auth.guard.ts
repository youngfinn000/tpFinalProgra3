import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.tokenVigente()) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};

export const adminGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.tokenVigente()) {
    router.navigate(['/login']);
    return false;
  }

  if (authService.tieneRol('ROLE_ADMIN')) {
    return true;
  }

  // Si no es admin, redirigir a su ruta correspondiente
  router.navigateByUrl(authService.rutaInicial());
  return false;
};

export const guestGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.tokenVigente()) {
    router.navigateByUrl(authService.rutaInicial());
    return false;
  }

  return true;
};

