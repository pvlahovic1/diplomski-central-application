import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule, RoutingComponents} from './app-routing.module';
import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {HttpSeviceInterceptor} from "./settings/interceptors/http.sevice.interceptor";
import {FormsModule} from "@angular/forms";
import {AngularMultiSelectModule} from "angular2-multiselect-dropdown";
import {AlertModule, ButtonsModule} from 'ngx-bootstrap';
import {MaterialComponents} from "./settings/material/material.component";
import {ProstorijeFormComponent} from "./prostorije/prostorije-form/prostorije-form.component";
import {NgbActiveModal, NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {MatPaginatorModule} from "@angular/material";
import {MomentModule} from "angular2-moment";

@NgModule({
  declarations: [
    AppComponent,
    RoutingComponents,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    AngularMultiSelectModule,
    FormsModule,
    AlertModule.forRoot(),
    ButtonsModule.forRoot(),
    NgbModule,
    MaterialComponents,
    MatPaginatorModule,
    MomentModule
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: HttpSeviceInterceptor, multi: true},
    NgbActiveModal],
  bootstrap: [AppComponent],
  entryComponents: [
    ProstorijeFormComponent
  ]
})
export class AppModule {
}
