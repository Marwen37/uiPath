import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { UenvironmentComponent } from './list/uenvironment.component';
import { UenvironmentDetailComponent } from './detail/uenvironment-detail.component';
import { UenvironmentUpdateComponent } from './update/uenvironment-update.component';
import { UenvironmentDeleteDialogComponent } from './delete/uenvironment-delete-dialog.component';
import { UenvironmentRoutingModule } from './route/uenvironment-routing.module';

@NgModule({
  imports: [SharedModule, UenvironmentRoutingModule],
  declarations: [UenvironmentComponent, UenvironmentDetailComponent, UenvironmentUpdateComponent, UenvironmentDeleteDialogComponent],
  entryComponents: [UenvironmentDeleteDialogComponent],
})
export class UenvironmentModule {}
