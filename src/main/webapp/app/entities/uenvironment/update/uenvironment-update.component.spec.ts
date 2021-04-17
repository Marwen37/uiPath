jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UenvironmentService } from '../service/uenvironment.service';
import { IUenvironment, Uenvironment } from '../uenvironment.model';
import { IUprocess } from 'app/entities/uprocess/uprocess.model';
import { UprocessService } from 'app/entities/uprocess/service/uprocess.service';

import { UenvironmentUpdateComponent } from './uenvironment-update.component';

describe('Component Tests', () => {
  describe('Uenvironment Management Update Component', () => {
    let comp: UenvironmentUpdateComponent;
    let fixture: ComponentFixture<UenvironmentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let uenvironmentService: UenvironmentService;
    let uprocessService: UprocessService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UenvironmentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UenvironmentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UenvironmentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      uenvironmentService = TestBed.inject(UenvironmentService);
      uprocessService = TestBed.inject(UprocessService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Uprocess query and add missing value', () => {
        const uenvironment: IUenvironment = { id: 456 };
        const uprocess: IUprocess = { id: 57886 };
        uenvironment.uprocess = uprocess;

        const uprocessCollection: IUprocess[] = [{ id: 51852 }];
        spyOn(uprocessService, 'query').and.returnValue(of(new HttpResponse({ body: uprocessCollection })));
        const additionalUprocesses = [uprocess];
        const expectedCollection: IUprocess[] = [...additionalUprocesses, ...uprocessCollection];
        spyOn(uprocessService, 'addUprocessToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ uenvironment });
        comp.ngOnInit();

        expect(uprocessService.query).toHaveBeenCalled();
        expect(uprocessService.addUprocessToCollectionIfMissing).toHaveBeenCalledWith(uprocessCollection, ...additionalUprocesses);
        expect(comp.uprocessesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const uenvironment: IUenvironment = { id: 456 };
        const uprocess: IUprocess = { id: 40112 };
        uenvironment.uprocess = uprocess;

        activatedRoute.data = of({ uenvironment });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(uenvironment));
        expect(comp.uprocessesSharedCollection).toContain(uprocess);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const uenvironment = { id: 123 };
        spyOn(uenvironmentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ uenvironment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: uenvironment }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(uenvironmentService.update).toHaveBeenCalledWith(uenvironment);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const uenvironment = new Uenvironment();
        spyOn(uenvironmentService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ uenvironment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: uenvironment }));
        saveSubject.complete();

        // THEN
        expect(uenvironmentService.create).toHaveBeenCalledWith(uenvironment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const uenvironment = { id: 123 };
        spyOn(uenvironmentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ uenvironment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(uenvironmentService.update).toHaveBeenCalledWith(uenvironment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUprocessById', () => {
        it('Should return tracked Uprocess primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUprocessById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
