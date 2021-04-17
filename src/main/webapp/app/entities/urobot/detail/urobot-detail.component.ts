import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUrobot } from '../urobot.model';

@Component({
  selector: 'jhi-urobot-detail',
  templateUrl: './urobot-detail.component.html',
})
export class UrobotDetailComponent implements OnInit {
  urobot: IUrobot | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ urobot }) => {
      this.urobot = urobot;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
