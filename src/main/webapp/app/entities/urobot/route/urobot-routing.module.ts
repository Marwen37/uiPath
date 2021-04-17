import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UrobotComponent } from '../list/urobot.component';
import { UrobotDetailComponent } from '../detail/urobot-detail.component';
import { UrobotUpdateComponent } from '../update/urobot-update.component';
import { UrobotRoutingResolveService } from './urobot-routing-resolve.service';

const urobotRoute: Routes = [
  {
    path: '',
    component: UrobotComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UrobotDetailComponent,
    resolve: {
      urobot: UrobotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UrobotUpdateComponent,
    resolve: {
      urobot: UrobotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UrobotUpdateComponent,
    resolve: {
      urobot: UrobotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(urobotRoute)],
  exports: [RouterModule],
})
export class UrobotRoutingModule {}
