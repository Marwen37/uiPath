import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUenvironment, Uenvironment } from '../uenvironment.model';
import { UenvironmentService } from '../service/uenvironment.service';
import { IUprocess } from 'app/entities/uprocess/uprocess.model';
import { UprocessService } from 'app/entities/uprocess/service/uprocess.service';

@Component({
  selector: 'jhi-uenvironment-update',
  templateUrl: './uenvironment-update.component.html',
})
export class UenvironmentUpdateComponent implements OnInit {
  isSaving = false;

  uprocessesSharedCollection: IUprocess[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    uprocess: [],
  });

  constructor(
    protected uenvironmentService: UenvironmentService,
    protected uprocessService: UprocessService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ uenvironment }) => {
      this.updateForm(uenvironment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const uenvironment = this.createFromForm();
    if (uenvironment.id !== undefined) {
      this.subscribeToSaveResponse(this.uenvironmentService.update(uenvironment));
    } else {
      this.subscribeToSaveResponse(this.uenvironmentService.create(uenvironment));
    }
  }

  trackUprocessById(index: number, item: IUprocess): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUenvironment>>): void {
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

  protected updateForm(uenvironment: IUenvironment): void {
    this.editForm.patchValue({
      id: uenvironment.id,
      name: uenvironment.name,
      description: uenvironment.description,
      uprocess: uenvironment.uprocess,
    });

    this.uprocessesSharedCollection = this.uprocessService.addUprocessToCollectionIfMissing(
      this.uprocessesSharedCollection,
      uenvironment.uprocess
    );
  }

  protected loadRelationshipsOptions(): void {
    this.uprocessService
      .query()
      .pipe(map((res: HttpResponse<IUprocess[]>) => res.body ?? []))
      .pipe(
        map((uprocesses: IUprocess[]) =>
          this.uprocessService.addUprocessToCollectionIfMissing(uprocesses, this.editForm.get('uprocess')!.value)
        )
      )
      .subscribe((uprocesses: IUprocess[]) => (this.uprocessesSharedCollection = uprocesses));
  }

  protected createFromForm(): IUenvironment {
    return {
      ...new Uenvironment(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      uprocess: this.editForm.get(['uprocess'])!.value,
    };
  }
}
