import { ViewChild, Directive } from '@angular/core';
import {GestioneUtenzeService} from "../../../gestione-utenze.service";
import {MessageService} from "primeng/api";
import {SharedService} from "../../shared-service.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {A4gMessages, A4gSeverityMessage} from "../../../../../a4g-common/a4g-messages";
import {Labels} from "../../../../../app.labels";
import {RichiesteAccessoSistemaElenco} from "../../dto/RichiesteAccessoSistemaElenco";
import {Subject} from "rxjs";
import {RichiesteAccessoSistema} from "../../dto/RichiesteAccessoSistema";
import {FiltroRicercaRichiesteAccessoSistema} from "../../dto/filtro-ricerca-richieste-accesso-sistema";
import {Paginazione} from "../../../../../a4g-common/utility/paginazione";
import {debounceTime, distinctUntilChanged, tap, takeUntil} from "rxjs/operators";

@Directive()
export abstract class RichiesteAccessoSistemaTabComponent {
  public textTitle: string;
  public cols: any[];
  public intestazioni = Labels;
  public elementiPerPagina = 10;
  public numeroPagina: number = 0;
  public inputFilter: string = '';
  public paginaDomande: RichiesteAccessoSistemaElenco;
  public proprieta: string = '';
  public ordine: string = '';
  public inputFilterUpdate = new Subject<string>();
  protected componentDestroyed$: Subject<boolean> = new Subject();
  protected stato: string;
  protected nome: string;
  protected cognome: string;
  protected codiceFiscale: string;
  protected idProtocollazione: string;
  protected dataInizio: string;
  protected dataFine: string;

  protected defaultSortingColumn: string = undefined;
  protected defaultSortingOrder: string = undefined;

  @ViewChild('table', { static: true }) table;

  constructor(
    protected gestioneUtenzeService: GestioneUtenzeService,
    protected messages: MessageService,
    protected sharedService: SharedService,
    protected router: Router,
    protected route: ActivatedRoute) {

      // inizializzazione del filtro testo generico
      this.inputFilterUpdate.pipe(
        debounceTime(400),
        distinctUntilChanged()).subscribe(value => {
          this.inputFilter = value;
          this.updatePageParameter(0, value, this.proprieta, this.ordine);
      });
  }
    
  protected abstract setCols();

  public viewDetail(id: number) {
    this.router.navigate(['./gestioneUtenze/:idUtenza/dettaglioUtenza'.replace(':idUtenza', id.toString())],
        { relativeTo: this.route.parent.parent });
  }

  public updatePageParameter(pagina: Number, inputFilter: string, sortProperty: string, sortOrder: string) {
    const queryParams: Params = { 
      inputFilters: inputFilter,
      page: pagina,
      sortOrder: sortOrder,
      sortProperty: sortProperty
    };
    this.router.navigate(
        [],
        {
          replaceUrl: true,
          queryParams: queryParams,
          queryParamsHandling: 'merge'
        });
  }

  public getElencoRichieste(event: any) {
    if (event != null && event.sortField != null) {
      this.proprieta = event.sortField;
      this.ordine = this.gestioneUtenzeService.getOrdine(event.sortOrder);
    }
    this.numeroPagina = Math.floor(this.table.first / this.elementiPerPagina);
    this.updatePageParameter(this.numeroPagina, this.inputFilter, this.proprieta, this.ordine);
  }

  protected refreshElencoRichieste(): void {
    let filtro: FiltroRicercaRichiesteAccessoSistema = FiltroRicercaRichiesteAccessoSistema.of(
      this.inputFilter, this.stato, this.codiceFiscale, this.nome, this.cognome,
      this.idProtocollazione, this.dataInizio, this.dataFine);
    
    let sortProperty = this.proprieta;
    if (sortProperty && (sortProperty === 'dtApprovazione' || sortProperty === 'dtRifiuto')) {
        sortProperty = 'istruttoriaEntita.dataTermineIstruttoria';
    }
    let paginazione: Paginazione = Paginazione.of(
        this.numeroPagina,
        this.elementiPerPagina,
        sortProperty,
        this.ordine);
    this.sharedService.changeCountersForApproveOrDanied();
    this.gestioneUtenzeService.getRichiesteAccessoSistemaPerStato(filtro, paginazione).subscribe(
        dati => this.gestisciRispostaRicarcaAccessoSistema(dati),
        err => this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati))
    );
  }

  protected setupRouting(tabName: string): void {
    this.route.queryParams.pipe(
      tap((queryParams) => {
        const page: string = queryParams['page'];
        if (page) {
          this.numeroPagina = Number.parseInt(page);
        }
        this.ordine = queryParams['sortOrder'];
        this.proprieta = queryParams['sortProperty'];
        this.inputFilter = queryParams['inputFilters'];
        const tmpSelectedTab: string = queryParams['tabselected'];
        if (tmpSelectedTab === tabName) {
          this.valorizzaParametriRicercaFrom(queryParams);
        }
        this.refreshElencoRichieste();
      }),
      takeUntil(this.componentDestroyed$)
    ).subscribe((resp) => {
    }, error => {
    });
    this.paginaDomande = new RichiesteAccessoSistemaElenco();
    this.paginaDomande.risultati = [];
  }

  protected valorizzaParametriRicercaFrom(params: Params) {
    if (params != null) {
      this.codiceFiscale = params['codiceFiscale'];
      this.cognome = params['cognome'];
      this.nome = params['nome'];
      this.idProtocollazione = params['idProtocollo'];
      this.dataInizio = params["dataInizio"];
      this.dataFine = params["dataFine"];
      if (!this.ordine) {
        this.ordine = this.defaultSortingOrder;
      }
      if (!this.proprieta) {
        this.proprieta = this.defaultSortingColumn;
      }
    }
  }

  private gestisciRispostaRicarcaAccessoSistema(dati: RichiesteAccessoSistemaElenco): void {
    let risultati: RichiesteAccessoSistema[] = [];
    if (dati) {
      dati.risultati.forEach(element => risultati.push(element));
      this.paginaDomande.count = dati.count;
      this.paginaDomande.risultati = risultati;
    }
  }

}
