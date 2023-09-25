import { Indirizzo, SportelloCAA } from "src/app/fascicolo/creazione-fascicolo/dto/DatiSportelloCAA";

export class FascicoloDaCuaa { /** Fascicolo DTO */
  id: number;
  cuaa: string;
  denominazione: string;
  stato: string;
  mandatoDto: MandatoDto;
  organismoPagatore: string;
  dataApertura: Date;
  dataValidazione: Date;
  dataModifica: Date;
  dataUltimaValidazione: Date;
  numeScheVali: string;
  idSchedaValidazione: number;
  numeIscrRea: string;
  numeIscrRegiImpr: string;
  comuniCapofila: string;
 
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
    dati.dataUltimaValidazione = inc.dataUltimaValidazione;
    dati.mandatoDto = MandatoDto.toDto(inc.mandatoDto);
    dati.idSchedaValidazione = inc.idSchedaValidazione;
    dati.organismoPagatore = inc.organismoPagatore;
    return dati;
  }
}

export class MandatoDto {
  id: number;
  codice: string;
  denominazione: string;
  sportello: SportelloCAA;
  dataInizio: Date;
  dataFine: Date;
  attoRiconoscimento: string;
  codiceFiscale: string;
  dataSottoscrizione: Date;
  partitaIva: string;
  societaServizi: string;
  indirizzoSedeAmministrativa: Indirizzo;

  static toDto(inc: any): MandatoDto {
    if (!inc) return null;
    const dati: MandatoDto = new MandatoDto();
    dati.id = inc.id;
    dati.codice = inc.codice;
    dati.denominazione = inc.denominazione;
    dati.sportello = SportelloCAA.toDto(inc.sportello);
    dati.dataInizio = inc.dataInizio ? inc.dataInizio : null;
    dati.dataFine = inc.dataFine ? inc.dataFine : null;
    dati.attoRiconoscimento = inc.attoRiconoscimento;
    dati.codiceFiscale = inc.codiceFiscale;
    dati.dataSottoscrizione = inc.dataSottoscrizione ? inc.dataSottoscrizione : null;
    dati.partitaIva = inc.partitaIva;
    dati.societaServizi = inc.societaServizi;
    dati.indirizzoSedeAmministrativa = Indirizzo.toDto(inc.indirizzoSedeAmministrativa);
    return dati;
  }
}
