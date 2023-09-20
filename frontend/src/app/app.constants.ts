import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';

@Injectable()
export class Configuration {
  
  // BASE_PATH
  public ags_server = environment.backendUrl + 'ags/api/v1';
  public fascicolo_territorio_server = `${environment.backendUrl}fascicolo/territorio/api/v1`;
  public antimafia_server = environment.backendUrl + 'a4gfascicolo/api/v1';
  public a4gfascicolo_server = environment.backendUrl + 'a4gfascicolo/api/v1';
  public a4gutente_server = environment.backendUrl + 'gestioneutenti/api/v1/';

  public a4gistruttoria_server = environment.backendUrl + 'a4gistruttoria/api/v1/';
  public a4gproxy_server = environment.backendUrl + 'a4gproxy/api/v1/';
  public srt_server = environment.backendUrl + 'srt/api/v1/';
  public anagrafica_server = 'http://localhost:8888/' + 'anagrafica/api/v1'; // environment.backendUrl + 
  public anagrafica_server_tn = 'http://localhost:8088/' + 'anagrafica/api/v1'; // environment.backendUrl + 
  public anagrafe_unica_lazio = 'http://localhost:8888/anagrafeWSNew'; 
  public zootecnia_server = environment.backendUrl + 'fascicolo/zootecnia/api/v1';
  public dotazione_tecnica_server = environment.backendUrl + 'fascicolo/dotazione-tecnica/api/v1';
  public territorio_server = environment.backendUrl + 'fascicolo/territorio/api/v1';
  public a4gproxy_server_V2 = environment.backendUrl + 'a4gproxy/api/v2/';
  public mediator_server = environment.backendUrl + 'fascicolo/mediator/api/v1';
  public siap_basepath = `${environment.agsUrl}/egeosGIG/service`;
  public uma_server = 'http://localhost:9012/' + 'uma/api/v1';

  // END BASE_PATH

  public UrlGetEntiCAA = this.a4gutente_server + 'domande/EntiCAA';
  public UrlGetProfili = this.a4gutente_server + 'profili';
  public UrlGetProfiliUtente = this.a4gutente_server + 'profili/utente';
  public UrlGetDipartimentiPat = this.a4gutente_server + 'domande/dipartimenti';
  public UrlPdfAutorizzazione = this.a4gutente_server + 'domande/fileAutorizzazione'; // get e set

  public UrlCheckFascicoloValido =
    this.antimafia_server +
    '/consultazione/fascicoli/${cuaa}/controllaValidita';
  public UrlAziendaRappresentante =
    this.antimafia_server +
    '/consultazione/persone/${cfPersona}/aziende/${cuaa}/isRappresentanteLegale';

  public UrlCountDomandeDU = this.ags_server + '/domandeDU/conta?params=';
  public UrlCountDomandeDUPAC = this.ags_server + '/domandeDU/count?';
  public UrlCountDomandeStatoPAC = this.a4gistruttoria_server + 'domandaunica/count?';
  public UrlCaricaAnniCampagna = this.a4gistruttoria_server + 'istruttorie/du/anniCampagna';
  public UrlGetDomandeDU = this.ags_server + '/domandeDU/${idDomanda}';
  public UrlCountDomandePerStato =
    this.a4gistruttoria_server + 'domande/count?params=';

  public UrlAntimafia = this.antimafia_server + '/antimafia/';
  public UrlGetRicercaAntimafia =
    this.antimafia_server + '/antimafia/?params=';
  public UrlGetRicercaAntimafiaInScadenza =
    this.antimafia_server + '/antimafia/?params=${parametri}';
  public UrlAntimafiaExpansion =
    this.UrlAntimafia + '${idDichiarazioneAntimafia}?expand=';
  public UrlUploadFirma = this.antimafia_server + '/uploadFile';
  public UrlUploadProtocolla = this.antimafia_server + '/uploadFile';
  public UrlVerificaFirma = this.a4gproxy_server + 'verificafirma';
  public UrlVerificaFirmaSingola = this.UrlVerificaFirma + '/singola/${codiceFiscaleTitolareRl}';
  public UrlVerificaFirmaMultipla = this.UrlVerificaFirma + '/firmatari';
  public UrlAllegatofamiliariConviventi =
    this.UrlAntimafia +
    '${idDichiarazioneAntimafia}/allegatoFamiliariConviventi';
  public UrlProtocollaDichiarazioneAntimafia =
    this.UrlAntimafia + '${idDichiarazioneAntimafia}/protocolla';
  public UrlProcedimentiDichiarazioneAntimafia(idDichiarazioneAntimafia) {
    return this.UrlAntimafia + `${idDichiarazioneAntimafia}/procedimenti`;
  }
  public UrlAntimafiaChiudiRicreaDichiarazione(idDichiarazioneAntimafia) {
    return this.UrlAntimafia + `${idDichiarazioneAntimafia}/chiudiRicreaDichiarazione`;
  } 

  public urlAvvioProtocollazioneUtente = this.a4gutente_server + "utenti/utente/avvio-protocollazione";
  public UrlScaricadomanda = this.UrlAntimafia + '${idDichiarazioneAntimafia}/scaricaDomanda';
  public UrlStatoDichiarazioneCount = this.UrlAntimafia + 'count/?params=';
  
  // public urlGetSSO = this.a4gutente_server + 'utenti/utente';
  public urlGetSSO = this.a4gutente_server + 'utente/getInfoUtente?applicazione=UMA';
  
  public urlGetDatiAnagraficiUtente = this.a4gutente_server + 'utenti/utente/anagrafica';
  public UrlUtente = this.a4gutente_server + 'domande';
  public UrlCreaDomanda = this.a4gutente_server + 'utenti';
  public UrlPersone = this.a4gutente_server + 'persone/';
  public UrlPersonaPrivacy = this.a4gutente_server + 'persone/?params=';
  public UrlRicercaDomanda = this.a4gutente_server + 'domande/?';
  public UrlGetDomanda = this.a4gutente_server + 'domande/${id}';
  public UrlChiudiDomanda = this.a4gutente_server + 'domande/${id}/chiudi';

  public UrlGetIstruttorie = this.a4gistruttoria_server + 'istruttorie/';
  public UrlGetIstruttoria = this.a4gistruttoria_server + 'istruttorie/${id}';
  public UrlPutIstruttoria = this.a4gistruttoria_server + 'istruttorie/${id}';
  public UrlGetDatiDettaglio = this.a4gistruttoria_server + 'istruttorie/${id}/datidettaglio?params=';
  public UrlAggiornaPremi = this.a4gistruttoria_server + 'istruttorie/${id}/premi';
  public UrlAggiornaDomandaIntegrativa = this.a4gistruttoria_server + 'istruttorie/${id}/domandaintegrativa';
  public UrlGetFinestraPresentazioneDI = this.a4gistruttoria_server + 'istruttorie/${id}/domandaintegrativa?params=';
  public urlGetFileDatiCapiAgea = this.a4gistruttoria_server + 'domandeintegrative/download/capi?params=';
  public UrlDescIntervento = this.UrlGetIstruttorie + '${id}/interventi?params=';
  public urlAntimafiaCreaNota = this.antimafia_server + '/antimafia/${id}/note';
  public urlAntimafiaGetNote = this.antimafia_server + '/antimafia/${id}/note/?params=';
  public urlDownloadDichiarazioniAntimafiaCsv = this.antimafia_server + '/antimafia/csv?params=${parametri}';
  public UrlGetElencoCuaa = this.a4gistruttoria_server + 'domande/elencoCuaa/?params=';
  public UrlGetElencoCuaaFiltrati = this.a4gistruttoria_server + 'domande/elenco-cuaa-filtrati';
  public UrlGetElencoRagioneSocialeFiltrati = this.a4gistruttoria_server + 'domande/elenco-ragionesociale-filtrati';
  public UrlGetAnagrafeTributaria = this.a4gproxy_server + 'anagrafetributaria/${codiceFiscale}';
  public UrlGetPersonaFisicaAnagrafeTributaria = this.a4gproxy_server + 'anagrafetributaria/personafisica/${codiceFiscale}';
  public UrlGetAnagraficaImpresa = this.a4gproxy_server + 'anagraficaimpresa/${cuaa}';
  public UrlGetDettaglioCompletoImpresa = this.a4gproxy_server + 'anagraficaimpresa/dettagliocompleto/?params=';
  public urlSincronizzazioneAntimafia = this.a4gproxy_server + 'sincronizzazione/antimafia/${id}';
  public urlPostSincronizzazioneAntimafia = this.a4gproxy_server + 'sincronizzazione/antimafia';

  public UrlStampaPDF = this.a4gproxy_server + 'stampa/';
  public UrlGetUtentiCodiceFiscale = this.a4gutente_server + 'utenti/{codiceFiscale}';
  public UrlGetUtentiById = this.a4gutente_server + 'utenti/idutente/{id}';
  public urlIsUtenteRegistrabile = this.a4gutente_server + 'utenti/utente/isRegistrabile';
  public urlGetProfiliUtenteConnesso = this.a4gutente_server + 'utenti/utente/profili';

  public UrlAggiornaStatoBloccoDomande = this.a4gistruttoria_server + 'domande/aggiornaStatoBloccoDomande';

  public UrlRedirectUtenti = '/utenti';

  public UrlRedirectModificaUtente = '/utenti/modificaUtente';

  public UrlAvviaAccoppiatoSuperficie = this.a4gistruttoria_server + 'domande/as/avvia';
  public UrlAvviaProcessoIstruttoriaDU = `${this.a4gistruttoria_server}processi/istruttorie/du`;

  public getUrlAvviaIstruttoria(annoCampagna: number) {
    return `${this.a4gistruttoria_server}domandaunica/${annoCampagna}/istruisci`;
  }

  public UrlAvviaRicevibilitaDU = this.a4gistruttoria_server + 'domandaunica/ricevibilita/${annoCampagna}';
  public UrlAvviaPremioAcz = this.a4gistruttoria_server + 'domande/az/premio';
  public UrlAvviaControlliLiquidazione = this.a4gistruttoria_server + 'domande/${sostegno}/avvialiquidabilita';
  public UrlAvviaControlliLiquidazioneZootecnia = this.a4gistruttoria_server + 'domande/az/avvialiquidabilita';
  public UrlGetListaProcessiAttiviIstruttoria = this.a4gistruttoria_server + 'processi?params=';
  public UrlGetProcessoById = this.a4gistruttoria_server + 'processi/${id}';
  public UrlAvviaControlloAntimafia = this.a4gistruttoria_server + 'antimafia/controlla';
  public UrlEsitoControlloAntimafia = this.a4gistruttoria_server + 'antimafia/${idDichiarazioneAntimafia}';
  public urlControlloAntimafiaStatoAvanzamento = this.a4gistruttoria_server + 'antimafia/statoavanzamento?params=';

  public UrlGetListaDomandePerStatoPaginata = this.a4gistruttoria_server + 'domande/?params=${parametri}&paginazione=${paginazione}&ordinamento=${ordinamento}';
  public UrlGetListaDomandePerStato = this.a4gistruttoria_server + 'domande?params=';

  public UrlGetDomandaDettaglio = this.a4gistruttoria_server + 'domande/${id}/dettaglio';

  public UrlGetInfoDomanda = this.a4gistruttoria_server + 'domande/${id}';

  public UrlDatiErede = this.UrlGetInfoDomanda + '/eredi';

  public UrlGetDomandaParticelle = this.a4gistruttoria_server + 'domande/${id}/particelle?paginazione=${paginazione}&ordinamento=${ordinamento}';

  public UrlGetDettaglioCalcoloParticelle = this.a4gistruttoria_server + 'domande/${id}/dettaglioParticelle?params=${params}&paginazione=${paginazione}&ordinamento=${ordinamento}';

  public UrlGetDettaglioPascoli = this.a4gistruttoria_server + 'domande/${id}/dettaglioPascoli?expand=';

  public UrlDatiIstruttoriaDomanda = this.a4gistruttoria_server + 'domande' + '/${id}/datiIstruttoria/datiIstruttore';

  public UrlDatiIstruttoriaPascoliDomanda = this.a4gistruttoria_server + 'domande' + '/${id}/datiIstruttoria/datiIstruttoriaPascoli';

  public UrlGetRichiesteAllevamDu = this.a4gistruttoria_server + 'domande' + '/${idDomanda}/capi';


  public UrlGetDomandaIntegrativaById = this.a4gistruttoria_server + 'domande/${id}/domandaintegrativa';

  public UrlGetDomandaIntegrativaByCuaa = this.a4gistruttoria_server + 'domande/az/domandaintegrativa?params=';

  public UrlGetEsitoCapo = this.a4gistruttoria_server + 'domande/${id}/richiesteallevamduesito/${idRichiestaAllevamentoEsito}';

  public UrlSalvaDomandaIntegrativa = this.a4gistruttoria_server + 'domande/${id}/domandaintegrativa/salva';
  public UrlConfermaDomandaIntegrativa = this.a4gistruttoria_server + 'domande/${id}/domandaintegrativa/conferma';
  public UrlAggiornaStatoDomande = this.a4gistruttoria_server + 'domande/${sostegno}/aggiornastatodomande';
  public UrlAggiornaStatoDomandeACZ = this.a4gistruttoria_server + 'domande/az/aggiornastatodomande';

  // costante per lavorare in ambiente locale e di test
  public production = environment.production;

  public UrlGetAllFamConv = environment.frontendUrl + 'assets/templates/all3famconv.docx';
  public UrlGetDichiarazioneAntimafiaDI = environment.frontendUrl + 'assets/templates/alldicantdind.docx';
  public UrlGetDichiarazioneAntimafiaPG = environment.frontendUrl + 'assets/templates/alldicantpgiur.docx';

  // public getModuloAutorizzazioneEnti = environment.frontendUrl + 'assets/staticfiles/autorizzazioneDirigente.pdf';

  public getModuloAutorizzazioneUtentePAT = environment.frontendUrl + 'assets/staticfiles/autorizzazioneUtentePAT.pdf';

  public getModuloAutorizzazioneAltriEnti = environment.frontendUrl + 'assets/staticfiles/autorizzazioneAltriEnti.pdf';

  public getModuloAutorizzazioneConsulente = environment.frontendUrl + 'assets/staticfiles/autorizzazioneConsulente.pdf';

  // public getModuloAutorizzazioneDistributore = environment.frontendUrl + 'assets/staticfiles/autorizzazioneConsulente.pdf';

  public UrlGetInfoPrivacy = environment.frontendUrl + 'assets/staticfiles/informativa_privacy.pdf';

  public UrlGetRicevutaDomandaIntegrativaZootecnia = environment.frontendUrl + 'assets/templates/DI/ricevutaDomandaIntegrativaZootecnia.docx';

  public UrlGetTemplateMandato = environment.frontendUrl + 'assets/templates/templateMandato.docx';

  public UrlGetTemplateRevocaImmediata = environment.frontendUrl + 'assets/templates/templateRevocaImmediata.docx';

  public getUrlTemplateStampa(pathToTemplate: string) {
    return environment.frontendUrl + pathToTemplate;
  }

  //
  public UrlPutSyncBDN(annoCampagna, cuaa) {
    return `${this.a4gistruttoria_server}dettagliopascoli/${annoCampagna}/${cuaa}/sincronizzaDatiPascoloBDN`;
  }

  // SrTrento
  public UrlSrtGetRuoliPerUtente = this.srt_server + 'utenti/${codiceFiscale}/ruoli';
  public UrlRedirectSrTrento = environment.srTrentoUrl;

  // public urlGetTemplateModuloRegistrazioneUtente =
  //   environment.frontendUrl +
  //   "assets/templates/AS/templateModuloDomandaRegistrazione.docx";
  public urlDomandaRegUtenteStampa = this.a4gutente_server + 'domande/{id}/export';
  public urlDomandaRegUtenteFirma = this.a4gutente_server + 'domande/{id}/firma';
  public urlDomandaProtocolla = this.a4gutente_server + 'domande/{id}/protocolla';
  public urlFirmaUtente = this.a4gutente_server + 'utenti/utente/firma';

  public FrontendUrl = environment.frontendUrl;

  // URL istruttoria antimafia
  public urlControlloDomandeCollegate = this.a4gistruttoria_server + 'antimafia/domandecollegate/controlla';
  public urlIstruttoriaAntimafiaImportaDatiDU = this.a4gistruttoria_server + 'antimafia/importadatidu';
  public urlIstruttoriaAntimafiaImportaDatiStrutturali = this.a4gistruttoria_server + 'antimafia/importadatistrutturali';
  public urlIstruttoriaAntimafiaImportaDatiSuperficie = this.a4gistruttoria_server + 'antimafia/importadatisuperficie';
  public urlIstruttoriaAntimafiaGetCertificazioni = this.a4gistruttoria_server + 'antimafia/certificazioni?';
  public urlIstruttoriaAntimafiaPostEsportaCSV = this.a4gistruttoria_server + 'antimafia/domandecollegate/trasmissione';
  public urlIstruttoriaAntimafiaGetDomandeCollegate = this.a4gistruttoria_server + 'antimafia/domandecollegate/{id}';
  public urlIstruttoriaAntimafiaConfermaDomandeCollegate = this.a4gistruttoria_server + 'antimafia/domandecollegate/conferma';
  public urlIstruttoriaAntimafiaAnnullaDomandeCollegate = this.a4gistruttoria_server + 'antimafia/domandecollegate/annulla';
  public urlIstruttoriaAntimafiGetDomandeCollegate = this.a4gistruttoria_server + 'antimafia/domandecollegate';
  public urlIstruttoriaAntimafiaImportaEsitiBdna = this.a4gistruttoria_server + 'antimafia/importaesitibdna';
  public urlIstruttoriaAntimafiaExportEsitiBdna = this.a4gistruttoria_server + 'antimafia/domandecollegate/esportaBDNA';
  public urlTrasmissioneBdna = this.a4gistruttoria_server + 'antimafia/domandecollegate/trasmissione';
  public urlPutTrasmissioneBdna = this.a4gistruttoria_server + 'antimafia/domandecollegate/trasmissione/{id}';
  public urlDeleteTrasmissioneBdna = this.a4gistruttoria_server + 'antimafia/domandecollegate/trasmissione/{id}';
  public urlDownloadCsv = this.a4gistruttoria_server + 'antimafia/domandecollegate/trasmissione/{id}/esporta';
  public urlGetDichiarazioneByStato = this.antimafia_server + '/antimafia/page?';


  // URL AccoppiatoZotecnia
  public UrlDomandeAvvioAccoppiatoZootecnia = this.a4gistruttoria_server + 'domande/az/avvia';
  public urlGetDomandeAccoppiatoZootecniaFiltrate = this.a4gistruttoria_server + 'domande/az';
  public urlGetCounterDomandeAccoppiatoZootecnia = this.a4gistruttoria_server + 'domande/az/count';
  // Ags
  public UrlAgsGetUtenzePerUtente =
    this.ags_server + '/utente/utenze';
  public UrlRedirectAgs = environment.agsUrl;

  public urlAgsPutEsitiAntimafia = this.ags_server + '/esiti/antimafia';

  // Costanti per protocollo
  public ProtocollaPrivacyOggetto = 'A4G - INFORMATIVA GENERALE PRIVACY';
  public tipologiaDocumentoProtocolloPrivacy = 'PRIVACY';

  public TipoLogin = environment.tipoLogin;
  public TipoLoginCittadino = 'cittadino';
  public TipoLoginDipendente = 'dipendente';
  public IndexPage = environment.indexPage;

  // URL stampa domanda istruttoria

  public urlVerbaleIstruttoria(idIstruttoria) {
    return `${this.a4gistruttoria_server}istruttorie/du/disaccoppiato/${idIstruttoria}/verbale`;
  }

  public urlVerbaleIstruttoriaAcz(idIstruttoria) {
    return `${this.a4gistruttoria_server}istruttorie/du/zootecnia/${idIstruttoria}/verbale`;
  }


  public urlIstruttoriaLiquidazioneVerbale(idElencoLiquidazione: number) {
    return `${this.a4gistruttoria_server}istruttorie/du/liquidazione/${idElencoLiquidazione}/verbale`;
  }

  public StampaElencoLiquidazione = this.a4gistruttoria_server + 'domande/stampaElencoLiquidazione';

  // URL recupero dettaglio domanda ACS ACZ
  public UrlGetDettaglioDomandaAccoppiati = this.a4gistruttoria_server + 'domande/${id}/${sostegno}?expand=${dettaglio}';
  public UrlPutDettaglioDatiIstruttoriaACS = this.a4gistruttoria_server + 'domande/${id}/datiIstruttoria/datiIstruttoriaSuperficie';
  public UrlPutDettaglioDatiIstruttoriaACZ = this.a4gistruttoria_server + 'domande/${id}/datiIstruttoria/datiIstruttoriaZootecnia';

  public UrlDomandaIntegrativa = this.a4gistruttoria_server + 'domandeintegrative/${idDomanda}/ricevuta';
  public UrlEliminaFileCaricato = this.a4gistruttoria_server + 'domandeintegrative/${idDomanda}/ricevuta';

  // Produzione Latte e Registrazione Alpeggio
  public UrlGetProduzioneLatte = this.a4gistruttoria_server + 'istruttorie/${id}/produzionelatte?params=';
  public UrlGetRegistrazioneAlpeggio = this.a4gistruttoria_server + 'istruttorie/${id}/alpeggio?params=';
  public UrlGetRegistrazioneAlpeggioZootecnia = this.a4gistruttoria_server + 'istruttorie/du/zootecnia/${annoCampagna}/alpeggio';
  public UrlGetProduzioneLatteZootecnia = this.a4gistruttoria_server + 'istruttorie/du/zootecnia/${annoCampagna}/produzionelatte';
  public UrlGetEtichettaturaZootecnia = this.a4gistruttoria_server + 'istruttorie/du/zootecnia/${annoCampagna}/etichettaturacarne';

  // Etichettatura
  public UrlGetEtichettatura = this.a4gistruttoria_server + 'istruttorie/etichettaturacarne?params=';

  public UrlAvviaControlliIntersostegnoACZ = this.a4gistruttoria_server + 'domande/az/avviacontrollointersostegno';
  public UrlAvviaControlliIntersostegnoACS = this.a4gistruttoria_server + 'domande/as/avviacontrollointersostegno';

  public StampaRicevutaDomandaIntegrativa = this.a4gistruttoria_server + 'domandeintegrative/stampaRicevutaDomandaIntegrativa';

  public UrlAvviaStatistiche = this.a4gistruttoria_server + 'statistiche';

  public UrlAvviaSincronizzazione = this.a4gistruttoria_server + 'sincronizzazione';

  public urlSogliaAcqusizione = this.a4gistruttoria_server + 'antimafia/soglie';

  public UrlAnnullaIstruttoria = (idDomanda: string) => this.a4gistruttoria_server + `domandaunica/${idDomanda}/annulla`;

  public urlDomandaunica = this.a4gistruttoria_server + 'domandaunica';

  public UrlGetDomandeIbanErrato = this.a4gistruttoria_server + 'domande/ibanerrato';

  //Domande registrazione utente
  public UrlGetDomandaRegistrazioneUtente = this.a4gutente_server + "domande/{id}";
  public UrlStatoRichiestaCount = this.a4gutente_server + 'domande/counters/';
  public UrlPresaInCarico = this.a4gutente_server + 'domande/${id}/presaincarico';

  public UrlIstruttoriaByDomanda = this.a4gutente_server + 'istruttoria/domanda';
  public UrlGetIstruttoriaByDomanda = this.UrlIstruttoriaByDomanda + '/${id}';
  public UrlApprovaDomanda = this.a4gutente_server + 'domande/${id}/approva';
  public UrlRifiutaDomanda = this.a4gutente_server + 'domande/${id}/rifiuta';
  public UrlPostIstruttoriaUtenteByIdUtente = this.a4gutente_server + 'istruttoria/utente/${idUtente}';
  public UrlGetStoricoIstruttoria = this.UrlPostIstruttoriaUtenteByIdUtente + '/storico';

  // V2
  public UrlGetAnagraficaImpresa_V2 = this.UrlGetAnagraficaImpresa.replace(this.a4gproxy_server, this.a4gproxy_server_V2);
  public UrlGetAnagrafeTributaria_V2 = this.UrlGetAnagrafeTributaria.replace(this.a4gproxy_server, this.a4gproxy_server_V2);
  public UrlGetPersonaFisicaAnagrafeTributaria_V2 = this.UrlGetPersonaFisicaAnagrafeTributaria.replace(this.a4gproxy_server, this.a4gproxy_server_V2);
  public UrlGetEnti = this.urlGetSSO + '/enti';

  //ricerca profili
  public UrlGetProfiliSrt = this.srt_server + 'utenti';
  public UrlGetProfiliAgs = this.ags_server + '/utente';
  public UrlGetProfiliA4g = this.a4gutente_server + 'utenti/ricerca';
  public UrlUtentiA4gCsv = this.a4gutente_server + 'utenti/utentia4gcsv';

  public UrlGetRicercaIstruttorieDU = this.a4gistruttoria_server + 'istruttorie/du';

  public UrlGetDettagliDU = this.a4gistruttoria_server + 'domandaunica/${id}/dichiarazioni';
  public UrlGetInformazioniDU = this.a4gistruttoria_server + 'domandaunica/disaccoppiato/${id}/informazioni';
  public UrlGetDatiParticellaIsNotPascoloDU = this.a4gistruttoria_server + 'dettaglioparticella/istruttoria/${idIstruttoria}';
  public UrlGetCSVDatiParticelleEleggibilita = this.a4gistruttoria_server + 'dettaglioparticella/istruttoria/${idIstruttoria}/csv-eleggibilita';
  public UrlGetCSVDatiParticelleGreening = this.a4gistruttoria_server + 'dettaglioparticella/istruttoria/${idIstruttoria}/csv-greening';
  public UrlGetCSVDatiParticelleMantenimento = this.a4gistruttoria_server + 'dettaglioparticella/istruttoria/${idIstruttoria}/csv-mantenimento';
  public UrlGetDatiParticellaIsPascoloDU = this.a4gistruttoria_server + 'dettaglioparticella/istruttoria/${idIstruttoria}/?pascolo=${pascolo}';
  public UrlGetSuperficiImpegnateDU = this.a4gistruttoria_server + 'domandaunica/disaccoppiato/${idDomanda}/superfici';
  public UrlAcsDU = this.a4gistruttoria_server + 'domandaunica/acs';
  public UrlAczDU = this.a4gistruttoria_server + 'domandaunica/acz';
  public UrlAcsDUSuperficie = this.a4gistruttoria_server + 'istruttorie/du/superficie';
  public UrlDettaglioPascoloIstruttoriaDU = this.a4gistruttoria_server + 'dettagliopascoli/istruttoria/';

  public getUrlDettaglioPascoloIstruttoriaConEsitoMantenimentoDU(idIstruttoria: number): string {
    return this.UrlDettaglioPascoloIstruttoriaDU + `${idIstruttoria}/conesitomantenimento`;
  }

  public UrlIstruttorieAcs = this.a4gistruttoria_server + 'istruttorie/du/superficie';
  public UrlIstruttorieAcz = this.a4gistruttoria_server + 'istruttorie/du/zootecnia';
  public UrlAggiornaCapo = this.UrlIstruttorieAcz + '/${idIstruttoria}/capi/${idCapoRichiesto}';
  public UrlIstruttorieAczGetAllevamentiImpegnati = this.a4gistruttoria_server + 'istruttorie/du/zootecnia/${idIstruttoria}/allevamenti';
  public UrlIstruttorieAczGetAllevamentiRichiesti = this.a4gistruttoria_server + 'istruttorie/du/zootecnia/${idIstruttoria}/allevamenti/richiesti';
  public UrlIstruttorieAczEsitiCalcoloCapiRicerca = this.a4gistruttoria_server + 'istruttorie/du/zootecnia/${idIstruttoria}/allevamenti/capi';
  public UrlDownloadCsvIstruttorie = this.a4gistruttoria_server + 'istruttorie/du/csv';

  public getUrlGetParticelleDettaglioSuperficiePerCalcolo(idDomanda: number, idParticella: number, codiceColtura: string): string {
    return `${this.a4gistruttoria_server}dettaglioparticella/domanda/${idDomanda}/particella/${idParticella}/codice-coltura/${codiceColtura}`;
  }

  public urlGetFileDatiCapiAgeaNew(annoCampagna: number) {
    return `${this.a4gistruttoria_server}istruttorie/du/zootecnia/${annoCampagna}/allevamenti/capi/impegnati`;
  }

  public getUrlStatisticheZootecnia(annoCampagna: number) {
    return `${this.a4gistruttoria_server}istruttorie/du/zootecnia/${annoCampagna}/allevamenti/capi/interventi/count`;
  }

  public urlDettaglioPascoloDUcondatiIstruttoria(idIstruttoria) {
    return this.UrlDettaglioPascoloIstruttoriaDU + `${idIstruttoria}/condatiistruttoria`;
  }

  public UrlGetProcessiDiControllo = this.a4gistruttoria_server + 'processi/istruttorie/du/inesecuzione';
  public UrlDettaglioIstruttoria = this.a4gistruttoria_server + 'istruttorie/du/conf/disaccoppiato/${annoCampagna}';
  public UrlConfIstruttoriaAcs = this.a4gistruttoria_server + 'istruttorie/du/conf/acs/${annoCampagna}';
  public UrlConfIstruttoriaAcz = this.a4gistruttoria_server + 'istruttorie/du/conf/acz/${annoCampagna}';
  public UrlConfRicevibilita = this.a4gistruttoria_server + 'istruttorie/du/conf/ricevibilita/${annoCampagna}';
  public UrlConfIstruttorie = this.a4gistruttoria_server + 'istruttorie/du/conf/istruttorie/${annoCampagna}';

  public UrlGetElencoDomande = this.a4gistruttoria_server + 'domandaunica/ricerca';

  public UrlGetUltimaDomandaUtenteCorrente = this.UrlUtente + '/ultima-domanda-utente-corrente';

  public getUrlTotaleSuperficiePerIntervento(annoCampagna: number) {
    return this.UrlAcsDUSuperficie + `/${annoCampagna}/superficie-intervento`;
  }
  
  public UrlGetDistributori = this.a4gutente_server + 'domande/distributori';

  public UrlgetRicercaDomandeUniche = this.a4gistruttoria_server + 'ricercaDomande';
  public UrlgetRicercaDomandeUnicheListaStati = this.UrlgetRicercaDomandeUniche + '/listaStati';
  public UrlgetRicercaDomandeUnicheListaAnni = this.UrlgetRicercaDomandeUniche + '/listaAnni';

  public UrlCupGeneraXml = this.a4gistruttoria_server + 'cup';

}
