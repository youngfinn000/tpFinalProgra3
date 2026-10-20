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
      {
        path: 'sabores',
        loadComponent: () =>
          import('./features/sabores/sabor-list/sabor-list').then((m) => m.SaborList),
      },
      {
        path: 'sabores/nuevo',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/sabores/sabor-form/sabor-form').then((m) => m.SaborForm),
      },
      {
        path: 'sabores/:id/editar',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/sabores/sabor-form/sabor-form').then((m) => m.SaborForm),
      },
      {
        path: 'sabores/:id',
        loadComponent: () =>
          import('./features/sabores/sabor-detalle/sabor-detalle').then((m) => m.SaborDetalle),
      },
      {
        path: 'insumos',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/insumos/insumos-list/insumo-list').then((m) => m.InsumoLista),
      },
      {
        path: 'insumos/nuevo',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/insumos/insumo-form/insumo-form').then((m) => m.InsumoForm),
      },
      {
        path: 'insumos/:id/editar',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/insumos/insumo-form/insumo-form').then((m) => m.InsumoForm),
      },
    ],
  },
  { path: '**', redirectTo: '' },
];
