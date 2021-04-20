import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUpackage } from '../upackage.model';
import { UpackageService } from '../service/upackage.service';

@Component({
  templateUrl: './upackage-delete-dialog.component.html',
})
export class UpackageDeleteDialogComponent {
  upackage?: IUpackage;

  constructor(protected upackageService: UpackageService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.upackageService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
