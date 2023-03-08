import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { MenuItem, SelectItem } from 'primeng/api';
import { Subject } from 'rxjs';
import { Paginazione, SortDirection } from 'src/app/a4g-common/utility/paginazione';
import { TipoIstruttoriaEnum } from '../../classi/TipoIstruttoriaEnum';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { AutocompleteElencoDomandeParams } from '../../domain/autocomplete-elenco-domande';
import { AnomaliaInterSostegnoEnum, AnomalieErrorCalcoloKoEnum, AnomalieErrorLiquidabileKoEnum, AnomalieInfoEnum, AnomalieWarnDebitiEnum, AnomalieWarnEnum, IstruttoriaFiltroRicercaDomande, RiservaEnum, YesNoEnum } from '../../istruttoria-shared-filter/istruttoria-filtro-ricerca-domande';
import { ElencoDomandeService } from '../../services/elenco-domande.service';
import { TabDisaccoppiatoCommonComponent } from '../TabDisaccoppiatoCommonComponent';
import { MenuItemAnomalieInterSost } from './menu-item-anomalie-inter-sost';


@Component({
  selector: 'app-filtri-disaccoppiato',
  templateUrl: './filtri-disaccoppiato.component.html',
  styleUrls: ['./filtri-disaccoppiato.component.css']
})
export class FiltriDisaccoppiatoComponent implements OnInit {
  @Input() autoCompleteParams: AutocompleteElencoDomandeParams;
  @Input() resultsNumber: number;
  @Output() selectedFiltersSubmit: EventEmitter<IstruttoriaFiltroRicercaDomande> = new EventEmitter();
  public filtersFormGroup: FormGroup;
  public cuaaS: string[];
  public denominazioneS: string[];
  private componentDestroyed$: Subject<boolean> = new Subject();

  public anomalieWarn: MenuItem[] = [
    { title: 'BPS: Riduzioni', id: AnomalieWarnEnum.BPS_RIDUZIONI },
    { title: 'BPS: Sanzioni <10%', id: AnomalieWarnEnum.BPS_SANZIONI_INF_10 },
    { title: 'BPS: Sanzioni >10%', id: AnomalieWarnEnum.BPS_SANZIONI_SUP_10 },
    { title: 'Greening: Riduzioni', id: AnomalieWarnEnum.GREENING_RIDUZIONI },
    { title: 'Greening: Sanzioni', id: AnomalieWarnEnum.GREENING_SANZIONI },
    { title: 'Giovane: No requisiti', id: AnomalieWarnEnum.GIOVANE_NO_REQUISITI },
    { title: 'Giovane: Riduzioni', id: AnomalieWarnEnum.GIOVANE_RIDUZIONI },
    { title: 'Giovane: Sanzioni', id: AnomalieWarnEnum.GIOVANE_SANZIONI },
    //{ title: 'Importo da recuperare', id: AnomalieWarnEnum.IMPORTO_DA_RECUPERARE },
    { title: 'Riduzione da capping', id: AnomalieWarnEnum.RIDUZIONE_DA_CAPPING },
    { title: 'Riduzione per ritardo', id: AnomalieWarnEnum.RIDUZIONE_PER_RITARDO }
  ];

  public anomalieWarnDebiti: MenuItem[] = [
    { title: 'Importo da recuperare', id: AnomalieWarnDebitiEnum.IMPORTO_DA_RECUPERARE },
    { title: 'Importo nullo', id: AnomalieWarnDebitiEnum.IMPORTO_NULLO },
    { title: 'Importo irrilevante', id: AnomalieWarnDebitiEnum.IMPORTO_IRRILEVANTE },
  ];

  public anomalieInfo: MenuItem[] = [
    { title: 'BPS: Anomalie coordinamento', id: AnomalieInfoEnum.BPS_ANOMALIE_COORDINAMENTO },
    { title: 'BPS: Presenza MAN', id: AnomalieInfoEnum.BPS_PRESENZA_MAN },
    { title: 'BPS: Superficie minima', id: AnomalieInfoEnum.BPS_SUPERFICE_MINIMA },
    { title: 'BPS: sconto sanzione', id: AnomalieInfoEnum.BPS_SCONTO_SANZIONE },
    { title: 'BPS: recidiva', id: AnomalieInfoEnum.BPS_RECIDIVA },
    { title: 'BPS: recupero sconto', id: AnomalieInfoEnum.BPS_RECUPERO_SCONTO },
    { title: 'Greening: rinuncia', id: AnomalieInfoEnum.GREENING_RINUNCIA },
    { title: 'Greening: biologica', id: AnomalieInfoEnum.GREENING_BIOLOGICA },
    { title: 'Greening: No impegni', id: AnomalieInfoEnum.GREENING_NO_IMPEGNI },
    { title: 'Greening: DIV', id: AnomalieInfoEnum.GREENING_DIV },
    { title: 'Greening: DIV+EFA', id: AnomalieInfoEnum.GREENING_DIV_EFA },
    { title: 'Greening: esente', id: AnomalieInfoEnum.GREENING_ESENTE },
    { title: 'Greening: esente EFA', id: AnomalieInfoEnum.GREENING_ESENTE_EFA },
    { title: 'Giovane: sconto sanzione', id: AnomalieInfoEnum.GIOVANE_SCONTO_SANZIONE },
    { title: 'Giovane: recidiva', id: AnomalieInfoEnum.GIOVANE_RECIDIVA },
    { title: 'Giovane: recupero sconto', id: AnomalieInfoEnum.GIOVANE_RECUPERO_SCONTO },
    { title: 'Riduzioni senza sanzioni', id: AnomalieInfoEnum.RIDUZIONI_SENZA_SANZIONI }
  ];

  public anomalieErrorControlliCalcoloKoNonAmmissibile: MenuItem[] = [
    { title: 'No info agricoltore attivo', id: AnomalieErrorCalcoloKoEnum.NO_INFO_AGRICOLTORE_ATTIVO },
    { title: 'Agricoltore non attivo', id: AnomalieErrorCalcoloKoEnum.AGRICOLTORE_NON_ATTIVO },
    { title: 'Nessun titolo presente', id: AnomalieErrorCalcoloKoEnum.NESSUN_TITOLO_PRESENTE },
    { title: 'Superficie minima richiesta', id: AnomalieErrorCalcoloKoEnum.SUPERFICIE_MINIMA_RICHIESTA },
    { title: 'Controllo in loco aperto', id: AnomalieErrorCalcoloKoEnum.CONTROLLO_IN_LOCO_APERTO },
    { title: 'Inserire informazioni mantenimento', id: AnomalieErrorCalcoloKoEnum.INSERIRE_INFORMAZIONI_MANTENIMENTO },
    { title: 'Domanda anno scorso non liquidata', id: AnomalieErrorCalcoloKoEnum.DOMANDA_ANNO_SCORSO_NON_LIQUIDATA },
    { title: 'Importo da recuperare', id: AnomalieErrorCalcoloKoEnum.IMPORTO_DA_RECUPERARE }
  ];

  public anomalieErrorControlliLiquidabileKo: MenuItem[] = [
    { title: 'IBAN non valido', id: AnomalieErrorLiquidabileKoEnum.IBAN_NON_VALIDO },
    { title: 'Agricoltore deceduto', id: AnomalieErrorLiquidabileKoEnum.AGRICOLTORE_DECEDUTO },
    { title: 'Azienda sospesa dai pagamenti', id: AnomalieErrorLiquidabileKoEnum.AZIENDA_SOSPESA_DAI_PAGAMENTI },
    { title: 'Dati erede non certificati', id: AnomalieErrorLiquidabileKoEnum.DATI_EREDE_NON_CERTIFICATI },
  ];

  public anomalieWarningDebiti: MenuItem[] = [
     { title: 'Importo da recuperare', id: AnomalieWarnDebitiEnum.IMPORTO_DA_RECUPERARE },
     { title: 'Importo nullo', id: AnomalieWarnDebitiEnum.IMPORTO_NULLO },
     { title: 'Importo Irrilevante', id: AnomalieWarnDebitiEnum.IMPORTO_IRRILEVANTE }
  ];

  

  anomalieInterSostegno: MenuItemAnomalieInterSost[] = [
    { title: 'No Importo minimo', id: AnomaliaInterSostegnoEnum.NO_IMPORTO_MINIMO_ERROR, livello: 'ERROR' },
    { title: 'Importo maggiore di 25000', id: AnomaliaInterSostegnoEnum.IMPORTO_MAGGIORE_DI_25000_ERROR, livello: 'ERROR' },
    { title: 'Importo minore di 25000', id: AnomaliaInterSostegnoEnum.IMPORTO_MINORE_DI_25000_INFO, livello: 'INFO' },
    { title: 'No dichiarazione antimafia', id: AnomaliaInterSostegnoEnum.NO_DICHIARAZIONE_ANTIMAFIA_INFO, livello: 'INFO' }
  ];
  
  public yesNo: SelectItem[] = [
    { label: this.translateService.instant('DOMANDA_UNICA_FILTRI.all'), value: null },
    { label: this.translateService.instant('DOMANDA_UNICA_FILTRI.yes'), value: YesNoEnum.YES },
    { label: this.translateService.instant('DOMANDA_UNICA_FILTRI.no'), value: YesNoEnum.NO }
  ];

  public yesNoIntegrazione: SelectItem[] = [
    { label: this.translateService.instant('DOMANDA_UNICA_FILTRI.all'), value: null },
    { label: this.translateService.instant('DOMANDA_UNICA_FILTRI.integrazioneYes'), value: YesNoEnum.YES },
    { label: this.translateService.instant('DOMANDA_UNICA_FILTRI.integrazioneNo'), value: YesNoEnum.NO }
  ]

  public riservaLabel: SelectItem[] = [
    { label: this.translateService.instant('DOMANDA_UNICA_FILTRI.all'), value: null},
    { label: 'Fattispecie A', value: RiservaEnum.A_GIOVANE_AGRICOLTORE },
    { label: 'Fattispecie B', value: RiservaEnum.B_NUOVO_AGRICOLTORE },
    { label: 'Fattispecie C', value: RiservaEnum.C_ABBANDONO_TERRE },
    { label: 'Fattispecie D', value: RiservaEnum.D_COMPENSAZIONE_SVANTAGGI_SPECIFICI },
    { label: 'Fattispecie F', value: RiservaEnum.F_PROVVEDIMENTI_AMMINISTRATIVI_DECISIONI_GIUDIZIARIE },
    { label: this.translateService.instant('DOMANDA_UNICA_FILTRI.no'), value: RiservaEnum.NON_RICHIESTA }
  ];

  constructor(
    private route: ActivatedRoute,
    private elencoDomandeService: ElencoDomandeService,
    private translateService: TranslateService
  ) {
    let filtersFormControls = {
      'cuaaFormControl': new FormControl(),
      'denominazioneFormControl': new FormControl(),
      'numeroDomandaFormControl': new FormControl(),
      'selectedCampioneFormControl': new FormControl(null),
      'selectedGiovaneFormControl': new FormControl(null),
      'selectedPascoliFormControl': new FormControl(null),
      'selectedBloccataBoolFormControl': new FormControl(null),
      'selectedAnomalieFormControl': new FormControl(null),
      'selectedRiservaFormControl': new FormControl(null),
      'selectedAnomalieWarnFormControl': new FormControl(),
      'selectedAnomalieInfoFormControl': new FormControl(),
      'selectedAnomalieErrorFormControl': new FormControl(),
      'selectedAnomalieInterSostegnoFormControl': new FormControl([]),
      'selectedErroreCalcoloFormControl': new FormControl(null),
      'selectedIntegrazioneFormControl': new FormControl(null)
    };
    this.filtersFormGroup = new FormGroup(filtersFormControls);
  }

  public isShowAnomalieErrorControlliCalcoloKoNonAmmissibile(): boolean {
    return this.autoCompleteParams.statoSostegno === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO
      || this.autoCompleteParams.statoSostegno === StatoIstruttoriaEnum.NON_AMMISSIBILE;
  }
  
  public isShowAnomalieInterSostegno(): boolean {
    return this.autoCompleteParams.statoSostegno === StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO;
  }

  public isShowAnomalieErrorControlliLiquidabileKo(): boolean {
    return this.autoCompleteParams.statoSostegno === StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO;
  }

  public isShowAnomalieWarningDebiti():boolean {
    return this.autoCompleteParams.statoSostegno === StatoIstruttoriaEnum.DEBITI;
  }


  public getAnomalieErrorControlliOptions(): MenuItem[] {
    if (this.isShowAnomalieErrorControlliCalcoloKoNonAmmissibile()) {
      return this.anomalieErrorControlliCalcoloKoNonAmmissibile;
    } else if (this.isShowAnomalieErrorControlliLiquidabileKo()) {
      return this.anomalieErrorControlliLiquidabileKo;
    }
  }

  public getAnomalieWarningControlliOption(): MenuItem[] {
    if (this.isShowAnomalieWarningDebiti()) {
      return this.anomalieWarnDebiti;
    } else {
      return this.anomalieWarn;
    }
  }

  public isShowAnomalieErrorControlli(): boolean {
    return this.isShowAnomalieErrorControlliCalcoloKoNonAmmissibile() || this.isShowAnomalieErrorControlliLiquidabileKo();
  }

  public isShowAnomalieWarningControlli(): boolean {
    return this.isShowControlliCalcolo() || this.isShowAnomalieWarningDebiti();
  }

  public isShowControlliCalcolo(): boolean {
    return this.autoCompleteParams.statoSostegno === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK;
  }

  public isShowBloccato() {
    let state: StatoIstruttoriaEnum = StatoIstruttoriaEnum[this.autoCompleteParams.statoSostegno];
    return TabDisaccoppiatoCommonComponent.isShowBloccatoByState(state);
  }

  public isShowErroriDiCalcolo(): boolean {
    let state: StatoIstruttoriaEnum = StatoIstruttoriaEnum[this.autoCompleteParams.statoSostegno];
    return TabDisaccoppiatoCommonComponent.isShowErroriByState(state);
  }

  public isShowIntegrazione(): boolean {
    return this.autoCompleteParams.statoSostegno === StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO
    && this.autoCompleteParams.tipo === TipoIstruttoriaEnum.SALDO;
  }

  ngOnInit() {
    this.autoCompleteParams.statoSostegno = this.route.routeConfig.path;
  }

  public onSubmit(): void {
    let istruttoriaFiltroRicercaDomande: IstruttoriaFiltroRicercaDomande = new IstruttoriaFiltroRicercaDomande();
    istruttoriaFiltroRicercaDomande.cuaa = this.filtersFormGroup.controls.cuaaFormControl.value;
    let numeroDomandaFilter = this.filtersFormGroup.controls.numeroDomandaFormControl.value;
    if (!numeroDomandaFilter) {
      istruttoriaFiltroRicercaDomande.numero_domanda = null;
    } else {
      istruttoriaFiltroRicercaDomande.numero_domanda = Number.parseInt(numeroDomandaFilter);
    }
    istruttoriaFiltroRicercaDomande.denominazione = this.filtersFormGroup.controls.denominazioneFormControl.value;
    istruttoriaFiltroRicercaDomande.campione = this.filtersFormGroup.controls.selectedCampioneFormControl.value;
    istruttoriaFiltroRicercaDomande.giovane = this.filtersFormGroup.controls.selectedGiovaneFormControl.value;
    istruttoriaFiltroRicercaDomande.pascoli = this.filtersFormGroup.controls.selectedPascoliFormControl.value;
    istruttoriaFiltroRicercaDomande.bloccataBool = this.filtersFormGroup.controls.selectedBloccataBoolFormControl.value;
    istruttoriaFiltroRicercaDomande.erroreCalcolo = this.filtersFormGroup.controls.selectedErroreCalcoloFormControl.value;
    istruttoriaFiltroRicercaDomande.anomalie = this.filtersFormGroup.controls.selectedAnomalieFormControl.value;
    istruttoriaFiltroRicercaDomande.integrazione = this.filtersFormGroup.controls.selectedIntegrazioneFormControl.value;

    if (this.autoCompleteParams.statoSostegno === StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO) {
      // caso in cui dobbiamo smistare le anomalie intersostegno in warning ed error
      istruttoriaFiltroRicercaDomande.anomalieWARNING = [];
      istruttoriaFiltroRicercaDomande.anomalieINFO = [];
      istruttoriaFiltroRicercaDomande.anomalieERROR = [];
      this.filtersFormGroup.controls.selectedAnomalieInterSostegnoFormControl.value.map(anom => {
        // smisto le anomalie in error e info in base al livello
        if (anom.livello === 'ERROR') {
          istruttoriaFiltroRicercaDomande.anomalieERROR.push(anom.id.toString());
        }
        if (anom.livello === 'INFO') {
          istruttoriaFiltroRicercaDomande.anomalieINFO.push(anom.id.toString());
        }
        return anom;
      });
    } else { // caso standard
      let selectedAnomalieWarn = this.filtersFormGroup.controls.selectedAnomalieWarnFormControl.value;
      let selectedAnomalieInfo = this.filtersFormGroup.controls.selectedAnomalieInfoFormControl.value;
      let selectedAnomalieError = this.filtersFormGroup.controls.selectedAnomalieErrorFormControl.value;
      istruttoriaFiltroRicercaDomande.anomalieWARNING = !selectedAnomalieWarn ? [] : selectedAnomalieWarn.map(anom => anom.id);
      istruttoriaFiltroRicercaDomande.anomalieINFO = !selectedAnomalieInfo ? [] : selectedAnomalieInfo.map(anom => anom.id);
      istruttoriaFiltroRicercaDomande.anomalieERROR = !selectedAnomalieError ? [] : selectedAnomalieError.map(anom => anom.id);
    }
    istruttoriaFiltroRicercaDomande.riservaNazionale = this.filtersFormGroup.controls.selectedRiservaFormControl.value;
    this.selectedFiltersSubmit.emit(istruttoriaFiltroRicercaDomande);
  }

  public inputFilterCuaaUpdate(event: any): void {
    let paginazione: Paginazione = Paginazione.of(
      0, 50, 'id', SortDirection.ASC);
    this.elencoDomandeService.getCuaaSuggestionsAutocomplete(
      this.autoCompleteParams.statoSostegno,
      this.autoCompleteParams.sostegno,
      this.autoCompleteParams.annoCampagna,
      event.query,
      this.autoCompleteParams.tipo,
      paginazione).subscribe(res => this.cuaaS = res.risultati);
  }

  public inputFilterRagioneSocialeUpdate(event: any): void {
    let paginazione: Paginazione = Paginazione.of(
      0, 50, 'id', SortDirection.ASC);
    this.elencoDomandeService.getRagioneSocialeSuggestionsAutocomplete(
      this.autoCompleteParams.statoSostegno,
      this.autoCompleteParams.sostegno,
      this.autoCompleteParams.annoCampagna,
      event.query,
      this.autoCompleteParams.tipo,
      paginazione).subscribe(res => this.denominazioneS = res.risultati);
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

}
