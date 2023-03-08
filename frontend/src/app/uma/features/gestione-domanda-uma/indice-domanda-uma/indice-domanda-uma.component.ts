import { ErrorDTO } from './../../../../a4g-common/interfaces/error.model';
import { StepRichiestaCarburante } from "src/app/uma/core-uma/models/enums/StepRichiestaCarburante.enum";
import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Params, Router } from "@angular/router";
import { ConfirmationService, MenuItem, MessageService } from "primeng/api";
import { EMPTY, Subscription } from "rxjs";
import { switchMap, catchError } from "rxjs/operators";
import {
  A4gMessages,
  A4gSeverityMessage,
} from "src/app/a4g-common/a4g-messages";
import { AppBreadcrumbService } from "src/app/a4g-common/app-breadcrumb/app.breadcrumb.service";
import { DateUtilService } from "src/app/a4g-common/services/date-util.service";
import { RichiestaCarburanteDto } from "src/app/uma/core-uma/models/dto/RichiestaCarburanteDto";
import { MacchinaDto } from "src/app/uma/core-uma/models/dto/MacchinaDto";
import { HttpClientDomandaUmaService } from "src/app/uma/core-uma/services/http-client-domanda-uma.service";
import { HttpClientMacchineUmaService } from "src/app/uma/core-uma/services/http-client-macchine-uma.service";
import { UMA_MESSAGES } from "src/app/uma/uma.messages";
import { GestioneDomandaUmaService } from "../gestione-domanda-uma.service";
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import * as FileSaver from 'file-saver';
@Component({
  selector: "app-indice-domanda-uma",
  templateUrl: "./indice-domanda-uma.component.html",
  styleUrls: ["./indice-domanda-uma.component.scss"],
})
export class IndiceDomandaUmaComponent implements OnInit {
  items: MenuItem[];        /** step dello stepper */
  activeIndex: number;      /** indice dello step attivo */
  idFascicolo: number;      /** idFascicolo corrente */
  inCompilazione: boolean;  /** se lo stato è in compilazione, è possibile modificare la pagina, altrimenti può essere consultata */
  mostraScarica: boolean;s
  richiestaCarburante: RichiestaCarburanteDto;
  macchine: Array<MacchinaDto>;

  // Subscriptions
  stampaSubscription: Subscription;
  routerSubscription: Subscription;
  deleteSubscription: Subscription;

  constructor(
    public dateUtilService: DateUtilService,
    private httpClientDomandaUma: HttpClientDomandaUmaService,
    private activeRoute: ActivatedRoute,
    private router: Router,
    private httpClientMacchineUmaService: HttpClientMacchineUmaService,
    private gestioneDomandaUmaService: GestioneDomandaUmaService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private breadCrumbService: AppBreadcrumbService,
    private errorService: ErrorService
  ) { }

  ngOnInit() {
    this.items = [];
    this.macchine = [];
    this.routerSubscription = this.activeRoute.params
      .pipe(
        switchMap((params: Params) => {
          this.idFascicolo = params['idFascicolo'];
          return this.httpClientDomandaUma.getDomandaById(params["idDomanda"]);
        }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err);
          return EMPTY;
        }),
        switchMap((richiesta: RichiestaCarburanteDto) => {
          this.richiestaCarburante = richiesta;
          this.items = [
            { label: "Macchine" },
            { label: "Lavorazioni" },
            { label: "Fabbisogno" },
          ];

          const pageStatus = this.gestioneDomandaUmaService.isInCompilazione(
            richiesta,
            this.items
          );

          this.inCompilazione = pageStatus.inCompilazione;
          this.mostraScarica = pageStatus.mostraScarica;
          this.loadLastStep();
          return this.httpClientMacchineUmaService.getMacchineByIdDomanda(
            this.richiestaCarburante.id.toString()
          );
        })
      )
      .subscribe(
        (macchine: Array<MacchinaDto>) => {
          this.macchine = macchine;
        },
        (error: ErrorDTO) => this.errorService.showError(error)
      );
  }

  ngOnDestroy() {
    if (this.stampaSubscription) {
      this.stampaSubscription.unsubscribe();
    }
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
    if (this.deleteSubscription) {
      this.deleteSubscription.unsubscribe();
    }
  }

  updateRichiesta($event: RichiestaCarburanteDto) {
    this.richiestaCarburante = $event;
  }

  activeIndexChange($event: any) {
    // console.log($event);
  }

  nextStep($event: number) {
    if (
      $event >= StepRichiestaCarburante.MACCHINE &&
      $event <= StepRichiestaCarburante.PROTOCOLLAZIONE
    ) {
      this.activeIndex = $event + 1;
    } else {
      this.activeIndex = StepRichiestaCarburante.MACCHINE;
    }
  }

  prevStep($event: number) {
    if (
      $event >= StepRichiestaCarburante.LAVORAZIONI &&
      $event <= StepRichiestaCarburante.PROTOCOLLAZIONE
    ) {
      this.activeIndex = $event - 1;
    } else {
      this.activeIndex = StepRichiestaCarburante.MACCHINE;
    }
  }

  eliminaDomanda() {
    this.confirmationService.confirm({
      message: UMA_MESSAGES.confermaCancellazioneDomanda,
      accept: () => {
        this.confermaEliminaDomanda();
      },
      reject: () => { },
      key: "cancellazione-domanda-dialog",
    });
  }

  private loadLastStep() {
    // Se la richiesta è in sola lettura comincio sempre dal primo step
    if (!this.inCompilazione) {
      this.activeIndex = StepRichiestaCarburante.MACCHINE;
      return;
    }
    // ... altrimenti dall'ultimo step compilato
    if (
      this.richiestaCarburante.carburanteRichiesto != null &&
      (this.richiestaCarburante.carburanteRichiesto.benzina != null ||
        this.richiestaCarburante.carburanteRichiesto.gasolio != null ||
        this.richiestaCarburante.carburanteRichiesto.gasolioSerre != null ||
        this.richiestaCarburante.carburanteRichiesto.gasolioTerzi != null)
    ) {
      this.activeIndex = StepRichiestaCarburante.FABBISOGNO;
    } else if (this.richiestaCarburante.haDichiarazioni) {
      this.activeIndex = StepRichiestaCarburante.LAVORAZIONI;
    } else {
      this.activeIndex = StepRichiestaCarburante.MACCHINE;
    }
  }

  // Confermo cancellazione
  private confermaEliminaDomanda() {
    this.deleteSubscription = this.httpClientDomandaUma
      .deleteDomandaById(this.richiestaCarburante.id.toString())
      .subscribe(
        () => {
          this.messageService.add(
            A4gMessages.getToast(
              "tst-cancellazione",
              A4gSeverityMessage.success,
              UMA_MESSAGES.cancellazioneDomandaOK
            )
          );
        },
        (error) => this.errorService.showError(error)
      );
  }

  public stampaRichiestaCarburantePDF() {
    this.stampaSubscription = this.httpClientDomandaUma.getRichiestaCarburanteFile(this.richiestaCarburante.id.toString()).subscribe(pdf => {
      FileSaver.saveAs(new Blob([pdf], { type: 'application/pdf' }), this.richiestaCarburante.id.toString() + this.getFileName());
    }, err => {
      console.log('ERRORE: ', err);
      this.errorService.showErrorWithMessage(UMA_MESSAGES.erroreStampaRichiesta, 'tst');
    });
  }

  private getFileName(): string {
    return this.richiestaCarburante.idRettificata == null ? '_richiestacarburante.pdf' : '_rettificacarburante.pdf';
  }

  onCloseToastCancellazione() {
    if (this.idFascicolo == null) {
      this.router.navigate(['/']);
    }
    let root: string;
    if (localStorage.getItem("selectedRole") == 'appag') { root = "funzioniPat/"; }
    if (localStorage.getItem("selectedRole") == 'amministratore') { root = "funzioniPat/"; }
    if (localStorage.getItem("selectedRole") == 'caa') { root = "funzioniCaa/"; }
    if (localStorage.getItem("selectedRole") == 'azienda') { root = "/"; }
    if (localStorage.getItem("selectedRole") == 'gestoreutenti') { root = "funzioniPat/"; }
    if (localStorage.getItem("selectedRole") == 'viewer_pat') { root = "funzioniPatVisualizzatore/"; }
    if (localStorage.getItem("selectedRole") == 'istruttoreuma') { root = "funzioniPatIstruttoreUMA/"; }
    if (localStorage.getItem("selectedRole") == 'operatore_distributore') { root = "funzioniDistributore/"; }
    if (localStorage.getItem("selectedRole") == 'amministratore') { root = "funzioniPat/"; }
    if (localStorage.getItem("selectedRole") == 'istruttoreamf') { root = "funzioniPat/"; }
    if (localStorage.getItem("selectedRole") == 'istruttoredu') { root = "funzioniPat/gestioneIstruttoria/"; }
    if (localStorage.getItem("selectedRole") == 'viewer_altro_ente') { root = "funzioniOperatoreVisualizzatore/"; }
    this.router.navigate([`${root}fascicolo/${this.idFascicolo.toString()}/presentazioneIstanze`]);
  }
}
