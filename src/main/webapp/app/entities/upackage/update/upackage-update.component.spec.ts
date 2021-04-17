jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UpackageService } from '../service/upackage.service';
import { IUpackage, Upackage } from '../upackage.model';
import { IProcess } from 'app/entities/process/process.model';
import { ProcessService } from 'app/entities/process/service/process.service';

import { UpackageUpdateComponent } from './upackage-update.component';

describe('Component Tests', () => {
  describe('Upackage Management Update Component', () => {
    let comp: UpackageUpdateComponent;
    let fixture: ComponentFixture<UpackageUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let upackageService: UpackageService;
    let processService: ProcessService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UpackageUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UpackageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UpackageUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      upackageService = TestBed.inject(UpackageService);
      processService = TestBed.inject(ProcessService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Process query and add missing value', () => {
        const upackage: IUpackage = { id: 456 };
        const process: IProcess = { id: 65240 };
        upackage.process = process;
        const process: IProcess = { id: 23497 };
        upackage.process = process;

        const processCollection: IProcess[] = [{ id: 7111 }];
        spyOn(processService, 'query').and.returnValue(of(new HttpResponse({ body: processCollection })));
        const additionalProcesses = [process, process];
        const expectedCollection: IProcess[] = [...additionalProcesses, ...processCollection];
        spyOn(processService, 'addProcessToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ upackage });
        comp.ngOnInit();

        expect(processService.query).toHaveBeenCalled();
        expect(processService.addProcessToCollectionIfMissing).toHaveBeenCalledWith(processCollection, ...additionalProcesses);
        expect(comp.processesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const upackage: IUpackage = { id: 456 };
        const process: IProcess = { id: 89631 };
        upackage.process = process;
        const process: IProcess = { id: 4228 };
        upackage.process = process;

        activatedRoute.data = of({ upackage });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(upackage));
        expect(comp.processesSharedCollection).toContain(process);
        expect(comp.processesSharedCollection).toContain(process);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const upackage = { id: 123 };
        spyOn(upackageService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ upackage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: upackage }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(upackageService.update).toHaveBeenCalledWith(upackage);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const upackage = new Upackage();
        spyOn(upackageService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ upackage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: upackage }));
        saveSubject.complete();

        // THEN
        expect(upackageService.create).toHaveBeenCalledWith(upackage);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const upackage = { id: 123 };
        spyOn(upackageService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ upackage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(upackageService.update).toHaveBeenCalledWith(upackage);
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
