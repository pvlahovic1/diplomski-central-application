import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {BeaconModel} from "../model/beacon.model";
import {observableToBeFn} from "rxjs/internal/testing/TestScheduler";

@Injectable({
  providedIn: 'root'
})
export class BeaconiService {

  readonly BASE_URL = '/api/beacons';

  constructor(private http: HttpClient) {
  }

  dohvatiSveBeacone(): Observable<any> {
    return this.http.get<BeaconModel[]>(this.BASE_URL);
  }

  dohvatiBeacon(beaconId: number): Observable<any> {
    return this.http.get<BeaconModel>(`${this.BASE_URL}/${beaconId}`);
  }

  obrisiBeacon(beaconId: number): Observable<any> {
    return this.http.delete(`${this.BASE_URL}/${beaconId}`);
  }

  pohraniBeacon(model: BeaconModel): Observable<any> {
    return this.http.post<BeaconModel>(this.BASE_URL, model);
  }

  dohvatiSveSlobodneBeacone(): Observable<any> {
    return this.http.get(`${this.BASE_URL}/free`);
  }

}
