export class IndirizzoDto {
    provincia: string;
    comune: string;
    localita: string;
    toponimo: string;
    cap: string;

    static toDto(inVal: any): IndirizzoDto {
        if (!inVal) {
            return null;
        }
        const dati: IndirizzoDto = new IndirizzoDto();
        dati.provincia = inVal.provincia;
        dati.comune = inVal.comune;
        dati.localita = inVal.localita;
        dati.cap = inVal.cap;
        return dati;
    }
}

export class DatiAnagraficiDto {
    nominativo: string;
    codiceFiscale: string;
    dataNascita: string;
    comuneNascita: string;
    provinciaNascita: string;
    pec: string;

    static toDto(inVal: any): DatiAnagraficiDto {
        if (!inVal) {
            return null;
        }
        const dati: DatiAnagraficiDto = new DatiAnagraficiDto();
        dati.nominativo = inVal.nominativo;
        dati.codiceFiscale = inVal.codiceFiscale;
        dati.comuneNascita = inVal.comuneNascita;
        dati.provinciaNascita = inVal.provinciaNascita;
        dati.pec = inVal.pec;
        return dati;
    }
}

export class DatiAperturaFascicoloDto {
    datiAnagraficiRappresentante: DatiAnagraficiDto;
    domicilioFiscaleRappresentante: IndirizzoDto;
    codiceFiscale: string;
    partitaIva: string;
    denominazione: string;
    naturaGiuridica: string;
    ubicazioneDitta: IndirizzoDto;
    denominazioneFascicolo: string;

    static toDto(inVal: any): DatiAperturaFascicoloDto {
        if (!inVal) {
            return null;
        }
        const dati: DatiAperturaFascicoloDto = new DatiAperturaFascicoloDto();
        dati.datiAnagraficiRappresentante = DatiAnagraficiDto.toDto(inVal.datiAnagraficiRappresentante);
        dati.domicilioFiscaleRappresentante = IndirizzoDto.toDto(inVal.domicilioFiscaleRappresentante);
        dati.codiceFiscale = inVal.codiceFiscale;
        dati.partitaIva = inVal.partitaIva;
        dati.denominazione = inVal.denominazione;
        dati.naturaGiuridica = inVal.naturaGiuridica;
        dati.ubicazioneDitta = inVal.ubicazioneDitta;
        dati.denominazioneFascicolo = inVal.denominazioneFascicolo;
        return dati;
    }
}

export interface ValidazioneFascicoloDto {
  cuaa: string;
  id: number;
  idValidazione: number;
  denominazioneImpresa: string;
  denominazioneSportello: string;
  dataValidazione: Date;
  dataModifica: Date;
  utenteValidazione: string;
}
