import { Component, OnInit } from "@angular/core";
import { Labels } from "src/app/app.labels";
import { DatePipe } from "@angular/common";
import { A4gMessages, A4gSeverityMessage } from "src/app/a4g-common/a4g-messages";
import { MessageService } from "primeng/api";
import { Router, NavigationExtras, ActivatedRoute } from "@angular/router";
import { DichiarazioneAntimafia } from "src/app/fascicolo/antimafia/classi/dichiarazioneAntimafia";
import { FascicoloCorrente } from "src/app/fascicolo/fascicoloCorrente";
import { AntimafiaService } from "src/app/fascicolo/antimafia/antimafia.service";
import { DichiarazioneAntimafiaFilter } from "src/app/fascicolo/antimafia/classi/dichiarazioneAntimafiaFilter";
import { HomeService } from "src/app/home/home.service";
import { InputFascicolo } from "src/app/a4g-common/classi/InputFascicolo";
import { Fascicolo } from "src/app/a4g-common/classi/Fascicolo";
import * as FileSaver from "file-saver";
import { FascicoloService } from "src/app/fascicolo/fascicolo.service";

@Component({
    selector: "app-dettaglio-domande-scadenza",
    templateUrl: "./dettaglio-domande-scadenza.component.html",
    styleUrls: ["./dettaglio-domande-scadenza.component.css"],
    providers: [DatePipe]
})
export class DettaglioDomandeScadenzaComponent implements OnInit {
    public labels = Labels;
    cols: any[];
    private dichiarazioniAntimafia: Array<DichiarazioneAntimafia> = [];
    dichiarazioniAntimafiaTable: Array<DichiarazioneAntimafia> = [];
    dichiarazioneSelezionata: DichiarazioneAntimafia;
    indexSelectedFromMenu: number;
    cuaaImpresa: string;

    constructor(
        private antimafiaService: AntimafiaService,
        private fascicoloCorrente: FascicoloCorrente,
        private messageService: MessageService,
        private router: Router,
        private route: ActivatedRoute,
        private homeService: HomeService,
        private fascicoloService: FascicoloService) {
            this.cols = [
                { field: 'datiDichiarazione.dettaglioImpresa.codiceFiscale', header: this.labels.cuaaSigla },
                { field: 'datiDichiarazione.dettaglioImpresa.denominazione', header: this.labels.descrizioneImpresa },
                { field: 'stato.identificativo', header: this.labels.stato }
            ];
    }

    ngOnInit() {
        const dichiarazioneAntimafiaParams = new DichiarazioneAntimafiaFilter();
        dichiarazioneAntimafiaParams.filtroUtenteEnte = true;
        dichiarazioneAntimafiaParams.statiDichiarazione = [
            "VERIFICA_PERIODICA",
            "RIFIUTATA"
        ];
        this.antimafiaService
            .getDichiarazioneAntimafiaInScadenza(dichiarazioneAntimafiaParams)
            .subscribe(
                (next: Array<DichiarazioneAntimafia>) => {
                    this.dichiarazioniAntimafia = next ? next : [];
                    this.dichiarazioniAntimafiaTable = this.dichiarazioniAntimafia;
                }, err => console.log("error: " + err)
            );
    }

    public domandeCollegate() {
        var params = {
            cuaa: this.fascicoloCorrente.fascicolo.cuaa
        };
        let navigationExtras: NavigationExtras = {
            queryParams: {
                params: JSON.stringify(params)
            },
            relativeTo: this.route.parent
        };
        this.router.navigate(["./dettaglioDomandeCollegate"], navigationExtras);
    }

    creaDomanda(domanda: DichiarazioneAntimafia): void {
        console.log("Nuova dichiatazione da domanda in scadenza azienda " + domanda.azienda.cuaa);
        let inputFascicolo: InputFascicolo = new InputFascicolo();
        inputFascicolo.cuaa = domanda.azienda.cuaa;
        inputFascicolo.denominazione = domanda.datiDichiarazione.dettaglioImpresa.denominazione;
        this.cuaaImpresa = domanda.azienda.cuaa;
        this.fascicoloService.ricercaFascicoli(inputFascicolo).subscribe(
            (fascicoli: Array<Fascicolo>) => {
                this.router.navigate(['../fascicolo/' + fascicoli[0].idFascicolo], { relativeTo: this.route });
            },
            error => this.handleError(error)
        );
    }

    public scarica() {
        if (this.dichiarazioniAntimafiaTable.length === 0) {
            return;
        }
        const dichiarazioneAntimafiaParams = new DichiarazioneAntimafiaFilter();
        dichiarazioneAntimafiaParams.filtroUtenteEnte = true;
        dichiarazioneAntimafiaParams.statiDichiarazione = [
            "VERIFICA_PERIODICA",
            "RIFIUTATA"
        ];
        this.antimafiaService.downloadDichiarazioneAntimafiaCsv(dichiarazioneAntimafiaParams).subscribe(csv => {
            const fileName = "dichiarazione_antimafia.csv";
            let csvBlob: Blob = new Blob([csv], { type: "text/plain;charset=utf-8" });
            FileSaver.saveAs(csvBlob, fileName);
        }, err => {
            this.messageService.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, err.message));
        });
    }

    private handleError(error: any) {
        const errMsg = error.error.message ? error.error.message : error.error;
        this.messageService.add(A4gMessages.getToast('errorCreazioneDichiarazione', A4gSeverityMessage.error, errMsg));
    }
}