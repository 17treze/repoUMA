import { CodiceResponsabilita } from "./CodiceResponsabilita";

export class ResponsabilitaConsulente {
    idResponsabilita: number;
    codResponsabilita: string;
    ordine: string;
    iscrizione: string;
    cuaa: string;
    denominazione: string;
    rappresentante: string;
    allegato: String;

    constructor(idResponsabilita: number, pordine: string, piscrizione: string, pcuaa: string, pdenominazione: string, prappresentante: string, allegato: String) {
        this.idResponsabilita = idResponsabilita;
        this.codResponsabilita = CodiceResponsabilita[CodiceResponsabilita.LPC];
        this.ordine = pordine;
        this.iscrizione = piscrizione;
        this.cuaa = pcuaa;
        this.denominazione = pdenominazione;
        this.rappresentante = prappresentante;
        this.allegato = allegato;
    }
}