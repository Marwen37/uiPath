import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUpackage } from '../upackage.model';

@Component({
  selector: 'jhi-upackage-detail',
  templateUrl: './upackage-detail.component.html',
})
export class UpackageDetailComponent implements OnInit {
  upackage: IUpackage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ upackage }) => {
      this.upackage = upackage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
