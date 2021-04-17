import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUrobot } from '../urobot.model';
import { UrobotService } from '../service/urobot.service';

@Component({
  templateUrl: './urobot-delete-dialog.component.html',
})
export class UrobotDeleteDialogComponent {
  urobot?: IUrobot;

  constructor(protected urobotService: UrobotService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.urobotService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
