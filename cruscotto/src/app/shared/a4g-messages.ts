import { Message, MessageService } from 'primeng-lts';

export enum A4gSeverityMessage {
  success,
  error,
  warn,
  info
}

export enum A4gUserMessages {
  NESSUN_PROFILO,
  PROFILO_ASSOCIATO,
  DOMANDA_PROTOCOLLATA
}

export class A4gMessages {
  static TOAST_LIFE = 10000;
  static SERVIZIO_NON_DISPONIBILE = `Servizio non disponibile. Riprovare più tardi`;
  static erroreRecuperoDatiIstruttoria = `Errore nel recupero dei dati di istruttoria`;
  static erroreRecuperoEsitiIstruttoria = `Errore nel recupero degli esiti di istruttoria`;
  static operazioneNegataPerStato = (stato: string) => `Operazione non disponibile per lo stato ${stato}`;
  static erroreRecuperoDati = 'Errore nel recupero dei dati richiesti';
  static erroreNessunaAziendaPresente = 'Non sono presenti aziende nel fascicolo aziendale per  cui il soggetto risulti titolare o rappresentante legale';
  static erroreNessunaAziendaPresenteRicercaCuaa = 'Non sono censite aziende per cui il soggetto risulti titolare o rappresentante legale. Si proceda con la ricerca dell’azienda per CUAA';
  static erroreRichiestaPresente = 'Esiste già una domanda di creazione o modifica al profilo in corso di lavorazione. Si prega di attendere la conclusione del processo';
  static erroreRecuperoDomandePSRStr = `Errore nel recupero delle domande`;
  static erroreRecuperoDettaglioPSRStr = `Errore nel recupero del dettaglio`;
  static erroreDettaglioVuotoPSRStr = "Si è verificato un errore. Si prega di riprovare più tardi."
  static erroreRecuperoTotaliPSRStr = `Errore nel recupero dei totali`;
  static erroreTotaliVuotiPSRStr = "Si è verificato un errore. Si prega di riprovare più tardi."
  static noDomandePSRStr = `Non esistono domande PSR Strutturali per l’azienda selezionata.`;
  static erroreRecuperoDichiarazioneAntimafia = `Non esistono certificazioni antimafia per l’azienda. Si ricorda che la normativa prevede la presentazione della domanda di certificazione antimafia solo nel caso in cui l’importo dei contributi ricevuti per singolo procedimento sia maggiore di € 25.000,00.`;
  static erroreRecuperoDomandeUma = `Non esistono domande UMA per l’azienda selezionata.`;
  static erroreRecuperoRettificheUma = `Si è verificato un errore nel recupero delle rettifiche.`;

  //============================= VALIDAZIONE INPUT
  static cfErrato = "Codice Fiscale non conforme";
  static cuaaErratoCompleto = "CUAA non conforme";
  static cuaaErrato = "CUAA troppo corto o non valido";
  static denominazioneErrata = "Denominazione non valida";
  static nomeErrato = "Nome non valido";
  static cognomeErrato = "Cognome non valido";
  //CTRL-TXT-MAIL-01
  static emailErrata = "Email/PEC non conforme";
  //CTRL-TXT-PHONE-01
  static telefonoErrato = "numero di telefono non conforme";
  //=============================

  static PROTOCOLLA_INFORMATIVA_ANTIMAFIA_ERROR =
    "Errore nella protocollazione del documento Informativa Privacy";
  static ERRORE_RESPONSABILITA = "Dati responsabilità mancanti";
  static ERRORE_SERVIZI = "Dati profilo mancanti";
  static RICHIESTA_REGISTRAZIONE_OK = "La richiesta di protocollazione della domanda di accesso al sistema è stata presa in carico e verrà elaborata a breve. La preghiamo di verificare la sua casella di posta per il completamento della procedura.";
  static ERRORE_DATI = "Dati compilati non validi";
  static ERRORE_NON_PREVISTO = "Si è verificato un errore non previsto, si prega di riprovare più tardi";
  static ERRORE_SALVATAGGIO_RICHIESTA_UTENTE = "Errore nel salvataggio dei dati base della richiesta";

  static RESPINGI_CONTROLLO =
    "Si è sicuri di voler respingere la richiesta del proprio Centro assistenza di validare il fascicolo?";

  static checkValidazione =
    "Si è sicuri di voler validare il fascicolo?";

  static errorGetUnitaTecnicoEconomiche = `Errore in fase di recupero Unità Tecnico Economiche, si prega di riprovare più tardi`;

  static getToast(
    mkey: string,
    mseverity: A4gSeverityMessage,
    mmessage
  ): Message {
    switch (mseverity) {
      case 0:
        return {
          key: mkey,
          severity: A4gSeverityMessage[mseverity],
          summary: 'Avviso!',
          detail: mmessage,
          life: A4gMessages.TOAST_LIFE
        };
      case 1:
        return {
          key: mkey,
          severity: A4gSeverityMessage[mseverity],
          summary: 'Errore!',
          detail: mmessage,
          life: A4gMessages.TOAST_LIFE
        };
      case 2:
        return {
          key: mkey,
          severity: A4gSeverityMessage[mseverity],
          summary: 'Attenzione!',
          detail: mmessage,
          life: A4gMessages.TOAST_LIFE
        };
      case 3:
        return {
          key: mkey,
          severity: A4gSeverityMessage[mseverity],
          summary: 'Informazione',
          detail: mmessage,
          life: A4gMessages.TOAST_LIFE
        };
    }
    return {
      key: mkey,
      severity: A4gSeverityMessage[mseverity],
      summary: 'Messaggio ' + A4gSeverityMessage[mseverity],
      detail: mmessage,
      life: A4gMessages.TOAST_LIFE
    };
  }

  static handleError(
    messageService: MessageService,
    error: any,
    genericError: string
  ) {

    if (error && error.error && error.status == 500) {
      const errMsg = error.error;
      messageService.add({
        key: 'tst',
        severity: 'error',
        summary: 'Errore!',
        detail: errMsg,
        life: A4gMessages.TOAST_LIFE
      });
    } else {
      messageService.add({
        key: 'tst',
        severity: 'error',
        summary: 'Errore!',
        detail: genericError,
        life: A4gMessages.TOAST_LIFE
      });
    }
  }

  //FAS-ANA-14
  static changeStatusInAggiornamentoOk = `La richiesta è stata respinta`;
  static changeStatusInAggiornamentoKo = `L’aggiornamento del fascicolo è fallito: riprovare più tardi`;
  static operazioneKoMandatoNonValido = `Operazione non consentita per l'utente connesso: mandato non valido`;

  static erroreRecuperoDatiFascicolo = 'Errore nel recupero dei dati di fascicolo';
  static erroreRecuperoDatiMandato = 'Errore nel recupero dei dati di mandato';

  static changeStatusValidazioneOk = `Il fascicolo è stato validato`;
  static changeStatusValidazioneKo = `La validazione del fascicolo è fallita: riprovare più tardi`;

  static changeStatusAllaFirmaAziendaOk = `Il fascicolo è in stato Alla firma Azienda`;
  static ExportSchedaValidazioneKo = "Errore nella generazione della scheda di validazione";

  static FILE_TYPE = fileExt => `Il File Caricato non è in formato ${fileExt}`;
  static FILE_MAXSIZE = maxSizeMb => `Il file non può essere superiore ai ${maxSizeMb}MB`;
  static UPLOAD_FILE_EXT = `Il formato del file caricato non è riconosciuto. Si prega di caricare un file pdf o pdf/p7m`;
  static UPLOAD_FILE_SIZE = `Il file deve avere una dimensione inferiore a 2 MB`;
  static UPLOAD_FILE_OK = `Il file è stato caricato correttamente`;
  static CONFERMA_FASCICOLO_VALIDATO = "Questa operazione aggiorna lo stato del fascicolo in 'Validato'. Continuare?";
  static changeStatusValidatoOk = `Il fascicolo è in stato Validato`;

  // modo pagamento
  static checkACancellaModalitaDiPagamento = `Confermi di voler cancellare l'IBAN: `;
  static salvataggioDatiKo = 'Si è verificato un errore nel salvataggio dei dati';
  static OPERAZIONE_OK = "Operazione terminata con successo";

  static ERRORE_GENERICO = "Errore generico";
}
