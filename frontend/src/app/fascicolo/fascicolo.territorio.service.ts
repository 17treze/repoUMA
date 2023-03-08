import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Configuration } from '../app.constants';
import { ConduzioneDto } from './creazione-fascicolo/dto/ConduzioneDto';

@Injectable({
  providedIn: 'root'
})
export class FascicoloTerritorioService {

  constructor(
    private httpClient: HttpClient,
    private configuration: Configuration) { }

  private urlFascicoloTerritorio = `${this.configuration.fascicolo_territorio_server}/fascicoli`;
  private urlFascicoloTerritorioPrivate = `${this.urlFascicoloTerritorio}/private`;

  private getUrlControlloCompletezza(cuaa: string) {
    return `${this.urlFascicoloTerritorio}/${cuaa}/controllo-completezza`;
  }

  private getOrPutUrlConduzioneTerreni(cuaa: string) {
    return `${this.configuration.fascicolo_territorio_server}/conduzione-terreno/${cuaa}`;
  }

  private getUrlControlloPianoColturaleGrafico(cuaa: string) {
    return `${this.urlFascicoloTerritorioPrivate}/${cuaa}/controllo-piano-colturale-grafico`;
  }

  // @Cached()
  public getControlloCompletezza(cuaa: string): Observable<Map<string, boolean>> {
    console.log("sending request to: " + this.getUrlControlloCompletezza(cuaa));
    return this.httpClient.get<Map<string, boolean>>(this.getUrlControlloCompletezza(cuaa));
  }

  public getConduzioneTerreni(cuaa: string): Observable<ConduzioneDto[]> {
    return this.httpClient.get<ConduzioneDto[]>(this.getOrPutUrlConduzioneTerreni(cuaa))
      .pipe(
        tap((retVal: any) => {
          return ConduzioneDto.toDtos(retVal);
        })
      );
  }

  public getControlloPianoColturaleGrafico(cuaa: string): Observable<number> {
    console.log("sending request to: " + this.getUrlControlloPianoColturaleGrafico(cuaa));
    return this.httpClient.get<number>(this.getUrlControlloPianoColturaleGrafico(cuaa));
  }

  public putSalvaConduzioneTerreni(cuaa: string, jsonConduzioniTerreni: ConduzioneDto[]): Observable<void> {
    return this.httpClient.put<void>(this.getOrPutUrlConduzioneTerreni(cuaa), jsonConduzioniTerreni);
  }

}
