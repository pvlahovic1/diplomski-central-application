import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-senzori-form',
  templateUrl: './senzori-form.component.html',
  styleUrls: ['./senzori-form.component.scss']
})
export class SenzoriFormComponent implements OnInit {
  model :any = {};


  constructor(private activeModal: NgbActiveModal) { }

  ngOnInit() {
  }

}
