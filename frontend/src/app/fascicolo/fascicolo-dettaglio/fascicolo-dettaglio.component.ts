import { DichiarazioneConsumiFilter } from './../../uma/core-uma/models/dto/DichiarazioneConsumiFilter';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FascicoloDaCuaa } from 'src/app/a4g-common/classi/FascicoloCuaa';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { FascicoloDettaglioService } from '../fascicolo-dettaglio-container/fascicolo-dettaglio.service';
import { MandatoService } from '../mandato.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AnagraficaFascicoloService } from '../creazione-fascicolo/anagrafica-fascicolo.service';
import { StatoFascicoloEnum } from '../creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import { catchError, switchMap, takeUntil } from 'rxjs/operators';
import { Subject, Observable, EMPTY, of, forkJoin, Subscription } from 'rxjs';
import { PersonaAgsDto } from 'src/app/uma/core-uma/models/dto/PersonaAgsDto';
import { Utente } from 'src/app/auth/user';
import { HttpClientDomandaUmaService } from 'src/app/uma/core-uma/services/http-client-domanda-uma.service';
import { AuthService } from 'src/app/auth/auth.service';
import { DatiCAA } from '../creazione-fascicolo/dto/DatiSportelloCAA';
import { DtoBuilderService } from 'src/app/uma/core-uma/services/dto-builder.service';
import { TipoRichiedenteUma } from 'src/app/a4g-common/classi/TipoRichiedenteUma-enum';
import { FascicoloService } from '../fascicolo.service';
import { DichiarazioneConsumiDto } from 'src/app/uma/core-uma/models/dto/DichiarazioneConsumiDto';
import { HttpClientDichiarazioneConsumiUmaService } from 'src/app/uma/core-uma/services/http-client-dichiarazione-consumi-uma.service';
import * as _ from 'lodash';
import { StatoDichiarazioneConsumiEnum } from 'src/app/a4g-common/classi/enums/uma/StatoDichiarazioneConsumi.enum';
import { environment } from 'src/environments/environment';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { FascicoloCorrente } from '../fascicoloCorrente';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { MyappagService } from 'src/app/a4g-common/a4g-integration/myappag-service';
import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { DomandaUmaFilter } from 'src/app/uma/core-uma/models/dto/DomandaUmaFilter';
import { StatoDomandaUma } from 'src/app/a4g-common/classi/enums/uma/StatoDomandaUma.enum';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
import { EredeDto } from '../creazione-fascicolo/dto/EredeDto';
import { FascicoloAgsDto } from 'src/app/a4g-common/classi/FascicoloAgsDto';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { CarburanteDto } from 'src/app/uma/core-uma/models/dto/CarburanteDto';
import { FormatConverterService } from 'src/app/uma/shared-uma/services/format-converter.service';
import { CarburanteTotale } from 'src/app/uma/core-uma/models/dto/CarburanteTotale';
import { PrelievoDto } from 'src/app/uma/core-uma/models/dto/PrelievoDto';
import { DichiarazioneAntimafia } from '../antimafia/classi/dichiarazioneAntimafia';
import { Azienda } from '../antimafia/classi/azienda';
import { AntimafiaService } from '../antimafia/antimafia.service';
import { StatoDichiarazione } from '../antimafia/classi/statoDichiarazioneEnum';

@Component({
  selector: 'app-fascicolo-dettaglio',
  templateUrl: './fascicolo-dettaglio.component.html',
  styleUrls: ['./fascicolo-dettaglio.component.css']
})
export class FascicoloDettaglioComponent implements OnInit, OnDestroy {
  public idFascicolo: number;
  public idMandato: number;
  public fascicoloCorrente: FascicoloDaCuaa;
  public popupSchedaValidazioneOpen = false;
  public isPopupPassaAStatoOpen = false;
  public isPopupControlloCompletezzaOpen = false;
  public popupSchedaValidazioneFirmaCaaAziendaOpen = false;
  public isMyappagEnabled = false;
  public statoFascicoloEnum = StatoFascicoloEnum;
  public eredi: EredeDto[];
  protected componentDestroyed$: Subject<boolean> = new Subject();
  private idValidazione: number = undefined;
  dichiarazioniAntimafia: DichiarazioneAntimafia[];
  readonly UMA_01_01_BR1_ERR_MSG = "Solamente il titolare o il rappresentante legale di un'azienda agricola è autorizzato alla presentazione della Richiesta di carburante on line";
  readonly UMA_FASCICOLO_NON_VALIDO = "Non è possibile reperire il fascicolo selezionato";
  readonly UMA_PERSONA_FISICA_DECEDUTA = "Il campo data decesso risulta valorizzato. Il sistema non permette l'inserimento di una richiesta di carburante";

  production = environment.production;

  richiestaCarburanteSubscription: Subscription;
  dichiarazioneConsumiSubscription: Subscription;
  getFascicoloAgsByCuaaSub: Subscription;
  prelieviAndRichiesteSubscription: Subscription;
  getDichiarazioniAntimafiaSub: Subscription;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private fascicoloCorrenteUMA: FascicoloCorrente,
    private errorService: ErrorService,
    private mandatoService: MandatoService,
    private httpClientDomandaUmaService: HttpClientDomandaUmaService,
    private httpClientDichiarazioneConsumiUma: HttpClientDichiarazioneConsumiUmaService,
    private authService: AuthService,
    private dtoBuilderService: DtoBuilderService,
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private fascicoloDettaglioService: FascicoloDettaglioService,
    private fascicoloService: FascicoloService,
    private myappagService: MyappagService,
    private dateUtilService: DateUtilService,
    private formatConverterService: FormatConverterService,
    private antimafiaService: AntimafiaService
  ) { }

  ngOnInit() {
    this.isMyappagEnabled = this.myappagService.isMyappagEnabled;
    this.subscribeStatoFascicolo();
    this.route.queryParams.pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(queryParams => {
      const paramIdVal: string = queryParams['id-validazione'];
      if (paramIdVal) {
        this.idValidazione = Number.parseInt(paramIdVal);
      } else {
        this.idValidazione = 0;
      }
    });
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
    if (this.richiestaCarburanteSubscription) {
      this.richiestaCarburanteSubscription.unsubscribe();
    }
    if (this.dichiarazioneConsumiSubscription) {
      this.dichiarazioneConsumiSubscription.unsubscribe();
    }
    if (this.getFascicoloAgsByCuaaSub) {
      this.getFascicoloAgsByCuaaSub.unsubscribe();
    }
    if (this.prelieviAndRichiesteSubscription) {
      this.prelieviAndRichiesteSubscription.unsubscribe();
    }
    if (this.getDichiarazioniAntimafiaSub) {
      this.getDichiarazioniAntimafiaSub.unsubscribe();
    }
  }

  private subscribeStatoFascicolo() {
    this.fascicoloDettaglioService.fascicoloCorrente.pipe(
      takeUntil(this.componentDestroyed$))
      .subscribe((fascicoloCorrente: FascicoloDaCuaa) => {
        console.log("subscribeStatoFascicolo()");
        this.fascicoloCorrente = fascicoloCorrente;
        if (this.fascicoloCorrente !== null) {
          this.idFascicolo = this.fascicoloCorrente.id;
          this.idMandato = this.fascicoloCorrente.mandatoDto.id;
          this.caricaIdFascicoloUMA();
        }
      });
  }

  public goToFascicoliValidati() {
    this.router.navigate(
      ['./fascicoli-validati'], { relativeTo: this.route });
  }

  public getMandato() {
    this.mandatoService.getMandatoFile(this.idFascicolo, this.idMandato, this.idValidazione).subscribe(
      result => {
        let mandatoFile: Blob = new Blob([result], { type: 'application/pdf' });
        if (mandatoFile != null) {
          const fileURL = URL.createObjectURL(mandatoFile);
          window.open(fileURL);
        } else {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, A4gMessages.erroreVisualizzazioneMandato));
        }
      }, err => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, A4gMessages.erroreVisualizzazioneMandato));
      });
  }

  public handleClick(redirect: string) {
    const cuaa = this.fascicoloCorrente.cuaa;
    if (cuaa.length === 16 || cuaa.length === 11) {
      console.log('Redirect to dati azienda... link: ' + redirect);
      // this.fascicoloDettaglioService.mostraDettaglioSezione.next(FascicoloDettaglio.DATI_AZIENDA);
      this.router.navigate([redirect], {
        relativeTo: this.route,
        queryParams: this.idValidazione ? { 'id-validazione': this.idValidazione } : {},
        queryParamsHandling: 'merge'
      });
    } else {
      this.messageService.add(A4gMessages.getToast(
        'tst', A4gSeverityMessage.error, 'WIP'));
    }
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
  }

  public setAllaFirmaAzienda(cuaa: string) {
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
  }

  public setInAggiornamento(cuaa: string) {
    this.anagraficaFascicoloService.putSetStatusFascicoloInAggiornamento(cuaa)
      .subscribe(
        () => {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.success, A4gMessages.changeStatusInAggiornamentoOk));
          this.fascicoloCorrente.stato = StatoFascicoloEnum.IN_AGGIORNAMENTO;
          this.reloadDataFascicolo();
        }, err => {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, A4gMessages.changeStatusInAggiornamentoKo));
        });
  }

  public sincronizzazione() {
    console.log('Visualizzazione sincronizzazione');
  }

  public anomalie() {
    console.log('Visualizzazione anomalie');
  }

  public linkPagina() {
    return this.router.navigate(['./'], { relativeTo: this.route });
  }

  public onClickGestioneCarburanteInEsubero() {
    if (!this.isFascicoloSelezionato()) {
      return;
    }

    const campagna = this.dateUtilService.getCurrentYear().toString();
    const cuaa = this.fascicoloCorrente.cuaa;

    const richiestaFilter: DomandaUmaFilter = {
      cuaa: cuaa,
      campagna: [campagna],
      stati: [StatoDomandaUma.AUTORIZZATA],
      numeroElementiPagina: 1,
      pagina: 0
    };

    const dichiarazioneFilter: DichiarazioneConsumiFilter = {
      cuaa: cuaa,
      campagna: [campagna],
      numeroElementiPagina: 1,
      pagina: 0
    }

    this.prelieviAndRichiesteSubscription = forkJoin([
      this.httpClientDomandaUmaService.getDomande(richiestaFilter),
      this.httpClientDichiarazioneConsumiUma.getDichiarazioniConsumi(dichiarazioneFilter),
      this.httpClientDichiarazioneConsumiUma.getResiduoAnnoPrecedente(cuaa, this.dateUtilService.getCurrentYear() - 1, [StatoDichiarazioneConsumiEnum.PROTOCOLLATA]),
      this.httpClientDomandaUmaService.getPrelieviByCuaaAndCampagna(cuaa, campagna)
    ]).subscribe(([richieste, dichiarazioni, dichiarazioniAnnoPrecedente, prelievi]: [Array<RichiestaCarburanteDto>, Array<DichiarazioneConsumiDto>, Array<DichiarazioneConsumiDto>, CarburanteTotale<PrelievoDto>]) => {
      // richiesta autorizzata
      if (!richieste || !richieste.length) {
        this.errorService.showErrorWithMessage(UMA_MESSAGES.richiesteNotPresent, 'tst');
        return;
      }
      const idRichiestaCarburante = richieste[0].id;
      // dichairazione consumi se esiste non deve essere protocollata
      if (dichiarazioni && dichiarazioni.length) {
        const dichiarazioneConsumi = dichiarazioni.pop();
        if (StatoDichiarazioneConsumiEnum.PROTOCOLLATA === dichiarazioneConsumi.stato) {
          this.errorService.showErrorWithMessage(UMA_MESSAGES.esisteDichiarazioneConsumiAutorizzata, 'tst');
          return;
        }
      }
      let carburanteInEsubero: CarburanteDto = new CarburanteDto();
      carburanteInEsubero.benzina = 0;
      carburanteInEsubero.gasolio = 0;
      carburanteInEsubero.gasolioSerre = 0;

      // recupero residuo anno precedente
      if (!dichiarazioniAnnoPrecedente || !dichiarazioniAnnoPrecedente.length) {
        console.log(UMA_MESSAGES.dichiarazioneAnnoPrecedenteNotDisponibile);
      } else {
        const dichiarazioneConsumiPrecedente = dichiarazioniAnnoPrecedente.pop();
        const residuo = (dichiarazioneConsumiPrecedente && dichiarazioneConsumiPrecedente.rimanenza) ? dichiarazioneConsumiPrecedente.rimanenza : null;
        if (residuo == null) {
          console.log(UMA_MESSAGES.residuoDichiarazioneAnnoPrecedenteNotDisponibile);
        } else {
          carburanteInEsubero = residuo;
        }
      }

      // recupero prelievi
      if (!prelievi || !prelievi.totale) {
        console.log(UMA_MESSAGES.prelieviNotDisponibile);
      } else {
        carburanteInEsubero.benzina += this.formatConverterService.toNumber(prelievi.totale.benzina);
        carburanteInEsubero.gasolio += this.formatConverterService.toNumber(prelievi.totale.gasolio);
        carburanteInEsubero.gasolioSerre += this.formatConverterService.toNumber(prelievi.totale.gasolioSerre);
      }

      // se carburante in esubero > 0 => navigate else toast errore
      console.log("Carburante in esubero ", carburanteInEsubero);
      if (carburanteInEsubero && (carburanteInEsubero.benzina > 0 || carburanteInEsubero.gasolio > 0 || carburanteInEsubero.gasolioSerre > 0)) {
        this.router.navigate([`uma/${this.fascicoloCorrenteUMA.fascicoloLegacy.cuaa}/esubero-carburante/${idRichiestaCarburante}`]);
      } else {
        this.errorService.showErrorWithMessage(UMA_MESSAGES.erroreCarburanteInEsubero, 'tst');
        return;
      }
    }, (error: ErrorDTO) => {
      this.messageService.clear(); /** rimozione messaggio di abilitazioni su 403 dell'interceptor */
      this.errorService.showErrorWithMessage(UMA_MESSAGES.erroreModificaCarburanteInEsubero, 'tst');
    });
  }

  public onClickRichiestaRettificaUma() {
    if (!this.isFascicoloSelezionato()) {
      return;
    }
    const cuaa = this.fascicoloCorrente.cuaa;
    const getTitRapprLeg$: Observable<Array<PersonaAgsDto>> = this.anagraficaFascicoloService.getTitolariRappresentantiLegali(cuaa);
    const loggedUser$: Observable<Utente> = this.authService.getUserFromSession();
    const richieste$: Observable<Array<RichiestaCarburanteDto>> = this.httpClientDomandaUmaService.getDomande(this.dtoBuilderService.buildDomandaUmaFilterWithAllStates(cuaa));

    this.richiestaCarburanteSubscription = this.anagraficaFascicoloService.getDatiSportelloCAA()
      .pipe(
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err);
          return EMPTY;
        }),
        switchMap((caa: DatiCAA) => {
          const hasCAA: boolean = caa != null ? true : false;
          console.log('Ruolo selezionato in Home: ', localStorage.getItem('selectedRole'));
          if (localStorage.getItem('selectedRole') == AuthService.roleCaa && hasCAA) {  // -> ENTE
            return forkJoin([getTitRapprLeg$, of('ENTE'), richieste$]);
          } else if (localStorage.getItem('selectedRole') == AuthService.roleAdmin) { // se non ho caa associati o se non ho selezionato il ruolo caa -> UTENTE Controllo se è effetivamente un' azienda per evitare accessi da utenze non permesse
            return forkJoin([loggedUser$, of('AMMINISTRATORE'), richieste$]);
          } else if (localStorage.getItem('selectedRole') == AuthService.rolePrivate) { // se non ho caa associati o se non ho selezionato il ruolo caa -> UTENTE Controllo se è effetivamente un' azienda per evitare accessi da utenze non permesse
            return forkJoin([loggedUser$, of('UTENTE'), richieste$]);
          }
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.UTENTE_NON_ABILITATO));
          return EMPTY;
        }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err);
          return EMPTY;
        }),
        switchMap(([titolariRapprOrUtente, tipo, richieste]: [Array<PersonaAgsDto> | Utente, string, Array<RichiestaCarburanteDto>]) => {
          if (tipo === 'UTENTE') { // -> azienda
            if (!richieste || !richieste.length) {
              return this.httpClientDomandaUmaService.presentaDomanda(cuaa, (titolariRapprOrUtente as Utente).codiceFiscale);
            } else {
              return of(richieste);
            }
          } else { // tipo == ENTE -> caa
            if (!richieste || !richieste.length) {
              // Se titolare deceduto in fase di creazione di una nuova richiesta -> non faccio la redirect e lancio il toast di errore
              if (this.fascicoloCorrenteUMA.isPersonaFisica(cuaa) && this.fascicoloCorrenteUMA.fascicoloLegacy?.dataMorteTitolare != null) {
                this.errorService.showErrorWithMessage(this.UMA_PERSONA_FISICA_DECEDUTA);
                return EMPTY;
              }
              if (titolariRapprOrUtente == null || !(titolariRapprOrUtente as Array<PersonaAgsDto>).length) {
                this.errorService.showErrorWithMessage(this.UMA_01_01_BR1_ERR_MSG);
                return EMPTY;
              }
              this.router.navigate([`uma/${this.fascicoloCorrenteUMA.fascicoloLegacy.cuaa}/richiedente/${TipoRichiedenteUma.richiesta}`]);
              return EMPTY;
            } else {
              return of(richieste);
            }
          }
        })
      ).subscribe((idDomandaOrDomande: number | Array<RichiestaCarburanteDto>) => {
        if (typeof idDomandaOrDomande === "number") {
          // E' stata creata una nuova domanda uma per il cuaa fornito
          this.router.navigate([`uma/${this.fascicoloCorrenteUMA.fascicoloLegacy.cuaa}/richiesta`, idDomandaOrDomande]);
        } else {
          // Esiste già almeno una richiesta o rettifica per il cuaa fornito
          this.router.navigate([`./fascicolo/${this.fascicoloCorrenteUMA.fascicoloLegacy.cuaa}/rettifiche/${this.fascicoloCorrenteUMA.fascicoloLegacy.cuaa}`], { relativeTo: this.route.parent.parent.parent });
        }
      }, (error: ErrorDTO) => this.errorService.showError(error));
  }

  public onClickDichiarazioneConsumi() {
    if (!this.isFascicoloSelezionato()) {
      return;
    }
    const cuaa = this.fascicoloCorrente.cuaa;
    const getTitRapprLeg$: Observable<Array<PersonaAgsDto>> = this.anagraficaFascicoloService.getTitolariRappresentantiLegali(cuaa);
    const loggedUser$: Observable<Utente> = this.authService.getUserFromSession();
    // Cerco la dichiarazione consumi per cuaa
    let stati = new Array<StatoDichiarazioneConsumiEnum>();
    stati.push(StatoDichiarazioneConsumiEnum.IN_COMPILAZIONE);
    stati.push(StatoDichiarazioneConsumiEnum.PROTOCOLLATA);
    const filter = new DichiarazioneConsumiFilter();
    filter.stati = stati;
    filter.cuaa = cuaa;
    const dichiarazioniConsumi$: Observable<Array<DichiarazioneConsumiDto>> = this.httpClientDichiarazioneConsumiUma.getDichiarazioniConsumi(filter);

    let domandaUmaFilter = new DomandaUmaFilter();
    domandaUmaFilter.campagna = [this.dateUtilService.getCurrentYear().toString()];
    domandaUmaFilter.cuaa = cuaa;
    let statiR = new Array<StatoDomandaUma>();
    statiR.push(StatoDomandaUma.AUTORIZZATA);
    domandaUmaFilter.stati = statiR;

    const richieste$: Observable<Array<RichiestaCarburanteDto>> = this.httpClientDomandaUmaService.getDomande(domandaUmaFilter);

    this.dichiarazioneConsumiSubscription = this.anagraficaFascicoloService.getDatiSportelloCAA()
      .pipe(
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err);
          return EMPTY;
        }),
        switchMap((caa: DatiCAA) => {
          const hasCAA: boolean = caa != null ? true : false;
          console.log('Ruolo selezionato in Home: ', localStorage.getItem('selectedRole'));
          if (localStorage.getItem('selectedRole') == AuthService.roleCaa && hasCAA) {  // -> ENTE
            return forkJoin([getTitRapprLeg$, of('ENTE'), dichiarazioniConsumi$, richieste$]);
          } else if (localStorage.getItem('selectedRole') == AuthService.roleAdmin) { // se non ho caa associati o se non ho selezionato il ruolo caa -> UTENTE Controllo se è effetivamente un' azienda per evitare accessi da utenze non permesse
            return forkJoin([loggedUser$, of('AMMINISTRATORE'), dichiarazioniConsumi$, richieste$]);
          } else if (localStorage.getItem('selectedRole') == AuthService.rolePrivate) { // se non ho caa associati o se non ho selezionato il ruolo caa -> UTENTE Controllo se è effetivamente un' azienda per evitare accessi da utenze non permesse
            return forkJoin([loggedUser$, of('UTENTE'), dichiarazioniConsumi$, richieste$]);
          }
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.UTENTE_NON_ABILITATO));
          return EMPTY;
        }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err);
          return EMPTY;
        }),
        switchMap(([titolariRapprOrUtente, tipo, dichiarazioni, richieste]: [Array<PersonaAgsDto> | Utente, string, Array<DichiarazioneConsumiDto>, Array<RichiestaCarburanteDto>]) => {
          let dichiarazioniFiltrate: DichiarazioneConsumiDto[] = [];
          if (dichiarazioni && dichiarazioni.length) {
            // considero solo dichiarazioni dell'anno corrente o precedente
            dichiarazioniFiltrate = dichiarazioni.filter((dichiarazione: DichiarazioneConsumiDto) => dichiarazione.campagnaRichiesta == this.dateUtilService.getCurrentYear() || dichiarazione.campagnaRichiesta == this.dateUtilService.getCurrentYear() - 1);
          }
          /**  Se è presente una Richiesta di carburante autorizzata per l'anno di campagna corrente, il sistema deve permettere l'inserimento della relativa Dichiarazione consumi e non visualizzare la Dichiarazione consumi della precedente campagna */
          let richiestaAutorizzataAnnoCorrente: RichiestaCarburanteDto = null;
          if (richieste && richieste.length) {
            richiestaAutorizzataAnnoCorrente = richieste[0];
          } else { // se non ci sono richieste autorizzate per l'anno corrente, cioè se non esiste o esiste una richiesta dell'anno corrente in compilazione...
            if (this.dateUtilService.getCurrentYear() == environment.annoInizioCampagnaUma) { // ...e se siamo nel primo anno di uma (2022)
              this.errorService.showErrorWithMessage("Non esiste una richiesta di carburante autorizzata");
              return EMPTY;
            }
          }
          if (tipo === 'UTENTE') { // -> azienda
            if ((dichiarazioniFiltrate == null || !dichiarazioniFiltrate.length) || (richiestaAutorizzataAnnoCorrente != null && !dichiarazioniFiltrate.find(dc => dc.campagnaRichiesta == this.dateUtilService.getCurrentYear()))) {
              return this.httpClientDichiarazioneConsumiUma.presentaDichiarazioneConsumi(cuaa, (titolariRapprOrUtente as Utente).codiceFiscale);
            } else {
              return of(dichiarazioniFiltrate);
            }
          } else { // tipo === ENTE -> caa
            if ((dichiarazioniFiltrate == null || !dichiarazioniFiltrate.length) || (richiestaAutorizzataAnnoCorrente != null && !dichiarazioniFiltrate.find(dc => dc.campagnaRichiesta == this.dateUtilService.getCurrentYear()))) {
              if (titolariRapprOrUtente == null || !(titolariRapprOrUtente as Array<PersonaAgsDto>).length) {
                this.errorService.showErrorWithMessage(this.UMA_01_01_BR1_ERR_MSG);
                return EMPTY;
              }
              this.router.navigate([`uma/${this.fascicoloCorrenteUMA.fascicoloLegacy.cuaa}/richiedente/${TipoRichiedenteUma['dichiarazione-consumi']}`]);
              return EMPTY;
            } else {
              return of(dichiarazioniFiltrate);
            }
          }
        })
      ).subscribe((idDichiarazioneOrDichiarazioni: number | Array<DichiarazioneConsumiDto>) => {
        if (typeof idDichiarazioneOrDichiarazioni === "number") {
          // E' stata creata una nuova domanda uma per il cuaa fornito
          this.router.navigate([`uma/${this.fascicoloCorrenteUMA.fascicoloLegacy.cuaa}/dichiarazione-consumi`, idDichiarazioneOrDichiarazioni]);
        } else {
          // Esiste già una domanda uma per il cuaa fornito
          const orderedList = _.orderBy(idDichiarazioneOrDichiarazioni, ['campagnaRichiesta'], ['desc']);
          idDichiarazioneOrDichiarazioni = orderedList;
          this.router.navigate([`uma/${this.fascicoloCorrenteUMA.fascicoloLegacy.cuaa}/dichiarazione-consumi`, (idDichiarazioneOrDichiarazioni as Array<DichiarazioneConsumiDto>)[0].id]);
        }
      }, (error: ErrorDTO) => this.errorService.showError(error));
  }

  public onClickDomandaAntimafia() {
    if (this.controlloStatoFascicolo()) {
      this.controlloStatoDichiarazioneAntimafia();
    }
  }

  private controlloStatoFascicolo(): boolean {
    // stati del fascicolo legacy
    if (this.fascicoloCorrenteUMA.fascicoloLegacy.stato.match('VALIDO|IN_LAVORAZIONE|IN_ANOMALIA')) {
      return true;
    }
    this.errorService.showErrorWithMessage(A4gMessages.CUAA_NON_ATTIVO(this.fascicoloCorrente.cuaa));
    return false;
  }

  private controlloStatoDichiarazioneAntimafia() {
    if (this.fascicoloCorrenteUMA == null || this.fascicoloCorrenteUMA.fascicoloLegacy == null || this.fascicoloCorrenteUMA.fascicoloLegacy.cuaa == null) {
      return;
    }

    const dichiarazione = new DichiarazioneAntimafia();
    dichiarazione.azienda = new Azienda();
    dichiarazione.azienda.cuaa = this.fascicoloCorrente.cuaa;

    this.getDichiarazioniAntimafiaSub = this.antimafiaService.getDichiarazioniAntimafia(dichiarazione)
      .subscribe((dichiarazioni: Array<DichiarazioneAntimafia>) => {
        this.dichiarazioniAntimafia = dichiarazioni || [];
        if (this.dichiarazioniAntimafia.length) {
          // dichiarazioni antimafia con esito positivo non possono essere movimentate di stato
          const dichiazioniEsitoPositivo = this.dichiarazioniAntimafia.filter(x => x.stato.identificativo === StatoDichiarazione.POSITIVO);
          if (dichiazioniEsitoPositivo.length) {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.BRDAMNVL005));
            return;
          }
        }
        this.router.navigate([`./fascicolo/${this.fascicoloCorrenteUMA.fascicoloLegacy.idAgs}/antimafia`], { relativeTo: this.route.parent.parent.parent });
      }, err => this.errorService.showError(err));
  }

  private isFascicoloSelezionato(): boolean {
    if (!this.fascicoloCorrente || !this.fascicoloCorrente.cuaa || !this.fascicoloCorrenteUMA || !this.fascicoloCorrenteUMA.fascicoloLegacy) {
      this.errorService.showErrorWithMessage(this.UMA_FASCICOLO_NON_VALIDO);
      return false;
    } else return true;
  }

  private caricaIdFascicoloUMA() {
    this.getFascicoloAgsByCuaaSub = this.fascicoloService.getUrlGetLegacyByCuaa(this.fascicoloCorrente.cuaa)
      .subscribe((response: FascicoloAgsDto) => {
        this.fascicoloCorrenteUMA.fascicoloLegacy = response;
      }, error => console.log(error));
  }

  public goMyappag() {
    this.myappagService.navigateForCuaa(this.fascicoloCorrente.cuaa);
  }

  public isFascicoloAttuale(): boolean {
    return (this.idValidazione === 0);
  }

  public showSectionEredi() {
    this.subscribeEredi();
    if (!this.fascicoloCorrente) {
      return false;
    }
    return this.fascicoloCorrenteUMA.isPersonaFisica(this.fascicoloCorrente.cuaa) &&
      (this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_CHIUSURA || (this.fascicoloCorrente.stato === StatoFascicoloEnum.CHIUSO && this.eredi && this.eredi.length > 0));
  }

  private subscribeEredi() {
    this.fascicoloDettaglioService.eredi.pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe((eredi: EredeDto[]) => {
      this.eredi = eredi;
    });
  }

}
