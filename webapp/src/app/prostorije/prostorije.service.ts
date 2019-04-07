import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ProstorijaModel} from "../model/prostorija.model";

@Injectable({
  providedIn: 'root'
})
export class ProstorijeService {
  readonly BASE_ROOM_URL = '/api/rooms';
  readonly BASE_SENSOR_URL = '/api/sensors';

  constructor(private http: HttpClient) {
  }

  dohvatiSveProstorije(): Observable<any> {
    return this.http.get(this.BASE_ROOM_URL);
  }

  dohvatiSveSenzoreProstorije(idProstorije: number): Observable<any> {
    return this.http.get(`${this.BASE_ROOM_URL}/${idProstorije}/sensors`);
  }

  dohvatiSveSlobodneSenzore() : Observable<any> {
    return this.http.get(`${this.BASE_SENSOR_URL}/free`);
  }

  pohraniProstoriju(prostorija: ProstorijaModel) {
    return this.http.post(`${this.BASE_ROOM_URL}`, prostorija);
  }
}
