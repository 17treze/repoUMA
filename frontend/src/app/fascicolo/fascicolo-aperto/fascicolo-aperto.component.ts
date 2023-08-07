import { Subscription } from 'rxjs/internal/Subscription';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { FascicoloService } from '../fascicolo.service';
import { A4gCostanti } from 'src/app/a4g-common/a4g-costanti';
import { FascicoloAgsDto, TipoDetenzioneAgs } from 'src/app/a4g-common/classi/FascicoloAgsDto';
import { FascicoloCorrente } from '../fascicoloCorrente';
import { forkJoin } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Fascicolo } from 'src/app/a4g-common/classi/Fascicolo';
import { FascicoloLazio } from 'src/app/a4g-common/classi/FascicoloLazio';
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
    private router: Router,
    private fascicoloService: FascicoloService,
    private errorService: ErrorService
  ) {
  }

  ngOnInit(): void {
    this.cuaa = this.router.url.split('/').pop();
    this.title = this.route.snapshot.data[A4gCostanti.ROUTE_DATA_BREADCRUMB].toUpperCase();
    this.fascicoloService.getFascicoloLazio(this.cuaa).subscribe(
      fascicolo => {
          if (fascicolo.data?.cuaa) {
            console.log('Cuaa: ' + fascicolo.data.cuaa);
            this.fascicoloCorrente.fascicoloLazio = fascicolo;
            this.formatDatiFascicolo();
          }
          else {
            this.errorService.showErrorWithMessage(fascicolo.text);
          }
      }, err => {
         this.errorService.showError(error, 'tst-fas-ap');
      });
  }

  ngOnDestroy() {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }

  formatDatiFascicolo() {
    // const detenzioneMandato = this.fascicoloCorrente.fascicoloLegacy.detenzioni.filter(detenzione => detenzione.tipoDetenzione === TipoDetenzioneAgs.MANDATO);
    // this.idFascicolo = this.fascicoloCorrente.fascicoloLegacy.idAgs.toString();
    this.denominazione = this.fascicoloCorrente.fascicoloLazio.data.denominazione;
    this.cuaa = this.fascicoloCorrente.fascicoloLazio.data.cuaa;
    this.caa = this.fascicoloCorrente.fascicoloLazio.data.detentore; // detenzioneMandato != null && detenzioneMandato.length === 1 ? detenzioneMandato[0].caa : null;
    this.stato = "VALIDO"; // this.fascicoloCorrente.fascicoloLegacy.stato;
    this.sportello = this.fascicoloCorrente.fascicoloLazio.data.detentore; // detenzioneMandato != null && detenzioneMandato.length === 1 ? detenzioneMandato[0].sportello : null;
    this.dataInizio = this.fascicoloCorrente.fascicoloLazio.data.dataAperturaFascicolo;
    this.dataUltimoAggiornamento = this.fascicoloCorrente.fascicoloLazio.data.dataElaborazione;
    this.dataUltimaValidazione = this.fascicoloCorrente.fascicoloLazio.data.dataValidazFascicolo;
    this.organismoPagatore = this.fascicoloCorrente.fascicoloLazio.data.organismoPagatore;
  }
}
