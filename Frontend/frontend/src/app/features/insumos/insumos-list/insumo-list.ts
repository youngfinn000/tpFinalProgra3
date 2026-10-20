import { Component, inject, signal } from '@angular/core';
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

  eliminar(insumo: Insumo) {
    const confirmar = confirm(
      `¿Querés eliminar el insumo "${insumo.nombre}"?`,
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