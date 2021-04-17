export interface IUjob {
  id?: number;
  name?: string | null;
  description?: string | null;
}

export class Ujob implements IUjob {
  constructor(public id?: number, public name?: string | null, public description?: string | null) {}
}

export function getUjobIdentifier(ujob: IUjob): number | undefined {
  return ujob.id;
}
