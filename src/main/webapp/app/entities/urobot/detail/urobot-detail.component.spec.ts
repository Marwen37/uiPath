import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UrobotDetailComponent } from './urobot-detail.component';

describe('Component Tests', () => {
  describe('Urobot Management Detail Component', () => {
    let comp: UrobotDetailComponent;
    let fixture: ComponentFixture<UrobotDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UrobotDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ urobot: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UrobotDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UrobotDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load urobot on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.urobot).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
