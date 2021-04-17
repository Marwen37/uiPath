import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEnvironment, Environment } from '../environment.model';
import { EnvironmentService } from '../service/environment.service';
import { IProcess } from 'app/entities/process/process.model';
import { ProcessService } from 'app/entities/process/service/process.service';

@Component({
  selector: 'jhi-environment-update',
  templateUrl: './environment-update.component.html',
})
export class EnvironmentUpdateComponent implements OnInit {
  isSaving = false;

  processesSharedCollection: IProcess[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    process: [],
    process: [],
  });

  constructor(
    protected environmentService: EnvironmentService,
    protected processService: ProcessService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ environment }) => {
      this.updateForm(environment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const environment = this.createFromForm();
    if (environment.id !== undefined) {
      this.subscribeToSaveResponse(this.environmentService.update(environment));
    } else {
      this.subscribeToSaveResponse(this.environmentService.create(environment));
    }
  }

  trackProcessById(index: number, item: IProcess): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnvironment>>): void {
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

  protected updateForm(environment: IEnvironment): void {
    this.editForm.patchValue({
      id: environment.id,
      name: environment.name,
      description: environment.description,
      process: environment.process,
      process: environment.process,
    });

    this.processesSharedCollection = this.processService.addProcessToCollectionIfMissing(
      this.processesSharedCollection,
      environment.process,
      environment.process
    );
  }

  protected loadRelationshipsOptions(): void {
    this.processService
      .query()
      .pipe(map((res: HttpResponse<IProcess[]>) => res.body ?? []))
      .pipe(
        map((processes: IProcess[]) =>
          this.processService.addProcessToCollectionIfMissing(
            processes,
            this.editForm.get('process')!.value,
            this.editForm.get('process')!.value
          )
        )
      )
      .subscribe((processes: IProcess[]) => (this.processesSharedCollection = processes));
  }

  protected createFromForm(): IEnvironment {
    return {
      ...new Environment(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      process: this.editForm.get(['process'])!.value,
      process: this.editForm.get(['process'])!.value,
    };
  }
}
