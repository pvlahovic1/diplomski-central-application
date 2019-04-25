import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material";
import {UserModel} from "../model/user.model";
import {KorisniciService} from "./korisnici.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {KorisniciFormComponent} from "./korisnici-form/korisnici-form.component";
import {AuthenticationService} from "../settings/service/authentication.service";

@Component({
  selector: 'app-korisnici',
  templateUrl: './korisnici.component.html',
  styleUrls: ['./korisnici.component.scss']
})
export class KorisniciComponent implements OnInit {

  dataSource = new MatTableDataSource<UserModel>();
  columnsToDisplay = ['username', 'firstName', 'lastName', 'roles', 'active', 'actions_col'];

  constructor(private korisniciService: KorisniciService,
              private modalService: NgbModal,
              private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    this.osvjeziModel();
  }

  addKorisnik() {
    const modalRef = this.modalService.open(KorisniciFormComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.model = {id: 0};
    modalRef.result.then(result => {
      if (result != 0) {
        this.osvjeziModel();
      }
    });
  }

  dohvatiRole(korisnik: UserModel) {
    let role = "";
    if (korisnik.roles.length > 0) {
      korisnik.roles.forEach(e => role = role + ' [' + e.itemName + '] ');
    } else {
      role = 'Nema rola';
    }

    return role;
  }

  dohvatiAktivnost(korisnik: UserModel) {
    if (korisnik.active) {
      return "Aktivan";
    } else {
      return "Neaktivan";
    }
  }

  urediKorisnika(idKorisnik: number) {
    const modalRef = this.modalService.open(KorisniciFormComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.model = {id: idKorisnik};
    modalRef.result.then(result => {
      if (result != 0) {
        this.osvjeziModel();
      }
    });
  }

  obrisiKorisnika(idKorisnik: number) {
    this.korisniciService.obrisiKorisnika(idKorisnik).subscribe(() => {
      this.osvjeziModel();
    }, error => {
      console.log(error);
    });
  }

  omoguciBrisanje(korisnik: UserModel) {
    return this.authenticationService.getCurrentUser().username !== korisnik.username;
  }

  private osvjeziModel() {
    this.korisniciService.dohvatiSveKorisnike().subscribe(data => {
      this.dataSource.data = data;
    });
  }

}
