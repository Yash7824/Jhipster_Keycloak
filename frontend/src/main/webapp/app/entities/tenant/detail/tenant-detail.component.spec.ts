import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TenantDetailComponent } from './tenant-detail.component';

describe('Tenant Management Detail Component', () => {
  let comp: TenantDetailComponent;
  let fixture: ComponentFixture<TenantDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TenantDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tenant: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TenantDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TenantDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tenant on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tenant).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
