import {Component, OnInit} from '@angular/core';
import {ProstorijeService} from "./prostorije.service";
import {ProstorijaModel} from "../model/prostorija.model";
import {DropdownSettingsBuilder} from "../settings/utils/dropdown.settings.builder";
import {SenzorViewModel} from "../model/senzor.view.model";

@Component({
  selector: 'app-prostorije',
  templateUrl: './prostorije.component.html',
  styleUrls: ['./prostorije.component.scss']
})
export class ProstorijeComponent implements OnInit {
  prostorije: ProstorijaModel[];
  senzoriList: SenzorViewModel[];
  columnsToDisplay = ['name', 'beaconDataPurgeInterval', 'beaconDataSendInterval'];
  prikaziUcitavanjeSenzora = false;

  dropdownModel = [];
  dropdownSettings = {};

  constructor(private prostorijeService: ProstorijeService) {
  }

  ngOnInit() {
    this.prostorije = [];
    this.senzoriList = [];
    this.initDropDowns();
    this.prostorijeService.dohvatiSveProstorije().subscribe(data => {
      this.prostorije = data;
    });
  }

  initDropDowns() {
    this.dropdownSettings = new DropdownSettingsBuilder()
      .setShowCheckbox(false)
      .setSingleSelection(true)
      .setSearchPlaceholderText('Pretražite prostorije')
      .setText("Odaberite prostoriju").build();
  }

  onItemSelect(data) {
    console.log("Odabrano: " + JSON.stringify(data));
    this.prikaziUcitavanjeSenzora = true;
    this.prostorijeService.dohvatiSveSenzoreProstorije(data.id).subscribe(data => {
      this.senzoriList = data;
      this.prikaziUcitavanjeSenzora = false;
    });
  }

}
