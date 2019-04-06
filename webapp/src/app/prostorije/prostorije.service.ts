import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ProstorijeService {
  readonly BASE_URL = '/api/rooms';

  constructor(private http: HttpClient) {
  }

  dohvatiSveProstorije(): Observable<any> {
    return this.http.get(this.BASE_URL);
  }

  dohvatiSveSenzoreProstorije(idProstorije: number): Observable<any> {
    return this.http.get(`${this.BASE_URL}/${idProstorije}/sensors`);
  }
}
