import { PoligoniSuoloDaAdl } from './../models/poligoniSuolo/poligoniSuoloDaAdl.model';
import { MapService } from './../components/mappa/map.service';
import { Injectable } from '@angular/core';
import { GisCostants } from './../shared/gis.constants';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { EsitoLavorazioneDichiarato } from '../shared/EsitoLavorazioneDichiarato.enum';
import { StatoLavorazioneSuolo } from '../shared/StatoLavorazioneSuolo.enum';
import { PropertyLayer } from '../shared/PropertyLayer.enum';
@Injectable()
export class CreazioneLavorazioneService {
  pathLavorazione: string;
  private pathAssociaSuoloDichiaratoAllaLavorazione: string;
  private pathRemoveSuoloDichiaratoAllaLavorazione: string;
  private pathSuoloDichiarato: string;
  private pathRemovePoligonoAllaLavorazione: string;
  private pathUpdateLavorazione: string;
  private pathLavorazioneWorkspace: string;
  constructor(private http: HttpClient, private gisConstants: GisCostants, private mapService: MapService) {
    this.pathLavorazione = this.gisConstants.pathLavorazioneSuolo;
    this.pathSuoloDichiarato = this.gisConstants.pathSuoloDichiarato;
  }

  insertLavorazione(annoCampagna): Observable<any> {
    return this.http.post<any>(this.pathLavorazione, { annoCampagna }, { observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  deleteLavorazione(idLavorazione): Observable<any> {
    const url = this.pathLavorazione + '/' + idLavorazione;
    return this.http.delete(url, { responseType: 'text', observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  getLavorazioneSuolo(idLavorazione): Observable<any> {
    const URL = this.pathLavorazione + '/' + idLavorazione;
    return this.http.get<any[]>(URL, {}).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  getLavorazioniSuoloNonConcluse(params): Observable<any> {
    const URL = this.pathLavorazione + '/nonConcluse';
    return this.http.get<any[]>(URL, { params }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  putAssociaSuoloDichiaratoALavorazione(idLavorazione, idSuoloDichiarato): Observable<any> {

    this.pathAssociaSuoloDichiaratoAllaLavorazione = this.pathSuoloDichiarato + '/' + idSuoloDichiarato + '/associaLavorazione';
    const dto = {
      'idLavorazione': idLavorazione
    };
    return this.http.put<any>(this.pathAssociaSuoloDichiaratoAllaLavorazione, dto, { observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  removeSuoloDichiaratoFromLavorazione(idLavorazione, idSuoloDichiarato): Observable<any> {
    const dto = {
      'idLavorazione': idLavorazione
    };
    this.pathRemoveSuoloDichiaratoAllaLavorazione = this.pathSuoloDichiarato + '/' + idSuoloDichiarato + '/rimuoviLavorazione';
    return this.http.put<any>(this.pathRemoveSuoloDichiaratoAllaLavorazione, dto, { observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  removePoligonoFromLavorazione(idLavorazione, idPoligono): Observable<any> {
    const dto = {
      'idLavorazioneInCorso': {
        'id': idLavorazione
      }
    };
    this.pathRemovePoligonoAllaLavorazione = this.pathLavorazione + '/' + idPoligono + '/rimuoviAssociazionePoligono';
    return this.http.put<any>(this.pathRemovePoligonoAllaLavorazione, dto, { observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  ricercaSuoloVigente(filterData): Observable<any> {
    const URL = this.pathLavorazione + '/' + filterData.idLavorazione + '/suolo';
    const params = new HttpParams().set('numeroElementiPagina', filterData.numeroElementiPagina).set('pagina', filterData.pagina);
    return this.http.get<any[]>(URL, { params }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  validaLavorazione(idLavorazione): Observable<any> {
    const URL = this.pathLavorazione + '/' + idLavorazione + '/validate';
    return this.http.get<any[]>(URL, {}).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  validaLavorazioneInCorso(idLavorazione): Observable<any> {
    const URL = this.pathLavorazione + '/' + idLavorazione + '/validaLavorazioneInCorso';
    return this.http.get<any[]>(URL, {}).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  putLavorazioneSuolo(idLavorazione, body): Observable<any> {
    this.pathUpdateLavorazione = this.pathLavorazione + '/' + idLavorazione;
    return this.http.put<any>(this.pathUpdateLavorazione, body, { observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  avviaLavorazione(idLavorazione, verificaPascoli): Observable<any> {
    this.pathLavorazioneWorkspace = this.pathLavorazione + '/' + idLavorazione + '/workspace';
    return this.http.post<any>(this.pathLavorazioneWorkspace, null,
      { observe: 'response', params: { 'verificaPascoli': verificaPascoli.toString() } }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  associaSuoloDaDichiarato(idLavorazione): Observable<any> {
    this.pathLavorazioneWorkspace = this.pathLavorazione + '/' + idLavorazione + '/associaSuoloDaDichiarato';
    return this.http.post<any>(this.pathLavorazioneWorkspace, { 'versione': 0 }, { observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  salvaWorkspace(idLavorazione, body): Observable<any> {
    const pathLavorazioneWorkspace = this.pathLavorazione + '/' + idLavorazione + '/workspace';

    return this.http.put<any>(pathLavorazioneWorkspace, body, { observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  updateAdleClipsuADL(idLavorazione, body): Observable<any> {
    const pathLavorazioneADL = this.pathLavorazione + '/' + idLavorazione + '/ADL';

    return this.http.put<any>(pathLavorazioneADL, body, { observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  cercaSuoloVigenteDaClickInMappa(idLavorazione, body): Observable<any> {
    const pathLavorazioneWorkspace = this.pathLavorazione + '/' + idLavorazione + '/aggiungiPoligonoDaPunto';

    return this.http.put<any>(pathLavorazioneWorkspace, body, { observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  cercaSuoloDichiaratoDaClickInMappa(idLavorazione, body): Observable<any> {
    const pathLavorazioneWorkspace = this.pathLavorazione + '/' + idLavorazione + '/aggiungiPoligonoDichiaratoDaPunto';

    return this.http.put<any>(pathLavorazioneWorkspace, body, { observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  putAssociaEsitoSuoloDichiaratoLavorazione(idSuoloDichiarato, esito: EsitoLavorazioneDichiarato): Observable<any> {
    const pathAssociaEsitoSuoloDichiaratoLavorazione = this.pathSuoloDichiarato + '/' + idSuoloDichiarato + '/setEsitoDichiarato';
    const params = new HttpParams().set('esito', esito);
    /*let dto = {
      'idLavorazione': idLavorazione
    };*/
    return this.http.put<any>(pathAssociaEsitoSuoloDichiaratoLavorazione, {}, { params, observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  putModificaStatoLavorazioneInModifica(idLavorazione, statoLavorazioneInModifica: StatoLavorazioneSuolo): Observable<any> {
    const pathModificaLavorazione = this.pathLavorazione + '/' + idLavorazione + '/lavorazioneInModifica';
    const params = new HttpParams().set('statoLavorazioneInModifica', statoLavorazioneInModifica);
    return this.http.put<any>(pathModificaLavorazione, {}, { params, observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  putConsolidaLavorazioneA4S(idLavorazione): Observable<any> {
    const pathModificaLavorazione = this.pathLavorazione + '/' + idLavorazione + '/consolidamentoA4S';
    return this.http.put<any>(pathModificaLavorazione, {}, { observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }


  putConsolidaLavorazioneAGS(idLavorazione): Observable<any> {
    const pathModificaLavorazione = this.pathLavorazione + '/' + idLavorazione + '/consolidamentoAGS';
    return this.http.put<any>(pathModificaLavorazione, {}, { observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }


  riprendiLavorazione(idLavorazione, verificaPascoli): Observable<any> {
    const pathRiprendiLavorazione = this.pathLavorazione + '/' + idLavorazione + '/riprendiLavorazione';
    return this.http.put<any>(pathRiprendiLavorazione, {}, { observe: 'response', params: { 'verificaPascoli': verificaPascoli.toString() } }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }


  ritagliaWorkspaceSuAreaDiLavoro(idLavorazione): Observable<any> {
    return this.http.post<any>(this.pathLavorazione + '/' + idLavorazione + '/ritagliasuADL', null, { observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  refreshStato(idLavorazione): Observable<any> {
    const params = { tipoJobFme: 'CONSOLIDAMENTO_AGS' }
    const URL = this.pathLavorazione + '/' + idLavorazione + '/refreshStatoJobFME';
    return this.http.get<any[]>(URL, { params }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  getClipdaAdlLavorazione(filterData): Observable<PoligoniSuoloDaAdl> {
    const URL = this.pathLavorazione + '/' + filterData.idLavorazione + '/clipDaADL';
    const params = new HttpParams().set('numeroElementiPagina', filterData.numeroElementiPagina).set('pagina', filterData.pagina);
    return this.http.get<PoligoniSuoloDaAdl[]>(URL, { params }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }

  deleteAdl(idLavorazione): Observable<any> {
    const url = this.pathLavorazione + '/' + idLavorazione + '/areaDiLavoro';
    return this.http.delete(url, { responseType: 'text', observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }
  ricaricaADL(idLavorazione: number) {
    return new Promise<PoligoniSuoloDaAdl>(((resolve, reject) => {
      this.mapService.refreshLayer(PropertyLayer.CODICE_LAYER_SUOLO_PRENOTATO_LAV_BO, idLavorazione);
      this.mapService.refreshLayer(PropertyLayer.CODICE_LAYER_CLIP_SU_ADL, idLavorazione);
      this.mapService.refreshLayer(PropertyLayer.CODICE_LAYER_ADL, idLavorazione);
      this.mapService.refreshWmsLayer([PropertyLayer.CODICE_LAYER_SUOLO_VIGENTE_INCLUSO_IN_LAVORAZIONI_BO]);
      this.ricaricaPoligoniSuoloDaADL(idLavorazione).then((results: PoligoniSuoloDaAdl) => {
        if (results) {
          resolve(results);
        } else {
          reject(false);
        }
      });
    }));
  }

  ricaricaPoligoniSuoloDaADL(idLavorazione: number) {
    const params = {
      'pagina': 0,
      'numeroElementiPagina': 500,
      'idLavorazione': idLavorazione
    };
    return new Promise<PoligoniSuoloDaAdl>(((resolve, reject) => {
      this.getClipdaAdlLavorazione(params)
        .subscribe(
          (results: PoligoniSuoloDaAdl) => {
            resolve(results);
          },
          (error) => {
            console.log(error);
          });
    }));
  }

  copiaLavorazione(idLavorazione, campagna): Observable<any> {
    return this.http.post<any[]>(this.pathLavorazione + '/copiaLavorazione', { idLavorazione, campagna }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError(error);
        })
      );
  }
}
