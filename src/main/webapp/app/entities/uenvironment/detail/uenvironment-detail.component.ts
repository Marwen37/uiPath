import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUenvironment } from '../uenvironment.model';

@Component({
  selector: 'jhi-uenvironment-detail',
  templateUrl: './uenvironment-detail.component.html',
})
export class UenvironmentDetailComponent implements OnInit {
  uenvironment: IUenvironment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ uenvironment }) => {
      this.uenvironment = uenvironment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
