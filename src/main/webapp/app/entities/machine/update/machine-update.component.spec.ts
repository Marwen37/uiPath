jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MachineService } from '../service/machine.service';
import { IMachine, Machine } from '../machine.model';
import { IUrobot } from 'app/entities/urobot/urobot.model';
import { UrobotService } from 'app/entities/urobot/service/urobot.service';

import { MachineUpdateComponent } from './machine-update.component';

describe('Component Tests', () => {
  describe('Machine Management Update Component', () => {
    let comp: MachineUpdateComponent;
    let fixture: ComponentFixture<MachineUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let machineService: MachineService;
    let urobotService: UrobotService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MachineUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MachineUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MachineUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      machineService = TestBed.inject(MachineService);
      urobotService = TestBed.inject(UrobotService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Urobot query and add missing value', () => {
        const machine: IMachine = { id: 456 };
        const urobot: IUrobot = { id: 52659 };
        machine.urobot = urobot;
        const urobot: IUrobot = { id: 87646 };
        machine.urobot = urobot;

        const urobotCollection: IUrobot[] = [{ id: 92391 }];
        spyOn(urobotService, 'query').and.returnValue(of(new HttpResponse({ body: urobotCollection })));
        const additionalUrobots = [urobot, urobot];
        const expectedCollection: IUrobot[] = [...additionalUrobots, ...urobotCollection];
        spyOn(urobotService, 'addUrobotToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ machine });
        comp.ngOnInit();

        expect(urobotService.query).toHaveBeenCalled();
        expect(urobotService.addUrobotToCollectionIfMissing).toHaveBeenCalledWith(urobotCollection, ...additionalUrobots);
        expect(comp.urobotsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const machine: IMachine = { id: 456 };
        const urobot: IUrobot = { id: 9671 };
        machine.urobot = urobot;
        const urobot: IUrobot = { id: 76554 };
        machine.urobot = urobot;

        activatedRoute.data = of({ machine });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(machine));
        expect(comp.urobotsSharedCollection).toContain(urobot);
        expect(comp.urobotsSharedCollection).toContain(urobot);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const machine = { id: 123 };
        spyOn(machineService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ machine });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: machine }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(machineService.update).toHaveBeenCalledWith(machine);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const machine = new Machine();
        spyOn(machineService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ machine });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: machine }));
        saveSubject.complete();

        // THEN
        expect(machineService.create).toHaveBeenCalledWith(machine);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const machine = { id: 123 };
        spyOn(machineService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ machine });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(machineService.update).toHaveBeenCalledWith(machine);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUrobotById', () => {
        it('Should return tracked Urobot primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUrobotById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
