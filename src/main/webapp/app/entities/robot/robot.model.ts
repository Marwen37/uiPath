import { IMachine } from 'app/entities/machine/machine.model';
import { IEnvironment } from 'app/entities/environment/environment.model';
import { RobotType } from 'app/entities/enumerations/robot-type.model';

export interface IRobot {
  id?: number;
  name?: string | null;
  description?: string | null;
  type?: RobotType | null;
  domainUsername?: string | null;
  password?: string | null;
  machines?: IMachine[] | null;
  environment?: IEnvironment | null;
  environment?: IEnvironment | null;
  machines?: IMachine[] | null;
}

export class Robot implements IRobot {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public type?: RobotType | null,
    public domainUsername?: string | null,
    public password?: string | null,
    public machines?: IMachine[] | null,
    public environment?: IEnvironment | null,
    public environment?: IEnvironment | null,
    public machines?: IMachine[] | null
  ) {}
}

export function getRobotIdentifier(robot: IRobot): number | undefined {
  return robot.id;
}
