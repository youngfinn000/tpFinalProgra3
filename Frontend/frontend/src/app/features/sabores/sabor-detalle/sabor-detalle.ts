import { Component, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Sabor } from '../models/sabor.model';
import { SaborService } from '../services/sabor-service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-sabor-detalle',
  imports: [RouterLink],
  templateUrl: './sabor-detalle.html',
  styleUrl: './sabor-detalle.css',
})
export class SaborDetalle {
  private svc = inject(SaborService);
  private auth = inject(AuthService);
  private route = inject(ActivatedRoute);

  esAdmin = computed(() => this.auth.tieneRol('ROLE_ADMIN'));

  sabor = signal<Sabor | null>(null);
  error = signal('');
  errorAccion = signal('');

  constructor() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.svc.obtener(id).subscribe({
      next: (s) => this.sabor.set(s),
      error: () => this.error.set('Sabor no encontrado'),
    });
  }

  cambiarEstado(activar: boolean) {
    const s = this.sabor();
    if (!s) return;

    const accion = activar ? 'reactivar' : 'desactivar';
    if (!confirm(`¿Seguro que querés ${accion} el sabor "${s.nombre}"?`)) return;

    this.errorAccion.set('');
    const req$ = activar ? this.svc.reactivar(s.id) : this.svc.desactivar(s.id);

    req$.subscribe({
      next: () => this.sabor.set({ ...s, activo: activar }),
      error: (err) => this.errorAccion.set(err.error?.message ?? 'No se pudo cambiar el estado'),
    });
  }
}
