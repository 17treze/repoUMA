import { ModalitaADL } from "./ModalitaADL.enum";
import { StatoLavorazioneSuolo, StatoLavorazioneSuoloDecode } from "./StatoLavorazioneSuolo.enum";

export class Lavorazione {
    id: number;
    utente: string ;
    utenteAgs: string;
    stato: StatoLavorazioneSuolo;
    statoDecodificato: string;
    dataInizioLavorazione: Date;
    dataFineLavorazione: Date;
    dataUltimaModifica: Date;
    note: string;
    titolo: string;
    sopralluogo: string;
    campagna: number;
    modalitaADL: ModalitaADL;
    xUltimoZoom: number;
    yUltimoZoom: number;
    scalaUltimoZoom: number;
    readOnly: string;

    public constructor(pid: number, pStato: StatoLavorazioneSuolo, pcampagna: number) {
        this.id = pid;
        this.stato = pStato;
        this.statoDecodificato = StatoLavorazioneSuoloDecode.decode(pStato);
        // default
        this.modalitaADL = ModalitaADL.POLIGONI_INTERI;
        this.campagna = pcampagna;
    }
}

// Creato il 28/07/2022 per gestire i cambi dello zoom di una lavorazione e evitare ridondanze
// Sarebbe da utilizzare al posto di tutti i LavorazioneChanged 
export class LavorazioneChangeStatus {
    changed: boolean;
    lavorazioneSuolo: Lavorazione;

    public constructor(pchanged: boolean, plavorazioneSuolo: Lavorazione) {
        this.changed = pchanged;
        this.lavorazioneSuolo = plavorazioneSuolo;
    }

}
