import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { UrobotComponent } from './list/urobot.component';
import { UrobotDetailComponent } from './detail/urobot-detail.component';
import { UrobotUpdateComponent } from './update/urobot-update.component';
import { UrobotDeleteDialogComponent } from './delete/urobot-delete-dialog.component';
import { UrobotRoutingModule } from './route/urobot-routing.module';

@NgModule({
  imports: [SharedModule, UrobotRoutingModule],
  declarations: [UrobotComponent, UrobotDetailComponent, UrobotUpdateComponent, UrobotDeleteDialogComponent],
  entryComponents: [UrobotDeleteDialogComponent],
})
export class UrobotModule {}
