import { RichiestaRevocaMandatoRifiutoDialogComponent } from './richiesta-revoca-mandato-rifiuto-dialog/richiesta-revoca-mandato-rifiuto-dialog.component';
import { Component, OnInit, ViewChild } from '@angular/core';
import { RichiestaRevocaImmediataDto } from '../dto/RichiestaRevocaImmediataDto';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { MessageService } from 'primeng/api';
import { ActivatedRoute, Router } from '@angular/router';
import { MandatoService } from '../../mandato.service';
import { RichiestaValutataEventService } from '../richiesta-valutata-event.service';

@Component({
  selector: 'app-richiesta-revoca-mandato-da-valutare',
  templateUrl: './richiesta-revoca-mandato-da-valutare.component.html',
  styleUrls: ['./richiesta-revoca-mandato-da-valutare.component.css']
})
export class RichiestaRevocaMandatoDaValutareComponent implements OnInit {
  public richiesteDaValutare: RichiestaRevocaImmediataDto[];
  public richiestaRevocaImmediata: any;
  public revocaImmediataFile: Blob;
  private cuaaDaAccettare: string;

  @ViewChild('rifiutoDialog', { static: true })
  public rifiutoDialog: RichiestaRevocaMandatoRifiutoDialogComponent;
  public richiestePresenti: boolean;

  constructor(
    protected route: ActivatedRoute,
    private mandatoService: MandatoService,
    protected messageService: MessageService,
    protected richiestaValutataEventService: RichiestaValutataEventService
  ) { }

  ngOnInit() {
    this.getRichiesteDaValutare(false);
  }

  private getRichiesteDaValutare(valutata: boolean) {
    this.richiestePresenti = true;
    this.mandatoService.getRichiesteRevocheImmediate(valutata).subscribe(response => {
      this.richiesteDaValutare = response;
      if (!this.richiesteDaValutare || this.richiesteDaValutare.length === 0) {
        this.richiestePresenti = false;
      }
    }, err => {
      this.richiestePresenti = false;
      this.messageService.add(A4gMessages.getToast(
        'tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
    });
  }

  public showDialog(richiestaRevocaImmediata: any) {
    this.rifiutoDialog.onOpen(richiestaRevocaImmediata);
  }

  public onDialogClose(evento) {
    this.getRichiesteDaValutare(false);
    this.richiestaValutataEventService.sendEvent(true);
  }

  public accettaRichiesta(cuaa: string) {
    this.cuaaDaAccettare = cuaa;
    this.messageService.add(
      A4gMessages.getToast(
        'accettaRichiestaRevocaImmediata',
        A4gSeverityMessage.warn,
        A4gMessages.checkAccettaRichiestaRevocaImmediata
      )
    );
  }

  public scaricaRevocaImmediata(idProtocollo: string) {
    console.log('scarica revoca immediata');
    this.scaricaRevocaImmediataFile(idProtocollo);
  }

  private scaricaRevocaImmediataFile(idProtocollo: string) {
    this.mandatoService.getRevocaImmediataFile(idProtocollo).subscribe(
      result => {
        this.revocaImmediataFile = new Blob([result], { type: 'application/pdf' });
        this.visualizzaRevocaImmediata();
      }, err => {
        this.revocaImmediataFile = null;
      }
    );
  }

  public visualizzaRevocaImmediata() {
    if (this.revocaImmediataFile != null) {
      const fileURL = URL.createObjectURL(this.revocaImmediataFile);
      window.open(fileURL);
    } else {
      this.messageService.add(A4gMessages.getToast(
        'tst', A4gSeverityMessage.error, A4gMessages.erroreVisualizzazioneRevocaImmediata));
    }
  }

  public confermaRevoca() {
    console.log('richiesta accettata');
    this.messageService.clear('accettaRichiestaRevocaImmediata');
    this.putAccettaRichiestaRevocaImmediata();
  }

  public annulla() {
    console.log('annulla comando');
    this.messageService.clear('accettaRichiestaRevocaImmediata');
  }

  private putAccettaRichiestaRevocaImmediata() {
    this.mandatoService.putAccettaRichiestaRevocaImmediata(this.cuaaDaAccettare)
      .subscribe(
        result => {
          if (result.esito === 'OK') {
            this.esitoOk();
            this.getRichiesteDaValutare(false);
            this.richiestaValutataEventService.sendEvent(true);
          }
          if (result.esito === 'NON_BLOCCANTE') {
            this.esitoNonBloccante(result.responseList);
            this.getRichiesteDaValutare(false);
            this.richiestaValutataEventService.sendEvent(true);
          }
          if (result.esito === 'KO') {
            this.esitoBloccante();
          }
        },
        err => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.modificaNonSalvata));
        }
      );
  }

  private esitoOk() {
    this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
    this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.notificaAziendaInviata));
    this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.notificaAppagInviata));
  }

  private esitoNonBloccante(responseList) {
    this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
    if (responseList.includes('NOTIFICA_TITOLARE_FALLITA')) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.notificaAziendaFallita));
    } else {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.notificaAziendaInviata));
    }
    if (responseList.includes('NOTIFICA_APPAG_FALLITA')) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.notificaAppagFallita));
    } else {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.notificaAppagInviata));
    }
  }

  private esitoBloccante() {
    this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.modificaNonSalvata));
  }
}
