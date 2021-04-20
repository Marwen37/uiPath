import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UenvironmentComponent } from '../list/uenvironment.component';
import { UenvironmentDetailComponent } from '../detail/uenvironment-detail.component';
import { UenvironmentUpdateComponent } from '../update/uenvironment-update.component';
import { UenvironmentRoutingResolveService } from './uenvironment-routing-resolve.service';

const uenvironmentRoute: Routes = [
  {
    path: '',
    component: UenvironmentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UenvironmentDetailComponent,
    resolve: {
      uenvironment: UenvironmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UenvironmentUpdateComponent,
    resolve: {
      uenvironment: UenvironmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UenvironmentUpdateComponent,
    resolve: {
      uenvironment: UenvironmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(uenvironmentRoute)],
  exports: [RouterModule],
})
export class UenvironmentRoutingModule {}
