import { Component, HostListener, input, output } from '@angular/core';

@Component({
  selector: 'app-modal',
  standalone: true,
  templateUrl: './modal.component.html',
  styleUrl: './modal.component.css',
})
export class ModalComponent {
  readonly abierto = input<boolean>(false);
  readonly titulo = input<string>('');
  readonly anchoMaximo = input<string>('540px');

  readonly cerrar = output<void>();

  @HostListener('document:keydown.escape')
  onEscape(): void {
    if (this.abierto()) {
      this.onCerrar();
    }
  }

  onCerrar(): void {
    this.cerrar.emit();
  }
}

