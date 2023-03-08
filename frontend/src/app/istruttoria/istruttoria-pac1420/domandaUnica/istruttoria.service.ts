import { Injectable } from '@angular/core';
import { forkJoin, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Paginazione } from 'src/app/a4g-common/utility/paginazione';
import { Cached } from 'src/assets/decorators/cached';
import { Istruttoria } from '../../../a4g-common/classi/Istruttoria';
import { Configuration } from '../../../app.constants';
import { TipoProcesso } from '../../istruttoria-antimafia/dto/TipoProcesso';
import { DomandaIntegrativa } from '../domanda-integrativa/classi/DomandaIntegrativa';
import { DatiDettaglio } from './classi/DatiDettaglio';
import { DatiDomandaIbanErrato } from "./classi/DatiDomandaIbanErrato";
import { DettaglioDatiIstruttoriaACS, DettaglioDatiIstruttoriaACZ, DettaglioDomandaAccoppiati } from './classi/dettaglioDomandaAccoppiati';
import { Intervento } from './classi/Intervento';
import { InterventoDuPremio } from './classi/InterventoDuPremio';
import { IstruttoriaEdit } from './classi/istruttoria-edit';
import { IstruttoriaDomandaUnica } from './classi/IstruttoriaDomandaUnica';
import { IstruttoriaDomandaUnicaFilter } from './classi/IstruttoriaDomandaUnicaFilter';
import { IstruttoriaPagina } from './classi/IstruttoriaPagina';
import { SostegnoDuDi } from './classi/SostegnoDuDi';
import { DatiIstruttoriaPascoliDTO } from './domain/dati-istruttoria-pascoli-dto';
import { DatiErede } from './domain/datiErede';
import { DatiIstruttoria } from './domain/datiIstruttoria';
import { PaginaDettaglioCalcoloParticella } from './domain/dettaglioCalcoloParticella';
import { CapoRichiesto, DettaglioCapi } from './domain/dettaglioCapi';
import { DomandaIstruttoriaDettaglio } from './domain/domandaIstruttoriaDettaglio';
import { Domanda } from './domain/domandaUnica';
import { EtichettaturaCarne } from './domain/etichettatura-carne';
import { PaginaDomande } from './domain/paginaDomande';
import { PaginaParticellaDomanda } from './domain/particellaDomanda';
import { PascoloDao } from './domain/pascolo-dao';
import { ProduzioneLatte } from './domain/produzione-latte';
import { registrazioneAlpeggio } from './domain/registrazione-alpeggio';
import { ProcessiDiControllo } from './sostegno-shared/dto/processi-di-controllo';
import { PaginaRichiestaSuperficie } from './domain/richiestaSuperficie';
import { ProcessoIstruttoria } from './processoIstruttoria';
import { ProcessoIstruttoriaPac1420 } from './processoIstruttoria1420';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { SostegnoDu } from './classi/SostegnoDu';
import { DomandaUnicaFilter } from './classi/DomandaUnicaFilter';
import { DomandaUnica } from './sostegno-shared/dto/DomandaUnica';
import { TipoIstruttoriaEnum } from './classi/TipoIstruttoriaEnum';
import { PremioPagamentiStatoIstruttoriaNettoLordoDto } from './domain/PremioPagamentiStatoIstruttoriaNettoLordoDto';
import { CodicePac } from './classi/CodicePac';
@Injectable({
  providedIn: 'root'
})
export class IstruttoriaService {

  constructor(private http: HttpClient, private _configuration: Configuration) {}

  public getIstruttoria(idIstruttoria: number): Observable<Istruttoria> {
    console.log('getIstruttoria');
    return this.http.get<Istruttoria>(this._configuration.UrlGetIstruttoria.replace('${id}', String(idIstruttoria)));
  }

  public putIstruttoria(idIstruttoria: number, istruttoriaEdit: IstruttoriaEdit): Observable<number> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const a = JSON.stringify(istruttoriaEdit);
    return this.http.put<number>
      (this._configuration.UrlPutIstruttoria.replace('${id}',
        String(idIstruttoria)), JSON.stringify(istruttoriaEdit), { headers: headers });
  }

  public putAmmissibilita(jsonDomande: Domanda[], identificativoSostegnoDu: String, statoSostegnoInput: string) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<number>(this._configuration.UrlAvviaProcessoIstruttoriaDU + '?' + 'identificativoSostegnoDu=' +
      identificativoSostegnoDu + '&' + 'statoSostegnoInput=' + statoSostegnoInput, JSON.stringify(jsonDomande), { headers: headers });
  }

  public countDomandeDU(istruttoria: string): Observable<number> {
    console.log('countDomandeDU');
    return this.http.get<number>(this._configuration.UrlCountDomandeDU + istruttoria);
  }

  public countDomandeDUPAC(parametri: string): Observable<number> {
    console.log('countDomandeDUPAC');
    return this.http.get<number>(this._configuration.UrlCountDomandeDUPAC + parametri);
  }

  public countDomandeStatoPAC(parametri: string): Observable<number> {
    console.log('countDomandeStatoPAC');
    return this.http.get<number>(this._configuration.UrlCountDomandeStatoPAC + parametri);
  }

  @Cached()
  public caricaAnniCampagna(codicePac: CodicePac): Observable<number[]> {
    console.log('caricaAnniCampagna');
    let parametri = '?codicePac=' + codicePac;
    return this.http.get<Array<number>>(this._configuration.UrlCaricaAnniCampagna + parametri);
  }

  public ricercaIstruttorie(): Observable<Array<Istruttoria>> {
    console.log('ricercaIstruttorie');
    return this.http.get<Array<Istruttoria>>(this._configuration.UrlGetIstruttorie);
  }

  public countDomandeFiltered(jsonRequest: string): Observable<number> {
    console.log('contaDomandePerStato');
    console.log(this._configuration.UrlCountDomandePerStato + jsonRequest);
    return this.http.get<number>(this._configuration.UrlCountDomandePerStato + jsonRequest);
  }

  public avviaProcessoIstruttoriaDU(annoCampagna: number): Observable<number> {
    console.log('avviaProcesso');
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<number>(
      this._configuration.getUrlAvviaIstruttoria(annoCampagna), { headers: headers });
  }

  public avviaProcessoRicevibilitaDU(annoCampagna: number): Observable<number> {
    console.log('avviaProcesso');
    return this.http.get<number>(this._configuration.UrlAvviaRicevibilitaDU
      .replace('${annoCampagna}', String(annoCampagna)));
  }

  public avviaProcessoIstruttoriaDUByID(idIstruttorie: number[], tipoProcesso: TipoProcesso, annoCampagna?: number, sostegno?: SostegnoDu): Observable<number> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    if (tipoProcesso === TipoProcesso.LIQUIDAZIONE) {
      let obj = { idIstruttorie: idIstruttorie, tipoProcesso: tipoProcesso, campagna: annoCampagna, sostegno: sostegno };
      return this.http.post<number>(this._configuration.UrlAvviaProcessoIstruttoriaDU + '/liquidazione', obj, { headers: headers });
    } else {
      let obj = { idIstruttorie: idIstruttorie, tipoProcesso: tipoProcesso };
      return this.http.post<number>(this._configuration.UrlAvviaProcessoIstruttoriaDU, obj, { headers: headers });
    }
  }

  public avviaProcessoIstruttoriaDUByAnno(annoCampagna: number, tipoProcesso: TipoProcesso, sostegno: string): Observable<number> {
    let obj = { campagna: annoCampagna, idTipoProcesso: tipoProcesso, sostegno: sostegno };
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<number>(this._configuration.UrlAvviaProcessoIstruttoriaDU, obj, { headers: headers });
  }

  public avviaAccoppiatoSuperficie(jsonAvvioProcesso: any): Observable<void> {
    console.log('avviaProcesso');
    console.log('Info processo da avviare: ' + jsonAvvioProcesso);
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<void>(this._configuration.UrlAvviaAccoppiatoSuperficie, JSON.stringify(jsonAvvioProcesso), { headers: headers });
  }

  public avviaPremioAcz(jsonAvvioProcesso: any): Observable<void> {
    console.log('avviaProcesso');
    console.log('Info processo da avviare:');
    console.log(jsonAvvioProcesso);
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<void>(this._configuration.UrlAvviaPremioAcz, JSON.stringify(jsonAvvioProcesso), { headers: headers });
  }

  public getListaProcessiAttivi(jsonListaProcessiIstruttoria): Observable<Array<ProcessoIstruttoria>> {
    console.log('getListaProcessiAttivi');
    return this.http.get<Array<ProcessoIstruttoria>>
      (this._configuration.UrlGetListaProcessiAttiviIstruttoria + jsonListaProcessiIstruttoria);
  }

  public getListaProcessiAttiviPac1420(jsonListaProcessiIstruttoria): Observable<Array<ProcessoIstruttoriaPac1420>> {
    console.log('getListaProcessiAttivi');
    return this.http.get<Array<ProcessoIstruttoriaPac1420>>
      (this._configuration.UrlGetListaProcessiAttiviIstruttoria + jsonListaProcessiIstruttoria);
  }

  public getProcesso(idProcesso: number): Observable<ProcessoIstruttoria> {
    console.log('getProcesso');
    return this.http.get<ProcessoIstruttoria>(this._configuration.UrlGetProcessoById.replace('${id}', String(idProcesso)));
  }

  public setDatiIstruttoria(idDomanda: number, datiIstruttoriaDaInserire: DatiIstruttoria): Observable<number> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    console.log('DatiIstruttoriaDaInserire');
    console.log(JSON.stringify(datiIstruttoriaDaInserire));
    return this.http.put<number>(
      this._configuration.UrlDatiIstruttoriaDomanda.replace('${id}', String(idDomanda)),
      JSON.stringify(datiIstruttoriaDaInserire),
      { headers: headers }
    );
  }

  public getDatiIstruttoria(idDomanda: number): Observable<DatiIstruttoria> {
    return this.http.get<DatiIstruttoria>(this._configuration.UrlDatiIstruttoriaDomanda.replace('${id}', String(idDomanda)));
  }

  public getDettaglioDomanda(id: String, jsonPaginazione, jsonOrdinamento): Observable<DomandaIstruttoriaDettaglio> {
    const a: Observable<DomandaIstruttoriaDettaglio> = this.http.get<DomandaIstruttoriaDettaglio>
      (this._configuration.UrlGetDomandaDettaglio.replace('${id}', id.toString())
        .replace('${paginazione}', String(jsonPaginazione)).replace('${ordinamento}', String(jsonOrdinamento)));
    return a;
  }

  public getPaginaParticelleAttuale(jsonPaginazione, jsonOrdinamento): Observable<PaginaParticellaDomanda> {
    const a: Observable<PaginaParticellaDomanda> = this.http.get<PaginaParticellaDomanda>
      (this._configuration.UrlGetListaDomandePerStatoPaginata
        .replace('${paginazione}', String(jsonPaginazione)).replace('${ordinamento}', String(jsonOrdinamento)));
    return a;
  }

  public getPaginaParticellaAttuale(id: String, jsonPaginazione, jsonOrdinamento): Observable<PaginaRichiestaSuperficie> {
    const a: Observable<PaginaRichiestaSuperficie> = this.http.get<PaginaRichiestaSuperficie>
      (this._configuration.UrlGetDomandaParticelle.replace('${id}', id.toString())
        .replace('${paginazione}', String(jsonPaginazione)).replace('${ordinamento}', String(jsonOrdinamento)));
    return a;
  }

  // tslint:disable-next-line:max-line-length
  public getPaginaDettaglioParticellaAttuale(id: String, params: String, jsonPaginazione, jsonOrdinamento): Observable<PaginaDettaglioCalcoloParticella> {
    const a: Observable<PaginaDettaglioCalcoloParticella> = this.http.get<PaginaDettaglioCalcoloParticella>
      (this._configuration.UrlGetDettaglioCalcoloParticelle.replace('${id}', id.toString()).replace('${params}', params.toString())
        .replace('${paginazione}', String(jsonPaginazione)).replace('${ordinamento}', String(jsonOrdinamento)));
    return a;
  }

  public getDatiPascoloByID_Domanda(id_domanda: number, datiIstruttoria: boolean): Observable<Array<PascoloDao>> {
    let ret: Observable<Array<PascoloDao>>;
    if (datiIstruttoria) {
      ret = this.http.get<Array<PascoloDao>>(this._configuration.UrlGetDettaglioPascoli.replace('${id}', id_domanda.toString()) + encodeURIComponent('datiIstruttoria'));
    } else {
      ret = this.http.get<Array<PascoloDao>>(this._configuration.UrlGetDettaglioPascoli.replace('${id}', id_domanda.toString()));

    }

    return ret;
  }

  public getDatiperPascoloByID_Domanda(id_domanda: number): Observable<Array<PascoloDao>> {
    let ret: Observable<Array<PascoloDao>>;
    ret = this.http.get<Array<PascoloDao>>(this._configuration.UrlGetDettaglioPascoli.replace('${id}', id_domanda.toString()) + encodeURIComponent('datiPerPascolo'));
    return ret;
  }

  public saveDatiIstruttoriaPascoli(id_domanda: number, datiPascoli: Array<DatiIstruttoriaPascoliDTO>): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    const ret = this.http.put<any>(this._configuration.UrlDatiIstruttoriaPascoliDomanda.replace('${id}', id_domanda.toString()), datiPascoli, { headers: headers });
    return ret;
  }


  public getOrdine(n: number) {
    if (n === 1) {
      return 'ASC';
    } else {
      return 'DESC';
    }
  }

  public avviaAccoppiatoZootecnia(jsonInput: any): Observable<void> {
    return this.http.post<void>(this._configuration.UrlDomandeAvvioAccoppiatoZootecnia, jsonInput);
  }

  @Cached()
  public getDocumentoDomanda(idIstruttoria: number): Observable<Blob> {
    return this.http.get(this._configuration.urlVerbaleIstruttoria(idIstruttoria), { responseType: 'blob' });
  }

  @Cached()
  public getVerbaleIstruttoriaAcz(idIstruttoria: number): Observable<Blob> {
    return this.http.get(this._configuration.urlVerbaleIstruttoriaAcz(idIstruttoria), { responseType: 'blob' });
  }

  @Cached()
  public getVerbaleLiquidazioneIstruttoria(idElencoLiquidazione: number): Observable<Blob> {
    return this.http.get(this._configuration.urlIstruttoriaLiquidazioneVerbale(idElencoLiquidazione), { responseType: 'blob' });
  }

  @Cached()
  public getDocumentoElencoLiquidazione(jsonInput: any): Observable<Blob> {
    return this.http.get(this._configuration.StampaElencoLiquidazione + '?params=' + jsonInput, { responseType: 'blob' });
  }

  public getCapi(id_domanda: string, jsonInput): Observable<Array<DettaglioCapi>> {
    return this.http.get<Array<DettaglioCapi>>(this._configuration.UrlGetRichiesteAllevamDu.replace('${idDomanda}', id_domanda.toString()) + '?params=' + jsonInput);
  }

  public aggiornaCapo(idIstruttoria: string, capo: CapoRichiesto): Observable<CapoRichiesto> {
    return this.http.put<CapoRichiesto>(this._configuration.UrlAggiornaCapo.replace('${idIstruttoria}', idIstruttoria.toString()).replace('${idCapoRichiesto}', capo.id.toString()), capo);
  }

  public getDatiDettaglio(idIstruttoria: number, identificativoSostegno: any): Observable<DatiDettaglio> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<DatiDettaglio>(this._configuration.UrlGetDatiDettaglio.replace('${id}', idIstruttoria.toString()) + encodeURIComponent(JSON.stringify(identificativoSostegno)), { headers: headers });
  }

  public aggiornaDomandaIntegrativa(idIstruttoria: number, domandaIntegrativa: SostegnoDuDi): Observable<SostegnoDuDi> {
    return this.http.put<SostegnoDuDi>(this._configuration.UrlAggiornaDomandaIntegrativa.replace('${id}', idIstruttoria.toString()), domandaIntegrativa);
  }

  public aggiornaInterventiPremio(idIstruttoria: number, interventoDuPremi: Array<InterventoDuPremio>): Observable<Array<InterventoDuPremio>> {
    return this.http.put<Array<InterventoDuPremio>>(this._configuration.UrlAggiornaPremi.replace('${id}', idIstruttoria.toString()), interventoDuPremi);
  }

  public getDettaglioDomandaAccoppiati(idDomanda: string, tipoSostegno: string, tipoDettaglio: string): Observable<DettaglioDomandaAccoppiati> {
    return this.http.get<DettaglioDomandaAccoppiati>(
      this._configuration.UrlGetDettaglioDomandaAccoppiati
        .replace('${id}', idDomanda.toString())
        .replace('${sostegno}', tipoSostegno.toString())
        .replace('${dettaglio}', tipoDettaglio.toString())
    );
  }

  public putDettaglioDatiIstruttoriaACS(idDomanda: number, datiIstruttoria: DettaglioDatiIstruttoriaACS): Observable<DettaglioDatiIstruttoriaACS> {
    return this.http.put<DettaglioDatiIstruttoriaACS>(this._configuration.UrlPutDettaglioDatiIstruttoriaACS
      .replace('${id}', idDomanda.toString()),
      datiIstruttoria
    );
  }

  public putDettaglioDatiIstruttoriaACZ(idDomanda: number, datiIstruttoria: DettaglioDatiIstruttoriaACZ): Observable<DettaglioDatiIstruttoriaACZ> {
    return this.http.put<DettaglioDatiIstruttoriaACZ>(this._configuration.UrlPutDettaglioDatiIstruttoriaACZ
      .replace('${id}', idDomanda.toString()),
      datiIstruttoria
    );
  }

  public getFinestraPresentazioneDI(idIstruttoria: string, jsonFilter: any): Observable<SostegnoDuDi> {
    return this.http.get<SostegnoDuDi>(this._configuration.UrlGetFinestraPresentazioneDI.replace('${id}', idIstruttoria) + jsonFilter);
  }

  public getDomandaUnicaFiltered(jsonParametri): Observable<PaginaDomande> {
    return this.http.get<PaginaDomande>
      (this._configuration.UrlGetListaDomandePerStatoPaginata.replace('${parametri}', JSON.stringify(jsonParametri))
        .replace('${paginazione}', null).replace('${ordinamento}', null));
  }

  public getDomandaIntegrativaByDomandaUnica(idDomandaUnica: number): Observable<DomandaIntegrativa> {
    return this.http.get<DomandaIntegrativa>(this._configuration.UrlGetDomandaIntegrativaById.replace('${id}', String(idDomandaUnica)));
  }

  public getEsitoCapo(idDomandaUnica: string, idEsito: string): Observable<any> {
    return this.http.get(this._configuration.UrlGetEsitoCapo.replace('${id}', idDomandaUnica).replace('${idRichiestaAllevamentoEsito}', idEsito));
  }

  public getInterventi(idIstruttoria: string, jsonFilter: any): Observable<Array<Intervento>> {
    return this.http.get<Array<Intervento>>(this._configuration.UrlDescIntervento.replace('${id}', idIstruttoria) + JSON.stringify(jsonFilter));
  }

  public salvaDomandaIntegrativa(idDomandaUnica: string, domandaIntegrativa: DomandaIntegrativa): Observable<DomandaIntegrativa> {
    return this.http.put<DomandaIntegrativa>(this._configuration.UrlSalvaDomandaIntegrativa.replace('${id}', idDomandaUnica), domandaIntegrativa);
  }

  public presentaDomandaIntegrativa(idDomandaUnica: string, domandaIntegrativa: DomandaIntegrativa): Observable<DomandaIntegrativa> {
    return this.http.put<DomandaIntegrativa>(this._configuration.UrlConfermaDomandaIntegrativa.replace('${id}', idDomandaUnica), domandaIntegrativa);
  }

  public inserisciDomanda(idDomanda: string, file: File): Observable<any> {
    console.log('salva dati domanda ');
    const formData: FormData = new FormData();
    formData.append('ricevuta', file, file.name);
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
    headers.append('Accept', 'application/json');
    return this.http.post<any>(
      this._configuration.UrlDomandaIntegrativa
        .replace('${idDomanda}', idDomanda),
      formData,
      { headers: headers }
    );
  }

  public deleteUploadedFile(idDomandaUnica: string): Observable<any> {
    return this.http.delete(this._configuration.UrlEliminaFileCaricato.replace('${idDomanda}', idDomandaUnica));
  }

  public getProduzioneLatte(idDomanda: string, jsonInput: any): Observable<ProduzioneLatte> {
    return this.http.get<ProduzioneLatte>(this._configuration.UrlGetProduzioneLatte.replace('${id}', idDomanda) + jsonInput);
  } 

   public getAlpeggio(idDomanda: string, jsonInput: any): Observable<Array<registrazioneAlpeggio>> {
    return this.http.get<Array<registrazioneAlpeggio>>(this._configuration.UrlGetRegistrazioneAlpeggio.replace('${id}', idDomanda) + jsonInput);
  } 

   public getEtichettatura(jsonInput: any): Observable<Array<EtichettaturaCarne>> {
    return this.http.get<Array<EtichettaturaCarne>>(this._configuration.UrlGetEtichettatura + jsonInput);
  }

  public aggiornaStatoDomande(sostegno: string, jsonInput: any): Observable<Array<any>> {
    return this.http.put<Array<any>>(this._configuration.UrlAggiornaStatoDomande.replace('${sostegno}', sostegno), jsonInput);
  }

  public avviaControlliLiquidazione(idsList: number[], sostegno: string): Observable<void> {
    return this.http.post<void>(this._configuration.UrlAvviaControlliLiquidazione.replace('${sostegno}', sostegno), idsList);
  }

  public avviaControlliLiquidazioneZootecnia(jsonInput: any): Observable<void> {
    return this.http.post<void>(this._configuration.UrlAvviaControlliLiquidazioneZootecnia, jsonInput);
  }

  public aggiornaStatoDomandeACZ(jsonInput: any): Observable<Array<any>> {
    return this.http.put<Array<any>>(this._configuration.UrlAggiornaStatoDomandeACZ, jsonInput);
  }

  public getDomandaIntegrativaByCuaa(jsonInput: any): Observable<DomandaIntegrativa> {
    return this.http.get<DomandaIntegrativa>(this._configuration.UrlGetDomandaIntegrativaByCuaa + JSON.stringify(jsonInput));
  }

  public avviaControlliIntersostegnoACZ(jsonInput: any): Observable<void> {
    return this.http.post<void>(this._configuration.UrlAvviaControlliIntersostegnoACZ, jsonInput);
  }

  public avviaControlliIntersostegnoACS(jsonInput: any): Observable<void> {
    console.log('avviaControlliIntersostegnoACS');
    console.log('Info processo da avviare: ' + jsonInput);
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<void>(this._configuration.UrlAvviaControlliIntersostegnoACS, JSON.stringify(jsonInput), { headers: headers });
  }

  public getRicevutaDomandaIntegrativa(jsonInput: any): Observable<Blob> {
    return this.http.get(this._configuration.StampaRicevutaDomandaIntegrativa + '?params=' + jsonInput, { responseType: 'blob' });
  }

  public avviaStatistiche(jsonInput: any): Observable<void> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<void>(this._configuration.UrlAvviaStatistiche, JSON.stringify(jsonInput), { headers: headers });
  }

  public avviaSincronizzazione(jsonInput: any): Observable<void> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<void>(this._configuration.UrlAvviaSincronizzazione, JSON.stringify(jsonInput), { headers: headers });
  }


  public getInfoDomanda(idDomanda: number, expands: String[]): Observable<any> {
    const params = {
    };
    params['expand'] = expands.join(',');
    return this.http.get(this._configuration.UrlGetInfoDomanda.replace('${id}', idDomanda.toString()), { params });
  }

  public aggiornaDomanda(domanda: Domanda): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put(this._configuration.UrlGetInfoDomanda.replace('${id}', domanda.id.toString()), JSON.stringify(domanda), { headers });
  }

  public getInfoLiquidabilita(idDomanda: number): Observable<any> {
    const params = {
    };
    params['expand'] ='infoLiquidabilita';
    return this.http.get(this._configuration.UrlGetDomandeDU.replace('${idDomanda}', idDomanda.toString()), { params });
  }


  public aggiornaDatiErede(idDomanda: number, datiErede: DatiErede): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put(this._configuration.UrlDatiErede.replace('${id}', idDomanda.toString()) + '/' + datiErede.id,
      JSON.stringify(datiErede),
      { headers });
  }

  public creaDatiErede(idDomanda: number, datiErede: DatiErede): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(this._configuration.UrlDatiErede.replace('${id}', idDomanda.toString()),
      JSON.stringify(datiErede),
      { headers });
  }

  public annullaIstruttoria(idDomanda: number): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(this._configuration.UrlAnnullaIstruttoria(idDomanda.toString()),
      { headers });
  }

  public getDomandeIbanErrato(): Observable<Array<DatiDomandaIbanErrato>> {
    console.log('getDomandeIbanErrato');
    return this.http.get<Array<DatiDomandaIbanErrato>>(this._configuration.UrlGetDomandeIbanErrato);
  }

  @Cached()
  public ricercaIstruttorieDU(
    istruttoriaDomandaUnicaFilter: IstruttoriaDomandaUnicaFilter, paginazione: Paginazione): Observable<IstruttoriaPagina> {
    let data: any = { ...istruttoriaDomandaUnicaFilter, ...paginazione };
    return this.http.get<IstruttoriaPagina>(this._configuration.UrlGetRicercaIstruttorieDU, { params: data });
  }


  @Cached()
  public countIstruttorieDU(istruttoriaDomandaUnicaFilter: IstruttoriaDomandaUnicaFilter): Observable<number> {
    let data: any = { ...istruttoriaDomandaUnicaFilter };
    return this.http.get<number>(this._configuration.UrlGetRicercaIstruttorieDU + '/count', { params: data });
  }

  @Cached()
  public getIstruttoriaDU(idIstruttoria: string): Observable<IstruttoriaDomandaUnica> {
    return this.http.get<IstruttoriaDomandaUnica>(this._configuration.UrlGetRicercaIstruttorieDU + `/${idIstruttoria}`);
  }

  public getProcessiDiControllo(tipoProcesso: string): Observable<Array<ProcessiDiControllo>> {
    if (tipoProcesso)
      return this.http.get<Array<ProcessiDiControllo>>(this._configuration.UrlGetProcessiDiControllo + `/?tipoProcesso=${tipoProcesso}`);
    else return this.http.get<Array<ProcessiDiControllo>>(this._configuration.UrlGetProcessiDiControllo);
  }

  /*
  *
  * return TRUE se esiste almeno un processo in esecuzione
  *
  */
  public checkProcessiInEsecuzione(tipiProcesso: TipoProcesso[]): Observable<boolean> {
    let requests = [];
    tipiProcesso.forEach(tipoProcesso => requests.push(this.getProcessiInEsecuzione(tipoProcesso)));
    return forkJoin(requests).pipe(
      map(val => val.some(v => {
        if (v) return true; //check se esiste almeno un processo valorizzato. Se valorizzato significa che è in esecuzione
        return false;
      }))
    );
    //.subscribe(val => console.log(val));
  }

  public getProcessiInEsecuzione(tipoProcesso: TipoProcesso): Observable<any> {
    let data: any = { tipoProcesso };
    return this.http.get<any>(this._configuration.UrlAvviaProcessoIstruttoriaDU + '/inesecuzione', { params: data });
  }

  public getElencoDomande(domandaUnicaFilter: DomandaUnicaFilter, paginazione: Paginazione): Observable<DomandaUnica> {
    let data: any = { ...domandaUnicaFilter, ...paginazione };
    return this.http.get<DomandaUnica>(this._configuration.UrlGetElencoDomande, { params: data });
  }

  public getCapiImpegnatiPerAGEA(annoCampagna: number): Observable<any> {
    return this.http.get(this._configuration.urlGetFileDatiCapiAgeaNew(annoCampagna), {responseType: 'blob' });
  }

  public downloadCsvIstruttorie(elencoId: number[]): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(
      this._configuration.UrlDownloadCsvIstruttorie, elencoId, { headers: headers, responseType: 'blob' }
    );
  }

  public resetIstruttoria(idIstruttorie: number[], tipoProcesso: TipoProcesso): Observable<number> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
      let obj = { idIstruttorie: idIstruttorie, processo: tipoProcesso };
      return this.http.post<number>(this._configuration.UrlAvviaProcessoIstruttoriaDU, obj, { headers: headers });
  }

  public aggiornaDatiBDN(annoCampagna: number, cuaa: string): Observable<boolean> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<boolean>(this._configuration.UrlPutSyncBDN(annoCampagna, cuaa), { headers: headers });
  }

  public generaXmlPerCUP(fileCSV: File): Observable<any> {
    console.log('genera Xml per CUP ');
    const formData = new FormData();
    const headers = new HttpHeaders();
    headers.set('Accept', 'application/json');
    headers.delete('Content-Type'); // mandate for accepting binary content
    formData.append('file', fileCSV, fileCSV.name);
    return this.http.post<any>(
      this._configuration.UrlCupGeneraXml,
      formData,
      {responseType: 'blob' as 'json'},
    );
  }
  
  @Cached()
  public getDatiAggregatiPagamenti(tipoSostegno: SostegnoDu, tipoIstruttoria: TipoIstruttoriaEnum, annoCampagna: number): Observable<PremioPagamentiStatoIstruttoriaNettoLordoDto[]> {
    return this.http.get<PremioPagamentiStatoIstruttoriaNettoLordoDto[]>(`${this._configuration.UrlGetRicercaIstruttorieDU}/${tipoSostegno}/${tipoIstruttoria}/${annoCampagna}/dati-aggregati-pagamenti`);
  }
}
