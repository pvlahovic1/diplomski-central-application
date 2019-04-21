import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {SenzorModel} from "../model/senzor.model";

@Injectable({
  providedIn: 'root'
})
export class SenzoriService {
  readonly BASE_URL = '/api/sensors';

  constructor(private http: HttpClient) { }

  dohvatiSveSenzore(): Observable<any> {
    return this.http.get(this.BASE_URL);
  }

  dohvatiSenzor(idSenzora): Observable<any> {
    return this.http.get(`${this.BASE_URL}/${idSenzora}`);
  }

  pohraniSenzor(model: SenzorModel) : Observable<any> {
    return this.http.post(this.BASE_URL, model);
  }

  obrisiSenzor(idSenzora) {
    return this.http.delete(`${this.BASE_URL}/${idSenzora}`);
  }
}
