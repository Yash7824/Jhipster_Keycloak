import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserk, getUserkIdentifier } from '../userk.model';

export type EntityResponseType = HttpResponse<IUserk>;
export type EntityArrayResponseType = HttpResponse<IUserk[]>;

@Injectable({ providedIn: 'root' })
export class UserkService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/userks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userk: IUserk): Observable<EntityResponseType> {
    return this.http.post<IUserk>(this.resourceUrl, userk, { observe: 'response' });
  }

  update(userk: IUserk): Observable<EntityResponseType> {
    return this.http.put<IUserk>(`${this.resourceUrl}/${getUserkIdentifier(userk) as number}`, userk, { observe: 'response' });
  }

  partialUpdate(userk: IUserk): Observable<EntityResponseType> {
    return this.http.patch<IUserk>(`${this.resourceUrl}/${getUserkIdentifier(userk) as number}`, userk, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserk>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserk[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUserkToCollectionIfMissing(userkCollection: IUserk[], ...userksToCheck: (IUserk | null | undefined)[]): IUserk[] {
    const userks: IUserk[] = userksToCheck.filter(isPresent);
    if (userks.length > 0) {
      const userkCollectionIdentifiers = userkCollection.map(userkItem => getUserkIdentifier(userkItem)!);
      const userksToAdd = userks.filter(userkItem => {
        const userkIdentifier = getUserkIdentifier(userkItem);
        if (userkIdentifier == null || userkCollectionIdentifiers.includes(userkIdentifier)) {
          return false;
        }
        userkCollectionIdentifiers.push(userkIdentifier);
        return true;
      });
      return [...userksToAdd, ...userkCollection];
    }
    return userkCollection;
  }
}
