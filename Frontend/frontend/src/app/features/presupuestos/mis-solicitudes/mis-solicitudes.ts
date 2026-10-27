import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { PresupuestoService } from '../../../core/services/presupuesto.service';
import { ClienteMayoristaService } from '../../../core/services/cliente-mayorista';
import { AuthService } from '../../../core/services/auth.service';
import { SolicitudPresupuestoResponse, EstadoPresupuesto } from '../../../core/models/presupuesto.model';
import { ClienteMayorista } from '../../../core/models/cliente-mayorista.model';

@Component({
  selector: 'app-mis-solicitudes',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './mis-solicitudes.html',
  styleUrl: './mis-solicitudes.css',
})
export class MisSolicitudes implements OnInit {
  private presupuestoService = inject(PresupuestoService);
  private clienteService = inject(ClienteMayoristaService);
  private authService = inject(AuthService);

  solicitudes = signal<SolicitudPresupuestoResponse[]>([]);
  solicitudesFiltradas = signal<SolicitudPresupuestoResponse[]>([]);
  clienteActual = signal<ClienteMayorista | null>(null);

  solicitudSeleccionada = signal<SolicitudPresupuestoResponse | null>(null);
  modalDetalleAbierto = signal<boolean>(false);

  cargando = signal<boolean>(true);
  mensajeError = signal<string>('');
  mensajeExito = signal<string>('');

  esClienteMayorista = signal<boolean>(false);
  filtroEstado = signal<string>('TODAS');
  busqueda = signal<string>('');

  // Para administradores que quieran cotizar una solicitud directamente desde la ficha
  montoCotizacion: number = 0;
  guardandoCotizacion = signal<boolean>(false);

  ngOnInit(): void {
    const rol = this.authService.rol();
    this.esClienteMayorista.set(rol === 'ROLE_WHOLESALE');
    this.cargarSolicitudes();
  }

  cargarSolicitudes(): void {
    this.cargando.set(true);
    this.mensajeError.set('');

    const usuario = this.authService.usuario();

    if (this.esClienteMayorista() && usuario) {
      // Cliente Mayorista: buscar su perfil y sus solicitudes
      if (usuario.id) {
        this.clienteService.obtenerPorId(usuario.id).subscribe({
          next: (c) => this.cargarSolicitudesDeCliente(c),
          error: () => this.buscarClientePorEmail(usuario.username),
        });
      } else {
        this.buscarClientePorEmail(usuario.username);
      }
    } else {
      // Administrador o Empleado: lista todas las solicitudes
      this.presupuestoService.listarTodos().subscribe({
        next: (data) => {
          this.solicitudes.set(data);
          this.aplicarFiltros();
          this.cargando.set(false);
        },
        error: (err) => {
          console.error('Error al cargar solicitudes:', err);
          this.mensajeError.set('No se pudieron obtener las solicitudes de presupuesto.');
          this.cargando.set(false);
        },
      });
    }
  }

  private buscarClientePorEmail(email: string): void {
    this.clienteService.buscarPorEmail(email).subscribe({
      next: (c) => this.cargarSolicitudesDeCliente(c),
      error: (err) => {
        console.error('No se pudo encontrar el cliente mayorista:', err);
        this.mensajeError.set('No se encontró el perfil de cliente comercial asociado a tu usuario.');
        this.cargando.set(false);
      },
    });
  }

  private cargarSolicitudesDeCliente(cliente: ClienteMayorista): void {
    this.clienteActual.set(cliente);
    this.presupuestoService.obtenerPorCliente(cliente.id).subscribe({
      next: (data) => {
        this.solicitudes.set(data);
        this.aplicarFiltros();
        this.cargando.set(false);
      },
      error: (err) => {
        console.error('Error al cargar solicitudes del cliente:', err);
        this.mensajeError.set('No se pudieron obtener tus solicitudes de presupuesto.');
        this.cargando.set(false);
      },
    });
  }

  aplicarFiltros(): void {
    const estado = this.filtroEstado();
    const texto = this.busqueda().trim().toLowerCase();

    let resultado = this.solicitudes();

    // Filtro por estado
    if (estado !== 'TODAS') {
      resultado = resultado.filter((s) => s.stateRequestEnum === estado);
    }

    // Filtro por texto (#ID, sabores, cliente)
    if (texto) {
      resultado = resultado.filter(
        (s) =>
          s.id.toString().includes(texto) ||
          s.customerName?.toLowerCase().includes(texto) ||
          s.flavors?.some((f) => f.name.toLowerCase().includes(texto)),
      );
    }

    this.solicitudesFiltradas.set(resultado);
  }

  cambiarFiltroEstado(nuevoEstado: string): void {
    this.filtroEstado.set(nuevoEstado);
    this.aplicarFiltros();
  }

  alBuscar(evento: Event): void {
    const valor = (evento.target as HTMLInputElement).value;
    this.busqueda.set(valor);
    this.aplicarFiltros();
  }

  verFicha(solicitud: SolicitudPresupuestoResponse): void {
    this.solicitudSeleccionada.set(solicitud);
    this.montoCotizacion = solicitud.budget || 0;
    this.modalDetalleAbierto.set(true);
  }

  cerrarModal(): void {
    this.modalDetalleAbierto.set(false);
    this.solicitudSeleccionada.set(null);
  }

  // Acción para que el Administrador cotice / apruebe una solicitud
  cotizarSolicitud(solicitudId: number): void {
    if (this.montoCotizacion <= 0) {
      alert('Ingresá un monto de presupuesto válido mayor a 0.');
      return;
    }

    this.guardandoCotizacion.set(true);
    this.presupuestoService
      .actualizarEstado(solicitudId, 'CONFIRMED', this.montoCotizacion)
      .subscribe({
        next: (actualizada) => {
          this.guardandoCotizacion.set(false);
          this.solicitudSeleccionada.set(actualizada);
          this.mostrarExito('¡Presupuesto cotizado y aprobado con éxito!');
          this.cargarSolicitudes();
        },
        error: (err) => {
          this.guardandoCotizacion.set(false);
          console.error('Error al actualizar estado:', err);
          alert(err.error?.message ?? 'No se pudo actualizar la cotización.');
        },
      });
  }

  rechazarSolicitud(solicitudId: number): void {
    if (!confirm('¿Estás seguro de que querés rechazar esta solicitud de presupuesto?')) return;

    this.guardandoCotizacion.set(true);
    this.presupuestoService.actualizarEstado(solicitudId, 'REJECTED', 0).subscribe({
      next: (actualizada) => {
        this.guardandoCotizacion.set(false);
        this.solicitudSeleccionada.set(actualizada);
        this.mostrarExito('La solicitud fue rechazada.');
        this.cargarSolicitudes();
      },
      error: (err) => {
        this.guardandoCotizacion.set(false);
        console.error('Error al rechazar solicitud:', err);
        alert(err.error?.message ?? 'No se pudo rechazar la solicitud.');
      },
    });
  }

  private mostrarExito(msj: string): void {
    this.mensajeExito.set(msj);
    setTimeout(() => this.mensajeExito.set(''), 4000);
  }
}

