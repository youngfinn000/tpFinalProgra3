import { Component, computed, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { Rol } from '../../../core/models/usuario.model';

interface MenuItem {
  label: string;
  ruta: string;
  roles: Rol[];
  icono: 'empleados' | 'clientes' | 'sabores' | 'productos' | 'insumos' | 'presupuesto';
}

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
  private auth = inject(AuthService);

  private items: MenuItem[] = [
    {
      label: 'Productos',
      ruta: '/productos',
      roles: ['ROLE_ADMIN', 'ROLE_USER'],
      icono: 'productos',
    },
    {
      label: 'Sabores',
      ruta: '/sabores',
      roles: ['ROLE_ADMIN', 'ROLE_USER', 'ROLE_WHOLESALE'],
      icono: 'sabores',
    },
    {
      label: 'Insumos',
      ruta: '/insumos',
      roles: ['ROLE_ADMIN'],
      icono: 'insumos',
    },
    {
      label: 'Empleados',
      ruta: '/empleados',
      roles: ['ROLE_ADMIN'],
      icono: 'empleados',
    },
    {
      label: 'Clientes mayoristas',
      ruta: '/clientes-mayoristas',
      roles: ['ROLE_ADMIN', 'ROLE_USER'],
      icono: 'clientes',
    },
    {
      label: 'Solicitar Presupuesto',
      ruta: '/solicitar-presupuesto',
      roles: ['ROLE_ADMIN', 'ROLE_USER', 'ROLE_WHOLESALE'],
      icono: 'presupuesto',
    },
    {
      label: 'Mis Solicitudes',
      ruta: '/mis-solicitudes',
      roles: ['ROLE_ADMIN', 'ROLE_USER', 'ROLE_WHOLESALE'],
      icono: 'presupuesto',
    },
  ];

  menu = computed(() =>
    this.items.filter((item) => this.auth.tieneRol(...item.roles)),
  );
}
