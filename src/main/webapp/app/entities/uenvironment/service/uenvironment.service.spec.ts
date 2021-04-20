import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUenvironment, Uenvironment } from '../uenvironment.model';

import { UenvironmentService } from './uenvironment.service';

describe('Service Tests', () => {
  describe('Uenvironment Service', () => {
    let service: UenvironmentService;
    let httpMock: HttpTestingController;
    let elemDefault: IUenvironment;
    let expectedResult: IUenvironment | IUenvironment[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UenvironmentService);
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

      it('should create a Uenvironment', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Uenvironment()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Uenvironment', () => {
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

      it('should partial update a Uenvironment', () => {
        const patchObject = Object.assign({}, new Uenvironment());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Uenvironment', () => {
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

      it('should delete a Uenvironment', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUenvironmentToCollectionIfMissing', () => {
        it('should add a Uenvironment to an empty array', () => {
          const uenvironment: IUenvironment = { id: 123 };
          expectedResult = service.addUenvironmentToCollectionIfMissing([], uenvironment);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(uenvironment);
        });

        it('should not add a Uenvironment to an array that contains it', () => {
          const uenvironment: IUenvironment = { id: 123 };
          const uenvironmentCollection: IUenvironment[] = [
            {
              ...uenvironment,
            },
            { id: 456 },
          ];
          expectedResult = service.addUenvironmentToCollectionIfMissing(uenvironmentCollection, uenvironment);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Uenvironment to an array that doesn't contain it", () => {
          const uenvironment: IUenvironment = { id: 123 };
          const uenvironmentCollection: IUenvironment[] = [{ id: 456 }];
          expectedResult = service.addUenvironmentToCollectionIfMissing(uenvironmentCollection, uenvironment);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(uenvironment);
        });

        it('should add only unique Uenvironment to an array', () => {
          const uenvironmentArray: IUenvironment[] = [{ id: 123 }, { id: 456 }, { id: 28661 }];
          const uenvironmentCollection: IUenvironment[] = [{ id: 123 }];
          expectedResult = service.addUenvironmentToCollectionIfMissing(uenvironmentCollection, ...uenvironmentArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const uenvironment: IUenvironment = { id: 123 };
          const uenvironment2: IUenvironment = { id: 456 };
          expectedResult = service.addUenvironmentToCollectionIfMissing([], uenvironment, uenvironment2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(uenvironment);
          expect(expectedResult).toContain(uenvironment2);
        });

        it('should accept null and undefined values', () => {
          const uenvironment: IUenvironment = { id: 123 };
          expectedResult = service.addUenvironmentToCollectionIfMissing([], null, uenvironment, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(uenvironment);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
