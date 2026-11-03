import { Component, effect, inject, input, output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ModalComponent } from '../../../../shared/components/modal/modal.component';
import { Empleado, EmpleadoRequest, RolEmpleado } from '../../models/empleado.model';

@Component({
  selector: 'app-empleado-form-modal',
  standalone: true,
  imports: [ReactiveFormsModule, ModalComponent],
  templateUrl: './empleado-form-modal.component.html',
  styleUrl: './empleado-form-modal.component.css',
})
export class EmpleadoFormModalComponent {
  private fb = inject(FormBuilder);

  readonly abierto = input<boolean>(false);
  readonly modo = input<'crear' | 'editar'>('crear');
  readonly empleado = input<Empleado | null>(null);
  readonly guardando = input<boolean>(false);
  readonly error = input<string>('');

  readonly guardar = output<EmpleadoRequest>();
  readonly cerrar = output<void>();

  empleadoForm: FormGroup = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(50)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4)]],
    rol: ['EMPLOYEE' as RolEmpleado, [Validators.required]],
  });

  constructor() {
    effect(() => {
      if (this.abierto()) {
        const emp = this.empleado();
        if (this.modo() === 'editar' && emp) {
          this.empleadoForm.reset({
            name: emp.name,
            email: emp.email,
            password: 'dummyPassword123',
            rol: emp.rol,
          });
          this.empleadoForm.get('password')?.clearValidators();
          this.empleadoForm.get('password')?.updateValueAndValidity();
        } else {
          this.empleadoForm.reset({
            name: '',
            email: '',
            password: '',
            rol: 'EMPLOYEE',
          });
          this.empleadoForm.get('password')?.setValidators([Validators.required, Validators.minLength(4)]);
          this.empleadoForm.get('password')?.updateValueAndValidity();
        }
      }
    });
  }

  onSubmit(): void {
    if (this.empleadoForm.invalid) {
      this.empleadoForm.markAllAsTouched();
      return;
    }

    const val = this.empleadoForm.value;
    const request: EmpleadoRequest = {
      name: val.name.trim(),
      email: val.email.trim(),
      passwordHash: val.password || 'password123',
      rol: val.rol,
    };

    this.guardar.emit(request);
  }

  onCerrar(): void {
    this.cerrar.emit();
  }
}

