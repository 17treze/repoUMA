import { Component, OnInit, OnDestroy } from '@angular/core';
import { Observable, interval } from 'rxjs';
import { CanComponentDeactivate } from '../a4g-common/can-deactivate-guard.service';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy, CanComponentDeactivate {

  //subscription: Subscription;
  isAlive: boolean;
  retutnUrl: string;
  cuaa: string;
  constructor(private authService: AuthService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    // tmp
    this.authService.setUser(null);

    this.isAlive = true;
    this.route.queryParams.subscribe(params => this.retutnUrl = params['returnUrl'] || '/');
  }

  ngOnDestroy(): void {
    this.isAlive = false;
  }

  loginAppag() {
  //  this.reRoute(this.authService.login('appag', 'appag'));
  }

  loginCittadino() {
    //this.reRoute(this.authService.login(this.cuaa, 'cit'));
  }

  loginCaa() {
    //this.reRoute(this.authService.login('caa', 'caa'));
  }

  reRoute(authSuccess){
      if(authSuccess){
      console.log('retutnUrl ' + this.retutnUrl);
      if(this.retutnUrl != '/')
        this.router.navigateByUrl(this.retutnUrl);
      else
        this.router.navigate(['../home'], { relativeTo: this.route });
    }
  }


  canDeactivate(): Observable<boolean> | Promise<boolean>| boolean{
    // se false una volta entrato nella form non puoi uscire
    return true; //confirm('Vuoi uscire?');
  }
} 
