import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { RobotType } from 'app/entities/enumerations/robot-type.model';
import { IRobot, Robot } from '../robot.model';

import { RobotService } from './robot.service';

describe('Service Tests', () => {
  describe('Robot Service', () => {
    let service: RobotService;
    let httpMock: HttpTestingController;
    let elemDefault: IRobot;
    let expectedResult: IRobot | IRobot[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RobotService);
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

      it('should create a Robot', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Robot()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Robot', () => {
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

      it('should partial update a Robot', () => {
        const patchObject = Object.assign(
          {
            description: 'BBBBBB',
            type: 'BBBBBB',
          },
          new Robot()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Robot', () => {
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

      it('should delete a Robot', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRobotToCollectionIfMissing', () => {
        it('should add a Robot to an empty array', () => {
          const robot: IRobot = { id: 123 };
          expectedResult = service.addRobotToCollectionIfMissing([], robot);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(robot);
        });

        it('should not add a Robot to an array that contains it', () => {
          const robot: IRobot = { id: 123 };
          const robotCollection: IRobot[] = [
            {
              ...robot,
            },
            { id: 456 },
          ];
          expectedResult = service.addRobotToCollectionIfMissing(robotCollection, robot);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Robot to an array that doesn't contain it", () => {
          const robot: IRobot = { id: 123 };
          const robotCollection: IRobot[] = [{ id: 456 }];
          expectedResult = service.addRobotToCollectionIfMissing(robotCollection, robot);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(robot);
        });

        it('should add only unique Robot to an array', () => {
          const robotArray: IRobot[] = [{ id: 123 }, { id: 456 }, { id: 31764 }];
          const robotCollection: IRobot[] = [{ id: 123 }];
          expectedResult = service.addRobotToCollectionIfMissing(robotCollection, ...robotArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const robot: IRobot = { id: 123 };
          const robot2: IRobot = { id: 456 };
          expectedResult = service.addRobotToCollectionIfMissing([], robot, robot2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(robot);
          expect(expectedResult).toContain(robot2);
        });

        it('should accept null and undefined values', () => {
          const robot: IRobot = { id: 123 };
          expectedResult = service.addRobotToCollectionIfMissing([], null, robot, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(robot);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
