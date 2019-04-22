import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {ProstorijeService} from "./prostorije.service";
import {ProstorijaViewModel} from "../model/prostorija.view.model";
import {DropdownSettingsBuilder} from "../settings/utils/dropdown.settings.builder";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ProstorijeFormComponent} from "./prostorije-form/prostorije-form.component";
import {MatPaginator, MatTableDataSource} from "@angular/material";
import {SenzorViewModel} from "../model/senzor.model";
import {UredajProstorijaModel} from "../model/uredaj.model";
import {WebSocketService} from "../settings/service/web-socket.service";

@Component({
  selector: 'app-prostorije',
  templateUrl: './prostorije.component.html',
  styleUrls: ['./prostorije.component.scss']
})
export class ProstorijeComponent implements OnInit, AfterViewInit {
  prostorije: ProstorijaViewModel[];
  dataSourceSenzorView = new MatTableDataSource<SenzorViewModel>();
  dataSourceUredajView = new MatTableDataSource<UredajProstorijaModel>();

  columnsToDisplay = ['name', 'beaconDataPurgeInterval', 'beaconDataSendInterval'];
  columnsToDisplayDevices = ['name', 'beaconData', 'time', 'distance'];
  @ViewChild(MatPaginator) paginator: MatPaginator;

  prikaziUcitavanjeSenzora = false;
  refreshDropDown = false;

  dropdownModel: any = {};
  dropdownSettings = {};

  constructor(private prostorijeService: ProstorijeService,
              private modalService: NgbModal,
              private webSocketService: WebSocketService) {
  }

  ngOnInit() {
    this.prostorije = [];
    this.initProstorije();
    this.initDropDowns();
    this.initWebSockets();
  }

  ngAfterViewInit(): void {
    this.dataSourceSenzorView.paginator = this.paginator;
  }

  initWebSockets() {
    this.webSocketService.roomSubject.subscribe(data => {
      const idSobe = JSON.parse(data);

      console.log("Potrebno azurirati sobu: " + idSobe[0]);
      if (this.dropdownModel.idProstorije !== undefined && this.dropdownModel.idProstorije.length > 0) {
        const present = this.dropdownModel.idProstorije.find(e => e.id == idSobe[0]);

        if (present !== undefined) {
          this.dohvatiSveUredajeUProstoriji(present);
        }
      }
    });
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
      this.dataSourceSenzorView.data = data;
      this.prikaziUcitavanjeSenzora = false;
    });

    this.dohvatiSveUredajeUProstoriji(data);
  }

  dohvatiSveUredajeUProstoriji(data) {
    this.prostorijeService.dohvatiSveUredajeUProstoji(data.id).subscribe(data => {
      this.dataSourceUredajView.data = data;
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

  deleteProstorija() {
    if (this.dropdownModel.idProstorije !== undefined && this.dropdownModel.idProstorije.length > 0) {
      this.prostorijeService.obrisiProstoriju(this.dropdownModel.idProstorije[0].id).subscribe(() => {
        console.log("Uspjesno brisanje");
        this.initProstorije();
        this.dropdownModel.idProstorije = [];
      }, error => {
        console.log(JSON.stringify(error));
      });
    }
  }
  secondsToTime(seconds: number) {
    const d = new Date(0,0,0,0,0,0,0);
    d.setSeconds(seconds);
    return d;
  }

}
