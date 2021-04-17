import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RobotComponent } from '../list/robot.component';
import { RobotDetailComponent } from '../detail/robot-detail.component';
import { RobotUpdateComponent } from '../update/robot-update.component';
import { RobotRoutingResolveService } from './robot-routing-resolve.service';

const robotRoute: Routes = [
  {
    path: '',
    component: RobotComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RobotDetailComponent,
    resolve: {
      robot: RobotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RobotUpdateComponent,
    resolve: {
      robot: RobotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RobotUpdateComponent,
    resolve: {
      robot: RobotRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(robotRoute)],
  exports: [RouterModule],
})
export class RobotRoutingModule {}
