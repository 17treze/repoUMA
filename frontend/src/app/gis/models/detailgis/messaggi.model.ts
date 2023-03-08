export interface Messaggi  {
    numeroElementi: number;
    messaggi: Messages[];
 }

 export interface Messages {
     id: number;
     utente: string;
     profiloUtente: string;
     dataInserimento: Date;
     testo: string;
 }
