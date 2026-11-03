import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { Producto } from '../models/producto.model';
import { ProductoService } from '../services/producto.service';
import {
  LucideIceCreamCone,
  LucideCakeSlice,
  LucideCupSoda,
  LucideFlame,
} from '@lucide/angular';

@Component({
  selector: 'app-producto-detalle',
  standalone: true,
  imports: [
  CommonModule,
  RouterLink,
  LucideIceCreamCone,
  LucideCakeSlice,
  LucideCupSoda,
  LucideFlame,
  ],
  templateUrl: './producto-detalle.html',
  styleUrl: './producto-detalle.css',
})
export class ProductoDetalle implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private productoService = inject(ProductoService);
  private authService = inject(AuthService);

  producto = signal<Producto | null>(null);
  cargando = signal<boolean>(true);
  error = signal<string>('');

  esAdmin = signal(this.authService.tieneRol('ROLE_ADMIN'));

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = Number(idParam);

    if (!id || isNaN(id)) {
      this.error.set('Identificador de producto no válido.');
      this.cargando.set(false);
      return;
    }

    this.cargarDetalle(id);
  }

  cargarDetalle(id: number): void {
    this.cargando.set(true);
    this.error.set('');

    this.productoService.obtener(id).subscribe({
      next: (prod) => {
        if (prod) {
          this.producto.set(prod);
        } else {
          this.error.set('No se encontró el producto solicitado.');
        }
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.error.set('Error al conectar con el servidor para obtener el producto.');
        console.error(err);
      },
    });
  }

  formatoPrecio(precio: number): string {
    return new Intl.NumberFormat('es-AR', {
      style: 'currency',
      currency: 'ARS',
      maximumFractionDigits: 0,
    }).format(precio);
  }

  obtenerClaseStock(stock: number): string {
    if (stock <= 0) return 'stock-agotado';
    if (stock < 10) return 'stock-bajo';
    return 'stock-disponible';
  }

  obtenerTextoStock(stock: number): string {
    if (stock <= 0) return 'Sin Stock';
    if (stock < 10) return `Bajo Stock (${stock} unidades)`;
    return `${stock} unidades disponibles`;
  }
}

