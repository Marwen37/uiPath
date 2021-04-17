import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUpackage, Upackage } from '../upackage.model';
import { UpackageService } from '../service/upackage.service';

@Injectable({ providedIn: 'root' })
export class UpackageRoutingResolveService implements Resolve<IUpackage> {
  constructor(protected service: UpackageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUpackage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((upackage: HttpResponse<Upackage>) => {
          if (upackage.body) {
            return of(upackage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Upackage());
  }
}
