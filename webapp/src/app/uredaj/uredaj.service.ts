import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {SaveUredajModel} from "./uredaj-form/model/save.uredaj.model";

@Injectable({
  providedIn: 'root'
})
export class UredajService {
  readonly  BASE_URL = '/api/devices';

  constructor(private http: HttpClient) { }

  dohvatiSveUredaje(): Observable<any> {
    return this.http.get(this.BASE_URL);
  }

  dohvatiSveSlobodneUredaje(): Observable<any> {
    return this.http.get(`${this.BASE_URL}/free`);
  }

  dohvatiUredaj(idUredaj: number) : Observable<any> {
    return this.http.get(`${this.BASE_URL}/${idUredaj}`);
  }

  pohraniUredaj(model: SaveUredajModel): Observable<any> {
    return this.http.post(this.BASE_URL, model);
  }

  obrisiUredaj(idUredaj) {
    return this.http.delete(`${this.BASE_URL}/${idUredaj}`);
  }

}
