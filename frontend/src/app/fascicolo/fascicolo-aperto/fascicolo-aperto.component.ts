import { Subscription } from 'rxjs/internal/Subscription';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { FascicoloService } from '../fascicolo.service';
import { A4gCostanti } from 'src/app/a4g-common/a4g-costanti';
import { FascicoloAgsDto, TipoDetenzioneAgs } from 'src/app/a4g-common/classi/FascicoloAgsDto';
import { FascicoloCorrente } from '../fascicoloCorrente';
import { forkJoin } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Fascicolo } from 'src/app/a4g-common/classi/Fascicolo';
import { ErrorService } from 'src/app/a4g-common/services/error.service';

@Component({
  selector: 'app-fascicolo-aperto',
  templateUrl: './fascicolo-aperto.component.html',
  styleUrls: ['./fascicolo-aperto.component.scss']
})

export class FascicoloApertoComponent implements OnInit, OnDestroy {
  public sub: Subscription;
  public idFascicolo: string;
  public denominazione: string;
  public cuaa: string;
  public stato: string;
  public organismoPagatore: string;
  public caa: string;
  public sportello: string;
  public dataInizio: Date;
  public dataUltimoAggiornamento: Date;
  public dataUltimaValidazione: Date;
  public title: string;

  constructor(
    public fascicoloCorrente: FascicoloCorrente,
    private route: ActivatedRoute,
    private fascicoloService: FascicoloService,
    private errorService: ErrorService
  ) {
  }

  ngOnInit(): void {
    this.sub = this.route.params
      .pipe(
        switchMap((params: Params) => {
          return forkJoin([this.fascicoloService.getFascicolo(params['idFascicolo']), this.fascicoloService.getLegacy(params['idFascicolo'])]);
        })).subscribe(([fascicolo, fascicoloLegacy]: [Fascicolo, FascicoloAgsDto]) => {
          if (fascicoloLegacy && fascicoloLegacy.cuaa != null) {
            this.fascicoloCorrente.fascicoloLegacy = fascicoloLegacy;
          }
          if (fascicolo && fascicolo.cuaa != null) {
            this.fascicoloCorrente.fascicolo = fascicolo;
          }
          this.formatDatiFascicolo();
        }, error => this.errorService.showError(error, 'tst-fas-ap'));
    this.title = this.route.snapshot.data[A4gCostanti.ROUTE_DATA_BREADCRUMB].toUpperCase();
  }



  ngOnDestroy() {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }

  formatDatiFascicolo() {
    const detenzioneMandato = this.fascicoloCorrente.fascicoloLegacy.detenzioni.filter(detenzione => detenzione.tipoDetenzione === TipoDetenzioneAgs.MANDATO);
    this.idFascicolo = this.fascicoloCorrente.fascicoloLegacy.idAgs.toString();
    this.denominazione = this.fascicoloCorrente.fascicoloLegacy.denominazione;
    this.cuaa = this.fascicoloCorrente.fascicoloLegacy.cuaa;
    this.caa = detenzioneMandato != null && detenzioneMandato.length === 1 ? detenzioneMandato[0].caa : null;
    this.stato = this.fascicoloCorrente.fascicoloLegacy.stato;
    this.sportello = detenzioneMandato != null && detenzioneMandato.length === 1 ? detenzioneMandato[0].sportello : null;
    this.dataInizio = this.fascicoloCorrente.fascicoloLegacy.dataCostituzione;
    this.dataUltimoAggiornamento = this.fascicoloCorrente.fascicoloLegacy.dataAggiornamento;
    this.dataUltimaValidazione = this.fascicoloCorrente.fascicoloLegacy.dataValidazione;
    this.organismoPagatore = this.fascicoloCorrente.fascicoloLegacy.organismoPagatore;
  }
}
