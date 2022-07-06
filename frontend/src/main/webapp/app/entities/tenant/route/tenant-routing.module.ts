import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TenantComponent } from '../list/tenant.component';
import { TenantDetailComponent } from '../detail/tenant-detail.component';
import { TenantUpdateComponent } from '../update/tenant-update.component';
import { TenantRoutingResolveService } from './tenant-routing-resolve.service';

const tenantRoute: Routes = [
  {
    path: '',
    component: TenantComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TenantDetailComponent,
    resolve: {
      tenant: TenantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TenantUpdateComponent,
    resolve: {
      tenant: TenantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TenantUpdateComponent,
    resolve: {
      tenant: TenantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tenantRoute)],
  exports: [RouterModule],
})
export class TenantRoutingModule {}
