import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMachine, Machine } from '../machine.model';
import { MachineService } from '../service/machine.service';
import { IRobot } from 'app/entities/robot/robot.model';
import { RobotService } from 'app/entities/robot/service/robot.service';

@Component({
  selector: 'jhi-machine-update',
  templateUrl: './machine-update.component.html',
})
export class MachineUpdateComponent implements OnInit {
  isSaving = false;

  robotsSharedCollection: IRobot[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    licence: [],
    robot: [],
    robot: [],
  });

  constructor(
    protected machineService: MachineService,
    protected robotService: RobotService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ machine }) => {
      this.updateForm(machine);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const machine = this.createFromForm();
    if (machine.id !== undefined) {
      this.subscribeToSaveResponse(this.machineService.update(machine));
    } else {
      this.subscribeToSaveResponse(this.machineService.create(machine));
    }
  }

  trackRobotById(index: number, item: IRobot): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMachine>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(machine: IMachine): void {
    this.editForm.patchValue({
      id: machine.id,
      name: machine.name,
      description: machine.description,
      licence: machine.licence,
      robot: machine.robot,
      robot: machine.robot,
    });

    this.robotsSharedCollection = this.robotService.addRobotToCollectionIfMissing(
      this.robotsSharedCollection,
      machine.robot,
      machine.robot
    );
  }

  protected loadRelationshipsOptions(): void {
    this.robotService
      .query()
      .pipe(map((res: HttpResponse<IRobot[]>) => res.body ?? []))
      .pipe(
        map((robots: IRobot[]) =>
          this.robotService.addRobotToCollectionIfMissing(robots, this.editForm.get('robot')!.value, this.editForm.get('robot')!.value)
        )
      )
      .subscribe((robots: IRobot[]) => (this.robotsSharedCollection = robots));
  }

  protected createFromForm(): IMachine {
    return {
      ...new Machine(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      licence: this.editForm.get(['licence'])!.value,
      robot: this.editForm.get(['robot'])!.value,
      robot: this.editForm.get(['robot'])!.value,
    };
  }
}
