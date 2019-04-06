import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule, RoutingComponents} from './app-routing.module';
import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {HttpSeviceInterceptor} from "./settings/interceptors/http.sevice.interceptor";
import {FormsModule} from "@angular/forms";
import {AngularMultiSelectModule} from "angular2-multiselect-dropdown";
import {AlertModule} from 'ngx-bootstrap';
import {MaterialComponents} from "./settings/material/material.component";

@NgModule({
  declarations: [
    AppComponent,
    RoutingComponents
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    AngularMultiSelectModule,
    FormsModule,
    AlertModule.forRoot(),
    MaterialComponents
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: HttpSeviceInterceptor, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
