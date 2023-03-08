import { ConduzioneTerreniDto } from './../../../../a4g-common/classi/dto/dotazione-tecnica/ConduzioneDto';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng-lts/api';
import { TerritorioService } from './../territorio.service';
import { PopupNuovoTitoloConduzioneComponent } from './popup-nuovo-titolo-conduzione/popup-nuovo-titolo-conduzione.component';
import { FascicoloDettaglioService } from './../../../fascicolo-dettaglio-container/fascicolo-dettaglio.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FascicoloDettaglio } from 'src/app/fascicolo/shared/fascicolo.model';
import { StatoFascicoloEnum } from 'src/app/fascicolo/creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import { TipoConduzioneDto } from 'src/app/a4g-common/classi/dto/dotazione-tecnica/ConduzioneDto';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';

@Component({
  selector: 'app-titoli-conduzione',
  templateUrl: './titoli-conduzione.component.html',
  styleUrls: ['./titoli-conduzione.component.css']
})
export class TitoliConduzioneComponent implements OnInit {

  @ViewChild('popupNuovoTitoloConduzione')
  popupNuovoTitoloConduzione: PopupNuovoTitoloConduzioneComponent;

  public readonly: boolean;
  private idValidazione: string;
  public popupOpen = false;
  public tipoConduzione: TipoConduzioneDto[]; 

  constructor(
    private fascicoloDettaglioService: FascicoloDettaglioService,
    private territorioService: TerritorioService,
    private messageService: MessageService,
    private translateService: TranslateService
  ) { }

  ngOnInit() {
    const statoFascicolo = this.fascicoloDettaglioService.fascicoloCorrente.value.stato;
    this.readonly = (statoFascicolo === StatoFascicoloEnum.VALIDATO || statoFascicolo === StatoFascicoloEnum.IN_AGGIORNAMENTO)
      && (this.idValidazione == null) ? false : true;
  }

  public goBack() {
    this.fascicoloDettaglioService.mostraDettaglioSezione.next(FascicoloDettaglio.DATI);
  }

  public isFascicoloAttuale(): boolean {
    return this.fascicoloDettaglioService.fascicoloCorrente && this.idValidazione == null;
  }

  public popupConduzioneOpen() {
    this.territorioService.getTipoConduzione().subscribe(
      (res: Array<TipoConduzioneDto>) => {
        this.tipoConduzione = res;
        this.popupOpen = true;
      }, err => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, this.translateService.instant('TERRITORIO.GET_ELENCO_TIPO_CONDUZIONE_MSG_ERROR')
        ));
      }
    );
  }  

  public popupConduzioneClose() {
    this.popupOpen = false;
  }

  private getCuaa() {
    return this.fascicoloDettaglioService.fascicoloCorrente?.value?.cuaa;
  }

  public saveConduzione(conduzioneTerreniDto: ConduzioneTerreniDto) {
    this.territorioService.putTitoloConduzione(this.getCuaa(), conduzioneTerreniDto)
      .subscribe(res => {
        this.popupConduzioneClose();
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.success, this.translateService.instant('TERRITORIO.PUT_TITOLO_CONDUZIONE_SUCCESS')
        ));
        // TODO: refresh lista
      }, err => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, this.translateService.instant('TERRITORIO.PUT_TITOLO_CONDUZIONE_ERROR')
        ));
      });
  }

}
