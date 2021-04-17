import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UpackageDetailComponent } from './upackage-detail.component';

describe('Component Tests', () => {
  describe('Upackage Management Detail Component', () => {
    let comp: UpackageDetailComponent;
    let fixture: ComponentFixture<UpackageDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UpackageDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ upackage: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UpackageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UpackageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load upackage on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.upackage).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
