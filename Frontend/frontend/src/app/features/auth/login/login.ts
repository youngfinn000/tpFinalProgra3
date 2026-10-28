import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  loginForm: FormGroup = this.fb.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]],
  });

  cargando = false;
  mostrarPassword = false;
  mensajeError = '';
  sesionExpiradaAviso = false;

  ngOnInit(): void {
    if (this.route.snapshot.queryParamMap.get('expirada') === 'true') {
      this.sesionExpiradaAviso = true;
    }
  }

  toggleMostrarPassword(): void {
    this.mostrarPassword = !this.mostrarPassword;
  }

  llenarDemo(usuario: 'admin' | 'empleado' | 'mayorista'): void {
    if (usuario === 'admin') {
      this.loginForm.setValue({
        username: 'admin',
        password: 'password123',
      });
    } else if (usuario === 'empleado') {
      this.loginForm.setValue({
        username: 'empleado',
        password: 'password123',
      });
    } else {
      this.loginForm.setValue({
        username: 'mayorista@stretto.com',
        password: 'password123',
      });
    }
    this.mensajeError = '';
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.cargando = true;
    this.mensajeError = '';
    this.sesionExpiradaAviso = false;

    const credenciales = {
      username: (this.loginForm.value.username || '').trim(),
      password: (this.loginForm.value.password || '').trim(),
    };

    this.authService.login(credenciales).subscribe({
      next: () => {
        this.cargando = false;
        const destino = this.authService.rutaInicial();
        this.router.navigateByUrl(destino);
      },
      error: (err) => {
        this.cargando = false;
        console.error('Error al iniciar sesión:', err);

        if (err.status === 401 || err.status === 403) {
          this.mensajeError = 'Credenciales inválidas. Verificá tu usuario y contraseña.';
        } else if (err.status === 0) {
          this.mensajeError = 'No se pudo conectar con el servidor backend en http://localhost:8080. Verificá que esté iniciado.';
        } else if (err.error?.message) {
          this.mensajeError = err.error.message;
        } else {
          this.mensajeError = 'Ocurrió un error inesperado al intentar iniciar sesión.';
        }
      },
    });
  }
}
