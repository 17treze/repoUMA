import { AuthService } from './../../../../auth/auth.service';
import { StatoDichiarazioneConsumiEnum } from './../../../../a4g-common/classi/enums/uma/StatoDichiarazioneConsumi.enum';
import { HttpClientDichiarazioneConsumiUmaService } from './../../../core-uma/services/http-client-dichiarazione-consumi-uma.service';
import { DichiarazioneConsumiDto } from 'src/app/uma/core-uma/models/dto/DichiarazioneConsumiDto';
import { catchError, switchMap } from 'rxjs/operators';
import { PrelieviBuilderService } from 'src/app/uma/core-uma/services/builders/prelievi-builder.service';
import { PrelievoDto } from 'src/app/uma/core-uma/models/dto/PrelievoDto';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { StatoDomandaUma } from 'src/app/a4g-common/classi/enums/uma/StatoDomandaUma.enum';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
import { FormatConverterService } from './../../../shared-uma/services/format-converter.service';
import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MessageService, SelectItem } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { DomandaUmaFilter } from 'src/app/uma/core-uma/models/dto/DomandaUmaFilter';
import { PaginatorA4G } from 'src/app/a4g-common/interfaces/paginator.model';
import { HttpClientDomandaUmaService } from 'src/app/uma/core-uma/services/http-client-domanda-uma.service';
import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { PopupNuovoPrelievoComponent } from './popup-nuovo-prelievo/popup-nuovo-prelievo.component';
import { EMPTY, forkJoin, Subscription } from 'rxjs';
import { CarburanteDto } from 'src/app/uma/core-uma/models/dto/CarburanteDto';
import { A4gMultiTableColumn, AlignTypeEnum, ColumnTypeEnum } from 'src/app/a4g-common/a4g-multi-table/a4g-multi-table-model';
import { HttpClientDistributoriService } from 'src/app/uma/core-uma/services/http-client-distributori.service';
import { PrelievoViewModel } from 'src/app/uma/core-uma/models/viewModels/PrelievoViewModel';
import { NullCheckService } from 'src/app/uma/shared-uma/services/null-check.service';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { CalendarService } from 'src/app/a4g-common/services/calendar.service';
import { HttpClientUtenteService } from 'src/app/uma/shared-uma/services/http-client-utente.service';
import { DistributoriBuilderService } from 'src/app/uma/core-uma/services/builders/distributori-builder.service';
import { UtenteDistributoreDto } from 'src/app/uma/core-uma/models/dto/DistributoreDto';
import { DichiarazioneConsumiFilter } from 'src/app/uma/core-uma/models/dto/DichiarazioneConsumiFilter';
import { CarburanteTotale } from 'src/app/uma/core-uma/models/dto/CarburanteTotale';
import { FormService } from 'src/app/a4g-common/services/form.service';
import * as _ from 'lodash';
@Component({
  selector: 'app-ricerca-aziende',
  templateUrl: './ricerca-aziende.component.html',
  styleUrls: ['./ricerca-aziende.component.scss']
})
export class RicercaAziendeComponent implements OnInit, OnDestroy {

  @ViewChild("popupNuovoPrelievo")
  popupNuovoPrelievo: PopupNuovoPrelievoComponent;

  a4gMessages = A4gMessages;
  umaMessages = UMA_MESSAGES;

  ricercaForm: FormGroup;
  querySearch: { cuaa: string, idDomanda: string }; /** modello della form di ricerca */
  prelevabile: CarburanteDto;                       /** massima quantita di carburante prelevabile */
  idDomanda: number;                                /** id richiesta di carburante cercata */
  distributoriVM: Array<SelectItem>;
  isDichiarazioneConsumiAutorizzataPresent: boolean; /** se esiste una dichiarazione autorizzata modifica/inserimento/cancellazione del prelievo non sono possibili */
  campagna: string;

  // Tabella visualizzazione prelievi
  cols: Array<A4gMultiTableColumn>;
  isChangedInputTable: boolean;
  isValidFormTable: boolean;
  datasourcePrelievi: Array<PrelievoViewModel>;

  // Subscription
  prelievoSubscription: Subscription;
  prelieviAndRichiesteSubscription: Subscription;
  deletePrelievoSubscription: Subscription;
  getDistributoriSubscription: Subscription;

  constructor(
    private httpClientDomandaUmaService: HttpClientDomandaUmaService,
    private messageService: MessageService,
    private errorService: ErrorService,
    private formatConverterService: FormatConverterService,
    private dateUtilService: DateUtilService,
    private formService: FormService,
    private httpClientDistributoriService: HttpClientDistributoriService,
    private prelieviBuilderService: PrelieviBuilderService,
    private nullCheckService: NullCheckService,
    private calendarService: CalendarService,
    private httpClientUtenteService: HttpClientUtenteService,
    private distributoriBuilderService: DistributoriBuilderService,
    private httpClientDichiarazioneConsumiUmaService: HttpClientDichiarazioneConsumiUmaService,
    private authService: AuthService
  ) {
  }

  ngOnInit() {
    this.initForm();
    this.initData();
  }

  ngOnDestroy(): void {
    if (this.prelievoSubscription) {
      this.prelievoSubscription.unsubscribe();
    }
    if (this.prelieviAndRichiesteSubscription) {
      this.prelieviAndRichiesteSubscription.unsubscribe();
    }
    if (this.deletePrelievoSubscription) {
      this.deletePrelievoSubscription.unsubscribe();
    }
    if (this.getDistributoriSubscription) {
      this.getDistributoriSubscription.unsubscribe();
    }
  }

  onSubmit() {
    this.formService.validateForm(this.ricercaForm);
    if (!this.ricercaForm.valid) {
      this.messageService.add(A4gMessages.getToast('tst-ricerca', A4gSeverityMessage.error, UMA_MESSAGES.datiNotCorretti));
      return;
    }
    this.prelevabile.benzina = null;
    this.prelevabile.gasolio = null;
    this.prelevabile.gasolioSerre = null;
    this.idDomanda = null;
    this.datasourcePrelievi = [];
    this.getRichiesteAndGetPrelievi(this.ricercaForm.value);
  }

  nuovoPrelievo() {
    if (!this.isDichiarazioneConsumiAutorizzataPresent) {
      this.popupNuovoPrelievo.open({ idRichiesta: this.idDomanda.toString(), prelievo: null, prelevabile: this.prelevabile, isDichiarazioneConsumiAutorizzata: false });
    } else {
      this.messageService.add(A4gMessages.getToast('tst-ricerca', A4gSeverityMessage.error, UMA_MESSAGES.esisteDichiarazioneConsumiAutorizzata));
    }

  }

  onChangePrelevabile($event: CarburanteDto) {
    this.prelevabile = $event;
  }

  onChangeForm($event: { isChanged: boolean, isValid: boolean }) {
    // setto a changed, quando scatta l'onChange su un qualsiasi input della tabella
    this.isChangedInputTable = $event.isChanged;
    // setto a valida quando il formato dei valori inseriti negli input della tabella è corretto
    this.isValidFormTable = $event.isValid;
  }

  onClickIcon($event: { element: PrelievoViewModel, col: A4gMultiTableColumn }) {
    if ($event.col.buttonOpts.id === 'elimina') {
      if (!this.isDichiarazioneConsumiAutorizzataPresent) {
        this.deletePrelievo($event.element);
      } else {
        this.messageService.add(A4gMessages.getToast('tst-ricerca', A4gSeverityMessage.error, UMA_MESSAGES.esisteDichiarazioneConsumiAutorizzata));
      }
    } else if ($event.col.buttonOpts.id === 'modifica') {
      this.editPrelievo($event.element);
    }
  }

  onCloseDialog($event: { type: 'SAVE' | 'UPDATE' | 'KO', model: PrelievoDto }) {
    if ($event.type === 'SAVE') {
      this.messageService.add(A4gMessages.getToast('tst-ricerca', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOK));
      this.getRichiesteAndGetPrelievi(this.querySearch);
    } else if ($event.type === 'UPDATE') {
      this.messageService.add(A4gMessages.getToast('tst-ricerca', A4gSeverityMessage.success, UMA_MESSAGES.aggiornamentoOK));
      this.getRichiesteAndGetPrelievi(this.querySearch);
    }
  }

  ricercaDomanda(query?: { cuaa: string, idDomanda: string }) {
    const params: DomandaUmaFilter = {
      id: query ? this.formatConverterService.toNumber(query.idDomanda) : this.formatConverterService.toNumber(this.ricercaForm.get('idDomanda').value),
      cuaa: query ? query.cuaa : this.ricercaForm.get('cuaa').value,
      campagna: [this.dateUtilService.getCurrentYear().toString()],
      stati: [StatoDomandaUma.AUTORIZZATA],
      numeroElementiPagina: 1,
      pagina: 0
    };
    return this.httpClientDomandaUmaService.getDomandePaged(params);
  }

  nuovaRicerca() {
    this.idDomanda = null;
    this.datasourcePrelievi = [];
    this.ricercaForm.reset();
  }

  private getPrelievi(cuaa: string) {
    return this.httpClientDomandaUmaService.getPrelieviByCuaaAndCampagna(cuaa, this.dateUtilService.getCurrentYear().toString());
  }

  private getRichiesteAndGetPrelievi(query?: { cuaa: string, idDomanda: string }) {
    this.prelieviAndRichiesteSubscription = forkJoin([this.ricercaDomanda(query), this.getPrelievi(query.cuaa), this.getDichiarazioneConsumiAutorizzata(query)])
      .subscribe(([richieste, prelievi, dichiarazioni]: [PaginatorA4G<Array<RichiestaCarburanteDto>>, CarburanteTotale<PrelievoDto>, PaginatorA4G<Array<DichiarazioneConsumiDto>>]) => {
        if (this.setRichieste(richieste)) {
          this.setPrelievi(prelievi.dati);
          if (dichiarazioni != null && dichiarazioni.count > 0) {
            this.isDichiarazioneConsumiAutorizzataPresent = true;
          } else {
            this.isDichiarazioneConsumiAutorizzataPresent = false;
          }
        } else {
          this.datasourcePrelievi = [];
        }
      }, (error: ErrorDTO) => {
        this.messageService.clear(); /** rimozione messaggio di abilitazioni su 403 dell'interceptor */
        this.errorService.showErrorWithMessage(UMA_MESSAGES.datiNotCorretti, 'tst-ricerca');
      });
  }

  getDichiarazioneConsumiAutorizzata(query?: { cuaa: string, idDomanda: string }) {
    const params: DichiarazioneConsumiFilter = {
      cuaa: query ? query.cuaa : this.ricercaForm.get('cuaa').value,
      campagna: [this.dateUtilService.getCurrentYear().toString()],
      stati: [StatoDichiarazioneConsumiEnum.PROTOCOLLATA],
      numeroElementiPagina: 1,
      pagina: 0
    };
    return this.httpClientDichiarazioneConsumiUmaService.getDichiarazioniConsumiPaged(params);
  }

  private editPrelievo(prelievoSelected: PrelievoViewModel) {
    if (!this.nullCheckService.isEmptyObject(this.prelevabile)) {
      this.popupNuovoPrelievo.open({
        idRichiesta: this.idDomanda.toString(), prelievo: prelievoSelected, prelevabile: {
          gasolio: this.prelevabile.gasolio + prelievoSelected.gasolio,
          benzina: this.prelevabile.benzina + prelievoSelected.benzina, gasolioSerre: this.prelevabile.gasolioSerre + prelievoSelected.gasolioSerre
        }, isDichiarazioneConsumiAutorizzata: this.isDichiarazioneConsumiAutorizzataPresent
      });
    } else {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.prelevabileNotPresent, 'tst-ricerca');
    }
  }

  private setRichieste(richieste: PaginatorA4G<Array<RichiestaCarburanteDto>>) {
    if (richieste && richieste.count > 0) {
      this.querySearch = { cuaa: this.ricercaForm.get('cuaa').value, idDomanda: richieste.risultati[0].id.toString() };
      this.idDomanda = richieste.risultati[0].id;
      return true;
    } else {
      this.messageService.clear(); /** rimoz ione messaggio di abilitazioni su 403 dell'interceptor */
      this.errorService.showErrorWithMessage(UMA_MESSAGES.datiNotCorretti, 'tst-ricerca');
      return false;
    }
  }

  private setPrelievi(prelievi: Array<PrelievoDto>) {
    if (prelievi && prelievi.length) {
      const dsPrelievi = this.prelieviBuilderService.prelieviDtoToPrelieviViewModelBuilder(prelievi);
      this.datasourcePrelievi = this.prelieviBuilderService.prelieviViewModelFilterByUtente(dsPrelievi, this.distributoriVM);
    } else {
      this.datasourcePrelievi = [];
    }
  }

  private deletePrelievo(prelievoSelected: PrelievoViewModel) {
    this.deletePrelievoSubscription = this.httpClientDistributoriService.deletePrelievoByIdDistributore(prelievoSelected.distributoreId.toString(), prelievoSelected.id)
      .pipe(
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err, 'tst-ricerca');
          return EMPTY;
        }),
        switchMap((res: any) => {
          this.messageService.add(A4gMessages.getToast('tst-ricerca', A4gSeverityMessage.success, UMA_MESSAGES.cancellazionePrelievoOK));
          return forkJoin([this.ricercaDomanda(this.querySearch), this.getPrelievi(this.querySearch.cuaa)]);
        }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err, 'tst-ricerca');
          return EMPTY;
        })
      )
      .subscribe(([richieste, prelievi]: [PaginatorA4G<Array<RichiestaCarburanteDto>>, CarburanteTotale<PrelievoDto>]) => {
        // Aggiornamento dati dopo eliminazione
        if (this.setRichieste(richieste)) {
          this.setPrelievi(prelievi.dati);
        } else {
          this.datasourcePrelievi = [];
        }
      }, (error: ErrorDTO) => this.errorService.showError(error, 'tst-ricerca'));
  }

  private initForm() {
    this.ricercaForm = new FormGroup({
      idDomanda: new FormControl(null, [Validators.required]),
      cuaa: new FormControl(null, [Validators.required])
    });
  }

  private initData() {
    this.campagna = this.dateUtilService.getCurrentYear().toString()
    this.prelevabile = {} as CarburanteDto;
    this.cols = this.defineColumns();
    this.isValidFormTable = true;
    this.idDomanda = null;
    this.calendarService.configITA();
    this.distributoriVM = [];
    this.getDistributoriSubscription = this.httpClientUtenteService.getDistributori(this.campagna).subscribe((distributori: Array<UtenteDistributoreDto>) => {
      const unorderedList: Array<SelectItem> = this.distributoriBuilderService.distributoreDtoToDistributoreVmBuilder(distributori);
      this.distributoriVM = _.orderBy(unorderedList, ['label'], ['asc']);
      this.distributoriVM.unshift({ label: 'Seleziona un distributore', value: null });
    }, (error: ErrorDTO) => this.errorService.showError(error, 'tst-ricerca'));
  }

  private defineColumns(): Array<A4gMultiTableColumn> {
    return [
      {
        field: '',
        header: '',
        sortable: false,
        width: '5%',
        alignCellHorizontal: AlignTypeEnum.CENTER,
        alignHeader: AlignTypeEnum.CENTER,
        buttonOpts: { id: 'elimina', icon: 'pi pi-times-circle', hiddenBy: 'isConsegnato' },
        type: ColumnTypeEnum.ICON_BUTTON_CIRCLE
      },
      {
        field: 'data',
        header: 'Data prelievo',
        sortable: true,
        disabled: false,
        width: '12%',
        alignCellHorizontal: AlignTypeEnum.LEFT,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'distributoreNomeEsteso',
        header: 'Distributore',
        sortable: true,
        width: '48%',
        alignCellHorizontal: AlignTypeEnum.LEFT,
        alignHeader: AlignTypeEnum.LEFT,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'gasolio',
        header: 'Gasolio',
        sortable: false,
        width: '10%',
        inputOpts: { pattern: '^([0-9])+$', maxLength: '7' },
        alignCellHorizontal: AlignTypeEnum.RIGHT,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'benzina',
        header: 'Benzina',
        sortable: false,
        width: '10%',
        inputOpts: { pattern: '^([0-9])+$', maxLength: '7' },
        alignCellHorizontal: AlignTypeEnum.RIGHT,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'gasolioSerre',
        header: 'Gasolio serre',
        sortable: false,
        width: '10%',
        inputOpts: { pattern: '^([0-9])+$', maxLength: '7' },
        alignCellHorizontal: AlignTypeEnum.RIGHT,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: '',
        header: '',
        sortable: false,
        width: '5%',
        alignCellHorizontal: AlignTypeEnum.CENTER,
        alignHeader: AlignTypeEnum.CENTER,
        buttonOpts: { id: 'modifica', icon: 'pi pi-pencil' },
        type: ColumnTypeEnum.ICON_BUTTON_SQUARE
      }
    ];
  }

  public disabledNuovoPrelievo() {
    return this.authService.userSelectedRole === AuthService.roleViewerPAT ||
      this.authService.userSelectedRole === AuthService.roleAltroEnte;
  }

}