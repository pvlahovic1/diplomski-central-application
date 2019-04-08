import {Component, OnInit, ViewChild} from '@angular/core';

import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {ProstorijaModel} from "../../model/prostorija.model";
import {SenzorViewModel} from "../../model/senzor.view.model";
import {ProstorijeService} from "../prostorije.service";
import {DropdownSettingsBuilder} from "../../settings/utils/dropdown.settings.builder";

@Component({
  selector: 'app-prostorije-form',
  templateUrl: './prostorije-form.component.html',
  styleUrls: ['./prostorije-form.component.scss']
})
export class ProstorijeFormComponent implements OnInit {
  @ViewChild('f') form;

  model = new ProstorijaModel();
  slobodniSenzori: SenzorViewModel[];
  dropdownModel: any = {};
  dropdownSettings = {};

  constructor(
    public activeModal: NgbActiveModal,
    private prostorijeService: ProstorijeService
  ) {}

  ngOnInit() {
    this.dropdownModel.idSenzora = [];
    this.initDropDowns();
    this.prostorijeService.dohvatiSveSlobodneSenzore().subscribe(data => {
      this.slobodniSenzori = data;
      if (this.model.id != 0) {
        this.initModel();
      } else {
        console.log("Iz modala - Novo je");
      }
    });
  }

  initModel() {
    this.prostorijeService.dohvatiProstoriju(this.model.id).subscribe(data => {
      this.model = data;
      console.log("Dohvat sa servera: " + JSON.stringify(data));
      this.model.sensors.forEach(e => {
        this.slobodniSenzori.push(e);
        this.dropdownModel.idSenzora.push(e);
        console.log("Nakon postavljanja forme: " + JSON.stringify(this.model));
        console.log("Nakon postavljanja forme: " + JSON.stringify(this.slobodniSenzori));
        console.log("Nakon postavljanja forme: " + JSON.stringify(this.dropdownModel));
      });
    });
  }

  initDropDowns() {
    this.dropdownSettings = new DropdownSettingsBuilder()
      .setSingleSelection(false)
      .setShowCheckbox(true)
      .setText('Slobodni senzori')
      .setLabelKey('name');
  }

  saveProstorija() {
    if (this.form.valid) {
      this.convertDropdownToModel();
      console.log(JSON.stringify(this.model));
      this.prostorijeService.pohraniProstoriju(this.model).subscribe(response => {
        console.log(JSON.stringify(response));
        this.activeModal.close(response.id);
      }, error => {
        console.log(error);
      });
    } else {
      console.log("invalid");
    }
  }

  convertDropdownToModel() {
    console.log("onvert dropdown to modell");
    this.model.sensors = [];
    if (this.dropdownModel.idSenzora !== undefined && this.dropdownModel.idSenzora.length > 0) {
      this.dropdownModel.idSenzora.forEach(e => {
        this.model.sensors.push(e);
      });
    }
  }

}
