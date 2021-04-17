jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IUrobot, Urobot } from '../urobot.model';
import { UrobotService } from '../service/urobot.service';

import { UrobotRoutingResolveService } from './urobot-routing-resolve.service';

describe('Service Tests', () => {
  describe('Urobot routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: UrobotRoutingResolveService;
    let service: UrobotService;
    let resultUrobot: IUrobot | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(UrobotRoutingResolveService);
      service = TestBed.inject(UrobotService);
      resultUrobot = undefined;
    });

    describe('resolve', () => {
      it('should return IUrobot returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUrobot = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUrobot).toEqual({ id: 123 });
      });

      it('should return new IUrobot if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUrobot = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultUrobot).toEqual(new Urobot());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUrobot = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUrobot).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
