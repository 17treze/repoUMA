import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FASCICOLO_SERVER, ISTRUTTORIA_SERVER } from '../../../app/app.constants';
import { DichiarazioneAntimafia } from './models/dichiarazione-antimafia';
import { DomandaCollegata } from './models/domanda-collegata';
import { DomandaCollegataFilter } from './models/domanda-collegata-filter';
import { Nota } from './models/nota';

@Injectable({
  providedIn: 'root',
})
export class DichiarazioneAntimafiaService {
  public getDichiarazioneUrl = FASCICOLO_SERVER + 'antimafia/domanda/${cuaa}';
  public getNoteUrl = FASCICOLO_SERVER + 'antimafia/${id}/note';
  public getAntimafiaDomandeCollegateUrl =
    ISTRUTTORIA_SERVER + 'antimafia/domandecollegate';

  constructor(protected http: HttpClient) {}

  public getDichiarazioneAntimafia(
    cuaa: string
  ): Observable<DichiarazioneAntimafia> {
    return this.http.get<DichiarazioneAntimafia>(
      this.getDichiarazioneUrl.replace('${cuaa}', String(cuaa))
    );
  }

  public getAntimafiaNote(id: number): Observable<Array<Nota>> {
    const noteFilter = {
      tipoNota: 0,
      chiaveEsterna: '',
    };
    const params = new HttpParams().set('params', JSON.stringify(noteFilter));
    return this.http.get<Array<Nota>>(
      this.getNoteUrl.replace('${id}', String(id)),
      { params: params }
    );
  }

  public getAntimafiaDomandeCollegate(
    cuaa: string,
    tipoDomanda: string
  ): Observable<Array<DomandaCollegata>> {
    const filter: DomandaCollegataFilter = {
      idDomanda: undefined,
      cuaa: cuaa,
      tipoDomanda: tipoDomanda,
      campagna: undefined,
    };

    const params = new HttpParams().set('params', JSON.stringify(filter));
    return this.http.get<Array<DomandaCollegata>>(
      this.getAntimafiaDomandeCollegateUrl,
      {
        params: params,
      }
    );
  }
}
