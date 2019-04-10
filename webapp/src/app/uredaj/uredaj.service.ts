import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UredajService {
  readonly  BASE_URL = '/api/devices';

  constructor(private http: HttpClient) { }

  dohvatiSveSlobodneUredaje(): Observable<any> {
    return this.http.get(`${this.BASE_URL}/free`);
  }
}
