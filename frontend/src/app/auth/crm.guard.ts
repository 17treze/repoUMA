import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  CanActivateChild,
  CanLoad,
  Route,
  Router,
  CanDeactivate
}
  from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { CanComponentDeactivate } from './canComponentDeactivate';

@Injectable()
export class CrmGuard implements CanActivate, CanActivateChild, CanLoad, CanDeactivate<CanComponentDeactivate> {

  canDeactivate(component: CanComponentDeactivate, currentRoute: ActivatedRouteSnapshot, currentState: RouterStateSnapshot, nextState?: RouterStateSnapshot): boolean | Observable<boolean> | Promise<boolean> {
    if (component.canDeactivate) {
      return component.canDeactivate();
    }
    return true;
  }
  constructor(private router: Router, private authService: AuthService) { }
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    console.log('canActivate');
    return this.checkUser(route, state);
  }

  canActivateChild(
    childRoute: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean | Observable<boolean> | Promise<boolean> {
    console.log('canActivateChild');
    return this.checkUser(childRoute, state);
  }

  canLoad(route: Route): boolean | Observable<boolean> | Promise<boolean> {
    console.log('canLoad');
    const isLoggedIn = this.authService.isLoggedIn();
    if (!isLoggedIn) {
      this.router.navigate(['home'], { queryParams: { returnUrl: route.path } });
    }
    return true;
  }

  private checkUser(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const roles = route.data['roles'] as Array<string>;
    
    const isLoggedIn = this.authService.isLoggedIn();
    if (!isLoggedIn) {
    // if (!this.authService.isLoggedIn() ) {//|| !this.authService.isUserInRoles(roles)) {
      this.router.navigate(['home'], { queryParams: { returnUrl: state.url } });
    }
    return true;
  }
}
