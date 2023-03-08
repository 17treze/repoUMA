import { Injectable } from '@angular/core';
import { GisCostants } from './../shared/gis.constants';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable()
export class LavorazioneWorkspaceService {
  pathLavorazione: string;
  constructor(private http: HttpClient, private gisConstants: GisCostants) {
    this.pathLavorazione = this.gisConstants.pathLavorazioneSuolo;
  }


  getCodificheSuolo(): Observable<any> {
    let URL = this.pathLavorazione + '/codificheSuolo' ;
    return this.http.get<any[]>(URL, {});
  }


}
