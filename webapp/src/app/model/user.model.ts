import {IdItemModel} from "./idItem.model";

export class UserLoginModel {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  token: string;
  roles: string[];
}

export class UserModel {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  password: string;
  roles: IdItemModel[];
  active: boolean;
}

