import {Component, OnInit, ViewChild} from '@angular/core';
import {BeaconViewModel} from "./model/beacon-view.model";
import {BeaconiService} from "../../beaconi/beaconi.service";
import {DropdownSettingsBuilder} from "../../settings/utils/dropdown.settings.builder";
import {SaveUredajModel} from "./model/save.uredaj.model";

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

  constructor(private beaconService: BeaconiService) { }

  ngOnInit() {
    this.initDropdowns();
    this.beaconService.dohvatiSveSlobodneBeacone().subscribe(data => {
      this.slobodniBeaconi = data;
    });
  }

  initDropdowns() {
    this.dropdownSettings = new DropdownSettingsBuilder()
      .setSingleSelection(true)
      .setShowCheckbox(false)
      .setText('Slobodni beaconi')
  }

}
