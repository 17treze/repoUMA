import { PoligoniDichiaratiEvent } from "./../shared/Poligoni-dichiarati/poligoni-dichiarati-event";
import { GisCostants } from "./../shared/gis.constants";
import { Messaggi } from "./../models/detailgis/messaggi.model";
import { SearchResults } from "./../models/searchgis/search-results.model";
import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { catchError } from "rxjs/operators";
import { Observable } from "rxjs/internal/Observable";
import { throwError } from "rxjs";
import { Allegati } from "../models/detailgis/allegati.model";
@Injectable()
export class RichiestaModificaSuoloService {
    private pathRichiesta: string;
    private getMessaggiUrl: string;
    paramsDichiarato: { pagina: number; numeroElementiPagina: number };

    handleError: (err: any, caught: Observable<SearchResults>) => never;
    getAllegatiListUrl: string;
    nuovoMessaggio: any;
    pathSuoloDichiarato: string;

    constructor(
        private http: HttpClient,
        private gisConstants: GisCostants,
        public poligoniDichiaratiEvent: PoligoniDichiaratiEvent
    ) {
        this.pathRichiesta = this.gisConstants.putRichiestaModificaSuolo;
        this.pathSuoloDichiarato = this.gisConstants.pathSuoloDichiarato;
    }

    ngOnChanges(): void {
        this.nuovoMessaggio = null;
    }
    putRichiestaModificaSuolo(params) {
        return this.http
            .put<SearchResults>(this.pathRichiesta + "/" + params.id, params, {
                observe: "response",
            })
            .pipe(
                (data: any) => {
                    return data;
                },
                catchError((error) => {
                    return throwError(error);
                })
            );
    }

    getMessaggi(val, idRichiesta, ordine) {
        // idRichiesta = 56996;
        this.getMessaggiUrl =
            this.pathRichiesta + "/" + idRichiesta + "/messaggi";
        let params = new HttpParams()
            .set("numeroElementiPagina", val.numeroElementiPagina)
            .set("pagina", val.pagina)
            .set("proprieta", "dataInserimento")
            .set("ordine", ordine);
        return this.http
            .get<Messaggi[]>(this.getMessaggiUrl, { params })
            .toPromise()
            .then((res) => <Messaggi[]>res)
            .then((data) => data);
    }

    getMessaggiDichiarati(idDichiarato) {
        this.getMessaggiUrl =
            this.pathSuoloDichiarato +
            "/" +
            idDichiarato +
            "/messaggiDichiarato";
        return this.http
            .get<Messaggi[]>(this.getMessaggiUrl, {})
            .toPromise()
            .then((res) => <Messaggi[]>res)
            .then((data) => data);
    }

    insertMessaggio(body, idRichiesta) {
        return this.http
            .post<any>(
                this.pathRichiesta + "/" + idRichiesta + "/messaggi",
                body,
                { observe: "response" }
            )
            .pipe(
                (data: any) => {
                    return data;
                },
                catchError((error) => {
                    return throwError("Error");
                })
            );
    }

    insertMessaggioDichiarati(body, idDichiarato) {
        return this.http
            .post<any>(
                this.pathSuoloDichiarato +
                    "/" +
                    idDichiarato +
                    "/messaggiDichiarato",
                body,
                { observe: "response" }
            )
            .pipe(
                (data: any) => {
                    return data;
                },
                catchError((error) => {
                    return throwError("Error");
                })
            );
    }

    getAllegati(idRichiesta, filter): Observable<Allegati> {
        let params = new HttpParams()
            .set("numeroElementiPagina", filter.numeroElementiPagina)
            .set("pagina", filter.pagina);
        this.getAllegatiListUrl =
            this.gisConstants.pathRichiestaModificaSuolo +
            "/" +
            idRichiesta +
            "/documenti/";
        return this.http.get<Allegati>(this.getAllegatiListUrl, { params });
    }

    getAllegatiDichiarato(idDichiarato, filter): Observable<Allegati> {
        let params = new HttpParams()
            .set("numeroElementiPagina", filter.numeroElementiPagina)
            .set("pagina", filter.pagina);
        this.getAllegatiListUrl =
            this.gisConstants.pathSuoloDichiarato +
            "/" +
            idDichiarato +
            "/documentiDichiarato/";
        return this.http.get<Allegati>(this.getAllegatiListUrl, { params });
    }

    getDichiarati(val, idRichiesta): Observable<any> {
        let params = new HttpParams()
            .set("numeroElementiPagina", val.numeroElementiPagina)
            .set("pagina", val.pagina);
        this.getMessaggiUrl =
            this.pathRichiesta + "/" + idRichiesta + "/dichiarati";
        return this.http.get<any>(this.getMessaggiUrl, { params });
    }

    loadDichiarati(idRichiesta) {
        this.poligoniDichiaratiEvent.poligoni = [];
        this.poligoniDichiaratiEvent.scrollCount = 10;
        this.poligoniDichiaratiEvent.pagina = 0;
        this.paramsDichiarato = {
            pagina: this.poligoniDichiaratiEvent.pagina,
            numeroElementiPagina: 10,
        };
        this.paramsDichiarato.numeroElementiPagina = 10;
        this.getDichiarati(this.paramsDichiarato, idRichiesta).subscribe(
            (results) => {
                results.risultati.forEach((element) => {
                    if (element.interventoInizio) {
                        element.interventoInizio = new Date(
                            element.interventoInizio
                        );
                    }
                    if (element.interventoFine) {
                        element.interventoFine = new Date(
                            element.interventoFine
                        );
                    }
                });
                this.poligoniDichiaratiEvent.poligoni = results["risultati"];
                this.poligoniDichiaratiEvent.count = results.count;
            },
            (error) => {
                console.log(error);
            }
        );
        this.resetScrollDichiarati();
    }

    loadDichiaratoID(idRichiesta, idDichiaratoSelezionato) {
        this.poligoniDichiaratiEvent.poligoni = [];
        this.poligoniDichiaratiEvent.scrollCount = 10;
        this.poligoniDichiaratiEvent.pagina = 0;
        this.paramsDichiarato = {
            pagina: this.poligoniDichiaratiEvent.pagina,
            numeroElementiPagina: 10,
        };
        this.paramsDichiarato.numeroElementiPagina = 10;
        this.getDichiarati(this.paramsDichiarato, idRichiesta).subscribe(
            (results) => {
                results.risultati.forEach((element) => {
                    if (element.interventoInizio) {
                        element.interventoInizio = new Date(
                            element.interventoInizio
                        );
                    }
                    if (element.interventoFine) {
                        element.interventoFine = new Date(
                            element.interventoFine
                        );
                    }
                });
                this.poligoniDichiaratiEvent.poligoni = results["risultati"];
                this.conservaDettaglioSelezionato(idDichiaratoSelezionato);
                this.poligoniDichiaratiEvent.count = results.count;
            },
            (error) => {
                console.log(error);
            }
        );
        this.resetScrollDichiarati();
    }

    private conservaDettaglioSelezionato(idSelezionato) {
        for (
            var i = this.poligoniDichiaratiEvent.poligoni.length - 1;
            i >= 0;
            i--
        ) {
            if (
                this.poligoniDichiaratiEvent.poligoni[i]["id"] !== idSelezionato
            ) {
                this.poligoniDichiaratiEvent.poligoni.splice(i, 1);
            }
        }
    }

    resetScrollDichiarati() {
        setTimeout(() => {
            let scrollBody = document.querySelector("#poligoniDichiaratiTable");
            if (scrollBody) {
                scrollBody.scrollTop = 0;
                this.paramsDichiarato.pagina = 0;
            }
        });
    }

    salvaDichiarati(params) {
        console.log(params);
        return this.http
            .put<any>(
                this.pathRichiesta +
                    "/" +
                    params[0].idRichiesta +
                    "/aggiornaDichiarati",
                params,
                { observe: "response" }
            )
            .pipe(
                (data: any) => {
                    return data;
                },
                catchError((error) => {
                    return throwError(error);
                })
            );
    }
}
