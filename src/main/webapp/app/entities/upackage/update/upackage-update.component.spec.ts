jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UpackageService } from '../service/upackage.service';
import { IUpackage, Upackage } from '../upackage.model';
import { IUprocess } from 'app/entities/uprocess/uprocess.model';
import { UprocessService } from 'app/entities/uprocess/service/uprocess.service';

import { UpackageUpdateComponent } from './upackage-update.component';

describe('Component Tests', () => {
  describe('Upackage Management Update Component', () => {
    let comp: UpackageUpdateComponent;
    let fixture: ComponentFixture<UpackageUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let upackageService: UpackageService;
    let uprocessService: UprocessService;

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
      uprocessService = TestBed.inject(UprocessService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Uprocess query and add missing value', () => {
        const upackage: IUpackage = { id: 456 };
        const uprocess: IUprocess = { id: 81337 };
        upackage.uprocess = uprocess;

        const uprocessCollection: IUprocess[] = [{ id: 82039 }];
        spyOn(uprocessService, 'query').and.returnValue(of(new HttpResponse({ body: uprocessCollection })));
        const additionalUprocesses = [uprocess];
        const expectedCollection: IUprocess[] = [...additionalUprocesses, ...uprocessCollection];
        spyOn(uprocessService, 'addUprocessToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ upackage });
        comp.ngOnInit();

        expect(uprocessService.query).toHaveBeenCalled();
        expect(uprocessService.addUprocessToCollectionIfMissing).toHaveBeenCalledWith(uprocessCollection, ...additionalUprocesses);
        expect(comp.uprocessesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const upackage: IUpackage = { id: 456 };
        const uprocess: IUprocess = { id: 57137 };
        upackage.uprocess = uprocess;

        activatedRoute.data = of({ upackage });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(upackage));
        expect(comp.uprocessesSharedCollection).toContain(uprocess);
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
