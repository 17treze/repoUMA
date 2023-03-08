export enum StatoValidazione {
  OK,
  ERROR,
  WARNING
}

export interface ResponseValidazione {
    esito: StatoValidazione;
    messaggio?: string;
}
