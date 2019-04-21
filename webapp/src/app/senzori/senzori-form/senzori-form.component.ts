import {Component, OnInit, ViewChild} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {SenzorModel} from "../../model/senzor.model";
import {SenzoriService} from "../senzori.service";

@Component({
  selector: 'app-senzori-form',
  templateUrl: './senzori-form.component.html',
  styleUrls: ['./senzori-form.component.scss']
})
export class SenzoriFormComponent implements OnInit {
  @ViewChild('f') form;

  model = new SenzorModel();

  constructor(private activeModal: NgbActiveModal,
              private senzoriService: SenzoriService) { }

  ngOnInit() {
    if (this.model.id != 0) {
      this.senzoriService.dohvatiSenzor(this.model.id).subscribe(data => {
        this.model = data;
      });
    }
  }

  pohraniSenzor() {
    if (this.form.valid) {
      this.senzoriService.pohraniSenzor(this.model).subscribe(response => {
        console.log(JSON.stringify(response));
        this.activeModal.close(response.id);
      }, error => {
        console.log(JSON.stringify(error));
      });
    }
  }



}
