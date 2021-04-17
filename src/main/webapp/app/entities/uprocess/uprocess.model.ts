import { IUpackage } from 'app/entities/upackage/upackage.model';
import { IUenvironment } from 'app/entities/uenvironment/uenvironment.model';
import { Priority } from 'app/entities/enumerations/priority.model';

export interface IUprocess {
  id?: number;
  name?: string | null;
  description?: string | null;
  jobPriority?: Priority | null;
  upackages?: IUpackage[] | null;
  uenvironments?: IUenvironment[] | null;
  upackages?: IUpackage[] | null;
  uenvironments?: IUenvironment[] | null;
}

export class Uprocess implements IUprocess {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public jobPriority?: Priority | null,
    public upackages?: IUpackage[] | null,
    public uenvironments?: IUenvironment[] | null,
    public upackages?: IUpackage[] | null,
    public uenvironments?: IUenvironment[] | null
  ) {}
}

export function getUprocessIdentifier(uprocess: IUprocess): number | undefined {
  return uprocess.id;
}
