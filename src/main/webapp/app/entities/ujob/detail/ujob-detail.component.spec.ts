import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UjobDetailComponent } from './ujob-detail.component';

describe('Component Tests', () => {
  describe('Ujob Management Detail Component', () => {
    let comp: UjobDetailComponent;
    let fixture: ComponentFixture<UjobDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UjobDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ujob: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UjobDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UjobDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ujob on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ujob).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
