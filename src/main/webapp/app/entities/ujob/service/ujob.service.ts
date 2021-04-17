import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUjob, getUjobIdentifier } from '../ujob.model';

export type EntityResponseType = HttpResponse<IUjob>;
export type EntityArrayResponseType = HttpResponse<IUjob[]>;

@Injectable({ providedIn: 'root' })
export class UjobService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/ujobs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(ujob: IUjob): Observable<EntityResponseType> {
    return this.http.post<IUjob>(this.resourceUrl, ujob, { observe: 'response' });
  }

  update(ujob: IUjob): Observable<EntityResponseType> {
    return this.http.put<IUjob>(`${this.resourceUrl}/${getUjobIdentifier(ujob) as number}`, ujob, { observe: 'response' });
  }

  partialUpdate(ujob: IUjob): Observable<EntityResponseType> {
    return this.http.patch<IUjob>(`${this.resourceUrl}/${getUjobIdentifier(ujob) as number}`, ujob, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUjob>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUjob[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUjobToCollectionIfMissing(ujobCollection: IUjob[], ...ujobsToCheck: (IUjob | null | undefined)[]): IUjob[] {
    const ujobs: IUjob[] = ujobsToCheck.filter(isPresent);
    if (ujobs.length > 0) {
      const ujobCollectionIdentifiers = ujobCollection.map(ujobItem => getUjobIdentifier(ujobItem)!);
      const ujobsToAdd = ujobs.filter(ujobItem => {
        const ujobIdentifier = getUjobIdentifier(ujobItem);
        if (ujobIdentifier == null || ujobCollectionIdentifiers.includes(ujobIdentifier)) {
          return false;
        }
        ujobCollectionIdentifiers.push(ujobIdentifier);
        return true;
      });
      return [...ujobsToAdd, ...ujobCollection];
    }
    return ujobCollection;
  }
}
