import { CodiceResponsabilita } from "./CodiceResponsabilita";

export class ResponsabilitaPat {
    idResponsabilita: number;
    codResponsabilita: string;
    matricola: string;
    dirigente: string;
    dipartimento: string;
    allegato: String;
    note: string;

    constructor(idResponsabilita: number, pmatricola: string, pdirigente: string, pdipartimento: string, allegato: String, pnote: string) {
        this.idResponsabilita = idResponsabilita;
        this.codResponsabilita = CodiceResponsabilita[CodiceResponsabilita.PAT];
        this.matricola = pmatricola;
        this.dirigente = pdirigente;
        this.dipartimento = pdipartimento;
        this.allegato = allegato;
        this.note = pnote;
    }
}