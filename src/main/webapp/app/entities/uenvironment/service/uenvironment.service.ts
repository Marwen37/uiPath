import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUenvironment, getUenvironmentIdentifier } from '../uenvironment.model';

export type EntityResponseType = HttpResponse<IUenvironment>;
export type EntityArrayResponseType = HttpResponse<IUenvironment[]>;

@Injectable({ providedIn: 'root' })
export class UenvironmentService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/uenvironments');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(uenvironment: IUenvironment): Observable<EntityResponseType> {
    return this.http.post<IUenvironment>(this.resourceUrl, uenvironment, { observe: 'response' });
  }

  update(uenvironment: IUenvironment): Observable<EntityResponseType> {
    return this.http.put<IUenvironment>(`${this.resourceUrl}/${getUenvironmentIdentifier(uenvironment) as number}`, uenvironment, {
      observe: 'response',
    });
  }

  partialUpdate(uenvironment: IUenvironment): Observable<EntityResponseType> {
    return this.http.patch<IUenvironment>(`${this.resourceUrl}/${getUenvironmentIdentifier(uenvironment) as number}`, uenvironment, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUenvironment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUenvironment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUenvironmentToCollectionIfMissing(
    uenvironmentCollection: IUenvironment[],
    ...uenvironmentsToCheck: (IUenvironment | null | undefined)[]
  ): IUenvironment[] {
    const uenvironments: IUenvironment[] = uenvironmentsToCheck.filter(isPresent);
    if (uenvironments.length > 0) {
      const uenvironmentCollectionIdentifiers = uenvironmentCollection.map(
        uenvironmentItem => getUenvironmentIdentifier(uenvironmentItem)!
      );
      const uenvironmentsToAdd = uenvironments.filter(uenvironmentItem => {
        const uenvironmentIdentifier = getUenvironmentIdentifier(uenvironmentItem);
        if (uenvironmentIdentifier == null || uenvironmentCollectionIdentifiers.includes(uenvironmentIdentifier)) {
          return false;
        }
        uenvironmentCollectionIdentifiers.push(uenvironmentIdentifier);
        return true;
      });
      return [...uenvironmentsToAdd, ...uenvironmentCollection];
    }
    return uenvironmentCollection;
  }
}
