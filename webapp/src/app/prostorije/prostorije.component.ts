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

  dropdownModel: any = {};
  dropdownSettings = {};

  constructor(private prostorijeService: ProstorijeService,
              private modalService: NgbModal) {
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
    this.prikaziUcitavanjeSenzora = true;
    this.prostorijeService.dohvatiSveSenzoreProstorije(data.id).subscribe(data => {
      this.senzoriList = data;
      this.prikaziUcitavanjeSenzora = false;
    });
  }

  editProstorija() {
    if (this.dropdownModel.idProstorije !== undefined && this.dropdownModel.idProstorije.length > 0) {
      const modalRef = this.modalService.open(ProstorijeFormComponent, { size: 'lg', backdrop: 'static',windowClass:'app-modal-window animated slideInUp'});
      modalRef.componentInstance.model = {
        id: this.dropdownModel.idProstorije[0].id,
        name: this.dropdownModel.idProstorije[0].itemName
      }
    }
  }

  addProstorija() {
    const modalRef = this.modalService.open(ProstorijeFormComponent,{ size: 'lg', backdrop: 'static',windowClass:'app-modal-window animated slideInUp'});
    modalRef.componentInstance.model = {id: 0};
  }

}
