import { TipoDetenzioneEnum } from './../models/FascicoloCuaa';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { MenuItem, MessageService } from 'primeng-lts';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, switchMap, takeUntil } from 'rxjs/operators';
import { EMPTY, forkJoin, Subject, timer } from 'rxjs';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { CodiceMisureIntervento } from '../models/codiceMisureInterventoEnum';
import { MandatoService } from 'src/app/shared/services/mandato.service';
import { AnagraficaFascicoloService } from 'src/app/shared/services/anagrafica-fascicolo.service';
import { DetenzioneDto, FascicoloDaCuaa, MandatoDto, StatoFascicoloEnum } from '../models/FascicoloCuaa';
import * as moment from 'moment';
import { FascicoloDettaglioService } from 'src/app/shared/services/fascicolo-dettaglio.service';
import { TranslateService } from '@ngx-translate/core';
import { Utente } from '../../register/utente.model';
import { AuthService } from 'src/app/shared/services/auth.service';
import * as FileSaver from 'file-saver';
import { PersonaFisicaConCaricaDto } from 'src/app/shared/models/persona';

@Component({
  selector: 'app-riepilogo-fascicolo-dettaglio',
  templateUrl: './riepilogo-fascicolo-dettaglio.component.html',
  styleUrls: ['./riepilogo-fascicolo-dettaglio.component.css']
})
export class RiepilogoFascicoloDettaglioComponent implements OnInit, OnDestroy {

  public numVisible = 3;
  public popupSchedaValidazioneOpen = false;
  public popupSchedaValidazioneInProprioOpen = false;
  public popupSelezioneFirmatarioOpen = false;
  public responsiveOptions;
  public mandatoWarning: string;
  public fascicoloCorrente: FascicoloDaCuaa = new FascicoloDaCuaa();
  public firmatario: PersonaFisicaConCaricaDto;
  public selectedFirmatario: PersonaFisicaConCaricaDto;
  public isPopupControlloCompletezzaOpen = false;
  public statoFascicoloEnum = StatoFascicoloEnum;
  public idValidazione: number = undefined;
  public activeState: boolean = true;

  // private cuaaFromUrl = '';
  private cuaa = '';
  private componentDestroyed$: Subject<boolean> = new Subject();
  private detenzioneCorrente: DetenzioneDto;
  private utenteCorrente: Utente;
  public activeItem: MenuItem;

  private readonly reloadTimeout = 5000;

  constructor(
    public anagraficaFascicoloService: AnagraficaFascicoloService,
    public mandatoService: MandatoService,
    private messageService: MessageService,
    protected route: ActivatedRoute,
    private router: Router,
    private fascicoloDettaglioService: FascicoloDettaglioService,
    private translateService: TranslateService,
    private authService: AuthService
  ) {
    this.responsiveOptions = [
      {
        breakpoint: '1024px',
        numVisible: 3,
        numScroll: 3
      },
      {
        breakpoint: '768px',
        numVisible: 2,
        numScroll: 2
      },
      {
        breakpoint: '560px',
        numVisible: 1,
        numScroll: 1
      }
    ];
  }

  private refreshDataFascicolo() {
    this.fascicoloDettaglioService.fascicoloCorrente.next(this.fascicoloCorrente);
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  private loadDataFascicolo() {
    return this.anagraficaFascicoloService.getFascicoloDaCuaa(this.cuaa, this.idValidazione).pipe(
      catchError(e => {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDatiFascicolo));
        return EMPTY;
      }),
      switchMap(respFascicoloDaCuaa => {
        if (respFascicoloDaCuaa) {
          this.fascicoloCorrente = respFascicoloDaCuaa;
          this.fascicoloCorrente.detenzioneDto = this.detenzioneCorrente;
          this.refreshDataFascicolo();
          return this.authService.getUser(false);
        } else {
          return EMPTY;
        }
      })
    );
  }

  ngOnInit() {
    this.subscribeStatoFascicolo();
    this.route.params.pipe(
      takeUntil(this.componentDestroyed$),
      switchMap(params => {
        this.cuaa = params.cuaa;
        this.mandatoWarning = null;
        const paramIdVal: string = params['id-validazione'];
        if (paramIdVal) {
          this.idValidazione = Number.parseInt(paramIdVal, 10);
        } else {
          this.idValidazione = 0;
        }
        return this.mandatoService.getDetenzione(this.cuaa);
      }),
      catchError(e => {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDatiMandato));
        return EMPTY;
      }),
      switchMap(respDetenzione => {
        this.setDetenzione(respDetenzione);
        return this.loadDataFascicolo();
      }),
      switchMap(respAuthService => {
        if (respAuthService) {
          this.utenteCorrente = respAuthService;
        }
        return this.anagraficaFascicoloService.getFirmatario(this.cuaa);
      })
    ).subscribe((firmatario: PersonaFisicaConCaricaDto) => {
      this.firmatario = firmatario;
    });
  }

  private subscribeStatoFascicolo() {
    this.route.queryParams.subscribe(queryParams => {
      this.idValidazione = queryParams['id-validazione'] ? queryParams['id-validazione'] : 0;
    });

    this.fascicoloDettaglioService.fascicoloCorrente.pipe(
      takeUntil(this.componentDestroyed$))
      .subscribe((fascicoloCorrente: FascicoloDaCuaa) => {
        this.fascicoloCorrente = fascicoloCorrente;
        // TODO refactoring necessario: chiamate che si ripertono in OnInit
        this.mandatoService.getDetenzione(this.fascicoloCorrente.cuaa)
          .subscribe(resp => {
            this.setDetenzione(resp);
            this.loadDataFascicolo();
            this.fascicoloCorrente.detenzioneDto = this.detenzioneCorrente;
          });
      });
  }

  private setDetenzione(resp: DetenzioneDto) {
    const dataInizio = moment(resp.dataInizio, 'YYYY-MM-DD');
    const dataFine = moment(resp.dataFine, 'YYYY-MM-DD');
    const year = dataInizio.format('YYYY');
    const currentYear = moment().format('YYYY');
    // detenzione corrente
    if (dataFine.isValid()) {
      // nel caso in cui sia valorizzata la dataFine controllo che oggi è compreso tra dataInizio e dataFine
      if (moment(new Date()).isSameOrBefore(dataFine) && moment(new Date()).isSameOrAfter(dataInizio)) {
        this.detenzioneCorrente = resp;
      }
    } else { // dataFine = null -> mandato in corso
      if (moment(new Date()).isSameOrAfter(dataInizio)) {
        this.detenzioneCorrente = resp;
      }
    }
  }

  private handleServiceError(err: any) {
    let messaggioErrore: string;
    if (err.error.message) {
      messaggioErrore = err.error.message;
    } else {
      messaggioErrore = A4gMessages.salvataggioDatiKo;
    }
    this.messageService.add(A4gMessages.getToast(
      'tst', A4gSeverityMessage.error, messaggioErrore));
  }

  moveAllaFirma() {
    this.anagraficaFascicoloService.putStatoAllaFirmaAziendaFascicolo(this.cuaa).pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(
      res => {
        this.fascicoloCorrente.stato = StatoFascicoloEnum.ALLA_FIRMA_AZIENDA;
        this.refreshDataFascicolo();
      },
      err => this.handleServiceError(err)
    );
  }

  public moveInAggiornamento() {
    this.messageService.add(A4gMessages.getToast('checkAggiornamento', A4gSeverityMessage.warn, A4gMessages.RESPINGI_CONTROLLO));
    /*this.anagraficaFascicoloService.putSetStatusFascicoloInAggiornamento(this.cuaa).pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(
      res => {
        this.fascicoloCorrente.stato = StatoFascicoloEnum.IN_AGGIORNAMENTO;
        this.reloadDataFascicolo();
      },
      err => this.handleServiceError(err)
    );*/
  }

  public confirmAggiornamento() {
    this.messageService.clear('checkAggiornamento');
    this.setRappresentanteLegaleRespingiValidazione();
  }

  private setRappresentanteLegaleRespingiValidazione(): void {
    this.anagraficaFascicoloService.putSetRappresentanteLegaleRespingiValidazione(this.fascicoloCorrente.cuaa)
      .subscribe(
        res => {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.success, A4gMessages.changeStatusInAggiornamentoOk));
          this.messageService.clear('checkConfirm');
          this.fascicoloCorrente.stato = StatoFascicoloEnum.IN_AGGIORNAMENTO;
          this.refreshDataFascicolo();
        },
        err => {
          if (err.error.status === 403) {
            this.messageService.add(A4gMessages.getToast(
              'tst', A4gSeverityMessage.error, A4gMessages.operazioneKoMandatoNonValido));
          } else {
            this.messageService.add(A4gMessages.getToast(
              'tst', A4gSeverityMessage.error, A4gMessages.changeStatusInAggiornamentoKo));
          }
        });
  }

  public rejectAggiornamento() {
    this.messageService.clear('checkAggiornamento');
  }

  public handleClick(redirect: string) {
    const cuaa = this.fascicoloCorrente.cuaa;
    if (cuaa.length === 16 || cuaa.length === 11) {
      console.log('Redirect to dati azienda... link: ' + redirect);
      // this.fascicoloDettaglioService.mostraDettaglioSezione.next(FascicoloDettaglio.DATI_AZIENDA);
      this.router.navigate(['../' + redirect], { relativeTo: this.route });
    } else {
      this.messageService.add(A4gMessages.getToast(
        'tst', A4gSeverityMessage.error, 'WIP'));
    }
  }

  public isUtenteFirmatarioAzienda(): boolean {
    if (!this.firmatario || !this.utenteCorrente) {
      return false;
    }
    return this.firmatario.codiceFiscale === this.utenteCorrente.codiceFiscale;
  }

  public isFascicoloInAggiornamento(): boolean {
    return this.fascicoloCorrente
      && this.fascicoloCorrente.id
      && this.idValidazione === 0
      && (this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_AGGIORNAMENTO);
  }

  public fascicoloEditable(): boolean {
    return this.fascicoloCorrente
      && this.fascicoloCorrente.id
      && this.idValidazione === 0
      && (this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_AGGIORNAMENTO
        || this.fascicoloCorrente.stato === StatoFascicoloEnum.VALIDATO);
  }

  public canMoveInAggiornamentoOrInValidato(): boolean {
    return this.fascicoloCorrente
      && this.fascicoloCorrente.id
      && (this.fascicoloCorrente.stato === StatoFascicoloEnum.FIRMATO_CAA
        || this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_AZIENDA);
  }

  public canMoveInAllaFirmaAziendaOrValidato(): boolean {
    return this.fascicoloCorrente
      && this.fascicoloCorrente.id
      && (this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_AGGIORNAMENTO
        || this.fascicoloCorrente.stato === StatoFascicoloEnum.ALLA_FIRMA_AZIENDA);
  }

  public openPopupSchedaValidazione() {
    this.popupSchedaValidazioneOpen = true;
  }

  public closePopupSchedaValidazione() {
    this.popupSchedaValidazioneOpen = false;
  }

  public openPopupSchedaValidazioneInProprio() {
    this.popupSchedaValidazioneInProprioOpen = true;
  }

  public closePopupSchedaValidazioneInProprio() {
    this.popupSchedaValidazioneInProprioOpen = false;
  }

  public openPopupSelezioneFirmatarioOpen() {
    this.popupSelezioneFirmatarioOpen = true;
  }

  public closePopupSelezioneFirmatarioOpen(selectedFirmatario: PersonaFisicaConCaricaDto) {
    if (selectedFirmatario) {
      this.firmatario = selectedFirmatario;
    }
    this.popupSelezioneFirmatarioOpen = false;
  }

  public getSchedaValidazioneDetenzioneAutonoma() {
    this.anagraficaFascicoloService.getReportSchedaValidazioneDetenzioneAutonoma(this.fascicoloCorrente.cuaa).pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(scheda => {
      //salva scheda
      const file = new Blob([scheda], {});
      const filename = 'SchedaValidazioneDetenzioneAutonoma' + '_' + this.fascicoloCorrente.cuaa + '.pdf';
      FileSaver.saveAs(file, filename);
      //aggiorna informazioni fascicolo corrente
      this.fascicoloCorrente.stato = StatoFascicoloEnum.ALLA_FIRMA_AZIENDA;
      this.refreshDataFascicolo();
    },
      err => {
        if (err.status === 403) {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('HttpErrorMessages.403')));
        } else if (err.status === 400) {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('FAS_ANA.COMPLETEZZA_CONTROLLI_FALLITI')));
        } else if (err.status === 500) {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('FAS_ANA.SCHEDA_VALIDAZIONE_STAMPA_FALLITA')));
        } else {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, A4gMessages.ExportSchedaValidazioneKo));
        }
      }
    );
  }

  public getSchedaValidazione() {
    this.anagraficaFascicoloService.getReportSchedaValidazioneFirmata(this.fascicoloCorrente.cuaa).pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(scheda => {
      //salva scheda
      const file = new Blob([scheda], {});
      const filename = 'SchedaValidazione' + '_' + this.fascicoloCorrente.cuaa + '.pdf';
      FileSaver.saveAs(file, filename);
      //aggiorna informazioni fascicolo corrente
      this.fascicoloCorrente.stato = StatoFascicoloEnum.ALLA_FIRMA_AZIENDA;
      this.refreshDataFascicolo();
    }, err => {
      this.messageService.add(A4gMessages.getToast(
        'tst', A4gSeverityMessage.error, A4gMessages.ExportSchedaValidazioneKo));
    }
    );
  }

  public getSchedaValidazioneBozza() {
    this.anagraficaFascicoloService.getReportSchedaValidazioneBozzaDetenzioneAutonoma(this.fascicoloCorrente.cuaa)
      .subscribe(res => {
        const file = new Blob([res], {});
        const filename = 'SchedaValidazioneBozza' + '_' + this.fascicoloCorrente.cuaa + '.pdf';
        FileSaver.saveAs(file, filename);
      }, err => {
        if (err.status === 403) {
          this.messageService.add(A4gMessages.getToast(
            'tst',
            A4gSeverityMessage.error, this.translateService.instant('HttpErrorMessages.403')));
        } else {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, A4gMessages.ExportSchedaValidazioneKo));
        }
      }
      );
  }

  public putSchedaValidazione(event: string) {
    document.getElementById(event).click();
  }

  public schedaValidazioneChange(file) {
    this.anagraficaFascicoloService.putReportSchedaValidazione(file, this.fascicoloCorrente.cuaa).pipe(
      takeUntil(this.componentDestroyed$),
      catchError(err => {
        if (err.error.status === 403) {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, this.translateService.instant('FAS_ANA.VALIDAZIONE_CONSENTITA_RAPPRESENTATE_LEGALE')));
        } else if (err.error.status === 400) {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, this.translateService.instant('EXC_VERIFICA_FIRMA.FIRMA_NON_VALIDA')
          ));
        } else {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, A4gMessages.changeStatusValidazioneKo));
        }
        return EMPTY;
      }),
      switchMap(() => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.success, A4gMessages.changeStatusValidazioneOk));
        this.fascicoloCorrente.stato = StatoFascicoloEnum.IN_VALIDAZIONE;
        this.refreshDataFascicolo();
        this.popupSchedaValidazioneOpen = false;
        return timer(this.reloadTimeout);
      }),
      switchMap(() => this.loadDataFascicolo())
    ).subscribe();
  }

  public schedaValidazioneInProprioChange(file) {
    this.anagraficaFascicoloService.putReportSchedaValidazioneInProprio(file, this.fascicoloCorrente.cuaa).pipe(
      takeUntil(this.componentDestroyed$),
      catchError(err => {
        if (err.error.status === 403) {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, this.translateService.instant('FAS_ANA.VALIDAZIONE_CONSENTITA_RAPPRESENTATE_LEGALE')));
        } else if (err.error.status === 400) {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, this.translateService.instant('EXC_VERIFICA_FIRMA.FIRMA_NON_VALIDA')
          ));
        } else {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, A4gMessages.changeStatusValidazioneKo));
        }
        return EMPTY;
      }),
      switchMap(res => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.success, A4gMessages.changeStatusValidazioneOk));
        this.fascicoloCorrente.stato = StatoFascicoloEnum.IN_VALIDAZIONE;
        this.refreshDataFascicolo();
        this.popupSchedaValidazioneInProprioOpen = false;
        return timer(this.reloadTimeout);
      }),
      switchMap(() => this.loadDataFascicolo())
    ).subscribe();
  }

  public update() {
    if (this.cuaa) {
      this.anagraficaFascicoloService.aggiorna(this.cuaa).pipe(
        takeUntil(this.componentDestroyed$),
        catchError(error => {
          if (error.status === 403) {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('HttpErrorMessages.403')));
          } else {
            this.messageService.add(A4gMessages.getToast(
              'tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
          }
          return EMPTY;
        }),
      ).subscribe(res => {
        this.fascicoloCorrente = res.fascicoloDto;
        this.fascicoloCorrente.detenzioneDto = this.detenzioneCorrente;
        const anomalies = res.anomalies;
        this.refreshDataFascicolo();
        if (Array.isArray(anomalies) && anomalies.length) {
          for (const anomalia of anomalies) {
            let severityMessage = A4gSeverityMessage.warn;
            if (anomalia === "CUAA_MISMATCH_ANAGRAFE_TRIBUTARIA_CAA") {
              severityMessage = A4gSeverityMessage.info;
              this.router.navigate([this.router.url.replace(this.cuaa, this.fascicoloCorrente.cuaa)]);
            }
            this.messageService.add(A4gMessages.getToast(
              'tst', severityMessage,
              this.translateService.instant("FAS_ANA.CONTROLLI_COMPLETEZZA_ANOMALIE." + anomalia)));
          }
        }
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
      });
    }
  }

  public openPopupControlloCompletezza() {
    this.isPopupControlloCompletezzaOpen = true;
  }

  public closePopupControlloCompletezza(event: string) {
    this.isPopupControlloCompletezzaOpen = false;
  }

  public isMandato() {
    return this.fascicoloCorrente.detenzioneDto.tipoDetenzione === TipoDetenzioneEnum.MANDATO;
  }

  public isDetenzioneInProprio() {
    return this.fascicoloCorrente.detenzioneDto.tipoDetenzione === TipoDetenzioneEnum.DETENZIONE_IN_PROPRIO;
  }

  public goToFascicoliValidati() {
    this.router.navigate(
      ['./fascicoli-validati'], { relativeTo: this.route });
  }

  public tornaAlFascicoloAttuale() {
    this.idValidazione = 0;
    this.anagraficaFascicoloService.getFascicoloDaCuaa(this.cuaa, this.idValidazione)
      .subscribe(a => this.fascicoloDettaglioService.fascicoloCorrente.next(a));
    // devo ricaricare il dettaglio fascicolo
    this.router.navigate(
      ['./'], {
      relativeTo: this.route,
      queryParams: { 'id-validazione': this.idValidazione },
      queryParamsHandling: 'merge'
    });
  }

  public isFascicoloAttuale(): boolean {
    return this.fascicoloCorrente && Number(this.idValidazione) === 0;
  }

  interruttore() {
      this.activeState = !this.activeState;
  }

  public getIcon() {
    if (this.isFascicoloAttuale()) {
      return "pi pi-pencil";
    }
    return "pi pi-eye";
  }

}
