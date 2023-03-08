import { PoligoniDichiaratiEvent } from "../../shared/Poligoni-dichiarati/poligoni-dichiarati-event";
import { RichiestaModificaSuoloService } from "./../../services/richiesta-modifica-suolo.service";
import { MapService } from "./../mappa/map.service";
import { Component, Input, OnInit, ViewEncapsulation } from "@angular/core";
import { EsitoLavorazioneDichiaratoDecode } from "../../shared/EsitoLavorazioneDichiarato.enum";
import { ProfiloUtente } from "../../shared/profilo-utente";
import { StatoLavorazioneSuoloDecode } from "../../shared/StatoLavorazioneSuolo.enum";

@Component({
    selector: "gis-poligoni-dichiarati-table",
    templateUrl: "./poligoni-dichiarati-table.component.html",
    styleUrls: ["./poligoni-dichiarati-table.component.css"],
    encapsulation: ViewEncapsulation.None,
})
export class PoligoniDichiaratiTableComponent implements OnInit {
    @Input() detailResults: any;
    poligoniDichiarati = [];
    cols: { field: string; header: string }[];
    private numeroElementiPagina = 10;
    totals: number;
    public itemsToShow: any;
    public isFullListDisplayed = false;
    private itemsToLoad = 10;
    scrollParams: any;
    listaEsitoLavorazione = EsitoLavorazioneDichiaratoDecode.lista;
    listaStatoLavorazione = StatoLavorazioneSuoloDecode.lista;
    idRichiesta: any;

    constructor(
        private mapService: MapService,
        private richiestaModificaSuoloService: RichiestaModificaSuoloService,
        public poligoniDichiaratiEvent: PoligoniDichiaratiEvent,
        public profiloUtente: ProfiloUtente
    ) {
        this.totals = this.poligoniDichiaratiEvent.count;
        this.profiloUtente.profilo = localStorage.getItem("selectedRole");
    }

    ngOnInit() {
        this.idRichiesta = this.detailResults.id;
    }

    loadPoligoniDichiarati() {
        // ricarico la lista
        this.richiestaModificaSuoloService.loadDichiarati(
            this.poligoniDichiaratiEvent.poligoni[0]["idRichiesta"]
        );
    }

    centerMap(extent) {
        this.mapService.centerMap(extent);
    }

    onScroll() {
        if (
            this.poligoniDichiaratiEvent.scrollCount <=
            this.poligoniDichiaratiEvent.count
        ) {
            this.poligoniDichiaratiEvent.scrollCount += this.itemsToLoad;
            this.itemsToShow = this.poligoniDichiaratiEvent.poligoni.slice(
                0,
                this.poligoniDichiaratiEvent.scrollCount
            );
            console.log("scrolled down");
            this.poligoniDichiaratiEvent.pagina += 1;
            this.searchOnScroll();
        } else {
            this.isFullListDisplayed = true;
        }
    }

    searchOnScroll() {
        this.scrollParams = {
            pagina: this.poligoniDichiaratiEvent.pagina,
            numeroElementiPagina: this.numeroElementiPagina,
        };
        this.richiestaModificaSuoloService
            .getDichiarati(this.scrollParams, this.idRichiesta)
            .subscribe(
                (results) => {
                    this.poligoniDichiaratiEvent.poligoni =
                        this.poligoniDichiaratiEvent.poligoni.concat(
                            results["risultati"]
                        );
                },
                (error) => {
                    console.log(error);
                }
            );
    }

    openDialogDichiarati(detailResults) {
        this.poligoniDichiaratiEvent["poligoni"].forEach((element) => {
            if (element["interventoInizio"]) {
                element["interventoInizio"] = new Date(
                    element["interventoInizio"]
                );
            }
            if (element["interventoFine"]) {
                element["interventoFine"] = new Date(element["interventoFine"]);
            }
        });
        this.poligoniDichiaratiEvent.showDialog = true;
        this.poligoniDichiaratiEvent.idDettaglioRichiesta = detailResults.id;
        this.poligoniDichiaratiEvent.statoRichiesta = detailResults.stato;
    }
}
