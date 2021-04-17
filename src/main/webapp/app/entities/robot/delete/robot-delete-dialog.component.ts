import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRobot } from '../robot.model';
import { RobotService } from '../service/robot.service';

@Component({
  templateUrl: './robot-delete-dialog.component.html',
})
export class RobotDeleteDialogComponent {
  robot?: IRobot;

  constructor(protected robotService: RobotService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.robotService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
