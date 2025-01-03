import {User} from '../user/user';

export class Task {
  id?: string;
  name?: string;
  description?: string;
  startDate?: string | null;
  endDate?: string | null;
  initiator?: User
}
