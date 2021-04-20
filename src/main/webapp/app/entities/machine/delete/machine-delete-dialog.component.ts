import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMachine } from '../machine.model';
import { MachineService } from '../service/machine.service';

@Component({
  templateUrl: './machine-delete-dialog.component.html',
})
export class MachineDeleteDialogComponent {
  machine?: IMachine;

  constructor(protected machineService: MachineService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.machineService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
