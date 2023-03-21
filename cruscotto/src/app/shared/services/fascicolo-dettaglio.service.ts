import { BehaviorSubject } from 'rxjs';
import { Injectable } from '@angular/core';
import { FascicoloDaCuaa } from 'src/app/modules/fascicolo-dettaglio/models/FascicoloCuaa';

@Injectable({
  providedIn: 'root'
})
export class FascicoloDettaglioService {

  private _fascicoloCorrente$ = new BehaviorSubject<FascicoloDaCuaa>(null);
  private _mostraDettaglioSezione$ = new BehaviorSubject<string>(null);

  constructor() { }

  get fascicoloCorrente(): BehaviorSubject<FascicoloDaCuaa> {
    return this._fascicoloCorrente$;
  }

  get mostraDettaglioSezione(): BehaviorSubject<string> {
    return this._mostraDettaglioSezione$;
  }
}
