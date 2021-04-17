import { IUrobot } from 'app/entities/urobot/urobot.model';

export interface IMachine {
  id?: number;
  name?: string | null;
  description?: string | null;
  licence?: number | null;
  urobot?: IUrobot | null;
  urobot?: IUrobot | null;
}

export class Machine implements IMachine {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public licence?: number | null,
    public urobot?: IUrobot | null,
    public urobot?: IUrobot | null
  ) {}
}

export function getMachineIdentifier(machine: IMachine): number | undefined {
  return machine.id;
}
