import {Component, Input, OnInit, ViewChild} from '@angular/core';

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

  model: ProstorijaModel;
  slobodniSenzori: SenzorViewModel[];
  dropdownModel: any = {};
  dropdownSettings = {};

  constructor(
    public activeModal: NgbActiveModal,
    private prostorijeService: ProstorijeService
  ) {}

  ngOnInit() {
    this.initDropDowns();
    this.prostorijeService.dohvatiSveSlobodneSenzore().subscribe(data => {
      this.slobodniSenzori = data;
      console.log(this.slobodniSenzori);
    });
    if (this.model.id != 0) {
      console.log("Iz modala - Ide dohvat sa servera: " + this.model.id);
    } else {
      console.log("Iz modala - Novo je");
    }
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
        console.log(JSON.stringify(response))
      }, error => {
        console.log(error);
      });
    } else {
      console.log("invalid");
    }
  }

  convertDropdownToModel() {
    if (this.dropdownModel.idSenzora !== undefined && this.dropdownModel.idSenzora.length > 0) {
      this.model.sensors = [];
      this.dropdownModel.idSenzora.forEach(e => {
        this.model.sensors.push(e);
      });
    }
  }

}
