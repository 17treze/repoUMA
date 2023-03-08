import { HttpClient, HttpHeaders, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import * as FileSaver from "file-saver";
import { MessageService } from 'primeng/api';
import { concat, from, Observable } from "rxjs";
import { concatMap } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { TipoNotaEnum } from "src/app/istruttoria/istruttoria-antimafia/dto/TipoNotaEnum";
import { Configuration } from "../../app.constants";
import { DatiChiusuraExNovoDichiarazioneAntimafia } from "./classi/datiChiusuraExNovoDichiarazioneAntimafia";
import { AllegatoDicFamConv, AllegatoMetadati, CaricaCodiceFiscale, SoggettiImpresa } from "./classi/datiDichiarazione";
import { DichiarazioneAntimafia } from "./classi/dichiarazioneAntimafia";
import { NotaDichiarazioneRifiutata } from './classi/NotaDichiarazioneRifiutata';
import { StatoDichiarazioneCount } from "./classi/statoDichiarazioneCount";
import { StatoDichiarazioneFilter } from "./classi/statoDichiarazioneFilter";

const httpOptions = {
  headers: new HttpHeaders({ "Content-Type": "application/json" })
};

@Injectable({
  providedIn: "root"
})
export class AntimafiaService {
  res: Array<DichiarazioneAntimafia>;
  constructor(
    private http: HttpClient,
    private _configuration: Configuration,
    private messages: MessageService,
  ) { }

  public getDichiarazioniAntimafia(
    dichiarazioneAntimafia: DichiarazioneAntimafia
  ): Observable<Array<DichiarazioneAntimafia>> {
    // console.log("getDichiarazioniAntimafia di " + dichiarazioneAntimafia.azienda.cuaa);

    let dichiarazione = encodeURIComponent(
      JSON.stringify(dichiarazioneAntimafia)
    );

    return this.http.get<Array<DichiarazioneAntimafia>>(
      this._configuration.UrlGetRicercaAntimafia +
      "${dichiarazione}".replace("${dichiarazione}", dichiarazione)
    );
  }

  public getDichiarazioneAntimafia(
    idDichiarazione: string
  ): Observable<DichiarazioneAntimafia> {
    let res = this.http.get<DichiarazioneAntimafia>(
      this._configuration.UrlAntimafia +
      "${id}".replace("${id}", String(idDichiarazione))
    );

    return res;
  }

  public getDichiarazioneAntimafiaWithPdfFirmato(
    idDichiarazione: string
  ): Observable<DichiarazioneAntimafia> {
    let res = this.http.get<DichiarazioneAntimafia>(
      this._configuration.UrlAntimafiaExpansion.replace(
        "${idDichiarazioneAntimafia}",
        String(idDichiarazione)
      ) + "pdfFirmato"
    );
    return res;
  }

  public getDichiarazioneAntimafiaInScadenza(dichiarazioneAntimafia: DichiarazioneAntimafia
  ): Observable<Array<DichiarazioneAntimafia>> {
    let dichiarazione = encodeURIComponent(
      JSON.stringify(dichiarazioneAntimafia)
    );
    let res = this.http.get<Array<DichiarazioneAntimafia>>(
      this._configuration.UrlGetRicercaAntimafiaInScadenza.replace(
        "${parametri}", dichiarazione
      )
    );
    return res;
  }

  public aggiornaDichiarazioneAntimafia(
    dichiarazioneAntimafia: DichiarazioneAntimafia
  ): Observable<any> {
    let headers = new HttpHeaders({ "Content-Type": "application/json" });

    return this.http.put<any>(
      this._configuration.UrlAntimafia + dichiarazioneAntimafia.id,
      JSON.stringify(dichiarazioneAntimafia),
      { headers: headers }
    );
  }

  public chiudiAndRicreaDichiarazioneAntimafia(
    datiChiusuraExNovo:DatiChiusuraExNovoDichiarazioneAntimafia
  ): Observable<any> {
    let headers = new HttpHeaders({ "Content-Type": "application/json" });
    
    return this.http.post<any>(
      this._configuration.UrlAntimafiaChiudiRicreaDichiarazione(datiChiusuraExNovo.daChiudere.id),
      JSON.stringify(datiChiusuraExNovo),
      { headers: headers }
    );
  }

  public eliminaDichiarazioneAntimafia(
    idDichiarazione: string
  ): Observable<any> {
    return this.http.delete<DichiarazioneAntimafia>(
      this._configuration.UrlAntimafia +
      "${id}".replace("${id}", String(idDichiarazione))
    );
  }

  public creaDichiarazioneAntimafia(
    dichiarazioneAntimafia: DichiarazioneAntimafia
  ): Observable<any> {
    let headers = new HttpHeaders({ "Content-Type": "application/json" });

    return this.http.post<any>(
      this._configuration.UrlAntimafia,
      JSON.stringify(dichiarazioneAntimafia),
      { headers: headers }
    );
  }

  public getPersona(coficeFiscale: string): Observable<any> {
    const url = this._configuration.UrlGetAnagrafeTributaria.replace(
      "${codiceFiscale}",
      String(coficeFiscale)
    );
    return this.http.get(url);
  }

  public allegaFamiliariConviventi(
    allegatoFamiliariConviventi: AllegatoDicFamConv,
    documento: File,
    idDichiarazioneAntimafia: number
  ): Observable<any> {
    let formInput: FormData = new FormData();
    formInput.append("documento", documento);
    formInput.append(
      "datiFamiliareConvivente",
      JSON.stringify(allegatoFamiliariConviventi)
    );

    return this.http.put<any>(
      this._configuration.UrlAllegatofamiliariConviventi.replace(
        "${idDichiarazioneAntimafia}",
        idDichiarazioneAntimafia.toString()
      ),
      formInput
    );
  }

  public verificaFirma(file: File): Observable<any> {
    let formInput: FormData = new FormData();
    formInput.append("documentoFirmato", file);
    return this.http.post<any>(this._configuration.UrlVerificaFirma, formInput);
  }

  public rappresentatiLegali(url: string): Observable<any> {
    return this.http.get<any>(url);
  }

  public newProtocollaDichiarazioneAntimafiaAsync(
    idDichiarazione: string
  ): Observable<HttpResponse<any>> {
    return this.http.put<any>(
      this._configuration.UrlProtocollaDichiarazioneAntimafia.replace(
        "${idDichiarazioneAntimafia}",
        idDichiarazione.toString()
      ),
      null,
      { observe: 'response' }
    );
  }

  public protocollaDichiarazioneAntimafia(
    idDichiarazione: string
  ): Observable<DichiarazioneAntimafia> {
    return this.http.put<DichiarazioneAntimafia>(
      this._configuration.UrlProtocollaDichiarazioneAntimafia.replace(
        "${idDichiarazioneAntimafia}",
        idDichiarazione.toString()
      ),
      null
    );
  }

  public getStatoDichiarazioniCount(
    statiDichiarazione: StatoDichiarazioneFilter
  ): Observable<StatoDichiarazioneCount> {
    let stati = encodeURIComponent(JSON.stringify(statiDichiarazione));

    return this.http.get<StatoDichiarazioneCount>(
      this._configuration.UrlStatoDichiarazioneCount +
      "${stati}".replace("${stati}", stati)
    );
  }

  public avviaControllo(idList: number[]): Observable<any> {
    return this.http.post<any>(
      this._configuration.UrlAvviaControlloAntimafia,
      idList
    );
  }

  /*  public getStatoAvanzamentoProcesso(tipoProcesso: string, statoProcesso: string) {
      const params = encodeURIComponent(JSON.stringify({ tipoProcesso: tipoProcesso, statoProcesso: statoProcesso }));
      return this.http.get(this._configuration.urlControlloAntimafiaStatoAvanzamento + params);
    }
    */

  public getEsitoControlloAntimafia(
    idDichiarazioneAntimafia: number
  ): Observable<any> {
    return this.http.get<any>(
      this._configuration.UrlEsitoControlloAntimafia.replace(
        "${idDichiarazioneAntimafia}",
        idDichiarazioneAntimafia.toString()
      )
    );
  }

  public creaNotaDichiarazioneAntimafia(idDichiarazione: number, info: NotaDichiarazioneRifiutata): Observable<any> {
    let headers = new HttpHeaders({ "Content-Type": "application/json" });
    let body = {
      dataInserimento: new Date(),
      nota: JSON.stringify(info),
      tipoNota: TipoNotaEnum.RIFIUTO_DICHIARAZIONE_ANTIMAFIA
    }
    return this.http.put<any>(this._configuration.urlAntimafiaCreaNota.replace("${id}", idDichiarazione.toString()), JSON.stringify(body), { headers: headers });
  }

  public getNoteDichiarazioneAntimafia(idDichiarazione: number, tipoNota: string): Observable<Array<any>> {
    const params = encodeURIComponent(JSON.stringify({ tipoNota: tipoNota }));
    return this.http.get<Array<any>>(this._configuration.urlAntimafiaGetNote.replace("${id}", idDichiarazione.toString()) + params);
  }

  // sincronizza esiti BDNA in ags - asincrono
  // public sincronizzaDateBDNAAntimafia(dichiarazioniToUpdate: SincronizzazioneDateBdnaDto[]) {
  //   const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
  //   this.http.put<SincronizzazioneDateBdnaDto>(this._configuration.urlAgsPutEsitiAntimafia, JSON.stringify(dichiarazioniToUpdate), { headers: headers }).subscribe();
  // }

  // pdfFirmato - codificato base 64
  public downloadDichiarazioneAntimafia(pdfFirmato: any[], tipoPdfFirmato: string) {
    if (!pdfFirmato) {
      this.messages.add(A4gMessages.getToast("tst", A4gSeverityMessage.error, A4gMessages.DICHIARAZIONE_MANCANTE));
      return;
    }

    let size: number = pdfFirmato.toString().length;
    let byteCharacters: string = atob(pdfFirmato.toString());
    let byteArrays: any = [];
    for (let offset: number = 0; offset < byteCharacters.length; offset += size) {
      let slice: string = byteCharacters.slice(offset, offset + size);
      let byteNumbers: any = new Array(slice.length);
      for (let i: number = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i);
      }
      let byteArray: Uint8Array = new Uint8Array(byteNumbers);
      byteArrays.push(byteArray);
    }

    let blob: Blob = new Blob(byteArrays, { type: "blob" });
    // TODO: Prendi nome file dagli headers della chiamata http
    let nomeFile: string = "DomandaCertificazioneAntimafia.".concat(tipoPdfFirmato);
    FileSaver.saveAs(blob, nomeFile);
  }

  public inserisciProcedimenti(idDichiarazione: number, procedimenti: string[]): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(
      this._configuration.UrlProcedimentiDichiarazioneAntimafia(idDichiarazione),
      procedimenti,
      { headers: headers }
    );
  }

  public recuperaProcedimenti(idDichiarazione: number): Observable<string[]> {
    return this.http.get<any>(
      this._configuration.UrlProcedimentiDichiarazioneAntimafia(idDichiarazione)
    );
  }

  public downloadDichiarazioneAntimafiaCsv(dichiarazioneAntimafia: DichiarazioneAntimafia): Observable<any> {
    const headers = new HttpHeaders({ 'Accept': 'application/text' });
    let dichiarazione = encodeURIComponent(JSON.stringify(dichiarazioneAntimafia));

    let res = this.http.get(
      this._configuration.urlDownloadDichiarazioniAntimafiaCsv.replace("${parametri}", dichiarazione),
      { headers: headers, responseType: "text" }
    );
    return res;
  }

  public getAllegati(dichiarazione: DichiarazioneAntimafia): Array<CaricaCodiceFiscale> {
    if (dichiarazione && dichiarazione.datiDichiarazione && dichiarazione.datiDichiarazione.dettaglioImpresa
      && dichiarazione.datiDichiarazione.dettaglioImpresa.soggettiImpresa && dichiarazione.datiDichiarazione.dettaglioImpresa.soggettiImpresa.length) {
      const soggettiImpresaArray: Array<SoggettiImpresa> = dichiarazione.datiDichiarazione.dettaglioImpresa.soggettiImpresa;
      const cariche: Array<CaricaCodiceFiscale> = [];
      soggettiImpresaArray.forEach((soggetto: SoggettiImpresa) => {
        const caricheConCf = soggetto.carica
          .map((c: CaricaCodiceFiscale) => c = { codiceFiscale: soggetto.codiceFiscale, ...c })
          .filter((carica: CaricaCodiceFiscale) => carica.href != null && carica.href.trim() != '');
        cariche.push(...caricheConCf);
      });
      return cariche;
    }
    return [];
  }

  /** download allegati sequenziale */
  public downloadAllegati(cariche: Array<CaricaCodiceFiscale>) {
    const obsArray: Array<AllegatoMetadati> = [];
    cariche.forEach((element: CaricaCodiceFiscale, index: number) => {
      const allegatoMetadati: AllegatoMetadati = {
        observable: this.http.get(this._configuration.antimafia_server + element.href, { responseType: 'arraybuffer' }),
        index: index,
        codiceFiscale: element.codiceFiscale,
        tipoAllegato: element.tipoAllegato
      }
      obsArray.push(allegatoMetadati);
    });

    let metadati: AllegatoMetadati;
    concat(from([...obsArray])).pipe(concatMap((allegatoMetadati: AllegatoMetadati) => {
      metadati = allegatoMetadati;
      return allegatoMetadati.observable;
    })).subscribe(
      response => this.downLoadFile(response, "application/octect-stream", metadati.codiceFiscale, metadati.index.toString(), metadati.tipoAllegato),
      err => console.log(err));
  }

  /** download allegati non sequenziale */
  // public downloadAllegati(cariche: Array<CaricaCodiceFiscale>) {
  //   if (cariche && cariche.length) {
  //     cariche.forEach((element: CaricaCodiceFiscale, index: number) => {
  //       const url = this._configuration.antimafia_server + '/' + element.href;
  //       this.http.get(url, { responseType: 'arraybuffer' }).subscribe(
  //         response => this.downLoadFile(response, "application/octect-stream", element.codiceFiscale, index.toString(), element.tipoAllegato),
  //         err => console.log(err));
  //     });
  //   }
  // }

  private downLoadFile(data: any, type: string, cf: string, index: string, ext: string) {
    let blob = new Blob([data], { type });
    FileSaver.saveAs(blob, 'AllegatoFamiliariConviventi' + cf + '-' + index + '.' + ext);
  }

}
