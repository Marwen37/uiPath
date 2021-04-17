import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMachine, Machine } from '../machine.model';
import { MachineService } from '../service/machine.service';
import { IUrobot } from 'app/entities/urobot/urobot.model';
import { UrobotService } from 'app/entities/urobot/service/urobot.service';

@Component({
  selector: 'jhi-machine-update',
  templateUrl: './machine-update.component.html',
})
export class MachineUpdateComponent implements OnInit {
  isSaving = false;

  urobotsSharedCollection: IUrobot[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    licence: [],
    urobot: [],
    urobot: [],
  });

  constructor(
    protected machineService: MachineService,
    protected urobotService: UrobotService,
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

  trackUrobotById(index: number, item: IUrobot): number {
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
      urobot: machine.urobot,
      urobot: machine.urobot,
    });

    this.urobotsSharedCollection = this.urobotService.addUrobotToCollectionIfMissing(
      this.urobotsSharedCollection,
      machine.urobot,
      machine.urobot
    );
  }

  protected loadRelationshipsOptions(): void {
    this.urobotService
      .query()
      .pipe(map((res: HttpResponse<IUrobot[]>) => res.body ?? []))
      .pipe(
        map((urobots: IUrobot[]) =>
          this.urobotService.addUrobotToCollectionIfMissing(urobots, this.editForm.get('urobot')!.value, this.editForm.get('urobot')!.value)
        )
      )
      .subscribe((urobots: IUrobot[]) => (this.urobotsSharedCollection = urobots));
  }

  protected createFromForm(): IMachine {
    return {
      ...new Machine(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      licence: this.editForm.get(['licence'])!.value,
      urobot: this.editForm.get(['urobot'])!.value,
      urobot: this.editForm.get(['urobot'])!.value,
    };
  }
}
