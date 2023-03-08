import { GisMessaggiToastCostants } from './../../shared/messaggi-toast.constants';
import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { GisCostants } from '../../shared/gis.constants';
import { StatiRichiesta } from '../../shared/StatiRichiesta.enum';

@Component({
  selector: 'app-toast-gis',
  templateUrl: './toast-gis.component.html',
  styleUrls: ['./toast-gis.component.css']
})
export class ToastGisComponent implements OnInit {


  constructor(private messageService: MessageService, private gisConstants: GisCostants, 
    private gisMessaggiToast: GisMessaggiToastCostants) { }

  ngOnInit() {
  }

  showError() {
    this.messageService.add({ severity: 'error', summary: 'Errore', detail: this.gisConstants.erroreSalvataggio });
  }

  showErrorDelete() {
    this.messageService.add({ severity: 'error', summary: 'Errore', detail: this.gisConstants.erroreCancellazione });
  }

  showErrorPoligonoCoinvoltoInAltraLavorazione() {
    this.messageService.add({ severity: 'error', summary: 'Errore', detail: this.gisConstants.errorPoligonoCoinvoltoInAltraLavorazione });
  }

  showErrorPoligonoNonTrovato() {
    this.messageService.add({ severity: 'error', summary: 'Errore', detail: this.gisConstants.errorPoligonoNonTrovato });
  }

  showSuccess() {
    this.messageService.add({ severity: 'success', summary: 'Salvataggio', detail: this.gisConstants.successSalvataggio });
  }

  showSuccessAllegato() {
    this.messageService.add({ severity: 'success', summary: 'Salvataggio', detail: this.gisConstants.successAllegato });
  }

  showSuccessDeleteAllegato() {
    this.messageService.add({ severity: 'success', summary: 'Salvataggio', detail: this.gisConstants.successCancellazione });
  }

  showWarning() {
    this.messageService.add({ severity: 'warning', summary: 'Attenzione', detail: this.gisConstants.noData });
  }


  showErrorAllegati() {
    this.messageService.add({ severity: 'error', summary: 'Errore', detail: this.gisConstants.noAllegato });
  }

  showErrorExt() {
    this.messageService.add({ severity: 'error', summary: 'Errore', detail: this.gisConstants.erroreExt });
  }

  showErrorSize() {
    this.messageService.add({ severity: 'error', summary: 'Errore', detail: this.gisConstants.erroreSize });
  }

  showErrorEditRichiestaModificaSuolo(stato: StatiRichiesta) {
    this.messageService.add({ severity: 'error', summary: 'Errore', detail: this.gisConstants.errorCantEditRichiestaModificaSuolo });
  }

  // MESSAGGI OPERATORE BO
  showSuccessNuovaLavorazione() {
    this.messageService.add({ severity: 'success', summary: 'Creazione lavorazione', detail: this.gisConstants.successNuovaLavorazione });
  }

  showErrorNuovaLavorazione() {
    this.messageService.add({ severity: 'error', summary: 'Creazione lavorazione', detail: this.gisConstants.errorNuovaLavorazione });
  }

  showSuccessDeleteLavorazione() {
    this.messageService.add({ severity: 'success', summary: 'Salvataggio', detail: this.gisConstants.successCancellazioneLavorazione });
  }

  showErrorGetLavorazioni() {
    this.messageService.add({ severity: 'error', summary: 'Ricerca Lavorazioni', detail: this.gisConstants.errorGetLavorazioni });
  }

  showErroAvviaLavorazione() {
    this.messageService.add({ severity: 'error', summary: 'Creazione Lavorazione', detail: this.gisConstants.errorAvviaLavorazione });
  }

  showErrorNuovaLavorazioneEsisteAltraLavorazioneVuota() {
    // tslint:disable-next-line: max-line-length
    this.messageService.add({ severity: 'error', summary: 'Creazione Lavorazione', detail: this.gisConstants.errorNuovaLavorazioneEsisteAltraLavorazioneVuota });
  }

  // Messaggio SERVER 500
  showErrorServer500() {
    this.messageService.add({ severity: 'error', summary: 'Ricerca poligoni suolo dichiarato', detail: this.gisConstants.errorServer500 });
  }

  validazioneLavorazioneError() {
    // tslint:disable-next-line: max-line-length
    this.messageService.add({ severity: 'error', summary: 'Validazione Lavorazione', detail: this.gisConstants.validazioneLavorazioneError });
  }

  // MAPPA
  showNoWmsIdentify() {
    this.messageService.add({ severity: 'warning', summary: 'Identifica', detail: this.gisConstants.noContentIdentify });
  }

  // Validazione esito lavorazione
  showWarningValida() {
    this.messageService.add({ severity: 'warning', summary: 'Validazione Lavorazione', detail: this.gisConstants.modifichenNonSalvate });
  }

  // Validazione esito lavorazione
  showSuccessValidazioneLavorazioneInCorso() {
    // tslint:disable-next-line: max-line-length
    this.messageService.add({ severity: 'success', summary: 'Validazione Lavorazione', detail: this.gisConstants.successValidazioneLavorazioneInCorso });
  }

  // Validazione esito lavorazione
  showErrorValidazioneLavorazioneInCorso() {
    // tslint:disable-next-line: max-line-length
    this.messageService.add({ severity: 'error', summary: 'Validazione Lavorazione', detail: this.gisConstants.errorValidazioneLavorazioneInCorso });
  }

  showErrorModificaLavorazione() {
    // tslint:disable-next-line: max-line-length
    this.messageService.add({ severity: 'error', summary: 'Modifica Lavorazione', detail: this.gisConstants.errorModificaLavorazione });
  }

  showErrorModificaLavorazioneMsg(message:string) {
    // tslint:disable-next-line: max-line-length
    this.messageService.add({ severity: 'error', summary: 'Modifica Lavorazione', detail: message });
  }

  // Consolida lavorazione
  showSuccessConsolidaLavorazione() {
    // tslint:disable-next-line: max-line-length
    this.messageService.add({ severity: 'success', summary: 'Modifica Lavorazione', detail: this.gisConstants.successConsolidaLavorazioneSuA4S });
  }

  showErrorConsolidaLavorazione(message) {
    this.messageService.add({ severity: 'error', summary: 'Modifica Lavorazione', detail: message });
  }

  // Consolida lavorazione
  showSuccessConsolidaLavorazioneInAGS() {
    // tslint:disable-next-line: max-line-length
    this.messageService.add({ severity: 'success', summary: 'Consolida Lavorazione', detail: this.gisConstants.successConsolidaLavorazioneSuAGS });
  }

  // Consolida lavorazione
  alertConsolidaLavorazioneInAGS(message) {
    // tslint:disable-next-line: max-line-length
    this.messageService.add({ severity: 'warning', summary: 'Consolida Lavorazione', detail: message });
  }

  // Refresh Job FME
  showMsgRefreshJobFme(message) {
    // tslint:disable-next-line: max-line-length
    this.messageService.add({ severity: 'error', summary: 'Aggiorna stato Lavorazione', detail: message});
  }

  showErrorGenerico(msg: string) {
    this.messageService.add({ severity: 'error', summary: 'Errore', detail: msg});
  }
 
  showWarningGenerico(msg: string) {
    this.messageService.add({ severity: 'warning', summary: 'Attenzione', detail: msg });
  }

  // aggiorna poligoni dichiarati
  showSuccessAggiornaDichiarati() {
    this.messageService.add({ severity: 'success', summary: 'Aggiorna dichiarati', detail: this.gisMessaggiToast.successAggiornaDichiarati });
  }
  showErrorAggiornaDichiarati(message) {
    this.messageService.add({ severity: 'error', summary: 'Aggiorna dichiarati', detail: message });
  }

  // Validazione fe su adl
  showErrorPoligoniAdl() {
    this.messageService.add({ severity: 'error', summary: 'Poligono Adl', detail: this.gisMessaggiToast.erroreValidazioneAdl });
  }

  showSuccessGenerico(header: string, msg: string) {
    this.messageService.add({ severity: 'success', summary: header, detail: msg});
  }

}
