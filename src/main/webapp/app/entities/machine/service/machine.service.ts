import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMachine, getMachineIdentifier } from '../machine.model';

export type EntityResponseType = HttpResponse<IMachine>;
export type EntityArrayResponseType = HttpResponse<IMachine[]>;

@Injectable({ providedIn: 'root' })
export class MachineService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/machines');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(machine: IMachine): Observable<EntityResponseType> {
    return this.http.post<IMachine>(this.resourceUrl, machine, { observe: 'response' });
  }

  update(machine: IMachine): Observable<EntityResponseType> {
    return this.http.put<IMachine>(`${this.resourceUrl}/${getMachineIdentifier(machine) as number}`, machine, { observe: 'response' });
  }

  partialUpdate(machine: IMachine): Observable<EntityResponseType> {
    return this.http.patch<IMachine>(`${this.resourceUrl}/${getMachineIdentifier(machine) as number}`, machine, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMachine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMachine[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMachineToCollectionIfMissing(machineCollection: IMachine[], ...machinesToCheck: (IMachine | null | undefined)[]): IMachine[] {
    const machines: IMachine[] = machinesToCheck.filter(isPresent);
    if (machines.length > 0) {
      const machineCollectionIdentifiers = machineCollection.map(machineItem => getMachineIdentifier(machineItem)!);
      const machinesToAdd = machines.filter(machineItem => {
        const machineIdentifier = getMachineIdentifier(machineItem);
        if (machineIdentifier == null || machineCollectionIdentifiers.includes(machineIdentifier)) {
          return false;
        }
        machineCollectionIdentifiers.push(machineIdentifier);
        return true;
      });
      return [...machinesToAdd, ...machineCollection];
    }
    return machineCollection;
  }
}
