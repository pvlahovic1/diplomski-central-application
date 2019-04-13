import {Component, OnInit, ViewChild} from '@angular/core';
import {BeaconViewModel} from "./model/beacon-view.model";
import {BeaconiService} from "../../beaconi/beaconi.service";
import {DropdownSettingsBuilder} from "../../settings/utils/dropdown.settings.builder";
import {SaveUredajModel} from "./model/save.uredaj.model";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {UredajService} from "../uredaj.service";

@Component({
  selector: 'app-uredaj-form',
  templateUrl: './uredaj-form.component.html',
  styleUrls: ['./uredaj-form.component.scss']
})
export class UredajFormComponent implements OnInit {
  @ViewChild('f') form;
  model = new SaveUredajModel();
  slobodniBeaconi: BeaconViewModel[];
  dropdownModel: any = {};
  dropdownSettings = {};

  constructor(private beaconService: BeaconiService,
              private uredajService: UredajService,
              private activeModal: NgbActiveModal ) { }

  ngOnInit() {
    this.initDropdowns();
    this.beaconService.dohvatiSveSlobodneBeacone().subscribe(data => {
      this.slobodniBeaconi = data;
      if (this.model.id != 0) {
        this.initModel();
      } else {
        console.log("Iz modala - Novo je");
      }
    });
  }

  initModel() {
    this.uredajService.dohvatiUredaj(this.model.id).subscribe(data => {
      this.model = data;
      if (this.model.beaconView != null) {
        this.slobodniBeaconi.push(this.model.beaconView);
        this.dropdownModel.idBeacon = [this.model.beaconView];
      }
    })
  }

  initDropdowns() {
    this.dropdownSettings = new DropdownSettingsBuilder()
      .setSingleSelection(true)
      .setShowCheckbox(false)
      .setText('Slobodni beaconi')
  }

  saveUredaj() {
    if (this.form.valid) {
      this.convertDropdownToModel();
      console.log(JSON.stringify(this.model));
      this.uredajService.pohraniUredaj(this.model).subscribe(response => {
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
    this.model.beaconView = null;
    if (this.dropdownModel.idBeacon !== undefined && this.dropdownModel.idBeacon.length > 0) {
      this.model.beaconView = this.dropdownModel.idBeacon[0];
    }
  }

}
