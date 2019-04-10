import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {BeaconModel} from "../model/beacon.model";
import {UredajModel} from "../model/uredaj.model";

@Injectable({
  providedIn: 'root'
})
export class BeaconiService {

  readonly BASE_URL = '/api/beacons';

  constructor(private http: HttpClient) {
  }

  dohvatiSveBeacone(): Observable<any> {
    return this.http.get<BeaconModel[]>(this.BASE_URL).pipe(map(data => {
      return this.processDataFromServer(data);
    }));
  }

  dohvatiBeacon(beaconId: number): Observable<any> {
    return this.http.get<BeaconModel>(`${this.BASE_URL}/${beaconId}`).pipe(map(data => {
      return this.processDataFromServer([data]);
    }));
  }

  obrisiBeacon(beaconId: number): Observable<any> {
    return this.http.delete(`${this.BASE_URL}/${beaconId}`);
  }

  pohraniBeacon(model: BeaconModel): Observable<any> {
    return this.http.post<BeaconModel>(this.BASE_URL, model).pipe(map(data => {
      return this.processDataFromServer([data]);
    }))
  }

  processDataFromServer(data: BeaconModel[]) {
    let processedData: BeaconModel[] = [];

    for (let item of data) {
      if (item.device == null) {
        item.device = new UredajModel();
        item.device.id = 0;
      }
      processedData.push(item);
    }

    return processedData;
  }

}
