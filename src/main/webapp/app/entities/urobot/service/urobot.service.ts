import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUrobot, getUrobotIdentifier } from '../urobot.model';

export type EntityResponseType = HttpResponse<IUrobot>;
export type EntityArrayResponseType = HttpResponse<IUrobot[]>;

@Injectable({ providedIn: 'root' })
export class UrobotService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/urobots');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(urobot: IUrobot): Observable<EntityResponseType> {
    return this.http.post<IUrobot>(this.resourceUrl, urobot, { observe: 'response' });
  }

  update(urobot: IUrobot): Observable<EntityResponseType> {
    return this.http.put<IUrobot>(`${this.resourceUrl}/${getUrobotIdentifier(urobot) as number}`, urobot, { observe: 'response' });
  }

  partialUpdate(urobot: IUrobot): Observable<EntityResponseType> {
    return this.http.patch<IUrobot>(`${this.resourceUrl}/${getUrobotIdentifier(urobot) as number}`, urobot, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUrobot>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUrobot[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUrobotToCollectionIfMissing(urobotCollection: IUrobot[], ...urobotsToCheck: (IUrobot | null | undefined)[]): IUrobot[] {
    const urobots: IUrobot[] = urobotsToCheck.filter(isPresent);
    if (urobots.length > 0) {
      const urobotCollectionIdentifiers = urobotCollection.map(urobotItem => getUrobotIdentifier(urobotItem)!);
      const urobotsToAdd = urobots.filter(urobotItem => {
        const urobotIdentifier = getUrobotIdentifier(urobotItem);
        if (urobotIdentifier == null || urobotCollectionIdentifiers.includes(urobotIdentifier)) {
          return false;
        }
        urobotCollectionIdentifiers.push(urobotIdentifier);
        return true;
      });
      return [...urobotsToAdd, ...urobotCollection];
    }
    return urobotCollection;
  }
}
