import { DichiarazioneConsumiDto } from './../../uma/core-uma/models/dto/DichiarazioneConsumiDto';
import { PaginatorEvent, Paginazione } from './../../a4g-common/utility/paginazione';
import { DomandaUmaFilter } from './../../uma/core-uma/models/dto/DomandaUmaFilter';
import { FormatConverterService } from './../../uma/shared-uma/services/format-converter.service';
import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { Labels } from "src/app/app.labels";
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClientDichiarazioneConsumiUmaService } from "src/app/uma/core-uma/services/http-client-dichiarazione-consumi-uma.service";
import { ConsultazioneUMA } from "src/app/uma/core-uma/models/dto/ConsultazioneUMA";
import { HttpClientDomandaUmaService } from "src/app/uma/core-uma/services/http-client-domanda-uma.service";
import { FormControl, FormGroup } from '@angular/forms';
import { TipoIntestazioneUma } from 'src/app/a4g-common/classi/enums/uma/TipoDocumentoUma.enum';
import { StatoDomandaUma } from 'src/app/a4g-common/classi/enums/uma/StatoDomandaUma.enum';
import { StatoDichiarazioneConsumiEnum } from 'src/app/a4g-common/classi/enums/uma/StatoDichiarazioneConsumi.enum';
import { TipologicheService } from 'src/app/a4g-common/services/tipologiche.service';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
import { Subscription, forkJoin } from 'rxjs';
import { PaginatorA4G } from 'src/app/a4g-common/interfaces/paginator.model';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { DichiarazioneConsumiFilter } from 'src/app/uma/core-uma/models/dto/DichiarazioneConsumiFilter';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
import { MessageService } from 'primeng-lts';
import { PaginatorService } from 'src/app/a4g-common/services/paginator.service';
import { FilterUtils } from 'primeng-lts/utils';
import * as _ from 'lodash';
import { DomandaUmaDto } from 'src/app/uma/core-uma/models/dto/DomandaUmaDto';
export interface FilterTable {
  field: string;
  value: string;
  matchMode: string;
}
@Component({
  selector: 'app-consultazione-UMA',
  templateUrl: './consultazione-UMA.component.html',
  styleUrls: ['./consultazione-UMA.component.scss']
})

/** Filtro ricerca richieste/rettifiche e dichiarazioni da parte dell'Istruttore UMA */
export class ConsultazioneUMAComponent implements OnInit, OnDestroy {
  labels = Labels;
  intestazioni = Labels;
  datiDichiarazioniPartial: Array<ConsultazioneUMA> = [];   /** parte del datasource mostrato di volta in volta in base alle pagine */
  datiDichiarazioniGlobal: Array<ConsultazioneUMA> = [];  /** intero datasource estratto dal DB una sola volta (solo per caa) */
  datiRichiestePartial: Array<ConsultazioneUMA> = [];       /** parte del datasource mostrato di volta in volta in base alle pagine */
  datiRichiesteGlobal: Array<ConsultazioneUMA> = [];      /** intero datasource estratto dal DB una sola volta (solo per caa) */
  tipoDichiarazione: TipoIntestazioneUma = TipoIntestazioneUma.DICHIARAZIONE_CONSUMI;
  tipoRichiesta: TipoIntestazioneUma = TipoIntestazioneUma.RICHIESTA;
  elementiPerPagina: number = 5;
  nrDichiarazioni: number;
  nrRichieste: number;
  datiUnificatiGlobal: Array<ConsultazioneUMA> = [];
  datiUnificatiPartial: Array<ConsultazioneUMA> = [];
  nrDomandeUnificate: number;
  ricercaForm: FormGroup;
  stati: Array<any>;
  anni: Array<any>;
  tableReset: boolean;

  consultazioneAsCaa: boolean; /** true, se sono nella pagina come caa */

  // Subscriptions
  listaDomandeSubscription: Subscription;
  listaDichiarazioniSubscription: Subscription;
  getListeSubscription: Subscription;
  listaDomandeUnificateSubscription: Subscription;

  constructor(
    private httpClientDichiarazioneConsumiUma: HttpClientDichiarazioneConsumiUmaService,
    private httpClientDomandaUmaService: HttpClientDomandaUmaService,
    private tipologicheService: TipologicheService,
    private dateUtilService: DateUtilService,
    private errrorService: ErrorService,
    private formatConverterService: FormatConverterService,
    private messageService: MessageService,
    private router: Router,
    private paginatorService: PaginatorService) {
  }

  ngOnInit() {
    this.consultazioneAsCaa = false;
    if (this.router.url && this.router.url.indexOf('funzioniCaa') > -1) {
      this.consultazioneAsCaa = true;
      this.elementiPerPagina = 10;
    }
    this.initSelect();
    this.initForm();
  }

  ngOnDestroy() {
    if (this.listaDomandeSubscription) {
      this.listaDomandeSubscription.unsubscribe();
    }
    if (this.listaDichiarazioniSubscription) {
      this.listaDichiarazioniSubscription.unsubscribe();
    }
    if (this.getListeSubscription) {
      this.getListeSubscription.unsubscribe();
    }
    if (this.listaDomandeUnificateSubscription) {
      this.listaDomandeUnificateSubscription.unsubscribe();
    }
  }

  // Solo per i caa
  getDatiUnificati(paginator?: PaginatorEvent, params?: DomandaUmaFilter) {
    if (!this.datiUnificatiGlobal || !this.datiUnificatiGlobal.length) {
      this.invokeGetRichiesteAndDichiarazioni(params);
    } else { // se già ho precaricato l'array di richieste
      const arrayOfFilters: Array<{ field: string, value: string, matchMode: string }> = this.buildLiveFilter(paginator);
      const filter: DomandaUmaFilter = this.buildFilterPaginazioneFE(paginator);
      this.datiUnificatiGlobal = this.doSort(filter, this.datiUnificatiGlobal);
      if (arrayOfFilters && arrayOfFilters.length) {
        arrayOfFilters.forEach((filter: { field: string, value: string, matchMode: string }, index: number) => {
          if (index === 0) { // la prima volta estraggo dall'intero datasource...
            this.datiUnificatiPartial = FilterUtils.filter(this.datiUnificatiGlobal, [filter.field], filter.value, filter.matchMode);
          } else {          // ...dalla seconda volta in poi filtro sull'insieme già filtrato (in caso di più filtri -> AND logica)
            this.datiUnificatiPartial = FilterUtils.filter(this.datiUnificatiPartial, [filter.field], filter.value, filter.matchMode);
          }
        });
        this.nrDomandeUnificate = this.datiUnificatiPartial.length;
        this.datiUnificatiPartial = this.paginatorService.getPage(this.datiUnificatiPartial, filter.pagina, filter.numeroElementiPagina);
      } else {
        this.nrDomandeUnificate = this.datiUnificatiGlobal.length;
        this.datiUnificatiPartial = this.paginatorService.getPage(this.datiUnificatiGlobal, filter.pagina, filter.numeroElementiPagina);
      }
    }
  }

  getDichiarazioni(paginator: PaginatorEvent, params: DichiarazioneConsumiFilter) {
    if (!this.consultazioneAsCaa) { // Paginazione BE 
      this.invokeGetDichiarazioni(params);
    }
  }

  getRichieste(paginator: PaginatorEvent, params: DomandaUmaFilter) {
    if (!this.consultazioneAsCaa) { // Paginazione BE
      this.invokeGetRichieste(params);
    }
  }

  aggiornaTabella($event: { paginator: PaginatorEvent, tipo: TipoIntestazioneUma | 'FULL' }) {
    // Il caa non può effettuare ricerche per Tutti gli anni -> ci sarebbero problemi di performance
    if (this.consultazioneAsCaa && this.ricercaForm.get('anni').value == null) {
      this.messageService.clear(); // evita la doppia visualizzazione del messaggio visto che (aggiornaTabella) è invocato sia dalla lista richieste che da quella dei consumi
      this.errrorService.showErrorWithMessage('E\' necessario selezionare un filtro per anno!', 'tst-csv');
      this.tableReset = false;
      return;
    }

    if ($event.tipo === TipoIntestazioneUma.RICHIESTA) {
      this.getRichieste($event.paginator, this.buildFilter($event.paginator, $event.tipo));
    } else if ($event.tipo === TipoIntestazioneUma.DICHIARAZIONE_CONSUMI) {
      this.getDichiarazioni($event.paginator, this.buildFilterConsumi($event.paginator, $event.tipo));
    } else if ($event.tipo === 'FULL') { /** ricerca specifica per caa, composta da una sola tabella che include tutto, con paginazione FE */
      this.elementiPerPagina = $event.paginator.rows;
      this.getDatiUnificati($event.paginator, this.buildFilter($event.paginator, 'FULL'));
    }
  }

  applicaFiltri() {
    // Questo reset triggera l'onLazyLoad della tabella con i parametri di default
    if (this.consultazioneAsCaa) {
      this.datiUnificatiGlobal = [];
      this.datiUnificatiPartial = [];
      this.tableReset = true;
    } else {
      this.tableReset = true;
      this.datiRichiesteGlobal = [];
      this.datiDichiarazioniGlobal = [];
    }
  }

  rimuoviFiltri() {
    this.initForm();
    // Questo reset triggera l'onLazyLoad della tabella con i parametri di default
    this.tableReset = true;
    this.datiRichiesteGlobal = [];
    this.datiDichiarazioniGlobal = [];
  }

  private buildFilterPaginazioneFE(paginator?: PaginatorEvent): DomandaUmaFilter {
    let filter = new DomandaUmaFilter();
    filter.pagina = (paginator != null && Math.floor(paginator.first / this.elementiPerPagina) != null) ? Math.floor(paginator.first / this.elementiPerPagina) : 0;
    filter.numeroElementiPagina = this.elementiPerPagina;
    filter.proprieta = paginator && paginator.sortField ? this.toSortVM(paginator.sortField) : 'nrDomanda';
    filter.ordine = paginator && paginator.sortOrder ? Paginazione.getOrdine(paginator.sortOrder) : 'ASC';
    return filter;
  }

  /** Filtri inseriti nei campi di input il cui identificativo è il nome della proprietà dell'oggetto */
  private buildLiveFilter(paginator: PaginatorEvent): Array<{ field: string, value: string, matchMode: string }> {
    const arrayOfFilters: Array<{ field: string, value: string, matchMode: string }> = [];
    if (paginator.filters != null && !_.isEmpty(paginator.filters)) {
      for (const prop in paginator.filters) {
        if (prop != 'global') {
          arrayOfFilters.push({ field: prop, value: paginator.filters[prop].value, matchMode: paginator.filters[prop].matchMode });
        }
      }
    }
    return arrayOfFilters;
  }

  private toSortVM(sortFieldFromDto: string): string {
    if (sortFieldFromDto === 'id') {
      return 'nrDomanda';
    }
    return sortFieldFromDto;
  }

  private doSort(params: DomandaUmaFilter, dataSource: Array<ConsultazioneUMA>): Array<ConsultazioneUMA> {
    return dataSource.sort((a, b) => {
      const property: string = params.proprieta;
      return this.paginatorService.compare((a as any)[property], (b as any)[property], params.ordine == 'ASC' ? true : false);
    });
  }

  private buildFilterConsumi(paginator: PaginatorEvent, tipo: TipoIntestazioneUma): DichiarazioneConsumiFilter {
    const richiestaCarburanteFilter: DomandaUmaFilter = this.buildFilter(paginator, tipo);
    const consumiFilter = new DichiarazioneConsumiFilter();
    Object.assign(consumiFilter, richiestaCarburanteFilter);
    return consumiFilter;
  }

  private buildFilter(paginator: PaginatorEvent, tipo: TipoIntestazioneUma | 'FULL'): DomandaUmaFilter {
    let filter = new DomandaUmaFilter();
    filter.id = this.ricercaForm.get("idDomanda").value;
    filter.cuaa = this.ricercaForm.get("cuaa").value;
    filter.denominazione = this.formatConverterService.isEmptyString(this.ricercaForm.get("denominazione").value) ? null : this.ricercaForm.get("denominazione").value;
    filter.campagna = [this.ricercaForm.get("anni").value == null ? this.getAllYearsFrom() : this.ricercaForm.get("anni").value];
    filter.stati = [];

    if (this.ricercaForm.get("stati").value) {
      filter.stati.push(this.ricercaForm.get("stati").value);
    } else if (localStorage.getItem("selectedRole") == "operatore_dogane" && (tipo == TipoIntestazioneUma.RICHIESTA || tipo == TipoIntestazioneUma.RETTIFICA)) {
      filter.stati.push(StatoDomandaUma.AUTORIZZATA);
      filter.stati.push(StatoDomandaUma.RETTIFICATA);
    } else if (localStorage.getItem("selectedRole") == "operatore_dogane" && tipo == TipoIntestazioneUma.DICHIARAZIONE_CONSUMI) {
      filter.stati.push(StatoDichiarazioneConsumiEnum.PROTOCOLLATA);
    }

    if (filter.stati.length && tipo === TipoIntestazioneUma.DICHIARAZIONE_CONSUMI && (filter.stati[0] === StatoDomandaUma.AUTORIZZATA || filter.stati[0] === StatoDomandaUma.RETTIFICATA)) {
      filter.stati = [];
      filter.stati[0] = StatoDichiarazioneConsumiEnum.PROTOCOLLATA;
    }

    // Paginator Filter
    filter.pagina = (paginator != null && Math.floor(paginator.first / this.elementiPerPagina) != null) ? Math.floor(paginator.first / this.elementiPerPagina) : 0;
    filter.numeroElementiPagina = this.elementiPerPagina;
    filter.proprieta = paginator && paginator.sortField ? paginator.sortField : 'id';
    filter.ordine = paginator && paginator.sortOrder ? Paginazione.getOrdine(paginator.sortOrder) : 'ASC';

    return filter;
  }

  private invokeGetRichiesteAndDichiarazioni(params: DomandaUmaFilter) {
    const getDomande$ = this.httpClientDomandaUmaService.getDomandeCaa(params);
    const getDichiarazioni$ = this.httpClientDichiarazioneConsumiUma.getDichiarazioniConsumiCaa(params);
    this.listaDomandeUnificateSubscription = forkJoin([getDomande$, getDichiarazioni$])
      .subscribe(([richieste, dichiarazioni]: [Array<DomandaUmaDto>, Array<DomandaUmaDto>]) => {
        this.datiUnificatiGlobal = [];
        this.datiUnificatiPartial = [];
        this.nrDomandeUnificate = 0;
        if (richieste && richieste.length) {
          this.nrDomandeUnificate = richieste.length;
          // const groupedByCuaa = _.groupBy(richieste, 'cuaa');
          // this.calcTipoRichiesta(groupedByCuaa, element);
          richieste.forEach((element: DomandaUmaDto) => {
            this.datiUnificatiGlobal.push({ nrDomanda: element.id, anno: element.campagna, tipo: element.tipo, stato: element.stato, cuaa: element.cuaa, denominazione: element.denominazione, protocollo: element.protocollo });
          });
        }
        if (dichiarazioni && dichiarazioni.length) {
          this.nrDomandeUnificate = this.nrDomandeUnificate + dichiarazioni.length;
          dichiarazioni.forEach((element: DomandaUmaDto) => {
            this.datiUnificatiGlobal.push({ nrDomanda: element.id, anno: element.campagna, tipo: element.tipo.split('_').join(' '), stato: element.stato, cuaa: element.cuaa, denominazione: element.denominazione, protocollo: element.protocollo });
          });
        }
        this.datiUnificatiPartial = this.paginatorService.getPage(this.datiUnificatiGlobal, params.pagina, params.numeroElementiPagina);
        this.tableReset = false;
      }, error => this.errrorService.showError(error));
  }

  /** @deprecated */
  private calcTipoRichiesta(richiesteRaggruppateByCuaa: { [key: string]: Array<DomandaUmaDto> }, richiestaCorrente: DomandaUmaDto) {
    for (const currentCuaa in richiesteRaggruppateByCuaa) {
      if (currentCuaa == richiestaCorrente.cuaa) {
        const cuaaWithRichieste = richiesteRaggruppateByCuaa[currentCuaa];
        // Nel caso di piu richieste, quella più vecchia è una RICHIESTE, le altre sono tutte RETTIFICHE
        if (cuaaWithRichieste && cuaaWithRichieste.length > 1) {
          const orderedList: Array<DomandaUmaDto> = _.sortBy(cuaaWithRichieste, 'dataPresentazione');
          if (richiestaCorrente.id == orderedList[0].id) {
            return TipoIntestazioneUma.RICHIESTA;
          } else {
            return TipoIntestazioneUma.RETTIFICA;
          }
        } else { // Nel caso di una sola richiesta per cuaa, quella è sicuramente una RICHIESTA
          return TipoIntestazioneUma.RICHIESTA;
        }
      }
    }
    return TipoIntestazioneUma.RICHIESTA;
  }

  private invokeGetRichieste(params: DomandaUmaFilter) {
    this.consultazioneAsCaa ? this.invokeGetRichiesteList(params) : this.invokeGetRichiestePaged(params);
  }

  private invokeGetRichiesteList(params: DomandaUmaFilter) {
    this.listaDomandeSubscription = this.httpClientDomandaUmaService.getDomandeCaa(params)
      .subscribe((result: Array<DomandaUmaDto>) => {
        this.datiRichiesteGlobal = [];
        this.datiRichiestePartial = [];
        this.nrRichieste = 0;
        if (result && result.length) { // Senza Paginazione
          this.nrRichieste = result.length;
          result.forEach((element: DomandaUmaDto) => {
            this.datiRichiesteGlobal.push({ nrDomanda: element.id, anno: element.campagna, tipo: TipoIntestazioneUma.RICHIESTA, stato: element.stato, cuaa: element.cuaa, denominazione: element.denominazione, protocollo: element.protocollo });
          });
          this.datiRichiestePartial = this.paginatorService.getPage(this.datiRichiesteGlobal, params.pagina, params.numeroElementiPagina);
        }
        this.tableReset = false;
      }, error => this.errrorService.showError(error));
  }

  private invokeGetRichiestePaged(params: DomandaUmaFilter) {
    this.listaDomandeSubscription = this.httpClientDomandaUmaService.getDomandePaged(params)
      .subscribe((result: PaginatorA4G<Array<RichiestaCarburanteDto>>) => {
        this.datiRichiesteGlobal = [];
        this.datiRichiestePartial = [];
        this.nrRichieste = 0;
        if (result && result.count > 0) { // Con Paginazione
          this.nrRichieste = result.count;
          result.risultati.forEach((element: RichiestaCarburanteDto) => {
            this.datiRichiesteGlobal.push({ nrDomanda: element.id, anno: element.campagna, tipo: TipoIntestazioneUma.RICHIESTA, stato: element.stato, cuaa: element.cuaa, denominazione: element.denominazione, protocollo: element.protocollo });
          });
          this.datiRichiestePartial = this.datiRichiesteGlobal;
        }
        this.tableReset = false;
      }, error => this.errrorService.showError(error));
  }

  private invokeGetDichiarazioni(params: DichiarazioneConsumiFilter) {
    this.consultazioneAsCaa ? this.invokeGetDichiarazioniList(params) : this.invokeGetDichiarazioniPaged(params);
  }

  private invokeGetDichiarazioniList(params: DichiarazioneConsumiFilter) {
    this.listaDichiarazioniSubscription = this.httpClientDichiarazioneConsumiUma.getDichiarazioniConsumiCaa(params)
      .subscribe((result: Array<DomandaUmaDto>) => {
        this.datiDichiarazioniGlobal = [];
        this.datiDichiarazioniPartial = [];
        this.nrDichiarazioni = 0;
        if (result && result.length) { // Senza Paginazione
          this.nrDichiarazioni = result.length;
          result.forEach((element: DomandaUmaDto) => {
            this.datiDichiarazioniGlobal.push({ nrDomanda: element.id, anno: element.campagna, tipo: TipoIntestazioneUma.DICHIARAZIONE_CONSUMI, stato: element.stato, cuaa: element.cuaa, denominazione: element.denominazione, protocollo: element.protocollo });
          });
          this.datiDichiarazioniPartial = this.paginatorService.getPage(this.datiDichiarazioniGlobal, params.pagina, params.numeroElementiPagina);
        }
        this.tableReset = false;
      }, error => this.errrorService.showError(error));
  }

  private invokeGetDichiarazioniPaged(params: DichiarazioneConsumiFilter) {
    this.listaDichiarazioniSubscription = this.httpClientDichiarazioneConsumiUma.getDichiarazioniConsumiPaged(params)
      .subscribe((result: PaginatorA4G<Array<DichiarazioneConsumiDto>>) => {
        this.datiDichiarazioniGlobal = [];
        this.datiDichiarazioniPartial = [];
        this.nrDichiarazioni = 0;
        if (result && result.count > 0) { // Con Paginazione
          this.nrDichiarazioni = result.count;
          result.risultati.forEach((element: DichiarazioneConsumiDto) => {
            this.datiDichiarazioniGlobal.push({ nrDomanda: element.id, anno: element.campagnaRichiesta, tipo: TipoIntestazioneUma.DICHIARAZIONE_CONSUMI, stato: element.stato, cuaa: element.cuaa, denominazione: element.denominazione, protocollo: element.protocollo });
          });
          this.datiDichiarazioniPartial = this.datiDichiarazioniGlobal;
        }
        this.tableReset = false;
      }, error => this.errrorService.showError(error));
  }

  // Il filtro "tutti gli anni", vuol dire dal 2022 (anno inizio campagna) in poi
  private getAllYearsFrom(): Array<string> {
    const years: Array<string> = [];
    this.dateUtilService.getListYearsFrom(environment.annoInizioCampagnaUma).forEach((year: number) => {
      years.push(year.toString());
    });
    return years;
  }

  private initSelect() {
    this.loadData();
    // costruzione lista anni iniziale
    if (this.consultazioneAsCaa) { // Il caa non ha il filtro per tutti gli anni
      this.anni = [];
    } else {
      this.anni = [{ label: 'Tutti gli anni', value: null }];
    }

    /** creo una lista di anni a partire dal 2020 per popolare la select in TEST e a partire dal 2022 per quality e prod*/
    this.dateUtilService.getListYearsFrom(environment.annoInizioCampagnaUma).forEach((year: number) => {
      this.anni.push({ label: year.toString(), value: year });
    });

    // costruzione lista stati
    this.stati = this.tipologicheService.statiRichiestaCarburanteUma.slice();
    this.stati.unshift({ label: 'Tutti gli stati', value: null });
  }

  private initForm() {
    this.ricercaForm = new FormGroup({
      idDomanda: new FormControl(null),
      cuaa: new FormControl(null),
      denominazione: new FormControl(null),
      anni: new FormControl(this.dateUtilService.getCurrentYear()),
      stati: new FormControl(null)
    });
  }

  private loadData() {
    this.tipologicheService.setDocumentiUma();
    this.tipologicheService.setStatiDichiarazioneConsumiUma();
    this.tipologicheService.setStatiRichiestaEDichiarazioneUma();
    this.tipologicheService.setStatiRettificaUma();
    if (localStorage.getItem("selectedRole") == "operatore_dogane") {
      this.tipologicheService.setStatiRichiestaCarburanteUmaOperatoreDogane();
    } else {
      this.tipologicheService.setStatiRichiestaCarburanteUma();
    }
  }
}
