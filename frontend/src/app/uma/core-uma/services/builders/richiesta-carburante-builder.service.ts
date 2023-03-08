import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { ErrorService } from './../../../../a4g-common/services/error.service';
import { StatoDichiarazione } from './../../../../fascicolo/antimafia/classi/statoDichiarazioneEnum';
import { ConsultazioneUMA } from 'src/app/uma/core-uma/models/dto/ConsultazioneUMA';
import { RichiestaCarburanteDto } from './../../models/dto/RichiestaCarburanteDto';
import { Injectable } from '@angular/core';
import { StatoDomandaUma } from 'src/app/a4g-common/classi/enums/uma/StatoDomandaUma.enum';
import { DichiarazioneConsumiDto } from '../../models/dto/DichiarazioneConsumiDto';
import { PaginatorA4G } from 'src/app/a4g-common/interfaces/paginator.model';

@Injectable({
  providedIn: 'root'
})
export class RichiestaCarburanteBuilderService {

  constructor(
    private errorService: ErrorService
  ) { }

  richiestaCarburanteDtoToConsultazioneUmaViewBuilder(richiesta: RichiestaCarburanteDto): ConsultazioneUMA {
    const consultazioneUma = {} as ConsultazioneUMA;
    consultazioneUma.nrDomanda = richiesta.id;
    consultazioneUma.anno = richiesta.campagna;
    consultazioneUma.tipo = 'Richiesta';
    consultazioneUma.stato = richiesta.stato;
    consultazioneUma.cuaa = richiesta.cuaa;
    consultazioneUma.denominazione = richiesta.denominazione;
    consultazioneUma.protocollo = richiesta.protocollo;
    consultazioneUma.dataPresentazione = richiesta.dataPresentazione;
    consultazioneUma.edit = richiesta.stato === StatoDomandaUma.IN_COMPILAZIONE ? true : false;
    consultazioneUma.view = richiesta.stato !== StatoDomandaUma.IN_COMPILAZIONE ? true : false;
    return consultazioneUma;
  }

  isRettificabile(richieste: Array<RichiestaCarburanteDto>, dichiarazioni: Array<DichiarazioneConsumiDto>): boolean {
    const inCompilazione = !!richieste.filter(richiesta => richiesta.stato === StatoDomandaUma.IN_COMPILAZIONE).length;
    const isAutorizzata = !!richieste.filter(richiesta => richiesta.stato === StatoDomandaUma.AUTORIZZATA).length;
    /** se non ci sono dichiarazioni oppure se non ci sono dichiarazioni in stato PROTOCOLLATA -> posso rettificare */
    const isDichiarazioneValid = !dichiarazioni || !dichiarazioni.length || (dichiarazioni && dichiarazioni.length && !dichiarazioni.filter(dichiarazione => dichiarazione.stato === StatoDichiarazione.PROTOCOLLATA).length);
    return !inCompilazione && isAutorizzata && isDichiarazioneValid;
  }

  getRettificabile(richieste: Array<RichiestaCarburanteDto>, keyToast: string = 'tst'): RichiestaCarburanteDto {
    const autorizzate = richieste.filter(richiesta => richiesta.stato === StatoDomandaUma.AUTORIZZATA);
    console.log('Richieste autorizzate: ', autorizzate);
    if (autorizzate && autorizzate.length > 1) { // Più richieste autorizzate
      this.errorService.showErrorWithMessage(UMA_MESSAGES.richiesteAutorizzateNotPresent, keyToast);
      return null;
    }

    if (!autorizzate || !autorizzate.length) { // Nessuna richiesta autorizzata
      this.errorService.showErrorWithMessage(UMA_MESSAGES.richiesteAutorizzateGtOne, keyToast);
      return null;
    }

    if (autorizzate && autorizzate.length == 1) { // SOLO 1 -> OK
      return autorizzate[0];
    }
    return null;
  }

}
