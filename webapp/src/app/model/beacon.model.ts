import {UredajModel} from "./uredaj.model";

export class BeaconModel {
  id: number;
  uuid: string;
  major: number;
  minor: number;
  device: UredajModel;
}
