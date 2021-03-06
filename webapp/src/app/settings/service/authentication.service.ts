import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {UserLoginModel} from "../../model/user.model";
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  readonly PRIJAVLJENI_KORISNIK_STORAGE = 'prijavljeniKorisnik';

  private currentUserSubject: BehaviorSubject<UserLoginModel>;
  public currentUser: Observable<UserLoginModel>;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<UserLoginModel>(JSON.parse(localStorage
      .getItem(this.PRIJAVLJENI_KORISNIK_STORAGE)));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public getCurrentUser(): UserLoginModel {
    return this.currentUserSubject.value;
  }

  public getCurrentRoles(): string[] {
    if (this.currentUserSubject.value) {
      return this.currentUserSubject.value.roles;
    } else {
      return [];
    }
  }

  public hasRole(role: string[]) {
    return role.some(r=> this.getCurrentRoles().includes(r));
  }


  login(username: string, password: string) {
    return this.http.post<any>(`/api/authenticate?username=${username}&password=${password}`, null)
      .pipe(map(user => {
        if (user && user.token) {
          localStorage.setItem(this.PRIJAVLJENI_KORISNIK_STORAGE, JSON.stringify(user));
          this.currentUserSubject.next(user);
        }

        return user;
      }));
  }

  logout() {
    localStorage.removeItem(this.PRIJAVLJENI_KORISNIK_STORAGE);
    this.currentUserSubject.next(null);
  }

}
