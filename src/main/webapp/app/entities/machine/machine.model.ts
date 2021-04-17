import { IRobot } from 'app/entities/robot/robot.model';

export interface IMachine {
  id?: number;
  name?: string | null;
  description?: string | null;
  licence?: number | null;
  robot?: IRobot | null;
  robot?: IRobot | null;
}

export class Machine implements IMachine {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public licence?: number | null,
    public robot?: IRobot | null,
    public robot?: IRobot | null
  ) {}
}

export function getMachineIdentifier(machine: IMachine): number | undefined {
  return machine.id;
}
