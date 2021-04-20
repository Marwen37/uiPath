import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUprocess } from '../uprocess.model';
import { UprocessService } from '../service/uprocess.service';

@Component({
  templateUrl: './uprocess-delete-dialog.component.html',
})
export class UprocessDeleteDialogComponent {
  uprocess?: IUprocess;

  constructor(protected uprocessService: UprocessService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.uprocessService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
