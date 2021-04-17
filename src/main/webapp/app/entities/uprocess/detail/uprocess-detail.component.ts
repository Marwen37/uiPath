import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUprocess } from '../uprocess.model';

@Component({
  selector: 'jhi-uprocess-detail',
  templateUrl: './uprocess-detail.component.html',
})
export class UprocessDetailComponent implements OnInit {
  uprocess: IUprocess | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ uprocess }) => {
      this.uprocess = uprocess;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
