import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable()
export class GisCostants {
  // path per test su JSON Server
  //public path = 'http://localhost:9011/';
  //public hostRichiesta = 'http://localhost:9009' + '/richiestemodificasuolo/api/v1';

  public path = environment.backendUrl;
  public hostRichiesta = environment.backendUrl + 'richiestemodificasuolo/api/v1';
  // layer wfs
  public defaultProjection = 'EPSG:25832';
  public featureNamespace = 'http://www.opengis.net/wfs';
  public proj4Transformtion = '+proj=utm +zone=32 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs';

  // GEOSERVER
  // modifica temporanea fino alla disponibilità di più ambienti del proxy geoserver
  public geoserverUrl = environment.backendUrl.includes('localhost') || environment.backendUrl.includes('a4g-test.infotn.it')
    ? this.path + 'proxy/geoserver'
    : this.hostRichiesta + '/proxy/geoserver';
  //public geoserverUrl = this.path + 'proxy/geoserver'; //Versione post disponibilità
  public geoserverA4sWorkspace = 'app_a4s/';
  public geoserverStemWorkspace = 'pub_stem_s/';

  // COMUNI CATASTALI
  public provinciaComuni = '?siglaProvincia=TN&tipoComune=CAT';
  public getComuniCatastaliUrl = this.path + 'fascicolo/territorio/api/v1/sezione' + this.provinciaComuni;

  // RICHIESTA MODIFICA SUOLO
  public pathRichiestaModificaSuolo = this.hostRichiesta + '/richiestamodificasuolo';
  public searchRichiestaModificaSuolo = this.pathRichiestaModificaSuolo + '/';
  public putRichiestaModificaSuolo = this.pathRichiestaModificaSuolo;

  // LAVORAZIONE SUOLO
  public pathSuoloDichiarato = this.hostRichiesta + '/suolodichiarato';
  public pathLavorazioneSuolo = this.hostRichiesta + '/lavorazionesuolo';

  // LAYER
  public pathLayerController = this.hostRichiesta + '/layer';


  // PAGINAZIONE
  public numMessaggi = 10;
  public numRisultati = 10;

  // MESSAGGI TOAST
  public erroreSalvataggio = 'Errore di sistema in fase di salvataggio informazioni. Contattare l’assistenza';
  public erroreCancellazione = 'Errore di sistema in fase di cancellazione. Contattare l’assistenza';
  public successSalvataggio = 'Salvataggio riuscito';
  public noData = 'Non ci sono dati con questi filtri di ricerca';
  public noAllegato = 'Errore nel recupero dell`allegato';
  public successAllegato = 'Documento allegato correttamente';
  public successCancellazione = 'Documento eliminato correttamente';
  public erroreExt = 'Il formato del file caricato non è riconosciuto. Si prega di caricare un file pdf - png';
  public erroreSize = 'Il file deve avere una dimensione inferiore a 10 MB';
  public successNuovaLavorazione = 'Lavorazione salvata con successo';
  public errorNuovaLavorazione = 'Lavorazione non creata per problemi tecnici';
  public errorNuovaLavorazioneEsisteAltraLavorazioneVuota = 'Per creare una nuova lavorazione è necessario completare o eliminare quelle esistenti';
  public successCancellazioneLavorazione = 'Lavorazione eliminata correttamente';
  public errorServer500 = 'Problema in fase di ricerca – Contattare l`assistenza';
  public validazioneLavorazioneError = 'Per procedere è necessario selezionare almeno un poligono di suolo vigente/dichiarato';
  public errorGetLavorazioni = 'Errore nel recupero delle lavorazioni';
  public errorAvviaLavorazione = 'Errore di sistema in fase di avvio lavorazione';
  public errorPoligonoCoinvoltoInAltraLavorazione = 'Errore, il poligono selezionato è coinvolto in un`altra lavorazione';
  public errorPoligonoNonTrovato = 'Errore nessun poligono associabile trovato';
  public errorCantEditRichiestaModificaSuolo = 'Non è possibile modificare una richiesta con stato LAVORABILE';
  public errorModificaLavorazione = 'Errore durante la modifica della lavorazione';
  public successConsolidaLavorazioneSuA4S = 'Lavorazione consolidata su A4S';
  public errorConsolidaLavorazioneSuA4S = 'Errore durante il consolidamento della lavorazione';
  public successConsolidaLavorazioneSuAGS = 'Richiesta consolidamento su AGS in elaborazione';
  public messageSnapAbilitato = 'Snap abilitato';
  public messageSnapDisabilitato = 'Snap disabilitato';

  // MESSAGGI MAPPA
  public noContentIdentify = 'Non ci sono dati nel punto cliccato';
  public modifichenNonSalvate = 'Attenzione modifiche non salvate impossibile proseguire';
  public successValidazioneLavorazioneInCorso = 'Nessun errore di validazione rilevato';
  public errorValidazioneLavorazioneInCorso = 'Errore validazione rilevato';

  //MESSAGGI LAVORAZIONE
  public messageChiusuraLavorazioneSenzaSalvataggio = 'Vuoi uscire dalla lavorazione senza salvare?';
  public messageChiusuraAdlSenzaSalvataggio = "<br> Tutte le modifiche all'ADL non salvate verranno perse";
  public messageEliminareLavorazione = 'Sei sicuro di voler eliminare questa lavorazione?'
  public messageChiusuraLavorazione = 'Vuoi uscire dalla lavorazione ?';
  public messageMoficheNonSalvate =  'Sono presenti modifiche non salvate';
  public messageCambioModalitaSelezioneSuoloLavorazione = 'Attenzione: i poligoni di suolo associati alla lavorazione verranno rimossi, vuoi proseguire?';
  public messageRicalcoloPoligoniSuoloDaDichiarato = 'Attenzione: il ricalcolo dei poligoni di suolo causerà la cancellazione di tutti i poligoni di suolo già associati';  

  // Configurazioni
  public endDrawCreateVertex = false;
  public endDrawSnapTolerance = 1;
  public drawClickTolerance = 1000;
  public modifyPixelTolerance = 8;
  public snapTolerance = 8;

  // RICERCA LOCALITA
  public searchLocalitaurl = this.hostRichiesta + '/proxy?https://webgis.provincia.tn.it/wgt/services/SearchServer';
}
