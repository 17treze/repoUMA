import { Observable } from 'rxjs';

export abstract class AllegatiGisService {
  
  abstract getAllegato(idRiferimento, idDocumento): Observable<any>;
  abstract deleteAllegato(idRiferimento, idDocumento): Observable<any>;
  abstract uploadFile(idRiferimento, params, uploadedFile): Observable<any>;
  abstract getListaAllegati(idRiferimento, filter): Observable<any>
}