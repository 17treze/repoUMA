import { EredeDto } from './../creazione-fascicolo/dto/EredeDto';
import { BehaviorSubject } from 'rxjs';
import { Injectable } from '@angular/core';
import { FascicoloDaCuaa } from '../../a4g-common/classi/FascicoloCuaa';
import { PersonaFisicaConCaricaDto } from '../creazione-fascicolo/dto/PersonaDto';

@Injectable({
  providedIn: 'root'
})
export class FascicoloDettaglioService {

  private _fascicoloCorrente$ = new BehaviorSubject<FascicoloDaCuaa>(null);
  private _mostraDettaglioSezione$ = new BehaviorSubject<string>(null);
  private _firmatario$ = new BehaviorSubject<PersonaFisicaConCaricaDto>(null);
  private _eredi$ = new BehaviorSubject<Array<EredeDto>>(null);

  constructor() { }

  get fascicoloCorrente(): BehaviorSubject<FascicoloDaCuaa> {
    return this._fascicoloCorrente$;
  }

  get firmatario(): BehaviorSubject<PersonaFisicaConCaricaDto> {
    return this._firmatario$;
  }

  get mostraDettaglioSezione(): BehaviorSubject<string> {
    return this._mostraDettaglioSezione$;
  }

  get eredi(): BehaviorSubject<Array<EredeDto>> {
    return this._eredi$;
  }

}
