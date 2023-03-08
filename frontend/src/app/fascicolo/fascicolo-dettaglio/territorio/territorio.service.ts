import { ConduzioneTerreniDto, DocumentoConduzioneDto, TipoDocumentoConduzioneDto } from './../../../a4g-common/classi/dto/dotazione-tecnica/ConduzioneDto';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { TipoConduzioneDto } from 'src/app/a4g-common/classi/dto/dotazione-tecnica/ConduzioneDto';
import { SottotipoConduzioneDto } from 'src/app/a4g-common/classi/dto/dotazione-tecnica/ConduzioneDto'; 
import { Configuration } from 'src/app/app.constants';
import { formatDate } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class TerritorioService {
  private urlTerritorio = `${this.configuration.territorio_server}`;
  private urlConduzioneTerreno = `${this.urlTerritorio}/conduzione-terreno`;

  constructor(
    private http: HttpClient,
    private configuration: Configuration
  ) { }

  // URL
  public urlTipoConduzione() {
    return `${this.urlConduzioneTerreno}/elenco-tipi-conduzione`;
  }

  // URL
  private urlSottoTipoConduzione(idTipoConduzione: number) {
    return `${this.urlConduzioneTerreno}/${idTipoConduzione}/sottotipologie/`;
  }

  // URL
  private urlTipoDocumentiConduzione(idTipoConduzione: number) {
    return `${this.urlConduzioneTerreno}/sottotipologia/${idTipoConduzione}/sottotipodocumenti/`;
  }

  private urlTitoloConduzione(cuaa: string) {
    return `${this.urlConduzioneTerreno}/salva/${cuaa}`;
  }

  // METODI
  public getTipoConduzione(): Observable<Array<TipoConduzioneDto>> {
    return this.http.get<Array<TipoConduzioneDto>>(this.urlTipoConduzione());
  }

  public getSottoTipoConduzione(idTipoConduzione: number): Observable<Array<SottotipoConduzioneDto>> {
    return this.http.get<Array<SottotipoConduzioneDto>>(this.urlSottoTipoConduzione(idTipoConduzione));
  }

  public getTipoDocumentoConduzione(idTipoDocumentoConduzione: number): Observable<Array<TipoDocumentoConduzioneDto>> {
    return this.http.get<Array<TipoDocumentoConduzioneDto>>(this.urlTipoDocumentiConduzione(idTipoDocumentoConduzione));
  }

  public putTitoloConduzione(cuaa: string, conduzioneTerreniDto: ConduzioneTerreniDto): Observable<void> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<void>(this.urlTitoloConduzione(cuaa), JSON.stringify(conduzioneTerreniDto), { headers: headers });
  }
}