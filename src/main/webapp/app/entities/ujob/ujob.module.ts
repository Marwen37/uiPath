import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { UjobComponent } from './list/ujob.component';
import { UjobDetailComponent } from './detail/ujob-detail.component';
import { UjobUpdateComponent } from './update/ujob-update.component';
import { UjobDeleteDialogComponent } from './delete/ujob-delete-dialog.component';
import { UjobRoutingModule } from './route/ujob-routing.module';

@NgModule({
  imports: [SharedModule, UjobRoutingModule],
  declarations: [UjobComponent, UjobDetailComponent, UjobUpdateComponent, UjobDeleteDialogComponent],
  entryComponents: [UjobDeleteDialogComponent],
})
export class UjobModule {}
