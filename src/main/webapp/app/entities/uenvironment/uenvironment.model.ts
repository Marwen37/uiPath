import { IUprocess } from 'app/entities/uprocess/uprocess.model';
import { IUrobot } from 'app/entities/urobot/urobot.model';

export interface IUenvironment {
  id?: number;
  name?: string | null;
  description?: string | null;
  uprocess?: IUprocess | null;
  urobots?: IUrobot[] | null;
}

export class Uenvironment implements IUenvironment {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public uprocess?: IUprocess | null,
    public urobots?: IUrobot[] | null
  ) {}
}

export function getUenvironmentIdentifier(uenvironment: IUenvironment): number | undefined {
  return uenvironment.id;
}
