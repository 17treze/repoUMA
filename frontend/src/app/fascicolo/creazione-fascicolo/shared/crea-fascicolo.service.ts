import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Configuration } from 'src/app/app.constants';
import { DatiAperturaFascicoloDto } from '../dto/DatiAperturaFascicoloDto';

@Injectable({
  providedIn: 'root'
})
export class CreazioneFascicoloService {

  constructor(private http: HttpClient, private _configuration: Configuration) { }

  // shared data service fra ricerca a creazione del fascicolo
  // se non valorizzata signigica che i controlli sulla ricerca non sono andati a buon fine
  // e che non si può procedere con la creazione
  public anagraficaTributaria: DatiAperturaFascicoloDto;

  public getTemplateMandato(): Observable<Blob> {
    return this.http.get(this._configuration.UrlGetTemplateMandato, { responseType: 'blob' });
  }

  public stampaMandato(file: File, dati: any): Observable<Blob> {
    const formData = new FormData();
    formData.append('file', file, file.name);
    formData.append('dati', JSON.stringify(dati));
    formData.append('formatoStampa', 'PDF_A');
    return this.http.post(this._configuration.UrlStampaPDF, formData, { responseType: 'blob' });
  }


}
