import { IUprocess } from 'app/entities/uprocess/uprocess.model';

export interface IUpackage {
  id?: number;
  name?: string | null;
  description?: string | null;
  uprocess?: IUprocess | null;
  uprocess?: IUprocess | null;
}

export class Upackage implements IUpackage {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public uprocess?: IUprocess | null,
    public uprocess?: IUprocess | null
  ) {}
}

export function getUpackageIdentifier(upackage: IUpackage): number | undefined {
  return upackage.id;
}
