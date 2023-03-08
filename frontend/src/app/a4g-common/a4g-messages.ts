import { Message, MessageService } from "primeng/api";
import { StatoDichiarazione } from "../fascicolo/antimafia/classi/statoDichiarazioneEnum";
import { StatoBdna } from "../istruttoria/istruttoria-antimafia/dto/StatoBdna";

export enum A4gSeverityMessage {
  success,
  error,
  warn,
  reject,
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

  static OPERAZIONE_OK = "Operazione terminata con successo";
  static ERRORE_DATI = "Dati compilati non validi";
  static VERIFICARE_DATI = "Verificare i dati inseriti!";
  static UTENTE_NON_ABILITATO = "Utente non abilitato";
  static FUNZIONE_WIP = "Funzionalità in lavorazione e al momento non ancora disponibile";
  //static UTENTE_PRESENTE = "Esiste già una domanda protocollata per questo utente, non è possibile registrarne una seconda";
  // validazione
  //CTRL-TXT-CF-01
  static cfErrato = "Codice Fiscale non conforme";

  static cuaaErratoCompleto = "CUAA non conforme";
  static cuaaErrato = "CUAA troppo corto o non valido";
  static denominazioneErrata = "Denominazione non valida";
  static nomeErrato = "Nome non valido";
  static cognomeErrato = "Cognome non valido";
  //CTRL-TXT-MAIL-01
  static emailErrata = "Email/PEC non conforme";
  //CTRL-TXT-PHONE-01
  static telefonoErrato = "Numero telefonico non conforme";
  static DT_ASSENTE = "Dichiarazione Direttore Tecnico Assente";

  static ERRORE_PROFILI = "Errore nel recupero dei Profili";

  static ERRORE_STAMPA_MANDATO = 'Il documento non può essere scaricato, si prega di riprovare più tardi';

  static ERRORE_STAMPA_REVOCA = 'Il documento non può essere scaricato, si prega di riprovare più tardi';

  static ERRORE_STAMPA_UTENTI_A4G = 'Il sistema non è disponibile: si prega di riprovare più tardi';

  // Controlli form richiesta utente
  //BR_FILE_UPLOAD_01
  static FILE_MAXSIZE = maxSizeMb =>
    `Il file non può essere superiore ai ${maxSizeMb}MB`;
  //BR_FILEFORM_01
  static FILE_TYPE = fileExt => `Il File Caricato non è in formato ${fileExt}`;

  //BR-CHECK-AS-01
  static ERRORE_DATI_ANAGRAFICI = datiAnagrafici =>
    `Dati Anagrafici mancanti ${datiAnagrafici}`;

  //BR-CHECK-AS-02
  static ERRORE_RESPONSABILITA = "Dati responsabilità mancanti";

  //BR-CHECK-AS-03
  static ERRORE_SERVIZI = "Dati profilo mancanti";

  //BR-WRITE-DB-AS-01
  static SALVATAGGIO_RICHIESTA_UTENTE =
    "Errore nel salvataggio dei dati base della richiesta";

  static FIRMA_PRIVACY = "Errore eseguendo la firma del modulo della privacy";
  static FIRMA_RICHIESTA_UTENTE =
    "Errore eseguendo la firma del modulo della domanda di registrazione";
  static STAMPA_RICHIESTA_UTENTE =
    "Errore eseguendo l'export della domanda di registrazione";

  // BR-CF-ANAGTRIB-01
  static ERR_ANAGRAFE_TRIBUTARIA = cf =>
    `Il Soggetto ${cf} non è censito in Anagrafe Tributaria `;

  //CTRL-CHECK-PR-AS-01	ACCESSO AL SISTEMA	CHECK BOX FIRMA INFORMATIVA	CONTROLLO CHECK FIRMA INFORMATIVA
  static VERIFICA_CONSULTAZIONE_PRIVACY =
    "E' necessario visulizzare e accettare l'informativa generale della privacy";
  //CTRL-CHECK-DO-AS-01	ACCESSO AL SISTEMA	CHECK BOX FIRMA MODULO DOMANDA	CONTROLLO CHECK FIRMA MODULO
  // BR-CHECK-AM-DO-FC-02
  static ALLEGATO_MANCANTE = cf =>
    `Attenzione - allegato dichiarazione familiari conviventi mancante per soggetto ${cf}`;

  static DICHIARAZIONE_MANCANTE = "Allegato Dichiarazione Antimafia Mancante";
  static VERIFICA_CONSULTAZIONE_MODULO =
    "E' necessario visulizzare e accetare il modulo della domanda";
  // BR-CHECK-AM-DO-FC-03
  static FAMILIARI_DIC =
    "Deve essere selezionato almeno un soggetto con carica";
  static ERRORE_GENERICO = "Errore generico";

  // BR-CHECK-AM-DO-FC-01
  static FAMILIARI_ERR = cf =>
    `Attenzione - incongruità nella dichiarazione Familiari conviventi del soggetto ${cf}`;
  static FAMILIARI_ERR_SPUNTA = `Attenzione - Dichiarazione Familiari obbligatoria`;

  // BR-AGE-18-01
  static FAMILIARI_MAGGIO = cf => `Il soggetto ${cf} non è maggiorenne`;
  //CREAZIONE RICHIESTA ANTIMAFIA
  //BR-CUAA-AGS-01
  static CUAA_NON_ATTIVO = CUAA =>
    `L'impresa ${CUAA} non ha un fascicolo attivo in SIAP`;
  //FAMILIARI CONVIVENTI - AGGIUNTA
  //CTRL-TXT-CF-01
  static CHECK_CONFORMITA_CF = "Codice Fiscale non conforme";
  static CF_ESISTENTE = cf => `Il Soggetto ${cf} è già stato inserito`;
  static FAMILIARI_CF_ASSENTE = cf =>
    `Soggetto ${cf}: Codice Fiscale familiare assente`;
  static FAMILIARI_PARENT = cf =>
    `Soggetto ${cf}: Valorizzare il grado di parentela di tutti i familiari`;
  //FAMILIARI CONVIVENTI - AZIONI
  //BR-FILE-UPLOAD-01
  static UPLOAD_FILE_MAX_SIZE_FILE = 2;
  static UPLOAD_FILE_TYPE_PDF = "pdf";
  static UPLOAD_FILE_TYPE_PDFP7M = "pdf.p7m";
  static UPLOAD_FILE_TYPE_CSV = "csv";
  //CTRL-BTN-UPLOAD-01
  static FILE_ALREADY_UPLOADED =
    "Il file già presente verrà sovrascritto Continuare?";
  //BR-CHECK-PR-01
  static INFORMATIVA_PRIVACY_ASSENTE = cf =>
    `Il Soggetto ${cf} non ha accettato l'informativa sulla Privacy`;

  //FAS-ANA-11 BR1
  static CONFERMA_FASCICOLO_ALLA_FIRMA_AZIENDA =
    "Questa operazione aggiorna lo stato del fascicolo in 'Pronto alla firma Azienda'. Continuare?";

  //FAS-ANA-12 BR1
  static CONFERMA_FASCICOLO_IN_AGGIORNAMENTO =
    "Si è sicuri di voler riportare in aggiornamento il fascicolo?";

  //BR-DIGSIGN-01
  static UPLOAD_FILE_NOT_SIGNED =
    "Il File Caricato non contiene una firma valida";

  // BRASSNVL014
  static NESSUN_PROFILO = cf =>
    `L'utente ${cf} non dispone di profili. E' necessario creare un profilo`;
  static PROFILO_ASSOCIATO = cf =>
    `L'utente ${cf} ha già un profilo associato. Modificare il profilo corrente`;
  static DOMANDA_PROTOCOLLATA = cf =>
    `L'utente ${cf} non dispone di profili: per poter creare un profilo è necessario attendere il completamento dell'iter della domanda di richiesta di accesso al sistema`;

  static AUTENTICAZIONE_FALLITA =
    `Per accedere al sistema bisogna autenticarsi`;

  static ERRORE_SALVATAGGIO_DATI_ISTRUTTORIA =
    "Errore nel salvataggio dei dati";

  static ERRORE_NUMERO =
    "Numero inserito non valido";

  static RESET_FILE_CARICATO = 
  "File caricati rimossi a seguito del cambio della sottotipologia";

  //FINE FAMILIARI CONVIVENTI - AZIONI

  static CTRLIAMCNT002 = "Selezionare almeno una domanda per avviare il processo";
  static errorIstruttoriaAntimafiaImportDati = "Errore nell'acquisizione delle domande! Si prega di riporvare più tardi";

  static IMPOSSILE_GENERARE_FILE = "Impossibile generare il file";

  // header popup rifiuta domanda
  static COMUNICAZIONE_RIFIUTO_DOMANDA = descImpresa => `Comunicazione rifiuto domanda ${descImpresa}`;

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
          summary: "Avviso!",
          detail: mmessage,
          life: A4gMessages.TOAST_LIFE
        };
      case 1:
        return {
          key: mkey,
          severity: A4gSeverityMessage[mseverity],
          summary: "Errore!",
          detail: mmessage,
          life: A4gMessages.TOAST_LIFE
        };
      case 2:
        return {
          key: mkey,
          severity: A4gSeverityMessage[mseverity],
          summary: "Attenzione!",
          detail: mmessage,
          life: A4gMessages.TOAST_LIFE
        };
      case 3:
        return {
          key: mkey,
          severity: A4gSeverityMessage[mseverity],
          summary: "Informazione",
          detail: mmessage,
          life: A4gMessages.TOAST_LIFE
        };
    }
    return {
      key: mkey,
      severity: A4gSeverityMessage[mseverity],
      summary: "Messaggio " + A4gSeverityMessage[mseverity],
      detail: mmessage,
      life: A4gMessages.TOAST_LIFE
    };
  }

  static handleError(
    messageService: MessageService,
    error: any,
    genericError: string) {
    if (error && error.error && error.status == 500) {
      let errMsg = error.error!.message ? error.error.message : error.error;
      messageService.add({
        key: "tst",
        severity: "error",
        summary: "Errore!",
        detail: errMsg,
        life: A4gMessages.TOAST_LIFE
      });
    } else
      messageService.add({
        key: "tst",
        severity: "error",
        summary: "Errore!",
        detail: genericError,
        life: A4gMessages.TOAST_LIFE
      });
  }

  // TODO: da eliminare ed eventualmente sostituire con string-support.ts isNullOrEmpty
  static isUndefinedOrNull(val): boolean {
    return val === undefined || val === null;
  }



  static PROTOCOLLA_DOMANDA_ACCESSO_ERROR =
    "Errore nella protocollazione della Domanda di Accesso";

  // commentata perche' la protocollazione e' asincrona
  // static PROTOCOLLA_ANTIMAFIA_SUCCESS = NR_PROTOCOLLO =>
  //   `Domanda Antimafia protocollata con Successo: ${NR_PROTOCOLLO}`;
  static PROTOCOLLA_ANTIMAFIA_SUCCESS = `La richiesta di protocollazione della domanda antimafia è stata presa in carico e verrà elaborata a breve. E'possibile visualizzare lo stato della domanda nel cruscotto informazioni`;
  static PROTOCOLLA_INFORMATIVA_ANTIMAFIA_SUCCESS = NR_PROTOCOLLO =>
    `Informativa Antimafia Protocollata con Successo: ${NR_PROTOCOLLO}`;
  static PROTOCOLLA_INFORMATIVA_PRIVACY_SUCCESS = `Informativa Privacy Protocollata con Successo`;
  static PROTOCOLLA_ANTIMAFIA_ERROR =
    "Errore nella protocollazione della Domanda Antimafia";
  static PROTOCOLLA_INFORMATIVA_ANTIMAFIA_ERROR =
    "Errore nella protocollazione del documento Informativa Privacy";
  //BR-CHECK-AM-DO-02
  static CHECK_DICHIARAZIONE_PROTOCOLLATA = cuaa =>
    `Esiste già una domanda protocollata per  ${cuaa}.\nCreare una nuova domanda e annullare la precedente?`;
  static WARNING_START_PROTOCOLLAZIONE = `L\'attività di protocollazione potrebbe richiedere da pochi secondi a qualche minuto a seconda del numero e della dimensione dei documenti caricati.\nNon chiudere il browser fino al completamento della stessa.\nProcedere?`;

  static BRDAMNVL005 = "Il CUAA in oggetto &egrave; attualmente sottoposto ad un verifica specifica per antimafia e non &egrave; possibile movimentare la certificazione";
  //##ISTRUTTORIA
  //CTRLIAMPRT001
  static CTRLIAMPRT001 = `Selezionare almeno una domanda per avviare il processo`;
  static BRIAMPRT001 = `Impossibile procedere poichè è già in corso un movimento di controllo massivo. Riprovare più tardi.`;
  static IMPPSRSUPERR = `Impossibile procedere poichè è già in corso un'acquisizione massiva delle domande PSR Superficie. Riprovare più tardi.`;
  static BRIAMPRT003 = `Errore nella schedulazione del processo riprovare più tardi.`;
  static CTRLIAMCNT007 = "Per confermare il passaggio di stato a esito negativo o in lavorazione o esito positivo si deve necessariamente compilare i campi data bdna e protocollo bdna";
  static CTRLIAMCNT007_KO = "Passaggio di stato della domanda non riuscito";
  static BRIAMCMN007 = "Aggiornamento dati sincronizzazione non risucito";
  static WARNING_AGGIORNA_DOMANDA_COLLEGATA = (stato: StatoBdna) =>
    `La domanda verrà passata nello stato ${stato}. Vuoi proseguire?`
  static WARNING_AGGIORNA_DOMANDA = (stato: StatoDichiarazione) =>
    `La domanda verrà passata nello stato ${stato}. Vuoi proseguire?`
  static processoDiControllo = (tot: string, parz: string) => `E' in corso un processo di controllo Domande Elaborate ${tot}/${parz}`;
  static processoDiCalcoloCapiAmm = (tot: string, parz: string) => `E' in corso un processo di calcolo Capi Ammissibili ${tot}/${parz}`;
  static processoDiCalcoloACS = (tot: string, parz: string) => `E' in corso un processo di calcolo Accoppiato Superficie ${tot}/${parz}`;
  static processoDiCalcoloPremioCapi = (tot: string, parz: string) => `E' in corso un processo di calcolo Premio Capi ${tot}/${parz}`;
  static processoDiControlliLiquidazione = (tot: string, parz: string) => `E' in corso un processo di Controlli Liquidazione ${tot}/${parz}`;
  static processoDiControlliIntersostegno = (tot: string, parz: string) => `E' in corso un processo di Liquidazione ${tot}/${parz}`;
  static processoDiControlliIntersostegnoACZ = (tot: string, parz: string) => `E' in corso un processo di Liquidazione Zootecnia ${tot}/${parz}`;
  static processoDiControlliIntersostegnoACS = (tot: string, parz: string) => `E' in corso un processo di Liquidazione Superficie ${tot}/${parz}`;
  static procesioDiControlloIstruttoria = (processo: string, parz: string, tot: string) => `E' in corso un processo di ${processo}: ${parz}/${tot}`;


  static processoDiCalcoloDisaccoppiato = (tot: string, parz: string) => `E' in corso un processo di Calcolo Disaccoppiato ${tot}/${parz}`;
  static processoDiControlliLiquidabilita = (tot: string, parz: string) => `E' in corso un processo di controllo Liquidabilità ${tot}/${parz}`;
  static processoDiNonAmmissibilita = (tot: string, parz: string) => `E' in corso un processo di non ammissibilità ${tot}/${parz}`;
  static processoDiLiquidazione = (tot: string, parz: string) => `E' in corso un processo di Liquidazione ${tot}/${parz}`;

  static calcoloAccoppiatoACSProcessoAvviato = domandeCount => `Il calcolo massivo accopiato superficie è stato schedulato con successo per le ${domandeCount} domande selezionate`;
  static calcoloAccoppiatoACSProcessoAvviatoDomandaSingola = `Il calcolo massivo accopiato superficie è stato schedulato con successo per la domanda selezionata`;
  static calcoloAccoppiatoACZProcessoAvviato = domandeCount => `Il calcolo massivo accopiato zootecnia è stato schedulato con successo per le ${domandeCount} domande selezionate`;
  static calcoloAccoppiatoACZProcessoAvviatoDomandaSingola = `Il calcolo massivo accopiato zootecnia è stato schedulato con successo per la domanda selezionata`;
  static calcoloControlliLiquidazioneAvviato = domandeCount => `Il calcolo massivo controlli liquidazione è stato schedulato con successo per le ${domandeCount} domande selezionate`;
  static calcoloControlliLiquidazioneAvviatoDomandaSingola = `Il calcolo massivo controlli liquidazione è stato schedulato con successo per la domanda selezionata`;
  static calcoloControllIntersostegno = domandeCount => `Il calcolo massivo controlli intersostegno è stato schedulato con successo per le ${domandeCount} domande selezionate`;
  static calcoloControlliIntersostegnoDomandaSingola = `Il calcolo massivo controlli intersostegno è stato schedulato con successo per la domanda selezionata`;
  static aggiornamentoDatiBDN_OK = `Aggiornamento dati BDN completato`;
  static aggiornamentoDatiBDN_KO = `Aggiornamento dati BDN non riuscito`;
  static aggiornamentoDatiBDN_Errore = `Errore nella schedulazione del processo di aggionamento dati BDN. Riprovare più tardi.`;
  static calcoloAccoppiatiErroreProcesso = `Errore nella schedulazione del processo. Riprovare più tardi.`;
  static calcoloAccoppiatiWarningNumeroDomande = `Selezionare almeno una domanda per avviare il processo.`;
  static calcoloAccoppiatoACZProcessoControllo = `Impossibile procedere perchè è già in corso un processo di calcolo capi ammissibili. Riprovare più tardi.`;
  static calcoloAccoppiatoACSProcessoControllo = `Impossibile procedere poichè è già in corso un movimento di calcolo massivo accoppiato superficie. Riprovare più tardi`;
  static calcoloPremioACZProcessoControllo = `Impossibile procedere poichè è già in corso un processo di calcolo premio per accoppiato zootecnia. Riprovare più tardi.`;
  static calcoloControlliLiquidazioneProcessoControllo = `Impossibile procedere poichè è già in corso un processo di calcolo controlli liquidazione. Riprovare più tardi.`;
  static calcoloFinestraTemporaleDomandaIntegrativa = (dataInizio: string, dataFine: string) => `La finestra per la presentazione della domanda integrativa zootecnia per la campagna corrente è chiusa.\nData Inizio: ${dataInizio}\nData Fine: ${dataFine}`;
  static calcoloAccoppiatoDomandaIntegrativaInCorso = `Non è possibile eseguire l'operazione richiesta poiché la domanda integrativa zootecnia è aperta o è già stata presentata`;
  static controlloPresentazioneDomandaIntegrativa = (cuaa: string) => `Per l'azienda ${cuaa} non è possibilie procedere all'apertura della domanda integrativa zootecnia `
  static erroreRecuperoDati = 'Errore nel recupero dei dati richiesti';
  static erroreRecuperoDatiFascicoloMandato = 'Errore nel recupero dei dati di fascicolo e/o mandato';
  static erroreRecuperoDatiSrt = 'Errore nel recupero dei dati richiesti da SRTrento';
  static erroreRecuperoDatiAgs = 'Errore nel recupero dei dati richiesti da SIAP';
  static erroreRecuperoDatiA4g = 'Errore nel recupero dei dati richiesti da A4G';
  static erroreRecuperoDatiEleggibilita = 'Errore nel recupero dei Dati Eleggibilità';
  static erroreRecuperoDatiGreening = 'Errore nel recupero dei Dati Greening';
  static erroreRecuperoDatiMantenimento = 'Errore nel recupero dei Dati Mantenimento';
  static controlloSalvaDomandaIntegrativa = 'I dati sono stati salvati correttamente, attenzione ci sono capi ammissibili o ammissibili con anomalie non selezionati, per confermare definitivamente la domanda è necessario che tutti i capi ammissibili o ammissibili con anomalie siano impegnati almeno su un intervento';
  static controlloPresentaDomandaIntegrativa = 'Attenzione ci sono capi ammissibili o ammissibili con anomalie non selezionati, per confermare definitivamente la domanda è necessario che tutti i capi ammissibili o ammissibili con anomalie siano impegnati almeno su un intervento';
  static modficiaDomandaIntegrativaDocumento = 'Il documento precedentemente caricato verrà cancellato. Una volta confermata la domanda integrativa modificata dovrà essere stampata, fatta firmare con firma autografa e ricaricata in formato .pdf con allegato documento di identità del firmatario (dimensione massima del file 2Mb)'
  static calcoloAccoppiatiWarningDomandePresentate = 'Almeno una delle domande selezionate ha già confermato la domanda integrativa';
  static stampaDomandaIntegrativa = 'Per effettuare la stampa è necessario presentare la domanda';
  static domandaIntegrativaPresentata = (ID_DOMANDA_DU: string, CUAA: string, SYSDATE: string, ID_DOMANDA_ZOOTECNIA: string) => `Operazione eseguita con successo: la domanda Integrativa Zootecnia alla domanda ${ID_DOMANDA_DU}  per il CUAA ${CUAA} è stata salvata in DATA: ${SYSDATE} con il seguente identificativo univoco: ${ID_DOMANDA_ZOOTECNIA}`;
  static calcoloFinestraTemporaleDomandaIntegrativaACZ = `La finestra per la presentazione della domanda integrativa zootecnia per la campagna corrente non è chiusa.`;
  static calcoloFinestraTemporaleDomandaIntegrativaACZChiusa = `La finestra per la presentazione della domanda integrativa zootecnia per la campagna corrente è chiusa.`;
  static finestraTemporaleDomandaIntegrativaChiusa = `La finestra per la presentazione della domanda integrativa zootecnia per la campagna corrente risulta aperta`;
  static calcoloControlliIntersostegnoProcessoControllo = `Impossibile procedere poichè è già in corso un processo di calcolo controlli intersostegno zootecnia, controlli intersostegno superficie o calcolo disaccoppiato. Riprovare più tardi.`;
  static processoInCorso = `Impossibile procedere poichè è già in corso un processo. Riprovare più tardi.`;
  static processoAvviatoOK = `Processo avviato correttamente`;
  static stampaRicevutaDomandaIntegrativa = `Attenzione la stampa della domanda non risulta correttamente caricata a sistema. Contattare l'operatore CAA.`;
  static operazioneAnnullata = `Operazione annullata`;
  static processoOkStatisticheBPS = `Processo di generazione dati statistici BPS schedulato con successo`;
  static processoOkStatisticheGreening = `Processo di generazione dati statistici Greening schedulato con successo`;
  static processoOkStatisticheGiovane = `Processo di generazione dati statistici Giovane schedulato con successo`;
  static processoOkStatisticheAccoppiato = `Processo di generazione dati statistici Accoppiato schedulato con successo`;
  static processoOkSincronizzazioneSuperficiAccertate = `Processo di generazione dati sincronizzazione superfici accertate AGEA schedulato con successo`;
  static processoOkSincronizzazionePagamenti = `Processo di generazione dati sincronizzazione AGEA pagamenti schedulato con successo`;
  static processoKoStatistiche = `Attenzione si è verificato un problema nella schedulazione del processo!`
  static inRunStatisticheBPS = `Attenzione c'è già un processo di calcolo statistiche BPS in corso. Si prega di riprovare più tardi!`;
  static inRunStatisticheGreening = `Attenzione c'è già un processo di calcolo statistiche Greening in corso. Si prega di riprovare più tardi!`;
  static inRunStatisticheGiovane = `Attenzione c'è già un processo di calcolo statistiche Giovane in corso. Si prega di riprovare più tardi!`;
  static inRunStatisticheAccoppiato = `Attenzione c'è già un processo di calcolo statistiche Accoppiato in corso. Si prega di riprovare più tardi!`;
  static inRunSincronizzazioneSuperficiAccertate = `Attenzione c'è già un processo di sincronizzazione superfici accertate in corso. Si prega di riprovare più tardi!`;
  static inRunSincronizzazionePagamenti = `Attenzione c'è già un processo di sincronizzazione AGEA pagamenti in corso. Si prega di riprovare più tardi!`;

  static ANNULLA_ISTRUTTORIA_DOMANDA_IN_PAGAMENTO = `L'istruttoria non può essere annullata: per la domanda in oggetto sono stati effettuati pagamenti.`;
  static ANNULLA_ISTRUTTORIA_OK = `L'istruttoria è stata annullata`;
  static ANNULLA_ISTRUTTORIA_KO = `Errore nell'annullo della domanda`;

  static CONFERMA_ANNULLA_ISTRUTTORIA = `La DOMANDA in oggetto con tutte le relative istruttorie verrà annullata.`;
  static CONFERMA_ANNULLA_ISTRUTTORIA_CONFERMA = `Sei sicuro di voler continuare? Se scegli 'Sì', alla fine del processo verrai reindirizzato al cruscotto`;
  static esitiBdna = (totDomande: number, domandeAggiornate: Array<String>, domandeConErrori: Array<String>) => `Operazione eseguita con successo. Totale domande elaborate: ${totDomande} - Domande aggiornate: ${domandeAggiornate} - Domande con errori: ${domandeConErrori}`
  static ELENCO_VUOTO = "Nessuna domanda presente in elenco";
  static CTRLIAMCNT012 = `Attenzione! Si sta cercando di caricare un esito per cui non è stato caricata la richiesta in BDNA. Si prega di procedere prima con il caricamento e successivamente con il caricamento dell'esito.`
  static ExportCsvKo = "Errore nella generazione del file .csv";
  static changeStatusKo = "Al momento non è possibile prendere in carico la richiesta. Riprovare più tardi";

  static inputRequiredInvalid = "E' necessario compilare i campi obbligatori (contrassegnati dall'asterisco)";
  static salvataggioDatiKo = 'Si è verificato un errore nel salvataggio dei dati';

  static utenteNonTrovato = "L'utente non è stato trovato! Verificare il testo digitato"

  static BRFASPRX003 = (cuaa: string) => `L\'impresa ${cuaa} non risulta attiva presso la Camera di Commercio`;

  static valoreNonConforme = "Attenzione: sono stati inseriti valori non conformi";

  static processoRicevibilitaOk = `Processo di ricevibilità schedulato con successo`;

  static processoAvvioIstruttoriaOk = `Processo di avvio istruttoria schedulato con successo`;

  static recuperoCodiciEnteDaUtenteKo = `Errore nel recupero dei codici Ente associati all'utente loggato`;
  static erroreRichiestaPresente = 'Esiste già una domanda di creazione o modifica al profilo in corso di lavorazione. Si prega di attendere la conclusione del processo';
  static verificaPrivacyKO = `Errore nella verifica dell'informativa privacy`;
  static UPLOAD_FILE_EXT = `Il formato del file caricato non è riconosciuto. Si prega di caricare un file pdf o pdf/p7m`;
  static UPLOAD_FILE_EXT_PDF =`Il formato del file caricato non è riconosciuto. Si prega di caricare un file pdf `;
  static UPLOAD_FILE_SIZE = `Il file deve avere una dimensione inferiore a 2 MB`;
  static UPLOAD_FILE_OK = `Il file è stato caricato correttamente`;
  static erroreVisualizzazioneMandato = "Non è possibile visualizzare il mandato. Si prega di riprovare più tardi";
  static erroreVisualizzazioneRevocaImmediata = "Il documento non può essere scaricato, si prega di riprovare più tardi";

  static sportelloNonSelezionato = `Il campo Sportello CAA è obbligatorio`;

  static CHECK_SOCIETA_SERVIZI = `Non hai inserito la società di servizi, vuoi procedere comunque con l'acquisizione del mandato?`;
  static ERRORE_RICERCA_FASCICOLO = 'Errore in fase di ricerca del fascicolo';

  static soggettoNonCensitoTitolare = 'Il soggetto autenticato non è censito come titolare/rappresentante legale  nel fascicolo';

  static erroreVerificaInserimentoRevocaImmediata = `Non è stato possibile verificare i criteri di presentazione richiesta revoca immediata. Si prega di riprovare più tardi`;
  static erroreVerificaPresenzaRevocaOrdinaria = `Non è stato possibile verificare la presenza di revoca ordinaria. Si prega di riprovare più tardi`;
  static checkVerificaPresenzaRevocaOrdinariaSummary = `Il mandato attuale è già stato revocato con revoca ordinaria`;
  static checkVerificaPresenzaRevocaOrdinariaDetail = `Vuoi procedere con la revoca immediata?`;
  static successRevocaImmediata = `La richiesta di revoca immediata è stata trasmessa correttamente`;
  static notFoundRevocaFile = `Si prega di caricare il modulo di revoca firmato digitalmente.`;
  static checkRifiutoRichiestaRevocaImmediata = `Vuoi rifiutare la richiesta di revoca immediata?`;
  static checkAccettaRichiestaRevocaImmediata = `Confermi di voler accettare la richiesta di revoca immediata?`;
  static checkACancellaModalitaDiPagamento = `Confermi di voler cancellare l'IBAN: `;
  static checkACancellaErede = `Confermi di voler cancellare l'erede: `;
  static notFoundMotivazioneRifiuto = `E' necessario digitare la motivazione del rifiuto`;
  static nessunaRichiestaPresente = `Nessuna richiesta presente`;
  static modificaNonSalvata = `La modifica non è stata salvata. Si prega di riprovare più tardi.`;
  static notificaAppagFallita = `Non è stato possibile inviare una email ad APPAG`;
  static notificaAppagInviata = `Email inviata ad APPAG`;
  static notificaAziendaFallita = `Non è stato possibile inviare una email all'Azienda`;
  static notificaAziendaInviata = `Email inviata all'Azienda`;
  static errorGetListSportelli = `Errore in fase di recupero lista Sportelli, si prega di riprovare più tardi`;
  static errorGetUnitaTecnicoEconomiche = `Errore in fase di recupero Unità Tecnico Economiche, si prega di riprovare più tardi`;
  static errorGetAllevamenti = `Errore in fase di recupero Allevamenti, si prega di riprovare più tardi`;
  static responsabilitaPresente = `La responsabilità selezionata è già presente in elenco`;

  //FAS-ANA-12 BR1
  static changeStatusInAggiornamentoOk = `Il fascicolo è in stato riportato In aggiornamento`;
  static changeStatusInAggiornamentoKo = `L’aggiornamento del fascicolo è fallito: riprovare più tardi`;
  static fascicoloNonValido = `Il fascicolo aziendale è stato modificato successivamente alla creazione della Richiesta di carburante. E' quindi necessario eliminare l’attuale domanda e poi presentare una nuova Richiesta di carburante. Procedere subito con la cancellazione della domanda?`;

  static ExportSchedaValidazioneKo = "Errore nella generazione della scheda di validazione";
  static changeStatusAllaFirmaCaaOk = `Il fascicolo è in stato Alla firma del CAA`;
  static RicercaDomandeUnicheNumeroDomandaNonValido = `Il numero domanda deve essere un numero`;

  static MACCHINARI = {
    salvataggioOK: "I dati sono stati salvati correttamente!",
    datiNonValidi: `Attenzione! E’ necessario completare i campi obbligatori`,
    documentoNonPresente: `Attenzione! E’ necessario allegare il documento di possesso`,
    telaioOrMatricolaObbligatorio: `E' necessario inserire un numero di telaio o di motore per la macchina da salvare`
  }

  static UPLOAD_FILE_CSV = `Il formato del file caricato non è riconosciuto. Si prega di caricare un file csv`;

  //FAS-SEGN-62-73 Messaggio controllo Firma
  static ERRORE_CONTROLLO_FIRMASINGOLA = 'Il file caricato non risulta firmato o sono presenti più firme';
  static ERRORE_CONTROLLO_FIRMAMULTIPLA = 'Il file caricato non risulta firmato dal firmatario del fascicolo';
  static ERRORE_FIRMA_SINGOLA = 'Il file caricato presenta una sola firma';
  static ERRORE_GENERICO_VERIFICA_FIRMA = 'Errore nella verifica della firma digitale del file';
}
