import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClasseFunzionaleDto, SottoTipoDto, TipologiaDto } from '../classi/dto/dotazione-tecnica/TipologiaDto';
import { AmbitoTipologia } from '../classi/enums/dotazione.-tecnica/AmbitoTipologia.enum';
import { HttpClientCoreService } from './http-client-core.service';
import { HttpHelperService } from './http-helper.service';

@Injectable({
  providedIn: 'root'
})
export class HttpClientTipologieService {

  readonly CTX_PATH = '/tipologie';

  constructor(
    private http: HttpClient,
    private httpClientCore: HttpClientCoreService,
    private httpHelperService: HttpHelperService
  ) { }

  // Recupera le tipologie di macchinari/fabbricati
  getTipologia(ambito: keyof typeof AmbitoTipologia): Observable<Array<TipologiaDto>> {
    const queryString = '?' + this.httpHelperService.buildQueryStringFromObject({ ambito });
    return this.http.get<Array<TipologiaDto>>(this.urlTipologie() + queryString);
  }

  // Recupera i sottotipi associati ad una tipologia di un fabbricati
  getSottoTipologieByIdTipologia(id: string): Observable<SottoTipoDto> {
    return this.http.get<SottoTipoDto>(this.urlTipologie() + `/${id}/sottotipi`);
  }

  // Recupera le classi funzionali associate ad una tipologia di un macchinario
  getClassiFunzionaliByIdTipologia(id: string): Observable<ClasseFunzionaleDto> {
    return this.http.get<ClasseFunzionaleDto>(this.urlTipologie() + `/${id}/classifunzionali`);
  }

  // Recupera i sottotipi associati ad un tipo di macchinario
  getSottoTipologieByIdClasseFunzionale(id: string): Observable<SottoTipoDto> {
    return this.http.get<SottoTipoDto>(this.urlTipologie() + `/${id}/sottotipi?ambito=MACCHINE`);
  }

  private urlTipologie() {
    return this.httpClientCore.HOST + this.httpClientCore.BASE_PATH_DOTAZIONE_TECNICA + this.httpClientCore.API_V1 + this.CTX_PATH;
  }
}
