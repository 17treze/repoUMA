import { TrasferimentoDto } from 'src/app/uma/core-uma/models/dto/CarburanteRicevutoDto';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Location } from '@angular/common';
import { EMPTY, forkJoin, Subscription } from 'rxjs';
import { IndiceUmaService } from 'src/app/uma/core-uma/services/indice-uma.service';
import { ActivatedRoute, Params } from '@angular/router';
import { A4gMultiTableColumn, FontTypeEnum, AlignTypeEnum, ColumnTypeEnum } from 'src/app/a4g-common/a4g-multi-table/a4g-multi-table-model';
import { catchError, switchMap } from 'rxjs/operators';
import { HttpClientDichiarazioneConsumiUmaService } from 'src/app/uma/core-uma/services/http-client-dichiarazione-consumi-uma.service';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { CarburanteTotale } from 'src/app/uma/core-uma/models/dto/CarburanteTotale';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { TrasferitiBuilderService } from 'src/app/uma/core-uma/services/builders/trasferiti-builder.service';
import { HttpClientTrasferimentiCarburanteService } from 'src/app/uma/core-uma/services/http-client-trasferimenti-carburante.service';
import { TipoIntestazioneUma } from 'src/app/a4g-common/classi/enums/uma/TipoDocumentoUma.enum';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { ConfirmationService, MessageService } from 'primeng/api';
import { TrasferitoViewModel } from 'src/app/uma/core-uma/models/viewModels/TrasferitoViewModel';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { PopupRicercaRichiestaCarburanteComponent } from '../popup-ricerca-richiesta-carburante/popup-ricerca-richiesta-carburante.component';
import { HttpClientDomandaUmaService } from 'src/app/uma/core-uma/services/http-client-domanda-uma.service';
import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { StatoDichiarazioneConsumiEnum } from 'src/app/a4g-common/classi/enums/uma/StatoDichiarazioneConsumi.enum';
import { PaginatorA4G } from 'src/app/a4g-common/interfaces/paginator.model';
import { DichiarazioneConsumiDto } from 'src/app/uma/core-uma/models/dto/DichiarazioneConsumiDto';
import { PrelievoDto } from 'src/app/uma/core-uma/models/dto/PrelievoDto';
import { CarburanteDto } from 'src/app/uma/core-uma/models/dto/CarburanteDto';
import { PopupInserisciEditaTrasferimentoComponent } from '../popup-inserisci-edita-trasferimento/popup-inserisci-edita-trasferimento.component';

@Component({
  selector: 'app-gestione-trasferimenti',
  templateUrl: './gestione-trasferimenti.component.html',
  styleUrls: ['./gestione-trasferimenti.component.scss']
})
export class GestioneTrasferimentiComponent implements OnInit {
  READONLY_MODE: boolean;
  cols: Array<A4gMultiTableColumn>;
  datasource: Array<any>;
  tipoIntestazioneEnum: typeof TipoIntestazioneUma;
  idRichiesta: string;
  dichiarazioneConsumi: DichiarazioneConsumiDto;
  richiestaMittente: RichiestaCarburanteDto;

  @ViewChild("popupRicercaRichiesta", { static: true })
  popupRicercaRichiesta: PopupRicercaRichiestaCarburanteComponent;

  @ViewChild("popupInserisciEditaTrasferimento", { static: true })
  popupInserisciEditaTrasferimento: PopupInserisciEditaTrasferimentoComponent;

  // Subscriptions
  routerSubscription: Subscription;
  deleteSubscription: Subscription;

  constructor(
    private indiceUmaService: IndiceUmaService,
    private location: Location,
    private route: ActivatedRoute,
    private httpCLientCarburanteRicevutoService: HttpClientTrasferimentiCarburanteService,
    private httpClientDomandaUmaService: HttpClientDomandaUmaService,
    private httpClientDichiarazioneConsumiUmaService: HttpClientDichiarazioneConsumiUmaService,
    private trasferitiBuilderService: TrasferitiBuilderService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private errorService: ErrorService
  ) { }

  ngOnInit() {
    this.initVariables();
    this.caricaDatiTrasferimenti(false);
  }

  ngOnDestroy() {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
    if (this.deleteSubscription) {
      this.deleteSubscription.unsubscribe();
    }
  }

  private initVariables() {
    this.tipoIntestazioneEnum = TipoIntestazioneUma;
    this.READONLY_MODE = this.indiceUmaService.READONLY_MODE_DICHIARAZIONE || (localStorage.getItem('UMA_RO_DICH') == 'true' ? true : false);
    this.cols = this.defineColumns();
  }

  goBack() {
    this.location.back();
  }

  public caricaDatiTrasferimenti(showSuccessMessage: boolean) {
    if (showSuccessMessage) {
      this.confirmationService
        .confirm({
          message: UMA_MESSAGES.saveTrasferimentoCarburanteWarn,
          rejectVisible: false,
          accept: () => { /** si chiude */ },
          reject: () => { /** si chiude */ },
          key: "trasferimenti-dialog-save"
        });
    }
    this.idRichiesta
    this.routerSubscription = this.route.params
      .pipe(
        switchMap((params: Params) => {
          this.idRichiesta = params['idDomanda'];
          return this.httpClientDomandaUmaService.getDomandaById(params['idDomanda'])
        }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err);
          return EMPTY;
        }),
        switchMap((richiesta: RichiestaCarburanteDto) => {
          this.richiestaMittente = richiesta;
          return this.httpCLientCarburanteRicevutoService.getCarburanteTrasferito(richiesta.cuaa, richiesta.campagna.toString());
        }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err);
          return EMPTY;
        }),
        switchMap((trasferiti: CarburanteTotale<TrasferimentoDto>) => {
          this.datasource = this.trasferitiBuilderService.trasferitiDtoToRicevutiViewModelBuilder(trasferiti.dati);
          if (trasferiti && trasferiti.dati.length) {
            return this.httpClientDichiarazioneConsumiUmaService.getDichiarazioniConsumi({ cuaa: trasferiti.dati[0].destinatario.cuaa, campagna: [this.richiestaMittente.campagna.toString()], numeroElementiPagina: 1, pagina: 0 }); // TODO sistemare dati destinatario
          }
          return EMPTY;
        }))
      .subscribe((dichiarazioni: Array<DichiarazioneConsumiDto>) => {
        if (dichiarazioni && dichiarazioni.length) {
          this.dichiarazioneConsumi = dichiarazioni[0];
        }
      }, error => this.errorService.showError(error));
  }

  onClickIcon($event: { element: TrasferitoViewModel, col: A4gMultiTableColumn }) {
    if ($event.col.buttonOpts.id === 'elimina') {
      this.deleteTrasferimento($event.element);
    } else if ($event.col.buttonOpts.id === 'modifica') {
      this.editTrasferimento($event.element);
    }
  }

  // Chiama il metodo di apertura della popup in modalita di modifica passando il trasferimento
  editTrasferimento(element: TrasferitoViewModel) {
    this.openInserisciEditaTrasferimentoPopup({ trasferimento: element, modalitaCrea: false});
  }

  // Confirm Dialog di eliminazione di un trasferimento
  deleteTrasferimento(element: TrasferitoViewModel) {
    this.confirmationService
      .confirm({
        message: UMA_MESSAGES.confermaCancellazioneTrasferimento,
        accept: () => {
          this.cancellaDaID(element);
        },
        reject: () => { /** si chiude */ },
        key: "trasferimenti-dialog"
      });
  }

  // Apre il popup di ricerca di un destinatario
  nuovoTrasferimento() {
    if (this.datasource.length > 0) {
      this.messageService.add(A4gMessages.getToast("tst-trasferimenti", A4gSeverityMessage.warn, UMA_MESSAGES.erroreTrasferimentoPresente));
    } else {
      this.popupRicercaRichiesta.open(this.richiestaMittente.id.toString());
    }
  }

  // Apre la popup che permette di modificare o inserire un trasferimento
  openInserisciEditaTrasferimentoPopup(data: { trasferimento?: TrasferitoViewModel, idDestinatario?: String, cuaa?: String, denominazione?: String, modalitaCrea: boolean}) {
    const $getConsumiAnnoPrecedente = this.httpClientDichiarazioneConsumiUmaService.getResiduoAnnoPrecedente(this.richiestaMittente.cuaa, this.richiestaMittente.campagna - 1, [StatoDichiarazioneConsumiEnum.PROTOCOLLATA]);
    const $getPrelevato = this.httpClientDomandaUmaService.getPrelieviByCuaaAndCampagna(this.richiestaMittente.cuaa, this.richiestaMittente.campagna.toString());

    forkJoin([$getConsumiAnnoPrecedente, $getPrelevato])
      .subscribe(([consumiPrecedenti, prelevato]): [Array<DichiarazioneConsumiDto>, CarburanteTotale<PrelievoDto>] => {
        // Calcolo del trasferibile (residuo + prelevato)
        const trasferibile = new CarburanteDto();
        trasferibile.gasolio = ((consumiPrecedenti && consumiPrecedenti[0]) ? consumiPrecedenti[0].rimanenza.gasolio : 0) + prelevato.totale.gasolio;
        trasferibile.benzina = ((consumiPrecedenti && consumiPrecedenti[0]) ? consumiPrecedenti[0].rimanenza.benzina : 0) + prelevato.totale.benzina;
        trasferibile.gasolioSerre = ((consumiPrecedenti && consumiPrecedenti[0]) ? consumiPrecedenti[0].rimanenza.gasolioSerre : 0) + prelevato.totale.gasolioSerre;

        if (data.modalitaCrea) {
          // In fase di creazione passiamo idMittente e idDestinatario
          this.popupInserisciEditaTrasferimento.open({
            trasferibile: trasferibile,
            idMittente: this.idRichiesta,
            idDestinatario: data.idDestinatario.toString(),
            cuaa: data.cuaa,
            denominazione: data.denominazione, 
            modalitaCrea: true
          });
        } else { // MODIFICA
          let trasferito = new CarburanteDto();
          trasferito.gasolio = data.trasferimento.gasolio;
          trasferito.benzina = data.trasferimento.benzina;
          trasferito.gasolioSerre = data.trasferimento.gasolioSerre;
          // In fase di modifica passiamo idTrasferimento e le quantità del trasferimento
          this.popupInserisciEditaTrasferimento.open({
            trasferibile: trasferibile,
            idTrasferimento: data.trasferimento.id.toString(),
            trasferimento: trasferito, 
            cuaa: data.trasferimento.cuaa,
            denominazione: data.trasferimento.denominazione, 
            modalitaCrea: false,
            dichiarazioneConsumi: this.dichiarazioneConsumi
          });
        }
        return;
      });

  }

  cancellaDaID(element: TrasferitoViewModel) {
    if (this.dichiarazioneConsumi && this.dichiarazioneConsumi.stato === StatoDichiarazioneConsumiEnum.PROTOCOLLATA) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.cancellazioneTrasferimentoKO, 'tst-trasferimenti');
      return;
    }
    this.deleteSubscription = this.httpCLientCarburanteRicevutoService.deleteTraseferito(element.id.toString())
      .subscribe(
        () => {
          this.messageService.add(A4gMessages.getToast("tst-trasferimenti", A4gSeverityMessage.success, UMA_MESSAGES.cancellazioneTrasferimentoOK));
          this.caricaDatiTrasferimenti(false);
        },
        (error) => this.errorService.showError(error, 'tst-trasferimenti')
      );
  }

  onChangeForm($event) {

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
        field: "cuaa",
        header: 'CUAA',
        sortable: false,
        font: FontTypeEnum.BOLD,
        width: '23%',
        alignCellHorizontal: AlignTypeEnum.CENTER,
        alignHeader: AlignTypeEnum.LEFT,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: "denominazione",
        header: 'Denominazione',
        sortable: false,
        font: FontTypeEnum.BOLD,
        width: '43%',
        alignCellHorizontal: AlignTypeEnum.LEFT,
        alignHeader: AlignTypeEnum.LEFT,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'gasolio',
        header: 'Gasolio trasferito (lt.)',
        sortable: false,
        width: '8%',
        alignCellHorizontal: AlignTypeEnum.RIGHT,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'benzina',
        header: 'Benzina trasferita (lt.)',
        sortable: false,
        width: '8%',
        alignCellHorizontal: AlignTypeEnum.RIGHT,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'gasolioSerre',
        header: 'Gasolio serre trasferito (lt.)',
        sortable: false,
        width: '8%',
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

}
