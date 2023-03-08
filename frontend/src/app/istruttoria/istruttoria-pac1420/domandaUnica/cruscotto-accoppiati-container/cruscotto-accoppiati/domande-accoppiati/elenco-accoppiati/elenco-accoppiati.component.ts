import { Subscription, timer } from 'rxjs';
import { Labels } from 'src/app/app.labels';
import { Configuration } from '../../../../../../../app.constants';
import { HttpClient } from '@angular/common/http';
import { StatoProcesso } from '../../../../../../istruttoria-antimafia/dto/StatoProcesso';
import { TipoProcesso } from '../../../../../../istruttoria-antimafia/dto/TipoProcesso';
import { DatiDettaglio } from '../../../../classi/DatiDettaglio';
import { SostegnoDu } from '../../../../classi/SostegnoDu';
import { Component, OnInit, ViewChild, Output, Input, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Istruttoria } from 'src/app/a4g-common/classi/Istruttoria';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { PaginaDomande } from '../../../../domain/paginaDomande';
import { ProcessoIstruttoria } from '../../../../processoIstruttoria';
import { IstruttoriaService } from '../../../../istruttoria.service';
import { Domanda } from '../../../../domain/domandaUnica';
import { FiltroAccoppiatiService } from '../../../../filtro-accoppiati.service';
import { ElencoDomandeService } from '../../../../services/elenco-domande.service';
import { Costanti } from '../../../../Costanti';
import { FiltroRicercaDomande } from '../../../../domain/filtroRicercaDomande';
import { AutocompleteElencoDomandeParams } from '../../../../domain/autocomplete-elenco-domande';
import { DomandaIntegrativaService } from 'src/app/istruttoria/istruttoria-pac1420/domanda-integrativa/domanda-integrativa.service';
import * as FileSaver from "file-saver";
import { LoaderService } from 'src/app/loader.service';


@Component({
  selector: 'app-elenco-accoppiati',
  templateUrl: './elenco-accoppiati.component.html',
  styleUrls: ['./elenco-accoppiati.component.css']
})

export class ElencoAccoppiatiComponent implements OnInit, OnDestroy {

  dettaglioIstruttoria: Istruttoria;
  public subIstruttoria;
  idIstruttoria: number;
  cols: any[];
  parent: String;
  statoSostegno: string;
  paginaDomande: PaginaDomande;
  numeroPagina = 1;
  elementiPerPagina = 10;
  listaProcessiAttivi: Array<ProcessoIstruttoria>;
  disableAvviaCalcolo = false;
  disableOkCalcolo = false;
  valueProgressbar = 0;
  interval: any;
  suggestionsCUAA: string[];
  suggestionsDenominazione: string[];
  domande: Domanda[] = [];
  selectedSostegno: string;
  idTipoProcesso: string;
  showButtonCalcoloCapi: boolean = false;
  showButtonAvviaCalcolo: boolean = false;
  showButtonCalcoloCapiControllo: boolean = false;
  showButtonVisualizzaDatiAggregati: boolean = false;
  showButtonDownloadDatiCapiAgea: boolean = false;
  showButtonAvviaControlliLiquidazione: boolean = false;
  showColumnDI: boolean = false;
  showButtonCreaElencoLiquidazione: boolean = false;
  showElencoLiquidazione: boolean = false;

  idList: number[] = [];
  showNonAmmissibile: boolean = false;
  @ViewChild('table') table;
  @ViewChild('checkBox') Checkbox;

  processiInCorso: string = Labels.NESSUN_PROCESSO_DI_CONTROLLO;
  labels = Labels;
  timer: Subscription;
  timeout: number = 30000;

  @Input() popupVisibile: boolean;

  constructor(
    private istruttoriaService: IstruttoriaService,
    private route: ActivatedRoute,
    private router: Router,
    private filtroAccoppiati: FiltroAccoppiatiService,
    private elencoDomandeService: ElencoDomandeService,
    private messageService: MessageService,
    private domandaIntegrativaService: DomandaIntegrativaService,
    private loader: LoaderService,
    private http: HttpClient,
    private _configuration: Configuration) {
  }

  ngOnInit() {
    this.loader.setTimeout(480000); //otto minuti
    this.statoSostegno = this.route.routeConfig.path;
    this.Checkbox;
    this.table = [];
    this.setParentTab();
    this.setSostegno();
    this.setSubProcessi();
    this.setFiltroAccoppiati();
    this.refreshProcessi();
    this.autocompleteCuaa();

    if (this.statoSostegno === "PAGAMENTO_AUTORIZZATO") {
      this.cols = [
        { field: 'cuaaIntestatario', header: 'CUAA' },
        { field: 'numeroDomanda', header: 'Numero Domanda' },
        { field: 'ragioneSociale', header: 'Descrizione Impresa' },
        { field: null, header: 'Elenco Liquidazione' }
      ];
    } else {
      this.cols = [
        { field: 'cuaaIntestatario', header: 'CUAA' },
        { field: 'numeroDomanda', header: 'Numero Domanda' },
        { field: 'ragioneSociale', header: 'Descrizione Impresa' },

      ];
    }

    this.paginaDomande = new PaginaDomande();
  }

  setSostegno() {
    console.log('stato sostegno: ' + this.statoSostegno);
    if (this.router.url.split('/').filter(url => url === Costanti.cruscottoAccoppiatoZootecnia).length > 0) {
      this.selectedSostegno = SostegnoDu.ZOOTECNIA;
    } else {
      this.selectedSostegno = SostegnoDu.SUPERFICIE;
    }
    if (this.selectedSostegno == SostegnoDu.ZOOTECNIA) {
      this.idTipoProcesso = 'CLC_ACCOPPIATO_ZOOTECNIA';
      if (this.statoSostegno == 'RICHIESTO') {
        this.showButtonCalcoloCapi = true;
      } else if (this.statoSostegno == 'INTEGRATO') {
        this.showButtonCalcoloCapiControllo = true;
        this.showButtonVisualizzaDatiAggregati = true;
        this.showButtonDownloadDatiCapiAgea = true;
        this.showColumnDI = true;
        this.showButtonAvviaCalcolo = true;
      } else if (this.statoSostegno == 'NON_AMMISSIBILE') {
        this.showButtonCalcoloCapiControllo = true;
        this.showColumnDI = true;
      }
      else if (this.statoSostegno == 'CONTROLLI_CALCOLO_KO') {
        this.showButtonAvviaCalcolo = true;
        this.showNonAmmissibile = true;
      }
      else if (this.statoSostegno == 'CONTROLLI_CALCOLO_OK') {
        if (this.parent === 'controlliLiquidabilita') {
          this.showButtonAvviaControlliLiquidazione = true;
        } else {
          this.showButtonAvviaCalcolo = true;
        }
      } else if (this.statoSostegno == 'CONTROLLI_LIQUIDABILE_KO') {
        this.showButtonAvviaControlliLiquidazione = true;
      }
      else {
        this.showColumnDI = true;
      }

    }
    else { // 'ACC_SUPERFICI'
      this.idTipoProcesso = 'CLC_ACCOPPIATO_SUPERFICIE';
      // this.statoSostegno != 'NON_AMMISSIBILE'
      if (this.parent == 'controlliLiquidabilita') {
        this.idTipoProcesso = TipoProcesso.CTRL_LIQ_ACC_SUPERFICIE;
      }
      if (this.parent == 'liquidazione') {
        this.idTipoProcesso = TipoProcesso.CTRL_INTERSOSTEGNO_ACC_SUPERFICIE;
      }
      if (this.statoSostegno === 'RICHIESTO') {
        this.showButtonAvviaCalcolo = true;

      } else if (this.statoSostegno === 'CONTROLLI_CALCOLO_OK') {
        if (this.parent === 'controlliLiquidabilita') {
          this.showButtonAvviaControlliLiquidazione = true;
        } else {
          this.showButtonAvviaCalcolo = true;
        }
      } else if (this.statoSostegno == 'CONTROLLI_CALCOLO_KO') {
        this.showNonAmmissibile = true;
        this.showButtonAvviaCalcolo = true;
      } else if (this.statoSostegno == 'CONTROLLI_LIQUIDABILE_KO') {
        this.showButtonAvviaControlliLiquidazione = true;
      } else if (this.statoSostegno == 'LIQUIDABILE' && this.parent == 'liquidazione') {
        this.idTipoProcesso = TipoProcesso.CTRL_INTERSOSTEGNO_ACC_SUPERFICIE;
        this.showButtonCreaElencoLiquidazione = true;
      } else if (this.statoSostegno == 'CONTROLLI_INTERSOSTEGNO_OK' && this.parent == 'liquidazione') {
        this.showButtonCreaElencoLiquidazione = true;
      } else if (this.statoSostegno == 'PAGAMENTO_NON_AUTORIZZATO' && this.parent == 'liquidazione') {
        this.showButtonCreaElencoLiquidazione = true;
        // this.showElencoLiquidazione = true;
      } else if (this.statoSostegno == 'PAGAMENTO_AUTORIZZATO' && this.parent == 'liquidazione') {
        this.showElencoLiquidazione = true;
      }
    }
  }

  setSubProcessi() {
    this.subIstruttoria = this.route
      .params
      .subscribe(params => {
        this.idIstruttoria = params['idIstruttoria'];
        this.getIstruttoria(params['idIstruttoria']);
        this.getListaProcessiAttiviIstruttoria(params['idIstruttoria']);
      })
  }

  getIstruttoria(idIstruttoria: number): void {
    this.istruttoriaService.getIstruttoria(idIstruttoria)
      .subscribe((next) => {
        this.dettaglioIstruttoria = next;
        console.log('Istruttoria:');
        console.log(this.dettaglioIstruttoria);
      });
  }

  getListaProcessiAttiviIstruttoria(idIstruttoria: number): void {
    const requestListaProcessi = '{ "idIstruttoria": ' + idIstruttoria + ' }';
    this.istruttoriaService.getListaProcessiAttivi(encodeURIComponent(requestListaProcessi)).subscribe((next) => {
      this.listaProcessiAttivi = next;
      if (this.listaProcessiAttivi != null) {
        this.listaProcessiAttivi.map(p => {
          if (p.idTipoProcesso === this.idTipoProcesso) {
            this.disableAvviaCalcolo = true;
            this.valueProgressbar = p.percentualeAvanzamento;
            this.disableOkCalcolo = true;
          }
        });
      } else {
        this.listaProcessiAttivi = new Array<ProcessoIstruttoria>();
      }
    });
  }

  private setParentTab() {
    if (this.route.parent.snapshot.routeConfig.path === 'calcoloAccoppiatoZootecnia')
      this.parent = 'calcoloAccoppiatoZootecnia';
    if (this.route.parent.snapshot.routeConfig.path === 'calcoloAccoppiatoSuperficie')
      this.parent = 'calcoloAccoppiatoSuperficie';
    if (this.route.parent.snapshot.routeConfig.path === 'controlliLiquidabilita')
      this.parent = 'controlliLiquidabilita';
    if (this.route.parent.snapshot.routeConfig.path === 'liquidazione')
      this.parent = 'liquidazione';
  }

  private setFiltroAccoppiati() {
    this.filtroAccoppiati.elencoDomande = this;
    this.filtroAccoppiati.filtro = new FiltroRicercaDomande();
    this.filtroAccoppiati.filtro.statoSostegno = this.statoSostegno;
    this.filtroAccoppiati.filtro.statoDomanda = 'RICEVIBILE';
    this.filtroAccoppiati.filtro.sostegno = this.selectedSostegno;
    if (this.selectedSostegno == SostegnoDu.ZOOTECNIA)
      this.filtroAccoppiati.filtro.interventi = ["310", "311", "313", "315", "320", "321", "316", "318", "322"];
    else
      this.filtroAccoppiati.filtro.interventi = ["122", "123", "124", "125", "128", "129", "132", "138", "126"];
    this.filtroAccoppiati.filtro.idDatiSettore = this.idIstruttoria;
  }

  private refreshProcessi() {
    this.interval = setInterval(() => {
      if (this.listaProcessiAttivi != null && this.listaProcessiAttivi.length > 0) {
        console.log('Verifica avanzamento processo');
        this.listaProcessiAttivi.forEach(p => {
          this.istruttoriaService.getProcesso(p.idProcesso).subscribe((next) => {
            p.percentualeAvanzamento = next.percentualeAvanzamento;
            if (next.percentualeAvanzamento >= 100 || next.idStatoProcesso === 'PROCESSO_KO') {
              const index = this.listaProcessiAttivi.indexOf(p);
              if (index !== -1) {
                this.listaProcessiAttivi.splice(index, 1);
              }
              if (p.idTipoProcesso === this.idTipoProcesso) {
                this.disableOkCalcolo = false;
                this.disableAvviaCalcolo = false;
              }
            }
          });
        });
      }
    }, 10000);

    this.checkStatoAvanzamento();
  }

  private autocompleteCuaa() {
    const autoComplete = new AutocompleteElencoDomandeParams();
    autoComplete.sostegno = this.filtroAccoppiati.filtro.sostegno;
    autoComplete.statoDomanda = this.filtroAccoppiati.filtro.statoDomanda;
    autoComplete.statoSostegno = this.filtroAccoppiati.filtro.statoSostegno;
    this.elencoDomandeService.getSuggestionsAutocomplete(encodeURIComponent(JSON.stringify(autoComplete)))
      .subscribe((dati) => {
        this.suggestionsCUAA = [];
        this.suggestionsDenominazione = [];
        if (dati) {
          for (const el of dati) {
            this.suggestionsCUAA.push(el.cuaa);
            this.suggestionsDenominazione.push(el.denominazione);
          }
        }
      });
  }

  avviaCalcoloCapiConControllo() {
    this.idTipoProcesso = 'CLC_ACCOPPIATO_ZOOTECNIA';
    if (!this.domande || this.domande.length == 0) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloAccoppiatiWarningNumeroDomande));
      return;
    }

    //TASK ESEGUIRE CALCOLO CAPI FINO A DATA CHIUSURA DOMANDA INTEGRATIVA (INCLUSA) E SOLO SE AZIENDA NON HA GIA CONFERMATO DOMANDA INTEGRATIVA
    const domandePresentate = this.domande.filter(domanda => domanda.identificativoDI != null);
    if (domandePresentate != null && domandePresentate.length > 0) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloAccoppiatiWarningDomandePresentate));
      return;
    }

    if (this.statoSostegno == 'INTEGRATO' || this.statoSostegno == 'NON_AMMISSIBILE') {
      this.route.params.subscribe(params => {
        let sostegno: any = { identificativoSostegno: SostegnoDu.ZOOTECNIA };
        this.istruttoriaService.getDatiDettaglio(params['idIstruttoria'], sostegno).subscribe((datiDettaglio: DatiDettaglio) => {
          let sysdate = new Date();
          let dtChiusuraDomanda = new Date(datiDettaglio.sostegnoDuDi.dtChiusuraDomanda);
          if (sysdate <= dtChiusuraDomanda) {
            this.calcoloCapi();
          }
          else {
            let dtAperturaDomanda = new Date(datiDettaglio.sostegnoDuDi.dtAperturaDomanda);
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloFinestraTemporaleDomandaIntegrativa(dtAperturaDomanda.toLocaleDateString(), dtChiusuraDomanda.toLocaleDateString())));
            return;
          }
        });
      });
    }
  }

  avviaCalcoloCapi() {
    this.idTipoProcesso = 'CLC_ACCOPPIATO_ZOOTECNIA';
    if (!this.domande || this.domande.length == 0) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloAccoppiatiWarningNumeroDomande));
      return;
    }
    this.calcoloCapi();
  }

  calcoloCapi(): any {
    const idList: number[] = this.domande.map(
      domanda => {
        return domanda.id;
      }
    );

    if (this.Checkbox.checked) {
      const jsonParametri = JSON.stringify(this.filtroAccoppiati.filtro);
      const jsonPaginazione = '{ "numeroElementiPagina": ' + this.paginaDomande.elementiTotali + ', "pagina": ' + 0 + '}';
      const jsonOrdinamento = '';

      this.elencoDomandeService.getPaginaDomandeAttuale(encodeURIComponent(jsonParametri),
        encodeURIComponent(jsonPaginazione), encodeURIComponent(jsonOrdinamento)).subscribe(
          (dati) => {
            dati.risultati.forEach(result => {
              this.idList.push(result.id);
            })
            let jsonInput: any = { "campagna": this.dettaglioIstruttoria.annoRiferimento, "idsDomande": this.idList };
            console.log(jsonInput);
            this.istruttoriaService.avviaAccoppiatoZootecnia(jsonInput).subscribe(
              (v: void) => {
                //Check controllo stato avanzamento calcolo capi accoppiato zootecnia
                this.runStatoAvanzamentoProcesso(this.idList.length);
                this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, this.idList.length > 1 ? A4gMessages.calcoloAccoppiatoACZProcessoAvviato(this.domande.length) : A4gMessages.calcoloAccoppiatoACZProcessoAvviatoDomandaSingola))
              },
              (err) => {
                if (err.error.message == 'BRIAMPRT001')
                  this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloAccoppiatoACZProcessoControllo));
                else {
                  console.log(err.error.message);
                  this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.calcoloAccoppiatiErroreProcesso));
                }
              }
            );
            this.idList = [];
          })
    }
    else {
      let jsonInput: any = { "campagna": this.dettaglioIstruttoria.annoRiferimento, "idsDomande": idList };
      console.log(jsonInput);
      this.istruttoriaService.avviaAccoppiatoZootecnia(jsonInput).subscribe(
        (v: void) => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, idList.length > 1 ? A4gMessages.calcoloAccoppiatoACZProcessoAvviato(this.domande.length) : A4gMessages.calcoloAccoppiatoACZProcessoAvviatoDomandaSingola
          ));
          this.runStatoAvanzamentoProcesso(idList.length);
        },
        (err) => {
          if (err.error.message == 'BRIAMPRT001')
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloAccoppiatoACZProcessoControllo));
          else {
            console.log(err.error.message);
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.calcoloAccoppiatiErroreProcesso));
          }
        }
      );
    }
  }

  dettagliClick(domanda: Domanda) {
    this.router.navigate(['./' + Costanti.dettaglioDomanda.replace(':' + Costanti.dettaglioDomandaParam, domanda.id.toString())],
      { relativeTo: this.route.parent.parent });
  }

  loadData(event) {

    console.log('filtro:')
    console.log(this.filtroAccoppiati.filtro)

    const value = event.value;
    if (event != null && event.sortField != null) {
      console.log(event.sortField);
    }

    console.log('this.numeroPagina ' + this.numeroPagina + ' event.first ' + event.first +
      ' this.elementiPerPagina  ' + this.elementiPerPagina);

    this.numeroPagina = Math.floor(event.first / this.elementiPerPagina);
    const jsonParametri = JSON.stringify(this.filtroAccoppiati.filtro);
    const jsonPaginazione = '{ "numeroElementiPagina": ' + this.elementiPerPagina + ', "pagina": ' + this.numeroPagina + '}';
    let jsonOrdinamento = '';

    if (event != null && event.sortField != null) {
      jsonOrdinamento = '[{ "proprieta": "' + event.sortField + '", "ordine": "' + this.istruttoriaService.getOrdine(event.sortOrder) + '"}]';
    }

    this.elencoDomandeService.getPaginaDomandeAttuale(encodeURIComponent(jsonParametri),
      encodeURIComponent(jsonPaginazione), encodeURIComponent(jsonOrdinamento))
      .subscribe((dati => {
        if (dati != null) {
          this.paginaDomande = dati;
          console.log('this.paginaDomande');
          console.log(this.paginaDomande);
          if (this.Checkbox.checked)
            for (let elem of this.paginaDomande.risultati) {
              this.domande.push(elem);
            }
        } else {
          this.paginaDomande.risultati = [];
          this.paginaDomande.elementiTotali = 0;
        }
      }));

    this.table.first = 0;

  }

  avviaNonAmmissibilita() {
    const identificativoSostegnoDu = this.selectedSostegno;
    const statoSostegnoInput = 'NON_AMMISSIBILE';
    const request = encodeURIComponent(identificativoSostegnoDu);
    const request2 = encodeURIComponent(statoSostegnoInput);

    this.istruttoriaService.putAmmissibilita(this.domande, request, request2).subscribe(x => {
      this.refresh();
    });
  }

  refresh(): void {
    window.location.reload();
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

  avviaCalcoloPremioAccoppiati() {
    if (this.domande.length == 0) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloAccoppiatiWarningNumeroDomande));
      return;
    }
    const idList: number[] = this.domande.map(
      domanda => {
        return domanda.id;
      }
    );
    if (this.Checkbox.checked) {
      const jsonParametri = JSON.stringify(this.filtroAccoppiati.filtro);
      const jsonPaginazione = '{ "numeroElementiPagina": ' + this.paginaDomande.elementiTotali + ', "pagina": ' + 0 + '}';
      const jsonOrdinamento = '';
      this.elencoDomandeService.getPaginaDomandeAttuale(encodeURIComponent(jsonParametri),
        encodeURIComponent(jsonPaginazione), encodeURIComponent(jsonOrdinamento))
        .subscribe(result => {
          this.idList = [];
          result.risultati.forEach(data => { this.idList.push(data.id) })
          this.calcoloPremioAccoppiati(this.idList);
        })
    }
    else
      this.calcoloPremioAccoppiati(idList);
  }

  calcoloPremioAccoppiati(domande: Array<number>) {

    if (this.selectedSostegno == SostegnoDu.ZOOTECNIA) {
      this.idTipoProcesso = 'CLC_PREMIO_ACCOPPIATO_ZOOTECNIA';
      let jsonInput: any = { "annoCampagna": this.dettaglioIstruttoria.annoRiferimento, "idsDomande": domande };
      this.istruttoriaService.avviaPremioAcz(jsonInput).subscribe(next => {
        this.messageService.add(
          A4gMessages.getToast(
            'tst',
            A4gSeverityMessage.success,
            domande.length > 1 ?
              A4gMessages.calcoloAccoppiatoACZProcessoAvviato(domande.length) :
              A4gMessages.calcoloAccoppiatoACZProcessoAvviatoDomandaSingola
          ));
        this.runStatoAvanzamentoProcesso(domande.length);
      },
        err => {
          if (err.error.message == 'BRIDUACZ124')
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloPremioACZProcessoControllo));
          else if (err.error.message == 'BRIDUACZ110')
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloFinestraTemporaleDomandaIntegrativaACZ));
          else {
            console.log(err.error.message);
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.calcoloAccoppiatiErroreProcesso));
          }
        })
    }
    else if (this.selectedSostegno == SostegnoDu.SUPERFICIE) {
      this.idTipoProcesso = TipoProcesso.ACC_SUPERFICIE;
      let jsonInput: any = { "annoCampagna": this.dettaglioIstruttoria.annoRiferimento, "idsDomande": domande };
      this.istruttoriaService.avviaAccoppiatoSuperficie(jsonInput).subscribe(next => {
        this.messageService.add(
          A4gMessages.getToast(
            'tst',
            A4gSeverityMessage.success,
            domande.length > 1 ?
              A4gMessages.calcoloAccoppiatoACSProcessoAvviato(domande.length) :
              A4gMessages.calcoloAccoppiatoACSProcessoAvviatoDomandaSingola
          ));
        this.runStatoAvanzamentoProcesso(domande.length);
      }, err => {
        if (err.error.message == 'BRIDUACS088') {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloAccoppiatoACSProcessoControllo));
        } else {
          console.log(err.error.message);
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.calcoloAccoppiatiErroreProcesso));
        }
      });
    }
  }

  openPopupDatiAggregati() {
    this.popupVisibile = true;
  }
  closePopupDatiAggregati() {
    this.popupVisibile = false;
  }

  ngOnDestroy() {
    clearInterval(this.interval);
    this.loader.resetTimeout();
  }

  selezionaTutto(checked: boolean, table: any) {
    if (checked) {
      this.domande = this.paginaDomande.risultati;
    }
    else {
      this.domande = [];
    }
  }


  downloadDatiCapiAgea() {

    this.domandaIntegrativaService.getFileDatiCapiAgea(this.idIstruttoria).subscribe(result => {
      FileSaver.saveAs(result, "CAPI_ZOOTECNIA_APPAG.csv");
    });
  }

  private checkStatoAvanzamento() {
    let params = encodeURIComponent(JSON.stringify({ tipoProcesso: this.idTipoProcesso, statoProcesso: StatoProcesso.RUN }));
    this.http.get(this._configuration.urlControlloAntimafiaStatoAvanzamento + params).subscribe(
      (statoAvanzamento: any) => {
        if ((String(statoAvanzamento.esito) === Labels.NESSUN_PROCESSO_DI_CONTROLLO)) {
          this.processiInCorso = Labels.NESSUN_PROCESSO_DI_CONTROLLO;
          return;
        }
        this.runStatoAvanzamentoProcesso(statoAvanzamento.numeroDomandeDaElaborare);
      }, err => {
        this.timer.unsubscribe();
        this.processiInCorso = Labels.NESSUN_PROCESSO_DI_CONTROLLO;
      }
    );
  }

  runStatoAvanzamentoProcesso(domandeTotali: number) {
    let domandeParziali: number = 0;

    if (this.idTipoProcesso == "CLC_ACCOPPIATO_ZOOTECNIA") {
      this.processiInCorso = A4gMessages.processoDiCalcoloCapiAmm(String(domandeParziali), String(domandeTotali));
    }
    else if ((this.idTipoProcesso == "CLC_ACCOPPIATO_SUPERFICIE")) {
      this.processiInCorso = A4gMessages.processoDiCalcoloACS(String(domandeParziali), String(domandeTotali));
    }
    else if ((this.idTipoProcesso == "CLC_PREMIO_ACCOPPIATO_ZOOTECNIA")) {
      this.processiInCorso = A4gMessages.processoDiCalcoloPremioCapi(String(domandeParziali), String(domandeTotali));
    }
    else if ((this.idTipoProcesso == "CTRL_LIQUIDABILITA_ACCOPPIATO_ZOOTECNIA")) {
      this.processiInCorso = A4gMessages.processoDiControlliLiquidazione(String(domandeParziali), String(domandeTotali));
    }
    else if ((this.idTipoProcesso == "CTRL_LIQUIDABILITA_ACCOPPIATO_SUPERFICIE")) {
      this.processiInCorso = A4gMessages.processoDiControlliLiquidazione(String(domandeParziali), String(domandeTotali));
    }
    else if ((this.idTipoProcesso == "CTRL_INTERSOSTEGNO_ACC_SUPERFICIE")) {
      this.processiInCorso = A4gMessages.processoDiControlliIntersostegnoACS(String(domandeParziali), String(domandeTotali));
    }
    let params = encodeURIComponent(JSON.stringify({ tipoProcesso: this.idTipoProcesso, statoProcesso: StatoProcesso.RUN }));
    this.timer = timer(0, this.timeout).subscribe((numRipetizioni) => {
      this.http.get(this._configuration.urlControlloAntimafiaStatoAvanzamento + params).subscribe(
        (statoAvanzamento: any) => {
          if (numRipetizioni > 1 && (String(statoAvanzamento.esito) === Labels.NESSUN_PROCESSO_DI_CONTROLLO)) {
            this.timer.unsubscribe();
            this.processiInCorso = Labels.NESSUN_PROCESSO_DI_CONTROLLO;
            this.refresh();
            // this.refresh = true;
            // this.loadCounters();
            return;
          }
          if (!A4gMessages.isUndefinedOrNull(statoAvanzamento.totaleDomandeGestite)) {
            domandeParziali = statoAvanzamento.totaleDomandeGestite;
            if (this.idTipoProcesso == "CLC_ACCOPPIATO_ZOOTECNIA") {
              this.processiInCorso = A4gMessages.processoDiCalcoloCapiAmm(String(domandeParziali), String(domandeTotali));
            }
            else if ((this.idTipoProcesso == "CLC_ACCOPPIATO_SUPERFICIE")) {
              this.processiInCorso = A4gMessages.processoDiCalcoloACS(String(domandeParziali), String(domandeTotali));
            }
            else if ((this.idTipoProcesso == "CLC_PREMIO_ACCOPPIATO_ZOOTECNIA")) {
              this.processiInCorso = A4gMessages.processoDiCalcoloPremioCapi(String(domandeParziali), String(domandeTotali));
            }
            else if ((this.idTipoProcesso == "CTRL_LIQUIDABILITA_ACCOPPIATO_ZOOTECNIA")) {
              this.processiInCorso = A4gMessages.processoDiControlliLiquidazione(String(domandeParziali), String(domandeTotali));
            } else if ((this.idTipoProcesso == "CTRL_LIQUIDABILITA_ACCOPPIATO_SUPERFICIE")) {
              this.processiInCorso = A4gMessages.processoDiControlliLiquidazione(String(domandeParziali), String(domandeTotali));
            }
            else if ((this.idTipoProcesso == "CTRL_INTERSOSTEGNO_ACC_SUPERFICIE")) {
              this.processiInCorso = A4gMessages.processoDiControlliIntersostegnoACS(String(domandeParziali), String(domandeTotali));
            }
          }
          if (+domandeParziali >= +domandeTotali) {
            this.timer.unsubscribe();
            this.processiInCorso = Labels.NESSUN_PROCESSO_DI_CONTROLLO;
            this.refresh();
            // this.refresh = true;
            // this.loadCounters();
          }
        }, err => {
          this.timer.unsubscribe();
          this.processiInCorso = Labels.NESSUN_PROCESSO_DI_CONTROLLO;
        }
      );

    });
  }

  setStatusNonAmmissibile() {

    if (this.domande.length == 0) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloAccoppiatiWarningNumeroDomande));
      return;
    }
    const idList: number[] = this.domande.map(
      domanda => {
        return domanda.id;
      }
    );
    if (this.Checkbox.checked) {
      const jsonParametri = JSON.stringify(this.filtroAccoppiati.filtro);
      const jsonPaginazione = '{ "numeroElementiPagina": ' + this.paginaDomande.elementiTotali + ', "pagina": ' + 0 + '}';
      const jsonOrdinamento = '';
      this.elencoDomandeService.getPaginaDomandeAttuale(encodeURIComponent(jsonParametri),
        encodeURIComponent(jsonPaginazione), encodeURIComponent(jsonOrdinamento))
        .subscribe(result => {
          this.idList = [];
          result.risultati.forEach(data => { this.idList.push(data.id) })
          this.doSetStatusNonAmmissibile(this.idList);
        })
    }
    else
      this.doSetStatusNonAmmissibile(idList);
  }

  doSetStatusNonAmmissibile(domande: Array<number>) {

    let aggiornaStatoDomande: any = {
      "idsDomande": domande,
      "identificativoSostegno": this.selectedSostegno,
      "identificativoStatoLavSostegno": "NON_AMMISSIBILE",
      "annoCampagna": this.dettaglioIstruttoria.annoRiferimento
    }

    let sostegno: string;
    if (this.selectedSostegno == SostegnoDu.ZOOTECNIA)
      sostegno = 'az';
    else if (this.selectedSostegno == SostegnoDu.SUPERFICIE)
      sostegno = 'as';

    this.istruttoriaService.aggiornaStatoDomande(sostegno, aggiornaStatoDomande).subscribe(v => {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
      this.refresh()
    },
      err => {
        if (err.error.message == 'BRIDUACZ124')
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloPremioACZProcessoControllo));
        else if (err.error.message == 'BRIDUACS088')
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloAccoppiatoACSProcessoControllo));
        else if (err.error.message == 'BRIDUACZ110')
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.finestraTemporaleDomandaIntegrativaChiusa));
        else {
          console.log(err.error.message);
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.calcoloAccoppiatiErroreProcesso));
        }
      });
  }


  avviaControlliLiquidazione() {

    if (this.domande.length == 0) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloAccoppiatiWarningNumeroDomande));
      return;
    }
    const idList: number[] = this.domande.map(domanda => domanda.id);

    if (this.Checkbox.checked) {
      const jsonParametri = JSON.stringify(this.filtroAccoppiati.filtro);
      const jsonPaginazione = '{ "numeroElementiPagina": ' + this.paginaDomande.elementiTotali + ', "pagina": ' + 0 + '}';
      const jsonOrdinamento = '';
      this.elencoDomandeService.getPaginaDomandeAttuale(encodeURIComponent(jsonParametri),
        encodeURIComponent(jsonPaginazione), encodeURIComponent(jsonOrdinamento))
        .subscribe(result => {
          this.idList = [];
          result.risultati.forEach(data => { this.idList.push(data.id) })
          this.avviaCalcoloControlliLiquidazione(this.idList);
        })
    }
    else
      this.avviaCalcoloControlliLiquidazione(idList);
  }

  avviaCalcoloControlliLiquidazione(domande: Array<number>) {
    let sostegno: string;
    if (this.selectedSostegno == SostegnoDu.ZOOTECNIA) {
      sostegno = 'az';
      this.idTipoProcesso = "CTRL_LIQUIDABILITA_ACCOPPIATO_ZOOTECNIA";
    } else if (this.selectedSostegno == SostegnoDu.SUPERFICIE) {
      sostegno = 'as';
      this.idTipoProcesso = "CTRL_LIQUIDABILITA_ACCOPPIATO_SUPERFICIE";
    }

    this.istruttoriaService.avviaControlliLiquidazione(domande, sostegno).subscribe(next => {
      this.messageService.add(
        A4gMessages.getToast(
          'tst',
          A4gSeverityMessage.success,
          domande.length > 1 ?
            A4gMessages.calcoloControlliLiquidazioneAvviato(domande.length) :
            A4gMessages.calcoloControlliLiquidazioneAvviatoDomandaSingola
        ));
      this.runStatoAvanzamentoProcesso(domande.length);
    },
      err => {
        if (err.error.message == 'CTRLLIQTAACZ') {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloControlliLiquidazioneProcessoControllo));
        } else if (err.error.message == 'CTRLLIQTAACS') {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloControlliLiquidazioneProcessoControllo));
        } else {
          console.log(err.error.message);
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.calcoloAccoppiatiErroreProcesso));
        }
      })
  }

  avviaControlloIntersostegnoACS() {

    this.idTipoProcesso = "CTRL_INTERSOSTEGNO_ACC_SUPERFICIE";

    if (this.domande.length == 0) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloAccoppiatiWarningNumeroDomande));
      return;
    }
    const idList: number[] = this.domande.map(domanda => domanda.id);

    if (this.Checkbox.checked) {
      const jsonParametri = JSON.stringify(this.filtroAccoppiati.filtro);
      const jsonPaginazione = '{ "numeroElementiPagina": ' + this.paginaDomande.elementiTotali + ', "pagina": ' + 0 + '}';
      const jsonOrdinamento = '';
      this.elencoDomandeService.getPaginaDomandeAttuale(encodeURIComponent(jsonParametri),
        encodeURIComponent(jsonPaginazione), encodeURIComponent(jsonOrdinamento))
        .subscribe(result => {
          this.idList = [];
          result.risultati.forEach(data => { this.idList.push(data.id) })
          this.avviaCalcoloControlloIntersostegnoACS(this.idList);
        })
    }
    else
      this.avviaCalcoloControlloIntersostegnoACS(idList);
  }

  avviaCalcoloControlloIntersostegnoACS(domande: Array<number>) {
    const jsonInput: any = { "annoCampagna": this.dettaglioIstruttoria.annoRiferimento, "idsDomande": domande };
    this.istruttoriaService.avviaControlliIntersostegnoACS(jsonInput).toPromise().then(
      (v: void) => {
        this.messageService.add(
          A4gMessages.getToast(
            'tst',
            A4gSeverityMessage.success,
            domande.length > 1 ?
              A4gMessages.calcoloControllIntersostegno(domande.length) :
              A4gMessages.calcoloControlliIntersostegnoDomandaSingola
          ));
        this.runStatoAvanzamentoProcesso(domande.length);
      },
      err => {
        if (err.error.message == 'CTRLLIQTAACS') {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloControlliIntersostegnoProcessoControllo));
        } else {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.calcoloAccoppiatiErroreProcesso));
        }
      }
    );
  }

  public downloadVerbaleLiquidazione(domanda: Domanda) {
    const baseRequest = { idDomanda: domanda.id, codiceElenco: domanda.codiceElenco };
    const request = encodeURIComponent(JSON.stringify(baseRequest));
    this.istruttoriaService.getDocumentoElencoLiquidazione(request).subscribe(result => {
      FileSaver.saveAs(result, domanda.codiceElenco.toString().concat('.pdf'));
    }, err => {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
    });
  }
}