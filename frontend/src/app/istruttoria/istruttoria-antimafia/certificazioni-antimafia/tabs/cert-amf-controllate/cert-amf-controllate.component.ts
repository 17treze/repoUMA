import { SelectItem } from 'primeng/api';
import { EsitiBdna } from './../../../dto/EsitiBdna';
import { SogliaAcquisizioneFilter } from './../../../dto/SogliaAcquisizioneFilter';
import { SogliaAcquisizione } from './../../../dto/SogliaAcquisizione';
import { Component, OnInit, ViewChild, EventEmitter, Output, Input, SimpleChanges, OnDestroy } from '@angular/core';
import { IstruttoriaAntimafiaService } from '../../../istruttoria-antimafia.service';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { MessageService, ConfirmationService } from "primeng/api";
import { Labels } from 'src/app/app.labels';
import { DichiarazioneAntimafia } from 'src/app/fascicolo/antimafia/classi/dichiarazioneAntimafia';
import { DichiarazioneAntimafiaConEsiti } from '../../../dto/DichiarazioneAntimafiaConEsiti';
import { Router, NavigationExtras, ActivatedRoute } from '@angular/router';
import * as FileSaver from "file-saver";
import { TipoDomandaEnum } from '../../../dto/TipoDomandaEnum';
import { SincronizzazioneDateBdnaDto } from 'src/app/a4g-common/classi/SincronizzazioneDateBdnaDto';
import { DomandaCollegata } from '../../../dto/DomandaCollegata';
import { StatoDichiarazione } from 'src/app/fascicolo/antimafia/classi/statoDichiarazioneEnum';
import { AntimafiaService } from 'src/app/fascicolo/antimafia/antimafia.service';
import { ActionMenuComponent } from 'src/app/a4g-common/action-menu.component';
import { PopupRifiutaDomandaComponent } from '../../../popup/popup-rifiuta-domanda/popup-rifiuta-domanda.component';
import { TrasmissioneBdna } from '../../../dto/TrasmissioneBdna';
import { StatoTrasmissioneEnum } from '../../../dto/StatoTrasmissioneEnum';
import { DatePipe } from '@angular/common';
import { RicercaAntimafiaFilter } from '../../../dto/RicercaAntimafiaFilter';
import { map, switchMap } from 'rxjs/operators';
import { of, Subscription } from 'rxjs';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { CodicePac } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/CodicePac';
import { CaricaCodiceFiscale } from 'src/app/fascicolo/antimafia/classi/datiDichiarazione';

@Component({
  selector: 'app-cert-amf-controllate',
  templateUrl: './cert-amf-controllate.component.html',
  styleUrls: ['./cert-amf-controllate.component.css', '../shared/esiti.css']
})
export class CertAmfControllateComponent implements OnInit, OnDestroy {
  cols: any[];
  tipoDomanda = TipoDomandaEnum;
  textTitle = Labels.istruttoriaCertificazioneAntimafia;
  isEmpty: boolean;
  intestazioni = Labels;
  certificazioneProtocollateSelezionate: DichiarazioneAntimafia[];
  certificazioniControllate: DichiarazioneAntimafiaConEsiti[];
  indexSelectedFromMenu: number;
  statoDaAggiornare: StatoDichiarazione;
  elementiPerPagina: number = 5;
  ricercaAntimafiaFilter: RicercaAntimafiaFilter = new RicercaAntimafiaFilter();
  elementiTotali = 0;
  isSelectedAll: boolean = false;
  dialogVisibile: boolean = false;
  selectedCampagna: string = "";
  campagne: number[];

  currentMenuAction: ActionMenuComponent;
  menuAction: any[] = new Array();
  domandaAntimafiaSelezionataFromMenuAction: DichiarazioneAntimafia;
  @Output() aggiornaCertificazioniCounters = new EventEmitter(); //EMETTI EVENTO quando serve
  @Output() runStatoAvanzamentoProcesso = new EventEmitter();
  @Input() refreshFromParent: boolean;

  // il parametro in binding accetta 'visible' oppure 'hidden'
  isDomandaStrutturaleVisibile: string;
  isDomandaSuperficieVisibile: string;
  isDomandaUnicaVisibile: string;
  sogliaAcquisizione: SogliaAcquisizioneFilter = new SogliaAcquisizioneFilter();
  typeImport: string;

  activeIndexDettaglioDomande: number[] = [-1, -1, -1];
  elencoCsvDaConfermare: SelectItem[] = [];
  trasmissioneBdnaSelected: TrasmissioneBdna;

  @ViewChild("file", { static: true }) file;
  @ViewChild(PopupRifiutaDomandaComponent, { static: true })
  private popupComponent: PopupRifiutaDomandaComponent;
  displayPopup: boolean;
  esitiBdna: EsitiBdna = new EsitiBdna();
  dichiarazioneAntimafiaSubscpription: Subscription;
  checkMassiveExport = false;

  constructor(private istruttoriaAntimafiaService: IstruttoriaAntimafiaService,
    private messages: MessageService, private router: Router,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private route: ActivatedRoute, private antimafiaService: AntimafiaService,
    private istruttoriaService: IstruttoriaService
  ) {
    // ordinamento sulle colonne
    this.cols = [
      {
        field: "datiDichiarazione.dettaglioImpresa.codiceFiscale",
        header: this.intestazioni.cuaaSigla
      },
      {
        field: "datiDichiarazione.dettaglioImpresa.denominazione",
        header: this.intestazioni.descrizioneImpresa
      },
      {
        field: "dtProtocollazione",
        header: this.intestazioni.data
      },
      {
        field: "idProtocollo",
        header: this.intestazioni.nrProtocollo
      },
      {
        field: "esito",
        header: this.intestazioni.esitoTrasmissione
      }
    ];
  }

  ngOnInit() {
    this.recuperaTrasmissioni();
    this.isEmpty = false;
    this.menuAction = this.buildMenuAction();
    this.recuperaAnniCampagna();
  }

  ngOnDestroy(): void {
    if (this.dichiarazioneAntimafiaSubscpription) {
      this.dichiarazioneAntimafiaSubscpription.unsubscribe();
    }
  }

  ngOnChanges(simpleChanges: SimpleChanges) {
    if (simpleChanges.refreshFromParent.currentValue) {
      this.loadList(0, null);
    }
  }

  recuperaAnniCampagna() {
    this.istruttoriaService.caricaAnniCampagna(CodicePac.PAC_2014_2020).toPromise().then(anniCampagna => {
      console.log("anni campagna", anniCampagna);
      this.campagne = anniCampagna;
    });
  }

  recuperaTrasmissioni() {
    // let trasmissione = new TrasmissioneBdna();
    // trasmissione.statoTrasmissione = StatoTrasmissioneEnum.NON_CONFERMATA;
    this.elencoCsvDaConfermare = [];
    //Recupero le trasmissioni non ancora confermate
    this.istruttoriaAntimafiaService.getTrasmissioniDaConfermare(null).subscribe((trasm: Array<TrasmissioneBdna>) => {
      //popola combobox
      if (trasm && trasm.length > 0) {
        trasm.forEach(trasm => {
          //COSTRUZIONE NOME FILE
          let datePipe: DatePipe = new DatePipe("it-IT");
          let date: string = datePipe.transform(trasm.dtCreazione, 'yyyyMMdd_HHmmss');
          let nomeFile = ""
          nomeFile = nomeFile.concat(trasm.cfOperatore).concat("_").concat("Flusso_");
          if (trasm.tipoDomanda == TipoDomandaEnum.DOMANDA_UNICA) {
            nomeFile = nomeFile.concat("DU_").concat(date);
          }
          if (trasm.tipoDomanda == TipoDomandaEnum.PSR_STRUTTURALI_EU) {
            nomeFile = nomeFile.concat("PSR_STR_").concat(date);
          }
          if (trasm.tipoDomanda == TipoDomandaEnum.PSR_SUPERFICIE_EU) {
            nomeFile = nomeFile.concat("PSR_SUP_").concat(date);
          }
          nomeFile = nomeFile.concat(".csv")
          //Inserimento in combobox
          this.elencoCsvDaConfermare.push({ label: nomeFile, value: trasm });
        })
      }
    }),
      err => {
        console.log(err);
      }
  }

  openDialog(isExport: boolean) {
    if (!this.certificazioneProtocollateSelezionate || this.certificazioneProtocollateSelezionate.length === 0) {
      this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.warn, A4gMessages.CTRLIAMCNT002));
      return;
    };
    this.checkMassiveExport = isExport;
    this.dialogVisibile = true;
  }

  annullaImportPsrSuperficie() {
    this.dialogVisibile = false;
    this.selectedCampagna = "";
  }

  confermaImportPsrSuperficie() {
    if (!this.selectedCampagna || this.selectedCampagna.length === 0) {
      return;
    }
    this.confirmationService.confirm({
      message: "Il processo di acquisizione delle domande richiede un tempo proporzionale al numero di domande selezionate. E' possibile visualizzare l'avanzamento nella barra dei processi.",
      header: 'ATTENZIONE',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.importSuperficieEU()
      },
      reject: () => {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.reject, A4gMessages.operazioneAnnullata));
      }
    });
    this.dialogVisibile = false;
  }

  // IMPORTS
  importSuperficieEU() {
    this.sogliaAcquisizione.settore = TipoDomandaEnum.PSR_SUPERFICIE_EU;
    if (!this.certificazioneProtocollateSelezionate || this.certificazioneProtocollateSelezionate.length === 0) {
      this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.warn, A4gMessages.CTRLIAMCNT002));
      return;
    }
    this.istruttoriaAntimafiaService.getSogliaAcquisizione(this.sogliaAcquisizione).subscribe((soglia: SogliaAcquisizione) => {
      of(this.isSelectedAll).pipe(
        switchMap(boolSelected => {
          if (boolSelected) {
            return this.istruttoriaAntimafiaService.getCertificazioniAntimafiaByStato(StatoDichiarazione.CONTROLLATA)
              .pipe(
                map(page =>
                  page.results.map(x => x.datiDichiarazione.dettaglioImpresa.codiceFiscale)
                )
              );
          }
          return of(this.certificazioneProtocollateSelezionate.map(x => x.datiDichiarazione.dettaglioImpresa.codiceFiscale));
        })
      ).subscribe(elencoCuaa => {
        this.istruttoriaAntimafiaService.importaDatiSuperficie(elencoCuaa, soglia, this.selectedCampagna).subscribe((domandeCollegate) => {
          this.runStatoAvanzamentoProcesso.emit(elencoCuaa.length);
          // Spostata lato BE perchè il servizio è stato reso asincrono
          //this.insertSincronizzazioneAgs(domandeCollegate);
          this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.success, A4gMessages.processoAvviatoOK));
        },
          err => {
            console.log(err);
            if ("IMPPSRSUPERR" === err.error.message) {
              this.messages.add(
                A4gMessages.getToast(
                  "generic",
                  A4gSeverityMessage.warn,
                  A4gMessages.IMPPSRSUPERR
                )
              );
              return;
            }
            if ("BRIAMPRT001" === err.error.message) {
              this.messages.add(
                A4gMessages.getToast(
                  "generic",
                  A4gSeverityMessage.warn,
                  A4gMessages.BRIAMPRT001
                )
              );
              return;
            }
            this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, A4gMessages.errorIstruttoriaAntimafiaImportDati));
          }, () => this.loadList(0, null));
      });

    },
      err => {
        console.log(err);
      })
  }

  importStrutturaliEU() {
    this.sogliaAcquisizione.settore = TipoDomandaEnum.PSR_STRUTTURALI_EU;
    if (!this.certificazioneProtocollateSelezionate || this.certificazioneProtocollateSelezionate.length === 0) {
      this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.warn, A4gMessages.CTRLIAMCNT002));
      return;
    }
    this.istruttoriaAntimafiaService.getSogliaAcquisizione(this.sogliaAcquisizione).subscribe((soglia: SogliaAcquisizione) => {
      of(this.isSelectedAll).pipe(
        switchMap(boolSelected => {
          if (boolSelected) {
            return this.istruttoriaAntimafiaService.getCertificazioniAntimafiaByStato(StatoDichiarazione.CONTROLLATA)
              .pipe(
                map(page =>
                  page.results.map(x => x.datiDichiarazione.dettaglioImpresa.codiceFiscale)
                )
              );
          }
          return of(this.certificazioneProtocollateSelezionate.map(x => x.datiDichiarazione.dettaglioImpresa.codiceFiscale));
        })
      ).subscribe(elencoCuaa => {
        this.istruttoriaAntimafiaService.importaDatiStrutturali(elencoCuaa, soglia).subscribe((domandeCollegate) => {
          this.insertSincronizzazioneAgs(domandeCollegate);
          this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        },
          err => {
            this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, A4gMessages.errorIstruttoriaAntimafiaImportDati));
          }, () => this.loadList(0, null));
      });

    },
      err => {
        console.log(err);
      })
  }

  importDomandaUnica(file: File) {
    this.sogliaAcquisizione.settore = TipoDomandaEnum.DOMANDA_UNICA;
    this.istruttoriaAntimafiaService.getSogliaAcquisizione(this.sogliaAcquisizione).subscribe((soglia: SogliaAcquisizione) => {
      this.istruttoriaAntimafiaService.importaDatiDU(file, soglia).subscribe((domandeCollegate) => {
        this.insertSincronizzazioneAgs(domandeCollegate);
        this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
      },
        err => {
          this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, err.error.message));
        }, () => this.loadList(0, null));
    },
      err => {
        console.log(err);
      })
  }

  confermaExportFileCSV() {
    if (!this.selectedCampagna || this.selectedCampagna.length === 0) {
      return;
    }
    // this.confirmationService.confirm({
    //   message: "Procedo all'export?",
    //   header: 'ATTENZIONE',
    //   icon: 'pi pi-exclamation-triangle',
    //   accept: () => {
    //     this.exportFileCSV(TipoDomandaEnum.PSR_SUPERFICIE_EU)
    //   },
    //   reject: () => {
    //     this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.reject, A4gMessages.operazioneAnnullata));
    //   }
    // });
    this.exportFileCSV(TipoDomandaEnum.PSR_SUPERFICIE_EU)
    this.dialogVisibile = false;
  }

  exportFileCSV(tipoDomanda: TipoDomandaEnum) {
    // se non ci sono nelle dichiarazioni parametri idDomande TipoDomanda not null ..  rreturn
    // altrimenti, prendimi tutte le domande siffatte e chiama il servizio con quella lista di cuaa
    if (!this.certificazioneProtocollateSelezionate || this.certificazioneProtocollateSelezionate.length === 0) {
      this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.warn, A4gMessages.CTRLIAMCNT002));
      return;
    }
    let domandaField;
    switch (tipoDomanda) {
      case TipoDomandaEnum.DOMANDA_UNICA:
        domandaField = "idDomandaDu";
        break;
      case TipoDomandaEnum.PSR_STRUTTURALI_EU:
        domandaField = "idDomandaStrutturale";
        break;
      case TipoDomandaEnum.PSR_SUPERFICIE_EU:
        domandaField = "idDomandaSuperficie";
    }
    const cuaa1 = this.certificazioneProtocollateSelezionate.map(c => {
      if (c[domandaField]) {
        return c.datiDichiarazione.dettaglioImpresa.codiceFiscale;
      }
    });
    // const cuaa = this.certificazioneProtocollateSelezionate.map(c => c.datiDichiarazione.dettaglioImpresa.codiceFiscale);
    of(this.isSelectedAll).pipe(
      switchMap(boolSelected => {
        if (boolSelected) {
          return this.istruttoriaAntimafiaService.getCertificazioniAntimafiaByStato(StatoDichiarazione.CONTROLLATA)
            .pipe(
              map(page =>
                page.results.map(x => x.datiDichiarazione.dettaglioImpresa.codiceFiscale)
              )
            );
        }
        return of(this.certificazioneProtocollateSelezionate.map(c => c.datiDichiarazione.dettaglioImpresa.codiceFiscale));
      })
    ).subscribe(elencoCuaa => {
      this.istruttoriaAntimafiaService.getEsportaCSV(elencoCuaa, tipoDomanda, this.selectedCampagna).subscribe(resp => {
        const fileName = this.getFileNameFromResponseContentDisposition(resp)
        FileSaver.saveAs(resp.body, fileName);
        this.recuperaTrasmissioni();
      }, err => {
        if (err.error) {
          //metodo utilizzato per leggere il ritorno della chiamatare rest che è un Blob
          const reader = new FileReader();
          reader.onload = () => {
            this.messages.add(A4gMessages.getToast("generic"
              , A4gSeverityMessage.error
              , A4gMessages.IMPOSSILE_GENERARE_FILE + ': ' + reader.result));
          };
          reader.readAsText(err.error);
        } else {
          this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, A4gMessages.IMPOSSILE_GENERARE_FILE));
        }
      });
    })
  }

  getFileNameFromResponseContentDisposition = (res: Response) => {
    const contentDisposition = res.headers.get('content-disposition') || '';
    const matches = /filename=([^;]+)/ig.exec(contentDisposition);
    const fileName = (matches[1] || 'untitled').trim();
    return fileName;
  };

  importaEsitiBDNA(file: File) {
    this.istruttoriaAntimafiaService.importaEsitiBdna(file).subscribe((esitiBdna: EsitiBdna) => {
      this.displayPopup = true;
      this.esitiBdna = esitiBdna;
    },
      err => {
        this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, err.error.message));
      }
    );
  }

  conferma() {
    if (!this.trasmissioneBdnaSelected) {
      return;
    }
    let trasmissioneDaAggiornare = new TrasmissioneBdna();
    trasmissioneDaAggiornare = this.trasmissioneBdnaSelected;
    trasmissioneDaAggiornare.dtConferma = new Date();
    this.istruttoriaAntimafiaService.confermaDomandeCollegate(trasmissioneDaAggiornare).subscribe(() => {
      this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
      this.recuperaTrasmissioni();
      this.trasmissioneBdnaSelected = null;
    }, err => {
      this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, err.error.message));
    });
  }
  annulla() {
    if (!this.trasmissioneBdnaSelected) {
      return;
    }
    this.istruttoriaAntimafiaService.annullaDomandeCollegate(this.trasmissioneBdnaSelected.id).subscribe(() => {
      this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
      this.recuperaTrasmissioni();
      this.trasmissioneBdnaSelected = null;
    }, err => {
      this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, err.error.message));
    });
  }

  scarica() {
    if (!this.trasmissioneBdnaSelected) {
      return;
    }
    this.istruttoriaAntimafiaService.downloadFileCsv(this.trasmissioneBdnaSelected.id).subscribe((resp) => {
      const fileName = this.getFileNameFromResponseContentDisposition(resp);
      FileSaver.saveAs(resp.body, fileName);
    }, err => {
      this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, err.error.message));
    });
  }

  // UPLOAD FILE CSV 
  uploadFileCsv(event) {
    if (event.target.files && event.target.files.length > 0) {
      let file: File = event.target.files[0];

      // controlla estensione del file
      if (file.name) {
        if (!file.name.endsWith(A4gMessages.UPLOAD_FILE_TYPE_CSV)) {
          this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, A4gMessages.FILE_TYPE(A4gMessages.UPLOAD_FILE_TYPE_CSV)));
          return;
        }
      } else {
        this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, A4gMessages.FILE_TYPE(A4gMessages.UPLOAD_FILE_TYPE_CSV)));
        return;
      }
      if (this.typeImport == 'du') {
        this.importDomandaUnica(file);
      }
      else if (this.typeImport == 'bdna') {
        this.importaEsitiBDNA(file);
      }
    }
  }

  openSOConsole(typeImport: string) {
    this.file.nativeElement.click();
    this.typeImport = typeImport;
  }

  visualizzaDettaglioDomanda(dichiarazione: DichiarazioneAntimafiaConEsiti, activeIndex: number) {

    let navigationExtras: NavigationExtras = {
      queryParams: {
        "activeIndex": activeIndex,
        "descAzienda": JSON.stringify(dichiarazione.datiDichiarazione.dettaglioImpresa.denominazione),
        "idDichiarazioneAntimafia": dichiarazione.id,
        "cuaa": JSON.stringify(dichiarazione.datiDichiarazione.dettaglioImpresa.codiceFiscale),
        "parentTabIndex": 2
      },
      relativeTo: this.route.parent
    };
    this.router.navigate(["./dettaglioDomandeCollegate"], navigationExtras);
  }


  private insertSincronizzazioneAgs(domandeCollegate: DomandaCollegata[]) {
    let body = new Array<SincronizzazioneDateBdnaDto>();
    if (!domandeCollegate) {
      return;
    }
    domandeCollegate.forEach(domanda => {
      let item: SincronizzazioneDateBdnaDto = {
        cuaa: domanda.cuaa,
        tipoDomanda: domanda.tipoDomanda,
        idDomanda: domanda.idDomanda,
        dtInizioEsitoNegativo: null,
        dtInizioSilenzioAssenso: null,
        dtFineEsitoNegativo: null,
        dtFineSilenzioAssenso: null
      }
      body.push(item);
    });

    if (body) {
      this.istruttoriaAntimafiaService.sincronizzaDateBDNAAntimafia(body);
    }
  }


  downloadDichiarazioneAntimafia() {
    const idDichiarazioneAntimafia = this.domandaAntimafiaSelezionataFromMenuAction.id.toString();
    const getDichiarazioneAntimafia$ = this.antimafiaService.getDichiarazioneAntimafiaWithPdfFirmato(idDichiarazioneAntimafia);

    this.dichiarazioneAntimafiaSubscpription = getDichiarazioneAntimafia$.pipe(
      switchMap((dichiarazione: DichiarazioneAntimafia) => {
        this.antimafiaService.downloadDichiarazioneAntimafia(dichiarazione.pdfFirmato, dichiarazione.tipoPdfFirmato);
        return of(dichiarazione);
      })
    ).subscribe((dichiarazione: DichiarazioneAntimafia) => {
      const cariche: Array<CaricaCodiceFiscale> = this.antimafiaService.getAllegati(dichiarazione);
      this.antimafiaService.downloadAllegati(cariche);
    });
  }

  onDropdownMenuOpen(
    actionMenu: ActionMenuComponent,
    certificazione: DichiarazioneAntimafia,
    indexSelectedFromMenu: number
  ) {
    this.indexSelectedFromMenu = indexSelectedFromMenu;
    this.domandaAntimafiaSelezionataFromMenuAction = certificazione;
    this.currentMenuAction = actionMenu;
    this.currentMenuAction.reset = false;
  }



  buildMenuAction(): Array<any> {
    return [
      {
        label: Labels.azioni.toUpperCase(),
        items: [
          {
            label: Labels.SCARICA,
            command: () => {
              this.downloadDichiarazioneAntimafia();
              this.currentMenuAction.reset = true;
            }
          },
          {
            label: Labels.RIFIUTA,
            command: () => {
              this.openPopup(StatoDichiarazione.RIFIUTATA);
              this.currentMenuAction.reset = true;
            }
          },
          {
            label: Labels.ESITO_POSITIVO,
            command: () => {
              this.openPopup(StatoDichiarazione.POSITIVO);
              this.currentMenuAction.reset = true;
            }
          }
        ]
      }
    ];
  }

  // POPOUP E CAMBIAMENTO DI STATO DOMANDA 
  openPopup(statoDichiarazione: StatoDichiarazione) {
    this.statoDaAggiornare = statoDichiarazione;
    this.messages.add(A4gMessages.getToast("aggiorna-stato", A4gSeverityMessage.warn, A4gMessages.WARNING_AGGIORNA_DOMANDA(statoDichiarazione)));
  }
  onConfirmAggiornaStato() {
    this.messages.clear();
    switch (this.statoDaAggiornare) {
      case StatoDichiarazione.RIFIUTATA:
        this.popupComponent.open();
        break;
      case StatoDichiarazione.POSITIVO:
        this.esitoPositivo();
        break;
    }
  }
  public onRejectAggiornaStato() {
    this.messages.clear();
  }

  esitoPositivo() {
    // cambia stato dichiarazione antimafia A4G
    let dichiarazione = this.domandaAntimafiaSelezionataFromMenuAction;
    dichiarazione.stato.descrizione = null;
    dichiarazione.stato.id = null;
    dichiarazione.stato.identificativo = StatoDichiarazione.POSITIVO;
    this.antimafiaService.aggiornaDichiarazioneAntimafia(dichiarazione).subscribe(dichiarazioneUpdate => {
      // recupera esiti antimafia ags
      let esitoAntimafiaExample: SincronizzazioneDateBdnaDto = new SincronizzazioneDateBdnaDto(this.domandaAntimafiaSelezionataFromMenuAction.datiDichiarazione.dettaglioImpresa.codiceFiscale);
      this.istruttoriaAntimafiaService.getEsitiAntimafiaAGS(esitoAntimafiaExample).subscribe(esiti => {
        // AM-04-03-02 PASSAGGIO DA TUTTI GLI STATI A ESITO POSITIVO - setta a null tutte le date
        if (esiti) {
          esiti.forEach(esito => {
            if (esito.cuaa === esitoAntimafiaExample.cuaa) {
              esito.dtFineEsitoNegativo = null;
              esito.dtFineSilenzioAssenso = null;
              esito.dtInizioEsitoNegativo = null;
              esito.dtInizioSilenzioAssenso = null;
            }
            // aggiorna i record in ags
            this.istruttoriaAntimafiaService.sincronizzaDateBDNAAntimafia(esiti);
          });
        }
        // aggiorna tabella e counter
        this.loadList(0, null);
      }, err => console.log(err));
    }, err => {
      // err put dichiarazione
      this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, A4gMessages.CTRLIAMCNT007_KO));
    });
  }

  exportEsitiBdna() {
    this.istruttoriaAntimafiaService.exportCsvEsitiBdna()
      .subscribe(
        resp => {
          const fileName = this.getFileNameFromResponseContentDisposition(resp);
          FileSaver.saveAs(resp.body, fileName);
          this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        },
        err => {
          console.log(err.error.error);
          this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, A4gMessages.ExportCsvKo));
        })
  };

  onC() { // chiudo dialog
    this.displayPopup = false;
  }

  // lazy loading - TO DO: L'ordinamento per esitoTrasmissione andrà fatto a backend
  loadData(event) {
    if (event.rows) {
      this.elementiPerPagina = event.rows;
    }
    if (event.sortField) {
      let parametroSort: string = "";
      switch (event.sortField) {
        case this.cols[0].field:
          parametroSort = "cuaa";
          break;
        case this.cols[1].field:
          parametroSort = "denominazioneImpresa";
          break;
        //Con il default lo fa giò, vedere cosa succede per gli altri filtri
        /* case this.cols[4].field:
          parametroSort = "esitoTrasmissione";
          break; */
        default:
          parametroSort = event.sortField;
          break;
      }
      const order = event.sortOrder === 1 ? "+" : "-";
      this.ricercaAntimafiaFilter.sortBy = order.concat(parametroSort);
    }
    this.loadList(event.first, event.globalFilter);
  }

  loadList(start: number, filter: string) {
    filter ? this.ricercaAntimafiaFilter.filtroGenerico = filter : this.ricercaAntimafiaFilter.filtroGenerico = null;
    this.ricercaAntimafiaFilter.statoDichiarazione = StatoDichiarazione.CONTROLLATA;
    this.ricercaAntimafiaFilter.pagSize = this.elementiPerPagina;
    this.ricercaAntimafiaFilter.pagStart = start;
    this.istruttoriaAntimafiaService
      .getCertificazioneAntimafia(this.ricercaAntimafiaFilter)
      .subscribe(
        next => {
          this.elementiTotali = next.total;
          this.certificazioniControllate = next.results ? next.results : [];
          this.certificazioniControllate.length === 0 ? this.isEmpty = true : this.isEmpty = false;
        }, err => {
          console.log("error: " + err);
          this.isEmpty = true;
        }
      );
  }

  private buildDescrizioneTooltip(cert: DichiarazioneAntimafiaConEsiti): string {
    let tooltipDescription = cert.esitoDescrizione;
    if (cert.esitoInvioAgea === 'OK' || cert.esitoInvioAgea === 'KO')
      // tooltipDescription += ' | ESITO INVIO AD AGEA ' + cert.esitoInvioAgea;
      tooltipDescription += ' | ESITO INVIO AD AGEA NON DISPONIBILE';
    else if (cert.esitoInvioAgea === 'ND')
      tooltipDescription += ' | ESITO INVIO AD AGEA NON DISPONIBILE';
    if (cert.esitoInvioBdna === 'OK' || cert.esitoInvioBdna === 'KO')
      tooltipDescription += ' | ESITO INVIO A BDNA ' + cert.esitoInvioBdna;
    else if (cert.esitoInvioBdna === 'ND')
      tooltipDescription += ' | ESITO INVIO A BDNA NON DISPONIBILE';
    return tooltipDescription;
  }
}
