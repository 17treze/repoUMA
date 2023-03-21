export class DichiarazioneAssociativaDto {
  id: number;
  organizzazione: OrganizzazioneDto;
  dataInizioAssociazione: Date;
  dataInserimentoAssociazione: Date;
  utenteInserimento: string;
  dataFineAssociazione: Date;
  dataCancellazioneAssociazione: Date;
  utenteCancellazione: string;

  static toDto(incs: DichiarazioneAssociativaDto[]): DichiarazioneAssociativaDto[] {
    if (!incs) {
      return null;
    }
    const organizzazioni: DichiarazioneAssociativaDto[] = [];
    for (const inc of incs) {
      if (!inc) {
        continue;
      }
      const organizzazione: DichiarazioneAssociativaDto = new DichiarazioneAssociativaDto();
      organizzazione.id = inc.id;
      organizzazione.organizzazione = OrganizzazioneDto.toDto(inc);
      organizzazione.dataInizioAssociazione = inc.dataInizioAssociazione;
      organizzazione.dataInserimentoAssociazione = inc.dataInserimentoAssociazione;
      organizzazione.utenteInserimento = inc.utenteInserimento;
      organizzazione.dataFineAssociazione = inc.dataFineAssociazione;
      organizzazione.dataCancellazioneAssociazione = inc.dataCancellazioneAssociazione;
      organizzazione.utenteCancellazione = inc.utenteCancellazione;
      organizzazioni.push(organizzazione);
    }
    return organizzazioni;
  }
}

export class OrganizzazioneDto {
  id: number;
  denominazione: string;

  static toDto(inc: DichiarazioneAssociativaDto): OrganizzazioneDto {
    if (!inc) {
      return null;
    }
    const organizzazione: OrganizzazioneDto = new OrganizzazioneDto();
    organizzazione.id = inc.organizzazione.id;
    organizzazione.denominazione = inc.organizzazione.denominazione;
    return organizzazione;
  }

  static toDtos(incs: OrganizzazioneDto[]): OrganizzazioneDto[] {
    if (!incs) {
      return null;
    }
    const organizzazioni: OrganizzazioneDto[] = [];
    for (const inc of incs) {
      if (!inc) {
        continue;
      }
      const organizzazione: OrganizzazioneDto = new OrganizzazioneDto();
      organizzazione.id = inc.id;
      organizzazione.denominazione = inc.denominazione;
      organizzazioni.push(organizzazione);
    }
    return organizzazioni;
  }
}
