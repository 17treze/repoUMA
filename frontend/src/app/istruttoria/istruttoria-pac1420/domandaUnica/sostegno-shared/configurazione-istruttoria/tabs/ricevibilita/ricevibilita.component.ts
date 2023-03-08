import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ActivatedRoute, Router } from '@angular/router';
import { ConfigurazioneIstruttoriaService } from '../../shared/configurazione.istruttoria.service';
import { ConfigurazioneRicevibilita } from '../../../../classi/ConfigurazioneRicevibilita';
import { Localization } from 'src/app/a4g-common/utility/localization';
import { DatePipe } from '@angular/common';
import { IstruttoriaCorrente } from '../../../../istruttoriaCorrente';
import { IstruttoriaService } from '../../../../istruttoria.service';
import { Istruttoria } from '../../../../../../../a4g-common/classi/Istruttoria';
import { RiepilogoStatoDomande } from '../../../../dettaglio-istruttoria/RiepilogoStatoDomande';
import { StatoDomandaEnum } from '../../../../dettaglio-istruttoria/statoDomanda';
import { ProcessoIstruttoria } from '../../../../processoIstruttoria';
import { Configuration } from 'src/app/app.constants';
import { Sostegno } from '../../../../dettaglio-istruttoria/sostegno';
import { RiepilogoSostegno } from '../../../../dettaglio-istruttoria/RiepilogoSostegno';
import { DatiDettaglio } from '../../../../classi/DatiDettaglio';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { ConfigurazioneIstruttorie } from '../../../dto/ConfigurazioneIstruttorie';

@Component({
  selector: 'app-ricevibilita',
  templateUrl: './ricevibilita.component.html',
  styleUrls: ['./ricevibilita.component.css'],
  providers: [DatePipe]
})
export class RicevibilitaComponent implements OnInit {
  public annoCampagna: number;

  public labels: string[] = [
    "Scadenza presentazione domande di ritiro parziale",
    "Scadenza presentazione domande iniziali con ritardo",
    "Data di ricevibilità",
    "Scadenza presentazione domande iniziali",
    "Percentuale Disciplina Finanziaria",
    "Percentuale pagamento",
    "Scadenza presentazione domande di modifica",
    "Scadenza presentazione domande di modifica con ritardo"];
  public dateForm: FormGroup;
  public date: ConfigurazioneRicevibilita = new ConfigurazioneRicevibilita();
  public language: any = Localization.itCalendar();
  edit: boolean = false;
  public confIstruttoria: ConfigurazioneIstruttorie = new ConfigurazioneIstruttorie();

  public subIstruttoria;
  public subProcessi;
  public disabledPercentualePagamento: boolean = false;
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

  constructor(private messageService: MessageService,
    private route: ActivatedRoute,
    private confIstruttoriaService: ConfigurazioneIstruttoriaService,
    private fb: FormBuilder,
    private istruttoriaCorrente: IstruttoriaCorrente,
    private istruttoriaService: IstruttoriaService,
    private datePipe: DatePipe,
    private router: Router, private conf: Configuration) { }

  ngOnInit() {
    this.annoCampagna = this.route.snapshot.params.annoCampagna;
    this.compareDates();
    this.getConfigurazioneIstruttorie();

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

  }

  private compareDates() {
    var today, someday;
    today = new Date();
    someday = new Date(); //deadline per editing percentualePagamento
    someday.setFullYear(this.annoCampagna, 10, 30);
    if (today > someday) {
      this.disabledPercentualePagamento = true;
    }
  }

  public getConfigurazioneIstruttorie() {
    this.confIstruttoriaService.getConfigurazioneIstruttorie(this.annoCampagna)
      .subscribe(
        result => {
          if (result===null) {
            let dataDefault = this.annoCampagna + '-12-31T00:00:00';
            this.confIstruttoria.campagna = this.annoCampagna;
            this.confIstruttoria.dtScadenzaDomandeIniziali = new Date(dataDefault);
            this.confIstruttoria.percentualeDisciplinaFinanziaria = 0;
            this.confIstruttoria.percentualePagamento = 0;
            this.confIstruttoria.dtScadenzaDomandeModifica = new Date(dataDefault);
          } else {
            this.confIstruttoria.id = result.id;
            this.confIstruttoria.campagna = result.campagna;
            this.confIstruttoria.dtScadenzaDomandeIniziali = new Date(result.dtScadenzaDomandeIniziali);
            this.confIstruttoria.percentualeDisciplinaFinanziaria = result.percentualeDisciplinaFinanziaria;
            this.confIstruttoria.percentualePagamento = result.percentualePagamento;
            this.confIstruttoria.dtScadenzaDomandeModifica = new Date(result.dtScadenzaDomandeModifica);
          }
          this.getConfigurazioneRicevibilitaAndSetForm();
        },
        error => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        }
      );
  }

  public getConfigurazioneRicevibilitaAndSetForm() {
    this.confIstruttoriaService.getConfigurazioneRicevibilita(this.annoCampagna).subscribe(configurazione => {

      if (configurazione===null) {
        let dataDefault = this.annoCampagna + '-12-31T00:00:00';
        this.date.campagna = this.annoCampagna;
        this.date.dataRicevibilita = new Date(dataDefault);
        this.date.dataScadenzaDomandaInizialeInRitardo = new Date(dataDefault);
        this.date.dataScadenzaDomandaRitiroParziale = new Date(dataDefault);
        this.date.dataScadenzaDomandaModificaInRitardo = new Date(dataDefault);
      } else {
        this.date.id = configurazione.id;
        this.date.campagna = configurazione.campagna;
        this.date.dataRicevibilita = new Date(configurazione.dataRicevibilita);
        this.date.dataScadenzaDomandaInizialeInRitardo = new Date(configurazione.dataScadenzaDomandaInizialeInRitardo);
        this.date.dataScadenzaDomandaRitiroParziale = new Date(configurazione.dataScadenzaDomandaRitiroParziale);
        this.date.dataScadenzaDomandaModificaInRitardo = new Date(configurazione.dataScadenzaDomandaModificaInRitardo);
      }
      this.dateForm = this.fb.group({
        "dataRicevibilita": this.fb.control(this.date.dataRicevibilita, [Validators.required]),
        "dataScadenzaDomandaInizialeInRitardo": this.fb.control(this.date.dataScadenzaDomandaInizialeInRitardo, [Validators.required]),
        "dataScadenzaDomandaRitiroParziale": this.fb.control(this.date.dataScadenzaDomandaRitiroParziale, [Validators.required]),
        "dtScadenzaDomandeIniziali": this.fb.control(this.confIstruttoria.dtScadenzaDomandeIniziali, [Validators.required]),
        "percentualeDisciplinaFinanziaria": this.fb.control((this.confIstruttoria.percentualeDisciplinaFinanziaria * 100).toFixed(6), [Validators.required, Validators.min(0), Validators.max(100), Validators.pattern(/^-?\d{0,3}(\.?\d{0,6})$/)]),
        "percentualePagamento": this.fb.control((this.confIstruttoria.percentualePagamento * 100), [Validators.required, Validators.min(0), Validators.max(100), Validators.pattern(/^-?\d{0,3}(\.?\d{0,2})$/)]),
        "dataScadenzaDomandaModificaInRitardo": this.fb.control(this.date.dataScadenzaDomandaModificaInRitardo, [Validators.required]),
        "dtScadenzaDomandeModifica": this.fb.control(this.confIstruttoria.dtScadenzaDomandeModifica, [Validators.required]),
      })
      console.log(configurazione);
    });
  }

  public modifica() {
    this.edit = true;
  }

  public annulla() {
    this.edit = false;
  }

  public salva() {
    this.messageService.add({ key: 'checkSalvataggio', sticky: true, severity: 'warn', summary: 'Operazione di Salvataggio', detail: 'Sei sicuro di voler salvare?' });
  }

  public confermaSalvataggio() {
    this.edit = false;
    this.date.dataRicevibilita = new Date(this.datePipe.transform(this.dateForm.controls.dataRicevibilita.value, "yyyy-MM-dd"));
    this.date.dataScadenzaDomandaInizialeInRitardo = new Date(this.datePipe.transform(this.dateForm.controls.dataScadenzaDomandaInizialeInRitardo.value, "yyyy-MM-dd"));
    this.date.dataScadenzaDomandaRitiroParziale = new Date(this.datePipe.transform(this.dateForm.controls.dataScadenzaDomandaRitiroParziale.value, "yyyy-MM-dd"));
    this.confIstruttoria.dtScadenzaDomandeIniziali = new Date(this.datePipe.transform(this.dateForm.controls.dtScadenzaDomandeIniziali.value, "yyyy-MM-dd"));
    this.confIstruttoria.percentualePagamento = this.dateForm.controls.percentualePagamento.value / 100;
    this.confIstruttoria.percentualeDisciplinaFinanziaria = Number(this.dateForm.controls.percentualeDisciplinaFinanziaria.value) / 100;
    this.date.dataScadenzaDomandaModificaInRitardo = new Date(this.datePipe.transform(this.dateForm.controls.dataScadenzaDomandaModificaInRitardo.value, "yyyy-MM-dd"));
    this.confIstruttoria.dtScadenzaDomandeModifica = new Date(this.datePipe.transform(this.dateForm.controls.dtScadenzaDomandeModifica.value, "yyyy-MM-dd"));
    this.setConfigurazioneIstruttorie(this.annoCampagna, this.confIstruttoria);
    console.log(this.date);
    this.confIstruttoriaService.setConfigurazioneIstruttoriaRicevibilita(this.annoCampagna, this.date)
      .subscribe(
        x => {
          this.date = x;
          //this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        },
        error => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.salvataggioDatiKo));
        }
      );
    this.messageService.clear('checkSalvataggio');
  }

  private setConfigurazioneIstruttorie(annoCampagna: number, configurazioneIstruttorie: ConfigurazioneIstruttorie) {
    this.confIstruttoriaService.setConfigurazioneIstruttorie(annoCampagna, configurazioneIstruttorie)
      .subscribe(
        result => {
          this.confIstruttoria = result;
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        },
        error => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.salvataggioDatiKo));
        }
      )
  }

  public rejectSalvataggio() {
    this.messageService.clear('checkSalvataggio');
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
    this.countDomandeProtocollate(StatoDomandaEnum.PROTOCOLLATA);
    this.countDomandePerStato(StatoDomandaEnum.RICEVIBILE);
    this.countDomandePerStato(StatoDomandaEnum.NON_RICEVIBILE);
    this.countDomandePerStato(StatoDomandaEnum.IN_ISTRUTTORIA);
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

  countDomandePerStato(stato: string) {
    let filtro: string = 'statoDomanda=' + stato + '&annoCampagna=' + this.annoCampagna;
    this.istruttoriaService.countDomandeStatoPAC(filtro).subscribe(domandeDUCounResult => {
      this.addDomandePerStato(stato, domandeDUCounResult)
    });
  }

  countDomandeProtocollate(stato: string) {
    let filtro: string = 'annoRiferimento=' + this.annoCampagna;
    this.confIstruttoriaService.countDomandeDUPAC(filtro).subscribe(domandeDUCounResult => {
      this.addDomandePerStato(stato, domandeDUCounResult)
    });
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
    // processo.idStatoProcesso = 'PROCESSO_START';
    // processo.idDatiSettore = this.istruttoriaCorrente.istruttoria.id;
    // console.log('Dettaglio processo' + JSON.stringify(processo, this.replacerProcessoJson));
    // this.istruttoriaService.avviaProcessoIstruttoriaDU(JSON.stringify(processo, this.replacerProcessoJson)).subscribe((next) => {
    //   if (next > 0 && next != null) {
    //     processo.idStatoProcesso = 'PROCESSO_RUN';
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
    // this.router.navigate(['./' + Costanti.elencoDomande], { relativeTo: this.route, queryParams: { stato: stato } });
  }

  refresh(): void {
    window.location.reload();
  }

  handleError(error: any) {
    console.log("Error " + error + " msg " + error.error + " error.code " + error.status);
  }

}
