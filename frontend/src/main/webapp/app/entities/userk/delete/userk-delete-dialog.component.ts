import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserk } from '../userk.model';
import { UserkService } from '../service/userk.service';

@Component({
  templateUrl: './userk-delete-dialog.component.html',
})
export class UserkDeleteDialogComponent {
  userk?: IUserk;

  constructor(protected userkService: UserkService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userkService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
