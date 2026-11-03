import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
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

  clientes: ClienteMayorista[] = [];
  clientesFiltrados: ClienteMayorista[] = [];
  presupuestosCliente: any[] = [];

  cargando = false;
  guardando = false;
  terminoBusqueda = '';
  
  modalFormularioAbierto = false;
  modalEliminarAbierto = false;
  modalPresupuestosAbierto = false;
  
  esEdicion = false;
  clienteSeleccionadoId: number | null = null;
  clienteAEliminar: ClienteMayorista | null = null;
  
  mensajeError = '';
  mensajeExito = '';

  clienteForm: FormGroup = this.fb.group({
    companyName: ['', [Validators.required, Validators.minLength(3)]],
    contactName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    cuit: ['', [Validators.pattern('^[0-9]{11}$')]],
    password: ['']
  });

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.cargando = true;
    this.mensajeError = '';
    this.clienteService.listar().subscribe({
      next: (data: ClienteMayorista[]) => {
        this.clientes = data;
        this.filtrarClientes();
        this.cargando = false;
      },
      error: (err: any) => {
        console.error('Error al cargar clientes mayoristas:', err);
        this.mensajeError = 'No se pudieron cargar los clientes mayoristas.';
        this.cargando = false;
      }
    });
  }

  filtrarClientes(): void {
    const termino = this.terminoBusqueda.trim().toLowerCase();
    if (!termino) {
      this.clientesFiltrados = [...this.clientes];
      return;
    }
    this.clientesFiltrados = this.clientes.filter(c => 
      c.companyName?.toLowerCase().includes(termino) ||
      c.contactName?.toLowerCase().includes(termino) ||
      c.cuit?.includes(termino) ||
      c.email?.toLowerCase().includes(termino)
    );
  }

  abrirModalCrear(): void {
    this.esEdicion = false;
    this.clienteSeleccionadoId = null;
    this.clienteForm.reset();
    this.modalFormularioAbierto = true;
  }

  abrirModalEditar(cliente: ClienteMayorista): void {
    this.esEdicion = true;
    this.clienteSeleccionadoId = cliente.id;
    this.clienteForm.patchValue({
      companyName: cliente.companyName,
      contactName: cliente.contactName,
      email: cliente.email,
      cuit: cliente.cuit || '',
      password: ''
    });
    this.modalFormularioAbierto = true;
  }

  cerrarModalFormulario(): void {
    this.modalFormularioAbierto = false;
    this.clienteForm.reset();
  }

  guardarCliente(): void {
    if (this.clienteForm.invalid) {
      this.clienteForm.markAllAsTouched();
      return;
    }

    this.guardando = true;
    const datosCliente: ClienteMayoristaRequest = this.clienteForm.value;

    if (this.esEdicion && this.clienteSeleccionadoId) {
      this.clienteService.actualizar(this.clienteSeleccionadoId, datosCliente).subscribe({
        next: () => {
          this.mostrarNotificacion('Cliente actualizado con éxito.');
          this.cerrarModalFormulario();
          this.cargarClientes();
          this.guardando = false;
        },
        error: (err: any) => {
          console.error(err);
          this.mensajeError = 'Error al actualizar el cliente.';
          this.guardando = false;
        }
      });
    } else {
      this.clienteService.crear(datosCliente).subscribe({
        next: () => {
          this.mostrarNotificacion('Cliente registrado con éxito.');
          this.cerrarModalFormulario();
          this.cargarClientes();
          this.guardando = false;
        },
        error: (err: any) => {
          console.error(err);
          this.mensajeError = 'Error al registrar el cliente mayorista.';
          this.guardando = false;
        }
      });
    }
  }

  confirmarEliminar(cliente: ClienteMayorista): void {
    this.clienteAEliminar = cliente;
    this.modalEliminarAbierto = true;
  }

  cerrarModalEliminar(): void {
    this.modalEliminarAbierto = false;
    this.clienteAEliminar = null;
  }

  ejecutarEliminacion(): void {
    if (!this.clienteAEliminar) return;

    this.clienteService.eliminar(this.clienteAEliminar.id).subscribe({
      next: () => {
        this.mostrarNotificacion('Cliente eliminado correctamente.');
        this.cerrarModalEliminar();
        this.cargarClientes();
      },
      error: (err: any) => {
        console.error(err);
        this.mensajeError = 'Error al intentar eliminar el cliente.';
        this.cerrarModalEliminar();
      }
    });
  }

  verPresupuestos(cliente: ClienteMayorista): void {
    this.clienteSeleccionadoId = cliente.id;
    this.presupuestosCliente = [];
    this.modalPresupuestosAbierto = true;

    this.clienteService.obtenerPresupuestos(cliente.id).subscribe({
      next: (data: any[]) => {
        this.presupuestosCliente = data;
      },
      error: (err: any) => {
        console.error(err);
        this.mensajeError = 'No se pudieron obtener los presupuestos del cliente.';
      }
    });
  }

  cerrarModalPresupuestos(): void {
    this.modalPresupuestosAbierto = false;
    this.presupuestosCliente = [];
  }

  private mostrarNotificacion(msj: string): void {
    this.mensajeExito = msj;
    setTimeout(() => this.mensajeExito = '', 4000);
  }
}
