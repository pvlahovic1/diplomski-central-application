import {UserModel} from "./user.model";
import {BeaconModel} from "./beacon.model";

export class UredajModel {
  id: number;
  name: string;
  beacon: BeaconModel;
  user: UserModel;
}
