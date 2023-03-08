import { of, Observable, timer, Subject, fromEvent, from } from 'rxjs';
import { map, takeUntil, repeatWhen, tap, delay } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class ProcessRepeatingServiceCall {
  readonly observable$: Observable<any>;
  private readonly _stop = new Subject<void>();
  private readonly _start = new Subject<void>();
  private delay: number = 30000;

  constructor(private router: Router) {
    this.observable$ = timer(0, this.delay)
      .pipe(
        // tap(val => console.log(val)),
        // map(() => <any>{}),
        takeUntil(this._stop),
        repeatWhen(completed  => this._start)
      );
  }

  start(): void {
    //faccio partire lo start con un breve delay per permettere al backend di effettuare le operazioni preliminari prima
    //di mettere in START il processo. In questo caso si evita la possibilita di non avere le informazioni aggiornate
    of('dummy').pipe(delay(1500)).subscribe(x => this._start.next());
  }

  stop(): void {
    this._stop.next();
  }

  stopAndRefresh(): void {
    this._stop.next();
    window.location.reload();
  }

}