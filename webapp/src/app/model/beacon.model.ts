import {UredajViewModel} from "./uredaj.model";

export class BeaconModel {
  id: number;
  uuid: string;
  major: number;
  minor: number;
  devices: UredajViewModel[];
  createFromImage: boolean;

}
