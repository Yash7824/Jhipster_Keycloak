import { IUserk } from 'app/entities/userk/userk.model';

export interface ITenant {
  id?: number;
  name?: string | null;
  userks?: IUserk[] | null;
}

export class Tenant implements ITenant {
  constructor(public id?: number, public name?: string | null, public userks?: IUserk[] | null) {}
}

export function getTenantIdentifier(tenant: ITenant): number | undefined {
  return tenant.id;
}
