import { FormatConverterService } from 'src/app/uma/shared-uma/services/format-converter.service';
import { NavigatorService } from 'src/app/a4g-common/services/navigator.service';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
import { NullCheckService } from './../../../../shared-uma/services/null-check.service';
import { FormService } from '../../../../../a4g-common/services/form.service';
import { StepRichiestaCarburante } from 'src/app/uma/core-uma/models/enums/StepRichiestaCarburante.enum';
import { GestioneDomandaUmaService } from '../../gestione-domanda-uma.service';
import { CarburanteRichiestoDto } from '../../../../core-uma/models/dto/CarburanteRichiestoDto';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { EMPTY, Subscription } from 'rxjs';
import { switchMap, catchError } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { HttpClientLavorazioniUmaService } from 'src/app/uma/core-uma/services/http-client-lavorazioni-uma.service';
import { HttpClientDichiarazioneConsumiUmaService } from 'src/app/uma/core-uma/services/http-client-dichiarazione-consumi-uma.service';
import { forkJoin } from 'rxjs';
import { HttpClientDomandaUmaService } from 'src/app/uma/core-uma/services/http-client-domanda-uma.service';
import { CarburanteDto } from 'src/app/uma/core-uma/models/dto/CarburanteDto';
import { DichiarazioneConsumiDto } from 'src/app/uma/core-uma/models/dto/DichiarazioneConsumiDto';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import * as _ from 'lodash';
import { FabbisognoDichiarato } from 'src/app/uma/core-uma/models/viewModels/FabbisognoDichiaratoViewModel';
import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { HttpClientMacchineUmaService } from 'src/app/uma/core-uma/services/http-client-macchine-uma.service';
import { AuthService } from 'src/app/auth/auth.service';
import { StatoDomandaUma } from 'src/app/a4g-common/classi/enums/uma/StatoDomandaUma.enum';
import { StatoDichiarazioneConsumiEnum } from 'src/app/a4g-common/classi/enums/uma/StatoDichiarazioneConsumi.enum';
import { PaginatorA4G } from 'src/app/a4g-common/interfaces/paginator.model';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { CarburanteTotale } from 'src/app/uma/core-uma/models/dto/CarburanteTotale';
import { PrelievoDto } from 'src/app/uma/core-uma/models/dto/PrelievoDto';
@Component({
  selector: 'app-fabbisogno',
  templateUrl: './fabbisogno.component.html',
  styleUrls: ['./fabbisogno.component.scss']
})
export class FabbisognoComponent implements OnInit {
  @Input() richiestaCarburante: RichiestaCarburanteDto;
  @Input() idFascicolo: number;
  @Input() inCompilazione: boolean;                     /** se lo stato è in compilazione, è possibile modificare la pagina, altrimenti può essere consultata */
  @Output() nextStep = new EventEmitter<number>();
  @Output() prevStep = new EventEmitter<number>();

  fabbisognoDichiarato: FabbisognoDichiarato;
  STATO_RICHIESTA_UMA = StatoDomandaUma;
  isRettificaInCompilazione: boolean; /** se true sto compilando una rettifica e il campo note è obbligatorio */
  isRettifica: boolean;               /** se true è una rettifica -> mostro i prelievi*/
  umaMessages = UMA_MESSAGES;

  // Subscriptions
  routerSubscription: Subscription;
  saveDichiarazioniSubscription: Subscription;
  updateDichiarazioniSubscription: Subscription;
  validaSubscription: Subscription;
  deleteSubscription: Subscription;

  // Form
  fabbisogniForm: FormGroup;

  constructor(
    private activeRoute: ActivatedRoute,
    private httpClientLavorazioniUmaService: HttpClientLavorazioniUmaService,
    private httpClientDomandaUmaService: HttpClientDomandaUmaService,
    private httpClientDichiarazioneConsumiUmaService: HttpClientDichiarazioneConsumiUmaService,
    private httpClientMacchineUmaService: HttpClientMacchineUmaService,
    private confirmationService: ConfirmationService,
    private gestioneDomandaUmaService: GestioneDomandaUmaService,
    private authService: AuthService,
    private messageService: MessageService,
    private errorService: ErrorService,
    private formService: FormService,
    private nullCheckService: NullCheckService,
    private dateUtilService: DateUtilService,
    private navigatorService: NavigatorService,
    private converterService: FormatConverterService
  ) { }

  ngOnInit() {
    this.controllaIstruttoreUMA();
    this.initVariables();
    this.routerSubscription = this.activeRoute.params
      .pipe(
        switchMap((params: Params) => {
          return this.httpClientLavorazioniUmaService.getDomandaById(params['idDomanda']);
        }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err, 'tst-fabbisogno');
          return EMPTY;
        }),
        switchMap((richiesta: RichiestaCarburanteDto) => {
          this.richiestaCarburante = richiesta;
          const getAmmissibile$ = this.httpClientDomandaUmaService.getCarburanteAmmissibile(this.richiestaCarburante.id.toString());
          const getResiduoAnnoPrecedente$ = this.httpClientDichiarazioneConsumiUmaService.getResiduoAnnoPrecedente(this.richiestaCarburante.cuaa, this.richiestaCarburante.campagna - 1, [StatoDichiarazioneConsumiEnum.PROTOCOLLATA]);
          const getPrelievi$ = this.httpClientDomandaUmaService.getPrelieviByCuaaAndCampagna(this.richiestaCarburante.cuaa, this.richiestaCarburante.campagna.toString(), this.dateUtilService.toLocalDate(this.richiestaCarburante.dataPresentazione));
          return forkJoin([getAmmissibile$, getResiduoAnnoPrecedente$, getPrelievi$]);
        })
      ).subscribe(([carburante, consumi, prelievi]: [CarburanteDto, Array<DichiarazioneConsumiDto>, CarburanteTotale<PrelievoDto>]) => {
        // La rettifica deve mostrare la riga relativa ai prelievi sia se è in compilazione che autorizzata
        this.isRettifica = this.richiestaCarburante.idRettificata != null ? true : false;
        // La rettifica in compilazione ha il campo note obbligatorio
        if (this.richiestaCarburante.idRettificata != null && this.richiestaCarburante.stato === this.STATO_RICHIESTA_UMA.IN_COMPILAZIONE ? true : false) {
          this.fabbisogniForm.get('note').setValidators(Validators.required);
        }
        // Residuo
        if (consumi && consumi.length > 0 && consumi[0] && consumi[0].rimanenza) {
          const rimanenza = consumi[0].rimanenza;
          this.fabbisogniForm.get("residuoGasolio").setValue(rimanenza.gasolio == null ? 0 : rimanenza.gasolio);
          this.fabbisogniForm.get("residuoBenzina").setValue(rimanenza.benzina == null ? 0 : rimanenza.benzina);
          this.fabbisogniForm.get("residuoGasolioSerre").setValue(rimanenza.gasolioSerre == null ? 0 : rimanenza.gasolioSerre);
          this.fabbisogniForm.get("residuoGasolioTerzi").setValue(rimanenza.gasolioTerzi == null ? 0 : rimanenza.gasolioTerzi);
        }
        // Ammissibile
        if (carburante) {
          this.fabbisogniForm.get("ammissibileGasolio").setValue((carburante.gasolio || '0'));
          this.fabbisogniForm.get("ammissibileBenzina").setValue((carburante.benzina || '0'));
          this.fabbisogniForm.get("ammissibileGasolioSerre").setValue((carburante.gasolioSerre || '0'));
        }
        // Quantitativo prelevato
        if (this.isRettifica) {
          this.fabbisogniForm.get('prelevatoGasolio').setValue(prelievi?.totale?.gasolio);
          this.fabbisogniForm.get('prelevatoBenzina').setValue(prelievi?.totale?.benzina);
          this.fabbisogniForm.get('prelevatoGasolioSerre').setValue(prelievi?.totale?.gasolioSerre);
        }
        // Richiesto/Assegnato
        this.fabbisogniForm.get("richiestoGasolio").setValue((this.richiestaCarburante.carburanteRichiesto != null && this.richiestaCarburante.carburanteRichiesto.gasolio != null ? this.richiestaCarburante.carburanteRichiesto.gasolio : null));
        this.fabbisogniForm.get("richiestoBenzina").setValue((this.richiestaCarburante.carburanteRichiesto != null && this.richiestaCarburante.carburanteRichiesto.benzina != null ? this.richiestaCarburante.carburanteRichiesto.benzina : null));
        this.fabbisogniForm.get("richiestoGasolioSerre").setValue((this.richiestaCarburante.carburanteRichiesto != null && this.richiestaCarburante.carburanteRichiesto.gasolioSerre != null ? this.richiestaCarburante.carburanteRichiesto.gasolioSerre : null));
        this.fabbisogniForm.get("richiestoGasolioTerzi").setValue((this.richiestaCarburante.carburanteRichiesto != null && this.richiestaCarburante.carburanteRichiesto.gasolioTerzi != null ? this.richiestaCarburante.carburanteRichiesto.gasolioTerzi : null));
        // Note
        this.fabbisogniForm.get("note").setValue((this.richiestaCarburante.note || null));
      }, error => this.errorService.showError(error, 'tst-fabbisogno'));
  }

  private initVariables() {
    this.fabbisognoDichiarato = this.gestioneDomandaUmaService.getTipoCarburanteDichiarato(this.richiestaCarburante);
    this.initForm();
  }

  goPrevStep() {
    if (this.inCompilazione) {
      this.confirmationService.confirm({
        message: UMA_MESSAGES.datiFabbisognoEliminazioneAlert,
        accept: () => {
          this.updateDichiarazioniSubscription = this.httpClientLavorazioniUmaService.updateDomandaUma(this.richiestaCarburante.id.toString(), this.toCarburanteRichiestoDtoWithNull())
            .subscribe(() => {
              this.messageService.add(A4gMessages.getToast('tst-fabbisogno', A4gSeverityMessage.error, UMA_MESSAGES.fabbisognoDatiEliminati));
              this.prevStep.emit(StepRichiestaCarburante.FABBISOGNO);
            }, error => this.errorService.showError(error, 'tst-fabbisogno'));
        },
        reject: () => { },
        key: "fabbisogno-dialog"
      });
    } else {
      this.prevStep.emit(StepRichiestaCarburante.FABBISOGNO);
    }
  }

  goNextStep() {
    // check se ho inserito almeno un fabbisogno
    if (!this.isRichiestoAssegnatoInserito()) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.richiestoAssegnatoObbligatorio, 'tst-fabbisogno');
      return;
    }

    // check se ho modificato i dati nella form
    if (!this.fabbisogniForm.pristine || !this.fabbisogniForm.untouched) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.fabbisognoDatiNonSalvati, 'tst-fabbisogno');
      return;
    }

    // warning 
    // se ho un ammissibile maggiore di 0 e in corrispondenza ho messo un richiesto/assegnato 0         oppure
    // se non ho inserito nessuna lavorazione (ho tutti gli ammissibili a 0) e ho messo un richiesto/assegnato 0
    if (this.isRichiestoAssegnatoEqualsZeroWithAmmissibile() || this.isAmmissibileEqualsZero()) {
      this.showWarningDialog();
      return;
    }

    // controllo di fascicolo valido prima della protocollazione
    this.validaRichiesta();
  }

  private validaRichiesta() {
    this.validaSubscription = this.httpClientMacchineUmaService.validaRichiestaCarburante(this.richiestaCarburante.id.toString())
      .subscribe((response: any) => {
        this.nextStep.emit(StepRichiestaCarburante.FABBISOGNO);
      }, (error: ErrorDTO) => {
        this.confirmationService.confirm({
          message: A4gMessages.fascicoloNonValido,
          accept: () => {
            this.deleteSubscription = this.httpClientDomandaUmaService.deleteDomandaById(this.richiestaCarburante.id.toString())
              .subscribe(
                () => this.messageService.add(A4gMessages.getToast("tst-eliminazione", A4gSeverityMessage.success, UMA_MESSAGES.cancellazioneDomandaOK)),
                (error) => this.errorService.showError(error, 'tst-fabbisogno')
              );
          },
          reject: () => { },
          key: "fabbisogno-dialog"
        });
      }
      );
  }

  onCloseToastEliminazione() {
    this.navigatorService.goToPresentazioneIstanze(this.idFascicolo.toString());
  }

  ngOnDestroy() {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
    if (this.saveDichiarazioniSubscription) {
      this.saveDichiarazioniSubscription.unsubscribe();
    }
    if (this.updateDichiarazioniSubscription) {
      this.updateDichiarazioniSubscription.unsubscribe();
    }
    if (this.validaSubscription) {
      this.validaSubscription.unsubscribe();
    }
    if (this.deleteSubscription) {
      this.deleteSubscription.unsubscribe();
    }
  }

  saveFabbisogno() {
    // check validità form
    this.formService.validateFormByErrors(this.fabbisogniForm, ['max', 'pattern', 'required']);
    /** solo per la rettifica il campo è required */
    if (this.fabbisogniForm.get('note').hasError('required')) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.noteRettificaObbligatorio, 'tst-fabbisogno');
      return;
    }
    if (!this.fabbisogniForm.valid) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.datiNonValidi, 'tst-fabbisogno');
      return;
    }
    if (!this.fabbisogniForm.valid) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.datiNonValidi, 'tst-fabbisogno');
      return;
    }
    // check se ho inserito almeno un fabbisogno
    if (!this.isRichiestoAssegnatoInserito()) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.richiestoAssegnatoObbligatorio, 'tst-fabbisogno');
      return;
    }
    // set a 0 i campi Richiesto/Assegnato non sono compilati
    this.fillWithZeros();
    // salvataggio
    this.saveDichiarazioniSubscription = this.httpClientLavorazioniUmaService.updateDomandaUma(this.richiestaCarburante.id.toString(), this.toCarburanteRichiestoDto()).subscribe(() => {
      this.messageService.add(A4gMessages.getToast('tst-fabbisogno', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOKFabbisogno));
      this.fabbisogniForm.markAsPristine();
      this.fabbisogniForm.markAsUntouched();
    }, error => this.errorService.showError(error, 'tst-fabbisogno'));
    return true;
  }

  private initForm() {
    this.fabbisogniForm = new FormGroup({
      // residuo e ammissibile sono inizializzati con 0
      residuoGasolio: new FormControl({ value: '0', disabled: true }),
      residuoBenzina: new FormControl({ value: '0', disabled: true }),
      residuoGasolioSerre: new FormControl({ value: '0', disabled: true }),
      residuoGasolioTerzi: new FormControl({ value: '0', disabled: true }),
      ammissibileGasolio: new FormControl({ value: '0', disabled: true }),
      ammissibileBenzina: new FormControl({ value: '0', disabled: true }),
      ammissibileGasolioSerre: new FormControl({ value: '0', disabled: true }),
      prelevatoGasolio: new FormControl({ value: '0', disabled: true }),
      prelevatoBenzina: new FormControl({ value: '0', disabled: true }),
      prelevatoGasolioSerre: new FormControl({ value: '0', disabled: true }),
      // richiesto/assegnato è inizializzato con vuoto
      richiestoGasolio: new FormControl({ value: null, disabled: !this.inCompilazione || !this.fabbisognoDichiarato || !this.fabbisognoDichiarato.gasolio }),
      richiestoBenzina: new FormControl({ value: null, disabled: !this.inCompilazione || !this.fabbisognoDichiarato || !this.fabbisognoDichiarato.benzina }),
      richiestoGasolioSerre: new FormControl({ value: null, disabled: !this.inCompilazione }),
      richiestoGasolioTerzi: new FormControl({ value: null, disabled: !this.inCompilazione }),
      note: new FormControl({ value: null, disabled: !this.inCompilazione })
    });
  }

  private fillWithZeros() {
    if (this.nullCheckService.isEmpty(this.fabbisogniForm.get("richiestoGasolio").value)) {
      this.fabbisogniForm.get("richiestoGasolio").setValue('0');
    }
    if (this.nullCheckService.isEmpty(this.fabbisogniForm.get("richiestoBenzina").value)) {
      this.fabbisogniForm.get("richiestoBenzina").setValue('0');
    }
    if (this.nullCheckService.isEmpty(this.fabbisogniForm.get("richiestoGasolioSerre").value)) {
      this.fabbisogniForm.get("richiestoGasolioSerre").setValue('0');
    }
    if (this.nullCheckService.isEmpty(this.fabbisogniForm.get("richiestoGasolioTerzi").value)) {
      this.fabbisogniForm.get("richiestoGasolioTerzi").setValue('0');
    }
  }

  private showWarningDialog() {
    this.confirmationService.confirm({
      message: UMA_MESSAGES.fabbisognoZero,
      accept: () => {
        this.validaRichiesta();
      },
      reject: () => { },
      key: "fabbisogno-dialog"
    });
  }

  private isRichiestoAssegnatoInserito() {
    let inserito: boolean = true;
    const richiestoGasolio = this.fabbisogniForm.get("richiestoGasolio").value;
    const richiestoBenzina = this.fabbisogniForm.get("richiestoBenzina").value;
    const richiestoGasolioSerre = this.fabbisogniForm.get("richiestoGasolioSerre").value;
    const richiestoGasolioTerzi = this.fabbisogniForm.get("richiestoGasolioTerzi").value;
    if ((richiestoGasolio == null || _.isEmpty(richiestoGasolio.toString()))
      && (richiestoBenzina == null || _.isEmpty(richiestoBenzina.toString()))
      && (richiestoGasolioSerre == null || _.isEmpty(richiestoGasolioSerre.toString()))
      && (richiestoGasolioTerzi == null || _.isEmpty(richiestoGasolioTerzi.toString()))) {
      inserito = false;
    }
    return inserito;
  }

  private isRichiestoAssegnatoEqualsZeroWithAmmissibile(): boolean {
    let hasZeroes: boolean = false;
    const richiestoGasolio = this.fabbisogniForm.get("richiestoGasolio").value;
    const richiestoBenzina = this.fabbisogniForm.get("richiestoBenzina").value;
    const richiestoGasolioSerre = this.fabbisogniForm.get("richiestoGasolioSerre").value;
    const ammissibileGasolio = this.fabbisogniForm.get("ammissibileGasolio").value;
    const ammissibileBenzina = this.fabbisogniForm.get("ammissibileBenzina").value;
    const ammissibileGasolioSerre = this.fabbisogniForm.get("ammissibileGasolioSerre").value;
    if (richiestoGasolio == '0' && this.converterService.toNumber(ammissibileGasolio) > 0 ||
      richiestoBenzina == '0' && this.converterService.toNumber(ammissibileBenzina) > 0 ||
      richiestoGasolioSerre == '0' && this.converterService.toNumber(ammissibileGasolioSerre) > 0) {
      hasZeroes = true;
    }
    return hasZeroes;
  }

  private isAmmissibileEqualsZero(): boolean {
    let hasZeroes: boolean = false;
    const richiestoGasolio = this.fabbisogniForm.get("richiestoGasolio").value;
    const richiestoBenzina = this.fabbisogniForm.get("richiestoBenzina").value;
    const richiestoGasolioSerre = this.fabbisogniForm.get("richiestoGasolioSerre").value;
    const ammissibileGasolio = this.fabbisogniForm.get("ammissibileGasolio").value;
    const ammissibileBenzina = this.fabbisogniForm.get("ammissibileBenzina").value;
    const ammissibileGasolioSerre = this.fabbisogniForm.get("ammissibileGasolioSerre").value;
    if (richiestoGasolio == '0' && this.converterService.toNumber(ammissibileGasolio) == 0 &&
      richiestoBenzina == '0' && this.converterService.toNumber(ammissibileBenzina) == 0 &&
      richiestoGasolioSerre == '0' && this.converterService.toNumber(ammissibileGasolioSerre) == 0) {
      hasZeroes = true;
    }
    return hasZeroes;
  }

  private toCarburanteRichiestoDto(): CarburanteRichiestoDto {
    const carburanteRichiestoDto = new CarburanteRichiestoDto();
    carburanteRichiestoDto.carburanteRichiesto = new CarburanteDto();
    carburanteRichiestoDto.carburanteRichiesto.gasolio = +this.fabbisogniForm.get("richiestoGasolio").value;
    carburanteRichiestoDto.carburanteRichiesto.benzina = +this.fabbisogniForm.get("richiestoBenzina").value;
    carburanteRichiestoDto.carburanteRichiesto.gasolioSerre = +this.fabbisogniForm.get("richiestoGasolioSerre").value;
    carburanteRichiestoDto.carburanteRichiesto.gasolioTerzi = +this.fabbisogniForm.get("richiestoGasolioTerzi").value;
    carburanteRichiestoDto.note = this.fabbisogniForm.get("note").value;
    return carburanteRichiestoDto;
  }

  private toCarburanteRichiestoDtoWithNull(): CarburanteRichiestoDto {
    const carburanteRichiestoDto = new CarburanteRichiestoDto();
    carburanteRichiestoDto.carburanteRichiesto = new CarburanteDto();
    carburanteRichiestoDto.carburanteRichiesto.gasolio = null;
    carburanteRichiestoDto.carburanteRichiesto.benzina = null;
    carburanteRichiestoDto.carburanteRichiesto.gasolioSerre = null;
    carburanteRichiestoDto.carburanteRichiesto.gasolioTerzi = null;
    carburanteRichiestoDto.note = null;
    return carburanteRichiestoDto;
  }

  controllaIstruttoreUMA() {
    if (this.authService.userSelectedRole == AuthService.roleIstruttoreUMA ||
      this.authService.userSelectedRole == AuthService.roleAppag) {
      this.inCompilazione = false;
    }
  }
}
