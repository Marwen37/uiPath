import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRobot, getRobotIdentifier } from '../robot.model';

export type EntityResponseType = HttpResponse<IRobot>;
export type EntityArrayResponseType = HttpResponse<IRobot[]>;

@Injectable({ providedIn: 'root' })
export class RobotService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/robots');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(robot: IRobot): Observable<EntityResponseType> {
    return this.http.post<IRobot>(this.resourceUrl, robot, { observe: 'response' });
  }

  update(robot: IRobot): Observable<EntityResponseType> {
    return this.http.put<IRobot>(`${this.resourceUrl}/${getRobotIdentifier(robot) as number}`, robot, { observe: 'response' });
  }

  partialUpdate(robot: IRobot): Observable<EntityResponseType> {
    return this.http.patch<IRobot>(`${this.resourceUrl}/${getRobotIdentifier(robot) as number}`, robot, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRobot>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRobot[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRobotToCollectionIfMissing(robotCollection: IRobot[], ...robotsToCheck: (IRobot | null | undefined)[]): IRobot[] {
    const robots: IRobot[] = robotsToCheck.filter(isPresent);
    if (robots.length > 0) {
      const robotCollectionIdentifiers = robotCollection.map(robotItem => getRobotIdentifier(robotItem)!);
      const robotsToAdd = robots.filter(robotItem => {
        const robotIdentifier = getRobotIdentifier(robotItem);
        if (robotIdentifier == null || robotCollectionIdentifiers.includes(robotIdentifier)) {
          return false;
        }
        robotCollectionIdentifiers.push(robotIdentifier);
        return true;
      });
      return [...robotsToAdd, ...robotCollection];
    }
    return robotCollection;
  }
}
