import { Injectable } from '@angular/core';
import { AziendaAgricola } from '../../models/searchgis/search-results.model';
@Injectable()
export class PoligoniDichiaratiEvent {
   poligoni: PoligonoObj[];
   count: number;
   scrollCount = 10;
   pagina = 0;
   showDialog = false;
   idDettaglioRichiesta: number;
   statoRichiesta: any;
   poligoniModificati = [];
   statoLavorabile: boolean;
}

export class PoligonoObj {
   id: number;
   idRichiesta: number;
   statoRichiesta: string | null;
   aziendaAgricola: AziendaAgricola;
   campagna: number;
   codSezione: string | null;
   tipoSuoloDichiarato: string | null;
   tipoSuoloRilevato: string | null;
   areaOri: number;
   codiRileDichiarato: string | null;
   descRileDichiarato: string | null;
   codiProdRileDichiarato: string | null;
   descProdRileDichiarato: string | null;
   codiRilePrevalenteDich: string | null;
   descRilePrevalenteDich: string | null;
   codiRileRilevato: string | null;
   descRileRilevato: string | null;
   codiProdRileRilevato: string | null;
   descProdRileRilevato: string | null;
   codiRilePrevalenteRil: string | null;
   descRilePrevalenteRil: string | null;
   esito: string | null;
   idLavorazione: number;
   idPoligonoDichiarato: number;
   numeroMessaggi: number;
   numeroDocumenti: number;
   visible: boolean;
   extent: number[];
   utenteLavorazione: string | null;
   statoLavorazione: string | null;
   nuovaModificaBo: boolean;
   nuovaModificaCaa: boolean;
   visibileInOrtofoto: boolean;
   tipoInterventoColturale: string | null;
   interventoInizio: any;
   interventoFine: any;
   vincoloAttributi: boolean;
}

