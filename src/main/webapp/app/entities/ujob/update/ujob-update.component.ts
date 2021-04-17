import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IUjob, Ujob } from '../ujob.model';
import { UjobService } from '../service/ujob.service';

@Component({
  selector: 'jhi-ujob-update',
  templateUrl: './ujob-update.component.html',
})
export class UjobUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
  });

  constructor(protected ujobService: UjobService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ujob }) => {
      this.updateForm(ujob);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ujob = this.createFromForm();
    if (ujob.id !== undefined) {
      this.subscribeToSaveResponse(this.ujobService.update(ujob));
    } else {
      this.subscribeToSaveResponse(this.ujobService.create(ujob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUjob>>): void {
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

  protected updateForm(ujob: IUjob): void {
    this.editForm.patchValue({
      id: ujob.id,
      name: ujob.name,
      description: ujob.description,
    });
  }

  protected createFromForm(): IUjob {
    return {
      ...new Ujob(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
