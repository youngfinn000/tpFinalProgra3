import { Routes } from '@angular/router';
import { MainLayout } from './layout/main-layout/main-layout';
import { adminGuard, authGuard, guestGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    canActivate: [guestGuard],
    loadComponent: () => import('./features/auth/login/login').then((m) => m.Login),
  },
  {
    path: '',
    component: MainLayout,
    canActivate: [authGuard],
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'empleados' },
      {
        path: 'empleados',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/empleados/empleados-list/empleados-list').then((m) => m.EmpleadosList),
      },
      {
        path: 'clientes-mayoristas',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./features/clientes-mayoristas/clientes-list/clientes-list').then(
            (m) => m.ClientesList,
          ),
      },
    ],
  },
  { path: '**', redirectTo: '' },
];