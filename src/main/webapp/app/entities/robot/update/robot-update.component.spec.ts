jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RobotService } from '../service/robot.service';
import { IRobot, Robot } from '../robot.model';
import { IEnvironment } from 'app/entities/environment/environment.model';
import { EnvironmentService } from 'app/entities/environment/service/environment.service';

import { RobotUpdateComponent } from './robot-update.component';

describe('Component Tests', () => {
  describe('Robot Management Update Component', () => {
    let comp: RobotUpdateComponent;
    let fixture: ComponentFixture<RobotUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let robotService: RobotService;
    let environmentService: EnvironmentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RobotUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RobotUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RobotUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      robotService = TestBed.inject(RobotService);
      environmentService = TestBed.inject(EnvironmentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Environment query and add missing value', () => {
        const robot: IRobot = { id: 456 };
        const environment: IEnvironment = { id: 14208 };
        robot.environment = environment;
        const environment: IEnvironment = { id: 55865 };
        robot.environment = environment;

        const environmentCollection: IEnvironment[] = [{ id: 64725 }];
        spyOn(environmentService, 'query').and.returnValue(of(new HttpResponse({ body: environmentCollection })));
        const additionalEnvironments = [environment, environment];
        const expectedCollection: IEnvironment[] = [...additionalEnvironments, ...environmentCollection];
        spyOn(environmentService, 'addEnvironmentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ robot });
        comp.ngOnInit();

        expect(environmentService.query).toHaveBeenCalled();
        expect(environmentService.addEnvironmentToCollectionIfMissing).toHaveBeenCalledWith(
          environmentCollection,
          ...additionalEnvironments
        );
        expect(comp.environmentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const robot: IRobot = { id: 456 };
        const environment: IEnvironment = { id: 24853 };
        robot.environment = environment;
        const environment: IEnvironment = { id: 37643 };
        robot.environment = environment;

        activatedRoute.data = of({ robot });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(robot));
        expect(comp.environmentsSharedCollection).toContain(environment);
        expect(comp.environmentsSharedCollection).toContain(environment);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const robot = { id: 123 };
        spyOn(robotService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ robot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: robot }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(robotService.update).toHaveBeenCalledWith(robot);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const robot = new Robot();
        spyOn(robotService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ robot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: robot }));
        saveSubject.complete();

        // THEN
        expect(robotService.create).toHaveBeenCalledWith(robot);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const robot = { id: 123 };
        spyOn(robotService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ robot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(robotService.update).toHaveBeenCalledWith(robot);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEnvironmentById', () => {
        it('Should return tracked Environment primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEnvironmentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
