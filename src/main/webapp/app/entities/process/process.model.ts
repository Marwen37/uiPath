import { IUpackage } from 'app/entities/upackage/upackage.model';
import { IEnvironment } from 'app/entities/environment/environment.model';
import { Priority } from 'app/entities/enumerations/priority.model';

export interface IProcess {
  id?: number;
  name?: string | null;
  description?: string | null;
  jobPriority?: Priority | null;
  upackages?: IUpackage[] | null;
  environments?: IEnvironment[] | null;
  upackages?: IUpackage[] | null;
  environments?: IEnvironment[] | null;
}

export class Process implements IProcess {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public jobPriority?: Priority | null,
    public upackages?: IUpackage[] | null,
    public environments?: IEnvironment[] | null,
    public upackages?: IUpackage[] | null,
    public environments?: IEnvironment[] | null
  ) {}
}

export function getProcessIdentifier(process: IProcess): number | undefined {
  return process.id;
}
