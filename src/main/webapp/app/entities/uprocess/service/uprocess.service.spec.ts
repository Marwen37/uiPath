import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Priority } from 'app/entities/enumerations/priority.model';
import { IUprocess, Uprocess } from '../uprocess.model';

import { UprocessService } from './uprocess.service';

describe('Service Tests', () => {
  describe('Uprocess Service', () => {
    let service: UprocessService;
    let httpMock: HttpTestingController;
    let elemDefault: IUprocess;
    let expectedResult: IUprocess | IUprocess[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UprocessService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        jobPriority: Priority.Low,
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

      it('should create a Uprocess', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Uprocess()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Uprocess', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            jobPriority: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Uprocess', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
            jobPriority: 'BBBBBB',
          },
          new Uprocess()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Uprocess', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            jobPriority: 'BBBBBB',
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

      it('should delete a Uprocess', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUprocessToCollectionIfMissing', () => {
        it('should add a Uprocess to an empty array', () => {
          const uprocess: IUprocess = { id: 123 };
          expectedResult = service.addUprocessToCollectionIfMissing([], uprocess);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(uprocess);
        });

        it('should not add a Uprocess to an array that contains it', () => {
          const uprocess: IUprocess = { id: 123 };
          const uprocessCollection: IUprocess[] = [
            {
              ...uprocess,
            },
            { id: 456 },
          ];
          expectedResult = service.addUprocessToCollectionIfMissing(uprocessCollection, uprocess);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Uprocess to an array that doesn't contain it", () => {
          const uprocess: IUprocess = { id: 123 };
          const uprocessCollection: IUprocess[] = [{ id: 456 }];
          expectedResult = service.addUprocessToCollectionIfMissing(uprocessCollection, uprocess);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(uprocess);
        });

        it('should add only unique Uprocess to an array', () => {
          const uprocessArray: IUprocess[] = [{ id: 123 }, { id: 456 }, { id: 48905 }];
          const uprocessCollection: IUprocess[] = [{ id: 123 }];
          expectedResult = service.addUprocessToCollectionIfMissing(uprocessCollection, ...uprocessArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const uprocess: IUprocess = { id: 123 };
          const uprocess2: IUprocess = { id: 456 };
          expectedResult = service.addUprocessToCollectionIfMissing([], uprocess, uprocess2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(uprocess);
          expect(expectedResult).toContain(uprocess2);
        });

        it('should accept null and undefined values', () => {
          const uprocess: IUprocess = { id: 123 };
          expectedResult = service.addUprocessToCollectionIfMissing([], null, uprocess, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(uprocess);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
