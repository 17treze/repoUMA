import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LoaderState } from './loader/loader.model';

@Injectable({
  providedIn: 'root'
})
export class LoaderService {

  private loaderSubject = new Subject<LoaderState>();
  loaderState = this.loaderSubject.asObservable();
  timer: any;
  timeout: number;
  isLoading: boolean;

  constructor() {
    this.timeout = environment.TIMEOUT_SPINNER; // timeout di default
  }

  public eternalShow() {
    this.loaderSubject.next(<LoaderState>{ show: true });
  }

  public show() {
    this.isLoading = true;
    this.loaderSubject.next(<LoaderState>{ show: true });
    // timeout asincrono per evitare loading infiniti sulla UI in caso di un malfunzionamento non previsto
    this.timer = setTimeout(() => {
      this.hide();
    }, this.timeout);
  }

  public hide() {
    this.isLoading = false;
    this.loaderSubject.next(<LoaderState>{ show: false });
    clearTimeout(this.timer);
  }

  public setTimeout(timeout: number) {
    this.timeout = timeout;
  }

  public resetTimeout() {
    this.timeout = environment.TIMEOUT_SPINNER;
  }


}