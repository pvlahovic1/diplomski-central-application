import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, MatTableDataSource} from "@angular/material";
import {UredajViewModel} from "../model/uredaj.model";
import {UredajService} from "./uredaj.service";
import {UredajFormComponent} from "./uredaj-form/uredaj-form.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DijalogService} from "../dijalog/dijalog.service";

@Component({
  selector: 'app-uredaj',
  templateUrl: './uredaj.component.html',
  styleUrls: ['./uredaj.component.scss']
})
export class UredajComponent implements OnInit, AfterViewInit {
  dataSource = new MatTableDataSource<UredajViewModel>();
  columnsToDisplay = ['name', 'beacon', "actions"];

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private uredajService: UredajService,
              private dijalogService: DijalogService,
              private modalService: NgbModal) {}

  ngOnInit() {
    this.refreshModel();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }

  refreshModel() {
    this.uredajService.dohvatiSveUredaje().subscribe(data => {
      this.dataSource.data = data;
    })
  }

  addUredaj() {
    const modalRef = this.modalService.open(UredajFormComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.model = {id: 0};
    modalRef.result.then(result => {
      if (result != 0) {
        this.refreshModel();
      }
    });
  }

  urediUredaj(idUredaj) {
    const modalRef = this.modalService.open(UredajFormComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.model = {id: idUredaj};
    modalRef.result.then(result => {
      if (result != 0) {
        this.refreshModel();
      }
    });
  }

  obrisiUredaj(idUredaj) {
    let uredaj = this.dataSource.data.find(s => s.id === idUredaj);

    this.dijalogService.confirm('Molim vas potvrdite', 'Jeste li sigurni da želite obrisati uređaj: ' + uredaj.name)
      .then(odluka => {
        if (odluka) {
          this.doDelete(idUredaj);
        }
      });
  }

  private doDelete(idUredaj) {
    this.uredajService.obrisiUredaj(idUredaj).subscribe(() => {
      console.log("Uspjesno obrisano");
      this.refreshModel();
    }, error => {
      console.log("Error prilikom brisanja uredaja: " + JSON.stringify(error));
    })
  }

}
