import { IUenvironment } from 'app/entities/uenvironment/uenvironment.model';
import { IMachine } from 'app/entities/machine/machine.model';
import { RobotType } from 'app/entities/enumerations/robot-type.model';

export interface IUrobot {
  id?: number;
  name?: string | null;
  description?: string | null;
  type?: RobotType | null;
  domainUsername?: string | null;
  password?: string | null;
  uenvironment?: IUenvironment | null;
  machines?: IMachine[] | null;
}

export class Urobot implements IUrobot {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public type?: RobotType | null,
    public domainUsername?: string | null,
    public password?: string | null,
    public uenvironment?: IUenvironment | null,
    public machines?: IMachine[] | null
  ) {}
}

export function getUrobotIdentifier(urobot: IUrobot): number | undefined {
  return urobot.id;
}
