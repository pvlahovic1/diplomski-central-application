import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {BeaconiComponent} from "./beaconi/beaconi.component";
import {KorisniciComponent} from "./korisnici/korisnici.component";
import {ProstorijeComponent} from "./prostorije/prostorije.component";
import {SenzoriComponent} from "./senzori/senzori.component";
import {UredajComponent} from "./uredaj/uredaj.component";
import {PrijavaComponent} from "./prijava/prijava.component";
import {HeadersComponent} from "./headers/headers.component";
import {ProstorijeFormComponent} from "./prostorije/prostorije-form/prostorije-form.component";

const routes: Routes = [
  {
    path: 'beaconi',
    component: BeaconiComponent
  },
  {
    path: 'korisnici',
    component: KorisniciComponent
  },
  {
    path: 'prostorije',
    component: ProstorijeComponent
  },
  {
    path: 'senzori',
    component: SenzoriComponent
  },
  {
    path: 'uredaji',
    component: UredajComponent
  },
  {
    path: 'prijava',
    component: PrijavaComponent
  },
  {
    path: 'prostorije-form',
    component: ProstorijeFormComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

export const RoutingComponents = [HeadersComponent, BeaconiComponent, KorisniciComponent, ProstorijeComponent,
  SenzoriComponent, UredajComponent, PrijavaComponent, ProstorijeFormComponent];
