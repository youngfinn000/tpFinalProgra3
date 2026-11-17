import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { Receta } from '../../recetas/models/receta.model';
import { RecetaService } from '../../recetas/services/receta.service';
import { LoteProduccion } from '../models/produccion.model';
import { ProduccionService } from '../services/produccion.service';

function obtenerFechaHoyLocal(): string {
  const hoy = new Date();
  const anio = hoy.getFullYear();
  const mes = String(hoy.getMonth() + 1).padStart(2, '0');
  const dia = String(hoy.getDate()).padStart(2, '0');
  return `${anio}-${mes}-${dia}`;
}

@Component({
  selector: 'app-lote-form',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, DecimalPipe, DatePipe],
  templateUrl: './lote-form.html',
  styleUrl: './lote-form.css',
})
export class LoteForm implements OnInit {
  private fb = inject(FormBuilder);
  private recetaService = inject(RecetaService);
  private produccionService = inject(ProduccionService);
  private route = inject(ActivatedRoute);

  recetas = signal<Receta[]>([]);
  cargandoRecetas = signal(true);
  guardando = signal(false);
  error = signal('');
  loteCreado = signal<LoteProduccion | null>(null);
  copiado = signal(false);

  presetsKg = [5, 10, 15, 20, 30, 50];

  form = this.fb.group({
    recipeId: [null as number | null, Validators.required],
    amountProduced: [null as number | null, [Validators.required, Validators.min(0.1)]],
    productionDate: [obtenerFechaHoyLocal(), Validators.required],
  });

  private formValues = toSignal(this.form.valueChanges, {
    initialValue: this.form.value,
  });

  recetaSeleccionada = computed(() => {
    const id = Number(this.formValues().recipeId);
    if (!id) return null;
    return this.recetas().find((r) => r.id === id) ?? null;
  });

  rendimientoEstimado = computed(() => {
    const r = this.recetaSeleccionada();
    const kg = Number(this.formValues().amountProduced);
    if (!r || !r.cantidadBase || r.cantidadBase <= 0 || !kg || kg <= 0) {
      return null;
    }
    return Math.round((kg / r.cantidadBase) * 1000) / 10;
  });

  ngOnInit() {
    const recetaIdParam = this.route.snapshot.queryParamMap.get('recetaId');
    const preselectId = recetaIdParam ? Number(recetaIdParam) : null;

    this.recetaService.listar().subscribe({
      next: (lista) => {
        this.recetas.set(lista);
        this.cargandoRecetas.set(false);

        if (preselectId && lista.some((r) => r.id === preselectId)) {
          this.form.patchValue({ recipeId: preselectId });
          this.alCambiarReceta();
        }
      },
      error: () => {
        this.error.set('No se pudieron cargar las recetas disponibles.');
        this.cargandoRecetas.set(false);
      },
    });
  }

  alCambiarReceta() {
    const id = Number(this.form.value.recipeId);
    const receta = this.recetas().find((r) => r.id === id);
    if (receta && !this.form.value.amountProduced) {
      this.form.patchValue({ amountProduced: receta.cantidadBase });
    }
  }

  elegirKgRapido(kg: number) {
    this.form.patchValue({ amountProduced: kg });
    this.form.controls.amountProduced.markAsTouched();
  }

  registrarLote() {
    if (this.guardando()) return;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.error.set('Seleccioná la receta, los kilos producidos y la fecha.');
      return;
    }

    const val = this.form.getRawValue();
    this.guardando.set(true);
    this.error.set('');

    this.produccionService
      .crear({
        recipeId: Number(val.recipeId),
        amountProduced: Number(val.amountProduced),
        productionDate: val.productionDate || obtenerFechaHoyLocal(),
        status: 'COMPLETED',
      })
      .subscribe({
        next: (creado) => {
          this.guardando.set(false);
          this.loteCreado.set(creado);
          this.copiado.set(false);
        },
        error: (err) => {
          this.guardando.set(false);
          this.error.set(
            err?.error?.message ?? 'No se pudo registrar el lote de producción.',
          );
        },
      });
  }

  copiarRotulo() {
    const lote = this.loteCreado();
    if (!lote) return;

    const texto = `LOTE #${lote.id} | Sabor: ${lote.saborNombre} (${lote.recetaNombre}) | Cantidad: ${lote.cantidadProducida} Kg | Fecha: ${lote.fechaProduccion}`;
    navigator.clipboard?.writeText(texto);
    this.copiado.set(true);
  }

  nuevoRegistro() {
    this.loteCreado.set(null);
    this.copiado.set(false);
    this.error.set('');
    this.form.reset({
      recipeId: null,
      amountProduced: null,
      productionDate: obtenerFechaHoyLocal(),
    });
  }
}
