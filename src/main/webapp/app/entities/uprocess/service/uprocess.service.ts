import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUprocess, getUprocessIdentifier } from '../uprocess.model';

export type EntityResponseType = HttpResponse<IUprocess>;
export type EntityArrayResponseType = HttpResponse<IUprocess[]>;

@Injectable({ providedIn: 'root' })
export class UprocessService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/uprocesses');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(uprocess: IUprocess): Observable<EntityResponseType> {
    return this.http.post<IUprocess>(this.resourceUrl, uprocess, { observe: 'response' });
  }

  update(uprocess: IUprocess): Observable<EntityResponseType> {
    return this.http.put<IUprocess>(`${this.resourceUrl}/${getUprocessIdentifier(uprocess) as number}`, uprocess, { observe: 'response' });
  }

  partialUpdate(uprocess: IUprocess): Observable<EntityResponseType> {
    return this.http.patch<IUprocess>(`${this.resourceUrl}/${getUprocessIdentifier(uprocess) as number}`, uprocess, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUprocess>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUprocess[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUprocessToCollectionIfMissing(uprocessCollection: IUprocess[], ...uprocessesToCheck: (IUprocess | null | undefined)[]): IUprocess[] {
    const uprocesses: IUprocess[] = uprocessesToCheck.filter(isPresent);
    if (uprocesses.length > 0) {
      const uprocessCollectionIdentifiers = uprocessCollection.map(uprocessItem => getUprocessIdentifier(uprocessItem)!);
      const uprocessesToAdd = uprocesses.filter(uprocessItem => {
        const uprocessIdentifier = getUprocessIdentifier(uprocessItem);
        if (uprocessIdentifier == null || uprocessCollectionIdentifiers.includes(uprocessIdentifier)) {
          return false;
        }
        uprocessCollectionIdentifiers.push(uprocessIdentifier);
        return true;
      });
      return [...uprocessesToAdd, ...uprocessCollection];
    }
    return uprocessCollection;
  }
}
