import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UprocessDetailComponent } from './uprocess-detail.component';

describe('Component Tests', () => {
  describe('Uprocess Management Detail Component', () => {
    let comp: UprocessDetailComponent;
    let fixture: ComponentFixture<UprocessDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UprocessDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ uprocess: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UprocessDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UprocessDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load uprocess on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.uprocess).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
