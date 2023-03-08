export interface ValidaTrasferitoViewModel {
    idRichiestaMittente: number;
    idRichiestaDestinatario: number;
    carburanteTrasferito: {
      gasolio: number;
      benzina: number;
      gasolioSerre: number;
    }
  }