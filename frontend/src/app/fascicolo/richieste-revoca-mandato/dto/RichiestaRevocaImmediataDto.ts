export class RichiestaRevocaImmediataDto {
    id: number;
    cuaa: string;
    codiceFiscaleRappresentante: string;
    sedeSportelloCaa: string;
    localitaSportelloCaa: string;
    motivazioneRifiuto: string;
    causaRichiesta: string;
    dataValutazione: Date;
    dataSottoscrizione: Date;
    esito: string;
    denominazioneAzienda: string;
    denominazioneSedeAmministrativaCaa: string;
    denominazioneSportelloCaa: string;
    idProtocollo: string;

    static toDto(inc: any): RichiestaRevocaImmediataDto {
        if (!inc) {
            return null;
        }
        const dati: RichiestaRevocaImmediataDto = new RichiestaRevocaImmediataDto();
        dati.id = inc.id ? inc.id : null;
        dati.cuaa = inc.cuaa ? inc.cuaa : null;
        dati.codiceFiscaleRappresentante = inc.codiceFiscaleRappresentante ? inc.codiceFiscaleRappresentante : null;
        dati.sedeSportelloCaa = inc.sedeSportelloCaa ? inc.sedeSportelloCaa : null;
        dati.localitaSportelloCaa = inc.localitaSportelloCaa ? inc.localitaSportelloCaa : null;
        dati.motivazioneRifiuto = inc.motivazioneRifiuto ? inc.motivazioneRifiuto : null;
        dati.causaRichiesta = inc.causaRichiesta ? inc.causaRichiesta : null;
        dati.dataValutazione = inc.dataValutazione ? inc.dataValutazione : null;
        dati.dataSottoscrizione = inc.dataSottoscrizione ? inc.dataSottoscrizione : null;
        dati.esito = inc.esito ? inc.esito : null;
        dati.denominazioneAzienda = inc.denominazioneAzienda ? inc.denominazioneAzienda : null;
        dati.denominazioneSedeAmministrativaCaa = inc.denominazioneSedeAmministrativaCaa ? inc.denominazioneSedeAmministrativaCaa : null;
        dati.denominazioneSportelloCaa = inc.denominazioneSportelloCaa ? inc.denominazioneSportelloCaa : null;
        dati.idProtocollo = inc.idProtocollo ? inc.idProtocollo : null;
        return dati;
    }
}

export class RichiesteRevocaImmediata {
    count: number;
    risultati: Array<RichiestaRevocaImmediataDto>;
}

export enum StatoRichiestaRevocaImmediata {
    DA_VALUTARE = 'DA_VALUTARE',
    ACCETTATA = 'ACCETTATA',
    RIFIUTATA = 'RIFIUTATA'
}
