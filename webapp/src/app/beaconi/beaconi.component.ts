import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, MatTableDataSource} from "@angular/material";
import {SenzorViewModel} from "../model/senzor.model";
import {DijalogService} from "../dijalog/dijalog.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {BeaconiService} from "./beaconi.service";
import {BeaconModel} from "../model/beacon.model";
import {SenzoriFormComponent} from "../senzori/senzori-form/senzori-form.component";
import {BeaconiFormComponent} from "./beaconi-form/beaconi-form.component";

@Component({
  selector: 'app-beaconi',
  templateUrl: './beaconi.component.html',
  styleUrls: ['./beaconi.component.scss']
})
export class BeaconiComponent implements OnInit, AfterViewInit {
  dataSource = new MatTableDataSource<BeaconModel>();
  columnsToDisplay = ['uuid_col', 'major_col', 'minor_col', 'device_col', 'actions_col'];

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private beaconService: BeaconiService,
              private dijalogService: DijalogService,
              private modalService: NgbModal) { }

  ngOnInit() {
    this.osvjeziModel();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }

  osvjeziModel() {
    this.beaconService.dohvatiSveBeacone().subscribe(data => {
      this.dataSource.data = data;
    });
  }

  addBeacon() {
    const modalRef = this.modalService.open(BeaconiFormComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.model = {id: 0};
    modalRef.result.then(result => {
      if (result != 0) {
        this.osvjeziModel();
      }
    });
  }

  urediBeacon(idBeacon) {
    const modalRef = this.modalService.open(BeaconiFormComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.model = {id: idBeacon};
    modalRef.result.then(result => {
      if (result != 0) {
        this.osvjeziModel();
      }
    });
  }

  obrisiBeacon(idBeacon) {
    let beacon = this.dataSource.data.find(b => b.id === idBeacon);

    this.dijalogService.confirm('Molim vas potvrdite', 'Jeste li sigurni da želite obrisati beacon: ' + beacon.uuid)
      .then(odluka => {
        if (odluka) {
          this.doDelete(idBeacon);
        }
      });
  }

  private doDelete(idBeacon) {
    this.beaconService.obrisiBeacon(idBeacon).subscribe(() => {
      console.log("Uspjesno obrisano");
      this.osvjeziModel();
    }, error => {
      console.log("Error prilikom brisanja senzora: " + JSON.stringify(error));
    })
  }

}
