jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MachineService } from '../service/machine.service';
import { IMachine, Machine } from '../machine.model';
import { IRobot } from 'app/entities/robot/robot.model';
import { RobotService } from 'app/entities/robot/service/robot.service';

import { MachineUpdateComponent } from './machine-update.component';

describe('Component Tests', () => {
  describe('Machine Management Update Component', () => {
    let comp: MachineUpdateComponent;
    let fixture: ComponentFixture<MachineUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let machineService: MachineService;
    let robotService: RobotService;

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
      robotService = TestBed.inject(RobotService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Robot query and add missing value', () => {
        const machine: IMachine = { id: 456 };
        const robot: IRobot = { id: 62576 };
        machine.robot = robot;
        const robot: IRobot = { id: 35171 };
        machine.robot = robot;

        const robotCollection: IRobot[] = [{ id: 2059 }];
        spyOn(robotService, 'query').and.returnValue(of(new HttpResponse({ body: robotCollection })));
        const additionalRobots = [robot, robot];
        const expectedCollection: IRobot[] = [...additionalRobots, ...robotCollection];
        spyOn(robotService, 'addRobotToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ machine });
        comp.ngOnInit();

        expect(robotService.query).toHaveBeenCalled();
        expect(robotService.addRobotToCollectionIfMissing).toHaveBeenCalledWith(robotCollection, ...additionalRobots);
        expect(comp.robotsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const machine: IMachine = { id: 456 };
        const robot: IRobot = { id: 68394 };
        machine.robot = robot;
        const robot: IRobot = { id: 3631 };
        machine.robot = robot;

        activatedRoute.data = of({ machine });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(machine));
        expect(comp.robotsSharedCollection).toContain(robot);
        expect(comp.robotsSharedCollection).toContain(robot);
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
      describe('trackRobotById', () => {
        it('Should return tracked Robot primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRobotById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
