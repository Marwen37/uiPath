import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IUprocess, Uprocess } from '../uprocess.model';
import { UprocessService } from '../service/uprocess.service';

@Component({
  selector: 'jhi-uprocess-update',
  templateUrl: './uprocess-update.component.html',
})
export class UprocessUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    jobPriority: [],
  });

  constructor(protected uprocessService: UprocessService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ uprocess }) => {
      this.updateForm(uprocess);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const uprocess = this.createFromForm();
    if (uprocess.id !== undefined) {
      this.subscribeToSaveResponse(this.uprocessService.update(uprocess));
    } else {
      this.subscribeToSaveResponse(this.uprocessService.create(uprocess));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUprocess>>): void {
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

  protected updateForm(uprocess: IUprocess): void {
    this.editForm.patchValue({
      id: uprocess.id,
      name: uprocess.name,
      description: uprocess.description,
      jobPriority: uprocess.jobPriority,
    });
  }

  protected createFromForm(): IUprocess {
    return {
      ...new Uprocess(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      jobPriority: this.editForm.get(['jobPriority'])!.value,
    };
  }
}
