import {Component, OnInit} from '@angular/core';
import {DodavanjeUredajaService} from "./dodavanje-uredaja.service";
import {SenzoriFormComponent} from "../senzori/senzori-form/senzori-form.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {BeaconiFormComponent} from "../beaconi/beaconi-form/beaconi-form.component";

@Component({
  selector: 'app-dodavanje-uredaja',
  templateUrl: './dodavanje-uredaja.component.html',
  styleUrls: ['./dodavanje-uredaja.component.scss']
})
export class DodavanjeUredajaComponent implements OnInit {

  isSelected = false;
  isFileUpload = false;
  spiner = false;
  selectedFile = null;
  imagePreviewUrl: any;

  constructor(private dodavanjeUredajaService: DodavanjeUredajaService,
              private modalService: NgbModal) {

  }

  ngOnInit() {

  }

  onFileChange(event) {
    if (event.target.files && event.target.files[0]) {
      this.selectedFile = <File>event.target.files[0];

      const reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);

      reader.onload = (data) => {
        this.imagePreviewUrl = reader.result;
      }
    } else {
      this.selectedFile = null;
      this.imagePreviewUrl = undefined;
    }
  }


  onCameraPressed() {
    this.isSelected = true;
    this.isFileUpload = false;
  }

  onFilePressed() {
    this.isSelected = true;
    this.isFileUpload = true;

  }

  showFileUpload() {
    return this.isSelected && this.isFileUpload;
  }

  showCameraUpload() {
    return this.isSelected && !this.isFileUpload;
  }

  showImage() {
    return this.imagePreviewUrl !== undefined;
  }

  processImage() {
    if (this.selectedFile != null) {
      const formData = new FormData();
      formData.append('multipartFile', this.selectedFile, this.selectedFile.name);
      this.spiner = true;
      this.dodavanjeUredajaService.processImage(formData).subscribe(data => {
        console.log('Uspjesan upload: ' + JSON.stringify(data));
        if (data.type === 'raspberry_pi') {
          this.addSenzor(data);
        } else if (data.type === 'ibeacon') {
          this.addBeacon(data);
        }

        this.spiner = false;
      }, err => {
        console.log(err)
        this.spiner = false;
      });
    }
  }


  addSenzor(imageData) {
    const modalRef = this.modalService.open(SenzoriFormComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.model = {
      id: 0, name: imageData.par1, beaconDataSendInterval:
      imageData.par2, beaconDataPurgeInterval: imageData.par3, createFromImage: true
    };
  }

  addBeacon(imageData) {
    const modalRef = this.modalService.open(BeaconiFormComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.model = {
      id: 0, uuid: imageData.par1, major: imageData.par2, minor: imageData.par3,
      createFromImage: true
    };
  }

}
