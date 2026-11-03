import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ModalComponent } from '../../../shared/components/modal/modal.component';
import { CategoriaProducto, Producto } from '../models/producto.model';
import { ProductoService } from '../services/producto.service';
import {
  LucideIceCreamCone,
  LucideCakeSlice,
  LucideCupSoda,
  LucideFlame,
  LucideEye,
} from '@lucide/angular';

@Component({
  selector: 'app-producto-list',
  standalone: true,
  imports: [
  CommonModule,
  FormsModule,
  RouterLink,
  ModalComponent,
  LucideIceCreamCone,
  LucideCakeSlice,
  LucideCupSoda,
  LucideFlame,
  LucideEye,
  ],
  templateUrl: './producto-list.html',
  styleUrl: './producto-list.css',
})
export class ProductoList implements OnInit {
  private productoService = inject(ProductoService);
  private authService = inject(AuthService);
  private router = inject(Router);

  // Permisos y usuario
  esAdmin = computed(() => this.authService.tieneRol('ROLE_ADMIN'));

  // Estado de datos
  productos = signal<Producto[]>([]);
  cargando = signal<boolean>(false);
  error = signal<string>('');
  mensajeExito = signal<string>('');

  // Filtros reactivos
  filtroNombre = signal<string>('');
  filtroCategoria = signal<string>('todas');
  filtroEstado = signal<'todos' | 'activos' | 'inactivos'>('activos');

  // Modal de Detalle de Producto
  modalDetalleAbierto = signal<boolean>(false);
  productoSeleccionado = signal<Producto | null>(null);

  // Lista de categorías para botones rápidos
  categorias = [
    { id: 'todas', label: 'Todos' },
    { id: 'GELATO', label: 'Gelato' },
    { id: 'POSTRES_HELADOS', label: 'Postres Helados' },
    { id: 'BATIDOS', label: 'Batidos' },
    { id: 'PROMO', label: 'Promos' },
  ];

  // Productos filtrados reactivamente
  productosFiltrados = computed(() => {
    const texto = this.filtroNombre().toLowerCase().trim();
    const cat = this.filtroCategoria();
    const est = this.filtroEstado();

    return this.productos().filter((p) => {
      // Búsqueda por nombre o categoría
      const coincideNombre =
        !texto ||
        p.nombre.toLowerCase().includes(texto) ||
        p.categoriaLabel.toLowerCase().includes(texto) ||
        p.precio.toString().includes(texto);

      // Filtro por categoría
      const coincideCat = cat === 'todas' || p.categoria === cat;

      // Filtro por estado
      const coincideEstado =
        est === 'todos' ||
        (est === 'activos' && p.activo) ||
        (est === 'inactivos' && !p.activo);

      return coincideNombre && coincideCat && coincideEstado;
    });
  });

  // Métricas computadas del catálogo
  totalProductos = computed(() => this.productos().length);
  totalActivos = computed(() => this.productos().filter((p) => p.activo).length);
  totalBajoStock = computed(() => this.productos().filter((p) => p.stock > 0 && p.stock < 10).length);
  totalSinStock = computed(() => this.productos().filter((p) => p.stock <= 0).length);

  ngOnInit(): void {
    this.cargarProductos();
  }

  /**
   * TASK: Actualiza listado de productos
   */
  cargarProductos(): void {
    this.cargando.set(true);
    this.error.set('');

    this.productoService.listar().subscribe({
      next: (lista) => {
        this.productos.set(lista);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.error.set('No se pudo actualizar el catálogo de productos. Verificá la conexión.');
        console.error('Error al cargar productos:', err);
      },
    });
  }

  /**
   * TASK: Crear vista de detalle de producto (Modal rápido)
   */
  abrirDetalle(prod: Producto): void {
    this.productoSeleccionado.set(prod);
    this.modalDetalleAbierto.set(true);
  }

  cerrarDetalle(): void {
    this.modalDetalleAbierto.set(false);
    this.productoSeleccionado.set(null);
  }

  /**
   * Navega a la vista de página completa de detalle
   */
  irADetallePagina(id: number): void {
    this.router.navigate(['/productos', id]);
  }

  /**
   * Limpia el buscador
   */
  limpiarBusqueda(): void {
    this.filtroNombre.set('');
  }

  /**
   * Formateador de moneda en pesos argentinos
   * TASK: Mostrar precio del producto
   */
  formatoPrecio(precio: number): string {
    return new Intl.NumberFormat('es-AR', {
      style: 'currency',
      currency: 'ARS',
      maximumFractionDigits: 0,
    }).format(precio);
  }


  // Modal de precio rápido
  modalPrecioAbierto = signal<boolean>(false);
  productoPrecioSel = signal<Producto | null>(null);
  nuevoPrecioInput = signal<number>(0);
  guardandoPrecio = signal<boolean>(false);

  abrirModalPrecio(prod: Producto, event?: Event): void {
    if (event) event.stopPropagation();
    this.productoPrecioSel.set(prod);
    this.nuevoPrecioInput.set(prod.precio);
    this.modalPrecioAbierto.set(true);
  }

  cerrarModalPrecio(): void {
    this.modalPrecioAbierto.set(false);
    this.productoPrecioSel.set(null);
  }

  aplicarPorcentaje(porcentaje: number): void {
    const prod = this.productoPrecioSel();
    if (prod) {
      const calculado = Math.round(prod.precio * (1 + porcentaje / 100));
      this.nuevoPrecioInput.set(calculado);
    }
  }

  guardarNuevoPrecio(): void {
    const prod = this.productoPrecioSel();
    const precio = this.nuevoPrecioInput();

    if (!prod || precio <= 0) return;

    this.guardandoPrecio.set(true);
    this.productoService.actualizarPrecio(prod.id, precio).subscribe({
      next: () => {
        this.guardandoPrecio.set(false);
        this.cerrarModalPrecio();
        this.cargarProductos(); // Recarga la grilla
      },
      error: () => {
        this.guardandoPrecio.set(false);
      }
    });
  }
  /**
   * Determina la clase del badge según el stock
   * TASK: Mostrar stock del producto
   */
  obtenerClaseStock(stock: number): string {
    if (stock <= 0) return 'stock-agotado';
    if (stock < 10) return 'stock-bajo';
    return 'stock-disponible';
  }

  obtenerTextoStock(stock: number): string {
    if (stock <= 0) return 'Sin Stock';
    if (stock < 10) return `Bajo Stock (${stock})`;
    return `${stock} u.`;
  }
}

