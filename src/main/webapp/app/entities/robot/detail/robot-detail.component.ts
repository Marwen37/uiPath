import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRobot } from '../robot.model';

@Component({
  selector: 'jhi-robot-detail',
  templateUrl: './robot-detail.component.html',
})
export class RobotDetailComponent implements OnInit {
  robot: IRobot | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ robot }) => {
      this.robot = robot;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
