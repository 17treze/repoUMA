export class Firmatario {
    codiceFiscale: string;
    tipoDocumento: TipoDocumentoIdentita;
    numeroDocumento: string;
    dataRilascio: Date;
    dataScadenza: Date;
    documento: File;
}

export class DocumentoIdentitaDto {
    codiceFiscale: string;
    numeroDocumento: string;
    tipoDocumento: TipoDocumentoIdentita;
    dataRilascio: Date;
    dataScadenza: Date;
    documento: string;

    static toDto(inVal: any): DocumentoIdentitaDto {
        if (!inVal) {
            return null;
        }
        const dati: DocumentoIdentitaDto = new DocumentoIdentitaDto();
        dati.codiceFiscale = inVal.codiceFiscale;
        dati.numeroDocumento = inVal.numeroDocumento;
        dati.tipoDocumento = inVal.tipoDocumento;
        dati.dataRilascio = inVal.dataRilascio;
        dati.dataScadenza = inVal.dataScadenza;
        dati.documento = inVal.documento;
        return dati;
    }
}

export enum TipoDocumentoIdentita {
    CARTA_IDENTITA = 'Carta di identità',
    PATENTE = 'Patente',
    PASSAPORTO = 'Passaporto',
    TESSERA_FERROVIE = 'Tessera delle ferrovie dello stato',
    TESSERA_POSTALE = 'Tessera postale',
    LIBRETTO_POSTALE = 'Libretto postale',
    ALTRO = 'Altro documento'
}
