import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Configuration } from '../app.constants';
import { DatiCAA } from './creazione-fascicolo/dto/DatiSportelloCAA';

@Injectable({
  providedIn: 'root'
})
export class CentroAssistenzaAgricolaService {

  constructor(
    private httpClient: HttpClient,
    private configuration: Configuration
  ) { }

  anagrafica_server_mandato = `${this.configuration.anagrafica_server}/caa`;

  public getUrlListaCaaConSportelli() {
    return `${this.anagrafica_server_mandato}/sportelli`;
  }

  public getListaCaaConSportelli(): Observable<DatiCAA[]> {
    const url = this.getUrlListaCaaConSportelli();
    return this.httpClient.get<DatiCAA[]>(url)
      .pipe(map(resp => {
        if (resp) {
          const richiesteDtoList: DatiCAA[] = [];
          for (const richiesta of resp) {
            const richiestaDto: DatiCAA = DatiCAA.toDto(richiesta);
            richiesteDtoList.push(richiestaDto);
          }
          return richiesteDtoList;
        }
        return resp;
      }));
  }

}
