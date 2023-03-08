import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatiDomanda } from './../../../utenti/classi/DatiDomanda';
import { GestioneUtenzeService } from './../gestione-utenze.service';
import { AssegnazioneConfigurazioniDialogComponent } from '../assegnazione-configurazioni-dialog/assegnazione-configurazioni-dialog.component';
import { takeUntil, switchMap } from 'rxjs/operators';
import { empty, Subject } from 'rxjs';
import { RifiutoDialogComponent } from '../rifiuto-dialog/rifiuto-dialog.component';
import { RichiesteAccessoSistema } from '../richieste-accesso-sistema/dto/RichiesteAccessoSistema';
import { DateSupport } from 'src/app/a4g-common/utility/date-support';
import {Labels} from "../../../app.labels";

@Component({
  selector: 'app-dettaglio-utenza',
  templateUrl: './dettaglio-utenza.component.html',
  styleUrls: ['./dettaglio-utenza.component.scss']
})
export class DettaglioUtenzaComponent implements OnInit {
  public datiDomanda: DatiDomanda;
  public applicativi = {};
  private componentDestroyed$: Subject<boolean> = new Subject();
  public richiestaAccessoCorrente: RichiesteAccessoSistema;
  public DATE_FORMAT = DateSupport.PATTERN_DATE_2;
  public labels: Labels =  Labels;

  @ViewChild("assegnazioneConfigurazioneDialog", { static: true })
  public assegnazioneConfigurazioneDialog: AssegnazioneConfigurazioniDialogComponent;

  @ViewChild("rifiutoDialog", { static: true })
  public rifiutoDialog: RifiutoDialogComponent;

  constructor(
    private route: ActivatedRoute,
    private gestioneUtenzeService: GestioneUtenzeService,
    private router: Router) {
  }

  ngOnInit() {
    this.fillApplicativi();
    this.route.params.pipe(
      switchMap((params) => {
        let idUtenza: Number = params['idUtenza'];
        if (idUtenza) {
          return this.gestioneUtenzeService.getDatiAnagraficiUtente(idUtenza);
        } else {
          return empty();
        }
      }),
      takeUntil(this.componentDestroyed$)
    ).subscribe(datiDomanda => {
      console.log('Dati della domanda di registrazione utente', datiDomanda);
      this.datiDomanda = datiDomanda;
    });
  }

  private fillApplicativi() {
    this.applicativi['SRT'] = 'SRTrento - Misure strutturali';
    this.applicativi['A4G'] = 'A4G - Nuovo Sistema Informativo Agricoltura';
    this.applicativi['AGS'] = 'SIAP - Sistema Informativo Agricoltura';
  }

  public showDialogConfigurazioneUtente() {
    this.router.navigate(['./'], {
      relativeTo: this.route,
      queryParams: { dialog: '1' },
      queryParamsHandling: 'merge',
      replaceUrl: true
    });
  }

  public onDialogConfigurazioneToggle(isOpening) {
    if (!isOpening) {
      this.router.navigate(['./'], {
        relativeTo: this.route,
        queryParams: { dialog: '0' },
        queryParamsHandling: 'merge',
        replaceUrl: true
      });
    }
  }

  public onDialogRifiutoClose(isOpening) {
  }

  public backToUtenze() {
    switch (this.datiDomanda.stato) {
      case 'PROTOCOLLATA':
        this.router.navigate(['./'], { relativeTo: this.route.parent, queryParams: { tabselected: 'protocollate' } });
        break;
      case 'IN_LAVORAZIONE':
        this.router.navigate(['./'], { relativeTo: this.route.parent, queryParams: { tabselected: 'inlavorazione' } });
        break;
      case 'APPROVATA':
        this.router.navigate(['./'], { relativeTo: this.route.parent, queryParams: { tabselected: 'approvate' } });
        break;
      case 'RIFIUTATA':
        this.router.navigate(['./'], { relativeTo: this.route.parent, queryParams: { tabselected: 'rifiutate' } });
        break;
    }
  }

  public showDialogRifiutoDomandaRichiestaUtente(domanda: DatiDomanda) {
    this.rifiutoDialog.onOpen(domanda.datiAnagrafici);
    this.richiestaAccessoCorrente = new RichiesteAccessoSistema;
    this.richiestaAccessoCorrente.id = domanda.id;
    this.richiestaAccessoCorrente.datiAnagrafici = domanda.datiAnagrafici;
    if (domanda.dataProtocollazione) {
      const date = domanda.dataProtocollazione.toString();
      this.richiestaAccessoCorrente.dataProtocollazione = date;
    }
    this.richiestaAccessoCorrente.idProtocollo = domanda.idProtocollo;
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

}

