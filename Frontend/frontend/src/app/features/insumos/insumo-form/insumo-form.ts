
import { Component, inject, signal } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { InsumoService } from '../services/insumo.service';
import {
  ESTADO_ACTIVO,
  Insumo,
  InsumoRequest,
  UNIDADES,
} from '../models/insumo';

@Component({
  selector: 'app-insumo-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './insumo-form.html',
  styleUrl: './insumo-form.css',
})
export class InsumoForm {
  private fb = inject(FormBuilder).nonNullable;
  private service = inject(InsumoService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  unidades = UNIDADES;
  guardando = signal(false);
  error = signal('');
  esEdicion = signal(false);

  private id: number | null = null;

  form = this.fb.group({
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
    unidad: ['KG', Validators.required],
    stockActual: [0, [Validators.required, Validators.min(0)]],
    puntoReposicion: [0, [Validators.required, Validators.min(0)]],
    estado: [ESTADO_ACTIVO, Validators.required],
  });

  constructor() {
    const id = this.route.snapshot.paramMap.get('id');

    if (id !== null) {
      this.id = Number(id);

      if (!Number.isInteger(this.id) || this.id <= 0) {
        this.error.set('El identificador del insumo no es válido.');
        return;
      }

      this.esEdicion.set(true);
      this.cargarInsumo();
    }
  }

  cargarInsumo() {
    if (this.id === null) return;

    this.service.obtener(this.id).subscribe({
      next: (insumo: Insumo) => {
        this.form.patchValue({
          nombre: insumo.nombre,
          unidad: insumo.unidad,
          stockActual: insumo.stockActual,
          puntoReposicion: insumo.puntoReposicion,
          estado: insumo.estado,
        });
      },
      error: () => {
        this.error.set('No se pudo cargar el insumo.');
      },
    });
  }

  guardar() {
  if (this.form.invalid || this.guardando()) {
    this.form.markAllAsTouched();
    return;
  }

  const datos: InsumoRequest = this.form.getRawValue();
  this.guardando.set(true);
  this.error.set('');

  const peticion =
    this.id === null
      ? this.service.crear(datos)
      : this.service.modificar(this.id, datos);

  peticion.subscribe({
    next: () => {
      this.guardando.set(false);
      this.router.navigate(['/insumos']);
    },
    error: () => {
      this.guardando.set(false);
      this.error.set('No se pudo guardar el insumo.');
    },
  });
}

  cancelar() {
    this.router.navigate(['/insumos']);
  }
}
