import { Component, OnInit, computed, inject, signal, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { ClienteMayoristaService } from '../services/cliente-mayorista.service';
import { ClienteMayorista, ClienteMayoristaRequest } from '../models/cliente-mayorista.model';

@Component({
  selector: 'app-clientes-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterLink],
  templateUrl: './clientes-list.html',
  styleUrl: './clientes-list.css'
})
export class ClientesList implements OnInit {
  private readonly clienteService = inject(ClienteMayoristaService);
  private readonly fb = inject(FormBuilder);
  private readonly cdr = inject(ChangeDetectorRef);

  clientes = signal<ClienteMayorista[]>([]);
  presupuestosCliente = signal<any[]>([]);

  cargando = signal<boolean>(false);
  guardando = signal<boolean>(false);
  terminoBusqueda = signal<string>('');
  
  modalFormularioAbierto = signal<boolean>(false);
  modalEliminarAbierto = signal<boolean>(false);
  modalPresupuestosAbierto = signal<boolean>(false);
  
  esEdicion = signal<boolean>(false);
  clienteSeleccionadoId = signal<number | null>(null);
  clienteAEliminar = signal<ClienteMayorista | null>(null);
  
  mensajeError = signal<string>('');
  mensajeExito = signal<string>('');

  clienteForm: FormGroup = this.fb.group({
    companyName: ['', [Validators.required, Validators.minLength(3)]],
    contactName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    cuit: ['', [Validators.pattern('^[0-9]{11}$')]],
    password: ['']
  });

  // Lista filtrada reactiva
  clientesFiltrados = computed(() => {
    const termino = this.terminoBusqueda().trim().toLowerCase();
    const listado = this.clientes();
    if (!termino) {
      return listado;
    }
    return listado.filter((c) =>
      c.companyName?.toLowerCase().includes(termino) ||
      c.contactName?.toLowerCase().includes(termino) ||
      c.cuit?.includes(termino) ||
      c.email?.toLowerCase().includes(termino)
    );
  });

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.cargando.set(true);
    this.mensajeError.set('');

    this.clienteService.listar().pipe(
      finalize(() => {
        this.cargando.set(false);
        this.cdr.markForCheck();
      })
    ).subscribe({
      next: (data: ClienteMayorista[]) => {
        this.clientes.set(Array.isArray(data) ? data : []);
        this.cdr.markForCheck();
      },
      error: (err: any) => {
        console.error('Error al cargar clientes mayoristas:', err);
        const detalle =
          err.status === 0
            ? 'No se pudo conectar con el servidor backend en http://localhost:8080. Verificá que esté encendido.'
            : err.error?.message || 'No se pudieron cargar los clientes mayoristas. Error ' + (err.status || '');
        this.mensajeError.set(detalle);
        this.cdr.markForCheck();
      }
    });
  }

  abrirModalCrear(): void {
    this.esEdicion.set(false);
    this.clienteSeleccionadoId.set(null);
    this.clienteForm.reset();
    this.clienteForm.get('password')?.setValidators([Validators.required, Validators.minLength(4)]);
    this.clienteForm.get('password')?.updateValueAndValidity();
    this.modalFormularioAbierto.set(true);
    this.cdr.markForCheck();
  }

  abrirModalEditar(cliente: ClienteMayorista): void {
    this.esEdicion.set(true);
    this.clienteSeleccionadoId.set(cliente.id);
    this.clienteForm.patchValue({
      companyName: cliente.companyName,
      contactName: cliente.contactName,
      email: cliente.email,
      cuit: cliente.cuit || '',
      password: ''
    });
    this.clienteForm.get('password')?.clearValidators();
    this.clienteForm.get('password')?.updateValueAndValidity();
    this.modalFormularioAbierto.set(true);
    this.cdr.markForCheck();
  }

  cerrarModalFormulario(): void {
    this.modalFormularioAbierto.set(false);
    this.clienteForm.reset();
    this.cdr.markForCheck();
  }

  guardarCliente(): void {
    if (this.clienteForm.invalid) {
      this.clienteForm.markAllAsTouched();
      return;
    }

    this.guardando.set(true);
    const formVal = this.clienteForm.value;
    const datosCliente: ClienteMayoristaRequest = {
      companyName: formVal.companyName,
      contactName: formVal.contactName,
      email: formVal.email,
      cuit: formVal.cuit || undefined,
      password: formVal.password ? formVal.password : undefined
    };

    const id = this.clienteSeleccionadoId();

    if (this.esEdicion() && id) {
      this.clienteService.actualizar(id, datosCliente).pipe(
        finalize(() => {
          this.guardando.set(false);
          this.cdr.markForCheck();
        })
      ).subscribe({
        next: () => {
          this.mostrarNotificacion('Cliente actualizado con éxito.');
          this.cerrarModalFormulario();
          this.cargarClientes();
        },
        error: (err: any) => {
          console.error(err);
          const msg = err.error?.message || 'Error al actualizar el cliente.';
          this.mensajeError.set(msg);
          this.cdr.markForCheck();
        }
      });
    } else {
      this.clienteService.crear(datosCliente).pipe(
        finalize(() => {
          this.guardando.set(false);
          this.cdr.markForCheck();
        })
      ).subscribe({
        next: () => {
          this.mostrarNotificacion('Cliente registrado con éxito.');
          this.cerrarModalFormulario();
          this.cargarClientes();
        },
        error: (err: any) => {
          console.error(err);
          const msg = err.error?.message || 'Error al registrar el cliente mayorista.';
          this.mensajeError.set(msg);
          this.cdr.markForCheck();
        }
      });
    }
  }

  confirmarEliminar(cliente: ClienteMayorista): void {
    this.clienteAEliminar.set(cliente);
    this.modalEliminarAbierto.set(true);
    this.cdr.markForCheck();
  }

  cerrarModalEliminar(): void {
    this.modalEliminarAbierto.set(false);
    this.clienteAEliminar.set(null);
    this.cdr.markForCheck();
  }

  ejecutarEliminacion(): void {
    const cliente = this.clienteAEliminar();
    if (!cliente) return;

    this.clienteService.eliminar(cliente.id).subscribe({
      next: () => {
        this.mostrarNotificacion('Cliente eliminado correctamente.');
        this.cerrarModalEliminar();
        this.cargarClientes();
      },
      error: (err: any) => {
        console.error(err);
        this.mensajeError.set(err.error?.message || 'Error al intentar eliminar el cliente.');
        this.cerrarModalEliminar();
        this.cdr.markForCheck();
      }
    });
  }

  verPresupuestos(cliente: ClienteMayorista): void {
    this.clienteSeleccionadoId.set(cliente.id);
    this.presupuestosCliente.set([]);
    this.modalPresupuestosAbierto.set(true);
    this.cdr.markForCheck();

    this.clienteService.obtenerPresupuestos(cliente.id).subscribe({
      next: (data: any[]) => {
        this.presupuestosCliente.set(Array.isArray(data) ? data : []);
        this.cdr.markForCheck();
      },
      error: (err: any) => {
        console.error(err);
        this.mensajeError.set('No se pudieron obtener los presupuestos del cliente.');
        this.cdr.markForCheck();
      }
    });
  }

  cerrarModalPresupuestos(): void {
    this.modalPresupuestosAbierto.set(false);
    this.presupuestosCliente.set([]);
    this.cdr.markForCheck();
  }

  private mostrarNotificacion(msj: string): void {
    this.mensajeExito.set(msj);
    this.cdr.markForCheck();
    setTimeout(() => {
      this.mensajeExito.set('');
      this.cdr.markForCheck();
    }, 4000);
  }
}
