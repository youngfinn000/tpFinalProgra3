import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductoService } from '../services/producto.service';

@Component({
  selector: 'app-producto-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './producto-form.html',
  styleUrl: './producto-form.css'
})
export class ProductoForm implements OnInit {
  private fb = inject(FormBuilder);
  private productoService = inject(ProductoService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  esEdicion = signal<boolean>(false);
  guardando = signal<boolean>(false);
  error = signal<string>('');
  mensajeExito = signal<string>('');
  productoId: number | null = null;

  form: FormGroup = this.fb.group({
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
    categoria: ['GELATO', [Validators.required]],
    saboresMax: [null, [Validators.min(0)]],
    precio: [null, [Validators.required, Validators.min(1)]],
    stock: [0, [Validators.required, Validators.min(0)]],
    descripcion: [''],
    activo: [true, [Validators.required]]
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.productoId = Number(idParam);
      this.esEdicion.set(true);
      this.cargarProducto(this.productoId);
    }
  }

  cargarProducto(id: number): void {
    this.productoService.obtener(id).subscribe({
      next: (prod) => {
        if (prod) {
          this.form.patchValue({
            nombre: prod.nombre,
            categoria: prod.categoria,
            saboresMax: prod.maxSabores,
            precio: prod.precio,
            stock: prod.stock,
            descripcion: prod.descripcion,
            activo: prod.activo
          });
        }
      },
      error: () => this.error.set('No se pudo cargar la información del producto.')
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.guardando.set(true);
    this.error.set('');
    this.mensajeExito.set('');

    const datos = this.form.value;
    const peticion$ = this.esEdicion() && this.productoId
      ? this.productoService.actualizarProducto(this.productoId, datos)
      : this.productoService.createProducto(datos);

    peticion$.subscribe({
      next: () => {
        this.guardando.set(false);
        this.mensajeExito.set(
          this.esEdicion()
            ? '¡Producto actualizado correctamente!'
            : '¡Producto creado con éxito!'
        );
        setTimeout(() => this.router.navigate(['/productos']), 1200);
      },
      error: (err) => {
        this.guardando.set(false);
        this.error.set('Error al guardar el producto. Verifique los datos e intente nuevamente.');
        console.error(err);
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/productos']);
  }
}