import { Injectable } from '@angular/core';
import { MacchinaDto } from '../../core-uma/models/dto/MacchinaDto';
import { TipoCarburante } from '../../core-uma/models/enums/TipoCarburante.enum';
import { FabbisognoDichiarato } from '../models/viewModels/FabbisognoDichiaratoViewModel';

@Injectable({
  providedIn: 'root'
})
export class ModelBuilderService {

  constructor() { }

  fabbisognoDichiaratoBuilder(macchine: Array<MacchinaDto>): FabbisognoDichiarato {
    // filtro le macchine utilizzate(dichiarate)
    const macchineDichiarate: Array<MacchinaDto> = macchine.filter((macchina: MacchinaDto) => macchina.isUtilizzata);
    // setto se c'è almeno una a gasolio o a benzina
    const fabbisognoDichiarato: FabbisognoDichiarato = {
      benzina: macchineDichiarate.some((macchina: MacchinaDto) => macchina.alimentazione === TipoCarburante.BENZINA),
      gasolio: macchineDichiarate.some((macchina: MacchinaDto) => macchina.alimentazione === TipoCarburante.GASOLIO)
    };
    return fabbisognoDichiarato;
  }
}
