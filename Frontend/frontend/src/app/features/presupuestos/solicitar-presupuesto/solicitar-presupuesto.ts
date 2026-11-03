import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PresupuestoService } from '../../../core/services/presupuesto.service';
import { ClienteMayoristaService } from '../../../core/services/cliente-mayorista';
import { SaborService } from '../../sabores/services/sabor-service';
import { AuthService } from '../../../core/services/auth.service';
import { ClienteMayorista } from '../../../core/models/cliente-mayorista.model';
import { Sabor } from '../../sabores/models/sabor.model';
import {
  SolicitudPresupuestoRequest,
  SolicitudPresupuestoResponse,
} from '../../../core/models/presupuesto.model';

@Component({
  selector: 'app-solicitar-presupuesto',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './solicitar-presupuesto.html',
  styleUrl: './solicitar-presupuesto.css',
})
export class SolicitarPresupuesto implements OnInit {
  private fb = inject(FormBuilder);
  private presupuestoService = inject(PresupuestoService);
  private clienteService = inject(ClienteMayoristaService);
  private saborService = inject(SaborService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  clientes = signal<ClienteMayorista[]>([]);
  clienteMayoristaActual = signal<ClienteMayorista | null>(null);

  saboresDisponibles = signal<Sabor[]>([]);
  saboresFiltrados = signal<Sabor[]>([]);
  saboresSeleccionados = signal<Sabor[]>([]);

  filtroSabor = signal<string>('');
  cargando = signal<boolean>(true);
  enviando = signal<boolean>(false);
  mensajeError = signal<string>('');
  solicitudExitosa = signal<SolicitudPresupuestoResponse | null>(null);

  esClienteMayorista = signal<boolean>(false);
  fechaMinima: string = '';

  form: FormGroup = this.fb.group({
    customerId: [null, [Validators.required, Validators.min(1)]],
    quantity: [15, [Validators.required, Validators.min(1)]],
    eventDate: ['', [Validators.required]],
    advancePayment: [false],
  });

  ngOnInit(): void {
    // Calcular fecha mínima (a partir de mañana)
    const manana = new Date();
    manana.setDate(manana.getDate() + 1);
    this.fechaMinima = manana.toISOString().split('T')[0];

    const rol = this.authService.rol();
    const esMayorista = rol === 'ROLE_WHOLESALE';
    this.esClienteMayorista.set(esMayorista);

    this.cargarDatosIniciales();
  }

  cargarDatosIniciales(): void {
    this.cargando.set(true);

    // Cargar sabores activos del catálogo
    this.saborService
      .listar({ estado: 'activos', nombre: '', modo: 'parcial' })
      .subscribe({
        next: (sabores) => {
          this.saboresDisponibles.set(sabores);
          this.saboresFiltrados.set(sabores);
        },
        error: (err) => console.error('Error al cargar sabores:', err),
      });

    const usuario = this.authService.usuario();

    // Si es Cliente Mayorista: obtener sus propios datos directamente
    if (this.esClienteMayorista() && usuario) {
      if (usuario.id) {
        this.clienteService.obtenerPorId(usuario.id).subscribe({
          next: (c) => this.establecerClienteMayorista(c),
          error: () => this.buscarClientePorEmail(usuario.username),
        });
      } else {
        this.buscarClientePorEmail(usuario.username);
      }
    } else {
      // Si es Admin o Empleado: cargar la lista para el desplegable de clientes
      this.clienteService.listar().subscribe({
        next: (lista) => {
          this.clientes.set(lista);
          this.cargando.set(false);

          // Si viene preseleccionado por query param ?clienteId=1
          const qId = this.route.snapshot.queryParamMap.get('clienteId');
          if (qId) {
            this.form.patchValue({ customerId: Number(qId) });
          }
        },
        error: (err) => {
          console.error('Error al cargar clientes mayoristas:', err);
          this.cargando.set(false);
        },
      });
    }
  }

  private buscarClientePorEmail(email: string): void {
    this.clienteService.buscarPorEmail(email).subscribe({
      next: (c) => this.establecerClienteMayorista(c),
      error: (err) => {
        console.error('No se pudo encontrar el cliente mayorista por email:', err);
        this.cargando.set(false);
      },
    });
  }

  private establecerClienteMayorista(c: ClienteMayorista): void {
    this.clienteMayoristaActual.set(c);
    this.form.patchValue({ customerId: c.id });
    this.cargando.set(false);
  }

  buscarSabores(evento: Event): void {
    const termino = (evento.target as HTMLInputElement).value.toLowerCase().trim();
    this.filtroSabor.set(termino);
    if (!termino) {
      this.saboresFiltrados.set(this.saboresDisponibles());
      return;
    }
    this.saboresFiltrados.set(
      this.saboresDisponibles().filter((s) => s.nombre.toLowerCase().includes(termino)),
    );
  }

  estaSeleccionado(sabor: Sabor): boolean {
    return this.saboresSeleccionados().some((s) => s.id === sabor.id);
  }

  toggleSabor(sabor: Sabor): void {
    const actuales = this.saboresSeleccionados();
    if (this.estaSeleccionado(sabor)) {
      this.saboresSeleccionados.set(actuales.filter((s) => s.id !== sabor.id));
    } else {
      this.saboresSeleccionados.set([...actuales, sabor]);
    }
  }

  eliminarSabor(saborId: number): void {
    this.saboresSeleccionados.set(
      this.saboresSeleccionados().filter((s) => s.id !== saborId),
    );
  }

  enviarSolicitud(): void {
    this.mensajeError.set('');

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    if (this.saboresSeleccionados().length === 0) {
      this.mensajeError.set('Debés seleccionar al menos un sabor para tu evento.');
      return;
    }

    // Validar fecha futura
    const hoyStr = new Date().toISOString().split('T')[0];
    if (this.form.value.eventDate <= hoyStr) {
      this.mensajeError.set('La fecha del evento debe ser posterior a la fecha actual.');
      return;
    }

    this.enviando.set(true);

    const ahora = new Date();
    const ahoraIso = ahora.toISOString().split('.')[0];

    const payload: SolicitudPresupuestoRequest = {
      quantity: Number(this.form.value.quantity),
      customerId: Number(this.form.value.customerId),
      eventDate: this.form.value.eventDate,
      requestDatetime: ahoraIso,
      advancePayment: Boolean(this.form.value.advancePayment),
      flavorsId: this.saboresSeleccionados().map((s) => s.id),
    };

    this.presupuestoService.crear(payload).subscribe({
      next: (resp) => {
        this.enviando.set(false);
        this.solicitudExitosa.set(resp);
      },
      error: (err) => {
        this.enviando.set(false);
        console.error('Error al registrar presupuesto:', err);
        this.mensajeError.set(
          err.error?.message ??
            'Ocurrió un error al procesar la solicitud de presupuesto. Verificá los datos ingresados.',
        );
      },
    });
  }

  nuevaSolicitud(): void {
    this.solicitudExitosa.set(null);
    this.saboresSeleccionados.set([]);
    this.form.reset({
      quantity: 15,
      advancePayment: false,
    });
    if (this.clienteMayoristaActual()) {
      this.form.patchValue({ customerId: this.clienteMayoristaActual()!.id });
    }
  }
}
