import {Component, OnInit} from '@angular/core';
import {ProstorijeService} from "./prostorije.service";
import {ProstorijaViewModel} from "../model/prostorija.view.model";
import {DropdownSettingsBuilder} from "../settings/utils/dropdown.settings.builder";
import {SenzorViewModel} from "../model/senzor.view.model";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ProstorijeFormComponent} from "./prostorije-form/prostorije-form.component";

@Component({
  selector: 'app-prostorije',
  templateUrl: './prostorije.component.html',
  styleUrls: ['./prostorije.component.scss']
})
export class ProstorijeComponent implements OnInit {
  prostorije: ProstorijaViewModel[];
  senzoriList: SenzorViewModel[];
  columnsToDisplay = ['name', 'beaconDataPurgeInterval', 'beaconDataSendInterval'];
  prikaziUcitavanjeSenzora = false;
  refreshDropDown = false;

  dropdownModel: any = {};
  dropdownSettings = {};

  constructor(private prostorijeService: ProstorijeService,
              private modalService: NgbModal) {
  }

  ngOnInit() {
    this.prostorije = [];
    this.senzoriList = [];
    this.initProstorije();
    this.initDropDowns();
  }

  initProstorije() {
    this.prostorijeService.dohvatiSveProstorije().subscribe(data => {
      this.prostorije = data;
      this.refreshDropdown();
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
    this.prikaziUcitavanjeSenzora = true;
    this.prostorijeService.dohvatiSveSenzoreProstorije(data.id).subscribe(data => {
      this.senzoriList = data;
      this.prikaziUcitavanjeSenzora = false;
    });
  }

  refreshDropdown() {
    if (this.refreshDropDown == true) {
      if (this.dropdownModel.idProstorije !== undefined && this.dropdownModel.idProstorije.length > 0) {
        this.dropdownModel.idProstorije = [this.prostorije.find(e => e.id == this.dropdownModel.idProstorije[0].id)];
      }
    }

    this.refreshDropDown = false;
  }

  editProstorija() {
    if (this.dropdownModel.idProstorije !== undefined && this.dropdownModel.idProstorije.length > 0) {
      const modalRef = this.modalService.open(ProstorijeFormComponent, {size: 'lg', backdrop: 'static'});
      modalRef.componentInstance.model = {
        id: this.dropdownModel.idProstorije[0].id,
        name: this.dropdownModel.idProstorije[0].itemName
      };

      modalRef.result.then(result => {
        if (result != 0) {
          this.onItemSelect({id: result});
          this.initProstorije();
          this.refreshDropDown = true;
        }
      });
    }
  }

  addProstorija() {
    const modalRef = this.modalService.open(ProstorijeFormComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.model = {id: 0};
    modalRef.result.then(result => {
      if (result != 0) {
        this.initProstorije();
      }
    });
  }

}
