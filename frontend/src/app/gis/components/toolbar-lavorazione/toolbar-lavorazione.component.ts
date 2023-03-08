import { LavorazioneModel, LavorazioneObj } from './../../models/lavorazione/lavorazione.model';
import { GestioneCampagnaService } from './../../services/gestione-campagna.service';
import { CurrentSession } from './../../shared/CurrentSession';
import { MapService } from './../mappa/map.service';
import { GisUtilsService } from './../../shared/gis-utils.service';
import { MapEvent } from './../../shared/MapEvent';
import { CreazioneLavorazioneService } from './../../services/creazione-lavorazione.service';
import { ToastGisComponent } from './../toast-gis/toast-gis.component';
import { Stepper } from './../../shared/Stepper';
import { LavorazioniEvent } from './../../shared/LavorazioniEvent';
import { PanelEvent } from './../../shared/PanelEvent';
import { ConfirmationService, SelectItem } from 'primeng-lts/api';
import { Component, EventEmitter, Input, OnInit, Output, ViewChild, ViewEncapsulation } from '@angular/core';
import { StatoLavorazioneSuolo, StatoLavorazioneSuoloDecode } from '../../shared/StatoLavorazioneSuolo.enum';
import { HeaderLavorazione } from './../../shared/HeaderLavorazione';
import { PropertyLayer } from '../../shared/PropertyLayer.enum';
import { OperazioneLavorazione } from "../../shared/OperazioneLavorazione.enum";
import { contextType } from '../../shared/ContextType';
@Component({
  selector: 'gis-toolbar-lavorazione',
  templateUrl: './toolbar-lavorazione.component.html',
  styleUrls: ['./toolbar-lavorazione.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ToolbarLavorazioneComponent implements OnInit {
  showResults: boolean;
  searchResults: any;
  searchFormParams: any;
  detailResults: object = null;
  showDetail: boolean = false;
  poligoniDichiarati: any;
  @Output() headerLavorazione = new EventEmitter<string>();
  @Output() deleteLavorazioneEmit: EventEmitter<any> = new EventEmitter();
  @Output() openStepLavorazione = new EventEmitter();
  anno_campagna: SelectItem[];
  annoCampagnaLav: number | undefined;
  items: Array<any>;

  constructor(private creazioneLavorazioneService: CreazioneLavorazioneService, public panelEvent: PanelEvent,
    public lavorazioniEvent: LavorazioniEvent, public stepper: Stepper, private toastComponent: ToastGisComponent,
    public mapEvent: MapEvent, private gisUtils: GisUtilsService, public contestoAttivo: CurrentSession,
    private gestioneCampagna: GestioneCampagnaService) { }

  ngOnInit() {
    if (this.statoLavorazioneSuolo.CONSOLIDATA_SU_AGS) {
      this.annoCampagnaLav = this.lavorazioniEvent.objectLavorazione?.campagna;
      this.anno_campagna = this.gisUtils.getComboAnniCampagna(false);
      this.items = [];
      this.anno_campagna.forEach(element => {
        this.items.push({
          'label': element.value, command: () => {
            this.copiaDaSelectList(element.value);
          }
        });
      });
    }
  }


  public get statoLavorazioneSuolo(): typeof StatoLavorazioneSuolo {
    return StatoLavorazioneSuolo;
  }

  triggerDeleteLavorazione() {
    this.deleteLavorazioneEmit.emit();
  }
  modificaLavorazione(id) {
    if (this.mapEvent.hasNotSavedAction()) {
      this.toastComponent.showWarningValida();
    } else {
      this.creazioneLavorazioneService.putModificaStatoLavorazioneInModifica(id, StatoLavorazioneSuolo.IN_MODIFICA)
        .subscribe((response: any) => {
          if (response['status'] === 200) {
            // Modifica lo stato in caso serva tenere aperta la lavorazione
            this.lavorazioniEvent.setOperazione(OperazioneLavorazione.inizioModifica);
            this.lavorazioniEvent.stato = StatoLavorazioneSuolo.IN_MODIFICA;
            this.lavorazioniEvent.statoMappato = StatoLavorazioneSuoloDecode.decode(this.lavorazioniEvent.stato);
            this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.MODIFICA_LAVORAZIONE;
            this.stepper.goToStep = 1;
            this.stepper.currentStepLavorazione = 1;
            this.stepper.refreshStep = !this.stepper.refreshStep;
          } else if (response['status'] === 412) {
            this.toastComponent.showErrorModificaLavorazioneMsg("Errore in riempimento buchi della lavorazione");
          } else {
            this.toastComponent.showErrorModificaLavorazione();
          }
        },
          err => {
            if (err.status === 412)
              this.toastComponent.showErrorModificaLavorazioneMsg("Errore in riempimento buchi della lavorazione");
            else
              this.toastComponent.showErrorModificaLavorazione();
          });
    }
  }

  consolidaLavorazioneSuA4SPopup(idLavorazione) {
    if (this.mapEvent.hasNotSavedAction()) {
      this.toastComponent.showWarningValida();
    } else {
      this.consolidaLavorazioneA4S(idLavorazione);
    }
  }
  consolidaLavorazioneA4S(idLavorazione) {
    const message = 'Attenzione! Eventuali poligoni di suolo cross griglia verranno ritagliati';
    this.toastComponent.alertConsolidaLavorazioneInAGS(message);
    this.creazioneLavorazioneService.putConsolidaLavorazioneA4S(idLavorazione)
      .subscribe((response: any) => {
        if (response['status'] === 200) {
          this.stepper.goToStep = 4;
          this.stepper.refreshStep = !this.stepper.refreshStep;
          this.toastComponent.showSuccessConsolidaLavorazione();
          this.lavorazioniEvent.ricaricaLavorazioneLayers();
          this.lavorazioniEvent.refreshWmsLayer([PropertyLayer.CODICE_LAYER_SUOLO_VIGENTE_BO, PropertyLayer.CODICE_LAYER_SUOLO_VIGENTE_INCLUSO_IN_LAVORAZIONI_BO]);
          this.lavorazioniEvent.editaCelleWorkSpace = false;
        }
      },
        err => {
          this.toastComponent.showErrorConsolidaLavorazione(err.error);
          // ricarico per recuperare eventuali rielaborazioni da validazione
          this.lavorazioniEvent.ricaricaLavorazioneLayers();
        });
  }

  consolidaLavorazioneAGS(idLavorazione) {
    this.creazioneLavorazioneService.putConsolidaLavorazioneAGS(idLavorazione)
      .subscribe((response: any) => {
        if (response['status'] === 202) {
          this.stepper.goToStep = 4;
          this.stepper.refreshStep = !this.stepper.refreshStep;
          this.toastComponent.showSuccessConsolidaLavorazioneInAGS();
          this.lavorazioniEvent.editaCelleWorkSpace = false;
        }
      },
        err => {
          this.toastComponent.showErrorConsolidaLavorazione(err.error);
        });
  }

  refreshStatoLavorazione() {
    this.getLavorazioneFromJobFme().then((results: any) => {
      this.lavorazioniEvent.stato = results.stato;
      this.lavorazioniEvent.statoMappato = StatoLavorazioneSuoloDecode.decode(results.stato);

      if (results.stato === StatoLavorazioneSuolo.CONSOLIDATA_SU_AGS) {
        this.lavorazioniEvent.refreshWmsLayer([PropertyLayer.CODICE_LAYER_SUOLO_VIGENTE_BO,
        PropertyLayer.CODICE_LAYER_SUOLO_VIGENTE_INCLUSO_IN_LAVORAZIONI_BO]);
      }
    });
  }

  getLavorazioneFromJobFme() {
    return new Promise(((resolve, reject) => {
      this.creazioneLavorazioneService.refreshStato(this.lavorazioniEvent.idLavorazione).subscribe(
        (res) => {
          if (res.stato) {
            resolve(res);
          }
        },
        (err) => {
          console.log(err);
          this.toastComponent.showMsgRefreshJobFme(err.error);
          reject(err);
        });
    }));
  }

  copiaLavorazione(element) {
    console.log('copia', this.lavorazioniEvent.idLavorazione, element);
    if (element) {
      this.postCopiaLavorazione(element);
    }
  }
  copiaDaSelectList(element) {
    if (element) {
      console.log('copia', this.lavorazioniEvent.idLavorazione, element);
      if (element) {
        this.postCopiaLavorazione(element);
      }
    }
  }
  postCopiaLavorazione(campagna) {
    this.creazioneLavorazioneService.copiaLavorazione(this.lavorazioniEvent.idLavorazione, campagna)
      .subscribe((idNuovaLavorazione: number) => {
        if (idNuovaLavorazione) {
          this.toastComponent.showSuccess();
          this.getNuovaLavorazione(idNuovaLavorazione).then((lavorazione) => {
            if (lavorazione) {
              const myLav: LavorazioneObj = lavorazione;
              this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.MODIFICA_LAVORAZIONE;
              this.stepper.goToStep = 1;
              this.stepper.currentStepLavorazione = 1;
              this.stepper.refreshStep = !this.stepper.refreshStep;
              this.contestoAttivo.context = contextType.CREAZIONE_LAVORAZIONE;
              this.lavorazioniEvent.editaCelleWorkSpace = false;
              this.lavorazioniEvent.readOnly = false;
              this.gestioneCampagna.setAnnoCampagna(myLav.campagna);
              this.gestioneCampagna.disabilitaSelectAnnoCampagna();
              this.openStepLavorazione.emit(myLav);
            }
          });
        } else {
          this.toastComponent.showError();
        }
      },
        (error) => {
          console.log(error);
          this.toastComponent.showError();
        });
  }
  getNuovaLavorazione(idNuovaLavorazione) {
    return new Promise(((resolve, reject) => {
      this.creazioneLavorazioneService.getLavorazioneSuolo(idNuovaLavorazione).subscribe(
        (res: LavorazioneObj) => {
          resolve(res);
        },
        (error) => {
          console.log(error);
          reject(error);
        });
    }));
  }

}
