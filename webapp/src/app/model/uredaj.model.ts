import {Moment} from "moment";

export class UredajViewModel {
  id: number;
  name: string;
  beaconData: string;
}

export class UredajProstorijaModel {
  deviceInfo: UredajViewModel;
  dateTime: Moment;
  distance: number;
}
