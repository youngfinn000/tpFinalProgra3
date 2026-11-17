import { DecimalPipe } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { etiquetaUnidad } from '../../insumos/models/insumo';
import { InsumoCalculado, Receta } from '../models/receta.model';
import { RecetaService } from '../services/receta.service';

@Component({
  selector: 'app-receta-detalle',
  standalone: true,
  imports: [RouterLink, FormsModule, DecimalPipe],
  templateUrl: './receta-detalle.html',
  styleUrl: './receta-detalle.css',
})
export class RecetaDetalle implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private recetaService = inject(RecetaService);
  private authService = inject(AuthService);

  receta = signal<Receta | null>(null);
  kgDeseados = signal<number>(10);
  calculadosApi = signal<InsumoCalculado[] | null>(null);

  cargando = signal(true);
  calculando = signal(false);
  error = signal('');

  etiquetaUnidad = etiquetaUnidad;
  presetsKg = [5, 10, 20, 30, 50];

  esAdmin = computed(() => this.authService.tieneRol('ROLE_ADMIN'));

  factorEscala = computed(() => {
    const r = this.receta();
    const kg = Number(this.kgDeseados());
    if (!r || !r.cantidadBase || r.cantidadBase <= 0 || !kg || kg <= 0) {
      return 1;
    }
    return kg / r.cantidadBase;
  });

  esEscalaModificada = computed(() => {
    const r = this.receta();
    if (!r) return false;
    return Math.abs(Number(this.kgDeseados()) - r.cantidadBase) > 0.001;
  });

  ingredientesEscalados = computed(() => {
    const r = this.receta();
    if (!r) return [];

    const kg = Number(this.kgDeseados());
    const base = r.cantidadBase > 0 ? r.cantidadBase : 1;
    const factor = kg > 0 ? kg / base : 0;
    const desdeApi = this.calculadosApi();

    return r.ingredientes.map((ing) => {
      const matchApi = desdeApi?.find(
        (a) =>
          (ing.id && a.id === ing.id) ||
          (ing.stockId && a.stockId === ing.stockId) ||
          a.nombreInsumo === ing.nombreInsumo,
      );

      const cantidadCalculada =
        matchApi !== undefined
          ? matchApi.cantidadCalculada
          : ing.cantidadRequerida * factor;

      return {
        ...ing,
        cantidadCalculada: Math.round(cantidadCalculada * 1000) / 1000,
      };
    });
  });

  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = Number(idParam);

    if (!idParam || !Number.isInteger(id) || id <= 0) {
      this.error.set('El identificador de la receta no es válido.');
      this.cargando.set(false);
      return;
    }

    this.cargarReceta(id);
  }

  private cargarReceta(id: number) {
    this.cargando.set(true);
    this.recetaService.obtener(id).subscribe({
      next: (datos) => {
        this.receta.set(datos);
        this.kgDeseados.set(datos.cantidadBase || 10);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudo cargar la ficha técnica de la receta.');
        this.cargando.set(false);
      },
    });
  }

  actualizarKg(valor: number | string) {
    const num = Number(valor);
    this.kgDeseados.set(num > 0 ? num : 0);
    this.calculadosApi.set(null);

    const r = this.receta();
    if (r && num > 0) {
      this.calculando.set(true);
      this.recetaService.calcularInsumos(r.id, num).subscribe({
        next: (res) => {
          if (Number(this.kgDeseados()) === num) {
            this.calculadosApi.set(res);
          }
          this.calculando.set(false);
        },
        error: () => {
          this.calculando.set(false);
        },
      });
    }
  }

  seleccionarPreset(kg: number) {
    this.actualizarKg(kg);
  }

  restablecerBase() {
    const r = this.receta();
    if (r) {
      this.kgDeseados.set(r.cantidadBase);
      this.calculadosApi.set(null);
    }
  }

  eliminar() {
    const r = this.receta();
    if (!r) return;

    const confirmar = confirm(
      `¿Querés eliminar la receta "${r.nombre}" (Sabor: ${r.saborNombre})?`,
    );
    if (!confirmar) return;

    this.recetaService.eliminar(r.id).subscribe({
      next: () => this.router.navigate(['/recetas']),
      error: () => this.error.set('No se pudo eliminar la receta.'),
    });
  }
}
