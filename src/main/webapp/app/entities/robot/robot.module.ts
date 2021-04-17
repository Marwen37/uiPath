import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { RobotComponent } from './list/robot.component';
import { RobotDetailComponent } from './detail/robot-detail.component';
import { RobotUpdateComponent } from './update/robot-update.component';
import { RobotDeleteDialogComponent } from './delete/robot-delete-dialog.component';
import { RobotRoutingModule } from './route/robot-routing.module';

@NgModule({
  imports: [SharedModule, RobotRoutingModule],
  declarations: [RobotComponent, RobotDetailComponent, RobotUpdateComponent, RobotDeleteDialogComponent],
  entryComponents: [RobotDeleteDialogComponent],
})
export class RobotModule {}
