import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUjob, Ujob } from '../ujob.model';

import { UjobService } from './ujob.service';

describe('Service Tests', () => {
  describe('Ujob Service', () => {
    let service: UjobService;
    let httpMock: HttpTestingController;
    let elemDefault: IUjob;
    let expectedResult: IUjob | IUjob[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UjobService);
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

      it('should create a Ujob', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Ujob()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Ujob', () => {
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

      it('should partial update a Ujob', () => {
        const patchObject = Object.assign(
          {
            description: 'BBBBBB',
          },
          new Ujob()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Ujob', () => {
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

      it('should delete a Ujob', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUjobToCollectionIfMissing', () => {
        it('should add a Ujob to an empty array', () => {
          const ujob: IUjob = { id: 123 };
          expectedResult = service.addUjobToCollectionIfMissing([], ujob);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ujob);
        });

        it('should not add a Ujob to an array that contains it', () => {
          const ujob: IUjob = { id: 123 };
          const ujobCollection: IUjob[] = [
            {
              ...ujob,
            },
            { id: 456 },
          ];
          expectedResult = service.addUjobToCollectionIfMissing(ujobCollection, ujob);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Ujob to an array that doesn't contain it", () => {
          const ujob: IUjob = { id: 123 };
          const ujobCollection: IUjob[] = [{ id: 456 }];
          expectedResult = service.addUjobToCollectionIfMissing(ujobCollection, ujob);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ujob);
        });

        it('should add only unique Ujob to an array', () => {
          const ujobArray: IUjob[] = [{ id: 123 }, { id: 456 }, { id: 20078 }];
          const ujobCollection: IUjob[] = [{ id: 123 }];
          expectedResult = service.addUjobToCollectionIfMissing(ujobCollection, ...ujobArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ujob: IUjob = { id: 123 };
          const ujob2: IUjob = { id: 456 };
          expectedResult = service.addUjobToCollectionIfMissing([], ujob, ujob2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ujob);
          expect(expectedResult).toContain(ujob2);
        });

        it('should accept null and undefined values', () => {
          const ujob: IUjob = { id: 123 };
          expectedResult = service.addUjobToCollectionIfMissing([], null, ujob, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ujob);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
