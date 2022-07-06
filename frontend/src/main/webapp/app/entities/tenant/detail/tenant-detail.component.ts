import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITenant } from '../tenant.model';

@Component({
  selector: 'jhi-tenant-detail',
  templateUrl: './tenant-detail.component.html',
})
export class TenantDetailComponent implements OnInit {
  tenant: ITenant | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tenant }) => {
      this.tenant = tenant;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
