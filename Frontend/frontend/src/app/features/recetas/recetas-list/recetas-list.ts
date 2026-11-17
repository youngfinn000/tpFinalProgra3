import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { etiquetaUnidad } from '../../insumos/models/insumo';
import { Receta } from '../models/receta.model';
import { RecetaService } from '../services/receta.service';

@Component({
  selector: 'app-recetas-list',
  standalone: true,
  imports: [RouterLink, FormsModule],
  templateUrl: './recetas-list.html',
  styleUrl: './recetas-list.css',
})
export class RecetasList {
  private recetaService = inject(RecetaService);
  private authService = inject(AuthService);

  recetas = signal<Receta[]>([]);
  busqueda = signal('');
  cargando = signal(true);
  error = signal('');
  mensajeExito = signal('');

  etiquetaUnidad = etiquetaUnidad;

  esAdmin = computed(() => this.authService.tieneRol('ROLE_ADMIN'));

  recetasFiltradas = computed(() => {
    const q = this.busqueda().trim().toLowerCase();
    if (!q) return this.recetas();
    return this.recetas().filter(
      (r) =>
        r.nombre.toLowerCase().includes(q) ||
        r.saborNombre.toLowerCase().includes(q) ||
        r.ingredientes.some((i) => i.nombreInsumo.toLowerCase().includes(q)),
    );
  });

  totalRecetas = computed(() => this.recetas().length);

  saboresConReceta = computed(() => {
    const ids = new Set(
      this.recetas()
        .map((r) => r.saborId ?? r.saborNombre)
        .filter(Boolean),
    );
    return ids.size;
  });

  promedioBatchKg = computed(() => {
    const lista = this.recetas();
    if (!lista.length) return 0;
    const suma = lista.reduce((acc, r) => acc + (Number(r.cantidadBase) || 0), 0);
    return Math.round((suma / lista.length) * 10) / 10;
  });

  constructor() {
    this.cargarRecetas();
  }

  cargarRecetas() {
    this.cargando.set(true);
    this.recetaService.listar().subscribe({
      next: (datos) => {
        this.recetas.set(datos);
        this.error.set('');
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar las recetas.');
        this.cargando.set(false);
      },
    });
  }

  eliminar(receta: Receta) {
    const confirmar = confirm(
      `¿Querés eliminar la receta "${receta.nombre}" (Sabor: ${receta.saborNombre})?`,
    );

    if (!confirmar) return;

    this.recetaService.eliminar(receta.id).subscribe({
      next: () => {
        this.mensajeExito.set(`Se eliminó la receta "${receta.nombre}".`);
        this.cargarRecetas();
      },
      error: () => {
        this.error.set('No se pudo eliminar la receta.');
      },
    });
  }
}
