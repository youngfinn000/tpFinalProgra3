import { Component, input, output } from '@angular/core';
import { ModalComponent } from '../modal/modal.component';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [ModalComponent],
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.css',
})
export class ConfirmDialogComponent {
  readonly abierto = input<boolean>(false);
  readonly titulo = input<string>('¿Estás seguro?');
  readonly mensaje = input<string>('Esta acción no se puede deshacer.');
  readonly textoConfirmar = input<string>('Confirmar');
  readonly textoCancelar = input<string>('Cancelar');
  readonly tipo = input<'danger' | 'warning' | 'primary'>('danger');
  readonly cargando = input<boolean>(false);

  readonly confirmar = output<void>();
  readonly cancelar = output<void>();

  onConfirmar(): void {
    if (!this.cargando()) {
      this.confirmar.emit();
    }
  }

  onCancelar(): void {
    if (!this.cargando()) {
      this.cancelar.emit();
    }
  }
}

