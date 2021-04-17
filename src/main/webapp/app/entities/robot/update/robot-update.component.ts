import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRobot, Robot } from '../robot.model';
import { RobotService } from '../service/robot.service';
import { IEnvironment } from 'app/entities/environment/environment.model';
import { EnvironmentService } from 'app/entities/environment/service/environment.service';

@Component({
  selector: 'jhi-robot-update',
  templateUrl: './robot-update.component.html',
})
export class RobotUpdateComponent implements OnInit {
  isSaving = false;

  environmentsSharedCollection: IEnvironment[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    type: [],
    domainUsername: [],
    password: [],
    environment: [],
    environment: [],
  });

  constructor(
    protected robotService: RobotService,
    protected environmentService: EnvironmentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ robot }) => {
      this.updateForm(robot);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const robot = this.createFromForm();
    if (robot.id !== undefined) {
      this.subscribeToSaveResponse(this.robotService.update(robot));
    } else {
      this.subscribeToSaveResponse(this.robotService.create(robot));
    }
  }

  trackEnvironmentById(index: number, item: IEnvironment): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRobot>>): void {
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

  protected updateForm(robot: IRobot): void {
    this.editForm.patchValue({
      id: robot.id,
      name: robot.name,
      description: robot.description,
      type: robot.type,
      domainUsername: robot.domainUsername,
      password: robot.password,
      environment: robot.environment,
      environment: robot.environment,
    });

    this.environmentsSharedCollection = this.environmentService.addEnvironmentToCollectionIfMissing(
      this.environmentsSharedCollection,
      robot.environment,
      robot.environment
    );
  }

  protected loadRelationshipsOptions(): void {
    this.environmentService
      .query()
      .pipe(map((res: HttpResponse<IEnvironment[]>) => res.body ?? []))
      .pipe(
        map((environments: IEnvironment[]) =>
          this.environmentService.addEnvironmentToCollectionIfMissing(
            environments,
            this.editForm.get('environment')!.value,
            this.editForm.get('environment')!.value
          )
        )
      )
      .subscribe((environments: IEnvironment[]) => (this.environmentsSharedCollection = environments));
  }

  protected createFromForm(): IRobot {
    return {
      ...new Robot(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      type: this.editForm.get(['type'])!.value,
      domainUsername: this.editForm.get(['domainUsername'])!.value,
      password: this.editForm.get(['password'])!.value,
      environment: this.editForm.get(['environment'])!.value,
      environment: this.editForm.get(['environment'])!.value,
    };
  }
}
