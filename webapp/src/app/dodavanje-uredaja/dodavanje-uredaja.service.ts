import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DodavanjeUredajaService {

  readonly BASE_URL='/api/image-processing';

  constructor(private http: HttpClient) { }

  processImage(file: FormData): Observable<any> {
    return this.http.post(this.BASE_URL, file);
  }
}
