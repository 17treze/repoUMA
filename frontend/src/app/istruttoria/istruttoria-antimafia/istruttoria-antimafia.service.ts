import { TrasmissioneBdna } from './dto/TrasmissioneBdna';
import { EsitiBdna } from './dto/EsitiBdna';
import { formatDate } from "@angular/common";
import { SogliaAcquisizioneFilter } from './dto/SogliaAcquisizioneFilter';
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Configuration } from "src/app/app.constants";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { DomandeCollegateImport } from './dto/DomandeCollegateImport';
import { DomandaCollegata } from "./dto/DomandaCollegata";
import { TipoDomandaEnum } from './dto/TipoDomandaEnum';
import { SincronizzazioneDateBdnaDto } from 'src/app/a4g-common/classi/SincronizzazioneDateBdnaDto';
import { DomandaCollegataFilter } from './dto/DomandaCollegataFilter';
import { SogliaAcquisizione } from "./dto/SogliaAcquisizione";
import { PaginaAntimafia } from './dto/PaginaAntimafia';
import { RicercaAntimafiaFilter } from './dto/RicercaAntimafiaFilter';
import { Cached } from 'src/assets/decorators/cached';
import { ProcessiDiControllo } from '../istruttoria-pac1420/domandaUnica/sostegno-shared/dto/processi-di-controllo';
import {Paginazione} from "../../a4g-common/utility/paginazione";
import { DatiDomandaRicercaPage} from "../istruttoria-pac1420/domandaUnica/classi/DatiDomandaRicerca";
import {IstruttoriaDomandaUnicaFilter} from "../istruttoria-pac1420/domandaUnica/classi/IstruttoriaDomandaUnicaFilter";
import {StatoDomandaEnum} from "../istruttoria-pac1420/domandaUnica/dettaglio-istruttoria/statoDomanda";
import { StatoTrasmissioneEnum } from './dto/StatoTrasmissioneEnum';
import { HttpHelperService } from 'src/app/a4g-common/services/http-helper.service';
import { DichiarazioneAntimafiaFilter } from 'src/app/fascicolo/antimafia/classi/dichiarazioneAntimafiaFilter';
@Injectable({
  providedIn: "root"
})
export class IstruttoriaAntimafiaService {


  headers = new HttpHeaders({ "Content-Type": "application/json" });
  domandeCollegateImport: DomandeCollegateImport = new DomandeCollegateImport();

  // index tab selezionato
  private tabIndex: number;

  setTabIndex(tabIndex: number) {
    this.tabIndex = tabIndex
  }
  getTabIndex(): number {
    return this.tabIndex
  }

  constructor(
    private _configuration: Configuration,
    private http: HttpClient,
    private httpHelperService: HttpHelperService
  ) { }

  // controllo istruttoria antimafia processo attivo - restituisce Id processo con nome file e flag
  public controllaDomandeCollegate(): Observable<any> {
    return this.http.get(this._configuration.urlControlloDomandeCollegate, { responseType: 'text' });
  }

  public importaDatiStrutturali(cuaa: string[], sogliaDati: SogliaAcquisizione): Observable<Array<DomandaCollegata>> {
    let domandaCollegata = Object.assign({}, this.domandeCollegateImport);
    domandaCollegata.cuaa = cuaa;
    domandaCollegata.importo = sogliaDati.soglia;
    domandaCollegata.dataPresentazione = formatDate(sogliaDati.dataPresentazione, 'dd/MM/yyyy', 'it-IT');

    return this.http.put<any>(this._configuration.urlIstruttoriaAntimafiaImportaDatiStrutturali, JSON.stringify(domandaCollegata), { headers: this.headers });
  }

  public importaDatiSuperficie(cuaa: string[], sogliaDati: SogliaAcquisizione, anniCampagna: string): Observable<Array<DomandaCollegata>> {
    this.domandeCollegateImport.cuaa = cuaa;
    this.domandeCollegateImport.importo = sogliaDati.soglia;
    this.domandeCollegateImport.dataPresentazione = formatDate(sogliaDati.dataPresentazione, 'dd/MM/yyyy', 'it-IT');
    this.domandeCollegateImport.anniCampagna = new Array<string>();
    this.domandeCollegateImport.anniCampagna.push(anniCampagna);
    return this.http.put<any>(this._configuration.urlIstruttoriaAntimafiaImportaDatiSuperficie, JSON.stringify(this.domandeCollegateImport), { headers: this.headers });
  }

  public importaDatiDU(file: File, sogliaDati: SogliaAcquisizione): Observable<Array<DomandaCollegata>> {
    let domandaCollegata = Object.assign({}, this.domandeCollegateImport);
    domandaCollegata.cuaa = null;
    domandaCollegata.importo = sogliaDati.soglia;
    domandaCollegata.anniCampagna = [];
    domandaCollegata.dataPresentazione = formatDate(sogliaDati.dataPresentazione, 'dd/MM/yyyy', 'it-IT');

    let formData: FormData = new FormData();
    formData.append('info', JSON.stringify(domandaCollegata));
    formData.append('csv', file, file.name);
    let headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
    headers.append('Accept', 'application/json');
    return this.http.put<any>(this._configuration.urlIstruttoriaAntimafiaImportaDatiDU, formData, { headers: headers });
  }

  public getCertificazioneAntimafia(ricercaAntimafiaFilter: RicercaAntimafiaFilter): Observable<PaginaAntimafia> {
    let headers = new HttpHeaders({ "Content-Type": "application/json" });
    let params = new HttpParams();
    params = params.append('pagSize', String(ricercaAntimafiaFilter.pagSize));
    params = params.append('pagStart', String(ricercaAntimafiaFilter.pagStart));
    params = params.append('stato', String(ricercaAntimafiaFilter.statoDichiarazione));
    if (ricercaAntimafiaFilter.sortBy) {
      // params = params.append('sortBy', encodeURIComponent(ricercaAntimafiaFilter.sortBy));
      if (ricercaAntimafiaFilter.sortBy && ricercaAntimafiaFilter.sortBy.startsWith('+')) {
        ricercaAntimafiaFilter.sortBy = ricercaAntimafiaFilter.sortBy.split('+')[1];
      } else {
        params = params.append('sortBy', ricercaAntimafiaFilter.sortBy);
      }
    }
    if (ricercaAntimafiaFilter.filtroGenerico) {
      params = params.append('filtroGenerico', ricercaAntimafiaFilter.filtroGenerico);
    }
    // problemi nell'encode del +
    return this.http.get<PaginaAntimafia>(this._configuration.urlIstruttoriaAntimafiaGetCertificazioni, { headers, params });
  }

  public getCertificazioniAntimafiaByStato(statoDichiarazione: string): Observable<any> {
    let filter: DichiarazioneAntimafiaFilter;
    filter = new DichiarazioneAntimafiaFilter();
    filter.statiDichiarazione = [statoDichiarazione];

    const queryString = filter != null ? this.httpHelperService.buildQueryStringFromObject(filter) : '';
    return this.http.get<any>(this._configuration.urlGetDichiarazioneByStato + queryString);
  }

  // ESPORTA CSV
  public getEsportaCSV(cuaa: string[], tipoDomanda: TipoDomandaEnum, anniCampagna: string): Observable<any> {
    const headers = new HttpHeaders({ "Content-Type": "application/json" });
    let anniCampagnaList = new Array<string>();
    anniCampagnaList.push(anniCampagna);
    return this.http.post(
      this._configuration.urlIstruttoriaAntimafiaPostEsportaCSV,
      JSON.stringify({ cuaa: cuaa, tipoDomanda: tipoDomanda, anniCampagna: anniCampagnaList }),
      { headers, responseType: 'blob', observe: 'response' }
    );
  }

  public annullaDomandeCollegate(id: number): any {
    return this.http.delete<any>(this._configuration.urlDeleteTrasmissioneBdna.replace("{id}", String(id)), { headers: this.headers });
  }

  public confermaDomandeCollegate(trasmDaAggiornare: TrasmissioneBdna): any {
    return this.http.put<any>(this._configuration.urlPutTrasmissioneBdna.replace("{id}", String(trasmDaAggiornare.id)), trasmDaAggiornare, { headers: this.headers });
  }

  public getDomandeCollegate(id: number): Observable<DomandaCollegata> {
    return this.http.get<DomandaCollegata>(this._configuration.urlIstruttoriaAntimafiaGetDomandeCollegate.replace("{id}", String(id)));
  }
  public aggiornaDomandeCollegate(domandaCollegata: DomandaCollegata): Observable<DomandaCollegata> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<DomandaCollegata>(
      this._configuration.urlIstruttoriaAntimafiaGetDomandeCollegate.replace('{id}', String(domandaCollegata.id)),
      JSON.stringify(domandaCollegata),
      { headers: headers });
  }

  // sincronizza esiti BDNA in ags - asincrono
  public sincronizzaDateBDNAAntimafia(dichiarazioniToUpdate: SincronizzazioneDateBdnaDto[]) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http.put<SincronizzazioneDateBdnaDto>(this._configuration.urlAgsPutEsitiAntimafia, JSON.stringify(dichiarazioniToUpdate), { headers: headers }).subscribe();
  }

  // delete domande collegate 
  // public cancellaDomandeCollegate(domandaCollegataFilter: DomandaCollegataFilter) {
  //   const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
  //   const params = encodeURIComponent(JSON.stringify(domandaCollegataFilter));
  //   this.http.delete(this._configuration.urlIstruttoriaAntimafiGetDomandeCollegate + "/?params=" + params, { headers: headers }).subscribe();
  // }

  public getDomandeCollegateFilter(domandaCollegataFilter: DomandaCollegataFilter): Observable<DomandaCollegata[]> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const params = encodeURIComponent(JSON.stringify(domandaCollegataFilter));
    return this.http.get<DomandaCollegata[]>(this._configuration.urlIstruttoriaAntimafiGetDomandeCollegate + "/?params=" + params, { headers: headers })
  }

  public getSogliaAcquisizione(sogliaAcquisizioneFilter: SogliaAcquisizioneFilter): Observable<SogliaAcquisizione> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const params = encodeURIComponent(JSON.stringify(sogliaAcquisizioneFilter));
    return this.http.get<SogliaAcquisizione>(this._configuration.urlSogliaAcqusizione + "/?params=" + params, { headers: headers });
  }

  public importaEsitiBdna(file: File): Observable<EsitiBdna> {
    let formData: FormData = new FormData();
    formData.append('csv', file, file.name);
    let headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
    headers.append('Accept', 'application/json');
    return this.http.put<any>(this._configuration.urlIstruttoriaAntimafiaImportaEsitiBdna, formData, { headers: headers });
  }


  // recupera esiti antimafia da ags 
  public getEsitiAntimafiaAGS(esitoAntimafia: SincronizzazioneDateBdnaDto): Observable<Array<SincronizzazioneDateBdnaDto>> {
    let headers = new HttpHeaders({ "Content-Type": "application/json" });

    const params = {
      'params': JSON.stringify(esitoAntimafia)
    };
    return this.http.get<Array<SincronizzazioneDateBdnaDto>>(this._configuration.urlAgsPutEsitiAntimafia, { headers, params });
  }

  public exportCsvEsitiBdna(): Observable<any> {
    const headers = new HttpHeaders({ "Content-Type": "application/json" });
    return this.http.post(
      this._configuration.urlIstruttoriaAntimafiaExportEsitiBdna,
      "",
      { headers, responseType: 'blob', observe: 'response' }
    );
  }

  public getTrasmissioniDaConfermare(cfOperatore: string): Observable<Array<TrasmissioneBdna>> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    let trasmissione = new TrasmissioneBdna();
    trasmissione.cfOperatore = cfOperatore;
    trasmissione.statoTrasmissione = StatoTrasmissioneEnum.NON_CONFERMATA;
    const queryString = trasmissione != null ? '?' + this.httpHelperService.buildQueryStringFromObject(trasmissione) : '';
    return this.http.get<Array<TrasmissioneBdna>>(this._configuration.urlTrasmissioneBdna + queryString);
  }

  public downloadFileCsv(id: number): Observable<any> {
    const headers = new HttpHeaders({ "Content-Type": "application/json" });
    return this.http.get(
      this._configuration.urlDownloadCsv.replace("{id}", String(id)),
      { headers, responseType: 'blob', observe: 'response' }
    );
  }

  @Cached()
  public caricaAnniCampagna(): Observable<number[]> {
    console.log('caricaAnniCampagna');
    return this.http.get<Array<number>>(this._configuration.UrlCaricaAnniCampagna);
  }

  public getProcessiDiControllo(tipoProcesso: string): Observable<Array<ProcessiDiControllo>> {
    if (tipoProcesso)
      return this.http.get<Array<ProcessiDiControllo>>(this._configuration.UrlGetProcessiDiControllo + `/?tipoProcesso=${tipoProcesso}`);
    else return this.http.get<Array<ProcessiDiControllo>>(this._configuration.UrlGetProcessiDiControllo);
  }

  public ricercaDomandeUniche(filter: IstruttoriaDomandaUnicaFilter, paginazione: Paginazione): Observable<DatiDomandaRicercaPage> {
    let data: any = { ...filter, ...paginazione };
    return this.http.get<DatiDomandaRicercaPage>(this._configuration.UrlgetRicercaDomandeUniche, {params: data});
  }

  public getListaStati(): Observable<StatoDomandaEnum[]> {
    return this.http.get<StatoDomandaEnum[]>(this._configuration.UrlgetRicercaDomandeUnicheListaStati);
  }
  public getListaAnni(): Observable<Number[]> {
    return this.http.get<Number[]>(this._configuration.UrlgetRicercaDomandeUnicheListaAnni);
  }

}
