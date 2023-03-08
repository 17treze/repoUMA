import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { DataService } from 'src/app/a4g-common/services/data.service';
import { Allegati } from '../models/detailgis/allegati.model';
import { GisCostants } from '../shared/gis.constants';
import { AllegatiGisService } from './allegati-gis.service';

@Injectable()
export class AllegatiDichiaratoGisService extends AllegatiGisService {

  constructor(
    private http: HttpClient,
    private gisConstants: GisCostants,
    private dataService: DataService
  ) {
    super();
  }

  getAllegato(idDichiarato, idDocumento): Observable<any> {
    const url = this.gisConstants.pathSuoloDichiarato + '/' + idDichiarato + '/documentoDichiarato/' + idDocumento;

    return this.http
      .get(url, { responseType: 'blob', observe: 'response' })
      .pipe(
        tap(e => this.dataService.dichiarato.allegato.getted.next(e))
      );
  }

  deleteAllegato(idDichiarato, idDocumento): Observable<any> {
    const url = this.gisConstants.pathSuoloDichiarato + '/' + idDichiarato + '/documentoDichiarato/' + idDocumento;

    return this.http
      .delete(url, { responseType: 'text', observe: 'response' })
      .pipe(
        tap(e => this.dataService.dichiarato.allegato.deleted.next(e))
      );
  }

  public uploadFile(idDichiarato, params, uploadedFile): Observable<any> {
    const url = this.gisConstants.pathSuoloDichiarato + '/' + idDichiarato + '/documentoDichiarato/';

    let formData = new FormData();

    formData.append('descrizione', params.descrizione);
    formData.append('dimensione', params.dimensione);
    formData.append('profiloUtente', params.profiloUtente);
    formData.append('file', uploadedFile);

    return this.http
      .post<any>(url, formData, { observe: 'response' })
      .pipe(
        tap(e => this.dataService.dichiarato.allegato.uploaded.next(e))
      );
  }

  getListaAllegati(idDichiarato, filter): Observable<Allegati> {
    const url = this.gisConstants.pathSuoloDichiarato + '/' + idDichiarato + '/documentiDichiarato/';

    let params = new HttpParams()
      .set('numeroElementiPagina', filter.numeroElementiPagina)
      .set('pagina', filter.pagina);

    return this.http
      .get<Allegati>(url, { params })
      .pipe(
        tap(e => this.dataService.dichiarato.allegato.listed.next(e))
      );
  }
}