import {Component, OnDestroy, OnInit} from '@angular/core';
import {MessageService, SelectItem} from 'primeng-lts/api';
import {Labels} from 'src/app/app.labels';
import {IstruttoriaAntimafiaService} from "../../../istruttoria/istruttoria-antimafia/istruttoria-antimafia.service";
import {Paginazione, SortDirection} from "../../../a4g-common/utility/paginazione";
import {A4gMessages, A4gSeverityMessage} from "../../../a4g-common/a4g-messages";
import {DatiDomandaRicercaPage} from "../../../istruttoria/istruttoria-pac1420/domandaUnica/classi/DatiDomandaRicerca";
import {IstruttoriaDomandaUnicaFilter} from "../../../istruttoria/istruttoria-pac1420/domandaUnica/classi/IstruttoriaDomandaUnicaFilter";
import {TranslateService} from "@ngx-translate/core";
import {forkJoin} from "rxjs";
import {Subscription} from "rxjs";

enum FilterFields {
  CAMPAGNA = 'campagna',
  STATODOMANDA = 'statoDomanda',
  SEARCHTEXT = 'searchText',
  SOSTEGNO = 'sostegno',
  TIPO = 'tipo'
}

@Component({
  selector: 'app-ricerca-domande',
  templateUrl: './ricerca-domande.component.html',
  styleUrls: ['./ricerca-domande.component.css'],
})
export class RicercaDomandeComponent implements OnInit, OnDestroy {
  filterFields = FilterFields;
  searchText = '';
  applicationFieldName: string;
  applicationFieldNames: SelectItem[] = [
    { label: Labels.selectField, value: null, disabled: true },
    { label: Labels.CUAA, value: Labels.CUAA },
    { label: Labels.companyDescription, value: 'ragioneSociale' },
    { label: Labels.NUMERO_DOMANDA, value: 'numeroDomanda' },
  ];
  applicationState: string;
  applicationStates: SelectItem[] = [];
  applicationYear: number;
  applicationYears: SelectItem[] = [];
  applicationSupport: string;
  applicationSupports: SelectItem[] = [
    { label: 'Disaccoppiato', value: 'DISACCOPPIATO' },
    { label: 'Accoppiato superficie', value: 'SUPERFICIE' },
    { label: 'Accoppiato zootecnia', value: 'ZOOTECNIA' },
  ];
  applicationPayment: string;
  applicationPayments: SelectItem[] = [
    { label: 'Anticipo', value: 'ANTICIPO' },
    { label: 'Saldo', value: 'SALDO' },
    { label: 'Integrazione', value: 'INTEGRAZIONE' },
  ];
  columns = [
    { name: Labels.CUAA.toUpperCase(), value: 'cuaaIntestatario', width: '17%' },
    {
      name: Labels.companyDescription,
      value: 'ragioneSociale',
      width: '40%',
    },
    { name: Labels.NUMERO_DOMANDA, value: 'numeroDomanda', width: '18%' },
    { name: Labels.anno, value: 'campagna', width: '6%' },
    { name: Labels.stato, value: 'stato', width: '11%' },
    { name: Labels.azioni, value: null, width: '8%' },
  ];
  datiDomandaRicercaPage = {
    count: 0,
    risultati: []
  } as DatiDomandaRicercaPage;
  sortBy: string = 'id';
  sortDirection: SortDirection;
  elementiPagina = 10;
  initialState = true;
  filters: IstruttoriaDomandaUnicaFilter;
  $ricercaDomandeUniceSubs: Subscription;
  $listeDatiSubs: Subscription;

  constructor(
      private istruttoriaAntimafiaService: IstruttoriaAntimafiaService,
      private messageService: MessageService,
      private translateService: TranslateService
  ) {}

  ngOnInit() {
    this.$listeDatiSubs = forkJoin( [this.istruttoriaAntimafiaService.getListaStati(),
        this.istruttoriaAntimafiaService.getListaAnni()]
    ).subscribe(([stati, anni]) => {
      this.applicationStates = stati.map(stato => {
        return {
          value: stato,
          label: this.translateService.instant(`StatoDomandaEnum.${stato}`)
        } as SelectItem;
      });
      this.applicationYears = anni.map(anno => {
        return {
          value: anno,
          label: anno.toString()
        } as SelectItem;
      });

    });
  }

  changePage(event: any, start: number = 0) {
    if (event != null) {
      this.sortDirection = event.sortOrder === 1 ? SortDirection.ASC : SortDirection.DESC;
      this.sortBy = event.sortField || 'id';
    }
    let paginazione: Paginazione = Paginazione.of(
        event.first/this.elementiPagina, this.elementiPagina, this.sortBy, this.sortDirection || SortDirection.ASC
    );
    let domandaUnicaFilter = this.getCurrentFilters();
    if (domandaUnicaFilter !== null) {
      this.$ricercaDomandeUniceSubs = this.istruttoriaAntimafiaService.ricercaDomandeUniche(domandaUnicaFilter, paginazione)
          .subscribe(
              (dati => {
                    if (dati) {
                      this.datiDomandaRicercaPage = dati;
                    }
                  }
              ),
              (error => {
                this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
              })
          );
    }
  }

  searchDomande(): void {
    if (this.applicationFieldName && this.searchText !== '') {
      this.initialState = false;
      let istruttoriaDomandaUnicaFilter = this.getCurrentFilters();
      let paginazione: Paginazione = Paginazione.of(
          Math.round(0/this.elementiPagina), this.elementiPagina, this.sortBy, this.sortDirection || SortDirection.ASC
      );
      if (istruttoriaDomandaUnicaFilter !== null) {
        this.$ricercaDomandeUniceSubs =  this.istruttoriaAntimafiaService.ricercaDomandeUniche(istruttoriaDomandaUnicaFilter, paginazione).subscribe(value => {
          this.datiDomandaRicercaPage = value;
          this.filters = istruttoriaDomandaUnicaFilter;
        });
      }
    }
  }

  private getCurrentFilters(): IstruttoriaDomandaUnicaFilter | null {
    if (this.applicationFieldName === 'numeroDomanda' && isNaN(Number(this.searchText))) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.RicercaDomandeUnicheNumeroDomandaNonValido));
      return null;
    } else {
      let istruttoriaDomandaUnicaFilter = new IstruttoriaDomandaUnicaFilter();
      if (this.applicationYear) {
        istruttoriaDomandaUnicaFilter.campagna = this.applicationYear;
      }
      if (this.applicationSupport) {
        istruttoriaDomandaUnicaFilter.sostegno = this.applicationSupport;
      }
      if (this.applicationState) {
        istruttoriaDomandaUnicaFilter.statoDomanda = this.applicationState;
      }
      if (this.applicationFieldName) {
        istruttoriaDomandaUnicaFilter[this.applicationFieldName] = this.searchText;
      }
      if (this.applicationPayment) {
        istruttoriaDomandaUnicaFilter.tipo = this.applicationPayment;
      }
      return istruttoriaDomandaUnicaFilter;
    }
  }

  removeFilter(field: string): void {
    switch (field) {
      case FilterFields.CAMPAGNA:
        this.applicationYear = undefined;
        this.filters.campagna = undefined;
        break;
      case FilterFields.STATODOMANDA:
        this.applicationState = undefined;
        this.filters.statoDomanda = undefined;
        break;
      case FilterFields.SEARCHTEXT:
        this.searchText = undefined;
        this.applicationFieldName = undefined;
        this.filters[this.applicationFieldName] = undefined;
        break;
      case FilterFields.SOSTEGNO:
        this.applicationSupport = undefined;
        this.filters.sostegno = undefined;
        break;
      case FilterFields.TIPO:
        this.applicationPayment = undefined;
        this.filters.tipo = undefined;
        break;
      default:
        break;
    }

  }

  ngOnDestroy(): void {
    if (this.$ricercaDomandeUniceSubs) {
      this.$ricercaDomandeUniceSubs.unsubscribe();
    }
    if (this.$listeDatiSubs) {
      this.$listeDatiSubs.unsubscribe();
    }
  }
}
