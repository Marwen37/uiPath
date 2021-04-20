import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUrobot, Urobot } from '../urobot.model';
import { UrobotService } from '../service/urobot.service';
import { IUenvironment } from 'app/entities/uenvironment/uenvironment.model';
import { UenvironmentService } from 'app/entities/uenvironment/service/uenvironment.service';

@Component({
  selector: 'jhi-urobot-update',
  templateUrl: './urobot-update.component.html',
})
export class UrobotUpdateComponent implements OnInit {
  isSaving = false;

  uenvironmentsSharedCollection: IUenvironment[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    type: [],
    domainUsername: [],
    password: [],
    uenvironment: [],
  });

  constructor(
    protected urobotService: UrobotService,
    protected uenvironmentService: UenvironmentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ urobot }) => {
      this.updateForm(urobot);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const urobot = this.createFromForm();
    if (urobot.id !== undefined) {
      this.subscribeToSaveResponse(this.urobotService.update(urobot));
    } else {
      this.subscribeToSaveResponse(this.urobotService.create(urobot));
    }
  }

  trackUenvironmentById(index: number, item: IUenvironment): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUrobot>>): void {
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

  protected updateForm(urobot: IUrobot): void {
    this.editForm.patchValue({
      id: urobot.id,
      name: urobot.name,
      description: urobot.description,
      type: urobot.type,
      domainUsername: urobot.domainUsername,
      password: urobot.password,
      uenvironment: urobot.uenvironment,
    });

    this.uenvironmentsSharedCollection = this.uenvironmentService.addUenvironmentToCollectionIfMissing(
      this.uenvironmentsSharedCollection,
      urobot.uenvironment
    );
  }

  protected loadRelationshipsOptions(): void {
    this.uenvironmentService
      .query()
      .pipe(map((res: HttpResponse<IUenvironment[]>) => res.body ?? []))
      .pipe(
        map((uenvironments: IUenvironment[]) =>
          this.uenvironmentService.addUenvironmentToCollectionIfMissing(uenvironments, this.editForm.get('uenvironment')!.value)
        )
      )
      .subscribe((uenvironments: IUenvironment[]) => (this.uenvironmentsSharedCollection = uenvironments));
  }

  protected createFromForm(): IUrobot {
    return {
      ...new Urobot(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      type: this.editForm.get(['type'])!.value,
      domainUsername: this.editForm.get(['domainUsername'])!.value,
      password: this.editForm.get(['password'])!.value,
      uenvironment: this.editForm.get(['uenvironment'])!.value,
    };
  }
}
