import { HttpHelperService } from '../../../a4g-common/services/http-helper.service';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClientUmaCoreService } from './http-client-uma-core.service';
import { CarburanteTotale } from '../models/dto/CarburanteTotale';
import { TrasferimentoDto } from '../models/dto/CarburanteRicevutoDto';
import { CarburanteDto } from '../models/dto/CarburanteDto';
import { AziendaDto } from '../models/dto/AziendaDto';

@Injectable({
  providedIn: 'root'
})
export class HttpClientTrasferimentiCarburanteService {
  readonly CTX_PATH = '/trasferimenti';

  constructor(
    private http: HttpClient,
    private httpClientCore: HttpClientUmaCoreService,
    private httpHelperService: HttpHelperService
  ) { }

  postTrasferimento(idRichiestaMittente: String, idRichiestaDestinatario: String, carburanteTrasferito: CarburanteDto): Observable<number> {
    return this.http.post<number>(this.urlTrasferimenti(), {
      idRichiestaMittente: idRichiestaMittente, idRichiestaDestinatario: idRichiestaDestinatario, carburanteTrasferito: carburanteTrasferito
    });
  }

  putTrasferimento(id: String, carburanteTrasferito: CarburanteDto): Observable<number> {
    return this.http.put<number>(`${this.urlTrasferimenti()}/` + id, carburanteTrasferito);
  }

  getCarburanteRicevuto(cuaa: string, campagna: string): Observable<CarburanteTotale<TrasferimentoDto>> {
    const cuaaDestinatario = cuaa;
    const params = '?' + this.httpHelperService.buildQueryStringFromObject({ cuaaDestinatario, campagna });
    return this.http.get<CarburanteTotale<TrasferimentoDto>>(`${this.urlTrasferimenti()}` + params);
  }

  getCarburanteTrasferito(cuaa: string, campagna: string): Observable<CarburanteTotale<TrasferimentoDto>> {
    const cuaaMittente = cuaa;
    const params = '?' + this.httpHelperService.buildQueryStringFromObject({ cuaaMittente, campagna });
    return this.http.get<CarburanteTotale<TrasferimentoDto>>(`${this.urlTrasferimenti()}` + params);
  }

  deleteTraseferito(id: string): Observable<void> {
    return this.http.delete<void>(`${this.urlTrasferimenti()}/${id}`);
  }
  
  validaTrasferimento(idRichiestaMittente: String, idRichiestaDestinatario: String): Observable<AziendaDto> {
    return this.http.post<AziendaDto>(`${this.urlTrasferimenti()}/valida`, {idRichiestaMittente: idRichiestaMittente, idRichiestaDestinatario: idRichiestaDestinatario});
  }

  urlTrasferimenti() {
    return this.httpClientCore.HOST + this.httpClientCore.BASE_PATH + this.httpClientCore.API_V1 + this.CTX_PATH;
  }
}
