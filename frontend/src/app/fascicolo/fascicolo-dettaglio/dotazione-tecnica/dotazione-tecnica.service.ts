import { TipologiaCatasto } from './../../../a4g-common/classi/enums/dotazione.-tecnica/TipologiaCatasto.enum';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Configuration } from 'src/app/app.constants';
import { FiltroMacchina, MacchinaDto } from '../../creazione-fascicolo/dto/MacchinaDto';
import { FabbricatoDto } from 'src/app/a4g-common/classi/dto/dotazione-tecnica/FabbricatoDto';
import { DatiCatastaliDto, DettaglioFabbricatoDto } from 'src/app/a4g-common/classi/dto/dotazione-tecnica/DettaglioFabbricatoDto';

@Injectable({
  providedIn: 'root'
})
export class DotazioneTecnicaService {

  private urlDotazioneTecnica = `${this.configuration.dotazione_tecnica_server}`;

  constructor(
    private http: HttpClient,
    private configuration: Configuration
  ) { }

  getMacchineByCuaa(filtro: FiltroMacchina): Observable<Array<MacchinaDto>> {
    return this.http.get<Array<MacchinaDto>>(this.urlMacchina(filtro));
  }

  public getFabbricatiByCuaaAndIdValidazione(cuaa: string, idValidazione: string): Observable<FabbricatoDto[]> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.http.get<FabbricatoDto[]>(this.urlFabbricato(cuaa), { params: paramsHttp, responseType: 'json' });
  }

  public getFabbricatoByIdAndIdValidazione(cuaa: string, id: string, idValidazione: string): Observable<DettaglioFabbricatoDto> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.http.get<DettaglioFabbricatoDto>(this.urlFabbricato(cuaa) + `/${id}`, { params: paramsHttp });
  }

  urlMacchina(filtro: FiltroMacchina) {
    return `${this.urlDotazioneTecnica}/macchina?cuaa=${filtro.cuaa}&data=${filtro.data}&tipiCarburante=${filtro.tipiCarburante}`;
  }

  urlFabbricati() {
    return `${this.urlDotazioneTecnica}/fabbricati`;
  }

  public urlFabbricato(cuaa: string) {
    return `${this.urlDotazioneTecnica}/fascicolo/${cuaa}/fabbricati`;
  }

  public urlGetVerificaParticellaCatasto() {
    return `${this.urlDotazioneTecnica}/fascicolo/fabbricati/catasto`;
  }

  public urlGetElencoSubalterniParticellaCatasto() {
    return `${this.urlDotazioneTecnica}/fascicolo/fabbricati/catasto/elenco-subalterni-particella`;
  }

  public postFabbricato(cuaa: string, dettaglioFabbricatoDto: DettaglioFabbricatoDto): Observable<number> {
    return this.http.post<number>(this.urlFabbricato(cuaa), dettaglioFabbricatoDto);
  }

  public deleteFabbricato(cuaa: string, id: string): Observable<void> {
    return this.http.delete<void>(this.urlFabbricato(cuaa) + `/${id}`);
  }

  public verificaParticellaCatasto(
    particella: string, denominatore: string, tipologia: TipologiaCatasto, comuneCatastale: number, sub: string
  ): Observable<DatiCatastaliDto> {
    let paramsHttp = new HttpParams();
    paramsHttp = paramsHttp.set('numeroParticella', particella);
    if (denominatore != null) {
      paramsHttp = paramsHttp.set('denominatore', denominatore);
    }
    paramsHttp = paramsHttp.set('tipologia', tipologia);
    paramsHttp = paramsHttp.set('codiceComuneCatastale', String(comuneCatastale));
    if (sub != null) {
      paramsHttp = paramsHttp.set('subalterno', sub);
    }
    return this.http.get<DatiCatastaliDto>(this.urlGetVerificaParticellaCatasto(), { params: paramsHttp });
  }

  public getElencoSubalterniParticellaCatasto(particella: string, comuneCatastale: number): Observable<string[]> {
    let paramsHttp = new HttpParams();
    paramsHttp = paramsHttp.set('numeroParticella', particella);
    paramsHttp = paramsHttp.set('codiceComuneCatastale', String(comuneCatastale));
    return this.http.get<string[]>(this.urlGetElencoSubalterniParticellaCatasto(), { params: paramsHttp });
  }

}
