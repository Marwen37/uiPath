import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMachine, Machine } from '../machine.model';

import { MachineService } from './machine.service';

describe('Service Tests', () => {
  describe('Machine Service', () => {
    let service: MachineService;
    let httpMock: HttpTestingController;
    let elemDefault: IMachine;
    let expectedResult: IMachine | IMachine[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MachineService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        licence: 0,
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

      it('should create a Machine', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Machine()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Machine', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            licence: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Machine', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
          },
          new Machine()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Machine', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            licence: 1,
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

      it('should delete a Machine', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMachineToCollectionIfMissing', () => {
        it('should add a Machine to an empty array', () => {
          const machine: IMachine = { id: 123 };
          expectedResult = service.addMachineToCollectionIfMissing([], machine);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(machine);
        });

        it('should not add a Machine to an array that contains it', () => {
          const machine: IMachine = { id: 123 };
          const machineCollection: IMachine[] = [
            {
              ...machine,
            },
            { id: 456 },
          ];
          expectedResult = service.addMachineToCollectionIfMissing(machineCollection, machine);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Machine to an array that doesn't contain it", () => {
          const machine: IMachine = { id: 123 };
          const machineCollection: IMachine[] = [{ id: 456 }];
          expectedResult = service.addMachineToCollectionIfMissing(machineCollection, machine);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(machine);
        });

        it('should add only unique Machine to an array', () => {
          const machineArray: IMachine[] = [{ id: 123 }, { id: 456 }, { id: 30186 }];
          const machineCollection: IMachine[] = [{ id: 123 }];
          expectedResult = service.addMachineToCollectionIfMissing(machineCollection, ...machineArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const machine: IMachine = { id: 123 };
          const machine2: IMachine = { id: 456 };
          expectedResult = service.addMachineToCollectionIfMissing([], machine, machine2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(machine);
          expect(expectedResult).toContain(machine2);
        });

        it('should accept null and undefined values', () => {
          const machine: IMachine = { id: 123 };
          expectedResult = service.addMachineToCollectionIfMissing([], null, machine, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(machine);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
