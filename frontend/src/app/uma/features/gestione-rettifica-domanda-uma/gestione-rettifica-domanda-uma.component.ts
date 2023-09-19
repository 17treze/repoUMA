import { HttpClientDichiarazioneConsumiUmaService } from 'src/app/uma/core-uma/services/http-client-dichiarazione-consumi-uma.service';
import { DomandaUmaFilter } from 'src/app/uma/core-uma/models/dto/DomandaUmaFilter';
import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { Labels } from "src/app/app.labels";
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ConsultazioneUMA } from "src/app/uma/core-uma/models/dto/ConsultazioneUMA";
import { HttpClientDomandaUmaService } from "src/app/uma/core-uma/services/http-client-domanda-uma.service";
import { TipoIntestazioneUma } from 'src/app/a4g-common/classi/enums/uma/TipoDocumentoUma.enum';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { PaginatorEvent, Paginazione } from 'src/app/a4g-common/utility/paginazione';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { catchError, switchMap } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
import { DichiarazioneConsumiDto } from '../../core-uma/models/dto/DichiarazioneConsumiDto';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { Observable, EMPTY, of, forkJoin, Subscription } from 'rxjs';
import { PersonaAgsDto } from 'src/app/uma/core-uma/models/dto/PersonaAgsDto';
import { AnagraficaFascicoloService } from 'src/app/fascicolo/creazione-fascicolo/anagrafica-fascicolo.service';
import { Utente } from 'src/app/auth/user';
import { AuthService } from 'src/app/auth/auth.service';
import { DatiCAA } from 'src/app/fascicolo/creazione-fascicolo/dto/DatiSportelloCAA';
import { TipoRichiedenteUma } from 'src/app/a4g-common/classi/TipoRichiedenteUma-enum';
import { FascicoloCorrente } from 'src/app/fascicolo/fascicoloCorrente';
import * as _ from 'lodash';
import { RichiestaCarburanteBuilderService } from '../../core-uma/services/builders/richiesta-carburante-builder.service';
import { DichiarazioneConsumiFilter } from '../../core-uma/models/dto/DichiarazioneConsumiFilter';
import { FascicoloService } from 'src/app/fascicolo/fascicolo.service';
import { Fascicolo } from 'src/app/a4g-common/classi/Fascicolo';
import { FascicoloAgsDto } from 'src/app/a4g-common/classi/FascicoloAgsDto';
@Component({
  selector: 'app-gestione-rettifica-domanda-uma',
  templateUrl: './gestione-rettifica-domanda-uma.component.html',
  styleUrls: ['./gestione-rettifica-domanda-uma.component.scss']
})

export class GestioneRettificaDomandaUMAComponent implements OnInit, OnDestroy {
  componentDestroyed$: Subject<boolean> = new Subject();
  labels = Labels;
  intestazioni = Labels;
  datiDichiarazioni: Array<ConsultazioneUMA> = [];
  datiRichieste: Array<ConsultazioneUMA> = [];
  tipoDichiarazione: TipoIntestazioneUma = TipoIntestazioneUma.DICHIARAZIONE_CONSUMI;
  tipoRichiesta: TipoIntestazioneUma = TipoIntestazioneUma.RICHIESTA;
  elementiPerPagina: number = 5;
  nrDichiarazioni: number;
  nrRichieste: number;
  stati: Array<any>;
  anni: Array<any>;
  cuaa: string;
  tableReset: boolean;
  tipoIntestazioneEnum: typeof TipoIntestazioneUma;
  isRettificabile: boolean;
  idFascicoloCorrente: number;
  idDomandaRettificabile: number;
  dichiarazioniConsumi: Array<DichiarazioneConsumiDto>;

  readonly UMA_01_01_BR1_ERR_MSG = "Solamente il titolare o il rappresentante legale di un'azienda agricola è autorizzato alla presentazione della Richiesta di carburante on line";
  readonly UMA_FASCICOLO_NON_VALIDO = "Non è possibile reperire il fascicolo selezionato";
  readonly UMA_PERSONA_FISICA_DECEDUTA = "Il campo data decesso risulta valorizzato. Il sistema non permette l'inserimento di una rettifica di carburante";

  // Subscriptions
  listaDomandeSubscription: Subscription;
  getListeSubscription: Subscription;
  richiestaCarburanteSubscription: Subscription;
  routeSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private httpClientDomandaUmaService: HttpClientDomandaUmaService,
    private errrorService: ErrorService,
    private dateUtilService: DateUtilService,
    private fascicoloCorrente: FascicoloCorrente,
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private authService: AuthService,
    private richiestaCarburanteBuilderService: RichiestaCarburanteBuilderService,
    private httpClientDichiarazioneConsumiUmaService: HttpClientDichiarazioneConsumiUmaService,
    private router: Router,
    private errorService: ErrorService,
    private fascicoloService: FascicoloService
  ) {
  }

  ngOnInit() {
    this.tipoIntestazioneEnum = TipoIntestazioneUma;
    this.isRettificabile = false;
    this.getCuaa();
  }

  ngOnDestroy() {
    if (this.listaDomandeSubscription) {
      this.listaDomandeSubscription.unsubscribe();
    }
    if (this.getListeSubscription) {
      this.getListeSubscription.unsubscribe();
    }
    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
  }

  getRichieste(params: DomandaUmaFilter) {
    this.listaDomandeSubscription = this.httpClientDichiarazioneConsumiUmaService.getDichiarazioniConsumi(this.buildDichiarazioneConsumiFilterByAnno(params))
      .pipe(
        switchMap((dichiarazioni: Array<DichiarazioneConsumiDto>) => {
          this.dichiarazioniConsumi = dichiarazioni;
          return this.httpClientDomandaUmaService.getDomande(params);
        }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err, 'tst-csv');
          return EMPTY;
        })
      ).subscribe((result: Array<RichiestaCarburanteDto>) => {
        if (result && result.length && this.richiestaCarburanteBuilderService.isRettificabile(result, this.dichiarazioniConsumi)) {
          this.datiRichieste = [];
          this.nrRichieste = result.length;
          this.isRettificabile = true;
          this.idDomandaRettificabile = this.richiestaCarburanteBuilderService.getRettificabile(result, 'tst-csv').id;
        } else {
          this.isRettificabile = false;
        }
        const unsortedRichieste: Array<ConsultazioneUMA> = [];
        if (result && result.length) {
          result.forEach((richiesta: RichiestaCarburanteDto) => {
            unsortedRichieste.push(this.richiestaCarburanteBuilderService.richiestaCarburanteDtoToConsultazioneUmaViewBuilder(richiesta));
          })
          this.datiRichieste = _.orderBy(unsortedRichieste, ['dataPresentazione'], ['desc']); /** ordino con la più recente in alto */
        }
        this.tableReset = false;
      }, (error: ErrorDTO) => this.errrorService.showError(error, 'tst-csv'));
  }

  aggiornaTabella($event: { paginator: PaginatorEvent, tipo: TipoIntestazioneUma }) {
    this.getRichieste(this.buildFilter($event.paginator, $event.tipo));
  }

  getCuaa() {
    this.routeSubscription = this.route.params
      .subscribe((urlParams) => {
        this.cuaa = urlParams['cuaa'];
        // this.idFascicoloCorrente = urlParams['id'];
      });
    /*
    this.route.params
      .pipe(
        switchMap((params: Params) => {
          return forkJoin([this.fascicoloService.getFascicolo(params['idFascicolo']), this.fascicoloService.getLegacy(params['idFascicolo'])]);
        })).subscribe(([fascicolo, fascicoloLegacy]: [Fascicolo, FascicoloAgsDto]) => {
          if (fascicoloLegacy && fascicoloLegacy.cuaa != null) {
            this.fascicoloCorrente.fascicoloLegacy = fascicoloLegacy;
          }
          if (fascicolo && fascicolo.cuaa != null) {
            this.fascicoloCorrente.fascicolo = fascicolo;
          }
        }, error => this.errorService.showError(error));
     */
  }

  buildFilter(paginator: PaginatorEvent, tipo: TipoIntestazioneUma): DomandaUmaFilter {
    let filter = new DomandaUmaFilter();
    filter.cuaa = this.cuaa;
    filter.denominazione = null;
    filter.campagna = [this.dateUtilService.getCurrentYear().toString()];
    // Paginator
    filter.pagina = (paginator != null && Math.floor(paginator.first / this.elementiPerPagina) != null)
      ? Math.floor(paginator.first / this.elementiPerPagina) : 0;
    filter.numeroElementiPagina = this.elementiPerPagina;
    filter.proprieta = paginator != null ? paginator.sortField : 'id';
    filter.ordine = paginator != null ? Paginazione.getOrdine(paginator.sortOrder) : 'ASC';
    return filter;
  }

  rettificaDomanda(nrDomanda: number) {
    if (!this.isFascicoloSelezionato()) {
      return;
    }

    const cuaa = this.cuaa;
    // const getTitRapprLeg$: Observable<Array<PersonaAgsDto>> = this.anagraficaFascicoloService.getTitolariRappresentantiLegali(cuaa);
    const loggedUser$: Observable<Utente> = this.authService.getUserFromSession();

    this.richiestaCarburanteSubscription = this.anagraficaFascicoloService.getDatiSportelloCAA()
      .pipe(
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err, 'tst-csv');
          return EMPTY;
        }),
        switchMap((caa: DatiCAA) => {
          const hasCAA: boolean = caa != null ? true : false;
          console.log('Ruolo selezionato in Home: ', localStorage.getItem('selectedRole'));
          if (localStorage.getItem('selectedRole') == AuthService.roleCaa && hasCAA) {  // -> ENTE
            return forkJoin([loggedUser$, of('ENTE')]);
          } else { // se non ho caa associati o se non ho selezionato il ruolo caa -> UTENTE
            return forkJoin([loggedUser$, of('UTENTE')]);
          }
        }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err, 'tst-csv');
          return EMPTY;
        }),
        switchMap(([titolariRapprOrUtente, tipo]: [Array<PersonaAgsDto> | Utente, string]) => {
          if (tipo === 'UTENTE') {  // -> azienda
            return this.httpClientDomandaUmaService.presentaDomanda(cuaa, (titolariRapprOrUtente as Utente).codiceFiscale);
          } else {                  // tipo == ENTE -> caa
            // if (titolariRapprOrUtente == null || !(titolariRapprOrUtente as Array<PersonaAgsDto>).length) {
            //   this.errorService.showErrorWithMessage(this.UMA_01_01_BR1_ERR_MSG);
            //   return EMPTY;
            // }
            this.router.navigate([`uma/${this.cuaa}/richiedente/${TipoRichiedenteUma.richiesta}`]);
            return EMPTY;
          }
        })
      ).subscribe((idRettifica: number) => {
        // Nel caso UTENTE è' stata creata una nuova rettifica uma per il cuaa fornito
        this.router.navigate([`uma/${this.cuaa}/richiesta`, idRettifica]);
      }, (error: ErrorDTO) => this.errorService.showError(error));
  }

  private isFascicoloSelezionato(): boolean {
    console.log('this.cuaa: ' + this.cuaa);
    if (!this.cuaa) {    
    // if (!this.fascicoloCorrente || !this.fascicoloCorrente.fascicolo || !this.fascicoloCorrente.fascicolo.cuaa) {
      this.errorService.showErrorWithMessage(this.UMA_FASCICOLO_NON_VALIDO);
      return false;
    } else return true;
  }

  private buildDichiarazioneConsumiFilterByAnno(params: DomandaUmaFilter): DichiarazioneConsumiFilter {
    const dichiarazioneFilter = new DichiarazioneConsumiFilter();
    dichiarazioneFilter.campagna = params.campagna;
    dichiarazioneFilter.cuaa = params.cuaa;
    // Paginator
    dichiarazioneFilter.pagina = 0;
    dichiarazioneFilter.numeroElementiPagina = this.elementiPerPagina;
    dichiarazioneFilter.proprieta = 'dataPresentazione';
    dichiarazioneFilter.ordine = 'DESC';
    return dichiarazioneFilter;
  }

}
