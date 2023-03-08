import { ErrorService } from './../../a4g-common/services/error.service';
import { ImpostazioniIstruttoreUmaVM } from './../../a4g-common/classi/viewModels/ImpostazioniIstruttoreUmaVM';
import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../auth/auth.service';
import { MessageService } from 'primeng/api';
import {
  A4gMessages,
  A4gSeverityMessage,
} from 'src/app/a4g-common/a4g-messages';
import { PopupIstruttoreUmaConfigurazioneComponent } from './popup-istruttore-uma-configurazione/popup-istruttore-uma-configurazione.component';
import { HttpClientConfigUmaService } from 'src/app/a4g-common/services/http-client-config-uma.service';
import { Subscription } from 'rxjs';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
import { PopupGeneraFileXmlCup } from './popup-genera-file-xml-cup/popup-genera-file-xml-cup.component';
import { PopupScaricaElenchiUmaComponent } from 'src/app/a4g-common/popup-scarica-elenchi-uma/popup-scarica-elenchi-uma.component';

@Component({
  selector: 'app-presentazione-istanze-istruttoria',
  templateUrl: './presentazione-istanze-istruttoria.component.html',
  styleUrls: ['./presentazione-istanze-istruttoria.component.scss'],
})
export class PresentazioneIstanzeIstruttoriaComponent implements OnInit, OnDestroy {
  @ViewChild("popupIstruttoreUmaConfigurazioneComponent", { static: true })
  popupIstruttoreUmaConfigurazioneComponent: PopupIstruttoreUmaConfigurazioneComponent;
  @ViewChild("popupGeneraFileXmlCup", { static: true })
  popupGeneraFileXmlCup: PopupGeneraFileXmlCup;
  @ViewChild('popupScaricaElenchiUma')
  popupScaricaElenchiUma: PopupScaricaElenchiUmaComponent;

  getConfigUmaSubscription: Subscription;
  postConfigUmaSubscription: Subscription;
  posGeneraFileXmlCup: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private messageService: MessageService,
    private httpClientConfigUmaService: HttpClientConfigUmaService,
    private errorService: ErrorService,
    private dateUtilService: DateUtilService
  ) { }

  ngOnInit() {
    this.messageService.clear();
  }

  ngOnDestroy(): void {
    if (this.getConfigUmaSubscription) {
      this.getConfigUmaSubscription.unsubscribe();
    }

    if (this.postConfigUmaSubscription) {
      this.postConfigUmaSubscription.unsubscribe();
    }

    if (this.posGeneraFileXmlCup) {
      this.posGeneraFileXmlCup.unsubscribe();
    }
  }

  openDialogIstruttoreConfigurazione() {
    let impostazioni = {} as ImpostazioniIstruttoreUmaVM;
    this.getConfigUmaSubscription = this.httpClientConfigUmaService.getConfigurazioneUma(this.dateUtilService.getCurrentYear().toString()).subscribe((data: string) => {
      if (data != null) {
        impostazioni.dataDefaultPrelievo = this.dateUtilService.toDateWithNoOffset(new Date(data));
      } else {
        impostazioni = null;
      }
      this.popupIstruttoreUmaConfigurazioneComponent.open(impostazioni);
    }, error => this.errorService.showError(error));
  }

  onClickGeneraXML() {
    console.log("Genera XML da csv");
    this.popupGeneraFileXmlCup.open();
  }

  closeDialogIstruttoreUmaConfig($event: ImpostazioniIstruttoreUmaVM) {
    this.postConfigUmaSubscription = this.httpClientConfigUmaService.postConfigurazioneUma($event.dataDefaultPrelievo).subscribe((res: number) => {
      this.messageService.add(A4gMessages.getToast("tst", A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOKImpostazioni));
    }, error => this.errorService.showError(error));
  }

  closeDialogGeneraFileXmlCup() {
    this.postConfigUmaSubscription = null;
  }

  onClickPac() {
    if (
      this.authService.isUserInRole(AuthService.roleIstruttoreDomandaUnica) ||
      this.authService.isUserInRole(AuthService.roleAdmin) ||
      this.authService.isUserInRole(AuthService.roleAppag) ||
      this.authService.isUserInRole(AuthService.roleViewerPAT)
    ) {
      this.router.navigate(['../istruttoriaPac'], {
        relativeTo: this.route,
      });
    } else {
      this.messageService.add(
        A4gMessages.getToast(
          'tst',
          A4gSeverityMessage.error,
          A4gMessages.UTENTE_NON_ABILITATO
        )
      );
    }
  }

  onClickCertificazioniAntimafia() {
    if (
      this.authService.isUserInRole(AuthService.roleIstruttoreAMF) ||
      this.authService.isUserInRole(AuthService.roleAdmin) ||
      this.authService.isUserInRole(AuthService.roleAppag) ||
      this.authService.isUserInRole(AuthService.roleAltroEnte) ||
      this.authService.isUserInRole(AuthService.roleViewerPAT)
    ) {
      this.router.navigate(['../istruttoriaAntimafia'], {
        relativeTo: this.route,
      });
    } else {
      this.messageService.add(
        A4gMessages.getToast(
          'tst',
          A4gSeverityMessage.error,
          A4gMessages.UTENTE_NON_ABILITATO
        )
      );
    }
  }

  onClickPac1420() {
    if (
      this.authService.isUserInRole(AuthService.roleIstruttoreDomandaUnica) ||
      this.authService.isUserInRole(AuthService.roleAdmin) ||
      this.authService.isUserInRole(AuthService.roleAppag) ||
      this.authService.isUserInRole(AuthService.roleAltroEnte) ||
      this.authService.isUserInRole(AuthService.roleViewerPAT)
    ) {
      this.router.navigate(['../istruttoriaPac1420'], {
        relativeTo: this.route,
      });
    } else {
      this.messageService.add(
        A4gMessages.getToast(
          'tst',
          A4gSeverityMessage.error,
          A4gMessages.UTENTE_NON_ABILITATO
        )
      );
    }
  }

  onClickUma(percorso: string) {
    if (
      this.authService.userSelectedRole === AuthService.roleIstruttoreUMA ||
      this.authService.userSelectedRole === AuthService.roleAppag ||
      this.authService.userSelectedRole === AuthService.roleAdmin ||
      this.authService.userSelectedRole === AuthService.roleAltroEnte ||
      this.authService.userSelectedRole === AuthService.roleViewerPAT
    ) {
      this.router.navigate(['../' + percorso], {
        relativeTo: this.route,
      });
    } else {
      this.messageService.add(
        A4gMessages.getToast(
          'tst',
          A4gSeverityMessage.error,
          A4gMessages.UTENTE_NON_ABILITATO
        )
      );
    }
  }

  ruoliAbilitatiPrimoGruppo(): boolean {
    if (this.authService.userSelectedRole !== AuthService.roleIstruttoreUMA) {
      return true;
    }
    return false;
  }

  ruoliAbilitatiSecondoGruppo(): boolean {
    // SE AMBIENTE DI PRODUZIONE NON VIENE VISUALIZZATO
    return this.authService.userSelectedRole === AuthService.roleIstruttoreUMA ||
      this.authService.userSelectedRole === AuthService.roleAppag ||
      this.authService.userSelectedRole === AuthService.roleAdmin ||
      this.authService.userSelectedRole === AuthService.roleAltroEnte ||
      this.authService.userSelectedRole === AuthService.roleViewerPAT;
  }

  public displayValidazionePrelievi() {
    return this.authService.userSelectedRole !== AuthService.roleAltroEnte &&
      this.authService.userSelectedRole !== AuthService.roleViewerPAT;
  }

  ruoloAPPAG(): boolean {
    // SE AMBIENTE DI PRODUZIONE NON VIENE VISUALIZZATO
    return this.authService.userSelectedRole === AuthService.roleAppag ||
      this.authService.userSelectedRole === AuthService.roleAdmin;
  }

  openDialogScarica() {
    this.popupScaricaElenchiUma.open();
  }

}
