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
import {DijalogComponent} from "./dijalog/dijalog.component";
import {SenzoriFormComponent} from "./senzori/senzori-form/senzori-form.component";
import {BeaconiFormComponent} from "./beaconi/beaconi-form/beaconi-form.component";
import {UredajFormComponent} from "./uredaj/uredaj-form/uredaj-form.component";
import {KorisniciFormComponent} from "./korisnici/korisnici-form/korisnici-form.component";
import {DodavanjeUredajaComponent} from "./dodavanje-uredaja/dodavanje-uredaja.component";

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
  },
  {
    path: 'dijalog',
    component: DijalogComponent
  },
  {
    path: 'senzori-from',
    component: SenzoriFormComponent
  },
  {
    path: 'beaconi-from',
    component: BeaconiFormComponent
  },
  {
    path: 'uredaj-from',
    component: UredajFormComponent
  },
  {
    path: 'korisnici-from',
    component: KorisniciFormComponent
  },
  {
    path: 'dodvanje-uredaja-from',
    component: DodavanjeUredajaComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

export const RoutingComponents = [HeadersComponent, BeaconiComponent, KorisniciComponent, ProstorijeComponent,
  SenzoriComponent, UredajComponent, PrijavaComponent, ProstorijeFormComponent, DijalogComponent, SenzoriFormComponent,
  BeaconiFormComponent, UredajFormComponent, KorisniciFormComponent, DodavanjeUredajaComponent];
