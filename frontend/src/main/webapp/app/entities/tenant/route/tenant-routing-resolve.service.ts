import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITenant, Tenant } from '../tenant.model';
import { TenantService } from '../service/tenant.service';

@Injectable({ providedIn: 'root' })
export class TenantRoutingResolveService implements Resolve<ITenant> {
  constructor(protected service: TenantService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITenant> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tenant: HttpResponse<Tenant>) => {
          if (tenant.body) {
            return of(tenant.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tenant());
  }
}
