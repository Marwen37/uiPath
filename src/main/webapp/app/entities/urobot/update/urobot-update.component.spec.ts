jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UrobotService } from '../service/urobot.service';
import { IUrobot, Urobot } from '../urobot.model';
import { IUenvironment } from 'app/entities/uenvironment/uenvironment.model';
import { UenvironmentService } from 'app/entities/uenvironment/service/uenvironment.service';

import { UrobotUpdateComponent } from './urobot-update.component';

describe('Component Tests', () => {
  describe('Urobot Management Update Component', () => {
    let comp: UrobotUpdateComponent;
    let fixture: ComponentFixture<UrobotUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let urobotService: UrobotService;
    let uenvironmentService: UenvironmentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UrobotUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UrobotUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UrobotUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      urobotService = TestBed.inject(UrobotService);
      uenvironmentService = TestBed.inject(UenvironmentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Uenvironment query and add missing value', () => {
        const urobot: IUrobot = { id: 456 };
        const uenvironment: IUenvironment = { id: 55058 };
        urobot.uenvironment = uenvironment;

        const uenvironmentCollection: IUenvironment[] = [{ id: 33979 }];
        spyOn(uenvironmentService, 'query').and.returnValue(of(new HttpResponse({ body: uenvironmentCollection })));
        const additionalUenvironments = [uenvironment];
        const expectedCollection: IUenvironment[] = [...additionalUenvironments, ...uenvironmentCollection];
        spyOn(uenvironmentService, 'addUenvironmentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ urobot });
        comp.ngOnInit();

        expect(uenvironmentService.query).toHaveBeenCalled();
        expect(uenvironmentService.addUenvironmentToCollectionIfMissing).toHaveBeenCalledWith(
          uenvironmentCollection,
          ...additionalUenvironments
        );
        expect(comp.uenvironmentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const urobot: IUrobot = { id: 456 };
        const uenvironment: IUenvironment = { id: 54447 };
        urobot.uenvironment = uenvironment;

        activatedRoute.data = of({ urobot });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(urobot));
        expect(comp.uenvironmentsSharedCollection).toContain(uenvironment);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const urobot = { id: 123 };
        spyOn(urobotService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ urobot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: urobot }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(urobotService.update).toHaveBeenCalledWith(urobot);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const urobot = new Urobot();
        spyOn(urobotService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ urobot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: urobot }));
        saveSubject.complete();

        // THEN
        expect(urobotService.create).toHaveBeenCalledWith(urobot);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const urobot = { id: 123 };
        spyOn(urobotService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ urobot });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(urobotService.update).toHaveBeenCalledWith(urobot);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUenvironmentById', () => {
        it('Should return tracked Uenvironment primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUenvironmentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
