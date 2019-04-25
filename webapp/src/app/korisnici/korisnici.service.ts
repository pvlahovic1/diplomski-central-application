import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserModel} from "../model/user.model";

@Injectable({
  providedIn: 'root'
})
export class KorisniciService {

  readonly BASE_URL = '/api/users';
  readonly ROLES_URL = '/api/roles';

  constructor(private http: HttpClient) { }

  dohvatiSveKorisnike(): Observable<any> {
    return this.http.get(this.BASE_URL);
  }

  dohvatiSveDostupneRole(): Observable<any> {
    return this.http.get(this.ROLES_URL);
  }

  dohvatiKorisnika(idKorisnik: number): Observable<any> {
    return this.http.get(`${this.BASE_URL}/${idKorisnik}`);
  }

  pohraniKorisnika(korisnik: UserModel): Observable<any> {
    return this.http.post(`${this.BASE_URL}`, korisnik);
  }

  aktivirajKorisnika(idKorisnik: number): Observable<any> {
    return this.http.put(`${this.BASE_URL}/${idKorisnik}/activate`, null);
  }

  deaktivirajKorisnika(idKorisnik: number): Observable<any> {
    return this.http.put(`${this.BASE_URL}/${idKorisnik}/deactivate`, null);
  }

  obrisiKorisnika(idKorisnik: number): Observable<any> {
    return this.http.delete(`${this.BASE_URL}/${idKorisnik}`);
  }



}
