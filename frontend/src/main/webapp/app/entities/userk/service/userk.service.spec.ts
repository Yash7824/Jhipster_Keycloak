import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserk, Userk } from '../userk.model';

import { UserkService } from './userk.service';

describe('Userk Service', () => {
  let service: UserkService;
  let httpMock: HttpTestingController;
  let elemDefault: IUserk;
  let expectedResult: IUserk | IUserk[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserkService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      username: 'AAAAAAA',
      email: 'AAAAAAA',
      firstName: 'AAAAAAA',
      lastName: 'AAAAAAA',
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

    it('should create a Userk', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Userk()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Userk', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          username: 'BBBBBB',
          email: 'BBBBBB',
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Userk', () => {
      const patchObject = Object.assign(
        {
          username: 'BBBBBB',
          email: 'BBBBBB',
          lastName: 'BBBBBB',
        },
        new Userk()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Userk', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          username: 'BBBBBB',
          email: 'BBBBBB',
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
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

    it('should delete a Userk', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUserkToCollectionIfMissing', () => {
      it('should add a Userk to an empty array', () => {
        const userk: IUserk = { id: 123 };
        expectedResult = service.addUserkToCollectionIfMissing([], userk);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userk);
      });

      it('should not add a Userk to an array that contains it', () => {
        const userk: IUserk = { id: 123 };
        const userkCollection: IUserk[] = [
          {
            ...userk,
          },
          { id: 456 },
        ];
        expectedResult = service.addUserkToCollectionIfMissing(userkCollection, userk);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Userk to an array that doesn't contain it", () => {
        const userk: IUserk = { id: 123 };
        const userkCollection: IUserk[] = [{ id: 456 }];
        expectedResult = service.addUserkToCollectionIfMissing(userkCollection, userk);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userk);
      });

      it('should add only unique Userk to an array', () => {
        const userkArray: IUserk[] = [{ id: 123 }, { id: 456 }, { id: 454 }];
        const userkCollection: IUserk[] = [{ id: 123 }];
        expectedResult = service.addUserkToCollectionIfMissing(userkCollection, ...userkArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userk: IUserk = { id: 123 };
        const userk2: IUserk = { id: 456 };
        expectedResult = service.addUserkToCollectionIfMissing([], userk, userk2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userk);
        expect(expectedResult).toContain(userk2);
      });

      it('should accept null and undefined values', () => {
        const userk: IUserk = { id: 123 };
        expectedResult = service.addUserkToCollectionIfMissing([], null, userk, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userk);
      });

      it('should return initial array if no Userk is added', () => {
        const userkCollection: IUserk[] = [{ id: 123 }];
        expectedResult = service.addUserkToCollectionIfMissing(userkCollection, undefined, null);
        expect(expectedResult).toEqual(userkCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
