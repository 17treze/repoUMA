import { HttpClient } from '@angular/common/http';
import { PresentaPrelievoDto } from './../models/dto/PresentaPrelievoDto';
import { PrelievoDto } from 'src/app/uma/core-uma/models/dto/PrelievoDto';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PrelieviFilter } from '../models/dto/PrelieviFilter';
import { HttpClientUmaCoreService } from './http-client-uma-core.service';
import { HttpHelperService } from '../../../a4g-common/services/http-helper.service';
import { DistributoreCarburanteDto } from '../../shared-uma/models/dto/DistributoreCarburanteDto';

@Injectable({
  providedIn: 'root'
})
export class HttpClientDistributoriService {

  readonly CTX_PATH = '/distributori';

  constructor(
    private http: HttpClient,
    private httpClientCore: HttpClientUmaCoreService,
    private httpHelperService: HttpHelperService
  ) { }

  getPrelieviByDistributore(idDistributore: string, filter?: PrelieviFilter): Observable<Array<PrelievoDto>> {
    const queryString = filter != null ? '?' + this.httpHelperService.buildQueryStringFromObject(filter) : '';
    return this.http.get<Array<PrelievoDto>>(`${this.urlDistributori()}/${idDistributore}/prelievi` + queryString);
  }

  salvaPrelievoByIdentificativoDistributore(identificativoDistributore: string, presentaPrelievoDto: PresentaPrelievoDto): Observable<number> {
    return this.http.post<number>(`${this.urlDistributori()}/${identificativoDistributore}/prelievi`, presentaPrelievoDto);
  }

  aggiornaPrelievoByIdentificativoDistributore(idDistributore: string, idPrelievo: string, prelievoDto: PrelievoDto): Observable<number> {
    return this.http.put<number>(`${this.urlDistributori()}/${idDistributore}/prelievi/${idPrelievo}`, prelievoDto);
  }

  aggiornaPrelieviByIdDistributore(idDistributore: string, prelievoDto: Array<PrelievoDto>): Observable<number> {
    return this.http.put<number>(`${this.urlDistributori()}/${idDistributore}/prelievi`, prelievoDto);
  }

  deletePrelievoByIdDistributore(idDistributore: string, idPrelievo: number): Observable<void> {
    return this.http.delete<void>(`${this.urlDistributori()}/${idDistributore}/prelievi/${idPrelievo}`);
  }

  getDistributori(campagna: string): Observable<Array<DistributoreCarburanteDto>> {
    return this.http.get<Array<DistributoreCarburanteDto>>(`${this.urlDistributori()}?campagna=${campagna}`);
  }

  private urlDistributori() {
    return this.httpClientCore.HOST + this.httpClientCore.BASE_PATH + this.httpClientCore.API_V1 + this.CTX_PATH;
  }
}
