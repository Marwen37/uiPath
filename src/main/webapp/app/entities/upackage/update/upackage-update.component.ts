import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUpackage, Upackage } from '../upackage.model';
import { UpackageService } from '../service/upackage.service';
import { IProcess } from 'app/entities/process/process.model';
import { ProcessService } from 'app/entities/process/service/process.service';

@Component({
  selector: 'jhi-upackage-update',
  templateUrl: './upackage-update.component.html',
})
export class UpackageUpdateComponent implements OnInit {
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
    protected upackageService: UpackageService,
    protected processService: ProcessService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ upackage }) => {
      this.updateForm(upackage);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const upackage = this.createFromForm();
    if (upackage.id !== undefined) {
      this.subscribeToSaveResponse(this.upackageService.update(upackage));
    } else {
      this.subscribeToSaveResponse(this.upackageService.create(upackage));
    }
  }

  trackProcessById(index: number, item: IProcess): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUpackage>>): void {
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

  protected updateForm(upackage: IUpackage): void {
    this.editForm.patchValue({
      id: upackage.id,
      name: upackage.name,
      description: upackage.description,
      process: upackage.process,
      process: upackage.process,
    });

    this.processesSharedCollection = this.processService.addProcessToCollectionIfMissing(
      this.processesSharedCollection,
      upackage.process,
      upackage.process
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

  protected createFromForm(): IUpackage {
    return {
      ...new Upackage(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      process: this.editForm.get(['process'])!.value,
      process: this.editForm.get(['process'])!.value,
    };
  }
}
