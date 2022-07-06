import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUserk, Userk } from '../userk.model';
import { UserkService } from '../service/userk.service';
import { ITenant } from 'app/entities/tenant/tenant.model';
import { TenantService } from 'app/entities/tenant/service/tenant.service';

@Component({
  selector: 'jhi-userk-update',
  templateUrl: './userk-update.component.html',
})
export class UserkUpdateComponent implements OnInit {
  isSaving = false;

  tenantsSharedCollection: ITenant[] = [];

  editForm = this.fb.group({
    id: [],
    username: [null, [Validators.required]],
    email: [],
    firstName: [],
    lastName: [],
    tenant: [],
  });

  constructor(
    protected userkService: UserkService,
    protected tenantService: TenantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userk }) => {
      this.updateForm(userk);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userk = this.createFromForm();
    if (userk.id !== undefined) {
      this.subscribeToSaveResponse(this.userkService.update(userk));
    } else {
      this.subscribeToSaveResponse(this.userkService.create(userk));
    }
  }

  trackTenantById(_index: number, item: ITenant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserk>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userk: IUserk): void {
    this.editForm.patchValue({
      id: userk.id,
      username: userk.username,
      email: userk.email,
      firstName: userk.firstName,
      lastName: userk.lastName,
      tenant: userk.tenant,
    });

    this.tenantsSharedCollection = this.tenantService.addTenantToCollectionIfMissing(this.tenantsSharedCollection, userk.tenant);
  }

  protected loadRelationshipsOptions(): void {
    this.tenantService
      .query()
      .pipe(map((res: HttpResponse<ITenant[]>) => res.body ?? []))
      .pipe(map((tenants: ITenant[]) => this.tenantService.addTenantToCollectionIfMissing(tenants, this.editForm.get('tenant')!.value)))
      .subscribe((tenants: ITenant[]) => (this.tenantsSharedCollection = tenants));
  }

  protected createFromForm(): IUserk {
    return {
      ...new Userk(),
      id: this.editForm.get(['id'])!.value,
      username: this.editForm.get(['username'])!.value,
      email: this.editForm.get(['email'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      tenant: this.editForm.get(['tenant'])!.value,
    };
  }
}
