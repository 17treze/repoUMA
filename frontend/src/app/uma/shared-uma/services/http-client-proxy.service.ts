import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Configuration } from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class HttpClientProxyService {

  constructor(
    private http: HttpClient,
    private configuration: Configuration
  ) { }

  public getTemplate(pathToFile: string = 'assets/templates/templateRichiestaCarburante.docx'): Observable<Blob> {
    return this.http.get(this.configuration.getUrlTemplateStampa(pathToFile), { responseType: 'blob' });
  }

  public stampaFile(file: File, dati: any): Observable<Blob> {
    const formData = new FormData();
    formData.append('file', file, file.name);
    formData.append('dati', JSON.stringify(dati));
    formData.append('formatoStampa', 'PDF');
    return this.http.post(this.configuration.UrlStampaPDF, formData, { responseType: 'blob' });
  }

  public verificaFirmaSingola(file: File, codiceFiscale: string): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('documentoFirmato', file);
    return this.http.post<any>(this.configuration.UrlVerificaFirmaSingola.replace('${codiceFiscaleTitolareRl}', codiceFiscale), formData);
  }

}
