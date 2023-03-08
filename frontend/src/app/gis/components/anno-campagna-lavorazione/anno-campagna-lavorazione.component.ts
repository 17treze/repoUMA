import { LavorazioneObj } from './../../models/lavorazione/lavorazione.model';
import { CreazioneLavorazioneService } from './../../services/creazione-lavorazione.service';
import { contextType } from './../../shared/ContextType';
import { OperazioneLavorazione } from './../../shared/OperazioneLavorazione.enum';
import { Lavorazione } from './../../shared/Lavorazione';
import { SearchGisService } from './../../services/search-gis.service';
import { MapService } from './../mappa/map.service';
import { CurrentSession } from './../../shared/CurrentSession';
import { PanelEvent } from 'src/app/gis/shared/PanelEvent';
import { NuovoAnnoCampagnaModel } from './../../models/lavorazione/nuovoAnnoCampagna.model';
import { Component, OnInit, Output, ViewEncapsulation, EventEmitter, Input, SimpleChanges, OnChanges } from '@angular/core';
import { GisUtilsService } from '../../shared/gis-utils.service';
import { LavorazioniEvent } from '../../shared/LavorazioniEvent';
import { SelectItem, ConfirmationService } from 'primeng-lts/api';
import { GisMessaggiToastCostants } from '../../shared/messaggi-toast.constants';
import { GestioneCampagnaService } from './../../services/gestione-campagna.service';
import * as cloneDeep from 'lodash/cloneDeep';
import { Stepper } from '../../shared/Stepper';
import { StatoLavorazioneSuolo } from '../../shared/StatoLavorazioneSuolo.enum';

@Component({
  selector: 'gis-anno-campagna-lavorazione',
  templateUrl: './anno-campagna-lavorazione.component.html',
  styleUrls: ['./anno-campagna-lavorazione.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AnnoCampagnaLavorazioneComponent implements OnInit, OnChanges {
  @Input() lavorazione: Lavorazione;
  @Output() openStepLavorazione = new EventEmitter();
  anno_campagna: SelectItem[];
  annoCampagnaSelezionato: number | undefined;
  annoCampagnaLavorazione: number;
  visualizzaCombo: String;

  constructor(public lavorazioniEvent: LavorazioniEvent, private gisUtils: GisUtilsService, public panelEvent: PanelEvent,
    private confirmationService: ConfirmationService, private gisMessaggiToast: GisMessaggiToastCostants,
    private gestioneCampagna: GestioneCampagnaService, public contestoAttivo: CurrentSession, public stepper: Stepper,
    private creazioneLavorazioneService: CreazioneLavorazioneService) { }

  ngOnInit() {
    this.annoCampagnaSelezionato = this.lavorazioniEvent.objectLavorazione?.campagna;
    this.annoCampagnaLavorazione = cloneDeep(this.lavorazioniEvent.objectLavorazione?.campagna);
    this.anno_campagna = this.gisUtils.getComboAnniCampagna(false);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.lavorazione && changes.lavorazione.currentValue &&
      changes.lavorazione.currentValue.campagna) {
      this.annoCampagnaSelezionato = changes.lavorazione.currentValue.campagna;
    }
  }
  cambioAnnoCampagna() {
    this.confirmationService.confirm({
      message: this.gisMessaggiToast.warningCambioAnnoDiCampagna,
      key: 'cambioAnnoCampagnaLavorazione',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        setTimeout(() => {
          const body: NuovoAnnoCampagnaModel = { annoCampagna: this.annoCampagnaSelezionato };
          this.gestioneCampagna.cambioCampagna(this.lavorazioniEvent.idLavorazione, body).subscribe((res) => {
            if (res['statusText'] === 'OK') {
              // Chiudo il pannello di ricerca dei dichiarati
              this.panelEvent.openPoligoniDichiarato = false;
              this.panelEvent.nuovaLavorazione = false;
              this.lavorazioniEvent.setOperazione(OperazioneLavorazione.chiudi);
              this.lavorazioniEvent.resetLavorazione();
              this.refreshLavorazione(res['body']);
            }
          });
        });
      },
      reject: () => {
        this.annoCampagnaSelezionato = this.annoCampagnaLavorazione;
      }
    });
  }

  refreshLavorazione(id) {
    this.creazioneLavorazioneService.getLavorazioneSuolo(id).subscribe(
      (data) => {
        this.lavorazioniEvent.objectLavorazione = data;
        this.openStepLavorazione.emit(this.lavorazioniEvent.objectLavorazione);
        this.stepper.currentStepLavorazione = 1;
        this.contestoAttivo.context = contextType.CREAZIONE_LAVORAZIONE;
        this.gestioneCampagna.setAnnoCampagna(this.annoCampagnaSelezionato);
        this.gestioneCampagna.disabilitaSelectAnnoCampagna();
      },
      (error) => {
        console.log(error);
      });
  }

  changeYearColor() {
    const year = this.lavorazioniEvent.objectLavorazione?.campagna;
    return this.gestioneCampagna.setColorCampagna(year);
  }

  checkStatoLavorazione() {
    this.visualizzaCombo = this.lavorazioniEvent.stato;
    if (this.visualizzaCombo) {
      if (this.visualizzaCombo === StatoLavorazioneSuolo.IN_CREAZIONE || this.visualizzaCombo === StatoLavorazioneSuolo.IN_MODIFICA) {
        return true;
      } else {
        return false;
      }
    }
  }
}


