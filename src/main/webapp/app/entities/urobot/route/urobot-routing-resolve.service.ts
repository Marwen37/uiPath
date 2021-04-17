import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUrobot, Urobot } from '../urobot.model';
import { UrobotService } from '../service/urobot.service';

@Injectable({ providedIn: 'root' })
export class UrobotRoutingResolveService implements Resolve<IUrobot> {
  constructor(protected service: UrobotService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUrobot> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((urobot: HttpResponse<Urobot>) => {
          if (urobot.body) {
            return of(urobot.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Urobot());
  }
}
