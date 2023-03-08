import { Component, OnInit, ViewEncapsulation, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MenuItem, SelectItem, MessageService, ConfirmationService } from 'primeng/api';
import { IstruttoriaService } from '../domandaUnica/istruttoria.service';
import { TipoProcesso } from '../../istruttoria-antimafia/dto/TipoProcesso';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { IstruttoriaDomandaUnicaFilter } from '../domandaUnica/classi/IstruttoriaDomandaUnicaFilter';
import { TranslateService } from '@ngx-translate/core';
import { TipoIstruttoriaEnum } from '../domandaUnica/classi/TipoIstruttoriaEnum';
import { SostegnoDu } from '../domandaUnica/classi/SostegnoDu';
import { takeUntil, catchError, switchMap, tap } from 'rxjs/operators';
import { Subject, EMPTY, Observable, iif, defer } from 'rxjs';
import * as FileSaver from "file-saver";
import { ProcessRepeatingServiceCall } from '../domandaUnica/sostegno-shared/repeating-service-call';
import { CONF_PROCESSI } from '../domandaUnica/sostegno-shared/conf-processi';
import { CodicePac } from '../domandaUnica/classi/CodicePac';

@Component({
  selector: 'app-sommario-istruttoria1420',
  templateUrl: './sommario-istruttoria1420.component.html',
  styleUrls: ['./sommario-istruttoria1420.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SommarioIstruttoria1420Component implements OnInit, OnDestroy {

  public popupDatiAggregatiSuperficieVisibile: boolean = false;
  public popupDatiAggregatiDisaccoppiatoVisibile: boolean = false;
  public popupDatiAggregatiVisibile: boolean = false;
  public popupCapiInterventoZootecniaVisibile: boolean = false;

  public tipoIstruttoria: TipoIstruttoriaEnum = undefined;
  public tipoSostegno: SostegnoDu = undefined;
  public istruttorie: any[];
  public istruttoria: any;

  public itemsCampagna: SelectItem[] = [];
  public itemsStatistiche: MenuItem[];
  public itemsSincronizzazione: MenuItem[];
  public itemsMenuDatiAggregati: MenuItem[];
  public msgText: string = "";
  public warnIco: string = "warn";
  
  // Da valorizzare nell'ng on init
  public numberBeforeButtonDsc: any = 0;
  public numberBeforeButtonDscAnt: any = 0;
  public numberBeforeButtonDscInt : any = 0;
  public numberBeforeButtonAcs: any = 0;
  public numberBeforeButtonAcz: any = 0;
  
  public annoCampagna: number = undefined;
  private numeroDomandeCount: number = 0;
  private componentDestroyed$: Subject<boolean> = new Subject();

  public processiDaControllare = CONF_PROCESSI.CRUSCOTTO;

  constructor(
    private istruttoriaService: IstruttoriaService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private translateService: TranslateService,
    private repeatingServiceCall: ProcessRepeatingServiceCall) {
      this.istruttorie = this.valoriAnniIstruttorie();
      this.istruttoria = [{
        anticipoDisaccoppiato: { sostegno: 'DISACCOPPIATO', tipo: 'ANTICIPO' },
        saldoDisaccoppiato: { sostegno: 'DISACCOPPIATO', tipo: 'SALDO' },
        integrazioneDisaccoppiato: { sostegno: 'DISACCOPPIATO', tipo: 'INTEGRAZIONE' },
        saldoAcs: { sostegno: 'SUPERFICIE', tipo: 'SALDO' },
        integrazioneAcs: { sostegno: 'SUPERFICIE', tipo: 'INTEGRAZIONE' },
        saldoAcz: { sostegno: 'ZOOTECNIA', tipo: 'SALDO' },
        integrazioneAcz: { sostegno: 'ZOOTECNIA', tipo: 'INTEGRAZIONE' }
      }];  
  }

  private manageAnniCampagna(anniCampagna: number[]) {
    anniCampagna.forEach(anno => {
      let menuItem: SelectItem = {
        label: `${anno}`, value: anno
      };
      this.itemsCampagna.push(menuItem);
    });
  }

  ngOnInit() {
    this.istruttoriaService.caricaAnniCampagna(CodicePac.PAC_2014_2020).pipe(
      switchMap(anniCampagna => {
        this.manageAnniCampagna(anniCampagna);
        return this.route.params;
      }),
      switchMap(params => {
        this.annoCampagna = Number(params['annoCampagna']);
        if (this.annoCampagna) {
          this.itemsSincronizzazione = this.valoriSincronizzazione(this.annoCampagna);
          this.itemsStatistiche = this.valoriStatistiche(this.annoCampagna);
          this.itemsMenuDatiAggregati = this.getItemsMenuDatiAggregati(this.annoCampagna);
          return this.aggiornaContatore(this.annoCampagna);
        } else {
          let annoDefRes = this.verificaAnnoDefault();
          if (annoDefRes.defaultAnnoCorrente) {
            this.annoCampagna = annoDefRes.annoAttuale;
          } else {
            this.annoCampagna = (annoDefRes.annoAttuale - 1);
          }
          this.router.navigate([`./${this.annoCampagna}`], { relativeTo: this.route });
        }
        return EMPTY;
      }),
      takeUntil(this.componentDestroyed$)
    ).subscribe(domandeDUCounResult => {
      this.manageAggiornaContatore(this.annoCampagna, domandeDUCounResult);
    });
  }

  public loadNumber(annoCampagna: number) {
    const annoCampagnaNum = annoCampagna;
    let filterDsc = new IstruttoriaDomandaUnicaFilter();
    filterDsc.campagna = annoCampagnaNum;
    filterDsc.sostegno = this.istruttoria[0].saldoDisaccoppiato.sostegno;
    filterDsc.tipo = this.istruttoria[0].saldoDisaccoppiato.tipo;
    let filterAcs = new IstruttoriaDomandaUnicaFilter();
    filterAcs.campagna = annoCampagnaNum;
    filterAcs.sostegno = this.istruttoria[0].saldoAcs.sostegno;
    filterAcs.tipo = this.istruttoria[0].saldoAcs.tipo;
    let filterAcz = new IstruttoriaDomandaUnicaFilter();
    filterAcz.campagna = annoCampagnaNum;
    filterAcz.sostegno = this.istruttoria[0].saldoAcz.sostegno;
    filterAcz.tipo = this.istruttoria[0].saldoAcz.tipo;
    this.istruttoriaService.countIstruttorieDU(filterDsc).subscribe(x => {
      this.numberBeforeButtonDsc = x;
    });
    // Calcolo con anticipo
    filterDsc.tipo = this.istruttoria[0].anticipoDisaccoppiato.tipo;
    this.istruttoriaService.countIstruttorieDU(filterDsc).subscribe(x => {
      this.numberBeforeButtonDscAnt = x;
    });
    // Calcolo con integrazione dsc
    filterDsc.tipo = this.istruttoria[0].integrazioneDisaccoppiato.tipo;
    this.istruttoriaService.countIstruttorieDU(filterDsc).subscribe(x => {
      this.numberBeforeButtonDscInt = x;
    });
    this.istruttoriaService.countIstruttorieDU(filterAcs).subscribe(x => {
      this.numberBeforeButtonAcs = x;
    });
    this.istruttoriaService.countIstruttorieDU(filterAcz).subscribe(x => {
      this.numberBeforeButtonAcz = x;
    });
  }

  public redirectCruscotto(istruttoria) {
    this.router.navigate(
      [`./${istruttoria.sostegno.toLowerCase()}/${istruttoria.tipo.toLowerCase()}`
      ], { relativeTo: this.route });
  }

  public apriConfigIstruttoria() {
    this.router.navigate([`./configurazione`], { relativeTo: this.route });
  }

  private valoriAnniIstruttorie(): any[] {
    return [
      {
        annoCampagna: 2018,
        anticipoDisaccoppiato: null,
        saldoDisaccoppiato: { campagna: 2018, sostegno: 'DISACCOPPIATO', tipo: 'SALDO' },
        integrazioneDisaccoppiato: { campagna: 2018, sostegno: 'DISACCOPPIATO', tipo: 'INTEGRAZIONE' },
        saldoAcs: { campagna: 2018, sostegno: 'SUPERFICIE', tipo: 'SALDO' },
        integrazioneAcs: { campagna: 2018, sostegno: 'SUPERFICIE', tipo: 'INTEGRAZIONE' },
        saldoAcz: { campagna: 2018, sostegno: 'ZOOTECNIA', tipo: 'SALDO' },
        integrazioneAcz: { campagna: 2018, sostegno: 'ZOOTECNIA', tipo: 'INTEGRAZIONE' }
      }, {
        annoCampagna: 2019,
        anticipoDisaccoppiato: { campagna: 2019, sostegno: 'DISACCOPPIATO', tipo: 'ANTICIPO' },
        saldoDisaccoppiato: { campagna: 2019, sostegno: 'DISACCOPPIATO', tipo: 'SALDO' },
        integrazioneDisaccoppiato: { campagna: 2019, sostegno: 'DISACCOPPIATO', tipo: 'INTEGRAZIONE' },
        saldoAcs: { campagna: 2019, sostegno: 'SUPERFICIE', tipo: 'SALDO' },
        integrazioneAcs: { campagna: 2019, sostegno: 'SUPERFICIE', tipo: 'INTEGRAZIONE' },
        saldoAcz: { campagna: 2019, sostegno: 'ZOOTECNIA', tipo: 'SALDO' },
        integrazioneAcz: { campagna: 2019, sostegno: 'ZOOTECNIA', tipo: 'INTEGRAZIONE' }
      }
    ];
  }
  
  private valoriStatistiche(annoCampagna: number): any[] {
    return [
      {
        label: 'STATISTICHE',
        items: [
          { label: "BPS", value: TipoProcesso.STATISTICHE_CS21,
          command: () => {
            this.calcoloStatisticheEsinc(annoCampagna, TipoProcesso.STATISTICHE_CS21, "STATISTICHE-BPS");
          } },
          { label: "Greening", value: TipoProcesso.STATISTICHE_CS22,
          command: () => {
            this.calcoloStatisticheEsinc(annoCampagna, TipoProcesso.STATISTICHE_CS22, "STATISTICHE-Greening");
          } },
          { label: "Giovane Agricoltore", value: TipoProcesso.STATISTICHE_CS25,
          command: () => {
            this.calcoloStatisticheEsinc(annoCampagna, TipoProcesso.STATISTICHE_CS25, "STATISTICHE-Giovane Agricoltore");
          } },
          { label: "Accoppiati", value: TipoProcesso.STATISTICHE_CS27,
          command: () => {
            this.calcoloStatisticheEsinc(annoCampagna, TipoProcesso.STATISTICHE_CS27, "STATISTICHE-Accoppiati");
          } }
        ]
      }
    ];
  }

  private valoriSincronizzazione(annoCampagna: number): any[] {
    return [
      {
        label: 'SINCRONIZZAZIONE',
        items: [
          { label: "SUP. ACCERTATE", value: TipoProcesso.SINCRONIZZAZIONE_SUPERFICI_ACCERTATE,
            command: () => {
              this.calcoloStatisticheEsinc(annoCampagna, TipoProcesso.SINCRONIZZAZIONE_SUPERFICI_ACCERTATE, "SINCRONIZZAZIONE-SUP. ACCERTATE");
            } 
          },
          { label: "PAGAMENTI", value: TipoProcesso.SINCRONIZZAZIONE_PAGAMENTI,
            command: () => { 
              this.calcoloStatisticheEsinc(annoCampagna, TipoProcesso.SINCRONIZZAZIONE_PAGAMENTI, "SINCRONIZZAZIONE-PAGAMENTI");
            } 
          }
        ]
      }
    ];
  }

  getItemsMenuDatiAggregati(annoCampagna: number): MenuItem[] {
    let itemsMenuDatiAggregati = [
      {
        label: this.translateService.instant("DATI_AGGREGATI"),
        items: [
          {
            label: this.translateService.instant("SOSTEGNO.DISACCOPPIATO"),
            //icon: 'pi pi-fw pi-plus',
            items: [
              {
                label: this.translateService.instant("ISTRUTTORIA_PAC.PAGAMENTI"),
                command: () => {
                  this.openPopupDatiAggregatiDisaccoppiato();
                }
              }
            ]
          }, {
            label: this.translateService.instant("SOSTEGNO.SUPERFICIE"),
            items: [
              {
                label: this.translateService.instant("ISTRUTTORIA_PAC.PAGAMENTI"),
                command: () => {
                  this.tipoSostegno = SostegnoDu.SUPERFICIE;
                  this.tipoIstruttoria = TipoIstruttoriaEnum.SALDO;
                  this.openPopupDatiAggregati();
                }
              }, {
                label: this.translateService.instant("ISTRUTTORIA_PAC.SUPERFICI_AMMISSIBILI"),
                command: () => {
                  this.openPopupDatiAggregatiSuperficie();
                }
              }
            ]
          }, {
            label: this.translateService.instant("SOSTEGNO.ZOOTECNIA"),
            items: [
              {
                label: this.translateService.instant("ISTRUTTORIA_PAC.PAGAMENTI"),
                command: () => {
                  this.tipoSostegno = SostegnoDu.ZOOTECNIA;
                  this.tipoIstruttoria = TipoIstruttoriaEnum.SALDO;
                  this.openPopupDatiAggregati();
                }
              }, {
                label: this.translateService.instant("ISTRUTTORIA_PAC.CAPI_RICHIESTI"),
                command: () => {
                  this.openPopupCapiInterventoZootecnia();
                }
              }, {
                label: this.translateService.instant("ISTRUTTORIA_PAC.ELENCO_CAPI_RICHIESTI"),
                command: () => {
                  this.istruttoriaService.getCapiImpegnatiPerAGEA(annoCampagna)
                    .pipe(
                      catchError(e => {
                        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
                        return EMPTY;
                      }),
                      takeUntil(this.componentDestroyed$)
                    ).subscribe(result => {
                      FileSaver.saveAs(result, "CAPI_ZOOTECNIA_APPAG.csv");
                  });
                }
              }
            ]
          }
        ]
      }
    ];
    return itemsMenuDatiAggregati;
  }

  calcoloStatisticheEsinc(annoCampagna: number, processo: TipoProcesso, selezione: string) {
    var listaPRocessiSinc: Array<string> = [TipoProcesso.SINCRONIZZAZIONE_SUPERFICI_ACCERTATE, TipoProcesso.SINCRONIZZAZIONE_PAGAMENTI];
    this.confirmationService.confirm({
      message: `Attenzione si sta per lanciare l\'elaborazione <br>${selezione}.<br> Questa operazione è irreversibile e sovrascriverà i dati precedenti.<br> Sei sicuro di voler procedere?`,
      header: 'Conferma',
      icon: 'pi pi-exclamation-triangle',
      accept: listaPRocessiSinc.includes(processo) 
        ? this.calcoloSincronizzazioneCallRestApi(processo, annoCampagna)
        : this.calcoloStatisticheCallRestApi(processo, annoCampagna),
      reject: () => {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.reject, A4gMessages.operazioneAnnullata));
    }
    });
  }


  private calcoloStatisticheCallRestApi(processo: TipoProcesso, annoCampagna: number): Function {
    return () => {
      let jsonInput: any = { "tipologiaStatistica": processo, "campagna": annoCampagna.toString() };

      const showMessage$ = defer(() => {
        if (processo == TipoProcesso.STATISTICHE_CS21) {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.inRunStatisticheBPS));
        }
        if (processo == TipoProcesso.STATISTICHE_CS22) {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.inRunStatisticheGreening));
        }
        if (processo == TipoProcesso.STATISTICHE_CS25) {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.inRunStatisticheGiovane));
        }
        if (processo == TipoProcesso.STATISTICHE_CS27) {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.inRunStatisticheAccoppiato));
        }
      });
      const avvia$ = this.istruttoriaService.avviaStatistiche(jsonInput).pipe(
        tap(val => {
          this.repeatingServiceCall.start();
          if (processo == TipoProcesso.STATISTICHE_CS21) {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.processoOkStatisticheBPS));
          }
          if (processo == TipoProcesso.STATISTICHE_CS22) {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.processoOkStatisticheGreening));
          }
          if (processo == TipoProcesso.STATISTICHE_CS25) {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.processoOkStatisticheGiovane));
          }
          if (processo == TipoProcesso.STATISTICHE_CS27) {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.processoOkStatisticheAccoppiato));
          }
          if (processo == TipoProcesso.SINCRONIZZAZIONE_SUPERFICI_ACCERTATE) {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.processoOkSincronizzazioneSuperficiAccertate));
          }
          if (processo == TipoProcesso.SINCRONIZZAZIONE_PAGAMENTI) {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.processoOkSincronizzazionePagamenti));
          }
        }));
      const checkTipiProcessoInEsecuzione=[processo];
      this.istruttoriaService.checkProcessiInEsecuzione(checkTipiProcessoInEsecuzione).pipe(
        takeUntil(this.componentDestroyed$),
        switchMap(inEsecuzione => iif(
          () => inEsecuzione, showMessage$, avvia$))).subscribe();
    };
  }

  private calcoloSincronizzazioneCallRestApi(processo: TipoProcesso, annoCampagna: number): Function {
    return () => {
      let jsonInput: any = { "tipologiaSincronizzazione": processo, "campagna": annoCampagna };
      const showMessage$ = defer(() => {
        if (processo == TipoProcesso.SINCRONIZZAZIONE_SUPERFICI_ACCERTATE) {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.inRunSincronizzazioneSuperficiAccertate));
        }
        if (processo == TipoProcesso.SINCRONIZZAZIONE_PAGAMENTI) {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.inRunSincronizzazionePagamenti));
        }
      });
      const avvia$ =       
        this.istruttoriaService.avviaSincronizzazione(jsonInput).pipe(
          tap(val => {
            this.repeatingServiceCall.start();
            if (processo == TipoProcesso.SINCRONIZZAZIONE_SUPERFICI_ACCERTATE) {
              this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.processoOkSincronizzazioneSuperficiAccertate));
            }
            if (processo == TipoProcesso.SINCRONIZZAZIONE_PAGAMENTI) {
              this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.processoOkSincronizzazionePagamenti));
            }
          }));
      const checkTipiProcessoInEsecuzione=[processo];
      this.istruttoriaService.checkProcessiInEsecuzione(checkTipiProcessoInEsecuzione).pipe(
        takeUntil(this.componentDestroyed$),
        switchMap(inEsecuzione => iif(
          () => inEsecuzione, showMessage$, avvia$))).subscribe();
    };
  }

  public manageCambioAnno(event: any) {
    this.router.navigate([`./${event.value}`], { relativeTo: this.route.parent });
  }

  private aggiornaContatore(annoCampagna: number): Observable<number> {
    let filtro: string = `annoRiferimento=${annoCampagna}`;
    return this.istruttoriaService.countDomandeDUPAC(filtro);
  }

  private manageAggiornaContatore(annoCampagna: number, domandeDUCounResult: number) {
    this.numeroDomandeCount = domandeDUCounResult;
    this.warnIco = "warn";
    this.msgText = "Ci sono " + this.numeroDomandeCount + " domande PROTOCOLLATE da ricevere";
    if (this.numeroDomandeCount <= 0) {
      this.warnIco = "";
      this.msgText = "";
    }
    if (this.numeroDomandeCount === 1 ) {
      this.msgText = "C'è 1 domanda PROTOCOLLATA da ricevere";
    }
    this.loadNumber(annoCampagna);
  }

  private verificaAnnoDefault() {
    let dataAttuale = new Date();
    let meseAttuale = dataAttuale.getMonth() + 1;
    let annoAttuale = dataAttuale.getFullYear();
    let defalutAnnoCorrente: boolean = false;
    if (meseAttuale > 9) {
      defalutAnnoCorrente = true;
    }
    return {'annoAttuale': annoAttuale, 'defaultAnnoCorrente': defalutAnnoCorrente}
  }

  public openPopupDatiAggregatiSuperficie() {
    this.popupDatiAggregatiSuperficieVisibile = true;
  }

  public closePopupDatiAggregatiSuperficie() {
    this.popupDatiAggregatiSuperficieVisibile = false;
  }

  public openPopupDatiAggregatiDisaccoppiato() {
    this.popupDatiAggregatiDisaccoppiatoVisibile = true;
  }
  
  public closePopupDatiAggregatiDisaccoppiato() {
    this.popupDatiAggregatiDisaccoppiatoVisibile = false;
  }

  public openPopupDatiAggregati() {
    this.popupDatiAggregatiVisibile = true;
  }

  public closePopupDatiAggregati() {
    this.popupDatiAggregatiVisibile = false;
  }

  public openPopupCapiInterventoZootecnia() {
    this.popupCapiInterventoZootecniaVisibile = true;
  }

  public closePopupCapiInterventoZootecnia() {
    this.popupCapiInterventoZootecniaVisibile = false;
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }
}
