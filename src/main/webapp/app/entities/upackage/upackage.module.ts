import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { UpackageComponent } from './list/upackage.component';
import { UpackageDetailComponent } from './detail/upackage-detail.component';
import { UpackageUpdateComponent } from './update/upackage-update.component';
import { UpackageDeleteDialogComponent } from './delete/upackage-delete-dialog.component';
import { UpackageRoutingModule } from './route/upackage-routing.module';

@NgModule({
  imports: [SharedModule, UpackageRoutingModule],
  declarations: [UpackageComponent, UpackageDetailComponent, UpackageUpdateComponent, UpackageDeleteDialogComponent],
  entryComponents: [UpackageDeleteDialogComponent],
})
export class UpackageModule {}
