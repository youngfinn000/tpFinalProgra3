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
        path: 'solicitar-presupuesto',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./features/presupuestos/solicitar-presupuesto/solicitar-presupuesto').then(
            (m) => m.SolicitarPresupuesto,
          ),
      },
      {
        path: 'mis-solicitudes',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./features/presupuestos/mis-solicitudes/mis-solicitudes').then(
            (m) => m.MisSolicitudes,
          ),
      },
      {
        path: 'enviar-anticipo',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./features/presupuestos/enviar-anticipo/enviar-anticipo').then(
            (m) => m.EnviarAnticipo,
          ),
      },
      {
        path: 'enviar-anticipo/:orderId',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./features/presupuestos/enviar-anticipo/enviar-anticipo').then(
            (m) => m.EnviarAnticipo,
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
      {
        path: 'productos',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./features/productos/producto-list/producto-list').then((m) => m.ProductoList),
      },
      {
        path: 'productos/nuevo',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/productos/producto-form/producto-form').then((m) => m.ProductoForm),
      },
      {
        path: 'productos/:id/editar',
        canActivate: [adminGuard],
        loadComponent: () =>
          import('./features/productos/producto-form/producto-form').then((m) => m.ProductoForm),
      },
      {
        path: 'productos/:id',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./features/productos/producto-detalle/producto-detalle').then(
            (m) => m.ProductoDetalle,
          ),
      },
    ],
  },
  { path: '**', redirectTo: '' },
];
