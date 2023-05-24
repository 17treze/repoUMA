import { Params } from '@angular/router';
import { FormatConverterService } from './../../uma/shared-uma/services/format-converter.service';
import { HttpClientDichiarazioneConsumiUmaService } from './../../uma/core-uma/services/http-client-dichiarazione-consumi-uma.service';
import { Observable, EMPTY, of, forkJoin, Subscription } from 'rxjs';
import { DomandaIntegrativaService } from './../../istruttoria/istruttoria-pac1420/domanda-integrativa/domanda-integrativa.service';
import { DomandaIntegrativa } from './../../istruttoria/istruttoria-pac1420/domanda-integrativa/classi/DomandaIntegrativa';
import { PaginaDomande } from './../../istruttoria/istruttoria-pac1420/domandaUnica/domain/paginaDomande';
import { Istruttoria } from 'src/app/a4g-common/classi/Istruttoria';
import { SostegnoDu } from './../../istruttoria/istruttoria-pac1420/domandaUnica/classi/SostegnoDu';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FascicoloCorrente } from '../fascicoloCorrente';
import { Router, ActivatedRoute, RouterEvent } from '@angular/router';
import {
  A4gMessages,
  A4gSeverityMessage
} from 'src/app/a4g-common/a4g-messages';
import { MessageService } from 'primeng/api';
import { SostegnoDuDi } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/SostegnoDuDi';
import { StatoDomandaEnum } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/dettaglio-istruttoria/statoDomanda';
import { AuthService } from 'src/app/auth/auth.service';
import { DichiarazioneAntimafia } from '../antimafia/classi/dichiarazioneAntimafia';
import { Azienda } from '../antimafia/classi/azienda';
import { AntimafiaService } from '../antimafia/antimafia.service';
import { StatoDichiarazione } from '../antimafia/classi/statoDichiarazioneEnum';
import { environment } from 'src/environments/environment';
import { DatiCAA } from '../creazione-fascicolo/dto/DatiSportelloCAA';
import { AnagraficaFascicoloService } from '../creazione-fascicolo/anagrafica-fascicolo.service';
import { Utente } from 'src/app/auth/user';
import { switchMap, catchError } from 'rxjs/operators';
import { PersonaAgsDto } from 'src/app/uma/core-uma/models/dto/PersonaAgsDto';
import { HttpClientDomandaUmaService } from 'src/app/uma/core-uma/services/http-client-domanda-uma.service';
import { DtoBuilderService } from 'src/app/uma/core-uma/services/dto-builder.service';
import { TipoRichiedenteUma } from 'src/app/a4g-common/classi/TipoRichiedenteUma-enum';
import { DichiarazioneConsumiDto } from 'src/app/uma/core-uma/models/dto/DichiarazioneConsumiDto';
import * as _ from 'lodash';
import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { StatoDichiarazioneConsumiEnum } from 'src/app/a4g-common/classi/enums/uma/StatoDichiarazioneConsumi.enum';
import { DichiarazioneConsumiFilter } from 'src/app/uma/core-uma/models/dto/DichiarazioneConsumiFilter';
import { MyappagService } from 'src/app/a4g-common/a4g-integration/myappag-service';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
import { DomandaUmaFilter } from 'src/app/uma/core-uma/models/dto/DomandaUmaFilter';
import { StatoDomandaUma } from 'src/app/a4g-common/classi/enums/uma/StatoDomandaUma.enum';
import { CarburanteTotale } from 'src/app/uma/core-uma/models/dto/CarburanteTotale';
import { PrelievoDto } from 'src/app/uma/core-uma/models/dto/PrelievoDto';
import { CarburanteDto } from 'src/app/uma/core-uma/models/dto/CarburanteDto';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { FascicoloService } from '../fascicolo.service';
import { FascicoloAgsDto } from 'src/app/a4g-common/classi/FascicoloAgsDto';
import { Fascicolo } from 'src/app/a4g-common/classi/Fascicolo';

@Component({
  selector: 'app-presentazione-istanze',
  templateUrl: './presentazione-istanze.component.html',
  styleUrls: ['./presentazione-istanze.component.css']
})
export class PresentazioneIstanzeComponent implements OnDestroy, OnInit {
  dichiarazioniAntimafia: DichiarazioneAntimafia[];
  isMyappagEnabled = false;

  protected gestioneDomandaSubscription: Subscription;
  readonly UMA_01_01_BR1_ERR_MSG = "Solamente il titolare o il rappresentante legale di un'azienda agricola è autorizzato alla presentazione della Richiesta di carburante on line";
  readonly UMA_FASCICOLO_NON_VALIDO = "Non è possibile reperire il fascicolo selezionato";
  readonly UMA_PERSONA_FISICA_DECEDUTA = "Il campo data decesso risulta valorizzato. Il sistema non permette l'inserimento di una richiesta di carburante";

  // Subscriptions
  richiestaCarburanteSubscription: Subscription;
  dichiarazioneConsumiSubscription: Subscription;
  prelieviAndRichiesteSubscription: Subscription;
  routeSubscription: Subscription;
  getDichiarazioniAntimafiaSub: Subscription;

  constructor(
    private fascicoloCorrente: FascicoloCorrente,
    private fascicoloService: FascicoloService,
    private router: Router,
    private service: MessageService,
    private route: ActivatedRoute,
    private istruttoriaService: IstruttoriaService,
    private domandaIntegrativaService: DomandaIntegrativaService,
    private messageService: MessageService,
    private antimafiaService: AntimafiaService,
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private authService: AuthService,
    private httpClientDomandaUmaService: HttpClientDomandaUmaService,
    private httpClientDichiarazioneConsumiUma: HttpClientDichiarazioneConsumiUmaService,
    private dtoBuilderService: DtoBuilderService,
    private errorService: ErrorService,
    private myappagService: MyappagService,
    private dateUtilService: DateUtilService,
    private formatConverterService: FormatConverterService
  ) { }

  ngOnInit(): void {
    this.isMyappagEnabled = this.myappagService.isMyappagEnabled;
    this.routeSubscription = this.route.params
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
  }

  ngOnDestroy() {
    if (this.gestioneDomandaSubscription) {
      this.gestioneDomandaSubscription.unsubscribe();
    }
    if (this.richiestaCarburanteSubscription) {
      this.richiestaCarburanteSubscription.unsubscribe();
    }
    if (this.dichiarazioneConsumiSubscription) {
      this.dichiarazioneConsumiSubscription.unsubscribe();
    }
    if (this.prelieviAndRichiesteSubscription) {
      this.prelieviAndRichiesteSubscription.unsubscribe();
    }
    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
    if (this.getDichiarazioniAntimafiaSub) {
      this.getDichiarazioniAntimafiaSub.unsubscribe();
    }
  }

  // NON HA UN FASCICOLO ATTIVO IN SIAP
  onClickAntimafia() {
    if (this.controlloStatoFascicolo()) {
      this.controlloStatoDichiarazioneAntimafia();
    }
  }

  onClickRichiestaRettificaUma() {
    if (!this.isFascicoloSelezionato()) {
      return;
    }

    const cuaa = this.fascicoloCorrente.fascicolo.cuaa;
    const getTitRapprLeg$: Observable<Array<PersonaAgsDto>> = this.anagraficaFascicoloService.getTitolariRappresentantiLegali(cuaa);
    const loggedUser$: Observable<Utente> = of(this.authService.getUserFromSession());
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
          } else if (localStorage.getItem('selectedRole') == AuthService.rolePrivate) { // se non ho caa associati o se non ho selezionato il ruolo caa -> UTENTE Controllo se è effetivamente un' azienda per evitare accessi da utenze non permesse
            return forkJoin([loggedUser$, of('UTENTE'), richieste$]);
          }
          this.service.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.UTENTE_NON_ABILITATO));
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
              if (this.fascicoloCorrente.isPersonaFisica(cuaa) && this.fascicoloCorrente.fascicoloLegacy.dataMorteTitolare != null) {
                this.errorService.showErrorWithMessage(this.UMA_PERSONA_FISICA_DECEDUTA);
                return EMPTY;
              }
              if (titolariRapprOrUtente == null || !(titolariRapprOrUtente as Array<PersonaAgsDto>).length) {
                this.errorService.showErrorWithMessage(this.UMA_01_01_BR1_ERR_MSG);
                return EMPTY;
              }
              this.router.navigate([`uma/${this.fascicoloCorrente.fascicolo.idFascicolo}/richiedente/${TipoRichiedenteUma.richiesta}`]);
              return EMPTY;
            } else {
              return of(richieste);
            }
          }
        })
      ).subscribe((idDomandaOrDomande: number | Array<RichiestaCarburanteDto>) => {
        if (typeof idDomandaOrDomande === "number") {
          // E' stata creata una nuova domanda uma per il cuaa fornito
          this.router.navigate([`uma/${this.fascicoloCorrente.fascicolo.idFascicolo}/richiesta`, idDomandaOrDomande]);
        } else {
          // Esiste già almeno una richiesta o rettifica per il cuaa fornito
          this.router.navigate([`./rettifiche/${this.fascicoloCorrente.fascicolo.cuaa}`], { relativeTo: this.route.parent });
        }
      }, (error: ErrorDTO) => this.errorService.showError(error));
  }

  onClickDichiarazioneConsumi() {
    if (!this.isFascicoloSelezionato()) {
      return;
    }

    const cuaa = this.fascicoloCorrente.fascicolo.cuaa;
    const getTitRapprLeg$: Observable<Array<PersonaAgsDto>> = this.anagraficaFascicoloService.getTitolariRappresentantiLegali(cuaa);
    const loggedUser$: Observable<Utente> = of(this.authService.getUserFromSession());
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
          } else if (localStorage.getItem('selectedRole') == AuthService.rolePrivate) { // se non ho caa associati o se non ho selezionato il ruolo caa -> UTENTE Controllo se è effetivamente un' azienda per evitare accessi da utenze non permesse
            return forkJoin([loggedUser$, of('UTENTE'), dichiarazioniConsumi$, richieste$]);
          }
          this.service.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.UTENTE_NON_ABILITATO));
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
              this.router.navigate([`uma/${this.fascicoloCorrente.fascicolo.idFascicolo}/richiedente/${TipoRichiedenteUma['dichiarazione-consumi']}`]);
              return EMPTY;
            } else {
              return of(dichiarazioniFiltrate);
            }
          }
        })
      ).subscribe((idDichiarazioneOrDichiarazioni: number | Array<DichiarazioneConsumiDto>) => {
        if (typeof idDichiarazioneOrDichiarazioni === "number") {
          // E' stata creata una nuova domanda uma per il cuaa fornito
          this.router.navigate([`uma/${this.fascicoloCorrente.fascicolo.idFascicolo}/dichiarazione-consumi`, idDichiarazioneOrDichiarazioni]);
        } else {
          // Esiste già una domanda uma per il cuaa fornito
          const orderedList = _.orderBy(idDichiarazioneOrDichiarazioni, ['campagnaRichiesta'], ['desc']);
          idDichiarazioneOrDichiarazioni = orderedList;
          this.router.navigate([`uma/${this.fascicoloCorrente.fascicolo.idFascicolo}/dichiarazione-consumi`, (idDichiarazioneOrDichiarazioni as Array<DichiarazioneConsumiDto>)[0].id]);
        }
      }, (error: ErrorDTO) => this.errorService.showError(error));
  }

  onClickGestioneCarburanteInEsubero() {
    if (!this.isFascicoloSelezionato()) {
      return;
    }

    const campagna = this.dateUtilService.getCurrentYear().toString();
    const cuaa = this.fascicoloCorrente.fascicolo.cuaa;

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
        this.router.navigate([`uma/${this.fascicoloCorrente.fascicolo.idFascicolo}/esubero-carburante/${idRichiestaCarburante}`]);
      } else {
        this.errorService.showErrorWithMessage(UMA_MESSAGES.erroreCarburanteInEsubero, 'tst');
        return;
      }
    }, (error: ErrorDTO) => {
      this.messageService.clear(); /** rimozione messaggio di abilitazioni su 403 dell'interceptor */
      this.errorService.showErrorWithMessage(UMA_MESSAGES.erroreModificaCarburanteInEsubero, 'tst');
    });
  }

  ricercaDomanda() {
    const params: DomandaUmaFilter = {
      cuaa: this.fascicoloCorrente.fascicolo.cuaa,
      campagna: [this.dateUtilService.getCurrentYear().toString()],
      stati: [StatoDomandaUma.AUTORIZZATA],
      numeroElementiPagina: 1,
      pagina: 0
    };
    return this.httpClientDomandaUmaService.getDomande(params);
  }

  public forwardToDomandaIntegrativa() {

    const jsonFilter: any = { identificativoSostegno: SostegnoDu.ZOOTECNIA };

    this.istruttoriaService.ricercaIstruttorie().subscribe(
      (istruttorie: Array<Istruttoria>) => {
        const currentYear = (new Date()).getFullYear();
        //istruttoriaCorretta =  prendo l'istruttora relativo l'anno precedente (anno attuale - 1). Questa serve per prendere i parametri relativi
        //la data chiusura domanda integrativa
        const istruttoriaCorretta = istruttorie.filter(ist => +ist.annoRiferimento === (currentYear - 1))[0];
        this.istruttoriaService.getFinestraPresentazioneDI(String(istruttoriaCorretta.id), JSON.stringify(jsonFilter)).subscribe(
          (sostegnoDuDi: SostegnoDuDi) => {
            //CONTROLLO FINESTRA APERTURA DOMANDA
            const dataApertura: Date = new Date(sostegnoDuDi.dtAperturaDomanda);
            const dataChiusura: Date = new Date(sostegnoDuDi.dtChiusuraDomanda);
            const sysdate: Date = new Date();
            sysdate.setDate(sysdate.getDate());
            if (sysdate <= dataChiusura && sysdate >= dataApertura) {
              //Costruisco il jsonParams per la chiamata alla domanda unica per il test sulla BR BRIDUACZ116
              const jsonParams: any = { idDatiSettore: String(istruttoriaCorretta.id), sostegno: SostegnoDu.ZOOTECNIA, statoDomanda: StatoDomandaEnum.IN_ISTRUTTORIA, statoSostegno: 'INTEGRATO', cuaa: this.fascicoloCorrente.fascicolo.cuaa }
              this.istruttoriaService.getDomandaUnicaFiltered(jsonParams).subscribe(
                (paginaDomande: PaginaDomande) => {
                  //CONTROLLO PER L'ESISTENZA DELLA DOMANDA UNICA
                  if (!A4gMessages.isUndefinedOrNull(paginaDomande) && !A4gMessages.isUndefinedOrNull(paginaDomande.risultati) && paginaDomande.risultati.length > 0) {
                    const idDomandaUnica = paginaDomande.risultati[0].id;
                    //Effettuo la chiamata al servizio con input l'id della domanda unica per ottenere i dati della domanda
                    //per i controlli preliminari BR BRIDUACZ116
                    this.istruttoriaService.getDomandaIntegrativaByDomandaUnica(idDomandaUnica).subscribe(
                      (domandaIntegrativa: DomandaIntegrativa) => {
                        console.log(domandaIntegrativa);
                        //CONTROLLO PRESENZA DOMANDA INTEGRATIVA
                        if (A4gMessages.isUndefinedOrNull(domandaIntegrativa)) {
                          this.service.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.controlloPresentazioneDomandaIntegrativa(this.fascicoloCorrente.fascicolo.cuaa)));
                        } else {
                          this.domandaIntegrativaService.setDomandaIntegrativa(domandaIntegrativa);
                          this.domandaIntegrativaService.setDataChiusura(dataChiusura);
                          this.domandaIntegrativaService.setIdDomandaUnica(String(idDomandaUnica));
                          //CONTROLLO LO STATO DELLA DOMANDA INTEGRATIVA
                          this.checkStatoDI(domandaIntegrativa, dataApertura, dataChiusura);
                        }
                      },
                      error => {
                        A4gMessages.handleError(this.messageService, error, 'Errore nel recupero della Domanda Integrativa collegata alla domanda unica con ID: ' + paginaDomande.risultati[0].id);
                      });
                  } else {
                    this.service.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.controlloPresentazioneDomandaIntegrativa(this.fascicoloCorrente.fascicolo.cuaa)));
                  }
                },
                error => {
                  A4gMessages.handleError(this.messageService, error, 'Errore nel recupero della domanda unica tramite i params: ' + JSON.stringify(jsonParams));
                });
            } else {
              const jsonInput: any = { cuaa: this.fascicoloCorrente.fascicolo.cuaa, annoRiferimento: istruttoriaCorretta.annoRiferimento };
              this.istruttoriaService.getDomandaIntegrativaByCuaa(jsonInput).subscribe(
                (domandaIntegrativa: DomandaIntegrativa) => {
                  this.domandaIntegrativaService.setDomandaIntegrativa(domandaIntegrativa);
                  this.domandaIntegrativaService.setDataChiusura(dataChiusura);
                  this.checkStatoDI(domandaIntegrativa, dataApertura, dataChiusura);
                }
              ),
                err => {
                  console.log(err);
                }
              // this.service.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloFinestraTemporaleDomandaIntegrativa(dataApertura.toLocaleDateString(), dataChiusura.toLocaleDateString())));
            }
          },
          error => {
            A4gMessages.handleError(this.messageService, error, 'Errore nel recupero della finestra temporale per la domanda integrativa');
          });
      },
      error => {
        A4gMessages.handleError(this.messageService, error, 'Errore nel recupero dell`istruttoria per la domanda integrativa');
      }
    );

    // if (this.controlloStatoFascicolo()) {
    //   this.router.navigate(['../domandaintegrativa'], { relativeTo: this.route });
    // }
  }

  private controlloStatoFascicolo(): boolean {
    if (this.fascicoloCorrente.fascicolo.stato.match('VALIDO|IN LAVORAZIONE|IN ANOMALIA')) {
      return true;
    }
    this.errorService.showErrorWithMessage(A4gMessages.CUAA_NON_ATTIVO(this.fascicoloCorrente.fascicolo.cuaa));
    return false;
  }

  private controlloStatoDichiarazioneAntimafia() {
    if (this.fascicoloCorrente == null || this.fascicoloCorrente.fascicolo == null || this.fascicoloCorrente.fascicolo.cuaa == null) {
      return;
    }

    const dichiarazione = new DichiarazioneAntimafia();
    dichiarazione.azienda = new Azienda();
    dichiarazione.azienda.cuaa = this.fascicoloCorrente.fascicolo.cuaa;

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
        this.router.navigate(['../antimafia'], { relativeTo: this.route });
      }, err => this.errorService.showError(err));
  }

  onRejectCheckModificaDomanda() {
    this.service.clear('checkModificaDomanda');
  }

  onConfirmCheckModificaDomanda(idDomandaUnica: number) {
    this.router.navigate(['../domandaunica/domandaintegrativa'], { relativeTo: this.route });
  }

  visibleTilesPac() {
    if (localStorage.getItem('selectedRole') == AuthService.roleCaa
      || localStorage.getItem('selectedRole') == AuthService.rolePrivate) {
      return true;
    } else {
      return false;
    }
  }

  checkStatoDI(domandaIntegrativa: DomandaIntegrativa, dataApertura: Date, dataChiusura: Date) {
    const sysdate = new Date();
    if (domandaIntegrativa.stato == 'CALCOLATO') {
      //SE LA DOMANDA E' PRESENTABILE CONTROLLO CHE LA FINESTRA SIA APERTA
      if (sysdate <= dataChiusura && sysdate >= dataApertura) {
        this.router.navigate(['../domandaunica/domandaintegrativa'], { relativeTo: this.route });
      } else {
        this.service.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloFinestraTemporaleDomandaIntegrativa(dataApertura.toLocaleDateString(), dataChiusura.toLocaleDateString())));
      }
    } else if (domandaIntegrativa.stato == 'SALVATA') {
      //SE LA DOMANDA E' GIA' STATA PRESENTATA E SONO ANCORA NELLA FINESTRA DI APERTURA POSSO MODIFICARLA 
      if (sysdate <= dataChiusura) {
        this.service.add({ key: 'checkModificaDomanda', sticky: true, severity: 'warn', summary: 'Esiste già una domanda integrativa zootecnia per l\'azienda ' + this.fascicoloCorrente.fascicolo.cuaa, detail: 'Si vuole procedere alla modifica?' });
      } else {
        this.service.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.calcoloFinestraTemporaleDomandaIntegrativa(dataApertura.toLocaleDateString(), dataChiusura.toLocaleDateString())));
      }
    } else if (domandaIntegrativa.stato == 'PRESENTATA') {
      if (sysdate <= dataChiusura) {
        this.service.add({ key: 'checkModificaDomanda', sticky: true, severity: 'warn', summary: 'Esiste già una domanda integrativa zootecnia per l\'azienda ' + this.fascicoloCorrente.fascicolo.cuaa, detail: 'Si vuole procedere alla modifica?' });
      } else {
        this.router.navigate(['../domandaunica/domandaintegrativa'], { relativeTo: this.route });
      }
    }
  }

  onMandato() {
    this.anagraficaFascicoloService.verificaPermessiAperturaFascicolo(this.fascicoloCorrente.fascicolo.cuaa, 0)
      .subscribe(
        result => {
          if (result) {
            this.router.navigate(['./mandato'], { relativeTo: this.route.parent });
          } else {
            this.service.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.soggettoNonCensitoTitolare));
          }
        },
        error => {
          this.service.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.SERVIZIO_NON_DISPONIBILE));
        }
      );
  }

  private isFascicoloSelezionato(): boolean {
    if (!this.fascicoloCorrente || !this.fascicoloCorrente.fascicolo || !this.fascicoloCorrente.fascicolo.cuaa) {
      this.errorService.showErrorWithMessage(this.UMA_FASCICOLO_NON_VALIDO);
      return false;
    } else return true;
  }

  goMyappag() {
    this.myappagService.navigateForCuaa(this.fascicoloCorrente.fascicolo.cuaa);
  }

  hasRoleCaa() {
    return localStorage.getItem('selectedRole') == AuthService.roleCaa;
  }
}
