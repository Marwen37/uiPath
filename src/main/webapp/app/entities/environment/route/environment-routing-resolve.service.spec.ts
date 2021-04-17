jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEnvironment, Environment } from '../environment.model';
import { EnvironmentService } from '../service/environment.service';

import { EnvironmentRoutingResolveService } from './environment-routing-resolve.service';

describe('Service Tests', () => {
  describe('Environment routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EnvironmentRoutingResolveService;
    let service: EnvironmentService;
    let resultEnvironment: IEnvironment | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EnvironmentRoutingResolveService);
      service = TestBed.inject(EnvironmentService);
      resultEnvironment = undefined;
    });

    describe('resolve', () => {
      it('should return IEnvironment returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEnvironment = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEnvironment).toEqual({ id: 123 });
      });

      it('should return new IEnvironment if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEnvironment = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEnvironment).toEqual(new Environment());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEnvironment = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEnvironment).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
