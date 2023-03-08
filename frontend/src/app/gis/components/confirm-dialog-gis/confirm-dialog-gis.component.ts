import { Component, Input, OnInit } from '@angular/core';
import { CreazioneLavorazioneService } from '../../services/creazione-lavorazione.service';
import { AllegatiEvent } from '../../shared/AllegatiEvent';
import { CurrentSession } from '../../shared/CurrentSession';
import { LavorazioniEvent } from '../../shared/LavorazioniEvent';
import { PanelEvent } from '../../shared/PanelEvent';
import { Stepper } from '../../shared/Stepper';
import { ToastGisComponent } from '../toast-gis/toast-gis.component';

@Component({
  selector: 'confirm-dialog-gis',
  templateUrl: './confirm-dialog-gis.component.html',
  styleUrls: ['./confirm-dialog-gis.component.css']
})
export class ConfirmDialogGisComponent implements OnInit {
  @Input() lavorazioneSuoloInput: any;
  @Input() closeLavorazioneStep3: any;
  initFile: boolean = true;

  constructor(private creazioneLavorazioneService: CreazioneLavorazioneService, public panelEvent: PanelEvent, public stepper: Stepper,
    private toastComponent: ToastGisComponent, public currentSession: CurrentSession, public lavorazioniEvent: LavorazioniEvent,
    public allegatiEvent: AllegatiEvent) { }

  ngOnInit() {
  }

  saveCloseLavorazione() {
    setTimeout(() => {
      this.lavorazioneSuoloInput.note = this.lavorazioniEvent.noteLavorazione;
      this.lavorazioneSuoloInput.titolo = this.lavorazioniEvent.titoloLavorazione;
      this.creazioneLavorazioneService.putLavorazioneSuolo(this.lavorazioniEvent.idLavorazione, this.lavorazioneSuoloInput)
        .subscribe(response => {
          if (response['status'] === 200 || response['status'] === 201) {
            this.toastComponent.showSuccess();
            this.panelEvent.nuovaLavorazione  = false;
            this.stepper.closeLavorazioneStep3 = false;
            this.lavorazioniEvent.resetLavorazione();
           // this.poligoniDichiarati = null;
            this.lavorazioniEvent.listaLavorazioni = ['1'];
            this.stepper.resetStep = true;
            this.stepper.currentStepLavorazione = 1;
            this.stepper.goToStep = 1;
            this.lavorazioniEvent.stato = '';
           // this.lavorazioneChanged = false;


          } else {
            this.toastComponent.showError();
          }
        });
    });
  }

  noSaveCloseLavorazione() {
    this.panelEvent.nuovaLavorazione = false;
    this.stepper.closeLavorazioneStep3 = false;
    this.lavorazioniEvent.resetLavorazione();
    // this.poligoniDichiarati = null;
    this.lavorazioniEvent.listaLavorazioni = ['1'];
    this.stepper.resetStep = true;
    this.stepper.currentStepLavorazione = 1;
    this.stepper.goToStep = 1;
    this.lavorazioniEvent.stato = '';
    this.lavorazioniEvent.noteLavorazione = '';
    this.lavorazioniEvent.titoloLavorazione = '';
  }

  setInitAllegato(val) {
    this.initFile = val;
    return val;
  }

  closeDialog() {
    this.allegatiEvent.displayAllegati = false;
    this.setInitAllegato(true);
  }

}
