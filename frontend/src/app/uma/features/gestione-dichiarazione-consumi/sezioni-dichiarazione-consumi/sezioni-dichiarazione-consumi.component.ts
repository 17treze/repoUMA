import { NavigatorService } from './../../../../a4g-common/services/navigator.service';
import { HttpClientDichiarazioneConsumiUmaService } from "./../../../core-uma/services/http-client-dichiarazione-consumi-uma.service";
import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Params, Router } from "@angular/router";
import { Subscription } from "rxjs";
import { switchMap } from "rxjs/operators";
import { DateUtilService } from "src/app/a4g-common/services/date-util.service";
import { DichiarazioneConsumiDto } from "src/app/uma/core-uma/models/dto/DichiarazioneConsumiDto";
import { SezioniDichiarazioneConsumi } from "src/app/uma/core-uma/models/enums/SezioniDichiarazioneConsumi.enum";
import { MessageService, ConfirmationService } from "primeng/api";
import { UMA_MESSAGES } from "src/app/uma/uma.messages";
import {
  A4gMessages,
  A4gSeverityMessage,
} from "src/app/a4g-common/a4g-messages";
import { StatoDichiarazioneConsumiEnum } from 'src/app/a4g-common/classi/enums/uma/StatoDichiarazioneConsumi.enum';
import { AuthService } from 'src/app/auth/auth.service';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { IndiceUmaService } from 'src/app/uma/core-uma/services/indice-uma.service';
import * as FileSaver from 'file-saver';
@Component({
  selector: "app-sezioni-dichiarazione-consumi",
  templateUrl: "./sezioni-dichiarazione-consumi.component.html",
  styleUrls: ["./sezioni-dichiarazione-consumi.component.scss"],
})
export class SezioniDichiarazioneConsumiComponent implements OnInit {
  idFascicolo: string;
  dichiarazioneConsumiDto: DichiarazioneConsumiDto;
  isInCompilazione: boolean;
  mostraScarica: boolean;
  sezioni = SezioniDichiarazioneConsumi;
  umaMessages = UMA_MESSAGES;

  // Subscriptions
  routerSubscription: Subscription;
  deleteSubscription: Subscription;
  getDomandeSubscription: Subscription;
  getConsuntiviSubscription: Subscription;

  constructor(
    public dateUtilService: DateUtilService,
    private httpClientDichiarazioneConsumiUmaService: HttpClientDichiarazioneConsumiUmaService,
    private activeRoute: ActivatedRoute,
    private indiceUmaService: IndiceUmaService,
    private router: Router,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private errorService: ErrorService,
    private authService: AuthService,
    private navigatorService: NavigatorService
  ) { }

  ngOnInit() {
    this.routerSubscription = this.activeRoute.params
      .pipe(
        switchMap((params: Params) => {
          this.idFascicolo = params["idFascicolo"];
          this.indiceUmaService.idFascicolo = params["idFascicolo"];
          return this.httpClientDichiarazioneConsumiUmaService.getDichiarazioneConsumiById(params["id"]);
        })
      )
      .subscribe((dichiarazioneConsumiDto: DichiarazioneConsumiDto) => {
        this.dichiarazioneConsumiDto = dichiarazioneConsumiDto;
        this.setPageStatus(dichiarazioneConsumiDto);
      }, error => this.errorService.showError(error));
  }

  // Le dichiarazioni nello stato in compilazione sono modificabili
  private setPageStatus(dichiarazioneConsumiDto: DichiarazioneConsumiDto) {
    if (dichiarazioneConsumiDto.stato === StatoDichiarazioneConsumiEnum.IN_COMPILAZIONE &&
      this.authService.userSelectedRole !== AuthService.roleDistributore &&
      this.authService.userSelectedRole !== AuthService.roleAdmin) {
      this.indiceUmaService.READONLY_MODE_DICHIARAZIONE = false;
      this.isInCompilazione = true;
      this.mostraScarica = false;
    } else {
      // AUTORIZZATA
      this.indiceUmaService.READONLY_MODE_DICHIARAZIONE = true;
      this.isInCompilazione = false;
      this.mostraScarica = dichiarazioneConsumiDto.stato === StatoDichiarazioneConsumiEnum.IN_COMPILAZIONE ? false : true;
    }
    localStorage.setItem(
      "UMA_RO_DICH",
      this.indiceUmaService.READONLY_MODE_DICHIARAZIONE.toString()
    );
  }

  ngOnDestroy() {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
    if (this.deleteSubscription) {
      this.deleteSubscription.unsubscribe();
    }
    if (this.getDomandeSubscription) {
      this.getDomandeSubscription.unsubscribe();
    }
    if (this.getConsuntiviSubscription) {
      this.getConsuntiviSubscription.unsubscribe();
    }
  }

  eliminaDichiarazione() {
    this.confirmationService.confirm({
      message: UMA_MESSAGES.confermaCancellazioneDichiarazioneConsumi,
      accept: () => {
        this.confermaEliminaDichiarazione();
      },
      reject: () => { },
      key: "cancellazione-dichiarazione-dialog",
    });
  }

  isCaa() {
    if (this.authService.userSelectedRole === AuthService.roleCaa) {
      return true;
    }
    return false;
  }

  private confermaEliminaDichiarazione() {
    this.deleteSubscription = this.httpClientDichiarazioneConsumiUmaService.deleteDichiarazioneConsumi(this.dichiarazioneConsumiDto.id.toString())
      .subscribe(() => {
        this.messageService.add(
          A4gMessages.getToast("tst", A4gSeverityMessage.success, UMA_MESSAGES.cancellazioneDichiarazioneConsumiOK));
      }, error => this.errorService.showError(error));
  }

  onCloseToastCancellazione() {
    this.navigatorService.goToPresentazioneIstanze(this.idFascicolo);
  }

  onClickBox(sezione: String) {
    switch (sezione) {
      case SezioniDichiarazioneConsumi.PRELIEVI: {
        this.router.navigate(["prelievi"], {
          relativeTo: this.activeRoute.parent,
        });
        break;
      }
      case SezioniDichiarazioneConsumi.CLIENTI_CONTOTERZI: {
        this.router.navigate(["clienti-contoterzi"], {
          relativeTo: this.activeRoute.parent,
        });
        break;
      }
      case SezioniDichiarazioneConsumi.RICEVUTI: {
        this.router.navigate(["ricevuti"], {
          relativeTo: this.activeRoute.parent,
        });
        break;
      }
      case SezioniDichiarazioneConsumi.CONSUMI: {
        this.router.navigate(["consumi"], {
          relativeTo: this.activeRoute.parent,
        });
        break;
      }
      /*
      case SezioniDichiarazioneConsumi.TRASFERIMENTI: {
        this.router.navigate(["trasferimenti"], {
          relativeTo: this.activeRoute.parent,
        });
        break;
      }
      */
      default: {
        break;
      }
    }
  }

  public scaricaDichiarazioneConsumiFile() {
    this.httpClientDichiarazioneConsumiUmaService.getDichiarazioneConsumiFile(this.dichiarazioneConsumiDto.id.toString()).subscribe(
      result => {
        FileSaver.saveAs(new Blob([result], { type: 'application/pdf' }), this.dichiarazioneConsumiDto.id.toString() + "_dichiarazioneconsumi.pdf");
       
      }, err => {
        console.log('ERRORE: ', err);
        this.messageService.add(A4gMessages.getToast('tst-valida', A4gSeverityMessage.error, 'Errore imprevisto. Si prega di riprovare più tardi'));
      }
    );
  }

}
