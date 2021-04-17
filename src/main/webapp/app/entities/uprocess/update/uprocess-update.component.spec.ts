jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UprocessService } from '../service/uprocess.service';
import { IUprocess, Uprocess } from '../uprocess.model';

import { UprocessUpdateComponent } from './uprocess-update.component';

describe('Component Tests', () => {
  describe('Uprocess Management Update Component', () => {
    let comp: UprocessUpdateComponent;
    let fixture: ComponentFixture<UprocessUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let uprocessService: UprocessService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UprocessUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UprocessUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UprocessUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      uprocessService = TestBed.inject(UprocessService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const uprocess: IUprocess = { id: 456 };

        activatedRoute.data = of({ uprocess });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(uprocess));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const uprocess = { id: 123 };
        spyOn(uprocessService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ uprocess });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: uprocess }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(uprocessService.update).toHaveBeenCalledWith(uprocess);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const uprocess = new Uprocess();
        spyOn(uprocessService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ uprocess });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: uprocess }));
        saveSubject.complete();

        // THEN
        expect(uprocessService.create).toHaveBeenCalledWith(uprocess);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const uprocess = { id: 123 };
        spyOn(uprocessService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ uprocess });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(uprocessService.update).toHaveBeenCalledWith(uprocess);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
