import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppBreadcrumbService {
  // Observable source
  private _breadCrumbUpdateStepsSource$ = new BehaviorSubject<number>(null);
  // Observable stream
  breadCrumbUpdateSteps$ = this._breadCrumbUpdateStepsSource$.asObservable();

  private _breadCrumbSteps: number; /** store number of breadcrumbs to remove going back */
  private _urlSource: string;       /** store urlSource */

  constructor() { }

  get breadCrumbSteps(): number {
    return this._breadCrumbSteps;
  }

  set breadCrumbSteps(value: number) {
    this._breadCrumbSteps = value;
  }

  get urlSource(): string {
    return this._urlSource;
  }

  set urlSource(value: string) {
    this._urlSource = value;
  }

  updateData(value: number) {
    this._breadCrumbUpdateStepsSource$.next(value);
  }

}
