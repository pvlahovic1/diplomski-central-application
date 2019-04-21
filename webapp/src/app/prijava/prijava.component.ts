import {Component, OnInit, ViewChild} from '@angular/core';
import {AuthenticationService} from "../settings/service/authentication.service";
import {first} from "rxjs/operators";
import {Router} from "@angular/router";

@Component({
  selector: 'app-prijava',
  templateUrl: './prijava.component.html',
  styleUrls: ['./prijava.component.scss']
})
export class PrijavaComponent implements OnInit {
  @ViewChild('f') form;
  usrname: string;
  password: string;
  error = '';

  constructor(private authenticationService: AuthenticationService,
              private router: Router) {
  }

  ngOnInit() {
    if (this.authenticationService.getCurrentUser()) {
      this.router.navigate(['/prostorije']);
    }
  }

  prijava() {
    this.authenticationService.login(this.usrname, this.password).pipe(first()).subscribe(data => {
      console.log(JSON.stringify(data));
      this.router.navigate(['/prostorije']);
    }, error => {
      this.error = error;
    });
  }

  submitDisabled() {
    return !this.form.valid;
  }

}
