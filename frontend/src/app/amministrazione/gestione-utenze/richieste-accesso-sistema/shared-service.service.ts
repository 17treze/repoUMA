import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { StatoRichiestaCount } from './dto/StatoRichiestaCount';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  private counters: BehaviorSubject<StatoRichiestaCount> = new BehaviorSubject({ stato: '', count: 0 });
  public currentCounters = this.counters.asObservable();

  private timersForChangeCounters: BehaviorSubject<Number> = new BehaviorSubject(0);
  public currentTimersForChangeCounters = this.timersForChangeCounters.asObservable();

  constructor() { }

  public changeCounters(count: StatoRichiestaCount) {
    this.counters.next(count);
  }

  public changeCountersForApproveOrDanied() {
    this.timersForChangeCounters.next(new Date().getMilliseconds());
  }

}
