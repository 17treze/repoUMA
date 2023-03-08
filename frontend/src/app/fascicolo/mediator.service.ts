import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DettaglioMacchinaDto } from '../a4g-common/classi/dto/dotazione-tecnica/DettaglioMacchinaDto';
import { Configuration } from '../app.constants';
import { EsitoControlloDto } from './creazione-fascicolo/dto/EsitoControlloDto';
import { AllegatoMandato, MandatoDto } from './creazione-fascicolo/dto/MandatoDto';

@Injectable()
export class MediatorService {

  constructor(
    private httpClient: HttpClient,
    private configuration: Configuration,
  ) { }

  mediator_server = `${this.configuration.mediator_server}/fascicolo`;

  private urlGetEsitiControlloCompletezza(cuaa: string) {
    return `${this.mediator_server}/${cuaa}/controllo-completezza`;
  }

  private urlPutStartControlloCompletezza(cuaa: string) {
    return `${this.mediator_server}/${cuaa}/start-controllo-completezza`;
  }

  private UrlGetReportSchedaValidazione(cuaa: string) {
    return `${this.mediator_server}/${cuaa}/report-scheda-validazione`;
  }

  private UrlGetReportSchedaValidazioneBozza(cuaa: string) {
    return `${this.mediator_server}/${cuaa}/report-scheda-validazione-bozza`;
  }

  private UrlPostMigraFascicolo(cuaa: string) {
    return `${this.mediator_server}/${cuaa}/migra`;
  }

  private urlPostMacchina(cuaa: string) {
    return `${this.mediator_server}/dotazionetecnica/${cuaa}/macchine`;
  }

  private urlDeleteMacchina(cuaa: string, id: string) {
    return `${this.mediator_server}/dotazionetecnica/${cuaa}/macchine/${id}`;
  }

  private urlPutAnnullaIterValidazione(cuaa: string) {
    return `${this.mediator_server}/${cuaa}/annulla-iter-validazione`;
  }

  public startControlloCompletezza(cuaa: string): Observable<void> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.httpClient.put<void>(this.urlPutStartControlloCompletezza(cuaa), headers);
  }

  public getEsitiControlloCompletezza(cuaa: string): Observable<Map<string, EsitoControlloDto>> {
    return this.httpClient.get<Map<string, EsitoControlloDto>>(this.urlGetEsitiControlloCompletezza(cuaa));
  }

  public getReportSchedaValidazione(cuaa: string): Observable<Blob> {
    return this.httpClient.get(this.UrlGetReportSchedaValidazione(cuaa), { responseType: 'blob' });
  }

  public getReportSchedaValidazioneBozza(cuaa: string): Observable<Blob> {
    return this.httpClient.get(this.UrlGetReportSchedaValidazioneBozza(cuaa), { responseType: 'blob' });
  }

  public deleteEsitiControlloCompletezza(cuaa: string): Observable<void> {
    return this.httpClient.delete<void>(this.urlGetEsitiControlloCompletezza(cuaa));
  }

  public migraFascicolo(mandato: MandatoDto, migraModoPagamento: boolean, migraMacchinari: boolean, migraFabbricati: boolean): Observable<any> {
    const formInput: FormData = new FormData();
    formInput.append('codiceFiscaleRappresentante', mandato.codiceFiscaleRappresentante);
    formInput.append('contratto', mandato.contratto);
    formInput.append('identificativoSportello', mandato.identificativoSportello.toString());
    mandato.allegati.forEach((el: AllegatoMandato) => {
      formInput.append('allegati', el.file, el.descrizione);
    });
    formInput.append('migraModoPagamento', migraModoPagamento ? 'true' : 'false');
    formInput.append('migraMacchinari', migraMacchinari ? 'true' : 'false');
    formInput.append('migraFabbricati', migraFabbricati ? 'true' : 'false');

    return this.httpClient.post(
      this.UrlPostMigraFascicolo(mandato.codiceFiscale),
      formInput, { responseType: 'text' });
  }

  public postMacchina(cuaa: string, dettaglioMacchinaDto: DettaglioMacchinaDto, documento: { file: File, name: string }): Observable<number> {
    const formData = new FormData();
    if(documento != null){
      formData.append('documento', documento.file);
    }
    formData.append('dati', new Blob([JSON.stringify(dettaglioMacchinaDto)], { type: "application/json" }));
    return this.httpClient.post<number>(this.urlPostMacchina(cuaa), formData);
  }

// Elimina un macchinario
deleteMacchinaById(cuaa: string, idMacchina: string): Observable<void> {
  return this.httpClient.delete<void>(`${this.urlDeleteMacchina(cuaa, idMacchina)}`);
}

  public annullaIterValidazione(cuaa: string): Observable<void> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.httpClient.put<void>(this.urlPutAnnullaIterValidazione(cuaa), headers);
  }

}
