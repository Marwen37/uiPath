import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRobot, Robot } from '../robot.model';
import { RobotService } from '../service/robot.service';

@Injectable({ providedIn: 'root' })
export class RobotRoutingResolveService implements Resolve<IRobot> {
  constructor(protected service: RobotService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRobot> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((robot: HttpResponse<Robot>) => {
          if (robot.body) {
            return of(robot.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Robot());
  }
}
