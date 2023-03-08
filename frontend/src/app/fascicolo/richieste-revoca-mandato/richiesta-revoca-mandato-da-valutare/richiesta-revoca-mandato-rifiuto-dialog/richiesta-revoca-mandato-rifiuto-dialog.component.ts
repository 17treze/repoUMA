import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { StringSupport } from 'src/app/a4g-common/utility/string-support';
import { MandatoService } from '../../../mandato.service';

@Component({
  selector: 'app-richiesta-revoca-mandato-rifiuto-dialog',
  templateUrl: './richiesta-revoca-mandato-rifiuto-dialog.component.html',
  styleUrls: ['./richiesta-revoca-mandato-rifiuto-dialog.component.css']
})
export class RichiestaRevocaMandatoRifiutoDialogComponent implements OnInit {

  @Output() displayChange = new EventEmitter();

  public display: boolean;
  public displayRifiutoDialog: boolean;
  public motivoRifiuto: string;
  public richiestaRevocaImmediata: any;

  constructor(
    protected messageService: MessageService,
    protected mandatoService: MandatoService
  ) {
    this.displayRifiutoDialog = false;
  }

  ngOnInit() {
  }

  public onOpen(richiestaRevocaImmediata: any) {
    this.display = true;
    this.motivoRifiuto = null;
    this.richiestaRevocaImmediata = richiestaRevocaImmediata;
  }

  public onClose() {
    this.display = false;
    this.displayChange.emit(false);
  }

  public rifiutaRevocaImmediata() {
    if (StringSupport.isNotEmpty(this.motivoRifiuto)) {
      this.rifiuta();
    } else {
      this.messageService.add(A4gMessages.getToast('tst-rifiutoDialog', A4gSeverityMessage.error, A4gMessages.notFoundMotivazioneRifiuto));
    }
  }

  private rifiuta() {
    this.messageService.add(
      A4gMessages.getToast(
        'checkRifiuto',
        A4gSeverityMessage.warn,
        A4gMessages.checkRifiutoRichiestaRevocaImmediata
      )
    );
  }

  public confirm() {
    this.putRifiutaRichiestaRevocaImmediata(this.richiestaRevocaImmediata.cuaa, this.motivoRifiuto);
  }

  public reject() {
    this.messageService.clear('checkRifiuto');
  }

  private putRifiutaRichiestaRevocaImmediata(cuaa, motivoRifiuto) {
    // PERCHE' QUESTO METODO E' NEL DIALOG??
    this.mandatoService.putRifiutaRichiestaRevocaImmediata(cuaa, motivoRifiuto)
      .subscribe(
        result => {
          if (result.esito === 'OK') {
            this.esitoOk();
            this.messageService.clear('checkRifiuto');
            this.onClose();
          }
          if (result.esito === 'NON_BLOCCANTE') {
            this.esitoNonBloccante(result.responseList);
            this.messageService.clear('checkRifiuto');
            this.onClose();
          }
          if (result.esito === 'KO') {
            this.esitoBloccante();
          }
        },
        err => {
          this.messageService.clear('checkRifiuto');
          this.messageService.add(A4gMessages.getToast('tst-rifiutoDialog', A4gSeverityMessage.error, A4gMessages.modificaNonSalvata));
        }
      );
  }

  private esitoOk() {
    this.messageService.add(A4gMessages.getToast('tst-rifiutoDialog', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
    this.messageService.add(A4gMessages.getToast('tst-rifiutoDialog', A4gSeverityMessage.success, A4gMessages.notificaAziendaInviata));
    this.messageService.add(A4gMessages.getToast('tst-rifiutoDialog', A4gSeverityMessage.success, A4gMessages.notificaAppagInviata));
  }

  private esitoNonBloccante(responseList) {
    this.messageService.add(A4gMessages.getToast('tst-rifiutoDialog', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
    if (responseList.includes('NOTIFICA_TITOLARE_FALLITA')) {
      this.messageService.add(A4gMessages.getToast('tst-rifiutoDialog', A4gSeverityMessage.warn, A4gMessages.notificaAziendaFallita));
    } else {
      this.messageService.add(A4gMessages.getToast('tst-rifiutoDialog', A4gSeverityMessage.success, A4gMessages.notificaAziendaInviata));
    }
    if (responseList.includes('NOTIFICA_APPAG_FALLITA')) {
      this.messageService.add(A4gMessages.getToast('tst-rifiutoDialog', A4gSeverityMessage.warn, A4gMessages.notificaAppagFallita));
    } else {
      this.messageService.add(A4gMessages.getToast('tst-rifiutoDialog', A4gSeverityMessage.success, A4gMessages.notificaAppagInviata));
    }
  }

  private esitoBloccante() {
    this.messageService.add(A4gMessages.getToast('tst-rifiutoDialog', A4gSeverityMessage.error, A4gMessages.modificaNonSalvata));
  }
}
