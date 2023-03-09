import { NavigatorService } from './../../../../../a4g-common/services/navigator.service';
// import { TipoCarburante } from './../../../../../../../../cruscotto/src/app/modules/domande-uma/models/tipo-carburante';
import { ConfirmationService } from 'primeng/api';
import { Calendar } from 'primeng/calendar';
import { MotivazioneConsuntivoTipo } from './../../../../core-uma/models/enums/MotivazioneConsuntivoTipo.enum';
import { PopupMotivoRecuperoComponent } from './popup-motivo-recupero/popup-motivo-recupero.component';
import { UMA_MESSAGES } from './../../../../uma.messages';
import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { Location } from '@angular/common';
import { IndiceUmaService } from 'src/app/uma/core-uma/services/indice-uma.service';
import { Subscription, forkJoin, EMPTY, of } from 'rxjs';
import { ActivatedRoute, NavigationEnd, Params, Router, RouterEvent } from '@angular/router';
import { catchError, switchMap } from 'rxjs/operators';
import { HttpClientDichiarazioneConsumiUmaService } from 'src/app/uma/core-uma/services/http-client-dichiarazione-consumi-uma.service';
import { DichiarazioneConsumiDto } from 'src/app/uma/core-uma/models/dto/DichiarazioneConsumiDto';
import { HttpClientTrasferimentiCarburanteService } from 'src/app/uma/core-uma/services/http-client-trasferimenti-carburante.service';
import { MessageService } from 'primeng/api';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { HttpClientMacchineUmaService } from 'src/app/uma/core-uma/services/http-client-macchine-uma.service';
import { MacchinaDto } from 'src/app/uma/core-uma/models/dto/MacchinaDto';
import { ModelBuilderService } from 'src/app/uma/core-uma/services/model-builder.service';
import { FabbisognoDichiarato } from 'src/app/uma/core-uma/models/viewModels/FabbisognoDichiaratoViewModel';
import { CarburanteDto } from 'src/app/uma/core-uma/models/dto/CarburanteDto';
import { HttpClientDomandaUmaService } from 'src/app/uma/core-uma/services/http-client-domanda-uma.service';
import { PrelievoDto } from 'src/app/uma/core-uma/models/dto/PrelievoDto';
import { PopupAllegatiAllegatiMotivoComponent } from './popup-allegati-motivo-consumi/popup-allegati-motivo-consumi.component';
import * as _ from 'lodash';
import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { TipoConsuntivo } from 'src/app/uma/core-uma/models/enums/TipoConsuntivo';
import { TipoCarburanteConsuntivo } from 'src/app/uma/core-uma/models/enums/TipoCarburanteConsuntivo.enum';
import { ConsuntivoDto } from 'src/app/uma/core-uma/models/dto/ConsuntivoDto';
import { MotivazioneConsuntivoDto, AllegatoMotivazioneView } from 'src/app/uma/core-uma/models/dto/MotivazioneConsuntivoDto';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { ConsumiBuilderService } from 'src/app/uma/core-uma/services/builders/consumi-builder.service';
import { FormatConverterService } from 'src/app/uma/shared-uma/services/format-converter.service';
import { Toast } from 'primeng/toast';
import { FormFieldMap } from 'src/app/uma/core-uma/models/viewModels/FormFieldMap';
import { TipologicheService } from 'src/app/a4g-common/services/tipologiche.service';
import { StatoDichiarazioneConsumiEnum } from 'src/app/a4g-common/classi/enums/uma/StatoDichiarazioneConsumi.enum';
import { AuthService } from 'src/app/auth/auth.service';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
import { CalendarService } from 'src/app/a4g-common/services/calendar.service';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { CarburanteTotale } from 'src/app/uma/core-uma/models/dto/CarburanteTotale';
import { TrasferimentoDto } from 'src/app/uma/core-uma/models/dto/CarburanteRicevutoDto';
import { ConfirmDialog } from 'primeng-lts';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { DomandaUmaFilter } from 'src/app/uma/core-uma/models/dto/DomandaUmaFilter';
import { StatoDomandaUma } from 'src/app/a4g-common/classi/enums/uma/StatoDomandaUma.enum';
import { ConsuntivoAllegatiVM } from 'src/app/uma/core-uma/models/viewModels/ConsuntivoAllegatiViewModel';
import { PaginatorA4G } from 'src/app/a4g-common/interfaces/paginator.model';
import { FormService } from 'src/app/a4g-common/services/form.service';

@Component({
  selector: 'app-consumi',
  templateUrl: './consumi.component.html',
  styleUrls: ['./consumi.component.scss']
})
export class ConsumiComponent implements OnInit, OnDestroy {
  @ViewChild("popupAllegatiMotivoConsumi", { static: true })
  popupAllegatiMotivoConsumi: PopupAllegatiAllegatiMotivoComponent;
  @ViewChild("popupAllegatiMotivoRecupero", { static: true })
  popupAllegatiMotivoRecupero: PopupMotivoRecuperoComponent;
  @ViewChild("dataConduzioneRef")
  dataConduzioneRef: Calendar;
  @ViewChild('cd') dialog: ConfirmDialog;

  // dialog protocollazione params
  popupProtocollaVisibile: boolean;
  idFascicolo: string;

  // Component config
  READONLY_MODE: boolean;
  readonly SEPARATOR = '$$';
  maxLengthInput: number;
  areCollapsed: Array<boolean>;         /** true=panels are collapsed, false=not collapsed */
  loading: boolean;                     /** loading data from BE */
  isUtenteIstruttoreUma: boolean;       /** se true, l'utente loggato ha come profilo quello di istruttore uma o appag*/

  // Calendar settings
  minDate: Date;
  maxDate: Date;
  dataConduzioneDefault: Date;

  // Form
  consumiForm: FormGroup;

  // Variables
  STATO_DICHIARAZIONE_CONSUMI = StatoDichiarazioneConsumiEnum;
  dichiarazioneConsumiDto: DichiarazioneConsumiDto;
  richiestaCarburanteDto: RichiestaCarburanteDto;
  motivoRecuperoAmmissibile: MotivazioneConsuntivoDto;
  motivoRecuperoGasolioContoProprio: MotivazioneConsuntivoDto;
  motivoRecuperoGasolioContoTerzi: MotivazioneConsuntivoDto;
  motivoRecuperoBenzina: MotivazioneConsuntivoDto;
  motivoRecuperoGasolioSerre: MotivazioneConsuntivoDto;

  // Subscriptions
  routerSubscription: Subscription;
  deleteConsuntivoSubscription: Subscription;
  consumatoGasolioContoProprioSub: Subscription;
  consumatoGasolioContoTerziSub: Subscription;
  rimanenzaGasolioContoProprioSub: Subscription;
  rimanenzaGasolioContoTerziSub: Subscription;
  recuperoGasolioContoTerziSub: Subscription;
  ammissibileGasolioContoTerziSub: Subscription;
  recuperoGasolioContoProprioSub: Subscription;
  saveConsuntivoSubscription: Subscription;
  saveDichiarazioneSubscription: Subscription;
  utenteSubscription: Subscription;
  navigationEndSubscription: Subscription;
  updateDataConduzioneSubscription: Subscription;
  consumatoGasolioTotaleSub: Subscription;
  ammissibileGasolioTotaleSub: Subscription;
  consumatoBenzinaSub: Subscription;
  ammissibileBenzinaSub: Subscription;
  consumatoGasolioSerreSub: Subscription;
  ammissibileGasolioSerreSub: Subscription;
  rimanenzaDicembreGasolioTotaleSub: Subscription;
  rimanenzaDicembreBenzinaSub: Subscription;
  rimanenzaDicembreGasolioSerreSub: Subscription;
  recuperoGasolioTotSub: Subscription;
  recuperoBenzinaSub: Subscription;
  recuperoGasolioSerreSub: Subscription;
  protocollaSubscription: Subscription;
  deleteSubscription: Subscription;
  saveConsuntivoWithAllegatiSub: Subscription;
  getDomandeSubscription: Subscription;

  constructor(
    public tipologicheService: TipologicheService,
    public calendarService: CalendarService,
    private indiceUmaService: IndiceUmaService,
    private activeRoute: ActivatedRoute,
    private httpClientDichiarazioneConsumiUmaService: HttpClientDichiarazioneConsumiUmaService,
    private httpClientTrasferimentiCarburanteService: HttpClientTrasferimentiCarburanteService,
    private httpClientMacchineUmaService: HttpClientMacchineUmaService,
    private httpClientDomandaUmaService: HttpClientDomandaUmaService,
    private modelBuilderService: ModelBuilderService,
    private authService: AuthService,
    private location: Location,
    private errorService: ErrorService,
    private messageService: MessageService,
    private formService: FormService,
    private confirmationService: ConfirmationService,
    private consumiBuilderService: ConsumiBuilderService,
    private formatSrv: FormatConverterService,
    private dateUtilService: DateUtilService,
    private router: Router,
    private httpClientDomandUmaService: HttpClientDomandaUmaService,
    private navigatorService: NavigatorService
  ) { }

  ngOnInit() {
    this.initRoles();
    // se ricarico la pagina scatta il navigationEnd
    this.navigationEndSubscription = this.router.events
      .pipe(switchMap((event: RouterEvent) => {
        // Nel caso in cui intercetto un navigation end event, re-inizializzo il component da qui
        if (event instanceof NavigationEnd && event.url.indexOf('dichiarazione-consumi') > -1) {
          return of({ trigger: 'LOAD_DICHIARAZIONE' });
        } else return of({ trigger: 'NONE' });
      })).subscribe((res: { trigger: 'LOAD_DICHIARAZIONE' | 'NONE' }) => {
        if (res && res.trigger && res.trigger === 'LOAD_DICHIARAZIONE') {
          this.initVariables();
          this.initDichiarazione();
          this.initOnChangeListeners();
        }
      }, error => this.errorService.showError(error));

    // se non ricarico la pagina inizializzo il componente da qui
    this.initVariables();
    this.initDichiarazione();
    this.initOnChangeListeners();
  }

  // changes event per calcolo somme dinamico sui campi
  initOnChangeListeners() {
    this.consumatoGasolioContoProprioSub = this.consumiForm.get("consumatoGasolioContoProprio").valueChanges.subscribe(value => {
      const consumatoGasolioTotale = this.formatSrv.toNumber(value) + this.formatSrv.toNumber(this.consumiForm.get("consumatoGasolioContoTerzi").value);
      this.consumiForm.get("consumatoGasolioTotale").setValue(consumatoGasolioTotale);
    });

    this.consumatoGasolioContoTerziSub = this.consumiForm.get("consumatoGasolioContoTerzi").valueChanges.subscribe(value => {
      const consumatoGasolioTotale = this.formatSrv.toNumber(value) + this.formatSrv.toNumber(this.consumiForm.get("consumatoGasolioContoProprio").value);
      this.consumiForm.get("consumatoGasolioTotale").setValue(consumatoGasolioTotale);
    });

    this.rimanenzaGasolioContoProprioSub = this.consumiForm.get("rimanenzaDicembreGasolioContoProprio").valueChanges.subscribe(value => {
      const rimanenzaDicembreGasolioTotale = this.formatSrv.toNumber(value) + this.formatSrv.toNumber(this.consumiForm.get("rimanenzaDicembreGasolioContoTerzi").value);
      this.consumiForm.get("rimanenzaDicembreGasolioTotale").setValue(rimanenzaDicembreGasolioTotale);
    });

    this.rimanenzaGasolioContoTerziSub = this.consumiForm.get("rimanenzaDicembreGasolioContoTerzi").valueChanges.subscribe(value => {
      const rimanenzaDicembreGasolioTotale = this.formatSrv.toNumber(value) + this.formatSrv.toNumber(this.consumiForm.get("rimanenzaDicembreGasolioContoProprio").value);
      this.consumiForm.get("rimanenzaDicembreGasolioTotale").setValue(rimanenzaDicembreGasolioTotale);
    });

    this.recuperoGasolioContoProprioSub = this.consumiForm.get("recuperoGasolioContoProprio").valueChanges.subscribe(value => {
      this.consumiForm.get("recuperoGasolioTotale").setValue(this.formatSrv.toNumber(value) + this.formatSrv.toNumber(this.consumiForm.get("recuperoGasolioContoTerzi").value));
    });

    this.recuperoGasolioContoTerziSub = this.consumiForm.get("recuperoGasolioContoTerzi").valueChanges.subscribe(value => {
      this.consumiForm.get("recuperoGasolioTotale").setValue(this.formatSrv.toNumber(value) + this.formatSrv.toNumber(this.consumiForm.get("recuperoGasolioContoProprio").value));
    });

    this.ammissibileGasolioContoTerziSub = this.consumiForm.get("ammissibileGasolioContoTerzi").valueChanges.subscribe(value => {
      if (this.consumiForm.get("ammissibileGasolioContoTerzi").valid) {
        this.setAmmissibileGasolioTotale();
        this.setAccisaByCarburante('GASOLIO', this.calcolaAccisaByCarburante('GASOLIO'));
      }
    });

    this.consumatoGasolioTotaleSub = this.consumiForm.get("consumatoGasolioTotale").valueChanges.subscribe(value => {
      this.setAccisaByCarburante('GASOLIO', this.calcolaAccisaByCarburante('GASOLIO'));
    });

    this.ammissibileGasolioTotaleSub = this.consumiForm.get("ammissibileGasolioTotale").valueChanges.subscribe(value => {
      this.setAccisaByCarburante('GASOLIO', this.calcolaAccisaByCarburante('GASOLIO'));
    });

    this.consumatoBenzinaSub = this.consumiForm.get("consumatoBenzina").valueChanges.subscribe(value => {
      this.setAccisaByCarburante('BENZINA', this.calcolaAccisaByCarburante('BENZINA'));
    });

    this.ammissibileBenzinaSub = this.consumiForm.get("ammissibileBenzina").valueChanges.subscribe(value => {
      this.setAccisaByCarburante('BENZINA', this.calcolaAccisaByCarburante('BENZINA'));
    });

    this.consumatoGasolioSerreSub = this.consumiForm.get("consumatoGasolioSerre").valueChanges.subscribe(value => {
      this.setAccisaByCarburante('GASOLIO_SERRE', this.calcolaAccisaByCarburante('GASOLIO_SERRE'));
    });

    this.ammissibileGasolioSerreSub = this.consumiForm.get("ammissibileGasolioSerre").valueChanges.subscribe(value => {
      this.setAccisaByCarburante('GASOLIO_SERRE', this.calcolaAccisaByCarburante('GASOLIO_SERRE'));
    });

    this.rimanenzaDicembreGasolioTotaleSub = this.consumiForm.get("rimanenzaDicembreGasolioTotale").valueChanges.subscribe(value => {
      this.setAccisaByCarburante('GASOLIO', this.calcolaAccisaByCarburante('GASOLIO'));
    });

    this.rimanenzaDicembreBenzinaSub = this.consumiForm.get("rimanenzaDicembreBenzina").valueChanges.subscribe(value => {
      this.setAccisaByCarburante('BENZINA', this.calcolaAccisaByCarburante('BENZINA'));
    });

    this.rimanenzaDicembreGasolioSerreSub = this.consumiForm.get("rimanenzaDicembreGasolioSerre").valueChanges.subscribe(value => {
      this.setAccisaByCarburante('GASOLIO_SERRE', this.calcolaAccisaByCarburante('GASOLIO_SERRE'));
    });

    this.recuperoGasolioTotSub = this.consumiForm.get("recuperoGasolioTotale").valueChanges.subscribe(value => {
      this.setAccisaByCarburante('GASOLIO', this.calcolaAccisaByCarburante('GASOLIO'));
    });

    this.recuperoBenzinaSub = this.consumiForm.get("recuperoBenzina").valueChanges.subscribe(value => {
      this.setAccisaByCarburante('BENZINA', this.calcolaAccisaByCarburante('BENZINA'));
    });

    this.recuperoGasolioSerreSub = this.consumiForm.get("recuperoGasolioSerre").valueChanges.subscribe(value => {
      this.setAccisaByCarburante('GASOLIO_SERRE', this.calcolaAccisaByCarburante('GASOLIO_SERRE'));
    });
  }

  initDichiarazione() {
    this.routerSubscription = this.activeRoute.params
      .pipe(
        switchMap((params: Params) => {
          return this.httpClientDichiarazioneConsumiUmaService.getDichiarazioneConsumiById(params['id']);
        }),
        catchError((err) => {
          this.errorService.showError(err);
          return EMPTY;
        }),
        switchMap((dichiarazioneConsumiDto: DichiarazioneConsumiDto) => {
          this.dichiarazioneConsumiDto = dichiarazioneConsumiDto;
          this.initDataConduzione();
          const getMacchine$ = this.httpClientMacchineUmaService.getMacchineByIdDomanda(dichiarazioneConsumiDto.idRichiesta.toString());
          const getCarburanteRicevuto$ = this.httpClientTrasferimentiCarburanteService.getCarburanteRicevuto(this.dichiarazioneConsumiDto.cuaa, this.dichiarazioneConsumiDto.campagnaRichiesta.toString());
          const getCarburanteTrasferito$ = this.httpClientTrasferimentiCarburanteService.getCarburanteTrasferito(this.dichiarazioneConsumiDto.cuaa, this.dichiarazioneConsumiDto.campagnaRichiesta.toString());
          const getCarburantePrelevato$ = this.httpClientDomandaUmaService.getPrelieviByCuaaAndCampagna(dichiarazioneConsumiDto.cuaa, dichiarazioneConsumiDto.campagnaRichiesta.toString());
          const getResiduoAnnoPrecedente$ = this.httpClientDichiarazioneConsumiUmaService.getResiduoAnnoPrecedente(this.dichiarazioneConsumiDto.cuaa, this.dichiarazioneConsumiDto.campagnaRichiesta - 1, [StatoDichiarazioneConsumiEnum.PROTOCOLLATA]);
          const getRichiestaCarburante$ = this.httpClientDomandaUmaService.getDomandaById(this.dichiarazioneConsumiDto.idRichiesta.toString());
          const getConsuntivi$ = this.httpClientDichiarazioneConsumiUmaService.getConsuntivi(this.dichiarazioneConsumiDto.id.toString());
          const getAmmissibile$ = this.httpClientDichiarazioneConsumiUmaService.getAmmissibile(this.dichiarazioneConsumiDto.id.toString());
          return forkJoin([getMacchine$, getCarburanteRicevuto$, getCarburanteTrasferito$, getCarburantePrelevato$, getResiduoAnnoPrecedente$, getRichiestaCarburante$, getConsuntivi$, getAmmissibile$]);
        }),
        catchError((err) => {
          this.errorService.showError(err);
          return EMPTY;
        }))
      .subscribe(([macchine, ricevuti, trasferiti, prelevati, consumiAnnoPrecedente, richiestaCarburante, consuntivi, ammissibile]:
        [Array<MacchinaDto>, CarburanteTotale<TrasferimentoDto>, CarburanteTotale<TrasferimentoDto>, CarburanteTotale<PrelievoDto>, Array<DichiarazioneConsumiDto>, RichiestaCarburanteDto, any, any]) => {
        this.richiestaCarburanteDto = richiestaCarburante;
        this.setFabbisognoDichiarato(macchine);
        const residuo = (consumiAnnoPrecedente && consumiAnnoPrecedente.length && consumiAnnoPrecedente[0].rimanenza) ? consumiAnnoPrecedente[0].rimanenza : null;
        this.setResiduoCampagnaPrecedente(residuo);
        this.setRichiestoAssegnato();
        this.setCarburanteRicevuto(ricevuti.totale);
        this.setCarburanteTrasferito(trasferiti.totale);
        this.setCarburantePrelevato(prelevati.totale);
        this.setDisponibile(this.calcolaDisponibile(residuo, ricevuti.totale, prelevati.totale, trasferiti.totale));
        this.setConsuntivi(consuntivi);
        this.setAmmissibile(ammissibile);
        this.setAmmissibileGasolioTotale();
        this.setAccisaByCarburante('GASOLIO', this.calcolaAccisaByCarburante('GASOLIO'));
        this.setAccisaByCarburante('BENZINA', this.calcolaAccisaByCarburante('BENZINA'));
        this.setAccisaByCarburante('GASOLIO_SERRE', this.calcolaAccisaByCarburante('GASOLIO_SERRE'));
        this.setMotivazioneAccisa();
        this.loading = false;
      });
  }

  showAllegatiIconAmmissibile(ammissibileInput: HTMLInputElement, richiestoAssegnatoInput: HTMLInputElement) {
    if ((!ammissibileInput.disabled &&
      this.formatSrv.toNumber(ammissibileInput.value) > 0 &&
      this.formatSrv.toNumber(ammissibileInput.value) != this.formatSrv.toNumber(richiestoAssegnatoInput.value))
      || (((this.motivoRecuperoAmmissibile || {} as MotivazioneConsuntivoDto).allegati || []).length)) {
      return true;
    }
    return false;
  }

  /** @deprecated - setta a 0 tutti i valori vuoti e li mostra settati nei campi */
  private fillWithZero() {
    const { accisaMotivo, ...withoutAccisa } = this.consumiForm.value; // omit accisaMotivo
    for (const prop in withoutAccisa) {
      const normalizedValue = this.formatSrv.toNumber(this.consumiForm.get(`${prop}`).value);
      this.consumiForm.get(`${prop}`).setValue(normalizedValue);
    }
  }

  private openPanelsByField(fields: Array<FormFieldMap>) {
    fields.forEach((field: FormFieldMap) => {
      if (field.name === 'ammissibileGasolioContoTerzi') {
        this.areCollapsed[0] = field.control.invalid ? false : true;
      }
      if (field.name === 'accisaMotivo') {
        this.areCollapsed[1] = field.control.invalid ? false : true;
      }
    });
    const dichiarazioneFields = ['consumatoGasolioContoProprio', 'consumatoGasolioContoTerzi', 'consumatoBenzina', 'consumatoGasolioSerre',
      'consumatoGasolioSerre', 'rimanenzaDicembreGasolioContoProprio', 'rimanenzaDicembreGasolioContoTerzi',
      'rimanenzaDicembreBenzina', 'rimanenzaDicembreGasolioSerre', 'recuperoGasolioContoProprio',
      'recuperoGasolioContoTerzi', 'recuperoBenzina', 'recuperoGasolioSerre'];
    const onlyInvalidsDichiarazioneFields = fields.filter((field: FormFieldMap) => dichiarazioneFields.indexOf(field.name) > -1);
    this.areCollapsed[2] = onlyInvalidsDichiarazioneFields && onlyInvalidsDichiarazioneFields.length ? false : true;
  }

  goBack() {
    this.location.back();
  }

  onChangeMotivoAccisa($event: any) {
    this.confirmationService.confirm({
      message: UMA_MESSAGES.accisaObbligatoria,
      accept: () => { },
      key: "conferma-accisa-dialog",
    });
  }

  isAccisaObbligatoria(): boolean {
    if (this.formatSrv.toNumber(this.consumiForm.get('accisaGasolioTotale').value) > 0 || this.formatSrv.toNumber(this.consumiForm.get('accisaBenzina').value) > 0 || this.formatSrv.toNumber(this.consumiForm.get('accisaGasolioSerre').value) > 0) {
      return true;
    }
    return false;
  }

  cancellaAllegati(tipo: keyof typeof MotivazioneConsuntivoTipo) {
    this.messageService.add({
      key: 'checkCancella',
      sticky: true,
      severity: 'warn',
      summary: 'Cancella dati',
      detail: 'Attenzione! Si desidera procedere con la cancellazione degli allegati inseriti e delle relative informazioni? Verrà ripristinata la situazione iniziale',
      data: tipo
    });
  }

  openDialogMotivazione(tipo: keyof typeof MotivazioneConsuntivoTipo) {
    switch (tipo) {
      case MotivazioneConsuntivoTipo.AMMISSIBILE_GASOLIO_TERZI: {
        this.popupAllegatiMotivoConsumi.open(this.motivoRecuperoAmmissibile);
        break;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_GASOLIO_PROPRIO: {
        this.popupAllegatiMotivoRecupero.open(tipo, this.motivoRecuperoGasolioContoProprio);
        break;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_GASOLIO_TERZI: {
        this.popupAllegatiMotivoRecupero.open(tipo, this.motivoRecuperoGasolioContoTerzi);
        break;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_BENZINA: {
        this.popupAllegatiMotivoRecupero.open(tipo, this.motivoRecuperoBenzina);
        break;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_GASOLIO_SERRE: {
        this.popupAllegatiMotivoRecupero.open(tipo, this.motivoRecuperoGasolioSerre);
        break;
      }
      default: {
        break;
      }
    }
  }

  closeDialogMotivazione($event: { idConsuntivo: string, tipo: keyof typeof MotivazioneConsuntivoTipo, motivazioneConsuntivoDto: MotivazioneConsuntivoDto }) {
    let consParam: ConsuntivoAllegatiVM = {} as ConsuntivoAllegatiVM;
    consParam.motivazione = {} as MotivazioneConsuntivoDto;
    consParam.motivazione = $event.motivazioneConsuntivoDto;
    consParam.tipo = $event.tipo;

    // costruzione parametri
    switch ($event.tipo) {
      case MotivazioneConsuntivoTipo.AMMISSIBILE_GASOLIO_TERZI: {
        consParam.tipoConsuntivo = TipoConsuntivo.AMMISSIBILE;
        consParam.value = this.consumiForm.get('ammissibileGasolioContoTerzi').value;
        consParam.tipoCarburante = TipoCarburanteConsuntivo.GASOLIO_TERZI;
        this.motivoRecuperoAmmissibile = $event.motivazioneConsuntivoDto;
        break;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_GASOLIO_PROPRIO: {
        consParam.tipoConsuntivo = TipoConsuntivo.RECUPERO;
        consParam.value = this.consumiForm.get('recuperoGasolioContoProprio').value;
        consParam.tipoCarburante = TipoCarburanteConsuntivo.GASOLIO;
        this.motivoRecuperoGasolioContoProprio = consParam.motivazione;
        break;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_GASOLIO_TERZI: {
        consParam.tipoConsuntivo = TipoConsuntivo.RECUPERO;
        consParam.value = this.consumiForm.get('recuperoGasolioContoTerzi').value;
        consParam.tipoCarburante = TipoCarburanteConsuntivo.GASOLIO_TERZI;
        this.motivoRecuperoGasolioContoTerzi = consParam.motivazione;
        break;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_BENZINA: {
        consParam.tipoConsuntivo = TipoConsuntivo.RECUPERO;
        consParam.value = this.consumiForm.get('recuperoBenzina').value;
        consParam.tipoCarburante = TipoCarburanteConsuntivo.BENZINA;
        this.motivoRecuperoBenzina = consParam.motivazione;
        break;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_GASOLIO_SERRE: {
        consParam.tipoConsuntivo = TipoConsuntivo.RECUPERO;
        consParam.value = this.consumiForm.get('recuperoGasolioSerre').value;
        consParam.tipoCarburante = TipoCarburanteConsuntivo.GASOLIO_SERRE;
        this.motivoRecuperoGasolioSerre = consParam.motivazione;
        break;
      }
      default: {
        break;
      }
    }

    this.saveConsuntivoWithAllegati(consParam);
  }

  validaDichiarazione() {
    // start form validation
    console.log('Salvataggio dichiarazione consumi: ', this.consumiForm.value);
    this.formService.validateForm(this.consumiForm);
    const invalidFields = this.formService.getInvalids(this.consumiForm);
    if (!this.consumiForm.valid) {
      this.openPanelsByField(invalidFields);
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, UMA_MESSAGES.datiNonValidi));
      return false;
    }
    // almeno un campo consumato deve essere inserito (>=0)
    if (this.formatSrv.isLessThanZero(this.consumiForm.get('consumatoGasolioContoProprio').value) && this.formatSrv.isLessThanZero(this.consumiForm.get('consumatoGasolioContoTerzi').value) &&
      this.formatSrv.isLessThanZero(this.consumiForm.get('consumatoBenzina').value) && this.formatSrv.isLessThanZero(this.consumiForm.get('consumatoGasolioSerre').value)) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, UMA_MESSAGES.consumatoObbligatorio));
      return false;
    }
    // warning sulla compilazione dell'accisa
    if (!this.isAccisaObbligatoria()) {
      this.consumiForm.get('accisaMotivo').reset();
    } else if (this.isAccisaObbligatoria() && this.formatSrv.isEmptyString(this.consumiForm.get('accisaMotivo').value)) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, UMA_MESSAGES.accisaObbligatoria));
      return false;
    }
    return true;
  }

  validaDichiarazioneDaProtocollare() {
    if (this.validaDichiarazione()) {
      if (this.consumiForm.get('accisaGasolioTotale').value > 0 || this.consumiForm.get('accisaBenzina').value > 0 || this.consumiForm.get('accisaGasolioSerre').value > 0) {
        this.confirmationService.confirm({
          message: UMA_MESSAGES.accisaValorizzataWarn,
          accept: () => { this.saveAndProtocollaDichiarazione() },
          key: "conferma-accisa-dialog",
        });
        return false;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }

  saveDichiarazione() {
    // Salvataggio dichiarazione
    this.dichiarazioneConsumiDto.motivazioneAccisa = this.consumiForm.get('accisaMotivo').value;
    const consuntivi: Array<ConsuntivoDto> = this.consumiBuilderService.buildConsuntiviDto(this.consumiForm.value).filter((consuntivo: ConsuntivoDto) => consuntivo.quantita >= 0);
    this.saveDichiarazioneSubscription = this.httpClientDichiarazioneConsumiUmaService.saveConsuntivi(this.dichiarazioneConsumiDto.id.toString(), consuntivi)
      .pipe(
        catchError((err) => {
          this.errorService.showError(err);
          return EMPTY;
        }),
        switchMap((consuntiviSaved: Array<ConsuntivoDto>) => {
          /** FIX la risetto e la invio al BE che altrimenti la setterebbe a null */
          const date = new Date(this.consumiForm.get("dataConduzione").value);
          const dataConduzione = this.dateUtilService.toDateWithNoOffset(date).toISOString();
          console.log('Update data: ', dataConduzione);
          console.log('Update accisa: ', this.dichiarazioneConsumiDto.motivazioneAccisa);
          return this.httpClientDichiarazioneConsumiUmaService.updateDichiarazioneConsumi(this.dichiarazioneConsumiDto.id.toString(), this.consumiBuilderService.buildDichiarazioneConsumiPatch(this.dichiarazioneConsumiDto.motivazioneAccisa, dataConduzione));
        })
      ).subscribe((res: any) => {
        // this.fillWithZero(); // non è richiesto di vedere gli zeri, ma solo quello che salvo
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioConsuntiviOk));
      }, error => this.errorService.showError(error));;
  }

  saveAndProtocollaDichiarazione() {
    this.dichiarazioneConsumiDto.motivazioneAccisa = this.consumiForm.get('accisaMotivo').value;
    const consuntivi: Array<ConsuntivoDto> = this.consumiBuilderService.buildConsuntiviDto(this.consumiForm.value).filter((consuntivo: ConsuntivoDto) => consuntivo.quantita >= 0);
    this.protocollaSubscription = this.httpClientDichiarazioneConsumiUmaService.saveConsuntivi(this.dichiarazioneConsumiDto.id.toString(), consuntivi)
      .pipe(
        catchError((err) => {
          this.errorService.showError(err);
          return EMPTY;
        }),
        switchMap((consuntiviSaved: Array<ConsuntivoDto>) => {
          /** FIX risetto la data conduzione e la invio al BE che altrimenti la setterebbe a null */
          const date = new Date(this.consumiForm.get("dataConduzione").value);
          const dataConduzione = this.dateUtilService.toDateWithNoOffset(date).toISOString();
          console.log('Update data: ', dataConduzione);
          console.log('Update accisa: ', this.dichiarazioneConsumiDto.motivazioneAccisa);
          return this.httpClientDichiarazioneConsumiUmaService.updateDichiarazioneConsumi(this.dichiarazioneConsumiDto.id.toString(), this.consumiBuilderService.buildDichiarazioneConsumiPatch(this.dichiarazioneConsumiDto.motivazioneAccisa, dataConduzione));
        }),
        catchError((err) => {
          this.errorService.showError(err);
          return EMPTY;
        }),
        switchMap((updateDichiarazione: any) => {
          return this.httpClientDichiarazioneConsumiUmaService.valida(this.dichiarazioneConsumiDto.id.toString());
        })
      ).subscribe(response => {
        this.popupProtocollaVisibile = true;
      }, (error: ErrorDTO) => {
        console.log("ERROR: ", error);
        if (error && error.error && error.error.message && error.error.message.includes("[ERR-1]")) {
          this.erroreEliminazioneDichiarazioneORettifica(error);
        } else if (error && error.error && error.error.message && error.error.message.includes("[ERR-2]")) {
          this.errorRichiestaObsoleta(error);
        } else this.errorService.showError(error);
      });
  }

  private saveConsuntivoWithAllegati(cons: ConsuntivoAllegatiVM) {
    // prima salvo i consuntivi per consolidare i parametri disponibili per i controlli e poi salvo il consuntivo con allegati evitando inconsistenze al DB
    this.dichiarazioneConsumiDto.motivazioneAccisa = this.consumiForm.get('accisaMotivo').value;
    const consuntivi: Array<ConsuntivoDto> = this.consumiBuilderService.buildConsuntiviDto(this.consumiForm.value).filter((consuntivo: ConsuntivoDto) => consuntivo.quantita >= 0);
    this.saveConsuntivoWithAllegatiSub = this.httpClientDichiarazioneConsumiUmaService.saveConsuntivi(this.dichiarazioneConsumiDto.id.toString(), consuntivi)
      .pipe(
        catchError((err) => {
          this.errorService.showError(err);
          return EMPTY;
        }),
        switchMap((updateDichiarazione: any) => {
          return this.saveConsuntivo(cons.tipoConsuntivo, cons.tipo, cons.value, cons.tipoCarburante, cons.motivazione);
        })
      ).subscribe((idConsuntivo: string) => {
        // aggiorno l'oggetto motivoRecupero con l'id appena salvato per consentire un'eventuale cancellazione e rivalido il campo
        this.updateAndValidateMotivo(cons.tipo, idConsuntivo);
        // aggiorno il controllo sul campo ammissibile
        if (cons.tipo === MotivazioneConsuntivoTipo.AMMISSIBILE_GASOLIO_TERZI) {
          this.consumiForm.get("ammissibileGasolioContoTerzi").updateValueAndValidity();
        }
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioAllegatiConsumiOk));
      }, error => {
        this.errorService.showError(error);
        this.location.back(); // nel caso in cui il salvataggio degli allegati va in errore torno alla pagina sezioni della dichiarazione
      });
  }

  closeDialogCambioData($event) {
  }

  confermaCancellazioneConsuntivo(toast: Toast) {
    const idConsuntivo = this.recuperaIDConsuntivo(toast.messages[0].data);
    if (idConsuntivo == null) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, UMA_MESSAGES.deleteConsuntivoKO));
      return;
    }
    this.deleteConsuntivoSubscription = this.httpClientDichiarazioneConsumiUmaService.deleteConsuntivo(this.dichiarazioneConsumiDto.id.toString(), idConsuntivo).subscribe(() => {
      this.svuotaAllegati(toast.messages[0].data);
      // ripristino il valore iniziale dell'ammissibile
      if (toast.messages[0].data === MotivazioneConsuntivoTipo.AMMISSIBILE_GASOLIO_TERZI) {
        this.setAmmissibileContoTerzi();
      }
      this.messageService.clear('checkCancella');
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, UMA_MESSAGES.deleteConsuntivoOk));
    }, error => this.errorService.showError(error));
  }

  rifiutaCancellazioneConsuntivo(toast: Toast) {
    this.messageService.clear('checkCancella');
  }

  isDataDefault(): boolean {
    if (!this.dichiarazioneConsumiDto || !this.dichiarazioneConsumiDto.dataConduzione) {
      return false;
    }
    const dataConduzione = new Date(this.dichiarazioneConsumiDto.dataConduzione);
    const dataDefault = new Date(this.dataConduzioneDefault);
    return this.dateUtilService.isSameDates(dataConduzione, dataDefault);
  }

  salvaDataConduzione($event: Date) {
    const lastDate = this.dateUtilService.toDate(this.dataConduzioneRef.inputFieldValue);
    this.confirmationService.confirm({
      message: UMA_MESSAGES.cambioDataConduzione,
      accept: () => {
        this.confermaCambioData($event);
      },
      reject: () => {
        this.consumiForm.get("dataConduzione").setValue(lastDate);
      },
      key: "conferma-cambio-data",
    });
  }

  ripristinaDataConduzione() {
    this.confirmationService.confirm({
      message: UMA_MESSAGES.cambioDataConduzione,
      accept: () => {
        this.dichiarazioneConsumiDto.dataConduzione = this.dataConduzioneDefault;
        this.consumiForm.get("dataConduzione").setValue(this.dataConduzioneDefault);
        this.confermaCambioData(this.dataConduzioneDefault);
      },
      reject: () => { },
      key: "conferma-cambio-data",
    });
  }

  onCloseToastCancellazione() {
    this.navigatorService.goToPresentazioneIstanze(this.idFascicolo);
  }

  closeProtocollazioneDialog() {
    this.popupProtocollaVisibile = false;
  }

  ngOnDestroy(): void {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
    if (this.deleteConsuntivoSubscription) {
      this.deleteConsuntivoSubscription.unsubscribe();
    }
    if (this.consumatoGasolioContoProprioSub) {
      this.consumatoGasolioContoProprioSub.unsubscribe();
    }
    if (this.consumatoGasolioContoTerziSub) {
      this.consumatoGasolioContoTerziSub.unsubscribe();
    }
    if (this.rimanenzaGasolioContoProprioSub) {
      this.rimanenzaGasolioContoProprioSub.unsubscribe();
    }
    if (this.rimanenzaGasolioContoTerziSub) {
      this.rimanenzaGasolioContoTerziSub.unsubscribe();
    }
    if (this.recuperoGasolioContoProprioSub) {
      this.recuperoGasolioContoProprioSub.unsubscribe();
    }
    if (this.recuperoGasolioContoTerziSub) {
      this.recuperoGasolioContoTerziSub.unsubscribe();
    }
    if (this.ammissibileGasolioContoTerziSub) {
      this.ammissibileGasolioContoTerziSub.unsubscribe();
    }
    if (this.saveConsuntivoSubscription) {
      this.saveConsuntivoSubscription.unsubscribe();
    }
    if (this.saveDichiarazioneSubscription) {
      this.saveDichiarazioneSubscription.unsubscribe();
    }
    if (this.utenteSubscription) {
      this.utenteSubscription.unsubscribe();
    }
    if (this.navigationEndSubscription) {
      this.navigationEndSubscription.unsubscribe();
    }
    if (this.updateDataConduzioneSubscription) {
      this.updateDataConduzioneSubscription.unsubscribe();
    }
    if (this.consumatoGasolioTotaleSub) {
      this.consumatoGasolioTotaleSub.unsubscribe();
    }
    if (this.ammissibileGasolioTotaleSub) {
      this.ammissibileGasolioTotaleSub.unsubscribe();
    }
    if (this.consumatoGasolioSerreSub) {
      this.consumatoGasolioSerreSub.unsubscribe();
    }
    if (this.ammissibileBenzinaSub) {
      this.ammissibileBenzinaSub.unsubscribe();
    }
    if (this.ammissibileGasolioSerreSub) {
      this.ammissibileGasolioSerreSub.unsubscribe();
    }
    if (this.consumatoBenzinaSub) {
      this.consumatoBenzinaSub.unsubscribe();
    }
    if (this.rimanenzaDicembreGasolioTotaleSub) {
      this.rimanenzaDicembreGasolioTotaleSub.unsubscribe();
    }
    if (this.rimanenzaDicembreBenzinaSub) {
      this.rimanenzaDicembreBenzinaSub.unsubscribe();
    }
    if (this.rimanenzaDicembreGasolioSerreSub) {
      this.rimanenzaDicembreGasolioSerreSub.unsubscribe();
    }
    if (this.recuperoGasolioTotSub) {
      this.recuperoGasolioTotSub.unsubscribe();
    }
    if (this.recuperoBenzinaSub) {
      this.recuperoBenzinaSub.unsubscribe();
    }
    if (this.recuperoGasolioSerreSub) {
      this.recuperoGasolioSerreSub.unsubscribe();
    }
    if (this.protocollaSubscription) {
      this.protocollaSubscription.unsubscribe();
    }
    if (this.deleteSubscription) {
      this.deleteSubscription.unsubscribe();
    }
    if (this.saveConsuntivoWithAllegatiSub) {
      this.saveConsuntivoWithAllegatiSub.unsubscribe();
    }
    if (this.getDomandeSubscription) {
      this.getDomandeSubscription.unsubscribe();
    }
  }

  private initRoles() {
    // se istruttore o appag posso modificare la data di conduzione
    if (this.authService.userSelectedRole == AuthService.roleIstruttoreUMA ||
      this.authService.userSelectedRole == AuthService.roleAppag) {
      this.isUtenteIstruttoreUma = true;
    }
  }

  private setMotivazioneAccisa() {
    if (this.dichiarazioneConsumiDto.motivazioneAccisa) {
      this.consumiForm.get("accisaMotivo").setValue(this.dichiarazioneConsumiDto.motivazioneAccisa);
    }
  }

  private setConsuntivi(consuntivi: Array<ConsuntivoDto>) {
    if (consuntivi && consuntivi.length) {
      consuntivi.forEach(consuntivo => {
        switch (consuntivo.tipo) {
          case TipoConsuntivo.AMMISSIBILE: {
            if (consuntivo.carburante == TipoCarburanteConsuntivo.GASOLIO_TERZI) {
              this.motivoRecuperoAmmissibile = { idConsuntivo: consuntivo.id, allegati: this.consumiBuilderService.toAllegatoMotivazione(consuntivo, this.dichiarazioneConsumiDto.id.toString()), motivazione: consuntivo.motivazione };
              this.consumiForm.get("ammissibileGasolioContoTerzi").setValue(consuntivo.quantita.toString());
              /** attesa che l'array di allegati sia valorizzato, per validare il campo */
              setTimeout(() => this.consumiForm.get("ammissibileGasolioContoTerzi").updateValueAndValidity());
            }
            break;
          }
          case TipoConsuntivo.CONSUMATO: {
            if (consuntivo.carburante == TipoCarburanteConsuntivo.GASOLIO) {
              this.consumiForm.get("consumatoGasolioContoProprio").setValue(consuntivo.quantita.toString());
              break;
            }
            if (consuntivo.carburante == TipoCarburanteConsuntivo.GASOLIO_TERZI) {
              this.consumiForm.get("consumatoGasolioContoTerzi").setValue(consuntivo.quantita.toString());
              break;
            }
            if (consuntivo.carburante == TipoCarburanteConsuntivo.BENZINA) {
              this.consumiForm.get("consumatoBenzina").setValue(consuntivo.quantita.toString());
              break;
            }
            if (consuntivo.carburante == TipoCarburanteConsuntivo.GASOLIO_SERRE) {
              this.consumiForm.get("consumatoGasolioSerre").setValue(consuntivo.quantita.toString());
              break;
            }
            break;
          }
          case TipoConsuntivo.RIMANENZA: {
            if (consuntivo.carburante == TipoCarburanteConsuntivo.GASOLIO) {
              this.consumiForm.get("rimanenzaDicembreGasolioContoProprio").setValue(consuntivo.quantita.toString());
              break;
            }
            if (consuntivo.carburante == TipoCarburanteConsuntivo.GASOLIO_TERZI) {
              this.consumiForm.get("rimanenzaDicembreGasolioContoTerzi").setValue(consuntivo.quantita.toString());
              break;
            }
            if (consuntivo.carburante == TipoCarburanteConsuntivo.BENZINA) {
              this.consumiForm.get("rimanenzaDicembreBenzina").setValue(consuntivo.quantita.toString());
              break;
            }
            if (consuntivo.carburante == TipoCarburanteConsuntivo.GASOLIO_SERRE) {
              this.consumiForm.get("rimanenzaDicembreGasolioSerre").setValue(consuntivo.quantita.toString());
              break;
            }
            break;
          }
          case TipoConsuntivo.RECUPERO: {
            if (consuntivo.carburante == TipoCarburanteConsuntivo.GASOLIO) {
              this.consumiForm.get("recuperoGasolioContoProprio").setValue(consuntivo.quantita.toString());
              this.motivoRecuperoGasolioContoProprio = { idConsuntivo: consuntivo.id, allegati: this.consumiBuilderService.toAllegatoMotivazione(consuntivo, this.dichiarazioneConsumiDto.id.toString()), motivazione: consuntivo.motivazione };
              break;
            }
            if (consuntivo.carburante == TipoCarburanteConsuntivo.GASOLIO_TERZI) {
              this.consumiForm.get("recuperoGasolioContoTerzi").setValue(consuntivo.quantita.toString());
              this.motivoRecuperoGasolioContoTerzi = { idConsuntivo: consuntivo.id, allegati: this.consumiBuilderService.toAllegatoMotivazione(consuntivo, this.dichiarazioneConsumiDto.id.toString()), motivazione: consuntivo.motivazione };
              break;
            }
            if (consuntivo.carburante == TipoCarburanteConsuntivo.BENZINA) {
              this.consumiForm.get("recuperoBenzina").setValue(consuntivo.quantita.toString());
              this.motivoRecuperoBenzina = { idConsuntivo: consuntivo.id, allegati: this.consumiBuilderService.toAllegatoMotivazione(consuntivo, this.dichiarazioneConsumiDto.id.toString()), motivazione: consuntivo.motivazione };
              break;
            }
            if (consuntivo.carburante == TipoCarburanteConsuntivo.GASOLIO_SERRE) {
              this.consumiForm.get("recuperoGasolioSerre").setValue(consuntivo.quantita.toString());
              this.motivoRecuperoGasolioSerre = { idConsuntivo: consuntivo.id, allegati: this.consumiBuilderService.toAllegatoMotivazione(consuntivo, this.dichiarazioneConsumiDto.id.toString()), motivazione: consuntivo.motivazione };
              break;
            }
            break;
          }
          default: {
            break;
          }
        }
      });
    }
  }

  private setFabbisognoDichiarato(macchine: Array<MacchinaDto>) {
    if (!macchine || !macchine.length)
      return;
    const fabbisognoDichiarato = this.modelBuilderService.fabbisognoDichiaratoBuilder(macchine);
    this.disableFieldsByCarburate(fabbisognoDichiarato);
  }

  private setResiduoCampagnaPrecedente(residuo: CarburanteDto) {
    if (!residuo) {
      return;
    }
    this.consumiForm.get('residuoGasolioContoProprio').setValue(residuo.gasolio ? residuo.gasolio : 0);
    this.consumiForm.get('residuoGasolioContoTerzi').setValue(residuo.gasolioTerzi ? residuo.gasolioTerzi : 0);
    this.consumiForm.get('residuoGasolioTotale').setValue(residuo.gasolio + residuo.gasolioTerzi);
    this.consumiForm.get('residuoBenzina').setValue(residuo.benzina);
    this.consumiForm.get('residuoGasolioSerre').setValue(residuo.gasolioSerre);
  }

  private setRichiestoAssegnato() {
    if (!this.richiestaCarburanteDto || !this.richiestaCarburanteDto.carburanteRichiesto) {
      return;
    }
    const richiesto = this.richiestaCarburanteDto.carburanteRichiesto;
    this.consumiForm.get('richAssegnatoGasolioContoProprio').setValue(richiesto.gasolio ? richiesto.gasolio : 0);
    this.consumiForm.get('richAssegnatoGasolioContoTerzi').setValue(richiesto.gasolioTerzi ? richiesto.gasolioTerzi : 0);
    this.consumiForm.get('richAssegnatoGasolioTotale').setValue((richiesto.gasolio ? richiesto.gasolio : 0) + (richiesto.gasolioTerzi ? richiesto.gasolioTerzi : 0));
    this.consumiForm.get('richAssegnatoBenzina').setValue(richiesto.benzina ? richiesto.benzina : 0);
    this.consumiForm.get('richAssegnatoGasolioSerre').setValue(richiesto.gasolioSerre ? richiesto.gasolioSerre : 0);
  }

  private setAmmissibile(ammissibile: CarburanteDto) {
    this.setAmmissibileContoTerzi();
    if (!ammissibile) {
      return;
    }
    this.consumiForm.get('ammissibileGasolioContoProprio').setValue(ammissibile.gasolio.toString());
    this.consumiForm.get('ammissibileBenzina').setValue(ammissibile.benzina);
    this.consumiForm.get('ammissibileGasolioSerre').setValue(ammissibile.gasolioSerre);
  }

  private setAmmissibileContoTerzi() {
    // se non esiste un consuntivo lo prendo dalla richiesta carburante
    if (this.consumiForm.get("ammissibileGasolioContoTerzi").value == null) {
      const value = this.richiestaCarburanteDto && this.richiestaCarburanteDto.carburanteRichiesto && this.richiestaCarburanteDto.carburanteRichiesto.gasolioTerzi;
      this.consumiForm.get("ammissibileGasolioContoTerzi").setValue(value);
    }
  }

  private setAmmissibileGasolioTotale() {
    const ammissibileContoProprio = this.formatSrv.toNumber(this.consumiForm.get("ammissibileGasolioContoProprio").value);
    const ammissibileGasolioTerzi = this.formatSrv.toNumber(this.consumiForm.get("ammissibileGasolioContoTerzi").value);
    this.consumiForm.get('ammissibileGasolioTotale').setValue(ammissibileContoProprio + ammissibileGasolioTerzi);
  }

  private setDisponibile(disponibile: CarburanteDto) {
    if (!disponibile) {
      return;
    }
    this.consumiForm.get('disponibileGasolioTotale').setValue(disponibile.gasolio);
    this.consumiForm.get('disponibileBenzina').setValue(disponibile.benzina);
    this.consumiForm.get('disponibileGasolioSerre').setValue(disponibile.gasolioSerre);
    this.consumiForm.get('consumatoGasolioContoProprio').updateValueAndValidity();
  }

  private calcolaDisponibile(residuo: CarburanteDto, ricevuto: CarburanteDto, prelevato: CarburanteDto, trasferito: CarburanteDto): CarburanteDto {
    if (!residuo && !ricevuto && !prelevato && !trasferito) {
      return;
    }
    const disponibile = new CarburanteDto();
    disponibile.gasolio = (((residuo ? residuo.gasolio : 0) + (residuo ? residuo.gasolioTerzi : 0)) +
      (ricevuto ? ricevuto.gasolio : 0) +
      (prelevato ? prelevato.gasolio : 0)) -
      ((trasferito ? trasferito.gasolio : 0));
    disponibile.benzina = ((residuo ? residuo.benzina : 0) + (ricevuto ? ricevuto.benzina : 0) + (prelevato ? prelevato.benzina : 0)) - (trasferito ? trasferito.benzina : 0);
    disponibile.gasolioSerre = ((residuo ? residuo.gasolioSerre : 0) + (ricevuto ? ricevuto.gasolioSerre : 0) + (prelevato ? prelevato.gasolioSerre : 0)) - (trasferito ? trasferito.gasolioSerre : 0);
    return disponibile;
  }

  private calcolaAccisaByCarburante(tipoCarburante: string): number {
  // private calcolaAccisaByCarburante(tipoCarburante: keyof typeof TipoCarburante): number {
    // SE(consumato > ammissibile) ALLORA
    // 	accisa = disponibile - ammissibile - rimanenza - recupero
    // ALTRIMENTI
    // 	accisa = disponibile - consumato - rimanenza - recupero

    const accisa = new CarburanteDto();
    if (tipoCarburante === 'GASOLIO') {
      const disponibileGasolioTotale = this.formatSrv.toNumber(this.consumiForm.get('disponibileGasolioTotale').value);
      const ammissibileGasolioTotale = this.formatSrv.toNumber(this.consumiForm.get('ammissibileGasolioTotale').value);
      const rimanenzaGasolioTotale = this.formatSrv.toNumber(this.consumiForm.get('rimanenzaDicembreGasolioTotale').value);
      const recuperoGasolioTotale = this.formatSrv.toNumber(this.consumiForm.get('recuperoGasolioTotale').value);
      const consumatoGasolioTotale = this.formatSrv.toNumber(this.consumiForm.get('consumatoGasolioTotale').value);
      if (consumatoGasolioTotale > ammissibileGasolioTotale) {
        accisa.gasolio = disponibileGasolioTotale - ammissibileGasolioTotale - rimanenzaGasolioTotale - recuperoGasolioTotale;
      } else {
        accisa.gasolio = disponibileGasolioTotale - consumatoGasolioTotale - rimanenzaGasolioTotale - recuperoGasolioTotale;
      }
      return accisa.gasolio;
    }

    if (tipoCarburante === 'BENZINA') {
      const disponibileBenzina = this.formatSrv.toNumber(this.consumiForm.get('disponibileBenzina').value);
      const rimanenzaDicembreBenzina = this.formatSrv.toNumber(this.consumiForm.get('rimanenzaDicembreBenzina').value);
      const recuperoBenzina = this.formatSrv.toNumber(this.consumiForm.get('recuperoBenzina').value);
      const ammissibileBenzinaTotale = this.formatSrv.toNumber(this.consumiForm.get('ammissibileBenzina').value);
      const consumatoBenzinaTotale = this.formatSrv.toNumber(this.consumiForm.get('consumatoBenzina').value);
      if (consumatoBenzinaTotale > ammissibileBenzinaTotale) {
        accisa.benzina = disponibileBenzina - ammissibileBenzinaTotale - rimanenzaDicembreBenzina - recuperoBenzina;
      } else {
        accisa.benzina = disponibileBenzina - consumatoBenzinaTotale - rimanenzaDicembreBenzina - recuperoBenzina;
      }
      return accisa.benzina;
    }

    if (tipoCarburante === 'GASOLIO_SERRE') {
      const disponibileGasolioSerre = this.formatSrv.toNumber(this.consumiForm.get('disponibileGasolioSerre').value);
      const rimanenzaDicembreGasolioSerre = this.formatSrv.toNumber(this.consumiForm.get('rimanenzaDicembreGasolioSerre').value);
      const recuperoGasolioSerre = this.formatSrv.toNumber(this.consumiForm.get('recuperoGasolioSerre').value);
      const ammissibileGasolioSerreTotale = this.formatSrv.toNumber(this.consumiForm.get('ammissibileGasolioSerre').value);
      const consumatoGasolioSerreTotale = this.formatSrv.toNumber(this.consumiForm.get('consumatoGasolioSerre').value);
      if (consumatoGasolioSerreTotale > ammissibileGasolioSerreTotale) {
        accisa.gasolioSerre = disponibileGasolioSerre - ammissibileGasolioSerreTotale - rimanenzaDicembreGasolioSerre - recuperoGasolioSerre;
      } else {
        accisa.gasolioSerre = disponibileGasolioSerre - consumatoGasolioSerreTotale - rimanenzaDicembreGasolioSerre - recuperoGasolioSerre;
      }
      return accisa.gasolioSerre;
    }

    return 0;
  }

  private setAccisaByCarburante(tipoCarburante: string, accisa: number) {
  // private setAccisaByCarburante(tipoCarburante: keyof typeof TipoCarburante, accisa: number) {
      if (accisa == null) {
      return;
    }
    setTimeout(() => {
      if (tipoCarburante === 'GASOLIO') {
        this.consumiForm.get('accisaGasolioTotale').setValue(accisa);
      } else if (tipoCarburante === 'BENZINA') {
        this.consumiForm.get('accisaBenzina').setValue(accisa);
      } else { // (tipoCarburante === 'GASOLIO_SERRE') {
        this.consumiForm.get('accisaGasolioSerre').setValue(accisa);
      }

      if (!this.isAccisaObbligatoria() || this.READONLY_MODE) {
        this.consumiForm.get('accisaMotivo').disable();
      } else {
        this.consumiForm.get('accisaMotivo').enable();
        this.consumiForm.get('accisaMotivo').setValidators([Validators.required]);
      }
    }, 0);
  }

  private setCarburanteRicevuto(ricevuto: CarburanteDto) {
    if (!ricevuto) {
      return;
    }
    this.consumiForm.get('ricevutoGasolioTotale').setValue(ricevuto.gasolio);
    this.consumiForm.get('ricevutoBenzina').setValue(ricevuto.benzina);
    this.consumiForm.get('ricevutoGasolioSerre').setValue(ricevuto.gasolioSerre);
  }

  private setCarburanteTrasferito(trasferito: CarburanteDto) {
    if (!trasferito) {
      return;
    }
    this.consumiForm.get('trasferitoGasolioTotale').setValue(trasferito.gasolio);
    this.consumiForm.get('trasferitoBenzina').setValue(trasferito.benzina);
    this.consumiForm.get('trasferitoGasolioSerre').setValue(trasferito.gasolioSerre);
  }

  private setCarburantePrelevato(prelevato: CarburanteDto) {
    if (!prelevato) {
      return;
    }
    this.consumiForm.get('prelevatoGasolioTotale').setValue(prelevato.gasolio);
    this.consumiForm.get('prelevatoBenzina').setValue(prelevato.benzina);
    this.consumiForm.get('prelevatoGasolioSerre').setValue(prelevato.gasolioSerre);
  }

  /** @deprecated */
  private sommaCarburante(carburanti: Array<CarburanteDto>): CarburanteDto {
    const carburanteSum = {} as CarburanteDto;
    carburanteSum.benzina = carburanti.reduce((accumulator: number, carburante: CarburanteDto) => (accumulator + carburante.benzina), 0);
    carburanteSum.gasolio = carburanti.reduce((accumulator: number, carburante: CarburanteDto) => (accumulator + carburante.gasolio), 0);
    carburanteSum.gasolioSerre = carburanti.reduce((accumulator: number, carburante: CarburanteDto) => (accumulator + carburante.gasolioSerre), 0);
    return carburanteSum;
  }

  private initVariables() {
    this.READONLY_MODE = this.indiceUmaService.READONLY_MODE_DICHIARAZIONE || (localStorage.getItem('UMA_RO_DICH') == 'true' ? true : false);
    this.calendarService.configITA();
    this.tipologicheService.setMotiviAccisa();
    this.idFascicolo = this.indiceUmaService.idFascicolo || null;
    this.areCollapsed = [true, true, true];
    this.loading = true;
    this.maxLengthInput = 7;
    this.motivoRecuperoAmmissibile = {} as MotivazioneConsuntivoDto;
    this.motivoRecuperoGasolioContoProprio = {} as MotivazioneConsuntivoDto;
    this.motivoRecuperoGasolioContoTerzi = {} as MotivazioneConsuntivoDto;
    this.motivoRecuperoBenzina = {} as MotivazioneConsuntivoDto;
    this.motivoRecuperoGasolioSerre = {} as MotivazioneConsuntivoDto;
    this.motivoRecuperoAmmissibile.allegati = [];
    this.motivoRecuperoGasolioContoProprio.allegati = [];
    this.motivoRecuperoGasolioContoTerzi.allegati = [];
    this.motivoRecuperoBenzina.allegati = [];
    this.motivoRecuperoGasolioSerre.allegati = [];
    this.initForm();
  }

  private initForm() {
    this.consumiForm = new FormGroup({
      dataConduzione: new FormControl({ value: null, disabled: false }),
      // Prima sezione
      residuoGasolioContoProprio: new FormControl({ value: '0', disabled: true }),
      residuoGasolioContoTerzi: new FormControl({ value: '0', disabled: true }),
      residuoGasolioTotale: new FormControl({ value: '0', disabled: true }),
      residuoBenzina: new FormControl({ value: '0', disabled: true }),
      residuoGasolioSerre: new FormControl({ value: '0', disabled: true }),
      ammissibileGasolioContoProprio: new FormControl({ value: '0', disabled: true }),
      ammissibileGasolioContoTerzi: new FormControl({ value: null, disabled: this.READONLY_MODE }),
      ammissibileGasolioTotale: new FormControl({ value: '0', disabled: true }),
      ammissibileBenzina: new FormControl({ value: '0', disabled: true }),
      ammissibileGasolioSerre: new FormControl({ value: '0', disabled: true }),
      richAssegnatoGasolioContoProprio: new FormControl({ value: '0', disabled: true }),
      richAssegnatoGasolioContoTerzi: new FormControl({ value: '0', disabled: true }),
      richAssegnatoGasolioTotale: new FormControl({ value: '0', disabled: true }),
      richAssegnatoBenzina: new FormControl({ value: '0', disabled: true }),
      richAssegnatoGasolioSerre: new FormControl({ value: '0', disabled: true }),
      // Seconda sezione
      ricevutoGasolioTotale: new FormControl({ value: '0', disabled: true }),
      ricevutoBenzina: new FormControl({ value: '0', disabled: true }),
      ricevutoGasolioSerre: new FormControl({ value: '0', disabled: true }),
      trasferitoGasolioTotale: new FormControl({ value: '0', disabled: true }),
      trasferitoBenzina: new FormControl({ value: '0', disabled: true }),
      trasferitoGasolioSerre: new FormControl({ value: '0', disabled: true }),
      prelevatoGasolioTotale: new FormControl({ value: '0', disabled: true }),
      prelevatoBenzina: new FormControl({ value: '0', disabled: true }),
      prelevatoGasolioSerre: new FormControl({ value: '0', disabled: true }),
      disponibileGasolioTotale: new FormControl({ value: '0', disabled: true }),
      disponibileBenzina: new FormControl({ value: '0', disabled: true }),
      disponibileGasolioSerre: new FormControl({ value: '0', disabled: true }),
      accisaGasolioTotale: new FormControl({ value: '0', disabled: true }),
      accisaBenzina: new FormControl({ value: '0', disabled: true }),
      accisaGasolioSerre: new FormControl({ value: '0', disabled: true }),
      accisaMotivo: new FormControl({ value: null, disabled: this.READONLY_MODE }),
      // Terza sezione
      consumatoGasolioContoProprio: new FormControl({ value: null, disabled: this.READONLY_MODE }),
      consumatoGasolioContoTerzi: new FormControl({ value: null, disabled: this.READONLY_MODE }),
      consumatoGasolioTotale: new FormControl({ value: '0', disabled: true }),
      consumatoBenzina: new FormControl({ value: null, disabled: this.READONLY_MODE }),
      consumatoGasolioSerre: new FormControl({ value: null, disabled: this.READONLY_MODE }),
      rimanenzaDicembreGasolioContoProprio: new FormControl({ value: null, disabled: this.READONLY_MODE }),
      rimanenzaDicembreGasolioContoTerzi: new FormControl({ value: null, disabled: this.READONLY_MODE }),
      rimanenzaDicembreGasolioTotale: new FormControl({ value: '0', disabled: true }),
      rimanenzaDicembreBenzina: new FormControl({ value: null, disabled: this.READONLY_MODE }),
      rimanenzaDicembreGasolioSerre: new FormControl({ value: null, disabled: this.READONLY_MODE }),
      recuperoGasolioContoProprio: new FormControl({ value: null, disabled: this.READONLY_MODE }),
      recuperoGasolioContoTerzi: new FormControl({ value: null, disabled: this.READONLY_MODE }),
      recuperoGasolioTotale: new FormControl({ value: '0', disabled: true }),
      recuperoBenzina: new FormControl({ value: null, disabled: this.READONLY_MODE }),
      recuperoGasolioSerre: new FormControl({ value: null, disabled: this.READONLY_MODE }),
    });
  }

  // In base alle macchine dichiarate disabilito la colonna benzina o gasolio (come per la richiesta carburante)
  private disableFieldsByCarburate(fabbisognoDichiarato: FabbisognoDichiarato) {
    if (!fabbisognoDichiarato) {
      return;
    }
    if (!fabbisognoDichiarato.benzina) {
      this.consumiForm.get('consumatoBenzina').disable();
      this.consumiForm.get('rimanenzaDicembreBenzina').disable();
      this.consumiForm.get('recuperoBenzina').disable();
    }
    if (!fabbisognoDichiarato.gasolio) {
      this.consumiForm.get('consumatoGasolioContoProprio').disable();
      this.consumiForm.get('rimanenzaDicembreGasolioContoProprio').disable();
      this.consumiForm.get('recuperoGasolioContoProprio').disable();
    }
  }

  private saveConsuntivo(tipoConsuntivo: TipoConsuntivo, tipo: keyof typeof MotivazioneConsuntivoTipo, quantita: string, tipoCarburante: keyof typeof TipoCarburanteConsuntivo, motivo: MotivazioneConsuntivoDto) {
    return this.httpClientDichiarazioneConsumiUmaService.saveAllegatiConsuntivo(this.dichiarazioneConsumiDto.id.toString(), this.consumiBuilderService.buildConsuntivoDto(quantita, tipoCarburante, tipoConsuntivo, motivo), this.toFileByTipo(motivo.allegati, tipo));
  }

  private updateAndValidateMotivo(tipo: keyof typeof MotivazioneConsuntivoTipo, idConsuntivo: string) {
    switch (tipo) {
      case MotivazioneConsuntivoTipo.AMMISSIBILE_GASOLIO_TERZI: {
        this.motivoRecuperoAmmissibile.idConsuntivo = idConsuntivo;
        this.consumiForm.get("ammissibileGasolioTotale").updateValueAndValidity();
        break;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_GASOLIO_PROPRIO: {
        this.motivoRecuperoGasolioContoProprio.idConsuntivo = idConsuntivo;
        this.consumiForm.get("recuperoGasolioContoProprio").updateValueAndValidity();
        break;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_GASOLIO_TERZI: {
        this.motivoRecuperoGasolioContoTerzi.idConsuntivo = idConsuntivo;
        this.consumiForm.get("recuperoGasolioContoTerzi").updateValueAndValidity();
        break;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_BENZINA: {
        this.motivoRecuperoBenzina.idConsuntivo = idConsuntivo;
        this.consumiForm.get("recuperoBenzina").updateValueAndValidity();
        break;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_GASOLIO_SERRE: {
        this.motivoRecuperoGasolioSerre.idConsuntivo = idConsuntivo;
        this.consumiForm.get("recuperoGasolioSerre").updateValueAndValidity();
        break;
      }
      default: {
        break;
      }
    }
  }

  private toFileByTipo(allegatiMotivazione: Array<AllegatoMotivazioneView>, tipo: keyof typeof MotivazioneConsuntivoTipo): Array<File> {
    if (tipo === MotivazioneConsuntivoTipo.AMMISSIBILE_GASOLIO_TERZI) {
      return this.consumiBuilderService.toFileWithMetaDati(allegatiMotivazione, this.SEPARATOR);
    } else { // RECUPERO
      return this.consumiBuilderService.toFile(allegatiMotivazione);
    }
  }

  private recuperaIDConsuntivo(tipo: keyof typeof MotivazioneConsuntivoTipo): string {
    switch (tipo) {
      case MotivazioneConsuntivoTipo.AMMISSIBILE_GASOLIO_TERZI: {
        return this.motivoRecuperoAmmissibile.idConsuntivo;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_GASOLIO_PROPRIO: {
        return this.motivoRecuperoGasolioContoProprio.idConsuntivo;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_GASOLIO_TERZI: {
        return this.motivoRecuperoGasolioContoTerzi.idConsuntivo;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_BENZINA: {
        return this.motivoRecuperoBenzina.idConsuntivo;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_GASOLIO_SERRE: {
        return this.motivoRecuperoGasolioSerre.idConsuntivo;
      }
      default: {
        return null;
      }
    }
  }

  private svuotaAllegati(tipo: keyof typeof MotivazioneConsuntivoTipo) {
    switch (tipo) {
      case MotivazioneConsuntivoTipo.AMMISSIBILE_GASOLIO_TERZI: {
        this.motivoRecuperoAmmissibile = null;
        this.popupAllegatiMotivoConsumi.reset();
        this.consumiForm.get("ammissibileGasolioContoTerzi").setValue(null);
        this.consumiForm.get("ammissibileGasolioContoTerzi").updateValueAndValidity();
        break;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_GASOLIO_PROPRIO: {
        this.motivoRecuperoGasolioContoProprio = null;
        this.consumiForm.get("recuperoGasolioContoProprio").setValue(null);
        this.consumiForm.get("recuperoGasolioContoProprio").updateValueAndValidity();
        break;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_GASOLIO_TERZI: {
        this.motivoRecuperoGasolioContoTerzi = null;
        this.consumiForm.get("recuperoGasolioContoTerzi").setValue(null);
        this.consumiForm.get("recuperoGasolioContoTerzi").updateValueAndValidity();
        break;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_BENZINA: {
        this.motivoRecuperoBenzina = null;
        this.consumiForm.get("recuperoBenzina").setValue(null);
        this.consumiForm.get("recuperoBenzina").updateValueAndValidity();
        break;
      }
      case MotivazioneConsuntivoTipo.RECUPERO_GASOLIO_SERRE: {
        this.motivoRecuperoGasolioSerre = null;
        this.consumiForm.get("recuperoGasolioSerre").setValue(null);
        this.consumiForm.get("recuperoGasolioSerre").updateValueAndValidity();
        break;
      }
      default: {
        break;
      }
    }
  }

  private initDataConduzione() {
    if (this.dichiarazioneConsumiDto.dataConduzione) {
      // la data di default viene settata solo la prima volta che entro nella pagina
      this.dataConduzioneDefault = this.dataConduzioneDefault ? this.dataConduzioneDefault : new Date(this.dichiarazioneConsumiDto.dataConduzione);
      this.minDate = this.setMinDate();
      this.maxDate = this.setMaxDate();
      this.consumiForm.get("dataConduzione").setValue(new Date(this.dichiarazioneConsumiDto.dataConduzione));
    } else {
      console.error('NON è stato possibile ricavare dal servizio backend la data di conduzione!');
    }
  }

  private setMinDate(): Date {
    // anno in cui faccio la dichiarazione nello stesso anno della richiesta (== anno di campagna)
    if (this.dichiarazioneConsumiDto && new Date(this.dichiarazioneConsumiDto.dataConduzione).getFullYear() == this.dateUtilService.getCurrentYear()) {
      return this.dateUtilService.toDateFrom(1, 1, new Date(this.dichiarazioneConsumiDto.dataConduzione).getFullYear());
    } else { // anno in cui faccio la dichiarazione l'anno successivo (!= dall'anno di campagna)
      return this.dateUtilService.toDateFrom(1, 1, this.dateUtilService.getCurrentYear());
    }
  }

  private setMaxDate(): Date {
    // anno in cui faccio la dichiarazione nello stesso anno della richiesta == anno di campagna
    if (this.dichiarazioneConsumiDto && new Date(this.dichiarazioneConsumiDto.dataConduzione).getFullYear() == this.dateUtilService.getCurrentYear()) {
      return this.dateUtilService.getToday();
    } else { // anno in cui faccio la dichiarazione l'anno successivo != dall'anno di campagna
      return this.dateUtilService.toDateFrom(31, 12, new Date(this.dichiarazioneConsumiDto.dataConduzione).getFullYear());
    }
  }

  private confermaCambioData(d: Date) {
    const date = new Date(this.consumiForm.get("dataConduzione").value);
    const dateToUpdate: string = this.dateUtilService.toDateWithNoOffset(date).toISOString();
    this.updateDataConduzioneSubscription = this.httpClientDichiarazioneConsumiUmaService.updateDichiarazioneConsumi(this.dichiarazioneConsumiDto.id.toString(), this.consumiBuilderService.buildDichiarazioneConsumiPatch(null, dateToUpdate))
      .subscribe((res) => {
        this.dichiarazioneConsumiDto.dataConduzione = this.dateUtilService.toDateWithNoOffset(date);
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioConsuntiviOk));
        this.router.navigate([this.router.url]);
      }, error => {
        this.errorService.showError(error);
        this.consumiForm.get("dataConduzione").setValue(this.dataConduzioneDefault);
      });
  }

  private erroreEliminazioneDichiarazioneORettifica(error: ErrorDTO) {
    this.confirmationService.confirm({
      message: UMA_MESSAGES.rettificaIncompilazione,
      accept: () => {
        // Logica di elimina rettifica + prosegui con protocolla
        const filter = new DomandaUmaFilter();
        filter.cuaa = this.dichiarazioneConsumiDto.cuaa;
        filter.campagna = [this.dichiarazioneConsumiDto.campagnaRichiesta.toString()];
        filter.stati = [StatoDomandaUma.IN_COMPILAZIONE];
        filter.numeroElementiPagina = 1;
        this.getDomandeSubscription = this.httpClientDomandUmaService.getDomande(filter)
          .pipe(
            catchError((err) => {
              this.errorService.showError(err);
              return EMPTY;
            }),
            switchMap((richieste: Array<RichiestaCarburanteDto>) => {
              console.log('richieste:', richieste);
              return this.httpClientDomandUmaService.deleteDomandaById(richieste[0].id.toString());
            })
          ).subscribe(res1 => {
            this.popupProtocollaVisibile = true;
          }, error => this.errorService.showError(error));
      },
      reject: () => {
        this.confermaEliminaDichiarazione(); // dopo conferma navigazione verso presentazione istanze
      },
      key: "rettifica-error-dialog",
    });
  }

  private errorRichiestaObsoleta(error: ErrorDTO) {
    this.confirmationService.confirm({
      message: UMA_MESSAGES.richiestaObsoleta,
      accept: () => {
        this.confermaEliminaDichiarazione(); // dopo conferma navigazione verso presentazione istanze
      },
      reject: () => { },
      key: "rettifica-error-dialog",
    });
  }

  private confermaEliminaDichiarazione() {
    this.deleteSubscription = this.httpClientDichiarazioneConsumiUmaService.deleteDichiarazioneConsumi(this.dichiarazioneConsumiDto.id.toString())
      .subscribe(() => {
        this.messageService.add(A4gMessages.getToast("tst-delete", A4gSeverityMessage.success, UMA_MESSAGES.cancellazioneDichiarazioneConsumiOK));
      }, error => this.errorService.showError(error));
  }
}