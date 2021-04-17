import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UenvironmentDetailComponent } from './uenvironment-detail.component';

describe('Component Tests', () => {
  describe('Uenvironment Management Detail Component', () => {
    let comp: UenvironmentDetailComponent;
    let fixture: ComponentFixture<UenvironmentDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UenvironmentDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ uenvironment: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UenvironmentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UenvironmentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load uenvironment on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.uenvironment).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
