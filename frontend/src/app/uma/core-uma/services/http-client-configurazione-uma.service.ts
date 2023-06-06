import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClientUmaCoreService } from './http-client-uma-core.service';
import { PaginatorA4G } from 'src/app/a4g-common/interfaces/paginator.model';
import { Paginazione } from 'src/app/a4g-common/utility/paginazione';
import { CoefficienteDto, GruppoColtureDto, GruppoFabbricatoDto, GruppoLavorazioneDto, LavorazioneDto } from '../models/dto/ConfigurazioneDto';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root'
})
export class HttpClientConfigurazioneUmaService {

  readonly CTX_PATH = '/configurazioni';

  constructor(
    private http: HttpClient,
    private oauthService: OAuthService,
    private httpClientCore: HttpClientUmaCoreService
  ) { }

  public getAccessToken() {
    return this.oauthService.getAccessToken();
  }

  public getGruppiColture(paginazione: Paginazione): Observable<PaginatorA4G<Array<GruppoColtureDto>>> {
    const data: any = { ...paginazione };
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<PaginatorA4G<Array<GruppoColtureDto>>>(`${this.urlBase()}/gruppi-colture`, { params: data, headers: headers });
  }

  public getGruppiFabbricato(paginazione: Paginazione): Observable<PaginatorA4G<Array<GruppoFabbricatoDto>>> {
    const data: any = { ...paginazione };
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<PaginatorA4G<Array<GruppoFabbricatoDto>>>(`${this.urlBase()}/gruppi-fabbricati`, { params: data, headers: headers });
  }

  public getGruppiLavorazioni(paginazione: Paginazione): Observable<PaginatorA4G<Array<GruppoLavorazioneDto>>> {
    const data: any = { ...paginazione };
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<PaginatorA4G<Array<GruppoLavorazioneDto>>>(`${this.urlBase()}/gruppi-lavorazioni`, { params: data, headers: headers });
  }

  public postGruppoLavorazione(gruppoLavorazione: GruppoLavorazioneDto): Observable<number> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.post<number>(`${this.urlBase()}/gruppi-lavorazioni`, gruppoLavorazione, { headers: headers });
  }

  public getLavorazioni(paginazione: Paginazione): Observable<PaginatorA4G<Array<LavorazioneDto>>> {
    const data: any = { ...paginazione };
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<PaginatorA4G<Array<LavorazioneDto>>>(`${this.urlBase()}/lavorazioni`, { params: data, headers: headers });
  }

  public postLavorazione(lavorazione: LavorazioneDto): Observable<number> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.post<number>(`${this.urlBase()}/lavorazioni`, lavorazione, { headers: headers });
  }

  public getCoefficienti(paginazione: Paginazione): Observable<PaginatorA4G<Array<CoefficienteDto>>> {
    const data: any = { ...paginazione };
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<PaginatorA4G<Array<CoefficienteDto>>>(`${this.urlBase()}/coefficienti`, { params: data, headers: headers });
  }

  public postCoefficiente(coefficiente: CoefficienteDto): Observable<number> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.post<number>(`${this.urlBase()}/coefficienti`, coefficiente, { headers: headers });
  }

  urlBase() {
    return this.httpClientCore.HOST + this.httpClientCore.BASE_PATH + this.httpClientCore.API_V1 + this.CTX_PATH;
  }

}