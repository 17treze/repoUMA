import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Configuration } from 'src/app/app.constants';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class SincronizzazioneAntimafiaService {

  constructor(
    private http: HttpClient,
    private _configuration: Configuration
  ) { }


  public getSincronizzazioneAntimafia(id: number): Observable<any> {
    return this.http.get<any>(this._configuration.urlSincronizzazioneAntimafia.replace("${id}", id.toString()));
  }

  public putSincronizzazioneAntimafia(id: number, body: any): Observable<any> {
    let headers = new HttpHeaders({ "Content-Type": "application/json" });
    return this.http.put<any>(this._configuration.urlSincronizzazioneAntimafia.replace("${id}", id.toString()), JSON.stringify(body), { headers: headers });
  }

  public postSincronizzazioneAntimafia(body: any): Observable<any> {
    let headers = new HttpHeaders({ "Content-Type": "application/json" });
    return this.http.post(this._configuration.urlPostSincronizzazioneAntimafia, JSON.stringify(body), { headers: headers });
  }
}
