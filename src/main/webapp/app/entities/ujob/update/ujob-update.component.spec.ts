jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UjobService } from '../service/ujob.service';
import { IUjob, Ujob } from '../ujob.model';

import { UjobUpdateComponent } from './ujob-update.component';

describe('Component Tests', () => {
  describe('Ujob Management Update Component', () => {
    let comp: UjobUpdateComponent;
    let fixture: ComponentFixture<UjobUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ujobService: UjobService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UjobUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UjobUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UjobUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ujobService = TestBed.inject(UjobService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const ujob: IUjob = { id: 456 };

        activatedRoute.data = of({ ujob });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ujob));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ujob = { id: 123 };
        spyOn(ujobService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ujob });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ujob }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ujobService.update).toHaveBeenCalledWith(ujob);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ujob = new Ujob();
        spyOn(ujobService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ujob });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ujob }));
        saveSubject.complete();

        // THEN
        expect(ujobService.create).toHaveBeenCalledWith(ujob);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ujob = { id: 123 };
        spyOn(ujobService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ujob });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ujobService.update).toHaveBeenCalledWith(ujob);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
