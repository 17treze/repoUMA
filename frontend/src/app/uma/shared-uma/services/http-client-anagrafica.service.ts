import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FascicoloAgsDto } from 'src/app/a4g-common/classi/FascicoloAgsDto';
import { Configuration } from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class HttpClientAnagraficaService {

constructor(
  private http: HttpClient,
  private configuration: Configuration
) { } 

public getFascicoloAgs(cuaa: string): Observable<FascicoloAgsDto> {
  return this.http.get<FascicoloAgsDto>(`${this.urlGetFascicoloAgsFromAnagrafica()}/${cuaa}`);
}

private urlGetFascicoloAgsFromAnagrafica() {
  return this.configuration.anagrafica_server +"/fascicolo/legacy";
}

}
