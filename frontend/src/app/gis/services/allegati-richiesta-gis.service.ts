import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { DataService } from 'src/app/a4g-common/services/data.service';
import { Allegati } from '../models/detailgis/allegati.model';
import { GisCostants } from '../shared/gis.constants';
import { AllegatiGisService } from './allegati-gis.service';

@Injectable()
export class AllegatiRichiestaGisService extends AllegatiGisService {
  
  constructor(
    private http: HttpClient,
    private gisConstants: GisCostants,
    private dataService: DataService
  ) {
    super();
  }

  getAllegato(idRichiesta, idDocumento): Observable<any> {
    const url = this.gisConstants.pathRichiestaModificaSuolo + '/' + idRichiesta + '/documenti/' + idDocumento;

    return this.http
      .get(url, { responseType: 'blob', observe: 'response' })
      .pipe(
        tap(e => this.dataService.richiesta.allegato.getted.next(e))
      );
  }

  deleteAllegato(idRichiesta, idDocumento): Observable<any> {
    const url = this.gisConstants.pathRichiestaModificaSuolo + '/' + idRichiesta + '/documenti/' + idDocumento;

    return this.http
      .delete(url, { responseType: 'text', observe: 'response' })
      .pipe(
        tap(e => this.dataService.richiesta.allegato.deleted.next(e))
      );
  }

  public uploadFile(idRichiesta, params, uploadedFile): Observable<any> {
    const url = this.gisConstants.pathRichiestaModificaSuolo + '/' + idRichiesta + '/documenti/';

    let formData = new FormData();

    formData.append('descrizione', params.descrizione);
    formData.append('dimensione', params.dimensione);
    formData.append('profiloUtente', params.profiloUtente);
    formData.append('file', uploadedFile);

    return this.http
      .post<any>(url, formData, { observe: 'response' })
      .pipe(
        tap(e => this.dataService.richiesta.allegato.uploaded.next(e))
      );
  }

  getListaAllegati(idRichiesta, filter): Observable<Allegati> {
    const url = this.gisConstants.pathRichiestaModificaSuolo + '/' + idRichiesta + '/documenti/';

    let params = new HttpParams()
      .set('numeroElementiPagina', filter.numeroElementiPagina)
      .set('pagina', filter.pagina);

    return this.http
      .get<{ risultati: Allegati }>(url, { params })
      .pipe(
        map(response => response.risultati),
        tap(e => this.dataService.richiesta.allegato.listed.next(e))
      );
  }
}