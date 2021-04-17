import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UprocessComponent } from '../list/uprocess.component';
import { UprocessDetailComponent } from '../detail/uprocess-detail.component';
import { UprocessUpdateComponent } from '../update/uprocess-update.component';
import { UprocessRoutingResolveService } from './uprocess-routing-resolve.service';

const uprocessRoute: Routes = [
  {
    path: '',
    component: UprocessComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UprocessDetailComponent,
    resolve: {
      uprocess: UprocessRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UprocessUpdateComponent,
    resolve: {
      uprocess: UprocessRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UprocessUpdateComponent,
    resolve: {
      uprocess: UprocessRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(uprocessRoute)],
  exports: [RouterModule],
})
export class UprocessRoutingModule {}
