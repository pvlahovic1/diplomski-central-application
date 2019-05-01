import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {AuthenticationService} from "../service/authentication.service";
import {environment} from "../../../environments/environment";

@Injectable()
export class HttpSeviceInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    req = req.clone({url: environment.apiUrl + req.url});

    if (!req.url.includes(environment.apiAuthenticationUrl)) {
      const token = this.authenticationService.getCurrentUser().token;


      req = req.clone({
        setHeaders: {
          'Authorization': environment.tokenPrefix + token
        }
      });
    }

    return next.handle(req);
  }

}
