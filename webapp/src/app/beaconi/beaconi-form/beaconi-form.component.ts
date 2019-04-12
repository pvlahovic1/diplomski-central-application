import {Component, OnInit, ViewChild} from '@angular/core';
import {BeaconModel} from "../../model/beacon.model";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {BeaconiService} from "../beaconi.service";

@Component({
  selector: 'app-beaconi-form',
  templateUrl: './beaconi-form.component.html',
  styleUrls: ['./beaconi-form.component.scss']
})
export class BeaconiFormComponent implements OnInit {
  @ViewChild('f') form;

  model = new BeaconModel();

  constructor(private activeModal: NgbActiveModal,
              private beaconiService: BeaconiService) {
  }

  ngOnInit() {
    if (this.model.id != 0) {
      this.beaconiService.dohvatiBeacon(this.model.id).subscribe(data => {
        this.model = data;
      });
    } else {
      console.log("Postavlja se novi");
    }
  }

  pohraniBeacon() {
    if (this.form.valid) {
      console.log("Ide na server " + JSON.stringify(this.model));
      this.beaconiService.pohraniBeacon(this.model).subscribe(response => {
        console.log(JSON.stringify(response));
        this.activeModal.close(response.id);
      }, error => {
        console.log(JSON.stringify(error));
      });
    } else {
      console.log("Forma nije validna");
    }
  }

}
