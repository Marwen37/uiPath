import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { UprocessComponent } from './list/uprocess.component';
import { UprocessDetailComponent } from './detail/uprocess-detail.component';
import { UprocessUpdateComponent } from './update/uprocess-update.component';
import { UprocessDeleteDialogComponent } from './delete/uprocess-delete-dialog.component';
import { UprocessRoutingModule } from './route/uprocess-routing.module';

@NgModule({
  imports: [SharedModule, UprocessRoutingModule],
  declarations: [UprocessComponent, UprocessDetailComponent, UprocessUpdateComponent, UprocessDeleteDialogComponent],
  entryComponents: [UprocessDeleteDialogComponent],
})
export class UprocessModule {}
