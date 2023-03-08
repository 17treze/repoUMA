import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MessageService} from 'primeng/api';
import {A4gMessages, A4gSeverityMessage} from '../../../../../../app/a4g-common/a4g-messages';
import {SharedService} from '../../shared-service.service';
import {ActivatedRoute, Router} from '@angular/router';
import {GestioneUtenzeService} from '../../../gestione-utenze.service';
import {MotivazioneRifiutoDialogComponent} from '../../../motivazione-rifiuto-dialog/motivazione-rifiuto-dialog.component';
import {RichiestaAccessoSistemaRifiuto} from '../../dto/richiesta-accesso-sistema-rifiuto';
import {RichiesteAccessoSistemaTabComponent} from "../richieste-accesso-sistema-tab/richieste-accesso-sistema-tab.component";

@Component({
  selector: 'app-richieste-rifiutate',
  templateUrl: './richieste-rifiutate.component.html',
  styleUrls: ['./richieste-rifiutate.component.css']
})
export class RichiesteRifiutateComponent extends RichiesteAccessoSistemaTabComponent implements OnInit, OnDestroy {

  public richiestaAccessoCorrente: RichiestaAccessoSistemaRifiuto;
  public istruttoriaDomandaCorrente: RichiestaAccessoSistemaRifiuto;
  @ViewChild("motivazioneRifiutoDialog", { static: true })
  public motivazioneRifiutoDialog: MotivazioneRifiutoDialogComponent;

  constructor(protected gestioneUtenzeService: GestioneUtenzeService,
              protected messages: MessageService,
              protected sharedService: SharedService,
              protected router: Router,
              protected route: ActivatedRoute) {
    super(gestioneUtenzeService, messages, sharedService, router, route);
    this.setCols();
    this.proprieta = 'dtRifiuto';
    this.ordine = 'DESC';
    this.stato = this.intestazioni.rifiutata;
    this.defaultSortingColumn = 'dtRifiuto';
    this.defaultSortingOrder = 'ASC';
  }

  ngOnInit() {
    this.setupRouting('rifiutate');
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }


  public showMotivazioneRifiutoDialog(richiesta) {
    this.getIstruttoria(richiesta);
    this.richiestaAccessoCorrente = richiesta;

  }

  public onDialogClose(isDialogOpen) {
    if (!isDialogOpen) {
      this.refreshElencoRichieste();
    }
  }

  public getIstruttoria(richiesta) {
    this.gestioneUtenzeService.getIstruttoriaProfiloByDomanda(richiesta.id).subscribe(
      result => {
        this.istruttoriaDomandaCorrente = new RichiestaAccessoSistemaRifiuto();
        this.istruttoriaDomandaCorrente.motivazioneRifiuto = result.motivazioneRifiuto;
        this.istruttoriaDomandaCorrente.note = result.note;
        this.istruttoriaDomandaCorrente.testoMail = result.testoMail;
        this.motivazioneRifiutoDialog.onOpen(this.istruttoriaDomandaCorrente);
      }, err => {
        this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
      }
    );
  }


  protected setCols() {
    this.cols = [
      { field: "idProtocollo", header: this.intestazioni.nrProtocollo },
      { field: "dtProtocollazione", header: this.intestazioni.dtProtocollazione },
      { field: "dtRifiuto", header: this.intestazioni.dtRifiuto },
      { field: "nome", header: this.intestazioni.nome },
      { field: "cognome", header: this.intestazioni.cognome },
      { field: "codiceFiscale", header: this.intestazioni.codiceFiscale },
      { field: null, header: this.intestazioni.contatti },
      { field: null, header: this.intestazioni.azioni }
    ];
  }

}
