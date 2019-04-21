import {SenzorViewModel} from "./senzor.model";

export class ProstorijaModel {
  id: number;
  name: string;
  length: number;
  width: number;
  height: number;
  sensors: SenzorViewModel[];
}
