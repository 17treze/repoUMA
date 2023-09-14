import { EredeDto } from './../creazione-fascicolo/dto/EredeDto';
import { FascicoloTerritorioService } from './../fascicolo.territorio.service';
import { FascicoloCreationResultDto, StatoFascicoloEnum } from './../creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import { catchError, switchMap, takeUntil } from 'rxjs/operators';
import { MandatoDto } from '../../a4g-common/classi/FascicoloCuaa';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { A4gCostanti } from 'src/app/a4g-common/a4g-costanti';
import { FascicoloDaCuaa } from 'src/app/a4g-common/classi/FascicoloCuaa';
import { forkJoin, EMPTY, Subject, of } from 'rxjs';
import { MenuItem, MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { FascicoloDettaglioService } from './fascicolo-dettaglio.service';
import { FascicoloDettaglio } from '../shared/fascicolo.model';
import { FascicoloLazio } from 'src/app/a4g-common/classi/FascicoloLazio';
import { FascicoloService } from '../fascicolo.service';
// import { MandatoService } from '../mandato.service';
// import { AnagraficaFascicoloService } from '../creazione-fascicolo/anagrafica-fascicolo.service';
import { TranslateService } from '@ngx-translate/core';
import { PersonaFisicaConCaricaDto } from '../creazione-fascicolo/dto/PersonaDto';
import * as  moment from 'moment';
import { HttpErrorResponse } from '@angular/common/http';
import { ConduzioneDto } from '../creazione-fascicolo/dto/ConduzioneDto';
import { MediatorService } from '../mediator.service';
import * as FileSaver from 'file-saver';
import { Firmatario } from '../creazione-fascicolo/dto/FirmatarioDto';
import { SospensioneFascicolo } from 'src/app/a4g-common/classi/Fascicolo';
import { ErrorService } from 'src/app/a4g-common/services/error.service';


@Component({
  selector: 'app-fascicolo-dettaglio-container',
  templateUrl: './fascicolo-dettaglio-container.component.html',
  styleUrls: ['./fascicolo-dettaglio-container.component.scss']
})
export class FascicoloDettaglioContainerComponent implements OnInit, OnDestroy {
  protected componentDestroyed$: Subject<boolean> = new Subject();
  private mandatoCorrente: MandatoDto;
  public title: string;
  public fascicoloCorrente: FascicoloDaCuaa = new FascicoloDaCuaa();
  public popupVisibile: boolean;
  public popupModificaFirmatarioOpen = false;
  public eredi: EredeDto[];
  public popupCompletaTrasferimentoDaAppagOpen = false;
  public popupCompletaTrasferimentoInAppagOpen = false;
  public cuaa: string;
  public mandatoWarning: string;
  public idValidazione: number = 0;
  public firmatario: PersonaFisicaConCaricaDto;
  public dataUltimoAggiornamento: Date;
  public dataUltimaValidazione: Date;
  public numeroSchedaValidazione: string;
  public conduzioneTerreniList: ConduzioneDto[];
  public activeState = true;
  public statoFascicoloEnum = StatoFascicoloEnum;
  public isPopupControlloCompletezzaOpen = false;
  public popupValidaOpen = false;
  public passaAStato = '';
  public popupConfermaCambioStato = false;
  private paramIdVal: string;
  public displayPopupSospensioneFascicolo = false;
  public itemsDropdown: MenuItem[];
  public labelStatoFascicolo: string;
  public showItemDropdown = false;

  constructor(
    private fascicoloService: FascicoloService,
    private errorService: ErrorService,
    // private anagraficaFascicoloService: AnagraficaFascicoloService,
    // private mandatoService: MandatoService,
    private route: ActivatedRoute,
    private router: Router,
    private messageService: MessageService,
    private fascicoloDettaglioService: FascicoloDettaglioService,
    private translateService: TranslateService,
    private fascicoloTerritorioService: FascicoloTerritorioService,
    private mediatorService: MediatorService,
  ) { }

  private subscribeStatoFascicolo() {
    this.fascicoloDettaglioService.fascicoloCorrente.pipe(
      takeUntil(this.componentDestroyed$))
      .subscribe((fascicoloCorrente: FascicoloDaCuaa) => {
        if (fascicoloCorrente) {
          if (this.mandatoCorrente) {
            fascicoloCorrente.mandatoDto = this.mandatoCorrente;
          }
          this.fascicoloCorrente = fascicoloCorrente;
        }
      });
  }

  private subscribeInformazioniFirmatario() {
    this.fascicoloDettaglioService.firmatario.pipe(
      takeUntil(this.componentDestroyed$))
      .subscribe((firmatario: PersonaFisicaConCaricaDto) => {
        this.firmatario = firmatario;
      });
  }

  ngOnInit(): void {
    // this.mandatoWarning = null;
    this.route.params.pipe(
      takeUntil(this.componentDestroyed$),
      switchMap(params => {
        this.cuaa = params['cuaa'];
        return this.route.queryParams;
      })
    ).subscribe(queryParams => {
      this.paramIdVal = queryParams['id-validazione'];
      if (this.paramIdVal) {
        this.idValidazione = Number.parseInt(this.paramIdVal);
      } else {
        this.idValidazione = 0;
      }
      this.subscribeSezioneData(this.idValidazione);
      this.subscribeStatoFascicolo();
      this.subscribeInformazioniFirmatario();
      this.subscribeEredi();
      this.loadFirmatario(this.cuaa);
      this.loadFascicolo(this.cuaa, this.idValidazione);
      this.getEredi();
      // this.loadDates(this.cuaa);
      if (this.idValidazione !== 0) {
        this.route.snapshot.data[A4gCostanti.ROUTE_DATA_BREADCRUMB] = this.translateService.instant('FAS_ANA.SCHEDA_VALIDAZIONE');
      } else {
        this.route.snapshot.data[A4gCostanti.ROUTE_DATA_BREADCRUMB] =
          this.translateService.instant(this.route.snapshot.data[A4gCostanti.ROUTE_DATA_BREADCRUMB_TRANSLATE_KEY]);
      }
      this.fascicoloDettaglioService.mostraDettaglioSezione.next(FascicoloDettaglio.DATI);
    });
  }

  private loadFirmatario(cuaa: string) {
    // da riscrivere
    /*
    this.anagraficaFascicoloService.getFirmatario(cuaa).pipe(
      takeUntil(this.componentDestroyed$))
      .subscribe((firmatario: PersonaFisicaConCaricaDto) => {
        if (firmatario) {
          this.fascicoloDettaglioService.firmatario.next(firmatario);
        }
      });
    */
  }

  interruttore() {
    this.activeState = !this.activeState;
  }

  private subscribeSezioneData(idValidazione: number) {
    this.fascicoloDettaglioService.mostraDettaglioSezione.pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe((sezione: string) => {
      if (sezione === FascicoloDettaglio.DATI_AZIENDA) {
        this.router.navigate(
          ['./dati-azienda'], {
          relativeTo: this.route,
          queryParams: idValidazione ? { 'id-validazione': idValidazione } : {},
          queryParamsHandling: 'merge'
        });
      } else if (sezione === FascicoloDettaglio.MODALITA_PAGAMENTO) {
        this.router.navigate(
          ['./modalita-pagamento'], {
          relativeTo: this.route,
          queryParams: idValidazione ? { 'id-validazione': idValidazione } : {},
          queryParamsHandling: 'merge'
        });
      } else if (sezione === FascicoloDettaglio.LISTA_MACCHINARI) {
        this.router.navigate(
          ['./macchine'], {
          relativeTo: this.route,
          queryParams: idValidazione ? { 'id-validazione': idValidazione } : {},
          queryParamsHandling: 'merge'
        });
      } else if (sezione === FascicoloDettaglio.LISTA_FABBRICATI) {
        this.router.navigate(
          ['./fabbricati'], {
          relativeTo: this.route,
          queryParams: idValidazione ? { 'id-validazione': idValidazione } : {},
          queryParamsHandling: 'merge'
        });
      } else if (sezione === FascicoloDettaglio.FASCICOLI_VALIDATI) {
        this.router.navigate(
          ['./fascicoli-validati'],
          {
            relativeTo: this.route,
            queryParams: idValidazione ? { 'id-validazione': idValidazione } : {},
            queryParamsHandling: 'merge'
          });
      } else if (sezione === FascicoloDettaglio.DATI) {
        this.router.navigate(
          ['./'],
          {
            relativeTo: this.route,
            queryParams: idValidazione ? { 'id-validazione': idValidazione } : {},
            queryParamsHandling: 'merge'
          });
      } else if (sezione === FascicoloDettaglio.DATI_SOSPENSIONE) {
        this.router.navigate(
          ['./dati-sospensione'],
          {
            relativeTo: this.route,
            queryParams: idValidazione ? { 'id-validazione': idValidazione } : {},
            queryParamsHandling: 'merge'
          });
      }
    });
  }

  private subscribeEredi() {
    this.fascicoloDettaglioService.eredi.pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe((eredi: EredeDto[]) => {
        this.eredi = eredi;
    });
  }

  private loadFascicolo(cuaa: string, idValidazione: number): void {
    /*
    const mandatiByCuaa$ = this.mandatoService.getMandati(cuaa, idValidazione);
    const fascicoloByCuaa$ = this.anagraficaFascicoloService.getFascicolo(cuaa, idValidazione);
    forkJoin(mandatiByCuaa$, fascicoloByCuaa$).pipe(
      catchError(e => {
        if (e.status === 404) {
          this.messageService.add(A4gMessages.getToast('fdc-toast', A4gSeverityMessage.error,
            this.translateService.instant('NO_CONTENT')));
        } else {
          this.messageService.add(A4gMessages.getToast('fdc-toast', A4gSeverityMessage.error, e));
        }
        return EMPTY;
      })).subscribe((res: Array<Array<MandatoDto> | FascicoloDaCuaa>) => {
        // cerco tra i mandati, quello con revoca a partire dall'anno futuro
        if (res[0] && (res[0] as Array<MandatoDto>).length) {
          (res[0] as Array<MandatoDto>).forEach((mandatoDto: MandatoDto) => {
            const dataInizio = moment(mandatoDto.dataInizio, 'YYYY-MM-DD');
            const dataFine = moment(mandatoDto.dataFine, 'YYYY-MM-DD');
            const year = dataInizio.format('YYYY');
            const currentYear = moment().format('YYYY');
            if (parseInt(year, 10) > parseInt(currentYear, 10)) {
              if (dataInizio.format('DD/MM/YYYY').startsWith('01/01')) { // se esiste un mandato futuro
                this.mandatoWarning = 'Mandato revocato a partire dal ' + dataInizio.format('DD/MM/YYYY');
              }
            }
            // detenzione corrente
            if (dataFine.isValid()) {
              // nel caso in cui sia valorizzata la dataFine controllo che oggi è compreso tra dataInizio e dataFine
              if (moment(new Date()).isSameOrBefore(dataFine, 'day') && moment(new Date()).isSameOrAfter(dataInizio, 'day')) {
                this.mandatoCorrente = mandatoDto;
              }
            } else { // dataFine = null -> mandato in corso
              if (moment(new Date()).isSameOrAfter(dataInizio, 'day')) {
                this.mandatoCorrente = mandatoDto;
              }
            }
          });
        }
        if (res[1]) {
          this.fascicoloDettaglioService.fascicoloCorrente.next(res[1] as FascicoloDaCuaa);
          this.setItemDropdown();
        }
      });
    */
    this.fascicoloService.getFascicoloLazio(cuaa).subscribe((fascicolo: FascicoloLazio) => {
        if (fascicolo.data) {
          console.log('fascicolo.data: ' + JSON.stringify(fascicolo.data));
          // inizializzare FascicoloDaCuaa...
          let fascicoloDaCuaa = new FascicoloDaCuaa();
          fascicoloDaCuaa.cuaa = fascicolo.data.codiCuaa;
          fascicoloDaCuaa.denominazione = fascicolo.data.descDeno;
          fascicoloDaCuaa.dataApertura = this.dateFromString(fascicolo.data.dataAperFasc);
          fascicoloDaCuaa.dataModifica = this.dateFromString(fascicolo.data.dataElab);
          fascicoloDaCuaa.dataUltimaValidazione = this.dateFromString(fascicolo.data.dataScheVali);
          fascicoloDaCuaa.numeScheVali = fascicolo.data.numeScheVali;
          // ...
          this.fascicoloDettaglioService.fascicoloCorrente.next(fascicoloDaCuaa as FascicoloDaCuaa);
          this.setItemDropdown();
        }
        else {
          console.log('err11');
          this.errorService.showErrorWithMessage(fascicolo.text);
        }
      }, error => {
        console.log('err12');
        this.errorService.showError(error, 'tst-fas-ap');
      });
  }

  public backgroundStato(): string {
    if (this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_VALIDAZIONE) {
      return 'background-grey';
    }
    if (this.fascicoloCorrente.stato === StatoFascicoloEnum.VALIDATO) {
      return 'background-green';
    }
    return 'background-default';
  }

  public fascicoloEditable(): boolean {
    return this.isFascicoloAttuale() && (
      this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_AGGIORNAMENTO
      || this.fascicoloCorrente.stato === StatoFascicoloEnum.VALIDATO
      || this.fascicoloCorrente.stato === StatoFascicoloEnum.CONTROLLATO_OK);
  }

  public getFirmatarioEsteso(): string {
    return (this.firmatario !== null && typeof this.firmatario !== 'undefined' ? this.firmatario.codiceFiscale + ' - ' + this.firmatario.cognome + ' ' + this.firmatario.nome : '');
  }

  // G.De Vincentiis, 15 apr 2021
  public isFascicoloAttuale(): boolean {
    return this.fascicoloCorrente && this.idValidazione === 0;
  }

  public isFascicoloStatoIdoneoPerDownloadScheda(): boolean {
    return this.isFascicoloAttuale()
      && (this.fascicoloCorrente.stato === StatoFascicoloEnum.CONTROLLATO_OK
        || this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_CAA);
  }

  public isStatoFascicoloValidato(): boolean {
    return this.fascicoloCorrente.stato === StatoFascicoloEnum.VALIDATO;
  }

  public isDisabledDownloadScheda(): boolean {
    return this.isFascicoloAttuale()
      && (this.fascicoloCorrente.stato === StatoFascicoloEnum.FIRMATO_CAA
        || this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_AZIENDA
        || this.fascicoloCorrente.stato === StatoFascicoloEnum.VALIDATO
        || this.fascicoloCorrente.stato === StatoFascicoloEnum.CHIUSO);
  }

  public isFascicoloInAttesaTrasferimento(): boolean {
    return this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_ATTESA_TRASFERIMENTO;
  }

 
  public openPopupCompletaTrasferimentoDaAppag() {
    if (this.isStatoFascicoloControlliInCorso()) {
      this.messageService.add(A4gMessages.getToast(
        'fdc-toast', A4gSeverityMessage.warn,
        this.translateService.instant('FAS_ANA.CONTROLLI_COMPLETEZZA_KO.CONTROLLI_IN_CORSO')));
    } else {
      this.popupCompletaTrasferimentoDaAppagOpen = true;
    }
  }

  public closePopupCompletaTrasferimentoDaAppag(reload: boolean) {
    this.popupCompletaTrasferimentoDaAppagOpen = false;
    if (reload) {
      // ...
      this.loadFascicolo(this.cuaa, this.idValidazione);
    }
  }

  public openPopupCompletaTrasferimentoInAppag() {
    this.popupCompletaTrasferimentoInAppagOpen = true;
  }

  public closePopupCompletaTrasferimentoInAppag(reload: boolean) {
    this.popupCompletaTrasferimentoInAppagOpen = false;
    if (reload) {
      // ...
      this.loadFascicolo(this.cuaa, this.idValidazione);
    }
  }

  public salvaConduzioneTerreni() {
    this.fascicoloTerritorioService.putSalvaConduzioneTerreni(this.cuaa, this.conduzioneTerreniList)
      .subscribe(resp => {
        this.closePopupCompletaTrasferimentoInAppag(true);
        this.messageService.add(A4gMessages.getToast(
          'fdc-toast',
          A4gSeverityMessage.success,
          this.translateService.instant('FAS_ANA.SAVE_CONDURZIONE_TERRENI_OK')
        ));
      },
        err => {
          this.messageService.add(A4gMessages.getToast(
            'fdc-toast',
            A4gSeverityMessage.error,
            this.translateService.instant('FAS_ANA.SAVE_CONDURZIONE_TERRENI_KO')
          ));
        });
  }

  public tornaAlFascicoloAttuale() {
    this.router.navigate(
      ['./'], {
      relativeTo: this.route
    });
  }

  public openPopupModificaSede() {
    if (this.isStatoFascicoloControlliInCorso()) {
      this.messageService.add(A4gMessages.getToast(
        'fdc-toast', A4gSeverityMessage.warn,
        this.translateService.instant('FAS_ANA.CONTROLLI_COMPLETEZZA_KO.CONTROLLI_IN_CORSO')));
    } else {
      this.popupVisibile = !this.popupVisibile;
    }
  }

  public openPopupModificaFirmatario() {
    if (this.isStatoFascicoloControlliInCorso()) {
      this.messageService.add(A4gMessages.getToast(
        'fdc-toast', A4gSeverityMessage.warn,
        this.translateService.instant('FAS_ANA.CONTROLLI_COMPLETEZZA_KO.CONTROLLI_IN_CORSO')));
    } else {
      // gestire this.paramIdVal per l'apertura in sola visualizzazione ed apertura storico id validazione
      this.popupModificaFirmatarioOpen = true;
    }
  }

  public getMandato() {
    // da riscrivere
    /*
    if (!this.isFascicoloAttuale()) {
      if (this.paramIdVal) {
        // parametro per l'apertura storico id validazione
      }
    }
    this.mandatoService.getMandatoFile(this.fascicoloCorrente.id, this.fascicoloCorrente.mandatoDto.id, this.idValidazione).subscribe(
      result => {
        const mandatoFile: Blob = new Blob([result], { type: 'application/pdf' });
        if (mandatoFile != null) {
          const fileURL = URL.createObjectURL(mandatoFile);
          window.open(fileURL);
        } else {
          this.messageService.add(A4gMessages.getToast(
            'fdc-toast', A4gSeverityMessage.error, A4gMessages.erroreVisualizzazioneMandato));
        }
      }, err => {
        this.messageService.add(A4gMessages.getToast(
          'fdc-toast', A4gSeverityMessage.error, A4gMessages.erroreVisualizzazioneMandato));
      }
    );
    */
  }

  public goToFascicoliValidati() {
    this.fascicoloDettaglioService.mostraDettaglioSezione.next(FascicoloDettaglio.FASCICOLI_VALIDATI);
  }

  public goToDatiSospensione() {
    this.fascicoloDettaglioService.mostraDettaglioSezione.next(FascicoloDettaglio.DATI_SOSPENSIONE);
  }

  public closePopupModificaSede(reload: boolean) {
    this.popupVisibile = !this.popupVisibile;
    if (reload) {
      this.loadFascicolo(this.cuaa, this.idValidazione);
    }
  }

  public closePopupModificaFirmatario(reload: boolean) {
    this.popupModificaFirmatarioOpen = false;
    if (reload) {
      this.loadFirmatario(this.cuaa);
      this.loadFascicolo(this.cuaa, this.idValidazione);
    }
  }

  /*private loadDates(cuaa: string) {
    const sortBy: string = 'dataValidazione';
    const pagination: Paginazione = this.paginatorService.getPagination(
      0,
      999,
      sortBy,
      SortDirection.ASC
    );
    this.fascicoloService.getFascicoliValidati(cuaa, null, pagination).pipe(
        takeUntil(this.componentDestroyed$),
        catchError(e => {
          this.messageService.add(
            A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
          return EMPTY;
        })
      ).subscribe(res => {
        const resLen = res.risultati.length;
        if (res.risultati.length > 0) {
          //const maxIdValidazione: number = Math.max(...validazioniFascicoloList.map(e => e.idValidazione));
          //const maxIdValidazioneFascicoloDto: ValidazioneFascicoloDto = res.risultati.find(e => e.idValidazione === maxIdValidazione);
          this.dataUltimoAggiornamento = res.risultati[resLen-1].dataModifica;
          this.dataUltimaValidazione = res.risultati[1].dataValidazione;
        } else {
          this.dataUltimoAggiornamento = null;
          this.dataUltimaValidazione = null;
        }
      });
  }*/

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  public getSchedaValidazioneBozza() {
    this.mediatorService.getReportSchedaValidazioneBozza(this.fascicoloCorrente.cuaa)
      .pipe(
        takeUntil(this.componentDestroyed$),
        catchError((err: HttpErrorResponse) => {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, A4gMessages.ExportSchedaValidazioneKo));
          return EMPTY;
        })).subscribe(res => {
          const file = new Blob([res], {});
          const filename = 'SchedaValidazioneBozza' + '_' + this.fascicoloCorrente.cuaa + '.pdf';
          FileSaver.saveAs(file, filename);
        });
  }

  public openPopupControlloCompletezza() {
    this.isPopupControlloCompletezzaOpen = true;
  }

  public closePopupControlloCompletezza(event: string) {
    this.isPopupControlloCompletezzaOpen = false;
  }

  public getSchedaValidazione() {
    /*
    if (!this.isFascicoloAttuale() || this.isStatoFascicoloValidato()) {
      this.anagraficaFascicoloService.getReportSchedaValidazioneSnapshot(this.cuaa, this.idValidazione).pipe(
        takeUntil(this.componentDestroyed$),
        catchError(() => {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, A4gMessages.ExportSchedaValidazioneKo));
          return EMPTY;
        })
      ).subscribe(res => {
        const file = new Blob([res], {});
        const filename = 'SchedaValidazione' + '_' + this.fascicoloCorrente.cuaa + '_' + this.idValidazione + '.pdf';
        this.updateStatoFascicolo();
        FileSaver.saveAs(file, filename);
      });
    } else {
      this.mediatorService.getReportSchedaValidazione(this.fascicoloCorrente.cuaa)
        .pipe(
          takeUntil(this.componentDestroyed$),
          catchError((err: HttpErrorResponse) => {
            if (err.status === 500) {
              this.messageService.add(A4gMessages.getToast(
                'tst', A4gSeverityMessage.error,
                this.translateService.instant('FAS_ANA.CONTROLLI_COMPLETEZZA_KO.' + err.error.message)));
            } else {
              this.messageService.add(A4gMessages.getToast(
                'tst', A4gSeverityMessage.error, A4gMessages.ExportSchedaValidazioneKo));
            }
            return EMPTY;
          })).subscribe(res => {
            const file = new Blob([res], {});
            const filename = 'SchedaValidazione' + '_' + this.fascicoloCorrente.cuaa + '.pdf';
            FileSaver.saveAs(file, filename);
            if (this.fascicoloCorrente.stato === StatoFascicoloEnum.CONTROLLATO_OK) {
              this.fascicoloCorrente.stato = StatoFascicoloEnum.ALLA_FIRMA_CAA;
              this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.changeStatusAllaFirmaCaaOk));
            }
            this.updateStatoFascicolo();
          });
    }
    */
  }

  public isPopupPassaAEnabled() {
    return this.fascicoloCorrente
      && (this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_CAA);
  }

  public openPopupValida() {
    this.popupValidaOpen = true;
  }

  public closePopupValida() {
    this.popupValidaOpen = false;
  }

  public setValida() {
    this.passaAStato = StatoFascicoloEnum.VALIDATO;
  }

  public setFirma() {
    this.passaAStato = StatoFascicoloEnum.ALLA_FIRMA_AZIENDA;
  }

  public putSchedaValidazione(event: string) {
    document.getElementById(event).click();
  }

  public switchSchedaValidazioneFirma(file) {
    // this.closePopupValida();
    if (this.passaAStato === StatoFascicoloEnum.VALIDATO) {
      this.schedaValidazioneFirmaCaaAziendaChange(file);
    }
    if (this.passaAStato === StatoFascicoloEnum.ALLA_FIRMA_AZIENDA) {
      this.schedaValidazioneFirmaCaaChange(file);
    }
    console.log(this.passaAStato);
  }

  public schedaValidazioneFirmaCaaChange(file) {
    /*
    this.anagraficaFascicoloService.putReportSchedaValidazione(file, this.fascicoloCorrente.cuaa)
      .pipe(takeUntil(this.componentDestroyed$))
      .subscribe(
        res => {
          this.fascicoloCorrente.stato = StatoFascicoloEnum.FIRMATO_CAA;
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, this.translateService.instant('FAS_ANA.FASCICOLO_PASSATO_STATO_FIRMATO_CAA')));
          this.closePopupValida();
        }, err => {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, this.translateService.instant('EXC_VERIFICA_FIRMA.FIRMA_NON_VALIDA')
          ));
        });
    */
  }

  public schedaValidazioneFirmaCaaAziendaChange(file) {
    /*
    this.anagraficaFascicoloService.putReportSchedaValidazioneFirmaCaaAzienda(file, this.fascicoloCorrente.cuaa)
      .pipe(takeUntil(this.componentDestroyed$))
      .subscribe(
        res => {
          this.fascicoloCorrente.stato = StatoFascicoloEnum.IN_VALIDAZIONE;
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.success,
            this.translateService.instant('FAS_ANA.FASCICOLO_PASSATO_STATO_IN_VALIDAZIONE')));
          this.closePopupValida();
        }, err => {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, this.translateService.instant('EXC_VERIFICA_FIRMA.FIRMA_NON_VALIDA')
          ));
        });
    */
  }

  public changeStatusInAggiornamentoFascicolo() {
    if (this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_CAA
      || this.fascicoloCorrente.stato === StatoFascicoloEnum.FIRMATO_CAA) {
      this.messageService.add(
        A4gMessages.getToast('checkChangeStatus', A4gSeverityMessage.warn, A4gMessages.CONFERMA_FASCICOLO_IN_AGGIORNAMENTO)
      );
    }
  }

  public reloadDataFascicolo() {
    this.fascicoloDettaglioService.fascicoloCorrente.next(this.fascicoloCorrente);
    this.setItemDropdown();
  }

  public setAllaFirmaAzienda(cuaa: string) {
    /*
    this.anagraficaFascicoloService.putStatoAllaFirmaAziendaFascicolo(cuaa)
      .subscribe(
        () => {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
          this.fascicoloCorrente.stato = StatoFascicoloEnum.ALLA_FIRMA_AZIENDA;
          this.reloadDataFascicolo();
        }, err => {
          let messaggioErrore: string;
          if (err.error.message) {
            messaggioErrore = err.error.message;
          } else {
            messaggioErrore = A4gMessages.salvataggioDatiKo;
          }
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, messaggioErrore));
        });
    */
  }

  public setInAggiornamento(cuaa: string) {
    /*
    let service;
    if (this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_CAA
      || this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_AZIENDA
      || this.fascicoloCorrente.stato === StatoFascicoloEnum.FIRMATO_CAA) {
      service = this.mediatorService.annullaIterValidazione(cuaa);

    } else {
      service = this.anagraficaFascicoloService.putSetStatusFascicoloInAggiornamento(cuaa);
    }
    service.subscribe(
      () => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.success, A4gMessages.changeStatusInAggiornamentoOk));
        this.fascicoloCorrente.stato = StatoFascicoloEnum.IN_AGGIORNAMENTO;
        this.reloadDataFascicolo();
      }, err => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, A4gMessages.changeStatusInAggiornamentoKo));
      });
    */
  }

  public canMoveInAggiornamento(): boolean {
    return this.isFascicoloAttuale()
      && (this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_CAA
        || this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_AZIENDA
        || this.fascicoloCorrente.stato === StatoFascicoloEnum.FIRMATO_CAA
        || (this.fascicoloCorrente.stato === StatoFascicoloEnum.SOSPESO && this.isResponsabileFascicoloPat()));
  }

  public canMoveChiuso(): boolean {
    return this.isFascicoloAttuale() && !this.isFascicoloChiuso() && !this.isFascicoloSospeso() && !this.isFascicoloInValidazione() && !this.isStatoFascicoloControlliInCorso();
  }

  public canMoveSospeso(): boolean {
    return this.isFascicoloAttuale() && !this.isFascicoloChiuso() && !this.isFascicoloSospeso() && this.isResponsabileFascicoloPat() && !this.isFascicoloInValidazione();
  }

  public confirmInAggiornamento() {
    this.messageService.clear('checkChangeStatus');
    if (this.fascicoloCorrente.stato === StatoFascicoloEnum.FIRMATO_CAA
      || this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_CAA
      || this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_AZIENDA) {
      this.setInAggiornamento(this.fascicoloCorrente.cuaa);
    } else {
      this.messageService.add(A4gMessages.getToast('fdc-toast', A4gSeverityMessage.warn,
        this.translateService.instant('FAS_ANA.CAMBIA_STATO_IN_AGGIORNAMENTO')));
    }
  }

  public reject() {
    this.messageService.clear('checkChangeStatus');
  }

  public riportaAInAggiornamento() {
    if (this.fascicoloCorrente.stato === StatoFascicoloEnum.SOSPESO) {
      this.openPopupSospensioneFascicolo();
    } else {
      this.openPopupConfermaCambioStato();
    }
  }

  public openPopupConfermaCambioStato() {
    this.popupConfermaCambioStato = true;
  }

  public closePopupConfermaCambioStato(event: string) {
    this.popupConfermaCambioStato = false;
    if (event === 'SI') {
      if (this.fascicoloCorrente.stato === StatoFascicoloEnum.FIRMATO_CAA
        || this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_CAA
        || this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_AZIENDA) {
        this.setInAggiornamento(this.fascicoloCorrente.cuaa);
      }
    }
  }

  public updateStatoFascicolo() {
    /*
    this.anagraficaFascicoloService.getFascicoloNotCached(this.cuaa, this.idValidazione)
      .subscribe(res => {
        if (res) {
          this.fascicoloDettaglioService.fascicoloCorrente.next(res as FascicoloDaCuaa);
          this.setItemDropdown();
        }
      },
        e => {
          if (e.status === 404) {
            this.messageService.add(A4gMessages.getToast('fdc-toast', A4gSeverityMessage.error,
              this.translateService.instant('NO_CONTENT')));
          } else {
            this.messageService.add(A4gMessages.getToast('fdc-toast', A4gSeverityMessage.error, e));
          }
        });
    */
  }

  private isStatoFascicoloControlliInCorso() {
    return this.fascicoloCorrente.stato === StatoFascicoloEnum.CONTROLLI_IN_CORSO;
  }

  public chiudiFascicolo() {
    this.messageService.add(
      A4gMessages.getToast('chiudiFascicolo', A4gSeverityMessage.warn, this.translateService.instant('FAS_ANA.CONFERMA_CHIUSURA_FASCICOLO'))
    );
  }

  public confirmChiudiFascicolo() {
    this.messageService.clear('chiudiFascicolo');
    /*
    this.anagraficaFascicoloService.chiudiFascicolo(this.fascicoloCorrente.cuaa)
      .subscribe(
        res => {
          this.messageService.add(A4gMessages.getToast('fdc-toast', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
          this.loadFascicolo(this.cuaa, this.idValidazione);
        }, err => {
          this.messageService.add(A4gMessages.getToast('fdc-toast', A4gSeverityMessage.error, err.error.message));
        }
      );
    */
  }

  public rejectChiudiFascicolo() {
    this.messageService.clear('chiudiFascicolo');
  }

  public isFascicoloChiuso() {
    return this.fascicoloCorrente.stato === StatoFascicoloEnum.CHIUSO;
  }

  public isFascicoloSospeso() {
    return this.fascicoloCorrente.stato === StatoFascicoloEnum.SOSPESO;
  }

  public isFascicoloInValidazione() {
    return this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_VALIDAZIONE;
  }

  public isFascicoloInChiusura() {
    return this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_CHIUSURA;
  }

  public openPopupSospensioneFascicolo() {
    this.displayPopupSospensioneFascicolo = true;
  }

  public closePopupSospensioneFascicolo() {
    this.displayPopupSospensioneFascicolo = false;
  }

  public sospendiFascicolo(sospensioneFascicolo: SospensioneFascicolo) {
    /*
    let service;
    if (this.fascicoloCorrente.stato === StatoFascicoloEnum.SOSPESO) {
      service = this.anagraficaFascicoloService.rimuoviSospensioneFascicolo(this.fascicoloCorrente.cuaa, sospensioneFascicolo);
    } else {
      service = this.anagraficaFascicoloService.sospendiFascicolo(this.fascicoloCorrente.cuaa, sospensioneFascicolo);
    }
    service.subscribe(
      res => {
        this.messageService.add(A4gMessages.getToast('fdc-toast', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        this.closePopupSospensioneFascicolo();
        this.loadFascicolo(this.cuaa, this.idValidazione);
      }, err => {
        this.messageService.add(A4gMessages.getToast('fdc-toast', A4gSeverityMessage.error, err.error.message));
      }
    );
    */
  }

  public isResponsabileFascicoloPat() {
    return localStorage.getItem('selectedRole') === 'responsabile_fascicolo_pat';
  }

  public disableRunControlliCompletezza() {
    return (this.fascicoloCorrente.stato === this.statoFascicoloEnum.IN_ATTESA_TRASFERIMENTO)
      || !this.isFascicoloAttuale()
      || this.isFascicoloChiuso()
      || this.isFascicoloSospeso()
      || this.isResponsabileFascicoloPat();
  }

  public showDropdownChangeStatusFascicolo() {
    this.labelStatoFascicolo = this.translateService.instant('FAS_ANA.STATO.' + this.fascicoloCorrente.stato);
    return this.isFascicoloAttuale()
      && !this.isFascicoloChiuso()
      && !this.isFascicoloInValidazione()
      && !this.isStatoFascicoloControlliInCorso()
      && (!this.isFascicoloSospeso() || this.isResponsabileFascicoloPat());
  }

  public showItemDropdownStatus() {
    this.showItemDropdown = !this.showItemDropdown;
  }

  private setItemDropdown() {
    this.itemsDropdown = [
      {
        label: 'Riporta a "In aggiornamento"',
        visible: this.canMoveInAggiornamento(),
        command: () => { this.riportaAInAggiornamento(); this.showItemDropdownStatus(); },
        icon: 'pi pi-fw pi-refresh'
      },
      {
        label: 'Chiudi',
        visible: this.canMoveChiuso(),
        command: () => { this.chiudiFascicolo(); this.showItemDropdownStatus(); },
        icon: 'pi pi-fw pi-ban'
      },
      {
        label: 'Sospendi',
        visible: this.canMoveSospeso(),
        command: () => { this.openPopupSospensioneFascicolo(); this.showItemDropdownStatus(); },
        icon: 'pi pi-fw pi-pause'
      }
    ].filter(element => element.visible);
  }

  public eredeIsPresent(): boolean {
    if (this.eredi && this.eredi.length > 0) {
      return true;
    }
    return false;
  }

  private getEredi() {
    // Il SIAN mi pare non li restituisca
    /*
    this.anagraficaFascicoloService.getEredi(this.cuaa, this.idValidazione)
      .subscribe(res => {
        this.eredi = res;
        this.fascicoloDettaglioService.eredi.next(this.eredi);
      });
    */
  }

  public canGetMandato(): boolean {
    return !this.isFascicoloChiuso() && !this.isFascicoloSospeso();
  }

  public canModificaSede(): boolean {
    return this.isFascicoloAttuale() && !this.isFascicoloChiuso() && !this.isFascicoloSospeso() && !this.isFascicoloInChiusura();
  }

  public canModificaFirmatario(): boolean {
    return !this.isFascicoloChiuso() && !this.isFascicoloSospeso();
  }

  public canCompletaTrasferimentoInAppag(): boolean {
    return this.isFascicoloAttuale() && this.isFascicoloInAttesaTrasferimento() && !this.isFascicoloChiuso() && !this.isFascicoloSospeso() && !this.isFascicoloInChiusura();
  }

  public canCompletaTrasferimentoDaAppag(): boolean {
    return this.isFascicoloAttuale() && !this.isFascicoloInAttesaTrasferimento() && !this.isFascicoloChiuso() && !this.isFascicoloSospeso() && !this.isFascicoloInChiusura();
  }

  public canGetSchedaValidazioneBozza(): boolean {
    return this.isFascicoloAttuale() && !this.isFascicoloChiuso() && !this.isFascicoloSospeso();
  }
  
  dateFromString (dateStr: string): Date {
    return new Date(parseInt(dateStr.substring(0, 4)), 
    	parseInt(dateStr.substring(4, 6)) - 1, parseInt(dateStr.substring(6, 8)));
  }

}
