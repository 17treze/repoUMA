import { Injectable } from '@angular/core';
import { Cached } from 'src/assets/decorators/cached';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Configuration } from 'src/app/app.constants';
import { DettaglioIstruttoria } from '../../../classi/DettaglioIstruttoria';
import { InterventoDuPremioAggregato } from '../../../classi/InterventoDuPremioAggregato';
import { ConfigurazioneRicevibilita } from '../../../classi/ConfigurazioneRicevibilita';
import { ConfigurazioneIstruttorie } from '../../dto/ConfigurazioneIstruttorie';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConfigurazioneIstruttoriaService {

  constructor(private http: HttpClient,
    private _configuration: Configuration) { }

  public countDomandeDU(istruttoria: string): Observable<number> {
    console.log('countDomandeDU');
    return this.http.get<number>(this._configuration.UrlCountDomandeDU + istruttoria);
  }

  public countDomandeDUPAC(parametri: string): Observable<number> {
    console.log('countDomandeDUPAC');
    return this.http.get<number>(this._configuration.UrlCountDomandeDUPAC + parametri);
  }

  @Cached()
  public caricaAnniCampagna(): Observable<Array<number>> {
    console.log('caricaAnniCampagna');
    return this.http.get<Array<number>>(this._configuration.UrlCaricaAnniCampagna);
  }

  public getConfigurazioneIstruttoriaDisaccoppiato(annoCampagna: number): Observable<DettaglioIstruttoria> {
    return this.http.get<DettaglioIstruttoria>(
      this._configuration.UrlDettaglioIstruttoria
        .replace('${annoCampagna}', String(annoCampagna))
    );
  }

  public getConfigurazioneIstruttoriaAcs(annoCampagna: number): Observable<InterventoDuPremioAggregato> {
    return this.http.get<InterventoDuPremioAggregato>(
      this._configuration.UrlConfIstruttoriaAcs
        .replace('${annoCampagna}', String(annoCampagna))
    );
  }

  public getConfigurazioneIstruttoriaAcz(annoCampagna: number): Observable<InterventoDuPremioAggregato> {
    return this.http.get<InterventoDuPremioAggregato>(
      this._configuration.UrlConfIstruttoriaAcz
        .replace('${annoCampagna}', String(annoCampagna))
    );
  }

  public setConfigurazioneIstruttoriaDisaccoppiato(annoCampagna: number, dettaglioIstruttoria: DettaglioIstruttoria): Observable<number> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<number>(
      this._configuration.UrlDettaglioIstruttoria
        .replace('${annoCampagna}', String(annoCampagna)),
      dettaglioIstruttoria,
      { headers: headers }
    );
  }

  public setConfigurazioneIstruttoriaAcs(annoCampagna: number, datiConfigurazioneAccoppiati: InterventoDuPremioAggregato): Observable<InterventoDuPremioAggregato> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<InterventoDuPremioAggregato>(
      this._configuration.UrlConfIstruttoriaAcs
        .replace('${annoCampagna}', String(annoCampagna)),
      datiConfigurazioneAccoppiati,
      { headers: headers }
    );
  }

  public setConfigurazioneIstruttoriaAcz(annoCampagna: number, datiConfigurazioneAccoppiati: InterventoDuPremioAggregato): Observable<InterventoDuPremioAggregato> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<InterventoDuPremioAggregato>(
      this._configuration.UrlConfIstruttoriaAcz
        .replace('${annoCampagna}', String(annoCampagna)),
      datiConfigurazioneAccoppiati,
      { headers: headers }
    );
  }

  public getConfigurazioneRicevibilita(annoCampagna: number): Observable<ConfigurazioneRicevibilita> {
    return this.http.get<ConfigurazioneRicevibilita>(
      this._configuration.UrlConfRicevibilita
        .replace('${annoCampagna}', String(annoCampagna))
    );
  }

  public setConfigurazioneIstruttoriaRicevibilita(annoCampagna: number, confRicevibilita: ConfigurazioneRicevibilita): Observable<ConfigurazioneRicevibilita> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<ConfigurazioneRicevibilita>(
      this._configuration.UrlConfRicevibilita
        .replace('${annoCampagna}', String(annoCampagna)),
      confRicevibilita,
      { headers: headers }
    );
  }

  public getConfigurazioneIstruttorie(annoCampagna: number): Observable<ConfigurazioneIstruttorie> {
    return this.http.get<ConfigurazioneIstruttorie>(
      this._configuration.UrlConfIstruttorie
        .replace('${annoCampagna}', String(annoCampagna))
    );
  }

  public setConfigurazioneIstruttorie(annoCampagna: number, confIstruttorie: ConfigurazioneIstruttorie): Observable<ConfigurazioneIstruttorie> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<ConfigurazioneIstruttorie>(
      this._configuration.UrlConfIstruttorie
        .replace('${annoCampagna}', String(annoCampagna)),
      confIstruttorie,
      { headers: headers }
    );
  }

}
