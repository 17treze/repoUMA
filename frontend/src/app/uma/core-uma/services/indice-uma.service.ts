import { Injectable } from '@angular/core';
@Injectable({
  providedIn: 'root'
})
export class IndiceUmaService {
  private _READONLY_MODE: boolean;                      /** Richiesta_UMA -> se ci sono macchine dichiarate e stato == IN_COMPILAZIONE -> le sezioni sono editabili */
  private _READONLY_MODE_DICHIARAZIONE: boolean;        /** Dichiarazione_consumi_UMA -> se stato != IN_COMPILAZIONE -> le sezioni sono readonly */
  private _idFascicolo: string;                         /** salvataggio temporaneo del fascicolo legato alla persona che effettuato una richiesta o dichiarazione consumi UMA */

  constructor() { }

  get READONLY_MODE(): boolean {
    return this._READONLY_MODE;
  }

  set READONLY_MODE(value: boolean) {
    this._READONLY_MODE = value;
  }

  get READONLY_MODE_DICHIARAZIONE(): boolean {
    return this._READONLY_MODE_DICHIARAZIONE;
  }

  set READONLY_MODE_DICHIARAZIONE(value: boolean) {
    this._READONLY_MODE_DICHIARAZIONE = value;
  }

  get idFascicolo(): string {
    return this._idFascicolo;
  }

  set idFascicolo(value: string) {
    this._idFascicolo = value;
  }

}
