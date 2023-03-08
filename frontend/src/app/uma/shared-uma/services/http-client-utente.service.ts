import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpClientUmaCoreService } from '../../core-uma/services/http-client-uma-core.service';
import { Observable } from 'rxjs';
import { UtenteDistributoreDto } from '../../core-uma/models/dto/DistributoreDto';

@Injectable({
  providedIn: 'root'
})
export class HttpClientUtenteService {

  readonly BASE_PATH = 'a4gutente';
  readonly CTX_PATH = '/utenti';

  constructor(
    private http: HttpClient,
    private httpClientCore: HttpClientUmaCoreService
  ) { }

  public getDistributori(campagna: string): Observable<Array<UtenteDistributoreDto>> {
    return this.http.get<Array<UtenteDistributoreDto>>(`${this.urlUtente()}/utente/distributori?campagna=${campagna}`);
  }

  private urlUtente() {
    return this.httpClientCore.HOST + this.BASE_PATH + this.httpClientCore.API_V1 + this.CTX_PATH;
  }
}
