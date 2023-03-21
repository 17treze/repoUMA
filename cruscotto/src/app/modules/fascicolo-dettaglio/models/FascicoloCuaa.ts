import { Indirizzo, SportelloCAA } from "./DatiSportelloCAA";

export enum TipoDetenzioneEnum {
  MANDATO = 'MANDATO',
  DETENZIONE_IN_PROPRIO = 'DETENZIONE_IN_PROPRIO'
}

export class FascicoloDaCuaa { /** Fascicolo DTO */
  id: number;
  cuaa: string;
  denominazione: string;
  stato: string;
  detenzioneDto: DetenzioneDto;
  organismoPagatore: string;
  dataApertura: Date;
  dataValidazione: Date;
  dataModifica: Date;
  tipoDetenzione: TipoDetenzioneEnum;

  static toDto(inc: any): FascicoloDaCuaa {
    if (!inc) return null;
    const dati: FascicoloDaCuaa = new FascicoloDaCuaa();
    dati.id = inc.id;
    dati.cuaa = inc.cuaa;
    dati.denominazione = inc.denominazione;
    dati.stato = inc.stato;
    dati.dataApertura = inc.dataApertura;
    dati.dataValidazione = inc.dataValidazione;
    dati.dataModifica = inc.dataModifica;
    if (inc.tipoDetenzione === TipoDetenzioneEnum.MANDATO) {
      dati.detenzioneDto = MandatoDto.toDto(inc.mandatoDto);
    } else if (inc.tipoDetenzione === TipoDetenzioneEnum.DETENZIONE_IN_PROPRIO) {
      dati.detenzioneDto = DetenzioneInProprioDto.toDto(inc.mandatoDto);
    }
    dati.organismoPagatore = inc.organismoPagatore;
    return dati;
  }
}

export class DetenzioneDto {
  id: number;
  dataInizio: Date;
  dataFine: Date;
  codiceFiscale: string;
  partitaIva: string;
  tipoDetenzione: TipoDetenzioneEnum;

  static toDtoAux(dati: DetenzioneDto, inc: any): DetenzioneDto {
    if (!inc) { return null; }
    dati.id = inc.id;
    dati.dataInizio = inc.dataInizio ? inc.dataInizio : null;
    dati.dataFine = inc.dataFine ? inc.dataFine : null;
    dati.codiceFiscale = inc.codiceFiscale;
    dati.partitaIva = inc.partitaIva ? inc.partitaIva : null;
    dati.tipoDetenzione = inc.tipoDetenzione;
    return dati;
  }

  static toDto(inc: any): DetenzioneDto {
    const dati: DetenzioneDto = new DetenzioneDto();
    return this.toDtoAux(dati, inc);
  }
}

export class DetenzioneInProprioDto extends DetenzioneDto {

  static toDto(inc: any): DetenzioneInProprioDto {
    return super.toDtoAux(new DetenzioneInProprioDto(), inc);
  }
}

export class MandatoDto extends DetenzioneDto {
  codice: string;
  sportello: SportelloCAA;
  denominazione: string;
  attoRiconoscimento: string;
  dataSottoscrizione: Date;
  societaServizi: string;
  indirizzoSedeAmministrativa: Indirizzo;

  static toDto(inc: any): MandatoDto {
    const dati: MandatoDto = new MandatoDto();
    super.toDtoAux(dati, inc);
    dati.codice = inc.codice ? inc.codice : null;
    dati.sportello = inc.sportello ? SportelloCAA.toDto(inc.sportello) : null;
    dati.denominazione = inc.denominazione;
    dati.attoRiconoscimento = inc.attoRiconoscimento ? inc.attoRiconoscimento : null;
    dati.dataSottoscrizione = inc.dataSottoscrizione ? inc.dataSottoscrizione : null;
    dati.societaServizi = inc.societaServizi ? inc.societaServizi : null;
    dati.indirizzoSedeAmministrativa = inc.indirizzoSedeAmministrativa ? Indirizzo.toDto(inc.indirizzoSedeAmministrativa) : null;
    return dati;
  }
}

export enum StatoFascicoloEnum {
  IN_AGGIORNAMENTO = 'IN_AGGIORNAMENTO',
  ALLA_FIRMA_AZIENDA = 'ALLA_FIRMA_AZIENDA',
  VALIDATO = 'VALIDATO',
  IN_VALIDAZIONE = 'IN_VALIDAZIONE',
  ALLA_FIRMA_CAA = 'ALLA_FIRMA_CAA',
  FIRMATO_CAA = 'FIRMATO_CAA',
  IN_ATTESA_TRASFERIMENTO = 'IN_ATTESA_TRASFERIMENTO'
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

export class FascicoloCreationResultDto {
  static toDto(res: FascicoloCreationResultDto): any {
      var f = new FascicoloCreationResultDto();
      f.fascicoloDto = FascicoloDaCuaa.toDto(res.fascicoloDto)
      f.anomalies = res.anomalies;
      return f;
  }
  anomalies: string[];
  fascicoloDto: FascicoloDaCuaa;
}

