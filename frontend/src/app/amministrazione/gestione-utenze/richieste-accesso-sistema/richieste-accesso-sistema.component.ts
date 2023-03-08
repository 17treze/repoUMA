import {Component, OnInit} from '@angular/core';
import {Labels} from 'src/app/app.labels';
import {UtentiService} from 'src/app/utenti/utenti.service';
import {StatoRichiestaCount} from './dto/StatoRichiestaCount';
import {MessageService} from 'primeng/api';
import {A4gMessages, A4gSeverityMessage} from 'src/app/a4g-common/a4g-messages';
import {SharedService} from './shared-service.service';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {takeUntil, tap} from 'rxjs/operators';
import {Subject} from 'rxjs';
import {StringSupport} from '../../../a4g-common/utility/string-support';
import {DateSupport} from "../../../a4g-common/utility/date-support";
import {FiltroRicercaRichiesteAccessoSistema} from "./dto/filtro-ricerca-richieste-accesso-sistema";
import {Localization} from "../../../a4g-common/utility/localization";

@Component({
  selector: 'app-richieste-accesso-sistema',
  templateUrl: './richieste-accesso-sistema.component.html',
  styleUrls: ['./richieste-accesso-sistema.component.css']
})
export class RichiesteAccessoSistemaComponent implements OnInit {

  public nome: string;
  public cognome: string;
  public codiceFiscale: string;
  public idProtocollo: string;
  public rangeDates: Date[];
  public labels = Labels;
  public itemBadges: Array<StatoRichiestaCount>;
  public reformattedItemBadges: {} = {};
  public selectedTab: String;
  private componentDestroyed$: Subject<boolean> = new Subject();
  private tabSelected: String;
  private minDateProtocollazioneFilter: string;
  private maxDateProtocollazioneFilter: string;
  public language: any = Localization.itCalendar();
  public openSearchTab: number = 0;

  constructor(private utentiService: UtentiService,
              private messages: MessageService,
              private sharedService: SharedService,
              private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit() {
    this.route.queryParams.pipe(
      takeUntil(this.componentDestroyed$),
      tap((queryParams: Params) => this.aggiornaParametri(queryParams))
    ).subscribe((resp) => {
        console.log(resp);
    }, error => {
      console.log(error);
    });

    this.loadCounters();
    this.sharedService.currentTimersForChangeCounters.subscribe(time => {
        console.log('Approvazione/Rifiuto domanda', time);
        this.loadCounters();
    });
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  public handleTabChange(event) {
    switch (event.index) {
      case 0:
          this.tabSelected = 'protocollate';
          break;
      case 1:
          this.tabSelected = 'inlavorazione';
          break;
      case 2:
          this.tabSelected = 'approvate';
          break;
      case 3:
          this.tabSelected = 'rifiutate';
          break;
    }
    const queryParams: Params = {
        tabselected: this.tabSelected,
        page: 0,
        sortOrder: null,
        sortProperty: null
    };
    this.router.navigate(
      [], 
      {
        queryParams: queryParams, 
        queryParamsHandling: 'merge'
      });
  }

  public eseguiRicercaSuTuttiTab() {
    this.loadCounters();
    const tabDaPassare: string = this.creaTabDaPassare();
    this.router.navigate(
        [],
        {
            queryParams: this.creaQueryParams(tabDaPassare),
            queryParamsHandling: 'merge',
        });
  }



  private calcolaMinMaxDateOfRangeDataProtocollazioneFilter() {
      if (!this.rangeDates || this.rangeDates.length == 0) {
          this.minDateProtocollazioneFilter = null;
          this.maxDateProtocollazioneFilter = null;
      } else if(this.rangeDates.length == 2 && !this.rangeDates[1]) {
          this.minDateProtocollazioneFilter = DateSupport.convertToPatternDate(this.rangeDates[0]);
          this.maxDateProtocollazioneFilter = DateSupport.convertToPatternDate(this.rangeDates[0]);
      } else {
          this.minDateProtocollazioneFilter = DateSupport.convertToPatternDate(this.rangeDates[0]);
          this.maxDateProtocollazioneFilter = DateSupport.convertToPatternDate(this.rangeDates[1]);
      }
  }

  private aggiornaParametri(queryParams: Params) {
      this.nome = queryParams['nome'];
      this.cognome = queryParams['cognome'];
      this.codiceFiscale = queryParams['codiceFiscale'];
      this.idProtocollo = queryParams['idProtocollo'];
      this.openSearchTab = queryParams['openSearchTab'];
      let dataIn: Date = DateSupport.convertStringToOnlyDate(queryParams['dataInizio'])
      let datafn: Date = DateSupport.convertStringToOnlyDate(queryParams['dataFine'])
      if (dataIn) {
          this.rangeDates = [dataIn, datafn];
      } else {
          this.rangeDates = null;
      }
      const tmpSelectedTab: string = queryParams['tabselected'];
      switch (tmpSelectedTab) {
          case 'protocollate':
              this.selectedTab = 'PROTOCOLLATE';
              break;
          case 'inlavorazione':
              this.selectedTab = 'IN_LAVORAZIONE';
              break;
          case 'approvate':
              this.selectedTab = 'APPROVATE';
              break;
          case 'rifiutate':
              this.selectedTab = 'RIFIUTATE';
              break;
      }
  }

  private creaQueryParams(tabDaPassare: string): Params {
      const queryParams: Params = {
        tabselected: tabDaPassare,
        page: 0,
        filter: 0
      };
      if (StringSupport.isNotEmpty(this.nome)) {
          queryParams.nome = this.nome;
      } else {
          queryParams.nome = null;
      }
      if (StringSupport.isNotEmpty(this.cognome)) {
          queryParams.cognome = this.cognome;
      } else {
          queryParams.cognome = null;
      }
      if (StringSupport.isNotEmpty(this.codiceFiscale)) {
          queryParams.codiceFiscale = this.codiceFiscale;
      } else {
          queryParams.codiceFiscale = null;
      }
      if (StringSupport.isNotEmpty(this.idProtocollo)) {
          queryParams.idProtocollo = this.idProtocollo;
      } else {
          queryParams.idProtocollo = null;
      }
      if (StringSupport.isNotEmpty(this.minDateProtocollazioneFilter)) {
          queryParams.dataInizio = this.minDateProtocollazioneFilter;
      } else {
          queryParams.dataInizio = null;
      }
      if (StringSupport.isNotEmpty(this.maxDateProtocollazioneFilter)) {
          queryParams.dataFine = this.maxDateProtocollazioneFilter;
      } else {
          queryParams.dataFine = null;
      }
      queryParams.openSearchTab = 1;
      return queryParams;
  }

  private creaTabDaPassare(): string {
      let tabDaPassare: string;
      switch (this.selectedTab) {
          case 'PROTOCOLLATE':
              tabDaPassare = 'protocollate';
              break;
          case 'IN_LAVORAZIONE':
              tabDaPassare = 'inlavorazione';
              break;
          case 'APPROVATE':
              tabDaPassare = 'approvate';
              break;
          case 'RIFIUTATE':
              tabDaPassare = 'rifiutate';
              break;
          default:
              tabDaPassare = 'protocollate';
              break;
      }
      return tabDaPassare;
  }

    private loadCounters(): void {
        this.calcolaMinMaxDateOfRangeDataProtocollazioneFilter();
        this.itemBadges = new Array<StatoRichiestaCount>();
        let filtro: FiltroRicercaRichiesteAccessoSistema = this.calcolaFiltroPerRicercaContatori();
        this.utentiService.getStatoRichiesteCount(filtro).subscribe(element => {
            element.forEach(item => {
                this.itemBadges.push(item);
            });
            this.reformattedItemBadges = this.itemBadges.reduce(function(map, tag) {
                map[tag.stato] = tag.count;
                return map;
            }, {});
        }, err => {
            this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        });
    }

    private calcolaFiltroPerRicercaContatori(): FiltroRicercaRichiesteAccessoSistema {
        let filtro: FiltroRicercaRichiesteAccessoSistema = FiltroRicercaRichiesteAccessoSistema
            .of(null, null,
                this.codiceFiscale, this.nome,
                this.cognome, this.idProtocollo,
                this.minDateProtocollazioneFilter, this.maxDateProtocollazioneFilter);
        return filtro;
    }
}
