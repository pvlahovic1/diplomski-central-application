import { Injectable } from '@angular/core';
import {DijalogComponent} from "./dijalog.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Injectable({
  providedIn: 'root'
})
export class DijalogService {

  constructor(private modalService: NgbModal) { }

  public confirm(
    title: string,
    message: string,
    dialogSize: 'sm'|'lg' = 'sm',
    btnOkText: string = 'Da',
    btnCancelText: string = 'Ne',): Promise<boolean> {
    const modalRef = this.modalService.open(DijalogComponent, { size: dialogSize });
    modalRef.componentInstance.title = title;
    modalRef.componentInstance.message = message;
    modalRef.componentInstance.btnOkText = btnOkText;
    modalRef.componentInstance.btnCancelText = btnCancelText;

    return modalRef.result;
  }
}
