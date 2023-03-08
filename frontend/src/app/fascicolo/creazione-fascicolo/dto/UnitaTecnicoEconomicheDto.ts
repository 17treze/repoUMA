export class UnitaTecnicoEconomicheDto {
    identificativoUte: string;
    dataApertura: Date;
    dataCessazione: Date;
    dataDenunciaCessazione: Date;
    causaleCessazione: string;
    attivita: string;
    dataDenunciaInizioAttivita: Date;
    settoreMerceologico: string;
    telefono: string;
    indirizzoPec: string;
    toponimo: string;
    via: string;
    numeroCivico: string;
    comune: string;
    cap: string;
    codiceIstatComune: string;
    frazione: string;
    provincia: string;
    indirizzo: string;
    destinazioniUso: DestinazioniUsoDto[];
    attivitaAteco: AttivitaAtecoDto[];

    static toDto(inVal: any): UnitaTecnicoEconomicheDto {
        if (!inVal) {
            return null;
        }
        const dati: UnitaTecnicoEconomicheDto = new UnitaTecnicoEconomicheDto();
        dati.identificativoUte = inVal.identificativoUte;
        dati.dataApertura = inVal.dataApertura;
        dati.dataCessazione = inVal.dataCessazione;
        dati.dataDenunciaCessazione = inVal.dataDenunciaCessazione;
        dati.causaleCessazione = inVal.causaleCessazione;
        dati.attivita = inVal.attivita;
        dati.dataDenunciaInizioAttivita = inVal.dataDenunciaInizioAttivita;
        dati.settoreMerceologico = inVal.settoreMerceologico;
        dati.telefono = inVal.telefono;
        dati.indirizzoPec = inVal.indirizzoPec;
        dati.toponimo = inVal.toponimo;
        dati.via = inVal.via;
        dati.numeroCivico = inVal.numeroCivico;
        dati.comune = inVal.comune;
        dati.cap = inVal.cap;
        dati.codiceIstatComune = inVal.codiceIstatComune;
        dati.frazione = inVal.frazione;
        dati.provincia = inVal.provincia;
        dati.indirizzo = inVal.indirizzo;
        dati.destinazioniUso = DestinazioniUsoDto.toDtos(inVal.destinazioniUso);
        dati.attivitaAteco = AttivitaAtecoDto.toDtos(inVal.attivitaAteco);
        return dati;
    }
}

export class DestinazioniUsoDto {
    descrizione: string;

    static toDtos(inVal: any[]): DestinazioniUsoDto[] {
        if (!inVal) return null;
        let destinazioniUso: DestinazioniUsoDto[] = [];
        for (let inc of inVal) {
            if (!inc) continue;
            let destinazione: DestinazioniUsoDto = DestinazioniUsoDto.toDto(inc);
            destinazioniUso.push(destinazione);
        }
        return destinazioniUso;
    }

    static toDto(inc: any): DestinazioniUsoDto {
        if (!inc) return null;
        let dati: DestinazioniUsoDto = new DestinazioniUsoDto();
        dati.descrizione = inc.descrizione;
        return dati;
    }
}

export class AttivitaAtecoDto {
    codice: string;
    descrizione: string;
    importanza: string;
    fonte: string;

    static toDtos(inVal: any[]): AttivitaAtecoDto[] {
        if (!inVal) return null;
        let attivitaAteco: AttivitaAtecoDto[] = [];
        for (let inc of inVal) {
            if (!inc) continue;
            let attivita: AttivitaAtecoDto = AttivitaAtecoDto.toDto(inc);
            attivitaAteco.push(attivita);
        }
        return attivitaAteco;
    }

    static toDto(inc: any): AttivitaAtecoDto {
        if (!inc) return null;
        let dati: AttivitaAtecoDto = new AttivitaAtecoDto();
        dati.codice = inc.codice;
        dati.descrizione = inc.descrizione;
        dati.importanza = inc.importanza;
        dati.fonte = inc.fonte;
        return dati;
    }
}
