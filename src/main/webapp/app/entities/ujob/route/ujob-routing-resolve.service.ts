import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUjob, Ujob } from '../ujob.model';
import { UjobService } from '../service/ujob.service';

@Injectable({ providedIn: 'root' })
export class UjobRoutingResolveService implements Resolve<IUjob> {
  constructor(protected service: UjobService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUjob> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ujob: HttpResponse<Ujob>) => {
          if (ujob.body) {
            return of(ujob.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ujob());
  }
}
