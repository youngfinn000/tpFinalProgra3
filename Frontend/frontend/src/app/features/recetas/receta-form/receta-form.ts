import { Component, OnInit, inject, signal } from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Insumo, etiquetaUnidad } from '../../insumos/models/insumo';
import { InsumoService } from '../../insumos/services/insumo.service';
import { Sabor } from '../../sabores/models/sabor.model';
import { SaborService } from '../../sabores/services/sabor-service';
import { Receta, RecetaRequest } from '../models/receta.model';
import { RecetaService } from '../services/receta.service';

@Component({
  selector: 'app-receta-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './receta-form.html',
  styleUrl: './receta-form.css',
})
export class RecetaForm implements OnInit {
  private fb = inject(FormBuilder);
  private recetaService = inject(RecetaService);
  private saborService = inject(SaborService);
  private insumoService = inject(InsumoService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  sabores = signal<Sabor[]>([]);
  insumos = signal<Insumo[]>([]);

  guardando = signal(false);
  cargando = signal(false);
  error = signal('');
  esEdicion = signal(false);

  etiquetaUnidad = etiquetaUnidad;

  private id: number | null = null;

  form = this.fb.group({
    flavorId: [null as number | null, Validators.required],
    nombre: ['', [Validators.required, Validators.maxLength(120)]],
    cantidadBase: [10, [Validators.required, Validators.min(0.1)]],
    ingredientes: this.fb.array<FormGroup>([]),
  });

  get ingredientes(): FormArray<FormGroup> {
    return this.form.get('ingredientes') as FormArray<FormGroup>;
  }

  ngOnInit() {
    this.cargarCatalogos();

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam !== null) {
      this.id = Number(idParam);
      if (!Number.isInteger(this.id) || this.id <= 0) {
        this.error.set('El identificador de la receta no es válido.');
        return;
      }
      this.esEdicion.set(true);
      this.cargarReceta(this.id);
    } else {
      this.agregarIngrediente();
    }
  }

  private cargarCatalogos() {
    this.saborService
      .listar({ estado: 'todos', nombre: '', modo: 'parcial' })
      .subscribe({
        next: (lista) => this.sabores.set(lista),
        error: () => this.error.set('No se pudieron cargar los sabores.'),
      });

    this.insumoService.listar().subscribe({
      next: (lista) => this.insumos.set(lista),
      error: () => this.error.set('No se pudieron cargar los insumos.'),
    });
  }

  private cargarReceta(id: number) {
    this.cargando.set(true);
    this.recetaService.obtener(id).subscribe({
      next: (receta: Receta) => {
        this.form.patchValue({
          flavorId: receta.saborId,
          nombre: receta.nombre,
          cantidadBase: receta.cantidadBase,
        });

        this.ingredientes.clear();
        if (receta.ingredientes.length > 0) {
          for (const ing of receta.ingredientes) {
            this.ingredientes.push(
              this.crearFilaIngrediente(
                ing.stockId,
                ing.cantidadRequerida,
                ing.unidad,
              ),
            );
          }
        } else {
          this.agregarIngrediente();
        }
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudo cargar la receta solicitada.');
        this.cargando.set(false);
      },
    });
  }

  private crearFilaIngrediente(
    stockId: number | null = null,
    cantidadRequerida: number | null = null,
    unidad = '',
  ): FormGroup {
    const grupo = this.fb.group({
      stockId: [stockId, Validators.required],
      cantidadRequerida: [
        cantidadRequerida,
        [Validators.required, Validators.min(0.001)],
      ],
      unidad: [unidad],
    });

    grupo.get('stockId')!.valueChanges.subscribe((nuevoId) => {
      const insumo = this.insumos().find((i) => i.id === Number(nuevoId));
      grupo.get('unidad')!.setValue(insumo ? insumo.unidad : '', {
        emitEvent: false,
      });
    });

    return grupo;
  }

  agregarIngrediente() {
    this.ingredientes.push(this.crearFilaIngrediente());
  }

  quitarIngrediente(index: number) {
    if (this.ingredientes.length > 1) {
      this.ingredientes.removeAt(index);
    }
  }

  alCambiarSabor() {
    const flavorId = Number(this.form.value.flavorId);
    const sabor = this.sabores().find((s) => s.id === flavorId);
    const nombreActual = (this.form.value.nombre ?? '').trim();

    if (sabor && !nombreActual) {
      this.form.patchValue({ nombre: sabor.nombre });
    }
  }

  unidadDeFila(index: number): string {
    const fila = this.ingredientes.at(index);
    const stockId = Number(fila.value.stockId);
    const insumo = this.insumos().find((i) => i.id === stockId);
    const codigo = insumo?.unidad || fila.value.unidad || '';
    return codigo ? this.etiquetaUnidad(codigo) : 'Se autocompleta al elegir insumo';
  }

  guardar() {
    if (this.guardando()) return;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.error.set(
        'Completá todos los campos obligatorios (*) antes de guardar la receta.',
      );
      return;
    }

    if (this.ingredientes.length === 0) {
      this.error.set('Debés agregar al menos un ingrediente a la receta.');
      return;
    }

    const raw = this.form.getRawValue();
    const filas = raw.ingredientes ?? [];

    // Validar que no haya insumos repetidos
    const idsElegidos = filas.map((f: any) => Number(f.stockId));
    const idsUnicos = new Set(idsElegidos);
    if (idsUnicos.size !== idsElegidos.length) {
      this.error.set(
        'Hay insumos repetidos en la fórmula. Sumá la cantidad en una sola línea por insumo.',
      );
      return;
    }

    const body: RecetaRequest = {
      name: (raw.nombre ?? '').trim(),
      baseQuantity: Number(raw.cantidadBase),
      unitBase: 'KG',
      flavorId: raw.flavorId ? Number(raw.flavorId) : null,
      ingredients: filas.map((f: any) => {
        const insumo = this.insumos().find((i) => i.id === Number(f.stockId));
        return {
          stockId: Number(f.stockId),
          requiredAmount: Number(f.cantidadRequerida),
          unit: insumo?.unidad || f.unidad || 'KG',
        };
      }),
    };

    this.guardando.set(true);
    this.error.set('');

    const peticion =
      this.id === null
        ? this.recetaService.crear(body)
        : this.recetaService.modificar(this.id, body);

    peticion.subscribe({
      next: () => {
        this.guardando.set(false);
        this.router.navigate(['/recetas']);
      },
      error: (err) => {
        this.guardando.set(false);
        this.error.set(
          err?.error?.message ?? 'No se pudo guardar la receta. Verificá los datos.',
        );
      },
    });
  }

  cancelar() {
    this.router.navigate(['/recetas']);
  }
}
