import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUpackage, Upackage } from '../upackage.model';
import { UpackageService } from '../service/upackage.service';
import { IUprocess } from 'app/entities/uprocess/uprocess.model';
import { UprocessService } from 'app/entities/uprocess/service/uprocess.service';

@Component({
  selector: 'jhi-upackage-update',
  templateUrl: './upackage-update.component.html',
})
export class UpackageUpdateComponent implements OnInit {
  isSaving = false;

  uprocessesSharedCollection: IUprocess[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    uprocess: [],
    uprocess: [],
  });

  constructor(
    protected upackageService: UpackageService,
    protected uprocessService: UprocessService,
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

  trackUprocessById(index: number, item: IUprocess): number {
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
      uprocess: upackage.uprocess,
      uprocess: upackage.uprocess,
    });

    this.uprocessesSharedCollection = this.uprocessService.addUprocessToCollectionIfMissing(
      this.uprocessesSharedCollection,
      upackage.uprocess,
      upackage.uprocess
    );
  }

  protected loadRelationshipsOptions(): void {
    this.uprocessService
      .query()
      .pipe(map((res: HttpResponse<IUprocess[]>) => res.body ?? []))
      .pipe(
        map((uprocesses: IUprocess[]) =>
          this.uprocessService.addUprocessToCollectionIfMissing(
            uprocesses,
            this.editForm.get('uprocess')!.value,
            this.editForm.get('uprocess')!.value
          )
        )
      )
      .subscribe((uprocesses: IUprocess[]) => (this.uprocessesSharedCollection = uprocesses));
  }

  protected createFromForm(): IUpackage {
    return {
      ...new Upackage(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      uprocess: this.editForm.get(['uprocess'])!.value,
      uprocess: this.editForm.get(['uprocess'])!.value,
    };
  }
}
