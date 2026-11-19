import { DecimalPipe } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Receta } from '../../recetas/models/receta.model';
import { RecetaService } from '../../recetas/services/receta.service';
import {
  LoteProduccion,
  formatearFechaLocal,
  obtenerFechaHoyLocal,
} from '../models/produccion.model';
import { ProduccionService } from '../services/produccion.service';

export type PeriodoRapido = 'todos' | 'hoy' | 'semana' | 'fecha';

function obtenerInicioSemanaLocal(): string {
  const hoy = new Date();
  const diaSemana = hoy.getDay(); // 0 (Dom) a 6 (Sáb)
  const diferenciaLunes = diaSemana === 0 ? -6 : 1 - diaSemana;
  const lunes = new Date(hoy.getFullYear(), hoy.getMonth(), hoy.getDate() + diferenciaLunes);
  const anio = lunes.getFullYear();
  const mes = String(lunes.getMonth() + 1).padStart(2, '0');
  const dia = String(lunes.getDate()).padStart(2, '0');
  return `${anio}-${mes}-${dia}`;
}

@Component({
  selector: 'app-produccion-historial',
  standalone: true,
  imports: [RouterLink, FormsModule, DecimalPipe],
  templateUrl: './produccion-historial.html',
  styleUrl: './produccion-historial.css',
})
export class ProduccionHistorial implements OnInit {
  private produccionService = inject(ProduccionService);
  private recetaService = inject(RecetaService);

  lotes = signal<LoteProduccion[]>([]);
  recetas = signal<Receta[]>([]);

  periodoActivo = signal<PeriodoRapido>('todos');
  fechaSeleccionada = signal<string>('');
  recetaSeleccionadaId = signal<number | null>(null);
  busquedaTexto = signal<string>('');

  cargando = signal(true);
  error = signal('');

  formatearFechaLocal = formatearFechaLocal;

  saboresDisponibles = computed(() => {
    const mapa = new Map<string, string>();
    for (const r of this.recetas()) {
      if (r.saborNombre) {
        mapa.set(r.saborNombre.toLowerCase(), r.saborNombre);
      }
    }
    for (const l of this.lotes()) {
      if (l.saborNombre) {
        mapa.set(l.saborNombre.toLowerCase(), l.saborNombre);
      }
    }
    return Array.from(mapa.values()).sort((a, b) => a.localeCompare(b));
  });

  saborSeleccionado = signal<string>('');

  lotesFiltrados = computed(() => {
    const periodo = this.periodoActivo();
    const hoy = obtenerFechaHoyLocal();
    const inicioSemana = obtenerInicioSemanaLocal();
    const fechaManual = this.fechaSeleccionada();
    const saborFiltro = this.saborSeleccionado().trim().toLowerCase();
    const recetaId = this.recetaSeleccionadaId();
    const texto = this.busquedaTexto().trim().toLowerCase();

    return this.lotes().filter((lote) => {
      const fechaLote = (lote.fechaProduccion || '').split('T')[0];

      // Filtro por período o fecha
      if (periodo === 'hoy' && fechaLote !== hoy) {
        return false;
      }
      if (periodo === 'semana' && (fechaLote < inicioSemana || fechaLote > hoy)) {
        return false;
      }
      if (periodo === 'fecha' && fechaManual && fechaLote !== fechaManual) {
        return false;
      }

      // Filtro por sabor
      if (saborFiltro && lote.saborNombre.toLowerCase() !== saborFiltro) {
        return false;
      }

      // Filtro por receta específica
      if (recetaId && lote.recetaId !== recetaId) {
        return false;
      }

      // Búsqueda libre
      if (texto) {
        const coincide =
          lote.saborNombre.toLowerCase().includes(texto) ||
          lote.recetaNombre.toLowerCase().includes(texto) ||
          lote.usuarioNombre.toLowerCase().includes(texto) ||
          String(lote.id).includes(texto);
        if (!coincide) return false;
      }

      return true;
    });
  });

  totalKilosFiltrados = computed(() => {
    const suma = this.lotesFiltrados().reduce(
      (acc, l) => acc + (Number(l.cantidadProducida) || 0),
      0,
    );
    return Math.round(suma * 100) / 100;
  });

  tieneFiltrosActivos = computed(
    () =>
      this.periodoActivo() !== 'todos' ||
      !!this.fechaSeleccionada() ||
      !!this.saborSeleccionado() ||
      !!this.recetaSeleccionadaId() ||
      !!this.busquedaTexto().trim(),
  );

  ngOnInit() {
    this.recetaService.listar().subscribe({
      next: (lista) => this.recetas.set(lista),
      error: () => {},
    });

    this.cargarHistorial();
  }

  cargarHistorial(fechaApi?: string) {
    this.cargando.set(true);
    this.produccionService
      .historial({
        fecha: fechaApi || undefined,
        recetaId: this.recetaSeleccionadaId() ?? undefined,
      })
      .subscribe({
        next: (datos) => {
          this.lotes.set(datos);
          this.error.set('');
          this.cargando.set(false);
        },
        error: () => {
          this.error.set('No se pudo cargar el historial de producción.');
          this.cargando.set(false);
        },
      });
  }

  seleccionarChipPeriodo(periodo: 'todos' | 'hoy' | 'semana') {
    this.periodoActivo.set(periodo);
    if (periodo === 'hoy') {
      const hoy = obtenerFechaHoyLocal();
      this.fechaSeleccionada.set(hoy);
      this.cargarHistorial(hoy);
    } else {
      this.fechaSeleccionada.set('');
      this.cargarHistorial();
    }
  }

  alCambiarFecha(fecha: string) {
    this.fechaSeleccionada.set(fecha);
    if (fecha) {
      this.periodoActivo.set('fecha');
      this.cargarHistorial(fecha);
    } else {
      this.periodoActivo.set('todos');
      this.cargarHistorial();
    }
  }

  alCambiarReceta(valor: number | null) {
    this.recetaSeleccionadaId.set(valor ? Number(valor) : null);
    const fechaApi =
      this.periodoActivo() === 'hoy' || this.periodoActivo() === 'fecha'
        ? this.fechaSeleccionada()
        : undefined;
    this.cargarHistorial(fechaApi);
  }

  limpiarFiltros() {
    this.periodoActivo.set('todos');
    this.fechaSeleccionada.set('');
    this.saborSeleccionado.set('');
    this.recetaSeleccionadaId.set(null);
    this.busquedaTexto.set('');
    this.cargarHistorial();
  }
}
