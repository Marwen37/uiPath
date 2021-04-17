jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRobot, Robot } from '../robot.model';
import { RobotService } from '../service/robot.service';

import { RobotRoutingResolveService } from './robot-routing-resolve.service';

describe('Service Tests', () => {
  describe('Robot routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RobotRoutingResolveService;
    let service: RobotService;
    let resultRobot: IRobot | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RobotRoutingResolveService);
      service = TestBed.inject(RobotService);
      resultRobot = undefined;
    });

    describe('resolve', () => {
      it('should return IRobot returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRobot = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRobot).toEqual({ id: 123 });
      });

      it('should return new IRobot if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRobot = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRobot).toEqual(new Robot());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRobot = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRobot).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
