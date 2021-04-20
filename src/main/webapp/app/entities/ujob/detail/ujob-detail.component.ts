import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUjob } from '../ujob.model';

@Component({
  selector: 'jhi-ujob-detail',
  templateUrl: './ujob-detail.component.html',
})
export class UjobDetailComponent implements OnInit {
  ujob: IUjob | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ujob }) => {
      this.ujob = ujob;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
