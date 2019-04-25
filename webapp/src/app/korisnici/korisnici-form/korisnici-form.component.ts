import {Component, OnInit, ViewChild} from '@angular/core';
import {UserModel} from "../../model/user.model";
import {IdItemModel} from "../../model/idItem.model";
import {KorisniciService} from "../korisnici.service";
import {DropdownSettingsBuilder} from "../../settings/utils/dropdown.settings.builder";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {AuthenticationService} from "../../settings/service/authentication.service";

@Component({
  selector: 'app-korisnici-form',
  templateUrl: './korisnici-form.component.html',
  styleUrls: ['./korisnici-form.component.scss']
})
export class KorisniciFormComponent implements OnInit {
  @ViewChild('f') form;

  model = new UserModel();
  dostupneRole: IdItemModel[];
  dropdownModel: any = {};
  dropdownSettings = {};

  constructor(private korisniciService: KorisniciService,
              private activeModal: NgbActiveModal,
              private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    this.initDropdowns();
    this.korisniciService.dohvatiSveDostupneRole().subscribe(data => {
      this.dostupneRole = data;
      if (this.model.id != 0) {
        this.initModel();
      } else {
        console.log("Postavlja se novi");
      }
    });
  }

  initModel() {
    if (this.model.id != 0) {
      this.korisniciService.dohvatiKorisnika(this.model.id).subscribe(data => {
        this.model = data;
        this.dropdownModel.idRole = this.model.roles;
      });
    } else {
      console.log("Postavlja se novi");
    }
  }

  initDropdowns() {
    this.dropdownSettings = new DropdownSettingsBuilder()
      .setSingleSelection(false)
      .setShowCheckbox(true)
      .setText('-');
  }

  pohraniKorisnika() {
    if (this.form.valid) {
      this.convertDropdownToModel();
      console.log("Ide na server " + JSON.stringify(this.model));
      this.korisniciService.pohraniKorisnika(this.model).subscribe(response => {
        console.log(JSON.stringify(response));
        this.activeModal.close(response.id);
      }, error => {
        console.log(JSON.stringify(error));
      });
    } else {
      console.log("Forma nije validna");
    }
  }

  convertDropdownToModel() {
    this.model.roles = [];
    if (this.dropdownModel.idRole !== undefined && this.dropdownModel.idRole.length > 0) {
      this.model.roles = this.dropdownModel.idRole;
    }
  }

  showChnageStatusButtons() {
    if (this.model.id === 0) {
      return false;
    } else {
      return this.authenticationService.getCurrentUser().username !== this.model.username;
    }
  }

  aktivirajKorisnika() {
    console.log("Aktiviraj korisnika");
    this.korisniciService.aktivirajKorisnika(this.model.id).subscribe(data => {
      console.log(data);
      this.model = data;
      this.activeModal.close(data.id);
    }, error => {
      console.log(JSON.stringify(error));
    });
  }

  deaktivirajKorisnika() {
    console.log("Deaktiviraj korisnika");
    this.korisniciService.deaktivirajKorisnika(this.model.id).subscribe(data => {
      console.log(data);
      this.model = data;
      this.activeModal.close(data.id);
    }, error => {
      console.log(JSON.stringify(error));
    });
  }

}
