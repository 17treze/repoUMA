import { GisCostants } from "./../shared/gis.constants";
import {
    ComnuniCatastali,
    RicercaLocalita,
    Risultati,
} from "../models/searchgis/comuni-catastali.model";
import {
    SearchResults,
    Lavorazione,
} from "./../models/searchgis/search-results.model";
import { Detail } from "./../models/searchgis/detail";
import { Injectable, Input, Directive } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { catchError } from "rxjs/operators";
import { ShowResults } from "./../shared/show-results";
import { Observable } from "rxjs/internal/Observable";
import { AnyObject } from "chart.js/types/basic";
import { Coordinate, format } from "ol/coordinate";
import { transform } from "ol/proj";
import { Location, Search } from "../models/searchgis/location.model";
import { UtenteAgs } from "../../a4g-common/classi/utenteAgs";
import { UtenteA4g } from "../../a4g-common/classi/utenteA4g";
import { Configuration } from "../../app.constants";
import { MapService } from "../components/mappa/map.service";

@Directive()
@Injectable({
    providedIn: "root",
})
export class SearchGisService {
    dismissible: false;
    blockScroll: true;
    isActive: boolean;
    showResults: boolean;
    dataDetails: any;
    numeroElementi = 5;
    private searchUrl: string;
    private searchComuniurl: string;
    private searchUtentiBoViticoloUrl: string;
    private searchLocalitaurl: string;
    private searchPoligoniSuoloDichiarato: string;
    private defaultProjection: string;
    searchLavorazioniUrl: string;

    // private searchTotalsUrl: string = 'http://localhost:3000/totals';

    constructor(
        showResults: ShowResults,
        private http: HttpClient,
        private gisConstants: GisCostants,
        private configuration: Configuration,
        private mapService: MapService
    ) {
        this.isActive = showResults.isActive;
        this.searchUrl = this.gisConstants.searchRichiestaModificaSuolo;
        this.searchLavorazioniUrl = this.gisConstants.pathLavorazioneSuolo;
        this.searchComuniurl = this.gisConstants.getComuniCatastaliUrl;
        this.searchUtentiBoViticoloUrl =
            this.configuration.a4gutente_server + "utenti/boviticolo";
        this.searchPoligoniSuoloDichiarato =
            this.gisConstants.pathSuoloDichiarato;
        this.searchLocalitaurl = this.gisConstants.searchLocalitaurl;
        this.defaultProjection = this.gisConstants.defaultProjection;
    }

    resultsPost(filterData): Observable<SearchResults[]> {
        const obj = filterData.richiestaModificaSuolo;

        let params = new HttpParams()
            .set("numeroElementiPagina", filterData.numeroElementiPagina)
            .set("pagina", filterData.pagina);

        if (obj.idRichiesta && obj.idRichiesta !== "") {
            params = params.append("idRichiesta", obj.idRichiesta);
        }
        if (obj.cuaa && obj.cuaa !== "") {
            params = params.append("cuaa", obj.cuaa);
        }
        if (obj.annoCampagna && obj.annoCampagna !== "") {
            params = params.append("campagna", obj.annoCampagna);
        }
        if (obj.statoRichiesta && obj.statoRichiesta !== "") {
            params = params.append("stato", obj.statoRichiesta);
        }
        if (obj.tipoRichiesta && obj.tipoRichiesta !== "") {
            params = params.append("tipo", obj.tipoRichiesta);
        }
        if (obj.dataInserimento && obj.dataInserimento !== "") {
            params = params.append("data", obj.dataInserimento);
        }
        if (obj.comune && obj.comune !== "" && obj.comune.codice) {
            params = params.append("comuneCatastale", obj.comune.codice);
        }
        return this.http.get<SearchResults[]>(this.searchUrl, { params });
    }

    ricercaLavorazioni(params): Observable<Lavorazione> {
        return this.http.get<any>(this.searchLavorazioniUrl + "/", { params });
    }

    resultsPoligoniSuoloDichiarato(
        filterData,
        isIdLavorazione
    ): Observable<SearchResults[]> {
        if (!isIdLavorazione) {
            const obj = filterData.poligoniSuoloDichiatato;

            let params = new HttpParams()
                .set("numeroElementiPagina", filterData.numeroElementiPagina)
                .set("pagina", filterData.pagina);

            if (obj.idRichiesta && obj.idRichiesta !== "") {
                params = params.append("idRichiesta", obj.idRichiesta);
            }
            if (obj.cuaa && obj.cuaa !== "") {
                params = params.append("cuaa", obj.cuaa);
            }
            // Anno di campagna recuperato da selettore in mappa
            params = params.append(
                "campagna",
                this.mapService.annoCampagna.toString()
            );
            if (
                obj.visibileOrtofoto !== undefined &&
                obj.visibileOrtofoto !== ""
            ) {
                params = params.append(
                    "visibileOrtofoto",
                    obj.visibileOrtofoto
                );
            }
            if (obj.tagRilevato && obj.tagRilevato !== "") {
                params = params.append("tipoSuoloRilevato", obj.tagRilevato);
            }
            if (obj.tagDichiarato && obj.tagDichiarato !== "") {
                params = params.append(
                    "tipoSuoloDichiarato",
                    obj.tagDichiarato
                );
            }
            if (obj.comune && obj.comune !== "" && obj.comune.codice) {
                params = params.append("comuneCatastale", obj.comune.codice);
            }
            if (obj.statoRichiesta && obj.statoRichiesta !== "") {
                params = params.append("statoRichiesta", obj.statoRichiesta);
            }

            return this.http.get<SearchResults[]>(
                this.searchPoligoniSuoloDichiarato,
                { params }
            );
        } else {
            const params = filterData;
            return this.http.get<SearchResults[]>(
                this.searchPoligoniSuoloDichiarato,
                { params }
            );
        }
    }

    getDettaglio(idRichiesta): Observable<Detail> {
        const url = this.searchUrl + idRichiesta;

        return this.http.get<Detail>(url, {});
    }

    getComuni() {
        return this.http
            .get<Risultati[]>(this.searchComuniurl)
            .toPromise()
            .then((res) => <Risultati[]>res)
            .then((data) => data);
    }

    getUtentiBoViticolo() {
        return this.http
            .get<Array<UtenteA4g>>(this.searchUtentiBoViticoloUrl)
            .toPromise()
            .then((res) => <Array<UtenteA4g>>res)
            .then((data) => data);
    }

    getRicercaInMappa(searchText: string): Promise<Search> {
        const regex = /[0-9]/;

        let type;

        if (regex.test(searchText)) {
            type = "cadastre";
        } else {
            type = "toponym";
        }

        const urlLocalita =
            this.searchLocalitaurl +
            "?searchText=" +
            searchText +
            "&lang=it&type=" +
            type;
        return this.http.get<Search>(urlLocalita).toPromise();
    }

    getRicercaPerCoordinate(query: string): Location[] {
        const coordinateRegex = /(\d+(\.\d*)?)\s*,\s*(\d+(\.\d*)?)/;

        const match = coordinateRegex.exec(query);
        if (!match) return;

        const x = parseFloat(match[1]);
        const y = parseFloat(match[3]);
        const coordinate = [x, y];

        const projections = [
            {
                name: "EPSG:25832 - ETRS89 UTM 32N",
                epsg: "EPSG:25832",
                precision: 4,
            },
            {
                name: "EPSG:3003 - ROMA40 GB Ovest TN-AA",
                epsg: "EPSG:3003",
                precision: 4,
            },
            {
                name: "EPSG:4326 - WGS84",
                epsg: "EPSG:4326",
                precision: 7,
            },
        ];
        const locations: Location[] = [];

        projections.forEach((projection) => {
            const label: string = format(
                coordinate,
                "{x}, {y}",
                projection.precision
            );
            const transformed: [number, number] = transform(
                coordinate,
                projection.epsg,
                this.defaultProjection
            );

            locations.push({
                label: label + " (" + projection.name + ")",
                x: transformed[0],
                y: transformed[1],
            });
        });

        return locations;
    }
}
