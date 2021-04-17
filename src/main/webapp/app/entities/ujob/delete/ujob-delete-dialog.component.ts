import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUjob } from '../ujob.model';
import { UjobService } from '../service/ujob.service';

@Component({
  templateUrl: './ujob-delete-dialog.component.html',
})
export class UjobDeleteDialogComponent {
  ujob?: IUjob;

  constructor(protected ujobService: UjobService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ujobService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
