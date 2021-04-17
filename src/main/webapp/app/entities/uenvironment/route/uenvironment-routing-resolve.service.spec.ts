jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IUenvironment, Uenvironment } from '../uenvironment.model';
import { UenvironmentService } from '../service/uenvironment.service';

import { UenvironmentRoutingResolveService } from './uenvironment-routing-resolve.service';

describe('Service Tests', () => {
  describe('Uenvironment routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: UenvironmentRoutingResolveService;
    let service: UenvironmentService;
    let resultUenvironment: IUenvironment | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(UenvironmentRoutingResolveService);
      service = TestBed.inject(UenvironmentService);
      resultUenvironment = undefined;
    });

    describe('resolve', () => {
      it('should return IUenvironment returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUenvironment = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUenvironment).toEqual({ id: 123 });
      });

      it('should return new IUenvironment if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUenvironment = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultUenvironment).toEqual(new Uenvironment());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUenvironment = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUenvironment).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
