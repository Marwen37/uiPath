import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UjobComponent } from '../list/ujob.component';
import { UjobDetailComponent } from '../detail/ujob-detail.component';
import { UjobUpdateComponent } from '../update/ujob-update.component';
import { UjobRoutingResolveService } from './ujob-routing-resolve.service';

const ujobRoute: Routes = [
  {
    path: '',
    component: UjobComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UjobDetailComponent,
    resolve: {
      ujob: UjobRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UjobUpdateComponent,
    resolve: {
      ujob: UjobRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UjobUpdateComponent,
    resolve: {
      ujob: UjobRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ujobRoute)],
  exports: [RouterModule],
})
export class UjobRoutingModule {}
