import { Injectable } from '@angular/core';
/*
 * Usare it-IT.json con pipe translate per le traduzioni
*/
@Injectable({
  providedIn: 'root'
})
export class Labels {
  static breadcrumbModificaSuolo = 'Modifica Suolo';
  static dtUltimoCalcolo = 'Data Ultimo Calcolo';
  static descrizioneImpresa = 'Descrizione Impresa';
  static denominazioneImpresa = 'Denominazione';
  static denominazioneAzienda = 'Denominazione Azienda';
  static sedeSportelloCaa = 'Sede Sportello CAA';
  static localitaSportelloCaa = 'Località sportello CAA';
  static dataSottoscrizione = 'Data richiesta';
  static esito = 'Esito';
  static motivazione = 'Motivazione';
  static provinciaImpresa = 'Provincia dell’impresa';
  static CCIAAnumeroIscrizione = 'Numero di iscrizione CCIA';
  static CCIAAdataIscrizione = 'Data di iscrizione';
  static oggettoSociale = 'Oggetto sociale';
  static formaGiuridica = 'Forma giuridica';
  static estremiCostituzione = 'Estremi atto di costituzione';
  static capitaleSociale = 'Capitale sociale, valore';
  static durataSocieta = `Durata della società`;
  static codiceFiscale = 'Codice fiscale';
  static partitaIva = 'Partita iva';
  static sedeLegale = 'Sede legale';
  static indirizzoPEC = 'Pec';
  static nome = 'Nome';
  static cognome = 'Cognome';
  static comuneNascita = 'Comune Nascita';
  static provinciaNascita = 'Provincia Nascita';
  static dataNascita = 'Data Nascita';
  static sesso = 'Sesso';
  static residenzaComune = 'Residenza Comune';
  static residenzaCAP = 'Residenza CAP';
  static residenzaProvincia = 'Residenza Provincia';
  static residenzaVia = 'Residenza Via';
  static residenza = 'Residenza Indirizzo';
  static carica = 'Carica';
  static dataInizioCarica = 'Inizio Carica';
  static dataFineCarica = 'Fine Carica';
  static dichiarazione = 'Dichiarazione';
  static azioni = 'Azioni';
  static comune = 'Comune';
  static indirizzo = 'indirizzo';
  static civico = 'N. Civico';
  static provinciaSigla = 'Sigla Provincia';
  static cap = 'CAP';
  static denominazione = 'Denominazione';
  static nessunaCarica = 'Nessuna Carica';
  static AVVIA_CONTROLLO = 'Avvia Controllo';
  static dataFine = 'Data Fine';
  static salva = 'Salva';
  static esciSenzaSalvare = 'Esci Senza Salvare';
  static emailPec = 'Email/PEC';
  static telefono = 'Telefono';
  static cf = 'C.F.';
  static titolare = 'Titolare impresa';
  static legaleRappresentante = 'Legale rappresentante';
  static liberoProfessionistaConsulente = 'Libero professionista/Consulente';
  static ordineDiAppartenenza = 'Ordine di appartenenza';
  static numeroDiIscrizione = 'Numero di iscrizione';
  static dipendenteCollaboratorePat = 'Dipendente/Collaboratore PAT';
  static matricola = 'Matricola';
  static struttura = 'Struttura';
  static dirigente = 'Dirigente';
  static dipendenteCollaboratoreCaa = ' Dipendente/Collaboratore CAA';
  static caaSedi = 'Caa/Sedi';
  static responsabile = 'Responsabile';
  static collaboratoreAltriEnti = 'Collaboratore Altri enti';
  static denominazioneEnte = 'Denominazione ente';
  static rappresentanteLegale = 'Rappresentante legale';
  static note = 'Note';
  static revocaOrdinariaRicerca = 'Digita il CUAA del fascicolo da revocare';
  static revoca = 'Revoca';
  static revocaMandato = 'Revoca Mandato';
  static REVOCA_IMMEDIATA = 'Revoca Immediata';
  static mandato = 'Mandato';
  static filter = 'Filtra';
  static fascicoli = 'fascicoli';

  // invio certificazione AM
  static dato = 'Dato';
  static tipologia = 'Tipologia';
  static necessario = 'Necessario';
  static stato = 'Stato';
  static errori = 'Errori';
  static datoCCIA = 'Dato CCIA';
  // ---------------------------

  // tab modulo
  static procedimenti = 'Procedimenti';
  static anagraficaRichiedente = 'Richiedente';
  static anagraficaImpresa = 'Impresa';
  static soggettiCarica = 'Soggetti con carica';
  static aziendeCollegate = 'Aziende Collegate';
  static collegate = 'Collegate';
  static familiari = 'Familiari e conviventi';
  static verifica = 'Verifica e Invio';
  // ---------------------------

  // documenti
  static dichiarazioneFamiliariConviventi =
    'Dichiarazione Familiari e Conviventi';
  static dichiarazioneAntimafia = 'Dichiarazione Antimafia';
  static documentoFirmato = 'Documenti Firmati';
  static documentoFirmatoDigitalmente = 'Documento Firmato Digitalmente';
  static documentoInformativaPrivacy = 'Informativa privacy';
  static DOWNLOAD_DICHIARAZIONE_ANTIMAFIA = 'Download Dichiarazione Antimafia';
  static CARICA_MODELLO = 'Carica Modello';
  static SCARICA_MODELLO = 'Scarica Modello';
  // ---------------------------

  // dettagli fascicolo
  static idFascicolo = 'Identificativo Fascicolo';
  static denominazioneFascicolo = 'Denominazione fascicolo';
  static cuaa = 'Codice Unico Aziendale';
  static caa = 'Centro Assistenza Agricola';
  static statoFascicolo = 'Stato Fascicolo';
  static cuaaSigla = 'CUAA';
  static dataInizioCompilazione = 'Data Inizio Compilazione';
  static cerca = 'CERCA';
  static creaFascicolo = 'Costituisci Fascicolo';
  static riaperturaFascicolo = 'Riapertura Fascicolo';
  static ricercaFascicolo = 'Ricerca Fascicolo';
  static revocaOrdinaria = 'Revoca ordinaria';
  static fascicoliDaMigrare = 'Fascicoli da migrare';
  static fascicoloDaRiaprire = 'Fascicolo da riaprire';
  static migrazioneFascicolo = 'Migrazione';
  static sede = 'Sede';
  static fonte = 'Fonte';
  static cercaInParixEAgenziaEntrate = 'Cerca in PARIX / Agenzia Entrate';
  static apriFascicolo = 'Costituisci fascicolo';
  static datiIdentificativi = 'Dati Identificativi';
  static residenzaSede = 'Residenza o Sede';
  static attivitaIVA = 'Attivita\' IVA';
  static iscrizioneCCIAA = 'Iscrizione CCIAA';
  static ricercaFascicoloNew = 'Ricerca Fascicolo New';
  static sportello = 'Sportello';
  static sportelloMandatario = 'Sportello mandatario';
  static btnCostituisciTrasferisciFascicolo = 'Costituzione e trasferimento';
  static fascicolo = 'Fascicolo';
  static selezioneCreazioneNuovoFascicolo = 'Costituzione: seleziona CUAA';
  static creazioneNuovoFascicolo = 'Costituisci';
  static selezioneCostituisciTrasferisciFascicolo = 'Costituzione e trasferimento: seleziona CUAA';
  static costituisciTrasferisciFascicolo = 'Costituzione e trasferimento';
  static selezioneTrasferisciFascicolo = 'Trasferimento: seleziona CUAA';
  static trasferisciFascicolo = 'Trasferimento';
  static riapriFascicolo = 'Riapri fascicolo';
  static migraFascicolo = 'Migrazione fascicolo';
  static tipoDetenzione = 'Tipo detenzione';
  static acquisizioneMandato = 'Acquisizione mandato';
  static ricercaFascicoloPerAcquisizioneMandato = 'Fascicolo Revocato: ricerca per CUAA';
  // ---------------------------

  // generici
  static STATO_IN_PROTOCOLLAZIONE = 'In Protocollazione';
  static nd = 'ND';
  static illimitata = 'Illimitata';
  static si = 'Si';
  static no = 'No';
  static noContent = 'Nessun contenuto disponibile';
  static noDichiarazioneAntimafia = 'Non sono presenti domande di certificazione antimafia';
  static data = 'Data';
  static seleziona = 'Seleziona';
  static CONFERMA = 'Conferma';
  static ANNULLA = 'Annulla';
  static PROCEDI = 'Procedi';
  static CAMPO_OBBLIGATORIO = 'Campo Obbligatorio';
  static APPROVA = 'Approva';
  static RIFIUTA = 'Rifiuta';
  static SCARICA = 'Scarica';
  static ESITO_CONTROLLO = 'Esito Controllo';
  static SCADENZE = 'Scadenze';
  static CHIUDI = 'CHIUDI';
  static ESTRAI = 'Estrai';
  static AGGIORNA = 'Aggiorna';
  static AGGIORNA_DATI = 'Aggiorna dati';
  static INVIA = 'Invia';
  static RIFIUTA_DOMANDA = 'Rifiuta domanda';
  // -------------------------

  // Grado Paretela
  static figlio = 'Figlio';
  static altro = 'Altro';
  static coniuge = 'Coniuge';

  static richiestaCertificazione = 'RICHIESTA CERTIFICAZIONE ANTIMAFIA';
  static datiAcquisiti = 'Dati acquisiti dalla camera di commercio';
  static datiAcquisitiTrib =
    `Dati acquisiti dalla camera di commercio e dall'anafrica tributaria`;
  static familiariConvinentiTitle =
    `Elenco dei Soggetti con carica presenti nell'organico aziendale`;
  // ---------------------------

  // Cruscotto informazioni
  static domanda = 'Domanda';
  static idDichRettifica = 'ID';
  static dataPresentazione = 'Data Presentazione';
  static nrProtocollo = 'Nr. Protocollo';
  static esitoTrasmissione = 'Esito trasmissione';
  static datiSintesiImpresaAgricola = 'Dati Sintesi Impresa Agricola';
  static dettaglio = 'Dettaglio';
  static istruttoriaCertificazioneAntimafia =
    'Istruttoria Certificazione Antimafia';
  static certificazioneAntimafia = 'CERTIFICAZIONE ANTIMAFIA';
  static consultazioneCertificazioneAntimafia =
    'Consultazione Certificazione Antimafia';
  static noTableContent =
    'Non sono presenti domande in questo stato';
  static noStoricoContent =
    'Non sono presenti istruttorie chiuse nello storico dell\'utente';
  static misura = 'Misura';
  static idDomanda = 'ID Domanda';
  static dtDomanda = 'Data Domanda';
  static campagna = 'Campagna';
  static anno = 'Anno';
  static tipo = 'Tipo';
  static importoRichiesto = 'Importo Richiesto';
  static dtBdnaOp = 'Data BDNA OP';
  static dtInizioSilenzioAssenso = 'Data Inizio Silenzio Assenso';
  static dtFineSilenzioAssenso = 'Data Fine Silenzio Assenso';
  static dtInizioEsitoNegativo = 'Data Inizio Esito Negativo';
  static dtFineEsitoNegativo = 'Data Fine Esito Negativo';
  static dtBdna = 'Data BDNA';
  static protocollo = 'Protocollo BDNA';
  static statoBdna = 'Stato BDNA';
  static domandeCollegateAntimafia = 'Domande di Contributo Collegate alla Certificazione Antimafia';
  static descAziendaDomandeCollegateAntimafia = 'Richiesta Certificazione Antimafia per l\' Azienda Agricola ';
  static DOMANDE_COLLEGATE = 'Domande Collegate';
  static ESITO_NEGATIVO = 'Esito Negativo';
  static ACCETTATO_BDNA = 'Accettato BDNA';
  static ANOMALIA_BDNA = 'Anomalia BDNA';
  static RIFIUTATA_OP = 'Rifiutata OP';
  // ---------------------------

  // Breadcrumb e titoli
  static DIPENDENTE_CAA = 'Dipendente CAA';
  static DIPENDENTE_PAT = 'Dipendente PAT';
  static DIPENDENTE_PAT_VISUALIZZATORE = 'Operatore PAT - Visualizzatore';
  static DIPENDENTE_PAT_RESPONSABILE_FASCICOLO = 'Responsabile fascicolo PAT Appag';
  static DIPENDENTE_PAT_ISTRUTTORE_UMA = 'Operatore PAT - Istruttore UMA';
  static DIPENDENTE_DOGANE = 'Dipendente Agenzia delle Dogane - Consultazione UMA';
  static DIPENDENTE_ALTRI_ENTI = 'Utenza di un ente differente da CAA';
  static DIPENDENTE_VISUALIZZATORE = 'Operatore Altro Ente';
  static DISTRIBUTORE = 'Distributore';
  static PRELIEVI_CARBURANTE = 'Prelievi carburante';
  static RICERCA_AZIENDE = 'Ricerca Aziende';
  static VALIDARE_PRELIEVI = 'Validare Prelievi';
  static GESTIONE_CONSEGNE = 'Gestione Consegne';
  static GESTIONE_AZIENDA = 'Gestione Azienda';
  static FASCICOLO_AZIENDALE = 'Fascicolo Aziendale';
  static FASCICOLO_MACCHINARI = 'Macchinari';
  static TITOLI_CONDUZIONE = 'Titoli conduzione';
  static FASCICOLO_FABBRICATI = 'Fabbricati';
  static FASCICOLO_DETTAGLIO = 'Dettaglio Fascicolo';
  static FASCICOLO_DETTAGLIO_DATI_AZIENDA = 'Dati azienda';
  static FASCICOLO_DETTAGLIO_PERSONA_FISICA = 'Dettaglio persona fisica con carica';
  static FASCICOLO_DETTAGLIO_EREDE = 'Erede';
  static FASCICOLO_DETTAGLIO_MODALITA_PAGAMENTO = 'Modalità pagamento';
  static FASCICOLO_DETTAGLIO_ALLEVAMENTI = 'Allevamenti';
  static FASCICOLO_DETTAGLIO_MACCHINARI = 'Dettaglio macchinari';
  static FASCICOLO_DETTAGLIO_FABBRICATI = 'Dettaglio fabbricati';
  static ISTRUTTORIA_ANTIMAFIA = 'Istruttoria Antimafia';
  static CONSULTAZIONE_UTENTI_UMA = 'Consultazione Utenti UMA';
  static CONSULTAZIONE_UMA = 'Consultazione richieste di carburante';
  static PAC_14_20 = 'Istruttoria PAC 2014-2020';
  static UTENTI = 'Utente';
  static CONSULTAZIONE_ANTIMAFIA = 'Consultazione Antimafia';
  static ISTRUTTORIA_DISACCOPPIATO = 'Disaccoppiato';
  static ISTRUTTORIA_ACCOPPIATO_SUPERFICIE = 'Accoppiato Superficie';
  static ISTRUTTORIA_ACCOPPIATO_ZOOTECNIA = 'Accoppiato Zootecnia';
  static DETTAGLIO_DOMANDA = 'Dettaglio Domanda';
  static GESTIONE_UTENTI = 'Gestione Utenti';
  static GESTIONE_UTENZE = 'Gestione Utenze';
  static RICERCA_UTENTI = 'Ricerca Utenti';
  static RICHIESTE_REVOCA_MANDATO = 'Richieste revoca mandato';
  static RICHIESTA_REVOCA_MANDATO_DETTAGLIO = 'Dettaglio richiesta revoca mandato';
  static DETTAGLIO_UTENZE = 'Dettaglio Utenza';
  static STORICO_UTENZE = 'Storico Utenza';
  static DOMANDE_UNICHE = 'Domande Uniche';
  static DOMANDE_STRUTTURALI_EU = 'Domande Strutturali EU';
  static DOMANDE_SUPERFICIE_EU = 'Domande Superficie EU';
  static DOMANDA_INTEGRATIVA = 'Domanda Integrativa';
  static DOMANDE_ANTIMAFIA = 'Domande Antimafia';
  static FASCICOLI_VALIDATI = 'Fascicoli Validati';
  static DATI_SOSPENSIONE = 'Dati sospensione';
  // -------------------------

  // istruttoria antimafia buttons
  static ACQUISIZIONE_DATI_DOMANDE = 'Acquisizione dati domande';
  static IMPORTA_DATI_PSR_SUPERFICIE_EU = 'Importa Dati PSR Superficie EU ';
  static IMPORTA_DATI_PSR_STRUTTURALI_EU = 'Importa Dati PSR Strutturali EU';
  static IMPORTA_DATI_DOMANDA_UNICA = 'Importa Dati Domanda Unica     ';
  static ESPORTATIONI_MASSIVE_DATI_BDNA = 'Esportazioni Massive Dati BDNA';
  static ESPORTA_DATI_PSR_SUPERFICIE_EU = 'Esporta Dati PSR Superficie EU';
  static ESPORTA_DATI_PSR_STRUTTURALI_EU = 'Esporta Dati PSR Strutturali EU';
  static ESPORTA_DATI_DOMANDA_UNICA = 'Esporta Dati Domanda Unica     ';
  static CONFERMA_IMPORTAZIONE_BDNA = 'Conferma Importazione BDNA';
  static IMPORTANZIONE_ESITI_BDNA = 'Esiti BDNA';
  static ESPORTA_ESITI_BDNA = 'Esporta Esiti BDNA';
  static IMPORTA_ESITI_BDNA = 'Importa Esiti BDNA';
  static PSR_STRUTTURALI_EU = 'PSR Strutturali EU';
  static DOMANDA_UNICA = 'DOMANDA UNICA';
  static PSR_SUPERFICIE_EU = 'PSR Superficie EU';
  // ---------------------------

  // STATI DICHIARAZIONE
  static NESSUN_PROCESSO_DI_CONTROLLO = 'Non ci sono in corso processi di controllo';
  static CONTROLLO_CERTIFICAZIONE_ANTIMAFIA_CONTROLLO_MANUALE = 'Domande Certificazione Antimafia da controllare manualmente';
  static CERTIFICAZIONI_ANTIMAFIA_RIFIUTATE = 'Domande Certificazione Antimafia Rifiutate';
  static DETTAGLIO_RIFIUTO = 'Dettaglio Rifiuto';
  static ESITO_POSITIVO = 'Esito positivo';
  static COMUNICAZIONE_NR_PROTOCOLLO = 'Comunicazione Nr. Protocollo';
  static NOTE_DI_CHIUSURA = 'Note di chiusura';
  static CERTIFICAZIONI_ANTIMAFIA_POSITIVE = 'Domande Certificazione Antimafia Esito Positivo';
  static CERTIFICAZIONI_ANTIMAFIA_VERIFICA_PERIODICA = 'Domande Certificazione Antimafia in Verifica Periodica';
  static CERTIFICAZIONE_ANTIMAFIA_BOZZA = 'Domande Certificazione Antimafia in Bozza';
  // ---------------------------

  // allevamenti capi
  static marcaAuricolare = 'Marca Auricolare Capo';
  static codiceIntervento = 'Codice Intervento';
  static codiceSpecie = 'Codice Specie';
  static descrizioneBreve = 'Descrizione Breve';
  static messaggioEsito = 'Dettaglio Esito Controllo';
  static duplicato = 'Duplicato';
  static controlloNonSuperato = 'Controllo Non Superato';
  static richiesto = 'Richiesto';
  static razza = 'Razza';
  // static dettaglioControllo = 'Dettaglio Controllo';

  // Istruttoria Domanda Unica
  static STAMPA_VERBALE_ISTRUTTORIA = 'Verbale Istruttoria';

  // istruttoria zootecnia
  static NON_AMMISSIBILE = 'Non ammissibile';
  static CALCOLO_PREMIO = 'Calcolo Premio';
  static CONTROLLI_LIQUIDABILITA = 'Controlli liquidabilità';
  static LIQUIDAZIONE = 'Liquidazione';
  static NESSUNA_DOMANDA_TROVATA = 'Nessuna domanda trovata';
  static NUMERO_DOMANDA = 'Numero domanda';
  static FILTRA_PER = 'Filtra Per';
  static SELEZIONA_SETTORE = 'Seleziona settore/i';
  static RISULTATI = 'Risultati: ';
  static APPLICA_FILTRI = 'APPLICA FILTRI';
  static SELEZIONATE = 'Selezionate: ';
  static DETTAGLI = 'Dettagli';
  static SETTORE = 'Settore';
  static AVVIA_CTRL_LIQUIDAZIONE = 'Avvia controlli liquidazione';
  static IDENTIFICATIVO_DOMANDA_INTEGRATIVA = 'Identificativo Domanda Integrativa';
  static DATA_ULTIMO_AGGIORNAMENTO_DOMANDA_INTEGRATIVA = 'Data Ultimo Aggiornamento Domanda Integrativa';
  static DATI_AGGREGATI = 'Dati aggregati';
  static DATI_CAPI_AGEA = 'Dati capi agea';
  static AVVIA_CONTROLLI_INTERSOSTEGNO = 'CREA ELENCO LIQUIDAZIONE';
  static ELENCO_LIQUIDAZIONE = 'Elenco Liquidazione';
  static IN_ISTRUTTORIA = 'In Istruttoria';
  static CAPI_PER_INTERVENTO = 'Capi per intervento';
  static RICERCA_DU = 'Ricerca DU';

  static tipoDomanda = 'Tipo domanda';
  static ibanDomanda = 'IBAN domanda';
  static ibanFascicolo = 'IBAN fascicolo';

  static APPROVATE = 'Approvate';

  static contatti = 'Contatti';
  static dettaglioDomanda = 'Visualizza dettaglio domanda';
  static dettaglioRevoca = 'Visualizza dettaglio revoca';
  static presaInCarico = 'Prendi in carico la domanda';
  static configurato = 'Configurato';
  static dtProtocollazione = 'Protocollato il';
  static dtApprovazione = 'Approvato il';
  static dtRifiuto = 'Rifiutato il';
  static dettaglioRifiuto = 'Dettaglio rifiuto';
  static dettaglioApprovazione = 'Dettaglio approvazione';
  static approvaDomanda = 'Approva la domanda';
  static rifiutaDomanda = 'Rifiuta la domanda';
  static dataProtocollazione = 'Data protocollazione';

  static sizePerPagina = [5, 10, 20, 50, 100, 200];
  static defaultPageSizePaginator = 10;
  static defaultStartIndexPaginator = 0;

  // stati richieste accesso sistema
  static protocollata = 'PROTOCOLLATA';
  static in_protocollazione = 'IN_PROTOCOLLAZIONE';
  static in_lavorazione = 'IN_LAVORAZIONE';
  static rifiutata = 'RIFIUTATA';
  static approvata = 'APPROVATA';
  // -----------------------------

  // configurazione utente
  static configurazioneProfili = 'Configurazione Profili';
  static caaSediAst = 'Caa/Sedi *';
  static UTENZA_TECNICA = 'Utenza Tecnica';
  static nuovoSistemaInformativoAgricoltura = 'A4G - Nuovo sistema Informativo Agricoltura';
  static misureStrutturali = 'SRTrento - Misure strutturali';
  static sistemaInformativoAgricoltura = 'AGS - Sistema Informativo Agricoltura';
  // -----------------------------

  static OPE = 'Operatore';
  static SEDI_CUAA_DIST = 'Sedi/Cuaa/Distributori';
  static PROF = 'Profili';
  static VAR_RICH_MOT_DISATT = 'Motivazione alle attività svolte / Motivazione sospensione';
  static SEDI = 'Sedi';
  static CUAA = 'Cuaa';
  static DIST = 'Distributori';

  // Storico Revoche Immediate
  static elencoVuotoRichiestaRevocaValutate = `Non sono presenti revoche immediate valutate`;

  // Inserimento Revoca Immediata
  static inserimentoRevocaImmediata = `La revoca del mandato è possibile solo dal 1 gennaio al 30 aprile`;

  // Revoca immediata: download documento
  static downloadDocumentoRevocaImmediata = `Download`;

  // Responsabilita' utente distributore
  static distributorTitle = 'Distributore/Pompa';
  static authorization = 'Autorizzazione';
  static warning = 'Attenzione';
  static distributorFirstWarning = `Selezionare la denominazione del Distributore di appartenenza e
  il nominativo del Responsabile che autorizza la richiesta.`;
  static distributorSecondWarning = `Scaricare e compilare il modulo per l'autorizzazione (premere il
  pulsante Scarica Modello Autorizzazione).`;
  static distributorThirdWarning = `Importare tale documento firmato dal Responsabile con allegato,
  in formato PDF, il documento d'identità dello stesso (premere il
  pulsante Carica Autorizzazione Firmata).`;
  static downloadAuthorization = 'Scarica Modello Autorizzazione';
  static uploadAuthorization = 'Carica Autorizzazione Firmata';
  static delete = 'Elimina';
  static selectField = 'Seleziona un campo';
  static companyDescription = 'Descrizione impresa';
  static applicationId = 'ID Domanda';

}
