import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { Empleado, EmpleadoRequest, RolEmpleado } from '../models/empleado.model';
import { EmpleadoService } from '../services/empleado.service';

@Component({
  selector: 'app-empleados-list',
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './empleados-list.html',
  styleUrl: './empleados-list.css',
})
export class EmpleadosList implements OnInit {
  private empleadoService = inject(EmpleadoService);
  private fb = inject(FormBuilder);

  empleados = signal<Empleado[]>([]);
  cargando = signal<boolean>(false);
  guardando = signal<boolean>(false);

  filtroTexto = signal<string>('');
  filtroEstado = signal<'todos' | 'activos' | 'inactivos'>('todos');

  mensajeExito = signal<string>('');
  mensajeError = signal<string>('');

  // Modales
  modalFormularioAbierto = signal<boolean>(false);
  modoFormulario = signal<'crear' | 'editar'>('crear');
  empleadoSeleccionadoId: number | null = null;

  modalConfirmacionAbierto = signal<boolean>(false);
  empleadoAfectado = signal<Empleado | null>(null);
  accionConfirmacion = signal<'desactivar' | 'activar'>('desactivar');

  empleadoForm: FormGroup = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(50)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4)]],
    rol: ['EMPLOYEE' as RolEmpleado, [Validators.required]],
  });

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

  // Estadísticas computadas
  totalEmpleados = computed(() => this.empleados().length);
  totalActivos = computed(() => this.empleados().filter((e) => e.active).length);
  totalInactivos = computed(() => this.empleados().filter((e) => !e.active).length);
  totalAdmins = computed(() => this.empleados().filter((e) => e.rol === 'ADMIN').length);

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
    this.empleadoSeleccionadoId = null;
    this.empleadoForm.reset({
      name: '',
      email: '',
      password: '',
      rol: 'EMPLOYEE',
    });
    this.empleadoForm.get('password')?.setValidators([Validators.required, Validators.minLength(4)]);
    this.empleadoForm.get('password')?.updateValueAndValidity();
    this.modalFormularioAbierto.set(true);
    this.mensajeError.set('');
  }

  abrirModalEditar(emp: Empleado): void {
    this.modoFormulario.set('editar');
    this.empleadoSeleccionadoId = emp.id;
    this.empleadoForm.reset({
      name: emp.name,
      email: emp.email,
      password: 'dummyPassword123', // El backend requiere el campo @NotBlank para pasar @Valid en update
      rol: emp.rol,
    });
    // En edición, no exigimos contraseña al usuario
    this.empleadoForm.get('password')?.clearValidators();
    this.empleadoForm.get('password')?.updateValueAndValidity();
    this.modalFormularioAbierto.set(true);
    this.mensajeError.set('');
  }

  cerrarModalFormulario(): void {
    this.modalFormularioAbierto.set(false);
  }

  guardarEmpleado(): void {
    if (this.empleadoForm.invalid) {
      this.empleadoForm.markAllAsTouched();
      return;
    }

    this.guardando.set(true);
    this.mensajeError.set('');

    const formValue = this.empleadoForm.value;
    const request: EmpleadoRequest = {
      name: formValue.name.trim(),
      email: formValue.email.trim(),
      passwordHash: formValue.password || 'password123',
      rol: formValue.rol,
    };

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
          this.mensajeError.set(
            err.error?.message || 'Error al crear el empleado. Verificá los datos ingresados.'
          );
        },
      });
    } else {
      if (!this.empleadoSeleccionadoId) return;
      this.empleadoService.actualizar(this.empleadoSeleccionadoId, request).subscribe({
        next: (actualizado) => {
          this.guardando.set(false);
          this.modalFormularioAbierto.set(false);
          this.mostrarExito(`Empleado "${actualizado.name}" actualizado con éxito.`);
          this.cargarEmpleados();
        },
        error: (err) => {
          this.guardando.set(false);
          console.error('Error al actualizar empleado:', err);
          this.mensajeError.set(
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
