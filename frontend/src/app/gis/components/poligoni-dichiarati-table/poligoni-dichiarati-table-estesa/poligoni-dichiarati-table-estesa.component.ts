import { getLength } from "ol/sphere";
import { ToastGisComponent } from "./../../toast-gis/toast-gis.component";
import { RichiestaModificaSuoloService } from "./../../../services/richiesta-modifica-suolo.service";
import { StatiRichiesta } from "./../../../shared/StatiRichiesta.enum";
import { PoligoniDichiaratiTableComponent } from "./../poligoni-dichiarati-table.component";
import { PoligoniDichiaratiEvent } from "./../../../shared/Poligoni-dichiarati/poligoni-dichiarati-event";
import { Component, Input, OnInit, ViewEncapsulation } from "@angular/core";
import { ProfiloUtente } from "src/app/gis/shared/profilo-utente";
import { StatoLavorazioneSuoloDecode } from "../../../shared/StatoLavorazioneSuolo.enum";
import { TipoInterventoColturale } from "../../../models/detailgis/tipoInterventoColturale.model";
import { AllegatiDichiaratoGisService } from "src/app/gis/services/allegati-dichiarato-gis.service";
import { AllegatiGisService } from "src/app/gis/services/allegati-gis.service";

@Component({
    selector: "gis-poligoni-dichiarati-table-estesa",
    templateUrl: "./poligoni-dichiarati-table-estesa.component.html",
    styleUrls: ["./poligoni-dichiarati-table-estesa.component.css"],
    providers: [
        { provide: AllegatiGisService, useClass: AllegatiDichiaratoGisService },
    ],
    encapsulation: ViewEncapsulation.None,
})
export class PoligoniDichiaratiTableEstesaComponent implements OnInit {
    @Input() detailResults: any;

    initFile: boolean = true;
    poligoniDichiarati = [];
    cols: { field: string; header: string }[];
    totals: number;
    public itemsToShow: any;
    public isFullListDisplayed = false;
    public tipoAllegato: string = "poligoni";
    scrollParams: any;
    listaStatoLavorazione = StatoLavorazioneSuoloDecode.lista;
    NodeTypeEnum = TipoInterventoColturale;
    statiRichiesta = StatiRichiesta;
    rowIsSelected: any;
    context = "dettaglio";
    vincoloAttributiSubmit: boolean;
    maxDate: Date;
    annoCampagnaPoligono: any;
    dateNonValide: boolean;
    constructor(
        public poligoniDichiaratiEvent: PoligoniDichiaratiEvent,
        private richiestaModificaSuoloService: RichiestaModificaSuoloService,
        private poligonoDichiaratiTable: PoligoniDichiaratiTableComponent,
        private toastComponent: ToastGisComponent,
        public profiloUtente: ProfiloUtente
    ) {
        this.profiloUtente.profilo = localStorage.getItem("selectedRole");
        this.poligoniDichiaratiEvent.poligoniModificati = [];
    }

    ngOnInit() {
        if (this.detailResults) {
            this.poligonoDichiaratiTable.idRichiesta = this.detailResults.id;
        }
        this.annoCampagnaPoligono = this.detailResults.campagna;
        this.checkVincoliAttributi();
    }

    loadPoligoniDichiarati() {
        this.poligonoDichiaratiTable.loadPoligoniDichiarati();
    }

    centerMap(extent) {
        this.poligonoDichiaratiTable.centerMap(extent);
    }

    onScroll() {
        this.poligonoDichiaratiTable.onScroll();
    }

    searchOnScroll() {
        this.poligonoDichiaratiTable.searchOnScroll();
    }

    onRowSelect(event) {
        this.rowIsSelected = event.data;
    }

    getPoligonoModificato(poligono) {
        const poligonoModificato =
            this.poligoniDichiaratiEvent.poligoniModificati.filter(
                (x) => x.id === poligono.id
            );
        if (poligonoModificato && poligonoModificato[0]) {
            let index =
                this.poligoniDichiaratiEvent.poligoniModificati.findIndex(
                    (x) => x.id === poligonoModificato[0].id
                );
            this.poligoniDichiaratiEvent.poligoniModificati[index] =
                poligonoModificato[0];
        } else {
            this.poligoniDichiaratiEvent.poligoniModificati.push(poligono);
        }
        this.checkVincoliAttributi();
    }

    checkVincoliAttributi() {
        let countPoligoniInvalidi = 0;
        setTimeout(() => {
            if (!this.maxDate) {
                this.maxDate = new Date(
                    "December 31," + this.annoCampagnaPoligono
                );
            }
            let poligoni = this.poligoniDichiaratiEvent.poligoni;
            for (let i = 0; i < poligoni.length; i++) {
                if (!poligoni[i].visibileInOrtofoto) {
                    if (
                        (!poligoni[i].tipoInterventoColturale ||
                            !poligoni[i].interventoInizio ||
                            !poligoni[i].interventoFine) &&
                        poligoni[i].idLavorazione == null &&
                        poligoni[i].idLavorazione == undefined
                    ) {
                        countPoligoniInvalidi = countPoligoniInvalidi + 1;
                        poligoni[i].vincoloAttributi = true;
                    } else {
                        poligoni[i].vincoloAttributi = false;
                    }
                } else {
                    poligoni[i].vincoloAttributi = false;
                }
            }
            this.vincoloAttributiSubmit = countPoligoniInvalidi > 0;
        }, 100);
    }

    salvaDichiarati(poligoni) {
        for (let i = 0; i < poligoni.length; i++) {
            // date format YYYY-MM-DD dataInizio
            if (poligoni[i].interventoInizio) {
                let dataInizio = new Date(poligoni[i].interventoInizio);
                let diff = dataInizio.getTimezoneOffset() / 60;
                dataInizio.setHours(0 - diff);
                poligoni[i].interventoInizio = dataInizio;
            }
            // date format YYYY-MM-DD dataFine
            if (poligoni[i].interventoFine) {
                let dataFine = new Date(poligoni[i].interventoFine);
                let diff = dataFine.getTimezoneOffset() / 60;
                dataFine.setHours(0 - diff);
                poligoni[i].interventoFine = dataFine;
            }
            this.checkCoerenzaDate(poligoni[i]);
            if (this.dateNonValide) {
                break;
            }
        }
        if (
            (this.poligoniDichiaratiEvent.statoRichiesta ===
                StatiRichiesta.APERTA ||
                !this.vincoloAttributiSubmit) &&
            !this.dateNonValide
        ) {
            this.richiestaModificaSuoloService
                .salvaDichiarati(poligoni)
                .subscribe(
                    (response: any) => {
                        console.log(response);
                        this.poligoniDichiaratiEvent.poligoniModificati = [];
                        this.toastComponent.showSuccessAggiornaDichiarati();
                    },
                    (err) => {
                        this.toastComponent.showErrorAggiornaDichiarati(
                            err.error
                        );
                    }
                );
        }
    }

    checkCoerenzaDate(poligono) {
        if (poligono.interventoInizio > poligono.interventoFine) {
            this.toastComponent.showWarningGenerico(
                "La data di fine intervento non può essere antecedente alla data di inizio intervento"
            );
            this.dateNonValide = true;
        } else {
            this.dateNonValide = false;
        }
    }
}
