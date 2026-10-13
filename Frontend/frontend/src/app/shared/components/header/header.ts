import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth';

@Component({
  selector: 'app-header',
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  private auth = inject(AuthService);
  private router = inject(Router);

  usuario = this.auth.usuario;

  cerrarSesion() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}