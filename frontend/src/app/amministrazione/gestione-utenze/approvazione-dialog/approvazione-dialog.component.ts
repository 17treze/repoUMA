import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { RichiesteAccessoSistema } from '../richieste-accesso-sistema/dto/RichiesteAccessoSistema';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { GestioneUtenzeService } from '../gestione-utenze.service';
import { RichiestaAccessoSistemaApprovazione } from '../richieste-accesso-sistema/dto/RichiestaAccessoSistemaApprovazione';
import { DatiAnagrafici } from 'src/app/utenti/classi/datiAnagrafici';
import { Params, Router } from '@angular/router';

@Component({
  selector: 'app-approvazione-dialog',
  templateUrl: './approvazione-dialog.component.html',
  styleUrls: ['./approvazione-dialog.component.css']
})
export class ApprovazioneDialogComponent implements OnInit {
  public display: boolean;
  public testoMail: string = '';
  public note: string;
  public displayApprovazioneDialog: boolean;

  @Output() displayChange = new EventEmitter();
  @Input() richiestaAccessoCorrente: RichiesteAccessoSistema;

  constructor(
    private messages: MessageService,
    private gestioneUtenzeSerice: GestioneUtenzeService,
    protected router: Router
  ) {
    this.displayApprovazioneDialog = false;
  }

  ngOnInit() { }

  public onOpen(datiAnagrafici: DatiAnagrafici) {
    this.display = true;
    this.displayChange.emit(true);
    this.note = null;
    this.testoMail = "Gentile " +
                      datiAnagrafici.nome + ' ' + datiAnagrafici.cognome + ",\n" +
                      "come da richiesta si è provveduto ad abilitarla al Sistema Informativo A4G al quale potrà accedere nei seguenti modi:\n\n" +
                      "- Da smartphone attraverso il seguente link: https://myappag.it accedendo con CPS/CNS o con SPID\n\n" +
                      "- Da personal computer o tablet attraverso il seguente link: https://a4g.provincia.tn.it\n\n" +
                      "Accedendo “come cittadino” potrà scegliere di autenticarsi con CPS/CNS o con SPID.\n" +
                      "Se possiede un'utenza della Provincia Autonoma di Trento, potrà accedere “come dipendente”.\n" +
                      "Dopo l’autenticazione, nella sezione A4G - Nuovo Sistema Informativo Agricoltura, dovrà selezionare il profilo interessato per poter operare.\n" +
                      "Cordiali saluti.";
  }

  public onClose() {
    this.display = false;
    this.displayChange.emit(false);
  }

  public approvaDomanda(idDomanda: number) {
    //se è presente il testo della mail chiamo il servizio di approvazione
    //altrimenti mostro error toast message
    if (this.testoMail.trim().length > 0) {
      //creo oggetto da passare al service
      const richiestaApprovazione = new RichiestaAccessoSistemaApprovazione();
      richiestaApprovazione.idDomanda = idDomanda;
      richiestaApprovazione.testoMail = this.testoMail;
      richiestaApprovazione.note = this.note;
      console.log(richiestaApprovazione);
      //call service
      this.gestioneUtenzeSerice.approvaRichiestaAccessoSistema(idDomanda, richiestaApprovazione)
        .subscribe(result => {
          this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
          this.onClose();
          const queryParams: Params = {
            tabselected: 'approvate'
            //, dialog: 0
          };
          this.router.navigate(
            [],
            {
              replaceUrl: false,
              queryParams: queryParams,
              queryParamsHandling: 'merge'
            });
        }, err => {
          this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.salvataggioDatiKo));
        });
    } else {
      this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.inputRequiredInvalid));
    }
  }

}
