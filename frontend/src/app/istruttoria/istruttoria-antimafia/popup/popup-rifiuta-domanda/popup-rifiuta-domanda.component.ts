import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { Labels } from 'src/app/app.labels';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { NgForm } from '@angular/forms';
import { NotaDichiarazioneRifiutata } from 'src/app/fascicolo/antimafia/classi/NotaDichiarazioneRifiutata';
import { AntimafiaService } from 'src/app/fascicolo/antimafia/antimafia.service';
import { DichiarazioneAntimafia } from 'src/app/fascicolo/antimafia/classi/dichiarazioneAntimafia';
import { StatoDichiarazione } from 'src/app/fascicolo/antimafia/classi/statoDichiarazioneEnum';
import { SincronizzazioneAntimafiaService } from 'src/app/fascicolo/antimafia/sincronizzazione-antimafia.service';
import { MessageService } from 'primeng/api';
import { LoaderService } from 'src/app/loader.service';

@Component({
  selector: 'app-popup-rifiuta-domanda',
  templateUrl: './popup-rifiuta-domanda.component.html',
  styleUrls: ['./popup-rifiuta-domanda.component.css']
})
export class PopupRifiutaDomandaComponent implements OnInit {

  headerPopup;
  labels = Labels;
  displayDialog: boolean; // determina se è visibile il popup

  nrProtocolloComunicazione: string = undefined;
  noteDiChiusura: string = undefined;
  @Input() dichiarazioneAntimafia: DichiarazioneAntimafia;
  @Output() event = new EventEmitter();

  constructor(
    private antimafiaService: AntimafiaService,
    private sincronizzazioneAntimafiaService: SincronizzazioneAntimafiaService,
    private messages: MessageService,
    private loader: LoaderService) { }

  // comportamento da eseguire in caso di "conferma"
  public onSuccess = (dichiarazione: DichiarazioneAntimafia) => {
    const noteDichiarazioneRifiutata: NotaDichiarazioneRifiutata = {
      nrProtocolloComunicazione: this.nrProtocolloComunicazione,
      noteDiChiusura: this.noteDiChiusura
    }
    this.antimafiaService.creaNotaDichiarazioneAntimafia(dichiarazione.id, noteDichiarazioneRifiutata).subscribe(result => {
      dichiarazione.stato.identificativo = StatoDichiarazione.RIFIUTATA;
      dichiarazione.stato.id = null;
      dichiarazione.stato.descrizione = null;

      dichiarazione.dtFine = new Date();
      this.antimafiaService.aggiornaDichiarazioneAntimafia(dichiarazione).subscribe(dichiarazioneUpdate => {
        // this.antimafiaService.sincronizzaDateBDNAAntimafia([new SincronizzazioneDateBdnaDto(dichiarazioneUpdate.dichiarazione.datiDichiarazione.dettaglioImpresa.codiceFiscale)]);
        this.event.emit(); // aggiorna tabella padre
        this.sincronizzazioneAntimafiaService.getSincronizzazioneAntimafia(dichiarazione.id).subscribe(sincronizzazione => {
          if (sincronizzazione) {
            sincronizzazione.dataFineVali = new Date();
            this.sincronizzazioneAntimafiaService.putSincronizzazioneAntimafia(dichiarazione.id, sincronizzazione).subscribe(() => {
              this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
            }, err => {
              // err put sincronizzazione
              this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, A4gMessages.BRIAMCMN007));
              // NOTA: da business no rollback - solo messaggio di errore + invio mail backend
            });
          }
        }, err => {
          // err get sincronizzazione
          this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, A4gMessages.BRIAMCMN007));
        });
      }, err => {
        // err put dichiarazione
        this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, A4gMessages.CTRLIAMCNT007_KO));
      });
    }, err => {
      // err crea nota
      this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, A4gMessages.CTRLIAMCNT007_KO));
    });
  };

  ngOnInit() {
    this.loader.setTimeout(480000); //otto minuti
  }

  ngOnDestroy(): void {
    this.loader.resetTimeout();
  }
  open() {
    this.headerPopup = A4gMessages.COMUNICAZIONE_RIFIUTO_DOMANDA(this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa.denominazione);
    this.displayDialog = true;
  }
  close() {
    this.displayDialog = false;
  }

  conferma(form: NgForm) {
    if (this.areValidFields(this.nrProtocolloComunicazione) && this.areValidFields(this.noteDiChiusura)) {
      this.onSuccess(this.dichiarazioneAntimafia);
      this.close();
    }
    form.form.reset();
  }

  annulla(form: NgForm) {
    this.close();
    form.form.reset();
  }

  areValidFields = (input: string): boolean => {
    if (input) {
      return input.trim() ? true : false;
    } else {
      return true;
    }
  }


}
