import {Component, OnInit, ViewChild} from '@angular/core';
import {BeaconModel} from "../../model/beacon.model";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {BeaconiService} from "../beaconi.service";
import {UredajService} from "../../uredaj/uredaj.service";
import {UredajViewModel} from "../../model/uredaj.model";
import {DropdownSettingsBuilder} from "../../settings/utils/dropdown.settings.builder";

@Component({
  selector: 'app-beaconi-form',
  templateUrl: './beaconi-form.component.html',
  styleUrls: ['./beaconi-form.component.scss']
})
export class BeaconiFormComponent implements OnInit {
  @ViewChild('f') form;

  model = new BeaconModel();
  slobodniUredaji: UredajViewModel[];
  dropdownModel: any = {};
  dropdownSettings = {};

  constructor(private activeModal: NgbActiveModal,
              private beaconiService: BeaconiService,
              private uredajService: UredajService) {
  }

  ngOnInit() {
    this.initDropdowns();
    this.uredajService.dohvatiSveSlobodneUredaje().subscribe(data => {
      this.slobodniUredaji = data;
      if (this.model.id != 0) {
        this.initModel();
      } else {
        console.log("Postavlja se novi");
      }
    })
  }

  initModel() {
    if (this.model.id != 0) {
      this.beaconiService.dohvatiBeacon(this.model.id).subscribe(data => {
        this.model = data;
        this.model.devices.forEach(e => {
          this.slobodniUredaji.push(e);
          this.dropdownModel.idUredaj = [e];
        });
      });
    } else {
      console.log("Postavlja se novi");
    }
  }

  initDropdowns() {
    this.dropdownSettings = new DropdownSettingsBuilder()
      .setSingleSelection(true)
      .setShowCheckbox(false)
      .setText('-')
      .setLabelKey('name');
  }

  pohraniBeacon() {
    if (this.form.valid) {
      this.convertDropdownToModel();
      console.log("Ide na server " + JSON.stringify(this.model));
      this.beaconiService.pohraniBeacon(this.model).subscribe(response => {
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
    this.model.devices = [];
    if (this.dropdownModel.idUredaj !== undefined && this.dropdownModel.idUredaj.length > 0) {
      this.model.devices = [this.dropdownModel.idUredaj[0]];
    }
  }

}
