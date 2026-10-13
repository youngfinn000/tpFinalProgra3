import { Component, computed, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/services/auth';
import { Rol } from '../../../core/models/usuario.model';

interface MenuItem {
  label: string;
  ruta: string;
  roles: Rol[];
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
    { label: 'Empleados', ruta: '/empleados', roles: ['ROLE_ADMIN'] },
    { label: 'Clientes mayoristas', ruta: '/clientes-mayoristas', roles: ['ROLE_ADMIN', 'ROLE_USER'] },
  ];

  menu = computed(() => this.items.filter((i) => this.auth.tieneRol(...i.roles)));
}