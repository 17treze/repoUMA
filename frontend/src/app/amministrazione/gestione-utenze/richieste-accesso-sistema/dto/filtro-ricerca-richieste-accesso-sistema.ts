import {HttpParams} from "@angular/common/http";
import {StringSupport} from "../../../../a4g-common/utility/string-support";
import {Paginazione} from "../../../../a4g-common/utility/paginazione";

export class FiltroRicercaRichiesteAccessoSistema {

    filtroGenerico: string;
    stato: string;
    codiceFiscale;
    nome: string;
    cognome: string;
    idProtocollazione: string;
    dataInizio: string;
    dataFine: string;

    public static of(filtroGenerico: string,
                     stato: string,
                     codiceFiscale,
                     nome: string,
                     cognome: string,
                     idProtocollazione: string,
                     dataInizio: string,
                     dataFine: string): FiltroRicercaRichiesteAccessoSistema {
        let filtro: FiltroRicercaRichiesteAccessoSistema = new FiltroRicercaRichiesteAccessoSistema();
        filtro.filtroGenerico = filtroGenerico;
        filtro.stato = stato;
        filtro.codiceFiscale = codiceFiscale;
        filtro.nome = nome;
        filtro.cognome = cognome;
        filtro.idProtocollazione = idProtocollazione;
        filtro.dataInizio = dataInizio;
        filtro.dataFine = dataFine;
        return filtro;
    }

    public getHttpParams(paginazione: Paginazione): HttpParams {
        let httpParams = new HttpParams();
        if (StringSupport.isNotEmpty(this.stato))
            httpParams = httpParams.append('stato', this.stato);
        if (StringSupport.isNotEmpty(this.filtroGenerico))
            httpParams = httpParams.append('filtroGenerico', this.filtroGenerico);
        if (StringSupport.isNotEmpty(this.codiceFiscale))
            httpParams = httpParams.append('codiceFiscaleUpperLike', this.codiceFiscale);
        if (StringSupport.isNotEmpty(this.cognome))
            httpParams = httpParams.append('cognome', this.cognome);
        if (StringSupport.isNotEmpty(this.nome))
            httpParams = httpParams.append('nome', this.nome);
        if (StringSupport.isNotEmpty(this.idProtocollazione))
            httpParams = httpParams.append('idProtocollo', this.idProtocollazione);
        if (StringSupport.isNotEmpty(this.dataInizio))
            httpParams = httpParams.append('dataInizio', this.dataInizio);
        if (StringSupport.isNotEmpty(this.dataFine))
            httpParams = httpParams.append('dataFine', this.dataFine);
        httpParams = Paginazione.fillHttpParamsWith(httpParams, paginazione);
        return httpParams;
    }
}
