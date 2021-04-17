import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUprocess, Uprocess } from '../uprocess.model';
import { UprocessService } from '../service/uprocess.service';

@Injectable({ providedIn: 'root' })
export class UprocessRoutingResolveService implements Resolve<IUprocess> {
  constructor(protected service: UprocessService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUprocess> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((uprocess: HttpResponse<Uprocess>) => {
          if (uprocess.body) {
            return of(uprocess.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Uprocess());
  }
}
