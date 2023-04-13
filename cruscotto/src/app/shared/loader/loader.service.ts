import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoaderService {

  private loaderSubject = new BehaviorSubject<boolean>(false);
  toShowOB = this.loaderSubject.asObservable();
  timer: any;
  timeout: number = 300000;

  constructor() { }


  public eternalShow() {
    this.loaderSubject.next(true);
  }

  public show() {
    this.loaderSubject.next(true);
    //timeout per salvaguardare l'utilizzo della UI in caso di un malfunzionamento non previsto
    this.timer = setTimeout(() => {
      this.hide();
    }, this.timeout);
  }

  public hide() {
    this.loaderSubject.next(false);
    clearTimeout(this.timer);
  }

  public setTimeout(timeout: number) {
    this.timeout = timeout;
  }

  public resetTimeout() {
    this.timeout = 30000;
  }


}