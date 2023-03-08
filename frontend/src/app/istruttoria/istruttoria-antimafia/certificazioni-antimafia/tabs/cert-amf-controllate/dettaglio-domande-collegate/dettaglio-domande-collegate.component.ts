import { DomandaCollegataFilter } from 'src/app/istruttoria/istruttoria-antimafia/dto/DomandaCollegataFilter';
import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { DomandaCollegata } from "src/app/istruttoria/istruttoria-antimafia/dto/DomandaCollegata";
import { IstruttoriaAntimafiaService } from "src/app/istruttoria/istruttoria-antimafia/istruttoria-antimafia.service";
import { A4gMessages, A4gSeverityMessage } from "src/app/a4g-common/a4g-messages";
import { Labels } from "src/app/app.labels";
import { StatoBdna } from "src/app/istruttoria/istruttoria-antimafia/dto/StatoBdna";
import { MessageService } from "primeng/api";
import { Location } from '@angular/common';
import { AntimafiaService } from 'src/app/fascicolo/antimafia/antimafia.service';
import { DichiarazioneAntimafia } from 'src/app/fascicolo/antimafia/classi/dichiarazioneAntimafia';
import { SincronizzazioneDateBdnaDto } from 'src/app/a4g-common/classi/SincronizzazioneDateBdnaDto';
import { LoaderService } from 'src/app/loader.service';
import { TipoDomandaEnum } from 'src/app/istruttoria/istruttoria-antimafia/dto/TipoDomandaEnum';
@Component({
    selector: "app-dettaglio-domande-collegate",
    templateUrl: "./dettaglio-domande-collegate.html"
})
export class DettaglioDomandeCollegateComponent implements OnInit {

    activeIndex: number = -1;
    activeIndexMenuAction: number = -1;
    intestazioni = Labels;
    textTitleInner = Labels.domandeCollegateAntimafia;
    textTitle = "";
    idDichiarazioneAntimafia: number;
    dichiarazioneAntimafia: DichiarazioneAntimafia;
    colsSUPDU: any[];
    colsPSRStr: any[];
    parentTabIndex: number;

    loaded: boolean[] = [false, false, false];
    domandeUnicheView: Array<DomandaCollegata> = new Array();
    domandeStrutturaliView: Array<DomandaCollegata> = new Array();
    domandeSuperficieView: Array<DomandaCollegata> = new Array();

    menuActionDomanda: any[] = new Array();
    statoDaAggiornare: StatoBdna;
    domandaDaAggiornare: DomandaCollegata;
    cuaa: string;

    constructor(private route: ActivatedRoute, private istruttoriaAntimafiaService: IstruttoriaAntimafiaService,
        private messages: MessageService,
        private location: Location,
        private antimafiaService: AntimafiaService,
        private loader: LoaderService
    ) {
        //colonne ordinamento PSR_SUPERFICIE e DOMANDA_UNICA
        this.colsSUPDU = [
            { field: "cuaa", header: this.intestazioni.cuaaSigla },
            { field: "idDomanda", header: this.intestazioni.idDomanda },
            { field: "dtDomanda", header: this.intestazioni.dtDomanda },
            { field: "campagna", header: this.intestazioni.campagna },
            { field: "importoRichiesto", header: this.intestazioni.importoRichiesto },
            { field: "dtBdnaOp", header: this.intestazioni.dtBdnaOp },
            { field: "dtInizioSilenzioAssenso", header: this.intestazioni.dtInizioSilenzioAssenso },
            { field: "dtFineSilenzioAssenso", header: this.intestazioni.dtFineSilenzioAssenso },
            { field: "dtInizioEsitoNegativo", header: this.intestazioni.dtInizioEsitoNegativo },
            { field: "dtFineEsitoNegativo", header: this.intestazioni.dtFineEsitoNegativo },
            { field: "dtBdna", header: this.intestazioni.dtBdna },
            { field: "protocollo", header: this.intestazioni.protocollo },
            { field: "statoBdna", header: this.intestazioni.statoBdna },
            { header: this.intestazioni.azioni }
        ];

        //colonne ordinamento PSR_STRUTTURALI
        this.colsPSRStr = [
            { field: "misura", header: this.intestazioni.misura },
            { field: "cuaa", header: this.intestazioni.cuaaSigla },
            { field: "idDomanda", header: this.intestazioni.idDomanda },
            { field: "dtDomanda", header: this.intestazioni.dtDomanda },
            { field: "importoRichiesto", header: this.intestazioni.importoRichiesto },
            { field: "dtBdnaOp", header: this.intestazioni.dtBdnaOp },
            { field: "dtInizioSilenzioAssenso", header: this.intestazioni.dtInizioSilenzioAssenso },
            { field: "dtFineSilenzioAssenso", header: this.intestazioni.dtFineSilenzioAssenso },
            { field: "dtInizioEsitoNegativo", header: this.intestazioni.dtInizioEsitoNegativo },
            { field: "dtFineEsitoNegativo", header: this.intestazioni.dtFineEsitoNegativo },
            { field: "dtBdna", header: this.intestazioni.dtBdna },
            { field: "protocollo", header: this.intestazioni.protocollo },
            { field: "statoBdna", header: this.intestazioni.statoBdna },
            { header: this.intestazioni.azioni }
        ];
        this.menuActionDomanda = [
            {
                label: Labels.azioni.toUpperCase(),
                items: [
                    {
                        label: Labels.ESITO_NEGATIVO,
                        command: () => {
                            this.aggiornaStato(StatoBdna.CHIUSA_CON_ESITO_NEGATIVO);
                        }
                    },
                    { separator: true },
                    {
                        label: Labels.ACCETTATO_BDNA,
                        command: () => {
                            this.aggiornaStato(StatoBdna.IN_ISTRUTTORIA);
                        }
                    },
                    { separator: true },
                    {
                        label: Labels.ANOMALIA_BDNA,
                        command: () => {
                            this.aggiornaStato(StatoBdna.ANOMALIA);
                        }
                    }
                ]
            }
        ];
    }

    ngOnDestroy(): void {
        this.loader.resetTimeout();
    }

    ngOnInit() {
        this.loader.setTimeout(480000); //otto minuti
        this.route.queryParams.subscribe(params => {
            this.activeIndex = Number.parseInt(JSON.parse(params["activeIndex"]));
            this.textTitle = Labels.descAziendaDomandeCollegateAntimafia + JSON.parse(params["descAzienda"]);
            this.idDichiarazioneAntimafia = Number.parseInt(JSON.parse(params["idDichiarazioneAntimafia"]));
            this.cuaa = JSON.parse(params["cuaa"]);
            this.parentTabIndex = Number.parseInt(JSON.parse(params["parentTabIndex"]));
        });

        if (!this.loaded[this.activeIndex] && this.cuaa) {
            this.selectView(this.activeIndex, this.cuaa);
        }

        this.antimafiaService.getDichiarazioneAntimafia(this.idDichiarazioneAntimafia.toString()).subscribe(dichiarazione => {
            this.dichiarazioneAntimafia = dichiarazione;
        }, err => {
            console.log("err - get dichiarazione antimafia" + this.idDichiarazioneAntimafia);
        });
    }

    goBack(): void {
        this.istruttoriaAntimafiaService.setTabIndex(this.parentTabIndex);
        this.location.back();
    }

    public onTabClose(event: any) {
    }

    public onTabOpen(event: any) {
        let index = event.index;
        if (!this.loaded[index] && this.cuaa) {
            this.selectView(index, this.cuaa);
        }
    }

    public onConfirmAggiornaStato() {
        this.messages.clear();
        switch (this.statoDaAggiornare) {
            case StatoBdna.IN_ISTRUTTORIA:
                this.accettata(this.domandaDaAggiornare);
                break;
            case StatoBdna.ANOMALIA:
                this.inAnomalia(this.domandaDaAggiornare);
                break;
            case StatoBdna.CHIUSA_CON_ESITO_NEGATIVO:
                this.esitoNegativo(this.domandaDaAggiornare);
                break;
        }
    }

    private setDomandaView(index: number, domande: DomandaCollegata[]) {
        if (domande) {
            domande.sort((a, b) => {
                if (a.dtDomanda > b.dtDomanda) {
                    return -1;
                }
                else {
                    return 1;
                }
            }).forEach(domanda => {
                if (domanda.dtBdna) {
                    domanda.dtBdna = new Date(domanda.dtBdna);
                }
            })
        }
        switch (index) {
            case 0:
                this.domandeSuperficieView = domande;
                this.loaded[index] = true;
                break;
            case 1:
                this.domandeStrutturaliView = domande;
                this.loaded[index] = true;
                break;
            case 2:
                this.domandeUnicheView = domande;
                this.loaded[index] = true;
                break;
        }
    }

    private selectView(index: number, cuaa: string) {
        switch (index) {
            case 0:
                let domandaCollegataFilterSup = new DomandaCollegataFilter();
                domandaCollegataFilterSup.cuaa = cuaa;
                domandaCollegataFilterSup.tipoDomanda = TipoDomandaEnum.PSR_SUPERFICIE_EU;
                this.istruttoriaAntimafiaService.getDomandeCollegateFilter(domandaCollegataFilterSup).subscribe((domande: DomandaCollegata[]) => {
                    this.setDomandaView(index, domande);
                });
                break;
            case 1:
                let domandaCollegataFilterStru = new DomandaCollegataFilter();
                domandaCollegataFilterStru.cuaa = cuaa;
                domandaCollegataFilterStru.tipoDomanda = TipoDomandaEnum.PSR_STRUTTURALI_EU;
                this.istruttoriaAntimafiaService.getDomandeCollegateFilter(domandaCollegataFilterStru).subscribe((domande: DomandaCollegata[]) => {
                    this.setDomandaView(index, domande);
                });
                break;
            case 2:
                let domandaCollegataFilterDu = new DomandaCollegataFilter();
                domandaCollegataFilterDu.cuaa = cuaa;
                domandaCollegataFilterDu.tipoDomanda = TipoDomandaEnum.DOMANDA_UNICA;
                this.istruttoriaAntimafiaService.getDomandeCollegateFilter(domandaCollegataFilterDu).subscribe((domande: DomandaCollegata[]) => {
                    this.setDomandaView(index, domande);
                });
                break;
        }
    }

    onDropdownMenuOpen(activeIndexMenuAction: number, domandaCollegata: DomandaCollegata) {
        this.activeIndexMenuAction = activeIndexMenuAction;
        this.domandaDaAggiornare = domandaCollegata;
    }

    private esitoNegativo(domanda: DomandaCollegata) {
        domanda.dtInizioSilenzioAssenso = new Date(domanda.dtBdna.getTime());;

        domanda.dtInizioSilenzioAssenso.setDate(domanda.dtInizioSilenzioAssenso.getDate() + 30);
        domanda.dtFineSilenzioAssenso = new Date(domanda.dtBdna.getTime());
        domanda.dtFineSilenzioAssenso.setDate(domanda.dtFineSilenzioAssenso.getDate() + 210);
        domanda.dtInizioEsitoNegativo = domanda.dtBdna;
        domanda.dtFineEsitoNegativo = new Date(domanda.dtBdna.getTime());;
        domanda.dtFineEsitoNegativo.setDate(domanda.dtFineEsitoNegativo.getDate() + 365);
        domanda.statoBdna = StatoBdna.CHIUSA_CON_ESITO_NEGATIVO;
        this.istruttoriaAntimafiaService.aggiornaDomandeCollegate(domanda).subscribe(domandaOut => {
            this.selectView(this.activeIndexMenuAction, domandaOut.cuaa);
            this.sincronizzaDateBDNA(domandaOut);
            this.messages.add(A4gMessages.getToast("tst", A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        }, err => {
            console.log("Err -" + err.toString());
            this.messages.add(A4gMessages.getToast("tst", A4gSeverityMessage.warn, A4gMessages.CTRLIAMCNT007_KO));
        });
    }

    private inAnomalia(domanda: DomandaCollegata) {
        domanda.dtInizioEsitoNegativo = null;
        domanda.dtFineEsitoNegativo = null;
        domanda.statoBdna = StatoBdna.ANOMALIA;
        this.istruttoriaAntimafiaService.aggiornaDomandeCollegate(domanda).subscribe(domandaOut => {
            this.selectView(this.activeIndexMenuAction, domandaOut.cuaa);
            this.sincronizzaDateBDNA(domandaOut);
            this.messages.add(A4gMessages.getToast("tst", A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        }, err => {
            console.log("Err -" + err.toString());
            this.messages.add(A4gMessages.getToast("tst", A4gSeverityMessage.warn, A4gMessages.CTRLIAMCNT007_KO));
        });
    }

    private accettata(domanda: DomandaCollegata) {
        domanda.dtInizioSilenzioAssenso = new Date(domanda.dtBdna.getTime());;
        domanda.dtInizioSilenzioAssenso.setDate(domanda.dtInizioSilenzioAssenso.getDate() + 30);
        domanda.dtFineSilenzioAssenso = new Date(domanda.dtBdna.getTime());;
        domanda.dtFineSilenzioAssenso.setDate(domanda.dtFineSilenzioAssenso.getDate() + 210);
        domanda.dtInizioEsitoNegativo = null;
        domanda.dtFineEsitoNegativo = null;
        domanda.statoBdna = StatoBdna.IN_ISTRUTTORIA;
        this.istruttoriaAntimafiaService.aggiornaDomandeCollegate(domanda).subscribe(domandaOut => {
            this.selectView(this.activeIndexMenuAction, domandaOut.cuaa);
            this.sincronizzaDateBDNA(domandaOut);
            this.messages.add(A4gMessages.getToast("tst", A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        }, err => {
            console.log("Err -" + err.toString());
            this.messages.add(A4gMessages.getToast("tst", A4gSeverityMessage.warn, A4gMessages.CTRLIAMCNT007_KO));
        });
    }

    private aggiornaStato(stato: StatoBdna) {
        let domanda: DomandaCollegata = this.domandaDaAggiornare;
        if (domanda.statoBdna === StatoBdna.NON_CARICATO) {
            this.messages.add(A4gMessages.getToast("tst", A4gSeverityMessage.error, A4gMessages.CTRLIAMCNT012));
        }
        else if (!domanda.dtBdna || !(domanda.protocollo || "").trim()) {
            this.messages.add(A4gMessages.getToast("tst", A4gSeverityMessage.warn, A4gMessages.CTRLIAMCNT007));
            return;
        }
        else {
            this.statoDaAggiornare = stato;
            this.messages.add(A4gMessages.getToast("aggiorna-stato", A4gSeverityMessage.warn, A4gMessages.WARNING_AGGIORNA_DOMANDA_COLLEGATA(stato)));
        }
    }

    // sincronizzazione ags 
    private sincronizzaDateBDNA(domanda: DomandaCollegata) {
        let sync: SincronizzazioneDateBdnaDto;
        sync = {
            cuaa: domanda.cuaa,
            idDomanda: domanda.idDomanda,
            tipoDomanda: domanda.tipoDomanda,
            dtInizioEsitoNegativo: domanda.dtInizioEsitoNegativo,
            dtInizioSilenzioAssenso: domanda.dtInizioSilenzioAssenso,
            dtFineEsitoNegativo: domanda.dtFineEsitoNegativo,
            dtFineSilenzioAssenso: domanda.dtFineSilenzioAssenso
        }
        this.istruttoriaAntimafiaService.sincronizzaDateBDNAAntimafia([sync]);
    }

    onRejectAggiornaStato() {
        this.messages.clear();
    }
}