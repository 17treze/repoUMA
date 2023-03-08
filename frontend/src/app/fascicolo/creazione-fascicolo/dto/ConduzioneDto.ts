export class ConduzioneDto {
    superficieCondotta: string;
    codiceTipoConduzione: string;
    descrizioneTipoConduzione: string;
    dataInizioConduzione: string;
    dataFineConduzione: string;
    codiceFiscaleProprietarioList: string[];
    datiParticella: ParticellaDto;
    caratteristicheZona: CaratteristicheZonaDto;
    documentiConduzione: DocumentiConduzioneDto[];

    static toDtos(incs: any): ConduzioneDto[] {
        if (!incs) {
            return null;
        }
        const list: ConduzioneDto[] = [];
        for (const inc of incs) {
            if (!inc) {
                continue;
            }
            list.push(ConduzioneDto.toDto(inc));
        }
        return list;
    }

    static toDto(inc: any): ConduzioneDto {
        if (!inc) {
            return null;
        }
        const dati: ConduzioneDto = new ConduzioneDto();
        dati.superficieCondotta = inc.superficieCondotta;
        dati.codiceTipoConduzione = inc.codiceTipoConduzione;
        dati.descrizioneTipoConduzione = inc.descrizioneTipoConduzione;
        dati.dataInizioConduzione = inc.dataInizioConduzione;
        dati.dataFineConduzione = inc.dataFineConduzione;
        dati.codiceFiscaleProprietarioList = inc.codiceFiscaleProprietarioList;
        dati.datiParticella = ParticellaDto.toDto(inc.datiParticella);
        dati.caratteristicheZona = CaratteristicheZonaDto.toDto(inc.caratteristicheZona);
        dati.documentiConduzione = DocumentiConduzioneDto.toDtos(inc.documentiConduzione);
        return dati;
    }
}

export class ParticellaDto {
    provincia: string;
    comune: string;
    sezione: string;
    foglio: string;
    particella: string;
    subalterno: string;

    static toDto(inc: any): ParticellaDto {
        if (!inc) {
            return null;
        }
        const dati: ParticellaDto = new ParticellaDto();
        dati.provincia = inc.provincia;
        dati.comune = inc.comune;
        dati.sezione = inc.sezione;
        dati.foglio = inc.foglio;
        dati.particella = inc.particella;
        dati.subalterno = inc.subalterno;
        return dati;
    }
}

export class CaratteristicheZonaDto {
    casiParticolari: string;
    casiParticolariDescrizione: string;
    flagGiust: string;
    flagGiustDescrizione: string;
    codiZVN: string;
    codiZVNDescrizione: string;

    static toDto(inc: any): CaratteristicheZonaDto {
        if (!inc) {
            return null;
        }
        const dati: CaratteristicheZonaDto = new CaratteristicheZonaDto();
        dati.casiParticolari = inc.casiParticolari;
        dati.casiParticolariDescrizione = inc.casiParticolariDescrizione;
        dati.flagGiust = inc.flagGiust;
        dati.flagGiustDescrizione = inc.flagGiustDescrizione;
        dati.codiZVN = inc.codiZVN;
        dati.codiZVNDescrizione = inc.codiZVNDescrizione;
        return dati;
    }
}

export class DocumentiConduzioneDto {
    dataRilascio: string;
    dataScadenza: string;
    numeroDocumento: string;
    tipoDocumento: string;

    static toDtos(incs: any): DocumentiConduzioneDto[] {
        if (!incs) {
            return null;
        }
        const list: DocumentiConduzioneDto[] = [];
        for (const inc of incs) {
            if (!inc) {
                continue;
            }
            list.push(DocumentiConduzioneDto.toDto(inc));
        }
        return list;
    }

    static toDto(inc: any): DocumentiConduzioneDto {
        if (!inc) {
            return null;
        }
        const dati: DocumentiConduzioneDto = new DocumentiConduzioneDto();
        dati.dataRilascio = inc.dataRilascio;
        dati.dataScadenza = inc.dataScadenza;
        dati.numeroDocumento = inc.numeroDocumento;
        dati.tipoDocumento = inc.tipoDocumento;
        return dati;
    }
}
