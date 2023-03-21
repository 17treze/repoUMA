import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AppagService {
  constructor() {
    this.init();
  }

  private init(): void {
    this.saveAppagData();
  }

  private saveAppagData() {
    if (this.getParameterByName('fromAppag'))
      localStorage.setItem('fromAppag', this.getParameterByName('fromAppag'));
    if (this.getParameterByName('cuaa'))
      localStorage.setItem('cuaa', this.getParameterByName('cuaa'));
    if (this.getParameterByName('appagUrl'))
      localStorage.setItem('appagUrl', this.getParameterByName('appagUrl'));
  }

  private getParameterByName(name: string) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
  }

  private cleanLocalStorage() {
    localStorage.removeItem('fromAppag');
    localStorage.removeItem('cuaa');
    localStorage.removeItem('appagUrl');
  }

  public isFromAppag(): boolean {
    if (localStorage.getItem('fromAppag')) return true;
    return false;
  }

  public fromAppagCuaa() {
    return localStorage.getItem('cuaa');
  }

  public returnAppag() {
    const returnUrl = this.getAppagUrl();
    this.cleanLocalStorage();
    window.location.href = returnUrl;
  }

  public getAppagUrl() {
    return localStorage.getItem('appagUrl');
  }
}
