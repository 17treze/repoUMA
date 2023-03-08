import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Configuration } from 'src/app/app.constants';
import { Cached } from 'src/assets/decorators/cached';
import { AllevamentoDto } from '../../creazione-fascicolo/dto/AllevamentoDto';

@Injectable({
  providedIn: 'root'
})
export class ZootecniaService {

  constructor(
    private httpClient: HttpClient,
    private configuration: Configuration
  ) { }

  private urlZootecnia = `${this.configuration.zootecnia_server}/zootecnia`;

  public getUrlAllevamenti(cuaa: string) {
    return `${this.urlZootecnia}/${cuaa}/anagrafica-allevamenti`;
  }

  public putUrlUpdateAllevamenti(cuaa: string): string {
    return `${this.getUrlAllevamenti(cuaa)}/aggiorna`;
  }

  public getAllevamenti(cuaa: string): Observable<AllevamentoDto[]> {
    return this.httpClient.get<AllevamentoDto[]>(this.getUrlAllevamenti(cuaa)).pipe(
      tap((retVal: any) => {
        return AllevamentoDto.toDto(retVal);
      }));
  }

  public putUpdateAllevamenti(cuaa: string, dataRichiesta: string): Observable<void> {
    const formInput: FormData = new FormData();
    formInput.append('dataRichiesta', dataRichiesta);
    return this.httpClient.put<void>(this.putUrlUpdateAllevamenti(cuaa), formInput);
  }

  private getUrlControlloCompletezza(cuaa: string) {
    return `${this.urlZootecnia}/${cuaa}/controllo-completezza`;
  }

  // @Cached()
  public getControlloCompletezza(cuaa: string): Observable<Map<string, boolean>> {
    console.log("sending request to: " + this.getUrlControlloCompletezza(cuaa));
    return this.httpClient.get<Map<string, boolean>>(this.getUrlControlloCompletezza(cuaa));
  }
}
