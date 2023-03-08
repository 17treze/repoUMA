import { VectorLayer } from 'ol/layer/Vector';
import { Feature } from 'ol/Feature';
import { LayerGisService } from 'src/app/gis/services/layer-gis.service';
import { HeaderLavorazione } from './HeaderLavorazione';
import { Injectable } from '@angular/core';
import { MapService } from '../components/mappa/map.service';
import { StatoLavorazioneSuolo } from './StatoLavorazioneSuolo.enum';
import { ToolBarService } from './ToolBar/toolBar.service';
import { enumTool } from '../components/mappa/gisTools/enumTools';
import { OperazioneLavorazione } from './OperazioneLavorazione.enum';
import { PropertyLayer } from "./PropertyLayer.enum";
import { ModalitaADL } from './ModalitaADL.enum';
import { Lavorazione } from './Lavorazione';
import { PoligoniSuoloDaAdl } from '../models/poligoniSuolo/poligoniSuoloDaAdl.model';
import Point from 'ol/geom/Point';

@Injectable()
export class LavorazioniEvent {
   listaLavorazioni: any;
   objectLavorazione: Lavorazione | null;
   poligoniSuoloOccupati: any;
   searchParams: any;
   titoloLavorazione: any;
   noteLavorazione: any;
   editaCelleWorkSpace = false;
   operazione: string;
   featureAdl: Feature;
   vectorLayerAdl: VectorLayer;
   activeDrawAdl: boolean;
   activeModifyAdl: boolean;
   readOnly: boolean;
   calcoloAdlActived: boolean;
   poligoniDaAdl: PoligoniSuoloDaAdl[];
   totalsPoligoniDaAdl = 0;
   stackAdl: boolean;
   modalitaAdlTemp: ModalitaADL | undefined;
   private _stato: any;
   public get stato(): any {
      return this._stato;
   }
   public set stato(value: any) {
      const isOldEdit = this.isEditabile;
      this._stato = value;

      if (this.isEditabile !== isOldEdit) {
         this.toolBarService.rimuoviListaInteraction(this.toolBarService.editToolGroup, true, true);
      }
   }
   public statoMappato: any;

   headerLavorazione: HeaderLavorazione;
   private lavorazioneEditabile = false;

   protected _idLavorazione: any;
   public get idLavorazione() {
      return this._idLavorazione;
   }

   public get isEditabile(): boolean {
      return (this.idLavorazione ?? false) && (this.stato ?? false) && this.stato === StatoLavorazioneSuolo.IN_CORSO;
   }

   public getidLavorazione(): number {
      return this._idLavorazione;
   }

   // non usare
   public set idLavorazione(idLavorazione: any) {
      this.setNuovaLavorazione(idLavorazione, StatoLavorazioneSuolo.IN_CREAZIONE, this.mapService.annoCampagna, true);
   }

   constructor(private mapService: MapService, private toolBarService: ToolBarService, private layerService: LayerGisService) { }

   public resetLavorazione() {
      if (this._idLavorazione) {
         this.stato = null;
         this._idLavorazione = null;
         this.objectLavorazione = null;
         this.stato = '';
         this.statoMappato = '';
         this.titoloLavorazione = '';
         this.noteLavorazione = '';
         this.mapService.ricaricaLavorazioneLayers(null, false, false);
      }
   }

   public setLavorazione(objLavorazione: Lavorazione, resetZoom: boolean, forceReload: boolean = false) {
      const idLavorazione = objLavorazione.id;
      console.log('setLavorazione ' + idLavorazione + ' resetZoom ' + resetZoom, this._idLavorazione);

      if (idLavorazione) {
         const toolSelect = this.toolBarService.getInteractionFomName(enumTool.selectFeatures);
         if (toolSelect) {
            this.toolBarService.getInteractionFomName(enumTool.selectFeatures).setActive(false);
         }

         const oldId = this._idLavorazione;
         this._idLavorazione = idLavorazione;
         this.objectLavorazione = objLavorazione;

         if (oldId && this._idLavorazione === oldId) {
            this.controllaEditabilitaLavorazione(resetZoom, false);
         } else {
            if (this.objectLavorazione &&
               this.objectLavorazione.xUltimoZoom &&
               this.objectLavorazione.yUltimoZoom &&
               this.objectLavorazione.scalaUltimoZoom) {
               this.controllaEditabilitaLavorazione(false, true);
               const pointView = new Point([this.objectLavorazione.xUltimoZoom, this.objectLavorazione.yUltimoZoom]);
               this.mapService.mapEvent.map.getView().fit(pointView,
                  {
                     minResolution: MapService.getResolutionForScale(this.mapService.mapEvent.map,
                        this.objectLavorazione.scalaUltimoZoom)
                  });
            } else {
               this.controllaEditabilitaLavorazione(resetZoom, true);
            }
         }
         // Ottiene la lista dei layers con proprietà CONTENUTO_INFORMATIVO = CONTENUTO_INFORMATIVO_SUOLO_OCCUPATO
         const layers = this.mapService.getLayerFromContenutoInformativo(PropertyLayer.CONTENUTO_INFORMATIVO_SUOLO_OCCUPATO);
         // aggiunge un filtro cql al layer dei suoli prenotati
         layers.forEach(l => {
            const params = l.getSource().getParams();
            params['CQL_FILTER'] = ' ID_LAV_IN_CORSO <> ' + this._idLavorazione;
            l.getSource().updateParams(params);
         });

      } else {
         // toglie la lavorazione aperta dal filtro cql
         this.resetCql();
         this.resetLavorazione();
      }
   }

   public setNuovaLavorazione(idLavorazione: any, stato: StatoLavorazioneSuolo, campagna: number, resetZoom: boolean, forceReload: boolean = false) {
      const nuovaLavorazione = new Lavorazione(idLavorazione, stato, campagna);
      this.setLavorazione(nuovaLavorazione, resetZoom, forceReload);
   }

   private resetCql() {
      const layers = this.mapService.getLayerFromContenutoInformativo(PropertyLayer.CONTENUTO_INFORMATIVO_SUOLO_OCCUPATO);

      layers.forEach(l => {
         const params = l.getSource().getParams();
         params['CQL_FILTER'] = null;
         l.getSource().updateParams(params);
      });
   }

   public controllaEditabilitaLavorazione(resetZoom: boolean, forceReload: boolean = false) {
      if (this.lavorazioneEditabile !== this.isEditabile || forceReload) {
         console.log('controllaEditabilitaLavorazione cambiato');
         this.lavorazioneEditabile = this.isEditabile;
         return this.mapService.ricaricaLavorazioneLayers(this.idLavorazione, this.isEditabile, resetZoom);
      }
   }

   public ricaricaLavorazioneLayers(): Promise<unknown> {
      return this.mapService.ricaricaLavorazioneLayers(this.idLavorazione, this.isEditabile, false);
   }

   public refreshWmsLayer(codici: string[]) {
      this.mapService.refreshWmsLayer(codici);
   }

   public setOperazione(operazione: string) {
      if (operazione === OperazioneLavorazione.fineValidazione) {
         // Fine Validazione
         this.layerService.toggleLayerSingolo(PropertyLayer.CODICE_LAYER_ANOMALIE_VALIDAZIONE_BO, true);
      } else if (operazione === OperazioneLavorazione.fineCreazione) {
         // Fine Creazione
         this.layerService.toggleLayerSingolo(PropertyLayer.CODICE_LAYER_SUOLO_PRENOTATO_LAV_BO, false);
         this.layerService.toggleLayerSingolo(PropertyLayer.CODICE_LAYER_CLIP_SU_ADL, false);
      } else if (operazione === OperazioneLavorazione.inizioCreazione) {
         // Inizio Creazione
         this.layerService.toggleLayerSingolo(PropertyLayer.CODICE_LAYER_SUOLO_PRENOTATO_LAV_BO, true);
         this.layerService.toggleLayerSingolo(PropertyLayer.CODICE_LAYER_CLIP_SU_ADL, true);
      } else if (operazione === OperazioneLavorazione.inizioModifica) {
         // Inizio Modifica
         this.layerService.toggleLayerSingolo(PropertyLayer.CODICE_LAYER_SUOLO_PRENOTATO_LAV_BO, true);
         this.layerService.toggleLayerSingolo(PropertyLayer.CODICE_LAYER_CLIP_SU_ADL, true);
      } else if (operazione === OperazioneLavorazione.fineModifica) {
         // Fine Modifica
         this.layerService.toggleLayerSingolo(PropertyLayer.CODICE_LAYER_SUOLO_PRENOTATO_LAV_BO, false);
         this.layerService.toggleLayerSingolo(PropertyLayer.CODICE_LAYER_CLIP_SU_ADL, false);
         // Inizio in Corso
         if (this.vectorLayerAdl) {
            this.vectorLayerAdl.set(PropertyLayer.VISIBLE, false);
         }
      } else if (operazione === OperazioneLavorazione.chiudi) {
         // Chiusura lavorazione
         if (this.stato === StatoLavorazioneSuolo.IN_CREAZIONE || this.stato === StatoLavorazioneSuolo.IN_MODIFICA) {
            this.layerService.toggleLayerSingolo(PropertyLayer.CODICE_LAYER_SUOLO_PRENOTATO_LAV_BO, false);
            this.layerService.toggleLayerSingolo(PropertyLayer.CODICE_LAYER_CLIP_SU_ADL, false);
         }
         this.resetCql();
      }
   }

   public getOperazione() {
      return this.operazione;
   }


}
