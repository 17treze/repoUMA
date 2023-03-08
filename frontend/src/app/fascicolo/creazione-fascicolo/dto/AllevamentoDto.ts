export class AllevamentoDto {
    identificativoFiscale: string;
    identificativo: number;
    specie: string;
    denominazione: string;
    tipoProduzione: string;
    orientamentoProduttivo: string;
    autorizzazioneSanitariaLatte: string;
    dtAperturaAllevamento: Date;
    dtChiusuraAllevamento: Date;
    cfProprietario: string;
    denominazioneProprietario: string;
    cfDetentore: string;
    denominazioneDetentore: string;
    dtInizioDetenzione: Date;
    dtFineDetenzione: Date;
    soccida: string;
    capiTotali: number;
    dataCalcoloCapi: Date;
    tipologiaAllevamento: string;
    strutturaAllevamentoDto: StrutturaDto;

    static toDto(inVal: any): AllevamentoDto {
        if (!inVal) {
            return null;
        }
        const dati: AllevamentoDto = new AllevamentoDto();
        dati.identificativoFiscale = inVal.identificativoFiscale;
        dati.identificativo = inVal.identificativo;
        dati.specie = inVal.specie;
        dati.denominazione = inVal.denominazione;
        dati.tipoProduzione = inVal.tipoProduzione;
        dati.orientamentoProduttivo = inVal.orientamentoProduttivo;
        dati.autorizzazioneSanitariaLatte = inVal.autorizzazioneSanitariaLatte;
        dati.dtAperturaAllevamento = inVal.dtAperturaAllevamento;
        dati.dtChiusuraAllevamento = inVal.dtChiusuraAllevamento;
        dati.cfProprietario = inVal.cfProprietario;
        dati.denominazioneProprietario = inVal.denominazioneProprietario;
        dati.cfDetentore = inVal.cfDetentore;
        dati.denominazioneDetentore = inVal.denominazioneDetentore;
        dati.dtInizioDetenzione = inVal.dtInizioDetenzione;
        dati.dtFineDetenzione = inVal.dtFineDetenzione;
        dati.soccida = inVal.soccida;
        dati.capiTotali = inVal.capiTotali;
        dati.dataCalcoloCapi = inVal.dataCalcoloCapi;
        dati.tipologiaAllevamento = inVal.tipologiaAllevamento;
        dati.strutturaAllevamentoDto = StrutturaDto.toDto(inVal.struttura);
        return dati;
    }
}

export class StrutturaDto {
    identificativo: string;
    indirizzo: string;
    cap: string;
    localita: string;
    comune: string;
    latitudine: number;
    longitudine: number;
    foglioCatastale: string;
    sezione: string;
    particella: string;
    subalterno: string;

    static toDto(inVal: any): StrutturaDto {
        if (!inVal) {
            return null;
        }
        const dati: StrutturaDto = new StrutturaDto();
        dati.identificativo = inVal.identificativo;
        dati.indirizzo = inVal.indirizzo;
        dati.cap = inVal.cap;
        dati.localita = inVal.localita;
        dati.comune = inVal.comune;
        dati.latitudine = inVal.latitudine;
        dati.longitudine = inVal.longitudine;
        dati.foglioCatastale = inVal.foglioCatastale;
        dati.sezione = inVal.sezione;
        dati.particella = inVal.particella;
        dati.subalterno = inVal.subalterno;
        return dati;
    }
}
