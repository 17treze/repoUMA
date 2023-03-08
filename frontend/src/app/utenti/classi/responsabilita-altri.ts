import { CodiceResponsabilita } from "./CodiceResponsabilita";

export class ResponsabilitaAltriEnti {
    idResponsabilita: number;
    codResponsabilita: string;
    denominazione: string;
    piva: string;
    dirigente: string;
    allegato: String;
    note: string;

    constructor(idResponsabilita: number, pdenominazione: string, ppiva: string, pdirigente: string, allegato: String, pnote: string) {
        this.idResponsabilita = idResponsabilita;
        this.codResponsabilita = CodiceResponsabilita[CodiceResponsabilita.ALTRI];
        this.denominazione = pdenominazione;
        this.dirigente = pdirigente;
        this.piva = ppiva;
        this.allegato = allegato;
        this.note = pnote;
    }
}