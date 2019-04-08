import {SenzorViewModel} from "./senzor.view.model";

export class ProstorijaModel {
  id: number;
  name: string;
  length: number;
  width: number;
  height: number;
  sensors: SenzorViewModel[];
}
