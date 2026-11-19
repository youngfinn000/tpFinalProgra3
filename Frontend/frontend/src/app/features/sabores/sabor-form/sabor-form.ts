import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { form, FormField, maxLength, minLength, required } from '@angular/forms/signals';
import { SaborService } from '../services/sabor-service';

@Component({
  selector: 'app-sabor-form',
  imports: [FormField],
  templateUrl: './sabor-form.html',
  styleUrl: './sabor-form.css',
})
export class SaborForm {
  private svc = inject(SaborService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  id = signal<number | null>(null);
  error = signal('');
  model = signal({ nombre: '' });

  f = form(this.model, (p) => {
    required(p.nombre);
    minLength(p.nombre, 2);
    maxLength(p.nombre, 60);
  });

  constructor() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.id.set(Number(id));
      this.svc.obtener(Number(id)).subscribe((s) => this.model.set({ nombre: s.nombre }));
    }
  }

  guardar(e: Event) {
    e.preventDefault();
    if (this.f().invalid()) return;

    const body = { nombre: this.model().nombre.trim() };
    const id = this.id();
    const req$ = id ? this.svc.modificar(id, body) : this.svc.crear(body);

    req$.subscribe({
      next: () => this.router.navigate(['/sabores']),
      error: (err) => this.error.set(err.error?.message ?? 'No se pudo guardar el sabor'),
    });
  }

  cancelar() {
    this.router.navigate(['/sabores']);
  }
}