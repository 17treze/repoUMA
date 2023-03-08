import { MapEvent } from './../../shared/MapEvent';
import { Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges } from '@angular/core';
import { CreazioneLavorazioneService } from '../../services/creazione-lavorazione.service';
import { ToastGisComponent } from '../toast-gis/toast-gis.component';
import { StatoLavorazioneSuolo, StatoLavorazioneSuoloDecode } from '../../shared/StatoLavorazioneSuolo.enum';
import { Observable, throwError } from 'rxjs';
import { SearchGisService } from '../../services/search-gis.service';
import { EsitoLavorazioneDichiarato, EsitoLavorazioneDichiaratoDecode } from '../../shared/EsitoLavorazioneDichiarato.enum';
import { CurrentSession } from '../../shared/CurrentSession';
import { MapService } from '../mappa/map.service';
import { PanelEvent } from '../../shared/PanelEvent';
import { Stepper } from '../../shared/Stepper';
import { LavorazioniEvent } from '../../shared/LavorazioniEvent';
import { HeaderLavorazione } from './../../shared/HeaderLavorazione';
import { ConfirmationService } from "primeng/api";
import { OperazioneLavorazione } from "../../shared/OperazioneLavorazione.enum";
import Point from 'ol/geom/Point';
import { PropertyLayer } from '../../shared/PropertyLayer.enum';
import { MapidService } from '../mappa/mapid.service';
import { LavorazioneChangeStatus } from '../../shared/Lavorazione';


@Component({
  selector: 'gis-wizard-component',
  templateUrl: './wizard-component.component.html',
  styleUrls: ['./wizard-component.component.css']
})
export class WizardComponentComponent implements OnInit, OnDestroy, OnChanges {
  @Input() profiloUtente: any;
  @Input() resetStep: any;
  @Input() poligoniById: any; // poligoni suolo dichiarato
  @Input() searchFormParamsLavorazione: any;
  @Input() totalsPoligoniDichiaratoInLavorazione: number; // totate poligoni suolo dichiarato
  @Output() stepEvent = new EventEmitter<number>();
  @Output() lavorazioneChangedOutput = new EventEmitter<string>();
  @Input() refreshStep: boolean;
  totalsPoligoniSuolo = -1;
  poligoniSuolo: any;

  params: any;
  lavorazioneSuolo: any;
  public itemsToShow: any;
  public isFullListDisplayed = false;

  private isVerificaPascoli = true;

  dettaglioLavorazione = false;
  response: any;
  returnedDataLavorazione$: Observable<any>;

  lavorazioneChanged: any;
  subscription: any;
  closePoligoniDichiarato: boolean;

  constructor(private searchService: SearchGisService, private creazioneLavorazioneService: CreazioneLavorazioneService,
    private toastComponent: ToastGisComponent, public currentSession: CurrentSession, private mapService: MapService,
    public panelEvent: PanelEvent, public stepper: Stepper, public lavorazioniEvent: LavorazioniEvent,
    private confirmationService: ConfirmationService, public mapEvent: MapEvent) {
    stepper.currentStepLavorazione = 1;
  }

  ngOnInit() {
    this.lavorazioniEvent.stackAdl = false;
    this.lavorazioniEvent.calcoloAdlActived = false;

    this.subscription = this.mapService.getReloadPoligoniDichiaratiEmitter()
      .subscribe((string) => {
        this.getSuoloDichiarato();
      });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.resetStep && changes.resetStep.currentValue) {
      // Svuoto la lista dei poligoni vigenti
      this.poligoniSuolo = [];
      this.totalsPoligoniSuolo = -1;
      // Svuoto la lista dei poligoni di dichiarato
      this.poligoniById = [];
      // this.poligoniById = [];
      this.stepper.currentStepLavorazione = 1;
      this.stepEvent.emit(this.stepper.currentStepLavorazione); // esporto lo stato del wizard

    }


    if (this.stepper.goToStep && this.stepper.goToStep === 4) {
      this.poligoniSuolo = [];
      this.totalsPoligoniSuolo = -1;

      this.poligoniById = [];
      this.totalsPoligoniDichiaratoInLavorazione = -1;

      this.stepper.currentStepLavorazione = this.stepper.goToStep;
      // if ( changes.stepEvent.goToStep && changes.stepEvent.goToStep.currentValue &&  changes.stepEvent.goToStep.currentValue === 4) {
      this.getLavorazione().then(value => {
        this.getSuoloVigente();
        this.getSuoloDichiarato().then(value => {
          this.stepper.goToStep = undefined;
        });

      });
    }
    this.stepper.currentStepLavorazione = this.stepper.currentStepLavorazione;
    this.stepEvent.emit(this.stepper.currentStepLavorazione); // esporto lo stato del wizard

  }

  /*

  resetSteps($event) {
    this.stepper.currentStepLavorazione = $event;
    if(this.stepper.currentStepLavorazione === 4){
      this.getLavorazione().then(value =>{
        this.getSuoloVigente().then(value => {
        });
      });
    }
  }
  */

  nextStep() {
    if (this.stepper.currentStepLavorazione === 1) { // vado nello step 2
      this.getSuoloVigente().then(value => {
        this.stepper.currentStepLavorazione += 1;
        this.panelEvent.openPoligoniDichiarato = false;
        this.stepEvent.emit(this.stepper.currentStepLavorazione); // esporto lo stato del wizard

      });

    } else if (this.stepper.currentStepLavorazione === 2) { // vado nello step 3
      this.validaLavorazioneAndGetLavorazionePromise().then(value => {
        this.stepper.currentStepLavorazione += 1;
        this.stepEvent.emit(this.stepper.currentStepLavorazione); // esporto lo stato del wizard
      }); // se valido vado avanti altrimenti errore
    } else if (this.stepper.currentStepLavorazione === 3) { // vado nello step 4
      this.lavorazioniEvent.setOperazione(OperazioneLavorazione.fineCreazione);
      this.getLavorazione().then(value => {
        this.getSuoloVigente().then(value => {
          this.stepper.currentStepLavorazione += 1;
          this.stepEvent.emit(this.stepper.currentStepLavorazione); // esporto lo stato del wizard
        });
      });
    }

    // else {
    // this.stepper.currentStepLavorazione += 1;
    // this.stepEvent.emit(this.stepper.currentStepLavorazione); // esporto lo stato del wizard
    // }
  }

  prevStep() {
    this.stepper.currentStepLavorazione -= 1;
    if (this.stepper.currentStepLavorazione === 2) {
      this.getSuoloVigente().then(value => {
        this.panelEvent.openPoligoniDichiarato = false;
      });
    }
    this.stepEvent.emit(this.stepper.currentStepLavorazione); // esporto lo stato del wizard
  }

  getSuoloVigente() {
    this.params = {
      'pagina': 0,
      'numeroElementiPagina': 500,
      'idLavorazione': this.lavorazioniEvent.idLavorazione
    };
    return new Promise(((resolve, reject) => {
      this.creazioneLavorazioneService.ricercaSuoloVigente(this.params)
        .subscribe(
          (results) => {
            this.totalsPoligoniSuolo = results['count'];
            this.poligoniSuolo = results['risultati'];
            resolve(true);
          },
          (error) => {
            console.log(error);
          });
    }));
  }

  validaLavorazioneAndGetLavorazionePromise() {
    return new Promise(((resolve, reject) => {
      this.validaLavorazionePromise().then(value => {
        this.getLavorazione().then(x => {
          resolve(true);
        });
      }).catch(err => {
        this.toastComponent.validazioneLavorazioneError();
        reject(false);
      });
    })
    );
  }

  validaLavorazionePromise() {

    return new Promise(((resolve, reject) => {
      try {
        this.creazioneLavorazioneService.validaLavorazione(this.lavorazioniEvent.idLavorazione).subscribe((results) => {
          if (results['message'] == null) {
            resolve(true);
          } else {
            reject(null);
          }
        });
      } catch (error) {
        reject(error);
      }
    }));
  }

  notifyLavorazioneChanged($event) {
    console.log('notifyLavorazioneChanged ', $event);
    this.lavorazioneChanged = $event;

    this.lavorazioneChangedOutput.emit($event);
  }

  getLavorazione() {
    return new Promise(((resolve, reject) => {
      this.creazioneLavorazioneService.getLavorazioneSuolo(this.lavorazioniEvent.idLavorazione).subscribe(
        (res) => {
          this.lavorazioneSuolo = res;
          if (!this.lavorazioneSuolo) {
            console.log('NON SONO IL PROPRIETARIO DELLA LAVORAZIONE');
          } else {
            this.lavorazioneSuolo.statoMappato = StatoLavorazioneSuoloDecode.decode(this.lavorazioneSuolo.stato);
            this.lavorazioniEvent.statoMappato = this.lavorazioneSuolo.statoMappato;
            this.lavorazioniEvent.stato = this.lavorazioneSuolo.stato;
            this.lavorazioniEvent.objectLavorazione = this.lavorazioneSuolo;
            resolve(this.lavorazioneSuolo);
          }
        },
        (error) => {
          console.log(error);
          reject(error);
        });
    }));
  }

  getSuoloDichiarato() {

    const params = {
      'idLavorazione': this.lavorazioniEvent.idLavorazione,
      'pagina': 0,
      'numeroElementiPagina': 500
    };
    return new Promise(((resolve, reject) => {
      this.searchService.resultsPoligoniSuoloDichiarato(params, true)
        .subscribe(
          (data) => {
            this.poligoniById = data['risultati'];
            this.totalsPoligoniDichiaratoInLavorazione = data['count'];
            resolve(true);
          },
          (error) => {
            this.toastComponent.showErrorServer500();
            console.log(error);
          });
      setTimeout(() => {
        const messageBody = document.querySelector('.for-results');
        if (messageBody) {
          messageBody.scrollTop = 0;
        }
      });
    }));
  }

  avviaLavorazione() {
    this.lavorazioneChanged = this.checkChanges();

    if (this.lavorazioneChanged.changed) {
      // tslint:disable-next-line: max-line-length
      this.creazioneLavorazioneService.putLavorazioneSuolo(this.lavorazioniEvent.idLavorazione, this.lavorazioneChanged.lavorazioneSuolo).subscribe(res => {
        if (res['status'] === 200) {
          this.lavorazioneChanged.changed = false;
          // this.lavorazioneChangedOutput.emit(this.lavorazioneChanged);
          this.creazioneLavorazioneService.avviaLavorazione(this.lavorazioniEvent.idLavorazione, this.isVerificaPascoli).subscribe((response: any) => {
            this.response = response;
            if (this.response['status'] === 200 || this.response['status'] === 201) {
              this.toastComponent.showSuccess();
              this.lavorazioniEvent.setOperazione(OperazioneLavorazione.fineCreazione);
              this.lavorazioneChanged.changed = false;
              this.lavorazioneChangedOutput.emit(this.lavorazioneChanged);
              this.isVerificaPascoli = true;
              this.nextStep();
              this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.DETTAGLIO_LAVORAZIONE;
              this.lavorazioniEvent.editaCelleWorkSpace = true;
              // Reset poligoni adl
              this.mapEvent.map.removeLayer(this.lavorazioniEvent.vectorLayerAdl);
            } else {
              this.toastComponent.showErroAvviaLavorazione();
            }
          },
            (error) => {
              if (error.status === 412) {
                this.confirmationService.confirm({
                  message: 'Nella lavorazione sono presenti suoli che intersecano lo SCHEDARIO PASCOLI, proseguire?',
                  header: 'Attenzione',
                  key: 'intersezionePascoli',
                  icon: 'pi pi-exclamation-triangle',
                  accept: () => {
                    this.isVerificaPascoli = false;
                    this.creazioneLavorazioneService.avviaLavorazione(this.lavorazioniEvent.idLavorazione, this.isVerificaPascoli).subscribe((response: any) => {
                      this.response = response;
                      if (this.response['status'] === 200 || this.response['status'] === 201) {
                        this.toastComponent.showSuccess();
                        this.lavorazioniEvent.setOperazione(OperazioneLavorazione.fineCreazione);
                        //this.lavorazioneChanged.changed = false;
                        //this.lavorazioneChangedOutput.emit(this.lavorazioneChanged);
                        this.isVerificaPascoli = true;
                        this.nextStep();
                        this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.DETTAGLIO_LAVORAZIONE;
                      } else {
                        this.toastComponent.showErroAvviaLavorazione();
                      }
                    });
                  },
                  reject: () => { }
                });
              } else {
                console.log(error);
                this.toastComponent.showError();
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
    } else {
      return new Promise((resolve, reject) => {
        this.creazioneLavorazioneService.avviaLavorazione(this.lavorazioniEvent.idLavorazione, this.isVerificaPascoli).subscribe((respone: any) => {
          this.response = respone;
          console.log(this.response);
          if (this.response['status'] === 200 || this.response['status'] === 201) {
            this.toastComponent.showSuccess();
            this.lavorazioniEvent.setOperazione(OperazioneLavorazione.fineCreazione);
            //this.lavorazioneChanged = { 'changed': false, 'lavorazioneSuolo': this.lavorazioneChanged };
            this.lavorazioneChanged.changed = false;
            this.lavorazioneChangedOutput.emit(this.lavorazioneChanged);
            this.nextStep();
            this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.DETTAGLIO_LAVORAZIONE;
            this.lavorazioniEvent.editaCelleWorkSpace = true;
            // Reset poligoni adl
            this.mapEvent.map.removeLayer(this.lavorazioniEvent.vectorLayerAdl);
          } else {
            this.toastComponent.showError();
          }
        },
          (error) => {
            if (error.status === 412) {
              this.confirmationService.confirm({
                message: 'Nella lavorazione sono presenti suoli che intersecano lo SCHEDARIO PASCOLI, proseguire?',
                header: 'Attenzione',
                key: 'intersezionePascoli',
                icon: 'pi pi-exclamation-triangle',
                accept: () => {
                  this.isVerificaPascoli = false;
                  this.creazioneLavorazioneService.avviaLavorazione(this.lavorazioniEvent.idLavorazione, this.isVerificaPascoli).subscribe((response: any) => {
                    this.response = response;
                    if (this.response['status'] === 200 || this.response['status'] === 201) {
                      this.toastComponent.showSuccess();
                      this.lavorazioniEvent.setOperazione(OperazioneLavorazione.fineCreazione);
                      //this.lavorazioneChanged.changed = false;
                      //this.lavorazioneChangedOutput.emit(this.lavorazioneChanged);
                      this.isVerificaPascoli = true;
                      this.nextStep();
                      this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.DETTAGLIO_LAVORAZIONE;
                    } else {
                      this.toastComponent.showErroAvviaLavorazione();
                    }
                  });
                },
                reject: () => { }
              });
            } else {
              console.log(error);
              this.toastComponent.showError();
            }
          });
      });
    }
  }

  riprendiLavorazione() {
    this.lavorazioneChanged = this.checkChanges();
    if (this.lavorazioneChanged.changed) {
      this.creazioneLavorazioneService.putLavorazioneSuolo(this.lavorazioniEvent.idLavorazione, this.lavorazioneChanged.lavorazioneSuolo).subscribe(res => {
        if (res['status'] === 200) {
          this.lavorazioneChanged.changed = false;
          // this.lavorazioneChangedOutput.emit(this.lavorazioneChanged);
          this.creazioneLavorazioneService.riprendiLavorazione(this.lavorazioniEvent.idLavorazione, this.isVerificaPascoli).subscribe((response: any) => {
            this.response = response;
            if (this.response['status'] === 200 || this.response['status'] === 201) {
              this.toastComponent.showSuccess();
              this.lavorazioniEvent.setOperazione(OperazioneLavorazione.fineModifica);
              //this.lavorazioneChanged = { 'changed': false, 'lavorazioneSuolo': this.lavorazioneChanged };
              this.lavorazioneChanged.changed = false;
              this.lavorazioneChangedOutput.emit(this.lavorazioneChanged);
              this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.DETTAGLIO_LAVORAZIONE;
              this.nextStep();
            }
          },
            (error) => {
              if (error.status === 412) {
                this.confirmationService.confirm({
                  message: 'Nella lavorazione sono presenti suoli che intersecano lo SCHEDARIO PASCOLI, proseguire?',
                  header: 'Attenzione',
                  key: 'intersezionePascoli',
                  icon: 'pi pi-exclamation-triangle',
                  accept: () => {
                    this.isVerificaPascoli = false;
                    this.creazioneLavorazioneService.riprendiLavorazione(this.lavorazioniEvent.idLavorazione, this.isVerificaPascoli).subscribe((response: any) => {
                      this.response = response;
                      if (this.response['status'] === 200 || this.response['status'] === 201) {
                        this.toastComponent.showSuccess();
                        this.lavorazioniEvent.setOperazione(OperazioneLavorazione.fineModifica);
                        //this.lavorazioneChanged = { 'changed': false, 'lavorazioneSuolo': this.lavorazioneChanged };
                        //this.lavorazioneChanged.changed = false;
                        this.lavorazioneChangedOutput.emit(this.lavorazioneChanged);
                        this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.DETTAGLIO_LAVORAZIONE;
                        this.isVerificaPascoli = true;
                        this.nextStep();
                      } else {
                        this.toastComponent.showErrorModificaLavorazione();
                      }
                    });
                  },
                  reject: () => { }
                });
              } else {
                console.log(error);
                this.toastComponent.showErrorModificaLavorazione();
              }
            });
        } else {
          this.toastComponent.showErrorModificaLavorazione();
        }
      },
        (error) => {
          console.log(error);
          this.toastComponent.showErrorModificaLavorazione();
        });
    } else {
      // tslint:disable-next-line: max-line-length
      // this.lavorazioneChanged.changed = false;
      this.lavorazioneChangedOutput.emit(this.lavorazioneChanged);
      this.creazioneLavorazioneService.riprendiLavorazione(this.lavorazioniEvent.idLavorazione, this.isVerificaPascoli).subscribe((response: any) => {
        this.response = response;
        if (this.response['status'] === 200 || this.response['status'] === 201) {
          this.toastComponent.showSuccess();
          this.lavorazioniEvent.setOperazione(OperazioneLavorazione.fineModifica);
          // this.lavorazioneChanged = { 'changed': false, 'lavorazioneSuolo': this.lavorazioneChanged };
          this.lavorazioneChanged.changed = false;
          this.lavorazioneChangedOutput.emit(this.lavorazioneChanged);
          this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.DETTAGLIO_LAVORAZIONE;
          this.nextStep();
        }
      },
        (error) => {
          if (error.status === 412) {
            this.confirmationService.confirm({
              message: 'Nella lavorazione sono presenti suoli che intersecano lo SCHEDARIO PASCOLI, proseguire?',
              header: 'Attenzione',
              key: 'intersezionePascoli',
              icon: 'pi pi-exclamation-triangle',
              accept: () => {
                this.isVerificaPascoli = false;
                this.creazioneLavorazioneService.riprendiLavorazione(this.lavorazioniEvent.idLavorazione, this.isVerificaPascoli).subscribe((response: any) => {
                  this.response = response;
                  if (this.response['status'] === 200 || this.response['status'] === 201) {
                    this.toastComponent.showSuccess();
                    this.lavorazioniEvent.setOperazione(OperazioneLavorazione.fineModifica);
                    // this.lavorazioneChanged = { 'changed': false, 'lavorazioneSuolo': this.lavorazioneChanged };
                    this.lavorazioneChanged.changed = false;
                    this.lavorazioneChangedOutput.emit(this.lavorazioneChanged);
                    this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.DETTAGLIO_LAVORAZIONE;
                    this.isVerificaPascoli = true;
                    this.nextStep();
                  } else {
                    this.toastComponent.showErrorModificaLavorazione();
                  }
                });
              },
              reject: () => { }
            });
          } else {
            console.log(error);
            this.toastComponent.showErrorModificaLavorazione();
          }
        });
    }
  }

  private checkChanges(): LavorazioneChangeStatus {
    if (!this.lavorazioneChanged ||
      !this.lavorazioneChanged.lavorazioneSuolo) {
      this.lavorazioneChanged = new LavorazioneChangeStatus(false, this.lavorazioneSuolo);
    }
    return this.mapService.populateZoomScaleLavorazione(this.lavorazioneChanged);
  }
}
