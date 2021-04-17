import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUenvironment, Uenvironment } from '../uenvironment.model';
import { UenvironmentService } from '../service/uenvironment.service';

@Injectable({ providedIn: 'root' })
export class UenvironmentRoutingResolveService implements Resolve<IUenvironment> {
  constructor(protected service: UenvironmentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUenvironment> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((uenvironment: HttpResponse<Uenvironment>) => {
          if (uenvironment.body) {
            return of(uenvironment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Uenvironment());
  }
}
