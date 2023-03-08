import { DomandaIntegrativa } from './classi/DomandaIntegrativa';
import { Injectable } from '@angular/core';
import { Configuration } from 'src/app/app.constants';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StatisticaZootecniaDto } from './classi/StatisticaZootecniaDto';
import { A4gMessages } from 'src/app/a4g-common/a4g-messages';

@Injectable({
  providedIn: 'root'
})
export class DomandaIntegrativaService {

  public domandaIntegrativa: DomandaIntegrativa;
  public dtChiusuraDI: Date;
  public idDomandaUnica: string;


  constructor(private configuration: Configuration, private http: HttpClient) { }

  getDomandaIntegrativa() {
    return this.domandaIntegrativa;
  }

  setDomandaIntegrativa(domandaIntegrativa: DomandaIntegrativa) {
    this.domandaIntegrativa = domandaIntegrativa;
  }

  getDataChiusura(){
    return this.dtChiusuraDI;
  }

  setDataChiusura(dtChiusuraDI: Date) {
    this.dtChiusuraDI = dtChiusuraDI;
  }

  getIdDomandaUnica(){
    return this.idDomandaUnica;
  }

  setIdDomandaUnica(idDomandaUnica : string){
    this.idDomandaUnica = idDomandaUnica;
  }

  // get statistiche zootecnia - raggruppate per intervento solo nello stato presentata
  public getStatisticheZootecnia(annoCampagna: number): Observable<Array<StatisticaZootecniaDto>> {
    return this.http.get<Array<StatisticaZootecniaDto>>(this.configuration.getUrlStatisticheZootecnia(annoCampagna));
  }

  // download file riassuntivo dati capi agea
  public getFileDatiCapiAgea(idDatiSettore: number): Observable<any> {
    const params = encodeURIComponent(JSON.stringify({ idDatiSettore: idDatiSettore }));

    return this.http.get(this.configuration.urlGetFileDatiCapiAgea + params, { responseType: 'blob' });
  }

  public readByteFile(file: File) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      const error = "Errore nell' estrapolare i byte dal file " + file.name;
      let value;
      if (A4gMessages.isUndefinedOrNull(file)) {
        reject(error);
      } else {
        reader.readAsDataURL(file);
        reader.onloadend = event => {
          value = reader.result;
          value == null
            ? reject(error)
            : resolve(value.toString().split(",")[1]);
        };
      }
    });
  }

}
