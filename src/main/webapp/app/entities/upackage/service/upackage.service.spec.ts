import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUpackage, Upackage } from '../upackage.model';

import { UpackageService } from './upackage.service';

describe('Service Tests', () => {
  describe('Upackage Service', () => {
    let service: UpackageService;
    let httpMock: HttpTestingController;
    let elemDefault: IUpackage;
    let expectedResult: IUpackage | IUpackage[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UpackageService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Upackage', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Upackage()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Upackage', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Upackage', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new Upackage()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Upackage', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Upackage', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUpackageToCollectionIfMissing', () => {
        it('should add a Upackage to an empty array', () => {
          const upackage: IUpackage = { id: 123 };
          expectedResult = service.addUpackageToCollectionIfMissing([], upackage);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(upackage);
        });

        it('should not add a Upackage to an array that contains it', () => {
          const upackage: IUpackage = { id: 123 };
          const upackageCollection: IUpackage[] = [
            {
              ...upackage,
            },
            { id: 456 },
          ];
          expectedResult = service.addUpackageToCollectionIfMissing(upackageCollection, upackage);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Upackage to an array that doesn't contain it", () => {
          const upackage: IUpackage = { id: 123 };
          const upackageCollection: IUpackage[] = [{ id: 456 }];
          expectedResult = service.addUpackageToCollectionIfMissing(upackageCollection, upackage);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(upackage);
        });

        it('should add only unique Upackage to an array', () => {
          const upackageArray: IUpackage[] = [{ id: 123 }, { id: 456 }, { id: 38775 }];
          const upackageCollection: IUpackage[] = [{ id: 123 }];
          expectedResult = service.addUpackageToCollectionIfMissing(upackageCollection, ...upackageArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const upackage: IUpackage = { id: 123 };
          const upackage2: IUpackage = { id: 456 };
          expectedResult = service.addUpackageToCollectionIfMissing([], upackage, upackage2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(upackage);
          expect(expectedResult).toContain(upackage2);
        });

        it('should accept null and undefined values', () => {
          const upackage: IUpackage = { id: 123 };
          expectedResult = service.addUpackageToCollectionIfMissing([], null, upackage, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(upackage);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
