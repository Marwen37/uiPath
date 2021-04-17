import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { RobotType } from 'app/entities/enumerations/robot-type.model';
import { IUrobot, Urobot } from '../urobot.model';

import { UrobotService } from './urobot.service';

describe('Service Tests', () => {
  describe('Urobot Service', () => {
    let service: UrobotService;
    let httpMock: HttpTestingController;
    let elemDefault: IUrobot;
    let expectedResult: IUrobot | IUrobot[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UrobotService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        type: RobotType.Studio,
        domainUsername: 'AAAAAAA',
        password: 'AAAAAAA',
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

      it('should create a Urobot', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Urobot()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Urobot', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            type: 'BBBBBB',
            domainUsername: 'BBBBBB',
            password: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Urobot', () => {
        const patchObject = Object.assign(
          {
            type: 'BBBBBB',
            domainUsername: 'BBBBBB',
            password: 'BBBBBB',
          },
          new Urobot()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Urobot', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            type: 'BBBBBB',
            domainUsername: 'BBBBBB',
            password: 'BBBBBB',
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

      it('should delete a Urobot', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUrobotToCollectionIfMissing', () => {
        it('should add a Urobot to an empty array', () => {
          const urobot: IUrobot = { id: 123 };
          expectedResult = service.addUrobotToCollectionIfMissing([], urobot);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(urobot);
        });

        it('should not add a Urobot to an array that contains it', () => {
          const urobot: IUrobot = { id: 123 };
          const urobotCollection: IUrobot[] = [
            {
              ...urobot,
            },
            { id: 456 },
          ];
          expectedResult = service.addUrobotToCollectionIfMissing(urobotCollection, urobot);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Urobot to an array that doesn't contain it", () => {
          const urobot: IUrobot = { id: 123 };
          const urobotCollection: IUrobot[] = [{ id: 456 }];
          expectedResult = service.addUrobotToCollectionIfMissing(urobotCollection, urobot);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(urobot);
        });

        it('should add only unique Urobot to an array', () => {
          const urobotArray: IUrobot[] = [{ id: 123 }, { id: 456 }, { id: 18458 }];
          const urobotCollection: IUrobot[] = [{ id: 123 }];
          expectedResult = service.addUrobotToCollectionIfMissing(urobotCollection, ...urobotArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const urobot: IUrobot = { id: 123 };
          const urobot2: IUrobot = { id: 456 };
          expectedResult = service.addUrobotToCollectionIfMissing([], urobot, urobot2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(urobot);
          expect(expectedResult).toContain(urobot2);
        });

        it('should accept null and undefined values', () => {
          const urobot: IUrobot = { id: 123 };
          expectedResult = service.addUrobotToCollectionIfMissing([], null, urobot, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(urobot);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
