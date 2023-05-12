import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClientUmaCoreService } from './http-client-uma-core.service';
import { PaginatorA4G } from 'src/app/a4g-common/interfaces/paginator.model';
import { Paginazione } from 'src/app/a4g-common/utility/paginazione';
import { CoefficienteDto, GruppoColtureDto, GruppoLavorazioneDto, LavorazioneDto } from '../models/dto/ConfigurazioneDto';

@Injectable({
  providedIn: 'root'
})
export class HttpClientConfigurazioneUmaService {

  readonly CTX_PATH = '/configurazioni';

  constructor(
    private http: HttpClient,
    private httpClientCore: HttpClientUmaCoreService
  ) { }

  public getGruppiColture(paginazione: Paginazione): Observable<PaginatorA4G<Array<GruppoColtureDto>>> {
    const data: any = { ...paginazione };
    return this.http.get<PaginatorA4G<Array<GruppoColtureDto>>>(`${this.urlBase()}/gruppi-colture`, { params: data });
  }

  public getGruppiLavorazioni(paginazione: Paginazione): Observable<PaginatorA4G<Array<GruppoLavorazioneDto>>> {
    const data: any = { ...paginazione };
    return this.http.get<PaginatorA4G<Array<GruppoLavorazioneDto>>>(`${this.urlBase()}/gruppi-lavorazioni`, { params: data });
  }

  public getLavorazioni(paginazione: Paginazione): Observable<PaginatorA4G<Array<LavorazioneDto>>> {
    const data: any = { ...paginazione };
    return this.http.get<PaginatorA4G<Array<LavorazioneDto>>>(`${this.urlBase()}/lavorazioni`, { params: data });
  }

  public postLavorazione(lavorazione: LavorazioneDto): Observable<number> {
    return this.http.post<number>(`${this.urlBase()}/lavorazioni`, lavorazione);
  }

  public getCoefficienti(paginazione: Paginazione): Observable<PaginatorA4G<Array<CoefficienteDto>>> {
    const data: any = { ...paginazione };
    return this.http.get<PaginatorA4G<Array<CoefficienteDto>>>(`${this.urlBase()}/coefficienti`, { params: data });
  }

  public postCoefficiente(coefficiente: CoefficienteDto): Observable<number> {
    return this.http.post<number>(`${this.urlBase()}/coefficienti`, coefficiente);
  }

  urlBase() {
    return this.httpClientCore.HOST + this.httpClientCore.BASE_PATH + this.httpClientCore.API_V1 + this.CTX_PATH;
  }

}