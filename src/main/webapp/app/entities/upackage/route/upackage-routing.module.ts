import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UpackageComponent } from '../list/upackage.component';
import { UpackageDetailComponent } from '../detail/upackage-detail.component';
import { UpackageUpdateComponent } from '../update/upackage-update.component';
import { UpackageRoutingResolveService } from './upackage-routing-resolve.service';

const upackageRoute: Routes = [
  {
    path: '',
    component: UpackageComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UpackageDetailComponent,
    resolve: {
      upackage: UpackageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UpackageUpdateComponent,
    resolve: {
      upackage: UpackageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UpackageUpdateComponent,
    resolve: {
      upackage: UpackageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(upackageRoute)],
  exports: [RouterModule],
})
export class UpackageRoutingModule {}
