import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-dijalog',
  templateUrl: './dijalog.component.html',
  styleUrls: ['./dijalog.component.scss']
})
export class DijalogComponent implements OnInit {

  @Input() title: string;
  @Input() message: string;
  @Input() btnOkText: string;
  @Input() btnCancelText: string;

  constructor(private activeModal: NgbActiveModal) { }

  ngOnInit() {
  }

  odustani() {
    this.activeModal.close(false);
  }

  prihvati() {
    this.activeModal.close(true);
  }


}
