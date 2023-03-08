import { Message } from "primeng/api";

export enum severityMessage {
    success,
    error,
    warn,
    info,
}

export class Messages {

    static TOAST_LIFE = 3000;

    static OPERAZIONE_OK = "Operazione terminata con successo";
    static ERRORE_DATI = "Dati compilati non validi";
    static CARICAMENTO = 'Caricamento in corso';
    // profilazione 
    static ERRORE_RESPONSABILITA = "Dati Responsabilità Mancanti";
    static ERRORE_PROFILO = "Dati Profilo Mancanti";
    static ERRORE_CARICAMENTO = 'Errore Caricamento dei file';
    // generics
    static RICHIEDENTE_ERR = 'nessun richiedente trovato';
    static PROCEDIMENTI_ERR = 'Selezionare almeno un procedimento';
    static IMPRESA_ERR = 'nessuna Impresa trovata';
    static SOGGETTI_ERR = 'Nessun Soggetto Impresa';

    // step antimafia
    static SOGGETTI_DT = 'Dichiarazione Direttore Tecnico Assente';

    static FAMILIARI_CF_ERR = 'Codice Fiscale Non Conforme';
    static FAMILIARI_ALLEGATI = 'Attenzione - allegato dichiarazione familiari conviventi mancante';


    static getToast(mkey: string, mseverity: severityMessage, mmessage): Message {
        return { key: mkey, severity: severityMessage[mseverity], summary: "Messaggio " + severityMessage[mseverity], detail: mmessage, life: Messages.TOAST_LIFE }
    }

    // files upload
    static MAX_SIZE_FILE = 15;
    static FILE_EXTENSION_ACCEPTED = ".pdf,.p7m";

}
