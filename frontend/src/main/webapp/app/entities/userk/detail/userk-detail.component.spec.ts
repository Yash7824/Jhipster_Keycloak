import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UserkDetailComponent } from './userk-detail.component';

describe('Userk Management Detail Component', () => {
  let comp: UserkDetailComponent;
  let fixture: ComponentFixture<UserkDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserkDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ userk: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UserkDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UserkDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load userk on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.userk).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
