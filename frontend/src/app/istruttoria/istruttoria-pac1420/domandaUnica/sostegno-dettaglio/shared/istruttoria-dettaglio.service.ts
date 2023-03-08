import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Configuration } from 'src/app/app.constants';
import { Observable } from 'rxjs';
import { Controllo } from '../../domain/controllo';
import { DichiarazioneDu } from '../../domain/dichiarazioneDu';
import { InformazioniDu } from '../../domain/informazioniDU';
import { Paginazione } from 'src/app/a4g-common/utility/paginazione';
import { Params } from '@angular/router';
import { DettaglioSuperficieCalcoloDto, PaginaDettaglioCalcoloParticella } from '../../domain/dettaglioCalcoloParticella';
import { PaginaSuperfici } from '../../domain/richiestaSuperficie';
import { DatiIstruttoria } from '../../domain/datiIstruttoria';
import { PascoloDao } from '../../domain/pascolo-dao';
import { DatiIstruttoriaPascoliDTO } from '../../domain/dati-istruttoria-pascoli-dto';
import { DatiIstruttoriaAccoppiati } from '../../domain/datiIstruttoriaAccoppiati';
import { SuperficiImpegnateACS, DatiDomandaACS, DatiInterventi, DatiParticella, DatiDomandaACZ } from '../../classi/dettaglioDomandaAccoppiati';
import { Cached } from 'src/assets/decorators/cached';
import { EsitiControlloSostegno } from '../../domain/esiti-controllo-sostegno';
import { DettaglioCapi } from '../../domain/dettaglioCapi';
import { EsitoCapiFilter } from '../../classi/esitoCapiFilter';
import { CapoPagina } from '../../classi/CapoPagina';
import { EtichettaturaCarne } from '../../domain/etichettatura-carne';
import { registrazioneAlpeggio } from '../../domain/registrazione-alpeggio';
import { ProduzioneLatte } from '../../domain/produzione-latte';
import { Istruttorie } from './dettaglio-istruttoria';
import { map } from 'rxjs/operators';
import { IstruttoriaGraficaDuDto } from '../../classi/IstruttoriaGraficaDuDto';
import { DateSupport } from 'src/app/a4g-common/utility/date-support';

@Injectable({
  providedIn: 'root'
})
export class IstruttoriaDettaglioService {

  private agsIstruttoriaGraficaPath: string;

  constructor(
    private http: HttpClient, private _configuration: Configuration) {
      this.agsIstruttoriaGraficaPath = `${this._configuration.ags_server}/istruttoria-grafica`;
  }

  @Cached()
  public getControlliSostegnoIstruttoriaDU(idIstruttoria: string): Observable<Array<Controllo>> {
    return this.http.get<Array<Controllo>>(this._configuration.UrlGetRicercaIstruttorieDU + `/disaccoppiato/${idIstruttoria}/controlli`);
  }

  public getDatiDomandaIstruttoriaDU(idIstruttoria: string): Observable<Array<Controllo>> {
    return this.http.get<Array<Controllo>>(this._configuration.UrlGetRicercaIstruttorieDU + `/disaccoppiato/${idIstruttoria}/datiDomanda`);
  }


  public getDatiIstruttoriaDU(idIstruttoria: string): Observable<DatiIstruttoria> {
    return this.http.get<DatiIstruttoria>(this._configuration.UrlGetRicercaIstruttorieDU + `/disaccoppiato/${idIstruttoria}/datiIstruttore`);
  }

  public getDatiIstruttoriaDuAcs(idIstruttoria: string): Observable<DatiIstruttoriaAccoppiati> {
    return this.http.get<DatiIstruttoriaAccoppiati>(this._configuration.UrlGetRicercaIstruttorieDU + `/superficie/${idIstruttoria}/datiIstruttore`);
  }

  public getDatiIstruttoriaDuAcz(idIstruttoria: string): Observable<DatiIstruttoriaAccoppiati> {
    return this.http.get<DatiIstruttoriaAccoppiati>(this._configuration.UrlGetRicercaIstruttorieDU + `/zootecnia/${idIstruttoria}/datiIstruttore`);
  }  

  public getDatiIstruttoriaDUDatiPascolo(idIstruttoria: string): Observable<Array<PascoloDao>> {
    return this.http.get<Array<PascoloDao>>(this._configuration.urlDettaglioPascoloDUcondatiIstruttoria(idIstruttoria));
  }

  @Cached()
  public getDatiperPascoloByIdIstruttoriaConEsitoMantenimento(idIstruttoria: number): Observable<Array<PascoloDao>> {
    return this.http.get<Array<PascoloDao>>(
      this._configuration.getUrlDettaglioPascoloIstruttoriaConEsitoMantenimentoDU(idIstruttoria));
  }

  public saveOrUpdateDatiIstruttoriaDU(idIstruttoria: string, datiIstruttoria: DatiIstruttoria ): Observable<DatiIstruttoria> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<DatiIstruttoria>(
      this._configuration.UrlGetRicercaIstruttorieDU + `/disaccoppiato/${idIstruttoria}/datiIstruttore`,
      JSON.stringify(datiIstruttoria),
      { headers: headers }
    );
  }

  public saveOrUpdateDatiIstruttoriaDuAcs(idIstruttoria: string, datiIstruttoria: DatiIstruttoriaAccoppiati ): Observable<DatiIstruttoriaAccoppiati> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<DatiIstruttoriaAccoppiati>(
      this._configuration.UrlGetRicercaIstruttorieDU + `/superficie/${idIstruttoria}/datiIstruttore`,
      JSON.stringify(datiIstruttoria),
      { headers: headers }
    );
  }

  public saveOrUpdateDatiIstruttoriaDuAcz(idIstruttoria: string, datiIstruttoria: DatiIstruttoriaAccoppiati ): Observable<DatiIstruttoriaAccoppiati> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<DatiIstruttoriaAccoppiati>(
      this._configuration.UrlGetRicercaIstruttorieDU + `/zootecnia/${idIstruttoria}/datiIstruttore`,
      JSON.stringify(datiIstruttoria),
      { headers: headers }
    );
  }

  public saveDatiIstruttoriaPascoli(idIstruttoria: number, datiPascoli: Array<DatiIstruttoriaPascoliDTO>): Observable<Array<DatiIstruttoriaPascoliDTO>> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    // const ret = this.http.post<any>(this._configuration.UrlDatiIstruttoriaPascoliDomanda.replace('${id}', id_domanda.toString()), datiPascoli, { headers: headers });
    // return ret;
    return this.http.post<Array<DatiIstruttoriaPascoliDTO>>(
      this._configuration.UrlGetRicercaIstruttorieDU + `/disaccoppiato/${idIstruttoria}/datiIstruttore/pascoli`,
      JSON.stringify(datiPascoli),
      { headers: headers }
    );    
  }

  public getDichiarazioniDU(idDomanda: string): Observable<Array<DichiarazioneDu>> {
    return this.http.get<Array<DichiarazioneDu>>(this._configuration.UrlGetDettagliDU
      .replace('${id}', idDomanda.toString()));
  }

  public getInformazioniDU(idDomanda: string): Observable<InformazioniDu> {
    return this.http.get<InformazioniDu>(this._configuration.UrlGetInformazioniDU
      .replace('${id}', idDomanda.toString()));
  }

  public getDatiParticellaIsPascoloDU(idIstruttoria: string, paginazione: Paginazione, isPascolo: boolean): Observable<PaginaDettaglioCalcoloParticella> {
    let data: Params = Paginazione.fillHttpParamsWith(new HttpParams(), paginazione);
    return this.http.get<PaginaDettaglioCalcoloParticella>(this._configuration.UrlGetDatiParticellaIsPascoloDU
      .replace('${idIstruttoria}', idIstruttoria.toString())
      .replace('${pascolo}', encodeURIComponent(isPascolo.toString()))
      , { params: data });
  }

  public getDatiParticellaIsNotPascoloDU(idIstruttoria: string, paginazione: Paginazione): Observable<PaginaDettaglioCalcoloParticella> {
    let data: Params = Paginazione.fillHttpParamsWith(new HttpParams(), paginazione);
    return this.http.get<PaginaDettaglioCalcoloParticella>(this._configuration.UrlGetDatiParticellaIsNotPascoloDU
      .replace('${idIstruttoria}', idIstruttoria.toString())
      , { params: data });
  }

  public getDCSVDatiParticelleDati(idIstruttoria: number, tipoDati: number): Observable<any> {
    const headers = new HttpHeaders({ 'Accept': 'application/text' });
    let UrlGetCSVDatiParticelle: string;
    if (tipoDati== 1) {UrlGetCSVDatiParticelle = this._configuration.UrlGetCSVDatiParticelleEleggibilita;}
    if (tipoDati== 2) {UrlGetCSVDatiParticelle = this._configuration.UrlGetCSVDatiParticelleGreening;}
    if (tipoDati== 3) {UrlGetCSVDatiParticelle = this._configuration.UrlGetCSVDatiParticelleMantenimento;}
    if (tipoDati < 1 || tipoDati > 3) {return null;}
    return this.http.get(UrlGetCSVDatiParticelle
      .replace('${idIstruttoria}', idIstruttoria.toString()),
      { headers: headers, responseType: "text" }
      );
  }

  public getSuperficiImpegnateDU(idDomanda: string, paginazione: Paginazione): Observable<PaginaSuperfici> {
    let data: Params = Paginazione.fillHttpParamsWith(new HttpParams(), paginazione);
    return this.http.get<PaginaSuperfici>(this._configuration.UrlGetSuperficiImpegnateDU
      .replace('${idDomanda}', idDomanda.toString())
      , { params: data });
  }

  public getSuperficiImpegnateAcs(idDomanda: string): Observable<SuperficiImpegnateACS> {
    return this.http.get<SuperficiImpegnateACS>(this._configuration.UrlAcsDU + `/${idDomanda}/superfici`);
  }

  public getDichiarazioniAcs(idDomanda: string): Observable<Array<DichiarazioneDu>> {
    return this.http.get<Array<DichiarazioneDu>>(this._configuration.UrlAcsDU + `/${idDomanda}/dichiarazioni`);
  }

  public getDichiarazioniAcz(idDomanda: string): Observable<Array<DichiarazioneDu>> {
    return this.http.get<Array<DichiarazioneDu>>(this._configuration.UrlAczDU + `/${idDomanda}/dichiarazioni`);
  }
  
  @Cached()
  public getEsitiControlloSostegno(idIstruttoria: string): Observable<EsitiControlloSostegno> {
    return this.http.get<EsitiControlloSostegno>(this._configuration.UrlIstruttorieAcs + `/${idIstruttoria}/esiti-controlli-sostegno`);
  }

  @Cached()
  public getControlliSostegno(idIstruttoria: string): Observable<EsitiControlloSostegno> {
    return this.http.get<EsitiControlloSostegno>(this._configuration.UrlIstruttorieAcz + `/${idIstruttoria}/esiticontrolli`);
  }

  @Cached()
  public getEsitiCalcoloIstruttoriaAcs(idIstruttoria: string): Observable<DatiDomandaACS> {
    return this.http.get<DatiDomandaACS>(this._configuration.UrlIstruttorieAcs + `/${idIstruttoria}/esiticalcoli`);
  }

  @Cached()
  public getEsitiCalcoloIstruttoriaAcz(idIstruttoria: string): Observable<DatiDomandaACZ> {
    return this.http.get<DatiDomandaACZ>(this._configuration.UrlIstruttorieAcz + `/${idIstruttoria}/esiticalcoli`);
  }

  @Cached()
  public getDisciplinaIstruttoriaAcs(idIstruttoria: string): Observable<DatiInterventi> {
    return this.http.get<DatiInterventi>(this._configuration.UrlIstruttorieAcs + `/${idIstruttoria}/disciplina`);
  }

  @Cached()
  public getDisciplinaIstruttoriaAcz(idIstruttoria: string): Observable<DatiInterventi> {
    return this.http.get<DatiInterventi>(this._configuration.UrlIstruttorieAcz + `/${idIstruttoria}/disciplina`);
  }

  @Cached()
  public getEsitiParticelle(idIstruttoria: string): Observable<DatiParticella> {
    return this.http.get<DatiParticella>(this._configuration.UrlIstruttorieAcs + `/${idIstruttoria}/esitiparticelle`);
  }

  public getAllevamentiImpegnati(idIstruttoria: string): Observable<Array<DettaglioCapi>> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<Array<DettaglioCapi>>(this._configuration.UrlIstruttorieAczGetAllevamentiImpegnati.replace('${idIstruttoria}', idIstruttoria.toString()), {headers: headers});
  }

  public getAllevamentiRichiesti(idIstruttoria: string): Observable<Array<DettaglioCapi>> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<Array<DettaglioCapi>>(this._configuration.UrlIstruttorieAczGetAllevamentiRichiesti.replace('${idIstruttoria}', idIstruttoria.toString()), {headers: headers});
  }

  public ricercaEsiticapi(filter: EsitoCapiFilter, paginazione: Paginazione): Observable<CapoPagina> {
    let data: any = { ...filter, ...paginazione };
    return this.http.get<CapoPagina>(this._configuration.UrlIstruttorieAczEsitiCalcoloCapiRicerca, {params: data});
  }

  public getEtichettaturaZootecnia(annoCampagna: string, cuaa: string): Observable<Array<EtichettaturaCarne>> {
    let data: any = {cuaa};
    return this.http.get<Array<EtichettaturaCarne>>(this._configuration.UrlGetEtichettaturaZootecnia.replace("${annoCampagna}", annoCampagna), {params: data});
  }

  public getAlpeggioZootecnia(annoCampagna: string, cuaa: string): Observable<Array<registrazioneAlpeggio>> {
    let data: any = {cuaa};
    return this.http.get<Array<registrazioneAlpeggio>>(this._configuration.UrlGetRegistrazioneAlpeggioZootecnia.replace('${annoCampagna}', annoCampagna), {params: data});
  }

  public getProduzioneLatteZootecnia(annoCampagna: string, cuaa: string): Observable<ProduzioneLatte> {
    let data: any = {cuaa};
    return this.http.get<ProduzioneLatte>(this._configuration.UrlGetProduzioneLatteZootecnia.replace('${annoCampagna}', annoCampagna), {params: data});
  }
  
  @Cached()
  public getIstruttorie(numeroDomanda): Observable<Istruttorie[]> {
    return this.http.get<Istruttorie[]>(`${this._configuration.urlDomandaunica}/${numeroDomanda}/istruttorieByNumeroDomanda`);
  }
  
  @Cached()
  public findIdParcelleByDomandaParticellaIntervento(idDomanda: number, idParticella: number, codiceColtura: string): Observable<DettaglioSuperficieCalcoloDto[]> {
    return this.http.get<DettaglioSuperficieCalcoloDto[]>(
      this._configuration.getUrlGetParticelleDettaglioSuperficiePerCalcolo(idDomanda, idParticella, codiceColtura));
  }

  @Cached()
  public getIstruttoriaGraficaDomandaUnica(idDomanda: number, cuaa: string): Observable<IstruttoriaGraficaDuDto> {
    return this.http.get<any>(`${this.agsIstruttoriaGraficaPath}/cuaa/${cuaa}/id-domanda/${idDomanda}`).pipe(
      map(source => {
        if (!source) return null;
        const dto: IstruttoriaGraficaDuDto = new IstruttoriaGraficaDuDto();
        dto.cuaa = source.cuaa;
        dto.dataIstruttoriaGrafica = DateSupport.convertStringToOnlyDate(source.dataIstruttoriaGrafica);
        dto.dataRiferimento = DateSupport.convertStringToOnlyDate(source.dataRiferimento);
        dto.anno = source.anno;
        return dto;
      })
    );
  }

  public getOrdine(n: number): string {
    if (n === 1) {
      return 'ASC';
    } else {
      return 'DESC';
    }
  }
}
