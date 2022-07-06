import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserkComponent } from '../list/userk.component';
import { UserkDetailComponent } from '../detail/userk-detail.component';
import { UserkUpdateComponent } from '../update/userk-update.component';
import { UserkRoutingResolveService } from './userk-routing-resolve.service';

const userkRoute: Routes = [
  {
    path: '',
    component: UserkComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserkDetailComponent,
    resolve: {
      userk: UserkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserkUpdateComponent,
    resolve: {
      userk: UserkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserkUpdateComponent,
    resolve: {
      userk: UserkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(userkRoute)],
  exports: [RouterModule],
})
export class UserkRoutingModule {}
