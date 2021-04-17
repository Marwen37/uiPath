import { IRobot } from 'app/entities/robot/robot.model';
import { IProcess } from 'app/entities/process/process.model';

export interface IEnvironment {
  id?: number;
  name?: string | null;
  description?: string | null;
  robots?: IRobot[] | null;
  process?: IProcess | null;
  process?: IProcess | null;
  robots?: IRobot[] | null;
}

export class Environment implements IEnvironment {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public robots?: IRobot[] | null,
    public process?: IProcess | null,
    public process?: IProcess | null,
    public robots?: IRobot[] | null
  ) {}
}

export function getEnvironmentIdentifier(environment: IEnvironment): number | undefined {
  return environment.id;
}
