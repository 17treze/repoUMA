import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { DetenzioneDto, DetenzioneInProprioDto, MandatoDto, TipoDetenzioneEnum } from 'src/app/modules/fascicolo-dettaglio/models/FascicoloCuaa';
import { environment } from 'src/environments/environment';

@Injectable()
export class MandatoService {

  constructor(
    private httpClient: HttpClient,
  ) { }

  private anagrafica_server_mandato = `${environment.backendUrl}/anagrafica/api/v1/mandato`;
  private anagrafica_server_detenzione = `${environment.backendUrl}/anagrafica/api/v1/detenzione`;

  public getUrlGetMandatoByCuaa(cuaa: string) {
    return `${this.anagrafica_server_mandato}/${cuaa}`;
  }

  public getUrlGetDetenzioneByCuaa(cuaa: string) {
    return `${this.anagrafica_server_detenzione}/${cuaa}`;
  }

  public getMandatiByCuaa(cuaa: string): Observable<MandatoDto[]> {
      console.log('getMandatiByFascicolo ' + cuaa);
      return this.httpClient.get<MandatoDto[]>(this.getUrlGetMandatoByCuaa(cuaa))
        .pipe(map(mandatiList => {
          const mandatiDtoList: MandatoDto[] = [];
          for (const mandato of mandatiList) {
            const mandatoDto: MandatoDto = MandatoDto.toDto(mandato);
            mandatiDtoList.push(mandatoDto);
          }
          return mandatiDtoList;
        }));
  }

  public getDetenzione(cuaa: string): Observable<DetenzioneDto> {
    console.log('getDetenzione ' + cuaa);
    return this.httpClient.get<DetenzioneDto>(this.getUrlGetDetenzioneByCuaa(cuaa))
      .pipe(map(detenzione => {
        let detenzioneDto: DetenzioneDto = undefined;
        if (detenzione.tipoDetenzione === TipoDetenzioneEnum.MANDATO) {
          detenzioneDto = MandatoDto.toDto(detenzione);
        } else if (detenzione.tipoDetenzione === TipoDetenzioneEnum.DETENZIONE_IN_PROPRIO) {
          detenzioneDto = DetenzioneInProprioDto.toDto(detenzione);
        }
        return detenzioneDto;
      }));
  }
}
