import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITenant } from '../tenant.model';
import { TenantService } from '../service/tenant.service';

@Component({
  templateUrl: './tenant-delete-dialog.component.html',
})
export class TenantDeleteDialogComponent {
  tenant?: ITenant;

  constructor(protected tenantService: TenantService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tenantService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
