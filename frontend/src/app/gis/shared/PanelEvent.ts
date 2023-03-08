import { Injectable } from '@angular/core';

@Injectable()
export class PanelEvent {
   comeFromRicerca: boolean;
   comeFromLavorazione: boolean;
   openPoligoniDichiarato: boolean;
   showPoligoniSuoloTable: boolean;
   nuovaLavorazione: boolean;
   showEsitoLavorazione: boolean;
   showDettaglioLavorazione: boolean;
   showSpinnerLoading: boolean;
   bufferSpinnerLoading: any[];
   showWmsResults: any;
   panelIdentifyVisible: boolean;
   showLayerSwitcher: boolean;
   showSettings: boolean;
   identifyTableContent: any = [];
   showUploadVettoriali: any;
   showmapNavigator: boolean;
   openResultsLavorazioni: boolean;
   searchRichieste: boolean;
   searchLavorazioni: boolean;
   showNoteFeatures: boolean;
   showCaricaLayerStorico: boolean;
   poligoniSuoloTableType: string;
   labelStoricoIdentify: string;
}

