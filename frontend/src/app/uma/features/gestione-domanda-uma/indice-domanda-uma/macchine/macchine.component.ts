import { TipoCarburante } from '../../../../core-uma/models/enums/TipoCarburante.enum';
import { ModelBuilderService } from '../../../../core-uma/services/model-builder.service';
import { StepRichiestaCarburante } from 'src/app/uma/core-uma/models/enums/StepRichiestaCarburante.enum';
import { HttpClientDomandaUmaService } from 'src/app/uma/core-uma/services/http-client-domanda-uma.service';
import { UMA_MESSAGES } from '../../../../uma.messages';
import { Component, OnInit, OnDestroy, Output, EventEmitter, Input, OnChanges, SimpleChanges } from '@angular/core';
import { EMPTY, Observable, of, Subscription } from 'rxjs';
import { ConfirmationService, MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
import { MacchinaDto } from 'src/app/uma/core-uma/models/dto/MacchinaDto';
import { HttpClientMacchineUmaService } from 'src/app/uma/core-uma/services/http-client-macchine-uma.service';
import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { switchMap, catchError } from 'rxjs/operators';
import { HttpClientLavorazioniUmaService } from 'src/app/uma/core-uma/services/http-client-lavorazioni-uma.service';
import { AuthService } from 'src/app/auth/auth.service';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
@Component({
  selector: 'app-macchine',
  templateUrl: './macchine.component.html',
  styleUrls: ['./macchine.component.scss']
})
export class MacchineComponent implements OnInit, OnDestroy, OnChanges {
  @Input() inCompilazione: boolean;                    /** se lo stato è in compilazione, è possibile modificare la pagina, altrimenti può essere consultata */
  @Input() richiestaCarburante: RichiestaCarburanteDto;
  @Input() idFascicolo: number;
  @Output() nextStep = new EventEmitter<number>();
  @Output() updateRichiesta = new EventEmitter<RichiestaCarburanteDto>();

  macchine: Array<MacchinaDto>;
  cols: Array<any>;
  valoriOriginari: boolean[] = [];
  isSelectedAll: boolean = false;

  // Subscriptions
  macchineSubscription: Subscription;
  dichiarazioneMacchineSubscription: Subscription;

  constructor(
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    public dateUtilService: DateUtilService,
    private httpClientMacchineUmaService: HttpClientMacchineUmaService,
    private httpClientDomandaUmaService: HttpClientDomandaUmaService,
    private httpClientLavorazioniUmaService: HttpClientLavorazioniUmaService,
    private authService: AuthService,
    private modelBuilderService: ModelBuilderService,
    private errorService: ErrorService
  ) { }

  ngOnInit() {
    this.controllaIstruttoreUMA();
    this.cols = this.defineTableColumns();
    this.macchine = [];
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes && changes.richiestaCarburante && changes.richiestaCarburante.currentValue) {
      this.getMacchine();
    }
  }

  goNextStep() {
    // skip controlli se la richiesta è in sola lettura
    if (!this.inCompilazione) {
      this.nextStep.emit(StepRichiestaCarburante.MACCHINE);
      return;
    }
    // controlli nel caso sia in compilazione
    if (!this.richiestaCarburante.haMacchineBenzina && !this.richiestaCarburante.haMacchineGasolio) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.macchineNotPresenti, 'tst-macchine');
      return;
    }
    if (!this.macchine.filter(macchina => macchina.isUtilizzata).length) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.macchineNotSelezionate, 'tst-macchine');
      return;
    }
    if (this.isChangedInputSwitch()) {
      this.confirmationService.confirm({
        message: UMA_MESSAGES.macchineNonSalvate,
        accept: () => {
          this.nextStep.emit(StepRichiestaCarburante.MACCHINE);
        },
        reject: () => { },
        key: "macchine-dialog"
      });
    } else {
      this.nextStep.emit(StepRichiestaCarburante.MACCHINE);
    }
  }

  ngOnDestroy() {
    if (this.macchineSubscription) {
      this.macchineSubscription.unsubscribe();
    }
    if (this.dichiarazioneMacchineSubscription) {
      this.dichiarazioneMacchineSubscription.unsubscribe();
    }
  }

  saveMacchine() {
    if (!this.macchine.filter(macchina => macchina.isUtilizzata).length) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.macchineNotSelezionate, 'tst-macchine')
      return;
    }
    // Verifico se sono cambiati i tipi di carburante prima di salvare 
    const nuovoFabbisognoDichiarato = this.modelBuilderService.fabbisognoDichiaratoBuilder(this.macchine);
    // benzina prima era presente e poi è stato eliminato
    if (this.richiestaCarburante.haMacchineBenzina && !nuovoFabbisognoDichiarato.benzina) {
      this.confirmationService.confirm({
        message: UMA_MESSAGES.macchineBenzinaRimosse,
        accept: () => { this.saveMacchineAndUpdateRichiesta(true, false) },
        reject: () => { this.getMacchine() },
        key: "macchine-dialog"
      });
      return;
    }
    // gasolio prima era presente e poi è stato eliminato
    if (this.richiestaCarburante.haMacchineGasolio && !nuovoFabbisognoDichiarato.gasolio) {
      this.confirmationService.confirm({
        message: UMA_MESSAGES.macchineGasolioRimosse,
        accept: () => { this.saveMacchineAndUpdateRichiesta(false, true) },
        reject: () => { this.getMacchine() },
        key: "macchine-dialog"
      });
      return;
    }
    // salvataggio macchine senza cancellazione di fabbisogni
    this.saveMacchineAndUpdateRichiesta(false, false);
  }

  onClickSelezionaTutto() {
    if (this.isSelectedAll == false) {
      this.macchine.forEach(macchina => {
        macchina.isUtilizzata = false;
      });
    }
    if (this.isSelectedAll == true) {
      this.macchine.forEach(macchina => {
        macchina.isUtilizzata = true;
      });
    }
  }

  private saveMacchineAndUpdateRichiesta(deleteBenzina: boolean, deleteGasolio: boolean) {
    this.dichiarazioneMacchineSubscription = this.httpClientMacchineUmaService.dichiaraMacchinariDomanda(this.richiestaCarburante.id.toString(), this.macchine)
      .pipe(
        switchMap((macchineSalvate: Array<MacchinaDto>) => {
          let deleteFabbisogni$: Observable<any>;
          if (deleteBenzina || deleteGasolio) {
            let carburanteToDelete = deleteBenzina ? [TipoCarburante.BENZINA] : [TipoCarburante.GASOLIO];
            deleteFabbisogni$ = this.httpClientLavorazioniUmaService.deleteFabbisogniById(this.richiestaCarburante.id.toString(), carburanteToDelete);
          } else {
            deleteFabbisogni$ = of({});
          }
          return deleteFabbisogni$;
        }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err, 'tst-macchine')
          return EMPTY;
        }),
        switchMap(() => {
          return this.httpClientDomandaUmaService.getDomandaById(this.richiestaCarburante.id.toString());
        })
      )
      .subscribe((richiesta: RichiestaCarburanteDto) => {
        this.messageService.add(A4gMessages.getToast('tst-macchine', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOK));
        this.initValoriOriginari();
        this.richiestaCarburante = richiesta;
        // comunico al padre la richiesta aggiornata con le macchine inserite (es. se prima erano solo gasolio e ora anche benzina)
        this.updateRichiesta.emit(richiesta);
      }, error => this.errorService.showError(error, 'tst-macchine'))
  }

  private getMacchine() {
    this.macchineSubscription = this.httpClientMacchineUmaService.getMacchineByIdDomanda(this.richiestaCarburante.id.toString())
      .subscribe((macchine: Array<MacchinaDto>) => {
        this.macchine = macchine;
        this.setIsSelectedAll();
        this.initValoriOriginari();
      }, error => this.errorService.showError(error, 'tst-macchine'));
  }

  private setIsSelectedAll() {
    if (this.macchine == null || this.macchine.length <= 0) {
      this.isSelectedAll = false;
      return;
    }
    if (this.macchine.every(macchina => macchina.isUtilizzata == true)) {
      this.isSelectedAll = true;
    } else {
      this.isSelectedAll = false;
    }
  }

  private isChangedInputSwitch(): boolean {
    let nrMacchina: number = 0;
    let isChanged: boolean = false;
    this.macchine.forEach(macchina => {
      if (this.valoriOriginari[nrMacchina] != macchina.isUtilizzata) {
        isChanged = true;
      }
      nrMacchina = nrMacchina + 1;
    })
    return isChanged;
  }

  private initValoriOriginari() {
    if (this.macchine == null || !this.macchine.length) {
      this.isSelectedAll = false;
      return;
    }
    let nrMacchina: number = 0;
    this.macchine.forEach(macchina => {
      this.valoriOriginari[nrMacchina] = macchina.isUtilizzata;
      nrMacchina = nrMacchina + 1;
    })
  }

  private defineTableColumns() {
    return [
      {
        field: null,
        header: "Macchina",
        width: "30%"
      },
      {
        field: "classe",
        header: "Classe",
        width: "10%"
      },
      {
        field: null,
        header: "Marca",
        width: "20%"
      },
      {
        field: "alimentazione",
        header: "Alimentazione",
        width: "15%"
      },
      {
        field: null,
        header: "Targa",
        width: "10%"
      },
      {
        field: null,
        header: "Possesso",
        width: "10%"
      }
    ];
  }

  controllaIstruttoreUMA() {
    if (this.authService.userSelectedRole == AuthService.roleIstruttoreUMA ||
      this.authService.userSelectedRole == AuthService.roleAppag) {
      this.inCompilazione = false;
    }
  }

}
