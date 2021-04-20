import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUpackage, getUpackageIdentifier } from '../upackage.model';

export type EntityResponseType = HttpResponse<IUpackage>;
export type EntityArrayResponseType = HttpResponse<IUpackage[]>;

@Injectable({ providedIn: 'root' })
export class UpackageService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/upackages');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(upackage: IUpackage): Observable<EntityResponseType> {
    return this.http.post<IUpackage>(this.resourceUrl, upackage, { observe: 'response' });
  }

  update(upackage: IUpackage): Observable<EntityResponseType> {
    return this.http.put<IUpackage>(`${this.resourceUrl}/${getUpackageIdentifier(upackage) as number}`, upackage, { observe: 'response' });
  }

  partialUpdate(upackage: IUpackage): Observable<EntityResponseType> {
    return this.http.patch<IUpackage>(`${this.resourceUrl}/${getUpackageIdentifier(upackage) as number}`, upackage, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUpackage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUpackage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUpackageToCollectionIfMissing(upackageCollection: IUpackage[], ...upackagesToCheck: (IUpackage | null | undefined)[]): IUpackage[] {
    const upackages: IUpackage[] = upackagesToCheck.filter(isPresent);
    if (upackages.length > 0) {
      const upackageCollectionIdentifiers = upackageCollection.map(upackageItem => getUpackageIdentifier(upackageItem)!);
      const upackagesToAdd = upackages.filter(upackageItem => {
        const upackageIdentifier = getUpackageIdentifier(upackageItem);
        if (upackageIdentifier == null || upackageCollectionIdentifiers.includes(upackageIdentifier)) {
          return false;
        }
        upackageCollectionIdentifiers.push(upackageIdentifier);
        return true;
      });
      return [...upackagesToAdd, ...upackageCollection];
    }
    return upackageCollection;
  }
}
