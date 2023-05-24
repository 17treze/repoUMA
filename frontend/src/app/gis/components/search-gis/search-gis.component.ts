import { LavorazioniEvent } from "src/app/gis/shared/LavorazioniEvent";
import { ProfiloUtente } from "./../../shared/profilo-utente";
import { PanelEvent } from "./../../shared/PanelEvent";
import { ToastGisComponent } from "./../toast-gis/toast-gis.component";
import {
    Component,
    Input,
    OnInit,
    ViewEncapsulation,
    SimpleChanges,
    OnChanges,
} from "@angular/core";
import { Validators, FormGroup, FormBuilder, NgForm } from "@angular/forms";
import {
    SearchResults,
    Lavorazione,
} from "./../../models/searchgis/search-results.model";
import { SidebarComponent } from "../sidebar/sidebar.component";
import { SearchGisService } from "./../../services/search-gis.service";
import { ShowResults } from "./../../shared/show-results";
import {
    StatiRichiesta,
    StatiRichiestaoDecode,
} from "../../shared/StatiRichiesta.enum";
import * as moment from "moment";
import { TagRilevatoDecode } from "../../shared/TagRilevato.enum";
import { TagDichiaratoDecode } from "../../shared/TagDichiarato.enum";
import { StatoLavorazioneSuoloDecode } from "../../shared/StatoLavorazioneSuolo.enum";
import { SelectItem } from "primeng/api";
import { UtenteA4g } from "../../../a4g-common/classi/utenteA4g";
import { AuthService } from "../../../auth/auth.service";
import { GisUtilsService } from "../../shared/gis-utils.service";
@Component({
    // tslint:disable-next-line:component-selector
    selector: "gis-search",
    templateUrl: "./search-gis.component.html",
    styleUrls: ["./search-gis.component.css"],
    encapsulation: ViewEncapsulation.None,
})
export class SearchGisComponent implements OnInit, OnChanges {
    cuaa: string;
    searchResults: SearchResults[];
    isActive: boolean;
    sidebarComponent: any;
    searchForm: FormGroup;
    submitted = false;
    dataInserimento: any;
    selectTipoRicerca = {
        value: ''
    };
    @Input() profiloUtente: any;
    @Input() showResults: boolean;
    @Input() idLavorazione: any;
    //@Input() lavorazioneReadOnly: boolean;

    TipoRichiesta: any = [{ 'nome': 'Istanza di riesame', 'value': 'ISTANZA_DI_RIESAME' }];

    selectTipoRichiesta = this.TipoRichiesta[0].value;
    annoCampagna: any;
    listaComuni = JSON.parse(sessionStorage.getItem('listaComuni_TN'));
    listaA4gUtentiBoViticoloSession: UtenteA4g[] = JSON.parse(sessionStorage.getItem('listaA4gUtentiBoViticoloSession'));
    NodeTypeEnum = StatiRichiesta;
    selectStatoRichiesta: any;
    statoRichiesta = StatiRichiestaoDecode.lista;
    statiLavorazione = StatoLavorazioneSuoloDecode.lista;
    utentiLavorazione: any[] = new Array();
    utenteConnesso: any[] = new Array();

    selectedComune: any;
    filteredComuni: any[];
    lavorazioneForm: FormGroup;
    searchFormLavorazione: FormGroup;

    richieste: SearchResults[];
    lavorazioni: Lavorazione;
    poligoniDichiarati: any;

    tagRilevato = TagRilevatoDecode.lista;
    tagDichiarato = TagDichiaratoDecode.lista;
    anno_campagna: SelectItem[];
    visibile_ortofoto: SelectItem[];
    ruoloBackoffice = localStorage.getItem("selectedRole") === "backoffice";
    ruoloViticolo = localStorage.getItem('selectedRole') === 'viticolo';

    constructor(private formBuilder: FormBuilder, private searchService: SearchGisService, showResults: ShowResults,
        sidebarComponent: SidebarComponent, private toastComponent: ToastGisComponent, public panelEvent: PanelEvent,
        private authService: AuthService, public lavorazioniEvent: LavorazioniEvent, public gisUtils: GisUtilsService
    ) {
        this.isActive = showResults.isActive;
        this.sidebarComponent = sidebarComponent;

        this.visibile_ortofoto = [
            { label: "", value: "" },
            { label: "Si", value: true },
            { label: "No", value: false },
        ];
    }

    ngOnInit() {
        this.anno_campagna = this.gisUtils.getComboAnniCampagna(true);
        this.selectStatoRichiesta = this.statoRichiesta[0].value;

        if (this.listaA4gUtentiBoViticoloSession !== null && this.listaA4gUtentiBoViticoloSession !== undefined
            && this.listaA4gUtentiBoViticoloSession.length > 0) {
            this.mapListaUtenti();
        }

        this.utenteConnesso = this.utentiLavorazione.filter(utente => {
            let user = this.authService.getUserFromSession(); 
            if (user != null && utente.value === user.identificativo) {
                return { name: utente.nome + ' ' + utente.cognome, value: utente.value };
            }
        })[0];

        // Form Ricerca Richieste di modifica suolo
        this.searchForm = this.formBuilder.group({
            cuaa: ['', [Validators.nullValidator, Validators.minLength(3), Validators.maxLength(16)]],
            comune: ['', [Validators.nullValidator]],
            annoCampagna: ['', [Validators.nullValidator]],
            dataInserimento: ['', [Validators.nullValidator]],
            tipoRichiesta: ['', [Validators.required]],
            statoRichiesta: ['', [Validators.nullValidator]],
            idRichiesta: ['', [Validators.nullValidator]],
        });
        // Form Ricerca Lavorazione
        this.searchFormLavorazione = this.formBuilder.group({
            idLavorazione: ['', [Validators.nullValidator, Validators.minLength(3)]],
            titolo: ['', [Validators.nullValidator, Validators.minLength(3)]],
            cuaa: ['', [Validators.nullValidator, Validators.minLength(3), Validators.maxLength(16)]],
            statoLavorazione: ['', [Validators.nullValidator]],
            utente: [this.utenteConnesso, [Validators.nullValidator]]
        });
        // Form Lavorazione
        this.lavorazioneForm = this.formBuilder.group({
            cuaa: ['', [Validators.nullValidator, Validators.minLength(3), Validators.maxLength(16)]],
            comune: ['', [Validators.nullValidator]],
            annoCampagna: ['', [Validators.nullValidator]],
            tagRilevato: ['', [Validators.nullValidator]],
            tagDichiarato: ['', [Validators.nullValidator]],
            idRichiesta: ['', [Validators.nullValidator]],
            visibileOrtofoto: ["", [Validators.nullValidator]],
        });
    }

    ngOnChanges(simpleChanges: SimpleChanges) {
        if (!this.idLavorazione) {
            this.resetFilterLavorazione();
        }
    }

    // get easy access to form fields
    get f() { return this.searchForm.controls; }
    get ricercaLav() { return this.searchFormLavorazione.controls; }
    get lav() { return this.lavorazioneForm.controls; }

    mapListaUtenti() {
        this.listaA4gUtentiBoViticoloSession = this.filtraUtentiDuplicati();
        this.utentiLavorazione = this.listaA4gUtentiBoViticoloSession.map(utente => {
            return { name: utente.nome.toUpperCase() + ' ' + utente.cognome.toUpperCase(), value: utente.userName };
        });
        this.utentiLavorazione.sort(function (a, b) {
            return (a.name < b.name) ? -1 : (a.name > b.name) ? 1 : 0;
        });
    }

    filtraUtentiDuplicati() {
        const utentiNonFiltrati = this.listaA4gUtentiBoViticoloSession, keys = ['codiceFiscale'],
            filtered = utentiNonFiltrati.filter((session => utente =>
                (k => !session.has(k) && session.add(k))
                    (keys.map(k => utente[k]).join('|'))
            )
                (new Set)
            );
        return filtered;
    }
    searchPost(): void {
        this.sidebarComponent.changeShow(null, null, null);

        const params = {
            'richiestaModificaSuolo': this.searchForm.value,
            'pagina': 0,
            'numeroElementiPagina': 10
        };
        // reset campo comune se non viene selezionato nulla
        if (this.selectedComune && !this.selectedComune.codice) {
            this.selectedComune = null;
        }

        if (params.richiestaModificaSuolo.dataInserimento) {
            params.richiestaModificaSuolo.dataInserimento = moment(
                params.richiestaModificaSuolo.dataInserimento
            ).format("YYYY-MM-DDTHH:mm:ss");
        }
        this.submitted = true;
        if (this.searchForm.invalid) {
            return;
        } else {
            this.searchService.resultsPost(params).subscribe(
                (data) => {
                    this.richieste = data;
                    if (this.richieste["count"] > 0) {
                        this.searchService.isActive = true;
                    } else if (this.richieste["count"] === 0) {
                        this.toastComponent.showWarning();
                    } else {
                        this.searchService.isActive = false;
                    }
                    this.panelEvent.searchRichieste = true;
                    this.sidebarComponent.changeShow(
                        this.richieste,
                        params,
                        null
                    );
                },
                (error) => {
                    console.log(error);
                }
            );
        }
        setTimeout(() => {
            const messageBody = document.querySelector(".for-results");
            if (messageBody) {
                messageBody.scrollTop = 0;
            }
        });
    }

    searchLavorazioni() {
        const params = this.searchFormLavorazione.value;
        this.lavorazioniEvent.searchParams = params;
        params.pagina = 0;
        params.numeroElementiPagina = 10;
        params.ordine = "DESC";
        params.proprieta = "id";
        params.titolo =
            params.titolo !== null && params.titolo !== undefined
                ? params.titolo
                : "";
        params.cuaa =
            params.cuaa !== null && params.cuaa !== undefined
                ? params.cuaa
                : "";
        params.statoLavorazione =
            params.statoLavorazione &&
                params.statoLavorazione !== null &&
                params.statoLavorazione !== undefined &&
                params.statoLavorazione.value
                ? params.statoLavorazione.value
                : "";

        if (
            params.utente &&
            params.utente !== null &&
            params.utente !== undefined &&
            params.utente.value
        ) {
            params.utente = params.utente.value;
        } else if (params.utente === null) {
            params.utente = "";
        }

        this.submitted = true;
        if (this.searchFormLavorazione.invalid) {
            return;
        } else {
            this.searchService.ricercaLavorazioni(params).subscribe(
                (data: Lavorazione) => {
                    this.lavorazioni = data;
                    if (this.lavorazioni["count"] > 0) {
                        this.searchService.isActive = true;
                    } else if (this.lavorazioni["count"] === 0) {
                        this.toastComponent.showWarning();
                    } else {
                        this.searchService.isActive = false;
                    }
                    this.panelEvent.searchLavorazioni = true;
                    this.sidebarComponent.changeShowRicercaLavorazioni(
                        this.lavorazioni,
                        params,
                        null
                    );
                },
                (error) => {
                    console.log(error);
                }
            );
        }
        setTimeout(() => {
            const messageBody = document.querySelector(".for-mie-lavorazioni");
            if (messageBody) {
                messageBody.scrollTop = 0;
            }
        });
    }

    searchPoligoniDichiarati(): void {
        const params = {
            poligoniSuoloDichiatato: this.lavorazioneForm.value,
            pagina: 0,
            numeroElementiPagina: 10,
        };
        params.poligoniSuoloDichiatato.statoRichiesta = "LAVORABILE";

        // reset campo comune se non viene selezionato nulla
        if (this.selectedComune && !this.selectedComune.codice) {
            this.selectedComune = null;
        }
        this.submitted = true;
        if (this.lavorazioneForm.invalid) {
            return;
        } else {
            this.searchService
                .resultsPoligoniSuoloDichiarato(params, false)
                .subscribe(
                    (data) => {
                        this.poligoniDichiarati = data;
                        // let counter = data.risultati;
                        if (this.poligoniDichiarati["count"] > 0) {
                            this.searchService.isActive = true;
                        } else if (this.poligoniDichiarati["count"] === 0) {
                            this.toastComponent.showWarning();
                        } else {
                            this.searchService.isActive = false;
                        }
                        this.sidebarComponent.changeShowBo(
                            this.poligoniDichiarati,
                            params,
                            null
                        );
                    },
                    (error) => {
                        this.toastComponent.showErrorServer500();
                        console.log(error);
                    }
                );
        }
        setTimeout(() => {
            const messageBody = document.querySelector(".for-results");
            if (messageBody) {
                messageBody.scrollTop = 0;
            }
        });
    }

    changeTipo(e) {
        if (this.tipoRicerca) {
            this.tipoRicerca.setValue(e.target.value, {
                onlySelf: true,
            });
        }
    }

    changeTipoRichiesta(e) {
        this.TipoRichiesta.setValue(e.target.value, {
            onlySelf: true,
        });
    }

    changeStatoRichiesta(stato) {
        this.selectStatoRichiesta = StatiRichiesta + stato;
        // console.log(this.selectStatoRichiesta)
    }

    filterComuni(event) {
        const filtered: any[] = [];
        const query = event.query;
        for (let i = 0; i < this.listaComuni.risultati.length; i++) {
            const comune = this.listaComuni.risultati[i];
            if (
                comune.denominazione.toLowerCase().includes(query.toLowerCase())
            ) {
                filtered.push(comune);
            } else if (
                comune.codice.toLowerCase().includes(query.toLowerCase())
            ) {
                //  let comuneCodice = comune.denominazione + ' ( ' + comune.codice + ' )';
                //  comune.denominazione = comuneCodice;
                filtered.push(comune);
            }
        }

        this.filteredComuni = filtered;
        // console.log(this.filteredComuni)
    }

    get tipoRicerca() {
        return this.searchForm.get("tipoRicerca");
    }

    resetFilterLavorazione() {
        if (this.lavorazioneForm) {
            this.lavorazioneForm.reset();
        }
    }
}
