import {Task} from '../task/task';

export class Action {
  id?: string;
  name?: string;
  description?: string;
  startDate?: string | null;
  endDate?: string | null;
  task?: Task
}
