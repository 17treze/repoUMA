export class AnagraficaDto {
    nome: string;
    cognome: string;
    sesso: string;
    dataNascita: string;
    comuneNascita: string;
    provinciaNascita: string;
}

export class IndirizzoDto {
    cap: string;
    civico: string;
    codiceIstat: string;
    comune: string;
    denominazioneEstesa: string;
    frazione: string;
    provincia: string;
    toponimo: string;
    via: string;
}

export enum SezioneEnum {
    ORDINARIA = "ORDINARIA",
    SPECIALE = "SPECIALE"
}

export class IscrizioneSezioneDto {
    sezione: SezioneEnum;
    dataIscrizione: Date;
    qualifica: string;
    coltivatoreDiretto: string;
}

export class PersonaDto {
    codiceFiscale: string;
    iscrizioniSezione: IscrizioneSezioneDto[];
}

export enum ImportanzaAttivitaEnum {
    PRIMARIO_IMPRESA,
    PRIMARIO,
    SECONDARIO
}

export enum FonteDatoAnagrafico {
    ANAGRAFE_TRIBUTARIA = "ANAGRAFE_TRIBUTARIA",
    // PARIX
    CAMERA_COMMERCIO = "CAMERA_COMMERCIO"
}

export class AttivitaAteco {
    codice: string;
    descrizione: string;
    importanza: 'PRIMARIO_IMPRESA' | 'PRIMARIO' | 'SECONDARIO';
    fonteDato: FonteDatoAnagrafico;
    peso: number; /* 0=PRIMARIO_IMPRESA, 1=PRIMARIO, 2=SECONDARIO */
}

export class IscrizioneRepertorioEconomicoDto {
    numero: string;
    dataIscrizione: Date;
    codiceRea: string;
    provinciaRea: string;
    cessata: boolean;
}

export class SedeDto {
    indirizzo: IndirizzoDto;
    indirizzoCameraCommercio: IndirizzoDto;
    attivitaAteco: AttivitaAteco[];
    iscrizioneRegistroImprese: IscrizioneRepertorioEconomicoDto;
    telefono: string;
    indirizzoPec: string;
}

export class ImpresaIndividualeDto {
    partitaIva: string;
    denominazione: string;
    sedeLegale: SedeDto;
    formaGiuridica: string;
    oggettoSociale: string;
}

export class PersonaFisicaDto extends PersonaDto {
    anagrafica: AnagraficaDto;
    domicilioFiscale: IndirizzoDto;
    impresaIndividuale: ImpresaIndividualeDto;
    sesso: string;
}

export class RappresentanteLegaleDto {
    codiceFiscale: string;
    nominativo: string;
}

export class PersonaGiuridicaDto extends PersonaDto {
    capitaleSocialeDeliberato: number;
    dataCostituzione: Date;
    dataTermine: Date;
    denominazione: string;
    formaGiuridica: string;
    oggettoSociale: string;
    partitaIva: string;
    rappresentanteLegale: RappresentanteLegaleDto;
    sedeLegale: SedeDto;
}

export class CaricaDto extends PersonaDto {
    descrizione: string;
    dataInizio: Date;
    identificativo: string;
    firmatario: boolean;
}

export enum VerificaCodiceFiscaleEnum {
    CORRETTO = 'CORRETTO',
    NON_TROVATO_IN_AT = 'NON_TROVATO_IN_AT',
    PARIX_DIVERSO_DA_AT = 'PARIX_DIVERSO_DA_AT'
}

export class PersonaFisicaConCaricaDto extends PersonaDto {
    codiceFiscale: string;
    nome: string;
    cognome: string;
    cariche: CaricaDto[];
    comuneNascita: string;
    siglaProvinciaNascita: string;
    dataNascita: Date;
    domicilioFiscale: IndirizzoDto;
    sesso: string;
    verificaCodiceFiscale: VerificaCodiceFiscaleEnum;
}

export class PersonaGiuridicaConCaricaDto extends PersonaDto {
    codiceFiscale: string;
    denominazione: string;
    partitaIVA: string;
    cariche: CaricaDto[];
}
