import { Component, inject, signal } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import {
  Subject,
  catchError,
  combineLatest,
  debounceTime,
  of,
  startWith,
  switchMap,
} from 'rxjs';

import { Sabor } from '../models/sabor.model';
import { SaborService } from '../services/sabor-service';

@Component({
  selector: 'app-sabor-list',
  imports: [RouterLink, FormsModule],
  templateUrl: './sabor-list.html',
  styleUrl: './sabor-list.css',
})
export class SaborList {
  private svc = inject(SaborService);

  esAdmin = signal(true); // TEMPORAL: reemplazar por el rol real

  texto = signal('');
  modo = signal<'parcial' | 'exacta'>('parcial');
  estado = signal<'todos' | 'activos' | 'inactivos'>('todos');
  error = signal('');

  private recargar$ = new Subject<void>();

  sabores = toSignal(
    combineLatest([
      toObservable(this.texto).pipe(debounceTime(300)),
      toObservable(this.modo),
      toObservable(this.estado),
      this.recargar$.pipe(startWith(undefined)),
    ]).pipe(
      switchMap(([nombre, modo, estado]) => {
        this.error.set('');

        return this.svc.listar({ estado, nombre, modo }).pipe(
          catchError((err) => {
            this.error.set(err.error?.message ?? 'No se pudieron cargar los sabores');
            return of([] as Sabor[]);
          }),
        );
      }),
    ),
    { initialValue: [] as Sabor[] },
  );

  cambiarEstado(s: Sabor, activar: boolean) {
    const accion = activar ? 'reactivar' : 'desactivar';

    if (!confirm(`¿Seguro que querés ${accion} el sabor "${s.nombre}"?`)) {
      return;
    }

    const req$ = activar ? this.svc.reactivar(s.id) : this.svc.desactivar(s.id);

    req$.subscribe({
      next: () => this.recargar$.next(),
      error: (err) => this.error.set(err.error?.message ?? 'No se pudo cambiar el estado'),
    });
  }
}