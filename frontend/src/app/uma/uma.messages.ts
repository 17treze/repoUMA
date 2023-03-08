export const UMA_MESSAGES =
{
    // Messaggi di successo
    salvataggioOK: 'I dati sono stati salvati correttamente!',
    salvataggioOKImpostazioni: 'Le impostazioni sono state salvate correttamente!',
    aggiornamentoOK: 'I dati sono stati aggiornati correttamente!',
    salvataggioOKLavorazioniSuperficie: 'Le lavorazioni sono state salvate correttamente!',
    salvataggioOKFabbisogno: 'I dati sono stati salvati correttamente!',
    salvataggioOKConsumiLavorazioni: 'I dati sono stati salvati',
    cancellazioneDomandaOK: 'La richiesta di carburante è stata eliminata dal sistema!',
    cancellazioneTrasferimentoOK: 'Il trasferimento di carburante è stato eliminato dal sistema!',
    cancellazionePrelievoOK: 'Il prelievo è stato eliminato dal sistema!',
    cancellazioneDichiarazioneConsumiOK: 'La Dichiarazione consumi è stata eliminata dal sistema!',
    cancellazioneClienteContoTerziOK: 'I dati sono stati eliminati correttamente!',
    salvataggioAllegatiConsumiOk: 'Gli allegati sono stati salvati correttamente!',
    salvataggioConsuntiviOk: 'I dati relativi alla dichiarazione sono stato salvati correttamente',
    downloadElenchiOk: 'I dati sono stati scaricati correttamente',
    deleteConsuntivoOk: 'I dati relativi al consuntivo sono stato eliminati correttamente',
    protocollaRichiestaOK: 'La Richiesta di carburante è stata creata correttamente. La protocollazione è stata presa in carico e verrà elaborata a breve',
    protocollaDichiarazioneOK: 'La Dichiarazione consumi è stata creata correttamente. La protocollazione è stata presa in carico e verrà elaborata a breve',
    fabbisognoDatiEliminati: `I dati relativi al fabbisogno sono stati eliminati correttamente`,

    // Messaggi di errore
    deleteConsuntivoKO: 'Non è possibile reperire l\'id del consuntivo da eliminare',
    confermaCancellazioneTrasferimento: 'Attenzione! Si è sicuri di voler procedere con la cancellazione del trasferimento di carburante?',
    confermaCancellazioneDomanda: 'Attenzione! Si è sicuri di voler procedere con la cancellazione della Richiesta di carburante?',
    confermaCancellazioneDichiarazioneConsumi: 'Attenzione! Si è sicuri di voler procedere con la cancellazione della Dichiarazione consumi?',
    noLavorazioniDisponibili: `Attenzione! Il cliente non ha colture nel fascicolo`,
    macchineNonSalvate: `Attenzione! I dati delle macchine selezionate non sono stati salvati, si desidera comunque procedere?`,
    accisaObbligatoria: `E’ necessario comunicare all’Agenzia delle Dogane e dei Monopoli il quantitativo di carburante in accisa e la motivazione dell’accisa`,
    cambioDataConduzione: `Si desidera procedere con l’aggiornamento delle informazioni alla data selezionata?`,
    macchineNotSelezionate: `Per poter procedere è necessario attivare almeno una macchina`,
    erroreStampaRichiesta: 'Il documento non può essere scaricato, si prega di riprovare più tardi',
    erroreProtocollaRichiesta: 'Il documento non può essere protocollato, si prega di riprovare più tardi',
    esisteDichiarazioneConsumiAutorizzata: 'Attenzione! Esiste una dichiarazione consumi protocollata per questa richiesta di carburante',
    richiesteAutorizzateNotPresent: 'Nessuna richiesta autorizzata presente',
    richiesteNotPresent: 'Nessuna richiesta presente',
    richiesteAutorizzateGtOne: 'Esiste più di una richiesta autorizzata',
    rettificaIncompilazione: 'L‘Azienda ha una Rettifica di carburante IN COMPILAZIONE. Per procedere è necessario:\n\n protocollare la Dichiarazione consumi - premere il pulsante OK (verrà automaticamente cancellata la Rettifica) \n\n oppure \n\n completare la Rettifica di carburante - premere il pulsante ANNULLA (verrà automaticamente cancellata la Dichiarazione consumi)',
    richiestaObsoleta: 'La Dichiarazione consumi in compilazione si riferisce ad una Richiesta di carburante obsoleta.\n\n E’ necessario eliminarla e procedere con l’inserimento di una nuova Dichiarazione. \n\n Premendo il pulsante OK verrà automaticamente cancellata l’attuale Dichiarazione consumi.',
    erroreModificaCarburanteInEsubero: 'Non è possibile gestire il carburante in esubero!',
    erroreCarburanteInEsubero: 'Non è presente il carburante in esubero!',
    erroreRichiestaNonValida: 'Id richiesta non valido',
    erroreTrasferimentoPresente: 'Attenzione! Non è possibile effettuare più di un trasferimento di carburante',
    prelieviNotDisponibile: 'I prelievi non sono disponibili',
    dichiarazioneAnnoPrecedenteNotDisponibile: 'La dichiarazione consumi dell\'anno precedente non è disponibile',
    residuoDichiarazioneAnnoPrecedenteNotDisponibile: 'Il residuo associato alla dichiarazione dell\'anno precedente non è disponibile',
    cancellazioneTrasferimentoKO: 'Non è possibile cancellare questo trasferimento di carburante',
    modificaTrasferimentoKO: 'Non è possibile modificare questo trasferimento di carburante',
    aziendaDestinatariaTrasferimentoKO: 'L\'azienda agricola selezionata non può ricevere carburante',
    protocollaInformativaPrivacyError: 'Errore nella protocollazione del documento Informativa Privacy',

    // Messaggi di controlli di obbligatorietà 
    mandatory: 'Campo obbligatorio',
    datiNonValidi: `E' necessario correggere il dato inserito per effettuare il salvataggio`,
    noteRettificaObbligatorio: `Attenzione! E’ necessario compilare il campo Note indicando le motivazioni che hanno portato alla creazione della Rettifica di carburante.`,
    raggruppamentiNotSelezionati: `Per poter procedere è necessario selezionare almeno un raggruppamento`,
    richiestoAssegnatoObbligatorio: 'Attenzione! E’ necessario compilare almeno un campo del record Richiesto/Assegnato',
    motivazioneNotSeleziona: 'Per poter procedere è necessario selezionare almeno una motivazione',
    consumatoObbligatorio: `Attenzione! E’ necessario compilare il record Consumato`,
    validaProtocollaConsumiKO: 'Attenzione! Per poter procedere è necessario compilare il record consumato nella sezione Dichiarazione consumi',
    prelevabileObbligatorio: 'Attenzione! E’ necessario digitare la quantità di carburante prelevata',
    prelevabileBenzinaLtDisponibile: 'La quantità di benzina prelevata non può superare il quantitativo prelevabile',
    prelevabileGasolioLtDisponibile: 'La quantità di gasolio prelevato non può superare il quantitativo prelevabile',
    prelevabileGasolioSerreLtDisponibile: 'La quantità di gasolio serre prelevato non può superare il quantitativo prelevabile',
    trasferimentoObbligatorio: 'Attenzione! E’ necessario digitare la quantità di carburante trasferita',
    trasferimentoBenzinaLtError: 'La quantità digitata di benzina eccede i litri di carburante che è possibile trasferire!',
    trasferimentoGasolioLtError: 'La quantità digitata di gasolio eccede i litri di carburante che è possibile trasferire!',
    trasferimentoGasolioSerreLtError: 'La quantità digitata di gasolio serre eccede i litri di carburante che è possibile trasferire!',

    // Messaggi di info/warning
    noRichiedenteUmaSelezionato: 'Nessun richiedente selezionato',
    noRettificaDisponibile: 'Nessuna rettifica possibile. Non esistono richieste Autorizzate!',
    noDistributoreUmaSelezionato: 'E’ necessario selezionare il Distributore',
    noParametriRicerca: `Attenzione! E’ necessario compilare un filtro di ricerca`,
    macchineNotPresenti: `Per poter procedere è necessario salvare almeno una macchina`,
    macchineBenzinaRimosse: `Attenzione! I dati imputati nei campi Benzina verranno eliminati, si desidera comunque procedere?`,
    macchineGasolioRimosse: `Attenzione! I dati imputati nei campi Gasolio verranno eliminati, si desidera comunque procedere?`,
    fabbisognoDatiNonSalvati: `Attenzione! I dati imputati non sono stati salvati, per procedere è necessario prima salvare`,
    datiNonSalvati: `Attenzione! I dati imputati non sono stati salvati, si desidera comunque procedere?`,
    superficieDichiarataGtSupMax: 'Attenzione! E’ già stato richiesto carburante per questa coltura: è necessario effettuare un controllo. <br> '+
    'Il Centro di Assistenza Agricola di riferimento è <br> ',
    superficieDichiarataGtSupMax2: ' <br><br> Si desidera comunque salvare i dati inseriti? <br>',
    datiFabbisognoEliminazioneAlert: `Attenzione! I dati relativi al fabbisogno saranno eliminati, si desidera comunque procedere?`,
    prelevabileNotPresent: `Attenzione! I dati relativi al carburante prelevabile non sono presenti`,
    datiNotCorretti: 'Verificare i dati inseriti!',
    saveTrasferimentoCarburanteWarn: 'Verificare che il destinatario del trasferimento di carburante abbia la capacità di ricevere il quantitativo impostato',
    accisaValorizzataWarn: 'Il campo Accisa è valorizzato. Si desidera comunque procedere con la protocollazione della Dichiarazione consumi?',
    prelieviOltreDataScadenza: 'Il cliente ha effettuato prelievi successivi al',
    fabbisognoZero: 'La quantità di carburante assegnato è 0. Si desidera comunque procedere con la protocollazione della Richiesta di carburante?',

    /** Errori su file */
    FILE_ERRORS: {
        EXT: 'Il formato del file caricato non è riconosciuto. Si prega di caricare un file pdf o pdf/p7m',
        EXT_PDF: 'Il formato del file caricato non è riconosciuto. Si prega di caricare un file pdf',
        SIZE: 'Il file deve avere una dimensione inferiore a 2 MB',
        FILENAME: 'Il nome del file supera il limite massimo di 50 caratteri',
        SPECIAL_CHAR: 'Il file contiene caratteri non ammessi',
        NO_FIRMA: 'll file non ha a bordo la firma',
        NO_FIRMA_VALIDA: 'La firma non è valida',
        CODICE_FISCALE_NO_MATCH: 'Il codice fiscale del firmatario non coincide con il codice fiscale del titolare/rappresentante legale',
        NO_DATI_FIRMATARIO: 'I dati del firmatario non sono presenti',
        NO_SINGLE_FIRMATARIO: 'Deve essere presente un unico firmatario',
        NO_ALLEGATI: 'Attenzione! E’ necessario allegare almeno un file'
    }

}