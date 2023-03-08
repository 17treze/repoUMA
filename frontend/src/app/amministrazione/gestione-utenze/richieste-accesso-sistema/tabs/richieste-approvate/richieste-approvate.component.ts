import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Labels} from '../../../../../../app/app.labels';
import {MessageService} from 'primeng/api';
import {A4gMessages, A4gSeverityMessage} from '../../../../../../app/a4g-common/a4g-messages';
import {SharedService} from '../../shared-service.service';
import {ActivatedRoute, Router} from '@angular/router';
import {MotivazioneApprovazioneDialogComponent} from '../../motivazione-approvazione-dialog/motivazione-approvazione-dialog.component';
import {RichiestaAccessoSistemaApprovazione} from '../../dto/RichiestaAccessoSistemaApprovazione';
import {GestioneUtenzeService} from '../../../gestione-utenze.service';
import {RichiesteAccessoSistemaTabComponent} from "../richieste-accesso-sistema-tab/richieste-accesso-sistema-tab.component";

@Component({
  selector: 'app-richieste-approvate',
  templateUrl: './richieste-approvate.component.html',
  styleUrls: ['./richieste-approvate.component.css']
})
export class RichiesteApprovateComponent extends RichiesteAccessoSistemaTabComponent implements OnInit, OnDestroy {

  @ViewChild("motivazioneApprovazioneDialogComponent", { static: true })
  public motivazioneApprovazioneDialogComponent: MotivazioneApprovazioneDialogComponent;
  public richiestaAccessoCorrente: RichiestaAccessoSistemaApprovazione;
  public istruttoriaDomandaCorrente: RichiestaAccessoSistemaApprovazione;

  constructor(protected gestioneUtenzeService: GestioneUtenzeService,
              protected messages: MessageService,
              protected sharedService: SharedService,
              protected router: Router,
              protected route: ActivatedRoute) {
    super(gestioneUtenzeService, messages, sharedService, router, route);
    this.setCols();
    this.proprieta = 'dtApprovazione';
    this.ordine = 'DESC';
    this.textTitle = Labels.APPROVATE;
    this.stato = this.intestazioni.approvata;
    this.defaultSortingColumn = 'dtApprovazione';
    this.defaultSortingOrder = 'ASC';
  }


  ngOnInit() {
    this.setupRouting('approvate');
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  public showDialog(richiesta) {
    this.getIstruttoria(richiesta);
    this.richiestaAccessoCorrente = richiesta;
  }

  public onDialogClose(evento) {
  }

  public getIstruttoria(richiesta) {
    this.gestioneUtenzeService.getIstruttoriaProfiloByDomanda(richiesta.id).subscribe(
      result => {
        this.istruttoriaDomandaCorrente = new RichiestaAccessoSistemaApprovazione();
        this.istruttoriaDomandaCorrente.idDomanda = result.idDomanda;
        this.istruttoriaDomandaCorrente.testoMail = result.testoMail;
        this.istruttoriaDomandaCorrente.note = result.note;
        this.motivazioneApprovazioneDialogComponent.onOpen(this.istruttoriaDomandaCorrente);
      }, err => {
        this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
      });
  }

  protected setCols() {
    this.cols = [
      { field: "idProtocollo", header: this.intestazioni.nrProtocollo },
      { field: "dtProtocollazione", header: this.intestazioni.dtProtocollazione },
      { field: "dtApprovazione", header: this.intestazioni.dtApprovazione },
      { field: "nome", header: this.intestazioni.nome },
      { field: "cognome", header: this.intestazioni.cognome },
      { field: "codiceFiscale", header: this.intestazioni.codiceFiscale },
      { field: null, header: this.intestazioni.contatti },
      { field: null, header: this.intestazioni.azioni }
    ];
  }

}
