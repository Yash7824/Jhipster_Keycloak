import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TenantComponent } from './list/tenant.component';
import { TenantDetailComponent } from './detail/tenant-detail.component';
import { TenantUpdateComponent } from './update/tenant-update.component';
import { TenantDeleteDialogComponent } from './delete/tenant-delete-dialog.component';
import { TenantRoutingModule } from './route/tenant-routing.module';

@NgModule({
  imports: [SharedModule, TenantRoutingModule],
  declarations: [TenantComponent, TenantDetailComponent, TenantUpdateComponent, TenantDeleteDialogComponent],
  entryComponents: [TenantDeleteDialogComponent],
})
export class TenantModule {}
