import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SenzoriService {
  readonly BASE_URL = '/api/sensors';

  constructor(private http: HttpClient) { }

  dohvatiSveSenzore(): Observable<any> {
    return this.http.get(this.BASE_URL);
  }

  obrisiSenzor(idSenzora) {
    return this.http.delete(`${this.BASE_URL}/${idSenzora}`);
  }
}
