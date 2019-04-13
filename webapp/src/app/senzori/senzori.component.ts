import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, MatTableDataSource} from "@angular/material";
import {SenzorViewModel} from "../model/senzor.model";
import {SenzoriService} from "./senzori.service";
import {DijalogService} from "../dijalog/dijalog.service";
import {SenzoriFormComponent} from "./senzori-form/senzori-form.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-senzori',
  templateUrl: './senzori.component.html',
  styleUrls: ['./senzori.component.scss']
})
export class SenzoriComponent implements OnInit, AfterViewInit {
  dataSource = new MatTableDataSource<SenzorViewModel>();
  columnsToDisplay = ['name', 'beaconDataPurgeInterval', 'beaconDataSendInterval', 'roomName', "actions"];

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private senzoriService: SenzoriService,
              private dijalogService: DijalogService,
              private modalService: NgbModal) {
  }

  ngOnInit() {
    this.osvjeziModel();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }

  osvjeziModel() {
    this.senzoriService.dohvatiSveSenzore().subscribe(data => {
      this.dataSource.data = data;
    });
  }

  urediSenzor(senzorId) {
    const modalRef = this.modalService.open(SenzoriFormComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.model = {id: senzorId};
    modalRef.result.then(result => {
      if (result != 0) {
        this.osvjeziModel();
      }
    });
  }

  obrisiSenzor(senzorId) {
    let senzor = this.dataSource.data.find(s => s.id === senzorId);

    this.dijalogService.confirm('Molim vas potvrdite', 'Jeste li sigurni da želite obrisati senzor: ' + senzor.name)
      .then(odluka => {
        if (odluka) {
          this.doDelete(senzorId);
        }
      });
  }

  addSenzor() {
    const modalRef = this.modalService.open(SenzoriFormComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.model = {id: 0};
    modalRef.result.then(result => {
      if (result != 0) {
        this.osvjeziModel();
      }
    });
  }

  private doDelete(senzorId) {
    this.senzoriService.obrisiSenzor(senzorId).subscribe(() => {
      console.log("Uspjesno obrisano");
      this.osvjeziModel();
    }, error => {
      console.log("Error prilikom brisanja senzora: " + JSON.stringify(error));
    })
  }

}
