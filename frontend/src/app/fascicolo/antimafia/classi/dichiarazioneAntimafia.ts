import { StatoDic } from "./statoDic";
import { Azienda } from "./azienda";
import { DatiDichiarazione, AllegatoDicFamConv } from "./datiDichiarazione";
import { Injectable } from "@angular/core";

@Injectable()
export class DichiarazioneAntimafia {
  idFascicolo: number;
  idSoggetto: number;
  azienda: Azienda;
  id?: number;
  stato: StatoDic;
  anno: number;
  datiDichiarazione: DatiDichiarazione;
  assenzaDt: boolean;
  dtUploadPdfFirmato: Date;
  pdfFirmato: any[];
  pdfFirmatoName: string;
  tipoPdfFirmato: string;
  idProtocollo: string;
  dtProtocollazione: Date;
  dtFine: Date;
  procedimenti: string[];

  constructor() { }

  setAzienda(cuaa: string) {
    this.azienda = new Azienda(null, cuaa);
  }

  setStato(identificativo: string) {
    this.stato = new StatoDic();
    this.stato.identificativo = identificativo;
  }
}
