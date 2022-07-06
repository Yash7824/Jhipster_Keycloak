import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'userk',
        data: { pageTitle: 'Userks' },
        loadChildren: () => import('./userk/userk.module').then(m => m.UserkModule),
      },
      {
        path: 'tenant',
        data: { pageTitle: 'Tenants' },
        loadChildren: () => import('./tenant/tenant.module').then(m => m.TenantModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
