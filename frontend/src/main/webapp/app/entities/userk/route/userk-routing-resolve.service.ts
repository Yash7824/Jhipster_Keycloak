import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserk, Userk } from '../userk.model';
import { UserkService } from '../service/userk.service';

@Injectable({ providedIn: 'root' })
export class UserkRoutingResolveService implements Resolve<IUserk> {
  constructor(protected service: UserkService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserk> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userk: HttpResponse<Userk>) => {
          if (userk.body) {
            return of(userk.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Userk());
  }
}
