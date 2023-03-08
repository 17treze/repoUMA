import { PoligoniDichiaratiEvent } from './../../shared/Poligoni-dichiarati/poligoni-dichiarati-event';
import { Component, Input, OnInit } from '@angular/core';
import { CreazioneLavorazioneService } from '../../services/creazione-lavorazione.service';
import { AllegatiEvent } from '../../shared/AllegatiEvent';
import { CurrentSession } from '../../shared/CurrentSession';
import { LavorazioniEvent } from '../../shared/LavorazioniEvent';
import { PanelEvent } from '../../shared/PanelEvent';
import { Stepper } from '../../shared/Stepper';
import { ToastGisComponent } from '../toast-gis/toast-gis.component';

@Component({
  selector: 'dialogs-gis',
  templateUrl: './dialogs-gis.component.html',
  styleUrls: ['./dialogs-gis.component.css']
})
export class DialogsGisComponent implements OnInit {
  @Input() profiloUtente: any;
  @Input() detailResults: any;
  @Input() lavorazioneSuoloInput: any;
  @Input() closeLavorazioneStep3: any;
  private isVerificaPascoli = true;


  constructor(private creazioneLavorazioneService: CreazioneLavorazioneService, public panelEvent: PanelEvent, public stepper: Stepper,
    private toastComponent: ToastGisComponent, public currentSession: CurrentSession, public lavorazioniEvent: LavorazioniEvent,
    public poligoniDichiaratiEvent: PoligoniDichiaratiEvent) { }

  ngOnInit() {
  }

  saveCloseLavorazione() {
    setTimeout(() => {
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
  }

}
