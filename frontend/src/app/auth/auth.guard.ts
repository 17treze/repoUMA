import { Injectable } from '@angular/core';
import {CanActivate,ActivatedRouteSnapshot,RouterStateSnapshot, Router} from '@angular/router';
import { AuthService } from './auth.service';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private authService: AuthService) { }
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Promise<boolean> {
    return new Promise((resolve) => {
      if (this.authService.isAuthenticated()) {
        resolve(true);
      }
      else {
        this.authService.login();
        this.authService.authenticationEventObservable.subscribe(
          result => {
            resolve(result);
          },
          error => { 
            console.error(error); 
            resolve(false);
          }
        )
      }
    });
  }
}