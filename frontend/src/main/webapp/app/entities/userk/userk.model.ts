import { ITenant } from 'app/entities/tenant/tenant.model';

export interface IUserk {
  id?: number;
  username?: string;
  email?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  tenant?: ITenant | null;
}

export class Userk implements IUserk {
  constructor(
    public id?: number,
    public username?: string,
    public email?: string | null,
    public firstName?: string | null,
    public lastName?: string | null,
    public tenant?: ITenant | null
  ) {}
}

export function getUserkIdentifier(userk: IUserk): number | undefined {
  return userk.id;
}
