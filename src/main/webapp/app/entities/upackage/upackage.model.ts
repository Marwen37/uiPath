import { IProcess } from 'app/entities/process/process.model';

export interface IUpackage {
  id?: number;
  name?: string | null;
  description?: string | null;
  process?: IProcess | null;
  process?: IProcess | null;
}

export class Upackage implements IUpackage {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public process?: IProcess | null,
    public process?: IProcess | null
  ) {}
}

export function getUpackageIdentifier(upackage: IUpackage): number | undefined {
  return upackage.id;
}
