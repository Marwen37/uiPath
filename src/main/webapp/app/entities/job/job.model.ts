export interface IJob {
  id?: number;
  name?: string | null;
  description?: string | null;
}

export class Job implements IJob {
  constructor(public id?: number, public name?: string | null, public description?: string | null) {}
}

export function getJobIdentifier(job: IJob): number | undefined {
  return job.id;
}
