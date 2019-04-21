import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {AuthenticationService} from "../service/authentication.service";
import {Observable, throwError} from "rxjs";
import {catchError} from "rxjs/operators";
import {Router} from "@angular/router";

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService,
              private router: Router ) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(catchError(err => {
      if (err.status === 401 || err.status === 403) {
        this.authenticationService.logout();
        this.router.navigate(['/prijava']);
      }

      const error = err.error.message || err.statusText;
      return throwError(error);
    }))
  }

}
