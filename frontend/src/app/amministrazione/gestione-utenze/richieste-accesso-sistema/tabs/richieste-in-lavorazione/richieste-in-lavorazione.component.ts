import {Component, OnInit, ViewChild} from '@angular/core';
import {MessageService} from 'primeng/api';
import {SharedService} from '../../shared-service.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ApprovazioneDialogComponent} from '../../../approvazione-dialog/approvazione-dialog.component';
import {RichiesteAccessoSistema} from '../../dto/RichiesteAccessoSistema';
import {GestioneUtenzeService} from '../../../gestione-utenze.service';
import {RifiutoDialogComponent} from '../../../rifiuto-dialog/rifiuto-dialog.component';
import {RichiesteAccessoSistemaTabComponent} from "../richieste-accesso-sistema-tab/richieste-accesso-sistema-tab.component";

@Component({
  selector: 'app-richieste-in-lavorazione',
  templateUrl: './richieste-in-lavorazione.component.html',
  styleUrls: ['./richieste-in-lavorazione.component.scss']
})
export class RichiesteInLavorazioneComponent extends RichiesteAccessoSistemaTabComponent implements OnInit {

  public richiestaAccessoCorrente: RichiesteAccessoSistema;
  @ViewChild("approvazioneDialog", { static: true })
  public approvazioneDialog: ApprovazioneDialogComponent;
  @ViewChild("rifiutoDialog", { static: true })
  public rifiutoDialog: RifiutoDialogComponent;

  constructor(protected gestioneUtenzeService: GestioneUtenzeService,
              protected messages: MessageService,
              protected sharedService: SharedService,
              protected router: Router,
              protected route: ActivatedRoute) {
    super(gestioneUtenzeService, messages, sharedService, router, route);
    this.setCols();
    this.stato = this.intestazioni.in_lavorazione;
    this.defaultSortingColumn = 'dtProtocollazione';
    this.defaultSortingOrder = 'ASC';
  }

  ngOnInit() {
    this.setupRouting('inlavorazione');
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  public showDialog(richiesta: RichiesteAccessoSistema) {
    this.approvazioneDialog.onOpen(richiesta.datiAnagrafici);
    this.richiestaAccessoCorrente = richiesta;
  }

  public showDialogRifiutoDomandaRichiestaUtente(richiesta: RichiesteAccessoSistema) {
    this.rifiutoDialog.onOpen(richiesta.datiAnagrafici);
    this.richiestaAccessoCorrente = richiesta;
  }

  public onDialogClose(isDialogOpen) {
    if (!isDialogOpen) {
      this.refreshElencoRichieste();
    }
  }

  public goToDetailWithDialog(id: number) {
    this.router.navigate(
      ['./gestioneUtenze/:idUtenza/dettaglioUtenza'.replace(':idUtenza', id.toString())],
      { relativeTo: this.route.parent.parent,
        queryParams: { dialog: 1 }
      });
  }

  protected setCols() {
    this.cols = [
      { field: "idProtocollo", header: this.intestazioni.nrProtocollo },
      { field: "dtProtocollazione", header: this.intestazioni.dtProtocollazione },
      { field: "nome", header: this.intestazioni.nome },
      { field: "cognome", header: this.intestazioni.cognome },
      { field: "codiceFiscale", header: this.intestazioni.codiceFiscale },
      { field: null, header: this.intestazioni.contatti },
      { field: "configurato", header: this.intestazioni.configurato },
      { field: null, header: this.intestazioni.azioni }
    ];
  }

}
