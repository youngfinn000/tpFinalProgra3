import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PedidoMayoristaService } from '../../../core/services/pedido-mayorista.service';
import { PresupuestoService } from '../../../core/services/presupuesto.service';
import { ClienteMayoristaService } from '../../../core/services/cliente-mayorista';
import { AuthService } from '../../../core/services/auth.service';
import { PedidoMayoristaResponse } from '../../../core/models/pedido-mayorista.model';
import { SolicitudPresupuestoResponse } from '../../../core/models/presupuesto.model';
import { ClienteMayorista } from '../../../core/models/cliente-mayorista.model';

@Component({
  selector: 'app-enviar-anticipo',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './enviar-anticipo.html',
  styleUrl: './enviar-anticipo.css',
})
export class EnviarAnticipo implements OnInit {
  private fb = inject(FormBuilder);
  private pedidoService = inject(PedidoMayoristaService);
  private presupuestoService = inject(PresupuestoService);
  private clienteService = inject(ClienteMayoristaService);
  private authService = inject(AuthService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  // Datos bancarios oficiales de STRETTO
  readonly aliasStretto = 'STRETTO.GELATO.MAYORISTA';
  readonly cbuStretto = '0070123420000012345678';
  readonly titularStretto = 'STRETTO HELADOS S.R.L.';
  readonly cuitStretto = '30-71829304-5';
  readonly bancoStretto = 'Banco Galicia';

  aliasCopiado = signal<boolean>(false);

  // Estados de datos
  pedido = signal<PedidoMayoristaResponse | null>(null);
  solicitud = signal<SolicitudPresupuestoResponse | null>(null);
  cliente = signal<ClienteMayorista | null>(null);
  pedidosPendientesAnticipo = signal<PedidoMayoristaResponse[]>([]);

  // Estados de carga y flujo
  cargando = signal<boolean>(true);
  enviando = signal<boolean>(false);
  mensajeError = signal<string>('');
  mensajeExito = signal<string>('');
  pagoConfirmado = signal<boolean>(false);

  // Recibo para KAN-267
  reciboPago = signal<{
    numeroRecibo: string;
    fechaHora: Date;
    montoAbonado: number;
    saldoRestante: number;
    metodoPago: string;
    comprobante: string;
    titular: string;
  } | null>(null);

  // Formulario de anticipo simplificado: pago mediante Alias
  form: FormGroup = this.fb.group({
    monto: [null, [Validators.required, Validators.min(1)]],
    titular: ['', [Validators.required, Validators.minLength(3)]],
    comprobante: ['', [Validators.required, Validators.minLength(3)]],
    observaciones: [''],
  });

  // Cálculos reactivos de montos (KAN-263)
  presupuestoTotal = computed(() => {
    return this.pedido()?.budget || this.solicitud()?.budget || 0;
  });

  montoMinimoAnticipo = computed(() => {
    return Math.round(this.presupuestoTotal() * 0.3);
  });

  monto50PorCiento = computed(() => {
    return Math.round(this.presupuestoTotal() * 0.5);
  });

  montoActualInput = signal<number>(0);

  saldoRestanteCalculado = computed(() => {
    const total = this.presupuestoTotal();
    const actual = this.montoActualInput();
    return Math.max(0, total - (actual || 0));
  });

  ngOnInit(): void {
    this.form.get('monto')?.valueChanges.subscribe((val) => {
      this.montoActualInput.set(Number(val) || 0);
    });

    this.resolverEntrada();
  }

  copiarAlias(): void {
    navigator.clipboard.writeText(this.aliasStretto).then(() => {
      this.aliasCopiado.set(true);
      setTimeout(() => this.aliasCopiado.set(false), 2500);
    });
  }

  private resolverEntrada(): void {
    const params = this.route.snapshot.params;
    const queryParams = this.route.snapshot.queryParams;

    const orderIdParam = params['orderId'] || queryParams['orderId'];
    const budgetIdParam = params['budgetId'] || queryParams['budgetId'];

    const usuario = this.authService.usuario();
    if (!usuario) {
      this.mensajeError.set('No se encontró sesión activa.');
      this.cargando.set(false);
      return;
    }

    const buscarCliente$ = usuario.id
      ? this.clienteService.obtenerPorId(usuario.id)
      : this.clienteService.buscarPorEmail(usuario.username);

    buscarCliente$.subscribe({
      next: (cli) => {
        this.cliente.set(cli);
        this.form.patchValue({ titular: cli.companyName || cli.contactName });

        if (orderIdParam) {
          this.cargarPorOrderId(Number(orderIdParam));
        } else if (budgetIdParam) {
          this.cargarPorBudgetId(Number(budgetIdParam));
        } else {
          this.cargarPedidosPendientes(cli.id);
        }
      },
      error: (err) => {
        console.error('Error al resolver cliente:', err);
        this.mensajeError.set('No se pudo identificar tu perfil de cliente mayorista.');
        this.cargando.set(false);
      },
    });
  }

  cargarPorOrderId(orderId: number): void {
    this.cargando.set(true);
    this.pedidoService.obtenerPorId(orderId).subscribe({
      next: (order) => {
        this.pedido.set(order);
        if (order.requestBudgetId) {
          this.cargarSolicitudAsociada(order.requestBudgetId);
        } else {
          this.inicializarMontoSugerido(order.budget || 0);
          this.cargando.set(false);
        }
      },
      error: (err) => {
        console.error('Error al cargar pedido:', err);
        this.mensajeError.set('No se encontró el pedido mayorista indicado.');
        this.cargando.set(false);
      },
    });
  }

  cargarPorBudgetId(budgetId: number): void {
    this.cargando.set(true);
    const cli = this.cliente();

    if (cli && cli.id) {
      this.pedidoService.obtenerPorCliente(cli.id).subscribe({
        next: (ordenes) => {
          const encontrada = ordenes.find((o) => o.requestBudgetId === budgetId);
          if (encontrada) {
            this.pedido.set(encontrada);
            this.cargarSolicitudAsociada(budgetId);
          } else {
            this.resolverPorPresupuesto(budgetId);
          }
        },
        error: () => this.resolverPorPresupuesto(budgetId),
      });
    } else {
      this.resolverPorPresupuesto(budgetId);
    }
  }

  private resolverPorPresupuesto(budgetId: number): void {
    this.pedidoService.obtenerPorPresupuestoId(budgetId).subscribe({
      next: (order) => {
        this.pedido.set(order);
        this.cargarSolicitudAsociada(budgetId);
      },
      error: () => {
        this.pedidoService.crearDesdePresupuesto(budgetId).subscribe({
          next: (nuevaOrden) => {
            this.pedido.set(nuevaOrden);
            this.cargarSolicitudAsociada(budgetId);
          },
          error: () => {
            this.presupuestoService.obtenerPorId(budgetId).subscribe({
              next: (req) => {
                this.solicitud.set(req);
                this.inicializarMontoSugerido(req.budget || 0);
                this.cargando.set(false);
              },
              error: () => {
                this.mensajeError.set('No se encontró la cotización de presupuesto solicitada.');
                this.cargando.set(false);
              },
            });
          },
        });
      },
    });
  }

  private cargarSolicitudAsociada(budgetId: number): void {
    this.presupuestoService.obtenerPorId(budgetId).subscribe({
      next: (req) => {
        this.solicitud.set(req);
        const total = this.pedido()?.budget || req.budget || 0;
        this.inicializarMontoSugerido(total);
        this.cargando.set(false);
      },
      error: (err) => {
        console.warn('No se pudo obtener el detalle de la solicitud:', err);
        this.inicializarMontoSugerido(this.pedido()?.budget || 0);
        this.cargando.set(false);
      },
    });
  }

  private cargarPedidosPendientes(clienteId: number): void {
    this.pedidoService.obtenerPorCliente(clienteId).subscribe({
      next: (ordenes) => {
        const pendientes = ordenes.filter((o) => !o.advancePaymentAmount);
        this.pedidosPendientesAnticipo.set(pendientes);

        if (pendientes.length === 1) {
          this.seleccionarPedido(pendientes[0]);
        } else {
          this.cargando.set(false);
        }
      },
      error: (err) => {
        console.error('Error al listar pedidos del cliente:', err);
        this.mensajeError.set('No se pudieron obtener tus pedidos mayoristas.');
        this.cargando.set(false);
      },
    });
  }

  seleccionarPedido(order: PedidoMayoristaResponse): void {
    this.pedido.set(order);
    if (order.requestBudgetId) {
      this.cargarSolicitudAsociada(order.requestBudgetId);
    } else {
      this.inicializarMontoSugerido(order.budget || 0);
      this.cargando.set(false);
    }
  }

  private inicializarMontoSugerido(total: number): void {
    if (this.pedido()?.advancePaymentAmount) {
      this.pagoConfirmado.set(true);
      return;
    }

    const sugerido = Math.round(total * 0.3) || 50000;
    this.form.patchValue({ monto: sugerido });
    this.montoActualInput.set(sugerido);
  }

  seleccionarPorcentaje(porcentaje: number): void {
    const total = this.presupuestoTotal();
    const calculado = Math.round((total * porcentaje) / 100);
    this.form.patchValue({ monto: calculado });
  }

  // Validar y Registrar Anticipo
  enviarAnticipo(): void {
    this.mensajeError.set('');

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.mensajeError.set('Por favor, completá todos los campos requeridos correctamente.');
      return;
    }

    const total = this.presupuestoTotal();
    const monto = Number(this.form.get('monto')?.value);

    if (monto <= 0) {
      this.mensajeError.set('El monto del anticipo debe ser mayor a $0.');
      return;
    }

    if (total > 0 && monto > total) {
      this.mensajeError.set(`El anticipo ($${monto.toLocaleString('es-AR')}) no puede superar el valor total del presupuesto ($${total.toLocaleString('es-AR')}).`);
      return;
    }

    const customerId = this.cliente()?.id || this.pedido()?.wholesaleCustomerId || this.solicitud()?.customerId;
    if (!customerId) {
      this.mensajeError.set('No se identificó el perfil de cliente comercial para registrar el pago.');
      return;
    }

    const orderId = this.pedido()?.id;
    const budgetId = this.pedido()?.requestBudgetId || this.solicitud()?.id;

    if (orderId && (!budgetId || orderId !== budgetId)) {
      this.ejecutarRegistroAnticipo(orderId, customerId, monto, total);
    } else if (budgetId) {
      this.enviando.set(true);
      this.pedidoService.crearDesdePresupuesto(budgetId).subscribe({
        next: (ordenCreada) => {
          this.pedido.set(ordenCreada);
          this.ejecutarRegistroAnticipo(ordenCreada.id, customerId, monto, total);
        },
        error: () => {
          this.ejecutarRegistroAnticipo(orderId || budgetId, customerId, monto, total);
        },
      });
    } else {
      this.mensajeError.set('No se pudo identificar el pedido a confirmar.');
    }
  }

  private ejecutarRegistroAnticipo(orderId: number, customerId: number, monto: number, total: number): void {
    this.enviando.set(true);
    this.pedidoService.registrarAnticipo(orderId, customerId, monto).subscribe({
      next: (ordenActualizada) => {
        this.enviando.set(false);
        this.pedido.set(ordenActualizada);

        const saldo = Math.max(0, total - monto);
        const comp = this.form.get('comprobante')?.value;

        this.reciboPago.set({
          numeroRecibo: `REC-STRETTO-${ordenActualizada.id || orderId}-${Math.floor(1000 + Math.random() * 9000)}`,
          fechaHora: new Date(),
          montoAbonado: monto,
          saldoRestante: saldo,
          metodoPago: `Transferencia (Alias: ${this.aliasStretto})`,
          comprobante: comp,
          titular: this.form.get('titular')?.value,
        });

        this.pagoConfirmado.set(true);
        this.mensajeExito.set('El anticipo fue registrado correctamente y tu pedido ha quedado confirmado.');
      },
      error: (err) => {
        this.enviando.set(false);
        console.error('Error al registrar anticipo:', err);
        const msj = err.error?.message || 'No se pudo procesar el anticipo. Verificá los datos e intentá nuevamente.';
        this.mensajeError.set(msj);
      },
    });
  }

  imprimirComprobante(): void {
    window.print();
  }

  volverAMisSolicitudes(): void {
    this.router.navigate(['/mis-solicitudes']);
  }
}
