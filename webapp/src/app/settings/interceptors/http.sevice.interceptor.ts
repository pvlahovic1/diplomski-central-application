import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable()
export class HttpSeviceInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const url = 'http://localhost:8080';
    const mojH = new HttpHeaders({
      'Authorization': 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzZWN1cmUtYXBpIiwiYXVkIjoic2VjdXJlLWFwcCIsInN1YiI6InB2bGFob3ZpYyIsImV4cCI6MTU1NjAxNDczMSwicm9sIjpbIlNFTlNPUiIsIlVTRVIiXX0.NnQSVCiaoCvMJ8qIMFqxKjMJ3yGJOnsHmhFELHf0M5sqRe3ZqsieLPy6lDRge_-tRmqChzMz3ByvRubmdo6V5w',
      'Content-Type': 'application/json'
    });



    req = req.clone(
      {
        url: url + req.url,
        headers: mojH
      }
    );

    console.log(JSON.stringify(req.headers));


    return next.handle(req);
  }
}
