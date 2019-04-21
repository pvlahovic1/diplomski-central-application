import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../settings/service/authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-headers',
  templateUrl: './headers.component.html',
  styleUrls: ['./headers.component.scss']
})
export class HeadersComponent implements OnInit {

  displayHeaders = false;
  constructor(private authenticationService: AuthenticationService,
              private router: Router) { }

  ngOnInit() {
    this.authenticationService.currentUser.subscribe(data => {
      console.log("DATA: " + JSON.stringify(data));

      this.displayHeaders = !!data;
    });
  }

  displayLogin() {
    return !this.displayHeaders;
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/prijava']);
  }

}
