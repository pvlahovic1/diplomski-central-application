import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, MatTableDataSource} from "@angular/material";
import {SenzorViewModel} from "../model/senzor.model";
import {SenzoriService} from "./senzori.service";
import {DijalogService} from "../dijalog/dijalog.service";
import {SenzoriFormComponent} from "./senzori-form/senzori-form.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {WebSocketService} from "../settings/service/web-socket.service";
import {AuthenticationService} from "../settings/service/authentication.service";

@Component({
  selector: 'app-senzori',
  templateUrl: './senzori.component.html',
  styleUrls: ['./senzori.component.scss']
})
export class SenzoriComponent implements OnInit, AfterViewInit {
  dataSource = new MatTableDataSource<SenzorViewModel>();
  columnsToDisplay = [];
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private senzoriService: SenzoriService,
              private dijalogService: DijalogService,
              private modalService: NgbModal,
              private webSocketService: WebSocketService,
              private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    if (this.authenticationService.hasRole(['ROLE_ADMIN'])) {
      this.columnsToDisplay = ['name', 'beaconDataPurgeInterval', 'beaconDataSendInterval', 'roomName', 'actions'];
    } else {
      this.columnsToDisplay = ['name', 'beaconDataPurgeInterval', 'beaconDataSendInterval', 'roomName'];
    }

    this.osvjeziModel();
    this.webSocketInit();
  }

  webSocketInit() {
    this.webSocketService.sensorSubject.subscribe(data => {
      const present = this.dataSource.data.find(e => e.id === Number(data));

      if (present !== undefined) {
        this.osvjeziModel();
      }
    })
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

  secondsToTime(seconds: number) {
    const d = new Date(0, 0, 0, 0, 0, 0, 0);
    d.setSeconds(seconds);
    return d;
  }

}
