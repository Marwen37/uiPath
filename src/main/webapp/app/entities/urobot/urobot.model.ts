import { IMachine } from 'app/entities/machine/machine.model';
import { IUenvironment } from 'app/entities/uenvironment/uenvironment.model';
import { RobotType } from 'app/entities/enumerations/robot-type.model';

export interface IUrobot {
  id?: number;
  name?: string | null;
  description?: string | null;
  type?: RobotType | null;
  domainUsername?: string | null;
  password?: string | null;
  machines?: IMachine[] | null;
  uenvironment?: IUenvironment | null;
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
    public machines?: IMachine[] | null,
    public uenvironment?: IUenvironment | null,
    public uenvironment?: IUenvironment | null,
    public machines?: IMachine[] | null
  ) {}
}

export function getUrobotIdentifier(urobot: IUrobot): number | undefined {
  return urobot.id;
}
