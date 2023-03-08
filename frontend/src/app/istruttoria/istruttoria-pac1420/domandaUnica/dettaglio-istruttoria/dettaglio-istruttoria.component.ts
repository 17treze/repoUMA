import { Component, OnInit, SimpleChanges, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IstruttoriaCorrente } from '../istruttoriaCorrente';
import { IstruttoriaService } from '../istruttoria.service';
import { Istruttoria } from '../../../../a4g-common/classi/Istruttoria';
import { RiepilogoStatoDomande } from './RiepilogoStatoDomande';
import { StatoDomandaEnum } from './statoDomanda';
import { ProcessoIstruttoria } from '../processoIstruttoria';
import { Costanti } from '../Costanti';
import { Configuration } from 'src/app/app.constants';
import { Sostegno } from './sostegno';
import { RiepilogoSostegno } from './RiepilogoSostegno';
import { ConfirmationService } from 'primeng/api';
import { SostegnoDu } from '../classi/SostegnoDu';
import { DatiDettaglio } from '../classi/DatiDettaglio';
@Component({
  selector: 'app-dettaglio-istruttoria',
  templateUrl: './dettaglio-istruttoria.component.html',
  styleUrls: ['./dettaglio-istruttoria.component.css'],
  providers: [ConfirmationService]
})
export class DettaglioIstruttoriaComponent implements OnInit, OnDestroy {

  public subIstruttoria;
  public subProcessi;
  listaRiepilogoStati: Array<RiepilogoStatoDomande>;
  listaRiepilogoSostegno: Array<RiepilogoSostegno>;

  listaProcessiAttivi: Array<ProcessoIstruttoria>;
  production: boolean;
  showProgressbarRicevibilita = false;
  disableAvviaRicevibilita = false;
  disableOkRicevibilita = false;
  valueProgressbarRicevibilita = 0;
  controlloRicev = false;
  showSostegno = false;
  controllo: any;
  interval: any;

  showProgressbarIstruttoria = false;
  disableAvviaIstruttoria = false;
  disableOkIstruttoria = false;
  valueProgressbarIstruttoria = 0;

  editable = false; // variabile che rende editabile i dati di dettaglio istruttoria

  premioAggregato: DatiDettaglio;
  premioAggregatoACS: DatiDettaglio;

  constructor(private istruttoriaCorrente: IstruttoriaCorrente,
    private route: ActivatedRoute, private istruttoriaService: IstruttoriaService,
    private router: Router, private conf: Configuration) {
  }

  ngOnInit() {

    // inizializzo la lista degli stati della domanda
    this.listaRiepilogoStati = new Array<RiepilogoStatoDomande>();
    this.listaRiepilogoStati.push(new RiepilogoStatoDomande(1, StatoDomandaEnum.PROTOCOLLATA, 0));
    this.listaRiepilogoStati.push(new RiepilogoStatoDomande(2, StatoDomandaEnum.RICEVIBILE, 0));
    this.listaRiepilogoStati.push(new RiepilogoStatoDomande(3, StatoDomandaEnum.NON_RICEVIBILE, 0));
    this.listaRiepilogoStati.push(new RiepilogoStatoDomande(4, StatoDomandaEnum.IN_ISTRUTTORIA, 0));

    this.listaRiepilogoSostegno = new Array<RiepilogoSostegno>();
    this.listaRiepilogoSostegno.push(new RiepilogoSostegno(1, Sostegno.SOSTEGNO_DISACCOPPIATO, 0));
    this.listaRiepilogoSostegno.push(new RiepilogoSostegno(2, Sostegno.SOSTEGNO_SUPERFICI, 0));
    this.listaRiepilogoSostegno.push(new RiepilogoSostegno(3, Sostegno.SOSTEGNO_ZOOTECNIA, 0));


    this.production = this.conf.production;

    this.listaRiepilogoStati.sort((a, b): number => {
      if (a.order > b.order) { return 1; }
      if (a.order < b.order) { return -1; }
      return 0;
    });

    this.listaRiepilogoSostegno.sort((a, b): number => {
      if (a.order > b.order) { return 1; }
      if (a.order < b.order) { return -1; }
      return 0;
    });

    // recupero le informazioni per l'istruttoria corrente
    this.subIstruttoria = this.route
      .params
      .subscribe(params => {
        this.getIstruttoria(params['idIstruttoria']);
      });

    // recupero la lista dei processi attivi lato backend per l'istruttoria corrente
    this.subProcessi = this.route.params.subscribe(params => {
      this.getListaProcessiAttiviIstruttoria(params['idIstruttoria']);
    });

    // TODO: per adesso implementato utilizzando l'interval. Prevedere rework per introdurre WebSocket.
    this.interval = setInterval(() => {
      if (this.listaProcessiAttivi != null && this.listaProcessiAttivi.length > 0) {
        console.log('Verifica avanzamento processo');
        this.listaProcessiAttivi.forEach(p => {
          this.controllo = this.istruttoriaService.getProcesso(p.idProcesso).subscribe((next) => {
            p.percentualeAvanzamento = next.percentualeAvanzamento;
            if (next.percentualeAvanzamento >= 100 || next.idStatoProcesso === 'PROCESSO_KO') {
              const index = this.listaProcessiAttivi.indexOf(p);
              if (index !== -1) {
                this.listaProcessiAttivi.splice(index, 1);
              }
              if (p.idTipoProcesso === 'RICEV_AGS') {
                this.disableOkRicevibilita = false;
                this.disableAvviaRicevibilita = false;
              } else if (p.idTipoProcesso === 'AVV_IST') {
                this.disableOkIstruttoria = false;
                this.disableAvviaIstruttoria = false;
              }
            }
          });
        });
      }
    }, 10000);

    this.route.params.subscribe(params => {
      this.istruttoriaService.getDatiDettaglio(params['idIstruttoria'], { identificativoSostegno: SostegnoDu.ZOOTECNIA }).subscribe((d: DatiDettaglio) => {
        this.premioAggregato = d;
      },
        error => this.handleError(error));
      this.istruttoriaService.getDatiDettaglio(params['idIstruttoria'], { identificativoSostegno: SostegnoDu.SUPERFICIE }).subscribe((d: DatiDettaglio) => {
        this.premioAggregatoACS = d;
      },
        error => this.handleError(error));
    });


  }

  ngOnDestroy() {
    this.subIstruttoria.unsubscribe();
    this.subProcessi.unsubscribe();
    clearInterval(this.interval);
  }

  addDomandePerStato(stato: string, numeroDomande: number) {
    this.listaRiepilogoStati.map(a => { if (a.stato === stato) { a.counter = numeroDomande; } });
    if (numeroDomande === 0 && stato === StatoDomandaEnum.RICEVIBILE) {
      this.disableAvviaIstruttoria = true;
    }
  }

  addDomandePerSostegno(sostegno: string, numeroDomande: number) {
    this.listaRiepilogoSostegno.map(a => { if (a.descrizioneSostegno === sostegno) { a.counter = numeroDomande; } });
  }

  getIstruttoria(idIstruttoria: number): void {
    this.istruttoriaService.getIstruttoria(idIstruttoria)
      .subscribe((next) => this.countDomandeDU(next));
    this.countDomandePerStato(StatoDomandaEnum.RICEVIBILE, idIstruttoria);
    this.countDomandePerStato(StatoDomandaEnum.NON_RICEVIBILE, idIstruttoria);
    this.countDomandePerStato(StatoDomandaEnum.IN_ISTRUTTORIA, idIstruttoria);
    this.countDomandeSostegno(Sostegno.SOSTEGNO_DISACCOPPIATO, idIstruttoria);
    this.countDomandeSostegno(Sostegno.SOSTEGNO_SUPERFICI, idIstruttoria);
    this.countDomandeSostegno(Sostegno.SOSTEGNO_ZOOTECNIA, idIstruttoria);
  }

  aggiornaDomandeCounters(idIstruttoria) {
    this.getIstruttoria(Number.parseInt(idIstruttoria.toString()));
  }

  countDomandeDU(result: any): void {
    this.istruttoriaCorrente.istruttoria = result;
    const istruttoriaJSON = encodeURIComponent(JSON.stringify(this.istruttoriaCorrente.istruttoria));
    this.istruttoriaService.countDomandeDU(istruttoriaJSON)
      .subscribe((next) => this.listaRiepilogoStati.map(a => { if (a.stato === StatoDomandaEnum.PROTOCOLLATA) { a.counter = next; } }));
  }

  getIstruttoriaCorrente(): Istruttoria {
    return this.istruttoriaCorrente.istruttoria;
  }

  countDomandePerStato(stato: string, idIstruttoria: number): void {
    const baseRequest = '{"idDatiSettore": "' + idIstruttoria +
      '", "statoDomanda": "' + stato + '"}';
    const requestJSON = encodeURIComponent(baseRequest);
    this.istruttoriaService.countDomandeFiltered(requestJSON).
      subscribe((next) => this.addDomandePerStato(stato, next));
  }

  countDomandeSostegno(sostegno: string, idIstruttoria: number): void {
    const richiestaSostegno: string = RiepilogoSostegno.ritornaSostegno(sostegno);
    const baseRequest = '{ "idDatiSettore": "' + idIstruttoria +
      '", "statoDomanda": "' + StatoDomandaEnum.IN_ISTRUTTORIA +
      '" , "sostegno": "' + richiestaSostegno + '"}';
    const requestJSON = encodeURIComponent(baseRequest);
    this.istruttoriaService.countDomandeFiltered(requestJSON).subscribe((next) => this.addDomandePerSostegno(sostegno, next));
  }


  avviaProcessoIstruttoria(tipoProcesso: string) {
    // const processo: ProcessoIstruttoria = new ProcessoIstruttoria();
    // processo.annoCampagna = Number(this.istruttoriaCorrente.istruttoria.annoRiferimento);
    // processo.idTipoProcesso = tipoProcesso;
    // processo.percentualeAvanzamento = 0;
    // processo.idStatoProcesso = 'START';
    // processo.idDatiSettore = this.istruttoriaCorrente.istruttoria.id;
    // console.log('Dettaglio processo' + JSON.stringify(processo, this.replacerProcessoJson));
    // this.istruttoriaService.avviaProcessoIstruttoriaDU(JSON.stringify(processo, this.replacerProcessoJson)).subscribe((next) => {
    //   if (next > 0 && next != null) {
    //     processo.idStatoProcesso = 'RUN';
    //     processo.idProcesso = next;
    //     this.listaProcessiAttivi.push(processo);
    //     if (processo.idTipoProcesso === 'RICEV_AGS') {
    //       this.disableAvviaRicevibilita = true;
    //       this.showProgressbarRicevibilita = true;
    //       this.disableOkRicevibilita = true;
    //       this.controlloRicev = true;
    //     } else if (processo.idTipoProcesso === 'AVV_IST') {
    //       this.disableAvviaIstruttoria = true;
    //       this.showProgressbarIstruttoria = true;
    //       this.disableOkIstruttoria = true;
    //     }
    //   }
    //   console.log(processo.numeroDomandeDaElaborare);
    //   console.log(this.production);
    //   console.log(processo);
    // });
  }

  getListaProcessiAttiviIstruttoria(idIstruttoria: number): void {
    const requestListaProcessi = '{ "idIstruttoria": ' + idIstruttoria + ' }';
    this.istruttoriaService.getListaProcessiAttivi(encodeURIComponent(requestListaProcessi)).subscribe((next) => {
      this.listaProcessiAttivi = next;
      if (this.listaProcessiAttivi != null) {
        this.listaProcessiAttivi.map(p => {
          if (p.idTipoProcesso === 'RICEV_AGS') {
            this.disableAvviaRicevibilita = true;
            this.showProgressbarRicevibilita = true;
            this.valueProgressbarRicevibilita = p.percentualeAvanzamento;
            this.disableOkRicevibilita = true;
          }
          if (p.idTipoProcesso === 'AVV_IST') {
            this.disableAvviaIstruttoria = true;
            this.showProgressbarIstruttoria = true;
            this.valueProgressbarIstruttoria = p.percentualeAvanzamento;
            this.disableOkIstruttoria = true;
          }
        });
      } else {
        this.listaProcessiAttivi = new Array<ProcessoIstruttoria>();
      }
    });
  }

  getPercentualeAvanzamentoProcesso(tipoProcesso: string): number {
    let percentuale = 100;
    if (this.listaProcessiAttivi != null && this.listaProcessiAttivi.length > 0) {
      this.listaProcessiAttivi.map(p => {
        if (p.idTipoProcesso === tipoProcesso) {
          percentuale = p.percentualeAvanzamento;
        }
      });
    }
    return percentuale;
  }

  collassaDivProgressBar(tipoProcesso: string) {
    if (tipoProcesso === 'RICEV_AGS') {
      this.showProgressbarRicevibilita = false;
    } else if (tipoProcesso === 'AVV_IST') {
      this.showProgressbarIstruttoria = false;
    }
  }

  goListaDomande(stato: String): void {
    this.router.navigate(['./' + Costanti.elencoDomande], { relativeTo: this.route, queryParams: { stato: stato } });
  }

  goListaSostegno(stato: String): void {
    this.router.navigate(['./' + Costanti.cruscottoDisaccoppiato], { relativeTo: this.route, queryParams: { stato: stato } });
  }

  vediTipiSostegno() {
    this.showSostegno = !this.showSostegno;
  }

  refresh(): void {
    window.location.reload();
  }

  disableButtonPerStato(stato: string): boolean {
    let disable = false;
    this.listaRiepilogoStati.map(riepilogo => {
      if (riepilogo.stato === stato) {
        if (riepilogo.counter === 0) {
          disable = true;
        }
      }
    });
    return disable;
  }


  replacerProcessoJson(key, value) {
    console.log(typeof value);
    if (key === 'numeroDomandeDaElaborare') {
      return undefined;
    }
    return value;
  }

  aggiornaPremioAggregato(premio: DatiDettaglio) {
    let datidettaglioUpdated: DatiDettaglio = new DatiDettaglio();
    this.istruttoriaService.aggiornaInterventiPremio(this.istruttoriaCorrente.istruttoria.id, premio.interventoDuPremi).subscribe(interventoDuPremi => {
      datidettaglioUpdated.interventoDuPremi = interventoDuPremi;
      this.istruttoriaService.aggiornaDomandaIntegrativa(this.istruttoriaCorrente.istruttoria.id, premio.sostegnoDuDi).subscribe(sostegnoDuDi => {
        datidettaglioUpdated.sostegnoDuDi = sostegnoDuDi;
        this.premioAggregato = datidettaglioUpdated;
      },
        error => this.handleError(error));
    },
      error => this.handleError(error));
  }

  aggiornaPremioAggregatoACS(premio: DatiDettaglio) {
    let datidettaglioUpdated: DatiDettaglio = new DatiDettaglio();
    this.istruttoriaService.aggiornaInterventiPremio(this.istruttoriaCorrente.istruttoria.id, premio.interventoDuPremi).subscribe(interventoDuPremi => {
      datidettaglioUpdated.interventoDuPremi = interventoDuPremi
      this.premioAggregatoACS = datidettaglioUpdated;
    },
      error => this.handleError(error));
  }

  handleError(error: any) {
    console.log("Error " + error + " msg " + error.error + " error.code " + error.status);
  }

}
