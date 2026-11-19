import { Component, inject, signal, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { etiquetaUnidad, Insumo } from '../models/insumo';
import { InsumoService } from '../services/insumo.service';

@Component({
  selector: 'app-insumo-lista',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './insumo-list.html',
  styleUrl: './insumo-list.css',
})
export class InsumoLista {
  private insumoService = inject(InsumoService);

  insumos = signal<Insumo[]>([]);
  error = signal('');

  etiquetaUnidad = etiquetaUnidad;

  // Cantidad total de insumos registrados.
  totalInsumos = computed(() => this.insumos().length);

  // Cantidad de insumos que necesitan reposición.
  insumosBajoStock = computed(
    () =>
      this.insumos().filter(
        (i) =>
          i.stockActual > 0 &&
          i.stockActual <= i.puntoReposicion,
      ).length,
  );

  // Cantidad de insumos que tienen stock en cero.
  insumosSinStock = computed(
    () =>
      this.insumos().filter((i) => i.stockActual <= 0).length,
  );

  constructor() {
    this.cargarInsumos();
  }

  cargarInsumos() {
    this.insumoService.listar().subscribe({
      next: (datos) => {
        this.insumos.set(datos);
        this.error.set('');
      },
      error: () => {
        this.error.set('No se pudieron cargar los insumos.');
      },
    });
  }

  // Calcula el estado automáticamente según el stock.
  estadoStock(i: Insumo): string {
    if (i.stockActual <= 0) {
      return 'Inactivo';
    }

    if (i.stockActual <= i.puntoReposicion) {
      return 'Bajo stock';
    }

    return 'Activo';
  }

  eliminar(insumo: Insumo) {
    const confirmar = confirm(
      `¿Querés dar de baja el insumo "${insumo.nombre}"?`,
    );

    if (!confirmar) {
      return;
    }

    this.insumoService.eliminar(insumo.id).subscribe({
      next: () => {
        this.cargarInsumos();
      },
      error: () => {
        this.error.set('No se pudo eliminar el insumo.');
      },
    });
  }
}