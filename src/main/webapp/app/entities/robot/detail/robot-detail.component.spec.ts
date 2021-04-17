import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RobotDetailComponent } from './robot-detail.component';

describe('Component Tests', () => {
  describe('Robot Management Detail Component', () => {
    let comp: RobotDetailComponent;
    let fixture: ComponentFixture<RobotDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RobotDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ robot: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RobotDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RobotDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load robot on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.robot).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
