import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITenant, getTenantIdentifier } from '../tenant.model';

export type EntityResponseType = HttpResponse<ITenant>;
export type EntityArrayResponseType = HttpResponse<ITenant[]>;

@Injectable({ providedIn: 'root' })
export class TenantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tenants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tenant: ITenant): Observable<EntityResponseType> {
    return this.http.post<ITenant>(this.resourceUrl, tenant, { observe: 'response' });
  }

  update(tenant: ITenant): Observable<EntityResponseType> {
    return this.http.put<ITenant>(`${this.resourceUrl}/${getTenantIdentifier(tenant) as number}`, tenant, { observe: 'response' });
  }

  partialUpdate(tenant: ITenant): Observable<EntityResponseType> {
    return this.http.patch<ITenant>(`${this.resourceUrl}/${getTenantIdentifier(tenant) as number}`, tenant, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITenant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITenant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTenantToCollectionIfMissing(tenantCollection: ITenant[], ...tenantsToCheck: (ITenant | null | undefined)[]): ITenant[] {
    const tenants: ITenant[] = tenantsToCheck.filter(isPresent);
    if (tenants.length > 0) {
      const tenantCollectionIdentifiers = tenantCollection.map(tenantItem => getTenantIdentifier(tenantItem)!);
      const tenantsToAdd = tenants.filter(tenantItem => {
        const tenantIdentifier = getTenantIdentifier(tenantItem);
        if (tenantIdentifier == null || tenantCollectionIdentifiers.includes(tenantIdentifier)) {
          return false;
        }
        tenantCollectionIdentifiers.push(tenantIdentifier);
        return true;
      });
      return [...tenantsToAdd, ...tenantCollection];
    }
    return tenantCollection;
  }
}
