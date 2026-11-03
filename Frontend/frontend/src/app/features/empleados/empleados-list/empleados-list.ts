import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Empleado, EmpleadoRequest } from '../models/empleado.model';
import { EmpleadoService } from '../services/empleado.service';
import { EmpleadoFormModalComponent } from '../components/empleado-form-modal/empleado-form-modal.component';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-empleados-list',
  imports: [FormsModule, EmpleadoFormModalComponent, ConfirmDialogComponent],
  templateUrl: './empleados-list.html',
  styleUrl: './empleados-list.css',
})
export class EmpleadosList implements OnInit {
  private empleadoService = inject(EmpleadoService);

  empleados = signal<Empleado[]>([]);
  cargando = signal<boolean>(false);
  guardando = signal<boolean>(false);

  filtroTexto = signal<string>('');
  filtroEstado = signal<'todos' | 'activos' | 'inactivos'>('todos');

  mensajeExito = signal<string>('');
  mensajeError = signal<string>('');
  mensajeErrorModal = signal<string>('');

  // Modales desacoplados
  modalFormularioAbierto = signal<boolean>(false);
  modoFormulario = signal<'crear' | 'editar'>('crear');
  empleadoSeleccionado = signal<Empleado | null>(null);

  modalConfirmacionAbierto = signal<boolean>(false);
  empleadoAfectado = signal<Empleado | null>(null);
  accionConfirmacion = signal<'desactivar' | 'activar'>('desactivar');

  // Lista filtrada computada
  empleadosFiltrados = computed(() => {
    const texto = this.filtroTexto().toLowerCase().trim();
    const estado = this.filtroEstado();

    return this.empleados().filter((emp) => {
      const coincideTexto =
        !texto ||
        emp.name.toLowerCase().includes(texto) ||
        emp.email.toLowerCase().includes(texto) ||
        emp.id.toString().includes(texto) ||
        emp.rol.toLowerCase().includes(texto);

      const coincideEstado =
        estado === 'todos' ||
        (estado === 'activos' && emp.active) ||
        (estado === 'inactivos' && !emp.active);

      return coincideTexto && coincideEstado;
    });
  });



  tituloConfirmacion = computed(() =>
    this.accionConfirmacion() === 'desactivar' ? '¿Desactivar Empleado?' : '¿Reactivar Empleado?'
  );

  mensajeConfirmacion = computed(() => {
    const nombre = this.empleadoAfectado()?.name ?? '';
    return this.accionConfirmacion() === 'desactivar'
      ? `¿Estás seguro de que deseás desactivar al empleado "${nombre}"? No podrá operar en el sistema hasta que sea reactivado.`
      : `¿Estás seguro de que deseás reactivar al empleado "${nombre}"? El empleado recuperará el acceso a sus funcionalidades correspondientes.`;
  });

  ngOnInit(): void {
    this.cargarEmpleados();
  }

  cargarEmpleados(): void {
    this.cargando.set(true);
    this.mensajeError.set('');

    this.empleadoService.listar().subscribe({
      next: (lista) => {
        this.empleados.set(lista);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        console.error('Error al cargar empleados:', err);
        this.mensajeError.set(
          err.status === 0
            ? 'No se pudo conectar con el servidor. Verificá que el backend esté encendido.'
            : 'Error al obtener la lista de empleados. Intentá nuevamente.'
        );
      },
    });
  }

  abrirModalCrear(): void {
    this.modoFormulario.set('crear');
    this.empleadoSeleccionado.set(null);
    this.mensajeErrorModal.set('');
    this.modalFormularioAbierto.set(true);
  }

  abrirModalEditar(emp: Empleado): void {
    this.modoFormulario.set('editar');
    this.empleadoSeleccionado.set(emp);
    this.mensajeErrorModal.set('');
    this.modalFormularioAbierto.set(true);
  }

  cerrarModalFormulario(): void {
    this.modalFormularioAbierto.set(false);
    this.empleadoSeleccionado.set(null);
  }

  guardarEmpleado(request: EmpleadoRequest): void {
    this.guardando.set(true);
    this.mensajeErrorModal.set('');

    if (this.modoFormulario() === 'crear') {
      this.empleadoService.crear(request).subscribe({
        next: (nuevo) => {
          this.guardando.set(false);
          this.modalFormularioAbierto.set(false);
          this.mostrarExito(`Empleado "${nuevo.name}" creado con éxito.`);
          this.cargarEmpleados();
        },
        error: (err) => {
          this.guardando.set(false);
          console.error('Error al crear empleado:', err);
          this.mensajeErrorModal.set(
            err.error?.message || 'Error al crear el empleado. Verificá los datos ingresados.'
          );
        },
      });
    } else {
      const emp = this.empleadoSeleccionado();
      if (!emp) return;
      this.empleadoService.actualizar(emp.id, request).subscribe({
        next: (actualizado) => {
          this.guardando.set(false);
          this.modalFormularioAbierto.set(false);
          this.mostrarExito(`Empleado "${actualizado.name}" actualizado con éxito.`);
          this.cargarEmpleados();
        },
        error: (err) => {
          this.guardando.set(false);
          console.error('Error al actualizar empleado:', err);
          this.mensajeErrorModal.set(
            err.error?.message || 'Error al actualizar el empleado.'
          );
        },
      });
    }
  }

  abrirConfirmacionEstado(emp: Empleado, accion: 'desactivar' | 'activar'): void {
    this.empleadoAfectado.set(emp);
    this.accionConfirmacion.set(accion);
    this.modalConfirmacionAbierto.set(true);
  }

  cerrarModalConfirmacion(): void {
    this.modalConfirmacionAbierto.set(false);
    this.empleadoAfectado.set(null);
  }

  confirmarCambioEstado(): void {
    const emp = this.empleadoAfectado();
    if (!emp) return;

    this.guardando.set(true);
    if (this.accionConfirmacion() === 'desactivar') {
      this.empleadoService.eliminar(emp.id).subscribe({
        next: () => {
          this.guardando.set(false);
          this.cerrarModalConfirmacion();
          this.mostrarExito(`Empleado "${emp.name}" desactivado correctamente.`);
          this.cargarEmpleados();
        },
        error: (err) => {
          this.guardando.set(false);
          console.error('Error al desactivar empleado:', err);
          this.mensajeError.set(err.error?.message || 'Error al desactivar el empleado.');
        },
      });
    } else {
      this.empleadoService.activar(emp.id).subscribe({
        next: () => {
          this.guardando.set(false);
          this.cerrarModalConfirmacion();
          this.mostrarExito(`Empleado "${emp.name}" reactivado correctamente.`);
          this.cargarEmpleados();
        },
        error: (err) => {
          this.guardando.set(false);
          console.error('Error al reactivar empleado:', err);
          this.mensajeError.set(err.error?.message || 'Error al reactivar el empleado.');
        },
      });
    }
  }

  private mostrarExito(mensaje: string): void {
    this.mensajeExito.set(mensaje);
    setTimeout(() => {
      if (this.mensajeExito() === mensaje) {
        this.mensajeExito.set('');
      }
    }, 4000);
  }

  cerrarAlertaExito(): void {
    this.mensajeExito.set('');
  }

  cerrarAlertaError(): void {
    this.mensajeError.set('');
  }
}
