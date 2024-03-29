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
    private fascicoloService: FascicoloService,
    private errorService: ErrorService
  ) {
  }

  ngOnInit(): void {
    this.sub = this.route.params
      .pipe(
        switchMap((params: Params) => {
          console.log('step-fascicoloaperto');
          return this.fascicoloService.getFascicoloLazio(params['idFascicolo']);
        })).subscribe((fascicolo: FascicoloLazio) => {
          if (fascicolo.data) {
            console.log('fascicolo.data: ' + JSON.stringify(fascicolo.data));
            this.fascicoloCorrente.fascicoloLazio = fascicolo;
            this.formatDatiFascicolo();
          }
          else {
            console.log('err11');
            this.errorService.showErrorWithMessage(fascicolo.text);
          }
        }, error => {
          console.log('err12');
          this.errorService.showError(error, 'tst-fas-ap');
        });
    this.title = this.route.snapshot.data[A4gCostanti.ROUTE_DATA_BREADCRUMB].toUpperCase();
  }

  ngOnDestroy() {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }

  formatDatiFascicolo() {
    const detenzioneMandato = this.fascicoloCorrente.fascicoloLazio.data.codiTipoDete;
    this.idFascicolo = this.fascicoloCorrente.fascicoloLazio.data.codiCuaa;
    this.denominazione = this.fascicoloCorrente.fascicoloLazio.data.descDeno;
    this.cuaa = this.fascicoloCorrente.fascicoloLazio.data.codiCuaa;
    this.caa = this.fascicoloCorrente.fascicoloLazio.data.descDete; 
    this.stato = "VALIDO"; // this.fascicoloCorrente.fascicoloLegacy.stato;
    this.sportello = this.fascicoloCorrente.fascicoloLazio.data.descDete; 
    this.dataInizio = this.dateFromString(this.fascicoloCorrente.fascicoloLazio.data.dataAperFasc);
    this.dataUltimoAggiornamento = this.dateFromString(this.fascicoloCorrente.fascicoloLazio.data.dataElab);
    this.dataUltimaValidazione = this.dateFromString(this.fascicoloCorrente.fascicoloLazio.data.dataValiFasc);
    this.organismoPagatore = this.fascicoloCorrente.fascicoloLazio.data.op.descOrgaPaga;
  }
  
  dateFromString (dateStr) {
    return new Date(parseInt(dateStr.substring(0, 4)), 
    	parseInt(dateStr.substring(4, 6)), parseInt(dateStr.substring(6, 8)));
  }
}
