jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EnvironmentService } from '../service/environment.service';
import { IEnvironment, Environment } from '../environment.model';
import { IProcess } from 'app/entities/process/process.model';
import { ProcessService } from 'app/entities/process/service/process.service';

import { EnvironmentUpdateComponent } from './environment-update.component';

describe('Component Tests', () => {
  describe('Environment Management Update Component', () => {
    let comp: EnvironmentUpdateComponent;
    let fixture: ComponentFixture<EnvironmentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let environmentService: EnvironmentService;
    let processService: ProcessService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EnvironmentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EnvironmentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EnvironmentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      environmentService = TestBed.inject(EnvironmentService);
      processService = TestBed.inject(ProcessService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Process query and add missing value', () => {
        const environment: IEnvironment = { id: 456 };
        const process: IProcess = { id: 48603 };
        environment.process = process;
        const process: IProcess = { id: 34053 };
        environment.process = process;

        const processCollection: IProcess[] = [{ id: 20273 }];
        spyOn(processService, 'query').and.returnValue(of(new HttpResponse({ body: processCollection })));
        const additionalProcesses = [process, process];
        const expectedCollection: IProcess[] = [...additionalProcesses, ...processCollection];
        spyOn(processService, 'addProcessToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ environment });
        comp.ngOnInit();

        expect(processService.query).toHaveBeenCalled();
        expect(processService.addProcessToCollectionIfMissing).toHaveBeenCalledWith(processCollection, ...additionalProcesses);
        expect(comp.processesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const environment: IEnvironment = { id: 456 };
        const process: IProcess = { id: 69171 };
        environment.process = process;
        const process: IProcess = { id: 29999 };
        environment.process = process;

        activatedRoute.data = of({ environment });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(environment));
        expect(comp.processesSharedCollection).toContain(process);
        expect(comp.processesSharedCollection).toContain(process);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const environment = { id: 123 };
        spyOn(environmentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ environment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: environment }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(environmentService.update).toHaveBeenCalledWith(environment);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const environment = new Environment();
        spyOn(environmentService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ environment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: environment }));
        saveSubject.complete();

        // THEN
        expect(environmentService.create).toHaveBeenCalledWith(environment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const environment = { id: 123 };
        spyOn(environmentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ environment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(environmentService.update).toHaveBeenCalledWith(environment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackProcessById', () => {
        it('Should return tracked Process primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProcessById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
