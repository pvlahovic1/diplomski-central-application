import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {AuthenticationService} from "../service/authentication.service";

@Injectable()
export class HttpSeviceInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    //const url = 'http://153.92.209.230:8080';
    const url = 'http://localhost:8080';

    req = req.clone({url: url + req.url});

    if (!req.url.includes('/api/authenticate')) {
      console.log(JSON.stringify(req.url));
      const token = this.authenticationService.getCurrentUser().token;

      console.log("ZAhtjev s novim tokenom: " + token);

      const authHeaders = new HttpHeaders({
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
      });

      req = req.clone({headers: authHeaders});
    }

    return next.handle(req);
  }

}
