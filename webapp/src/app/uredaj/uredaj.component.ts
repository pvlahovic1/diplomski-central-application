import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, MatTableDataSource} from "@angular/material";
import {UredajModel} from "../model/uredaj.model";
import {UredajService} from "./uredaj.service";
import {beforeEach} from "selenium-webdriver/testing";
import {ProstorijeFormComponent} from "../prostorije/prostorije-form/prostorije-form.component";
import {UredajFormComponent} from "./uredaj-form/uredaj-form.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-uredaj',
  templateUrl: './uredaj.component.html',
  styleUrls: ['./uredaj.component.scss']
})
export class UredajComponent implements OnInit, AfterViewInit {
  dataSource = new MatTableDataSource<UredajModel>();
  columnsToDisplay = ['name', 'beacon', 'user', "actions"];

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private uredajService: UredajService,
              private modalService: NgbModal) { }

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


  getBeaconForUredaj(uredaj: UredajModel) {
    if (uredaj.beacon != null) {
      return `${uredaj.beacon.uuid} ${uredaj.beacon.major} ${uredaj.beacon.minor}`;
    }

    return '';
  }

  getUserForUredaj(uredaj: UredajModel) {
    if (uredaj.user != null) {
      return uredaj.user.firstName + uredaj.user.lastName;
    }

    return '';
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

}
