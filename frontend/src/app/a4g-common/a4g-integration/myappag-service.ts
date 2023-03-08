import { Injectable } from '@angular/core';
import { AuthService } from '../../../app/auth/auth.service';
import { Utente } from '../../../app/auth/user';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MyappagService {
  constructor(private authService: AuthService) { }

  get isMyappagEnabled() {
    const currentUser: Utente = JSON.parse(sessionStorage.getItem("user"));
    if (!currentUser) return;
    return this.authService.isUserInRole(AuthService.roleCaa, currentUser)
      || this.authService.isUserInRole(AuthService.rolePrivate, currentUser)
      || this.authService.isUserInRole(AuthService.roleAdmin, currentUser)
      || this.authService.isUserInRole(AuthService.roleAppag, currentUser);
  }

  navigateForCuaa(cuaa: string) {
    const flagAppag = 'fromAppag=true';
    const currentUrl = window.location.href;
    const queryParams = `?${flagAppag}&appagUrl=${currentUrl}&cuaa=${cuaa}`;
    window.location.href = `${environment.mobileUrl}${queryParams}`;
  }
}
