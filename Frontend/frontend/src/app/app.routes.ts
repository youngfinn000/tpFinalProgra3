import { Routes } from '@angular/router';
import { MainLayout } from './layout/main-layout/main-layout';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login').then((m) => m.Login),
  },
  {
    path: '',
    component: MainLayout,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'empleados' },
      {
        path: 'empleados',
        loadComponent: () =>
          import('./features/empleados/empleados-list/empleados-list').then((m) => m.EmpleadosList),
      },
      {
        path: 'clientes-mayoristas',
        loadComponent: () =>
          import('./features/clientes-mayoristas/clientes-list/clientes-list').then(
            (m) => m.ClientesList,
          ),
      },
    ],
  },
  { path: '**', redirectTo: '' },
];