import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUenvironment } from '../uenvironment.model';
import { UenvironmentService } from '../service/uenvironment.service';

@Component({
  templateUrl: './uenvironment-delete-dialog.component.html',
})
export class UenvironmentDeleteDialogComponent {
  uenvironment?: IUenvironment;

  constructor(protected uenvironmentService: UenvironmentService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.uenvironmentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
