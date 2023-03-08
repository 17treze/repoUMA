import { DichiarazioneConsumiFilterDto } from './../models/dto/DichiarazioneConsumiFilterDto';
import { Injectable } from '@angular/core';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
import { DomandaUmaFilter } from '../models/dto/DomandaUmaFilter';
import { PresentaDomandaDto } from '../../core-uma/models/dto/PresentaDomandaDto';
import { StatoDichiarazioneConsumiEnum } from 'src/app/a4g-common/classi/enums/uma/StatoDichiarazioneConsumi.enum';
import { StatoDomandaUma } from 'src/app/a4g-common/classi/enums/uma/StatoDomandaUma.enum';

@Injectable({
  providedIn: 'root'
})
export class DtoBuilderService {

  constructor(private dateUtilService: DateUtilService) { }

  buildPresentaDomandaDto(cuaa: string, codiceFiscaleRichiedente: string) {
    const presentaDomandaDto: PresentaDomandaDto = {
      cuaa,
      codiceFiscaleRichiedente
    };
    return presentaDomandaDto;
  }

  buildDichiarazioneConsumiFilterDto(cuaa: string, campagna: number, stati: Array<keyof typeof StatoDichiarazioneConsumiEnum>) {
    const dichiarazioneConsumiFilter: DichiarazioneConsumiFilterDto = {
      cuaa,
      campagna,
      stati
    };
    return dichiarazioneConsumiFilter;
  }

  buildDomandaUmaFilter(cuaa: string): DomandaUmaFilter {
    const filter = new DomandaUmaFilter();
    filter.cuaa = cuaa;
    filter.campagna = [this.dateUtilService.getCurrentYear().toString()];

    const statiList = Array<StatoDomandaUma>();
    statiList.push(StatoDomandaUma.IN_COMPILAZIONE);
    statiList.push(StatoDomandaUma.AUTORIZZATA);
    filter.stati = statiList;

    return filter;
  }

  buildDomandaUmaFilterWithAllStates(cuaa: string): DomandaUmaFilter {
    const filter = new DomandaUmaFilter();
    filter.cuaa = cuaa;
    filter.campagna = [this.dateUtilService.getCurrentYear().toString()];

    const statiList = Array<StatoDomandaUma>();
    statiList.push(StatoDomandaUma.IN_COMPILAZIONE);
    statiList.push(StatoDomandaUma.AUTORIZZATA);
    statiList.push(StatoDomandaUma.RETTIFICATA);
    filter.stati = statiList;

    return filter;
  }
}
