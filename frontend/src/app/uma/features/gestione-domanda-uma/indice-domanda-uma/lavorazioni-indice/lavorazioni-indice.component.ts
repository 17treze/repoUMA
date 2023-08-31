import { MessageService } from 'primeng/api';
import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { LavorazioniFabbricatiTipo } from 'src/app/uma/core-uma/models/enums/LavorazioniFabbricatiTipo.enum';
import { ConfirmationService } from 'primeng/api';
import { TabView } from 'primeng/tabview';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { StepRichiestaCarburante } from 'src/app/uma/core-uma/models/enums/StepRichiestaCarburante.enum';
import { TabCloseEvent } from 'src/app/uma/core-uma/models/events/TabCloseEvent.model';
import { TabLavorazioniRichiesta } from 'src/app/uma/core-uma/models/enums/TabLavorazioniRichiesta.enum';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { AuthService } from 'src/app/auth/auth.service';

@Component({
  selector: 'app-lavorazioni-indice',
  templateUrl: './lavorazioni-indice.component.html',
  styleUrls: ['./lavorazioni-indice.component.scss']
})
export class LavorazioniIndiceComponent implements OnInit {
  @Input() richiestaCarburante: RichiestaCarburanteDto;
  @Input() inCompilazione: boolean;
  @Input() idFascicolo: number;
  @Output() nextStep = new EventEmitter<number>();
  @Output() prevStep = new EventEmitter<number>();

  @ViewChild('tabView', { static: true }) tabView: TabView;

  fabbricatoEnum = LavorazioniFabbricatiTipo;
  isChangedFabbisogno: boolean;

  activeIndex: number;
  prevActiveIndex: number;

  constructor(
    private confirmationService: ConfirmationService,
    private authService: AuthService,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.controllaIstruttoreUMA();
    this.prevActiveIndex = this.getDefaultActiveIndex();
  }

  changeFabbisogno($event: boolean) {
    this.isChangedFabbisogno = $event;
  }

  onChangeTab($event: TabCloseEvent) {
    // evita apertura nuovo tab cliccato (resta aperto il corrente)
    this.openTabByIndex(this.prevActiveIndex);

    if (this.isChangedFabbisogno) {
      // this.openTabByIndex(this.prevActiveIndex);
      this.confirmationService
        .confirm({
          message: UMA_MESSAGES.datiNonSalvati,
          accept: () => {
            this.openTabByIndex($event.index);
            this.activeIndex = $event.index;
            this.prevActiveIndex = this.activeIndex;
            this.isChangedFabbisogno = false; // evita alert dopo aver premuto si
          },
          reject: () => { /** si chiude */ },
          key: "lavorazioni-dialog"
        });
    } else {
      // Se non è cambiato nessun dato sulle lavorazioni, apri il tab senza dialog
      this.openTabByIndex($event.index);
      this.activeIndex = $event.index;
      this.prevActiveIndex = this.activeIndex;
    }
  }

  // check se ci sono dati modificati prima di procedere
  goPrevStep() {
    if (this.inCompilazione && this.isChangedFabbisogno) {
      this.confirmationService.confirm({
        message: UMA_MESSAGES.datiNonSalvati,
        accept: () => {
          this.prevStep.emit(StepRichiestaCarburante.LAVORAZIONI);
        },
        reject: () => {
        },
        key: "lavorazioni-dialog"
      });
    } else {
      this.prevStep.emit(StepRichiestaCarburante.LAVORAZIONI);
    }
  }

  // check se ci sono dati modificati prima di procedere
  goNextStep() {
    if (this.inCompilazione && this.isChangedFabbisogno) {
      this.confirmationService.confirm({
        message: UMA_MESSAGES.datiNonSalvati,
        accept: () => {
          this.nextStep.emit(StepRichiestaCarburante.LAVORAZIONI);
        },
        reject: () => {
        },
        key: "lavorazioni-dialog"
      });
    } else {
      this.nextStep.emit(StepRichiestaCarburante.LAVORAZIONI);
    }
  }

  private getDefaultActiveIndex(): number {
    // Nel caso in cui non ci sono macchine dichiarate e in qualche modo entro nella pagina
    if (!this.richiestaCarburante || (!this.richiestaCarburante.haMacchineBenzina && !this.richiestaCarburante.haMacchineGasolio)) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, UMA_MESSAGES.macchineNotPresenti));
      this.activeIndex = null;
      return this.activeIndex;
    }
    if (this.richiestaCarburante.haSuperfici) {
      this.activeIndex = TabLavorazioniRichiesta.SUPERFICI;
    } else {
      this.activeIndex = TabLavorazioniRichiesta.ALTRE;
    }
    return this.activeIndex;
  }

  private openTabByIndex(indexToOpen: number) {
    this.tabView.tabs.forEach((tab, index) => {
      if (index == indexToOpen) {
        tab.selected = true;
      } else {
        tab.selected = false;
      }
    });
  }

  controllaIstruttoreUMA() {
    if (this.authService.userSelectedRole == AuthService.roleIstruttoreUMA ||
      this.authService.userSelectedRole == AuthService.roleAdmin) {
      this.inCompilazione = false;
    }
  }

}
