import { Component, OnInit, ViewChild, ElementRef, OnDestroy } from '@angular/core';
import { DomandaIntegrativa, Capo, Intervento, Allevamento, DettaglioIntervento } from './classi/DomandaIntegrativa';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { DomandaIntegrativaService } from './domanda-integrativa.service';
import { IstruttoriaService } from '../domandaUnica/istruttoria.service';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { Labels } from 'src/app/app.labels';
import { Istruttoria } from 'src/app/a4g-common/classi/Istruttoria';
import { CapoRichiesto, DettaglioCapi } from '../domandaUnica/domain/dettaglioCapi';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Configuration } from 'src/app/app.constants';
import * as FileSaver from "file-saver";
import { FascicoloService } from 'src/app/fascicolo/fascicolo.service';
import { Domanda } from '../domandaUnica/domain/domandaUnica';
import { ElencoDomandeService } from '../domandaUnica/services/elenco-domande.service';
import { FascicoloCorrente } from 'src/app/fascicolo/fascicoloCorrente';
import { DatePipe } from '@angular/common';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { LoaderService } from 'src/app/loader.service';
import { SostegnoDuDi } from '../domandaUnica/classi/SostegnoDuDi';

// import json from './dummy_domanda';

@Component({
    selector: 'app-domanda-integrativa',
    templateUrl: './domanda-integrativa.component.html',
    styleUrls: ['./domanda-integrativa.component.css', './domanda-integrativa.component.scss']
})
export class DomandaIntegrativaComponent implements OnInit, OnDestroy {
    constructor(
        private domandaIntegrativaService: DomandaIntegrativaService,
        private route: ActivatedRoute,
        private istruttoriaService: IstruttoriaService,
        private messageService: MessageService,
        private http: HttpClient,
        private _configuration: Configuration,
        private fascicoloService: FascicoloService,
        private elencoDomandeService: ElencoDomandeService,
        private fascicoloCorrente: FascicoloCorrente,
        private loader: LoaderService
    ) { }

    public domandaIntegrativa: DomandaIntegrativa;
    public selezionatiConSanzione: number = 0;
    public selezionatiSenzaSanzione: number = 0;
    public nonSelezionati: number = 0;
    public sanzione: boolean = false;
    public selezionatoConSanzione: boolean = false;
    public btnPresentaDomanda: boolean = false;
    public btnModificaDomanda: boolean = false;
    public index = undefined;
    public btnStampa: boolean = true;
    public finestraChiusa: boolean = false;

    // variabili popup
    displayPopupInfoCapo = false;
    popupMessaggio = "";
    codiceCapo = "";
    esitoCapo = "";
    popupCodiceIntervento = "";
    popupCodiceSpecie = "";

    displayPopupInfoIntervento = false;
    codiceAgeaPopUp = "";
    descInterventoLungaPopUp = "";
    descInterventoBrevePopUp = "";

    totali: Map<string, number[]>;
    numeroCapiNonSelezionati = 0;

    idDomandaUnica: string;

    labels = Labels;

    allevamenti: DettaglioCapi[] = [];
    capiPerIntervento = new Map<string, DettaglioIntervento[]>();
    sumCapiPerIntervento = new Map<string, number>();
    domanda = new Domanda;
    cuaa: string;

    icon = "ui-icon-file-upload";
    iconDownload = "ui-icon-file-download";
    uploadOk: boolean = false;
    fileExt: string = ".PDF";
    maxSize: number = 2;
    fileRicevutaDI: String;
    contentIsReady: boolean = false;

    sorting = (a, b) => {
        if (a.interventi.every(int => int.esito === "NON_AMMISSIBILE")) return 1;
        if (b.interventi.every(int => int.esito === "NON_AMMISSIBILE")) return -1;
        return 0;
    };

    @ViewChild('upFileDI', { static: true }) fileInput: ElementRef;

    ngOnDestroy(): void {
        this.loader.resetTimeout();
    }

    ngOnInit() {
        // this.domandaIntegrativa = json as any;
        this.loader.setTimeout(480000); //otto minuti
        this.domandaIntegrativa = this.domandaIntegrativaService.getDomandaIntegrativa();
        if (this.domandaIntegrativa.identificativo) {
            this.idDomandaUnica = this.domandaIntegrativa.identificativo.replace("DI_", "");
        }
        else {
            this.idDomandaUnica = this.domandaIntegrativaService.getIdDomandaUnica();
        }
        this.domandaIntegrativa.allevamenti.forEach(allevamento => {

            // allevamento.capi = allevamento.capi.sort(this.sorting);
            allevamento.capi.sort(this.sorting).forEach(capo => {

                capo.interventi = this.fillInterventi(capo.interventi);
            });

        });
        this.initMappaTotali();
        this.calcolaTotali();
        this.contentIsReady = true;
        this.checkFinestraPresentazione();
        this.verificaPresenzaRicevutaDomandaIntegrativa();
    }

    get isReadOnly() {
        return !this.btnPresentaDomanda && this.domandaIntegrativa.stato == 'PRESENTATA';
    }

    fillInterventi(interventi: Intervento[]): Intervento[] {
        let listaInterventi: string[] = ["310", "311", "313", "322", "315", "316", "318", "320", "321"];
        listaInterventi.forEach(int => {
            let trovato = interventi.find(intervento => intervento.codice === int);
            if (!trovato) {
                let interventoToFill = new Intervento();
                interventoToFill.codice = int;
                interventi.push(interventoToFill);
            }
        });
        let temp: string;
        interventi = interventi.sort((a, b) => a.codice.localeCompare(b.codice));
        interventi.splice(3, 0, interventi[8]);
        interventi.pop();
        return interventi;
    }
    /*
    310  311
    A       A         O tutti e due o nessuno dei due
    A       NA      Può scegliere solo il 310
    A       AS      O tutti e due o nessuno dei due
    NA    NA      -
    AS    AS         O tutti e due o nessuno dei due
    AS    NA      Può scegliere solo il 310
    
    */
    exclusiveCheckbox(currentIntervento: Intervento, listaIntervento: Intervento[], checked: any, primeCheckBox: any) {
        if (currentIntervento.codice === '310' || currentIntervento.codice === '311') {
            const int310: Intervento = listaIntervento.filter(int => int.codice === '310')[0];
            const int311: Intervento = listaIntervento.filter(int => int.codice === '311')[0];
            if (int310.esito !== 'NON_AMMISSIBILE' && int311.esito !== 'NON_AMMISSIBILE') {
                int310.selezionato = checked;
                int311.selezionato = checked;
            }
        }
        listaIntervento.filter(int => int.codice != currentIntervento.codice).forEach(int => {
            if (!(currentIntervento.codice == '310' || currentIntervento.codice == '311'))
                int.selezionato = false;
            else {
                if (!(int.codice == '310' || int.codice == '311'))
                    int.selezionato = false;
            }
        });
        this.calcolaTotali();
    };

    mostraIconaSanzione(capo: Capo): boolean {
        return capo.interventi.some((intervento: Intervento) => {
            if (intervento.selezionato && intervento.esito == 'AMMISSIBILE_CON_SANZIONE') {
                return true;
            }
            else {
                return false;
            }
        });
    };

    mostraIconaNonSelezionato(capo: Capo): boolean {
        const capoInterventiFilter = capo.interventi.filter(intervento => intervento.esito != null);
        let interventiNonAmmissibili = capo.interventi.filter(intervento => intervento.esito === "NON_AMMISSIBILE");
        if (interventiNonAmmissibili.length !== capoInterventiFilter.length) {
            let interventiNonSelezionati = capoInterventiFilter.filter(intervento => intervento.selezionato === false);
            if (interventiNonSelezionati.length === capoInterventiFilter.length) {
                return true;
            }
        }
        return false;

    };

    checkDisabled(capo: Capo, numIntervento: string): boolean {
        return capo.interventi.some((intervento: Intervento) => {
            if (intervento.codice == numIntervento && intervento.esito == 'NON_AMMISSIBILE') {
                return true;
            }
            else {
                return false;
            }
        });
    };

    getVisibilityDiv(capo: Capo, numIntervento: string): boolean {
        return capo.interventi.some((intervento: Intervento) => {
            if (intervento.codice == numIntervento) {
                return true;
            }
            else {
                return false;
            }
        });
    }

    checkSanzione(capo: Capo, numIntervento: string): boolean {
        return capo.interventi.some((intervento: Intervento) => {
            if (intervento.codice == numIntervento && intervento.esito == 'AMMISSIBILE_CON_SANZIONE') {
                return true;
            }
            else {
                return false;
            }
        });
    };

    checkSelezione(capo: Capo, numIntervento: string): string {
        let listaInterventi: string[] = [];
        capo.interventi.forEach(intervento => {
            if (intervento.codice == numIntervento && intervento.selezionato) {
                listaInterventi.push(numIntervento);
            }
        });
        return listaInterventi[0] != null ? listaInterventi[0] : "";
    }

    contaNonSelezionati(allevamento: Allevamento): number {
        let contaNonSelezionati = 0;
        allevamento.capi.forEach(capo => {
            const capoInterventiFilter = capo.interventi.filter(intervento => intervento.esito != null);
            let interventiNonAmmissibili = capoInterventiFilter.filter(intervento => intervento.esito === "NON_AMMISSIBILE");
            if (interventiNonAmmissibili.length !== capoInterventiFilter.length) {
                let interventiNonSelezionati = capoInterventiFilter.filter(intervento => intervento.selezionato === false);
                if (interventiNonSelezionati.length === capoInterventiFilter.length) {
                    contaNonSelezionati++;
                }
            }
        });
        return contaNonSelezionati;
    }

    contaSelezionatiConSanzione(allevamento) {
        let contaSelezionatiConSanzione: number = 0;
        allevamento.capi.forEach((capo: Capo) => {
            capo.interventi.forEach((intervento: Intervento) => {
                if (intervento.selezionato == true && intervento.esito == "AMMISSIBILE_CON_SANZIONE") {
                    contaSelezionatiConSanzione = contaSelezionatiConSanzione + 1;
                }
            });
        });
        return contaSelezionatiConSanzione;
    }

    contaSelezionatiSenzaSanzione(allevamento) {
        let contaSelezionatiSenzaSanzione: number = 0;
        allevamento.capi.forEach((capo: Capo) => {
            capo.interventi.forEach((intervento: Intervento) => {
                if (intervento.selezionato == true && intervento.esito == "AMMISSIBILE") {
                    contaSelezionatiSenzaSanzione = contaSelezionatiSenzaSanzione + 1;
                }
            });
        });
        return contaSelezionatiSenzaSanzione;
    }

    getEsitoCapo(capo, codIntervento) {
        let codiceEsito = capo.interventi.filter(intervento => { return intervento.codice == codIntervento })[0].idEsito;

        this.istruttoriaService.getEsitoCapo(this.idDomandaUnica, codiceEsito).toPromise().then(
            data => {
                if (data.esito === "AMMISSIBILE") { return; }
                this.popupCodiceSpecie = capo.codSpecie;
                this.popupMessaggio = data.messaggio.replace("sanzioni", "anomalia");
                this.codiceCapo = data.codiceCapo;
                this.esitoCapo = data.esito.replace("_", " ").toLowerCase();
                this.esitoCapo = this.esitoCapo.replace("_", " ");
                this.esitoCapo = this.esitoCapo.replace("sanzione", "anomalia");
                this.popupCodiceIntervento = codIntervento;
                this.displayPopupInfoCapo = true;
            },
            error => {
                this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
            }
        )
    }

    chiudiPopup() {
        this.displayPopupInfoCapo = false;
    }

    chiudiPopupDescIntervento() {
        this.displayPopupInfoIntervento = false;
    }

    calcolaTotali() {
        this.numeroCapiNonSelezionati = 0;
        this.initMappaTotali();
        this.domandaIntegrativa.allevamenti.forEach(allevamento => {
            allevamento.capi.forEach(capo => {
                const capoInterventiFilter = capo.interventi.filter(intervento => intervento.esito != null);

                capoInterventiFilter.forEach(intervento => {
                    if (intervento.esito === "AMMISSIBILE_CON_SANZIONE" && intervento.selezionato) {
                        this.totali.get(intervento.codice)[1]++;
                    } else if (intervento.esito === "NON_AMMISSIBILE") {
                        this.totali.get(intervento.codice)[2]++;
                    } else if (intervento.esito === "AMMISSIBILE" && intervento.selezionato) {
                        this.totali.get(intervento.codice)[0]++;
                    }
                });

                let interventiNonAmmissibili = capoInterventiFilter.filter(intervento => intervento.esito === "NON_AMMISSIBILE");
                if (interventiNonAmmissibili.length !== capoInterventiFilter.length) {
                    let interventiNonSelezionati = capoInterventiFilter.filter(intervento => intervento.selezionato === false);
                    if (interventiNonSelezionati.length === capoInterventiFilter.length) {
                        this.numeroCapiNonSelezionati++;
                    }
                }
            });
        });
    }

    initMappaTotali() {
        this.totali = new Map<string, number[]>();
        this.totali.set("310", [0, 0, 0]);
        this.totali.set("311", [0, 0, 0]);
        this.totali.set("313", [0, 0, 0]);
        this.totali.set("322", [0, 0, 0]);
        this.totali.set("315", [0, 0, 0]);
        this.totali.set("316", [0, 0, 0]);
        this.totali.set("318", [0, 0, 0]);
        this.totali.set("320", [0, 0, 0]);
        this.totali.set("321", [0, 0, 0]);
    }

    getDescIntervento(codiceAgea: string) {
        this.istruttoriaService.ricercaIstruttorie().subscribe(
            (istruttorie: Array<Istruttoria>) => {
                let jsonParams: any = { codiceAgea: codiceAgea };
                this.istruttoriaService.getInterventi(String(istruttorie[0].id), jsonParams).subscribe(interventi => {
                    this.codiceAgeaPopUp = codiceAgea;
                    this.descInterventoBrevePopUp = interventi[0].descrizioneBreve;
                    this.descInterventoLungaPopUp = interventi[0].descrizioneLunga;
                    this.displayPopupInfoIntervento = true;
                },
                    error => {
                        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
                    }

                );
            }, error => {
                this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
            }
        );

    }

    stampaDomanda() {
        if (this.domandaIntegrativa.stato == 'PRESENTATA') {
            this.getDatiFascicoloDomanda();
            let params: any = { tipo: "RICHIESTO" };
            this.istruttoriaService.getCapi(this.idDomandaUnica, JSON.stringify(params))
                .subscribe((dati => {
                    if (dati) {
                        dati.forEach(element => {
                            if (element.richiesteAllevamentoDuEsito.length > 0) {
                                element.datiAllevamento = JSON.parse(element.datiAllevamento);
                                element.datiDetentore = JSON.parse(element.datiDetentore);
                                element.datiProprietario = JSON.parse(element.datiProprietario);
                                this.allevamenti.push(element);
                            }
                        });
                        this.allevamenti.forEach(element => {
                            let capiRichiesti: Array<CapoRichiesto> = new Array<CapoRichiesto>();
                            element.richiesteAllevamentoDuEsito.forEach(result => {
                                if (result.stato == "PRESENTATA") {
                                    result.esito = result.esito.split('_').join(' ');
                                    capiRichiesti.push(result);
                                }
                            });
                            element.richiesteAllevamentoDuEsito = capiRichiesti;
                        });
                        this.allevamenti.forEach(element => {
                            if (!(element.richiesteAllevamentoDuEsito.length > 0)) {
                                const index = this.allevamenti.indexOf(element);
                                if (index !== -1) {
                                    this.allevamenti.splice(index, 1);
                                }
                            }
                        });
                        this.allevamenti.sort(function (a, b) {
                            return a.codiceIntervento - b.codiceIntervento;
                        });
                    }
                    let totCapiConSanzione: number = 0;
                    let totCapiSenzaSanzione: number = 0;
                    let totCapiTotali: number = 0;
                    let listaInterventi: string[] = ["310", "311", "313", "322", "315", "316", "318", "320", "321"];
                    listaInterventi.forEach(int => {
                        this.capiPerIntervento.set(int, Array<DettaglioIntervento>());
                        this.allevamenti.forEach(element => {
                            if (element.codiceIntervento === int) {
                                this.capiPerIntervento.get(int).push({ descAllevamento: (element.datiAllevamento.codiceAllevamento + ' - ' + element.datiAllevamento.descrizioneAllevamento), codSpecie: element.codiceSpecie, capi: element.richiesteAllevamentoDuEsito })
                            }
                        });
                        this.sumCapiPerIntervento.set(int, (this.totali.get(int)[0] + this.totali.get(int)[1]));
                        totCapiConSanzione = totCapiConSanzione + this.totali.get(int)[1];
                        totCapiSenzaSanzione = totCapiSenzaSanzione + this.totali.get(int)[0];
                        totCapiTotali = totCapiTotali + (this.totali.get(int)[0] + this.totali.get(int)[1]);
                    });
                    this.sumCapiPerIntervento.set('totCapiRichiestiConSanzione', totCapiConSanzione);
                    this.sumCapiPerIntervento.set('totCapiRichiestiSenzaSanzione', totCapiSenzaSanzione);
                    this.sumCapiPerIntervento.set('totCapiTotali', totCapiTotali);
                    this.getPDF();
                    this.allevamenti = [];
                }), (error => {
                    console.log(error);
                    this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
                }));
        }
        else
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.stampaDomandaIntegrativa));
    }

    private getDatiFascicoloDomanda() {
        this.route
            .params
            .subscribe(params => {
                this.getFascicolo(params['idFascicolo']);
            });
    }

    getFascicolo(idFascicolo: number): void {
        this.fascicoloService.getFascicolo(idFascicolo).subscribe((next) => {
            this.cuaa = next.cuaa;
            this.getDatiDomanda();
        });
    }

    private getDatiDomanda() {
        const parametri = { statoSostegno: 'INTEGRATO', sostegno: 'ACC_ZOOTECNIA', cuaa: this.cuaa };
        const jsonParametri = JSON.stringify(parametri);
        const jsonPaginazione = '';
        let jsonOrdinamento = '';
        this.elencoDomandeService.getPaginaDomandeAttuale(encodeURIComponent(jsonParametri), encodeURIComponent(jsonPaginazione), encodeURIComponent(jsonOrdinamento))
            .subscribe((dati => {
                var date = this.convertDate(dati.risultati[0].dataDI);
                dati.risultati[0].dataDI = date
                this.domanda = dati.risultati[0];
            }));
    }

    convertDate(dateString) {
        var p = dateString.split(/\D/g)
        return [p[2], p[1], p[0]].join("-")
    }

    getPDF(): any {
        this.http.get(this._configuration.UrlGetRicevutaDomandaIntegrativaZootecnia, { responseType: "blob" })
            .subscribe(response => {
                let url = this._configuration.UrlStampaPDF;
                let template: File = this.blobToFile(response, "ricevutaDomandaIntegrativaZootecnia.docx");
                var dati = {
                    "datiDomanda": this.domanda,
                    "allevamenti": Array.from(this.capiPerIntervento.entries()),
                    "totali": Array.from(this.totali.entries()),
                    "sumCapi": Array.from(this.sumCapiPerIntervento.entries())
                };
                console.log(dati);
                const formData = new FormData();
                formData.append("file", template, template.name);
                formData.append("dati", JSON.stringify(dati));
                formData.append("formatoStampa", "PDF");
                return this.http
                    .post(url, formData, { responseType: "blob" })
                    .subscribe(data => {
                        FileSaver.saveAs(data, "RicevutaDomandaIntegrativa.pdf");
                    }, (err: HttpErrorResponse) => {
                        console.error("Errore nella creazione della ricevuta di domanda integrativa:", err);
                    });
            }),
            (error => {
                console.log(error);
                this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
            });
    }

    presentaDomanda() {
        this.index = undefined;
        if (this.numeroCapiNonSelezionati > 0) {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.controlloPresentaDomandaIntegrativa));
        }
        else {
            this.btnModificaDomanda = false;
            this.btnPresentaDomanda = false;
            this.btnStampa = true;
            this.domandaIntegrativa.allevamenti.map((allevamenti: Allevamento) => {
                return allevamenti.capi.map((capo: Capo) => {
                    let newListOfInterventi: Array<Intervento> = capo.interventi.filter((intervento: Intervento) => intervento.selezionato);
                    capo.interventi = newListOfInterventi;
                    return capo;
                });
            });

            this.istruttoriaService.presentaDomandaIntegrativa(this.idDomandaUnica, this.domandaIntegrativa).subscribe((domandaIntegrativa: DomandaIntegrativa) => {
                this.istruttoriaService.getDomandaIntegrativaByDomandaUnica(Number.parseInt(this.idDomandaUnica)).subscribe((di: DomandaIntegrativa) => {
                    this.setDomandaIntegrativa(di);
                    let datePipe: DatePipe = new DatePipe("it-IT");
                    let sysdate: string = datePipe.transform(new Date(), 'dd/MM/yyyy HH:MM:ss');
                    this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.domandaIntegrativaPresentata(this.idDomandaUnica, this.fascicoloCorrente.fascicolo.cuaa, sysdate, di.identificativo)));

                },
                    error => A4gMessages.handleError(this.messageService, error.error, "Errore nel salvataggio della domanda integrativa")
                );
            },
                error => A4gMessages.handleError(this.messageService, error.error, "Errore nel salvataggio della domanda integrativa")
            );
        }
    }

    salvaDomanda() {
        this.index = undefined;
        this.domandaIntegrativa.allevamenti.map((allevamenti: Allevamento) => {
            return allevamenti.capi.map((capo: Capo) => {
                let newListOfInterventi: Array<Intervento> = capo.interventi.filter((intervento: Intervento) => intervento.selezionato);
                capo.interventi = newListOfInterventi;
                return capo;
            });
        });

        this.istruttoriaService.salvaDomandaIntegrativa(this.idDomandaUnica, this.domandaIntegrativa).subscribe((domandaIntegrativa: DomandaIntegrativa) => {
            this.istruttoriaService.getDomandaIntegrativaByDomandaUnica(Number.parseInt(this.idDomandaUnica)).subscribe((di: DomandaIntegrativa) => {
                this.setDomandaIntegrativa(di);
                if (this.numeroCapiNonSelezionati > 0) {
                    this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.controlloSalvaDomandaIntegrativa));
                }
                else {
                    this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
                }
            },
                error => A4gMessages.handleError(this.messageService, error.error, "Errore nel salvataggio della domanda integrativa")
            );
        },
            error => A4gMessages.handleError(this.messageService, error.error, "Errore nel salvataggio della domanda integrativa")
        );

    }

    setDomandaIntegrativa(domandaIntegrativa: DomandaIntegrativa) {
        this.domandaIntegrativaService.setDomandaIntegrativa(domandaIntegrativa);
        this.domandaIntegrativa = this.domandaIntegrativaService.getDomandaIntegrativa();
        this.domandaIntegrativa.allevamenti.forEach(allevamento => {
            allevamento.capi.sort(this.sorting).forEach(capo => {
                capo.interventi = this.fillInterventi(capo.interventi);
            });
        });
        this.initMappaTotali();
        this.calcolaTotali();
    }

    modificaDomanda() {
        this.messageService.add({ key: 'checkModificaDomanda', sticky: true, severity: 'warn', summary: A4gMessages.modficiaDomandaIntegrativaDocumento, detail: 'Si vuole procedere alla modifica?' });
    }

    verifyUploadFile() {
        if (this.uploadOk)
            this.messageService.add(A4gMessages.getToast("warn-already-uploadedFile", A4gSeverityMessage.warn, A4gMessages.FILE_ALREADY_UPLOADED));
        else
            document.getElementById('upFileDI').click();
    }

    onRejectHasAlreadyUploadedFile() {
        this.messageService.clear('warn-already-uploadedFile');
    }

    onConfirmHasAlreadyUploadedFile() {
        this.messageService.clear('warn-already-uploadedFile');
        document.getElementById('upFileDI').click();
    }

    uploadFile(event) {
        console.log("ChangeFile");
        if (event.target.files && event.target.files.length > 0) {
            let file: File = event.target.files[0];
            console.log("FileName " + file.name);
            console.log(file);
            let uploadHelper = new UploadHelper(this.fileExt, this.maxSize);
            console.log("FileSize " + uploadHelper.getFileSize(file));
            if (uploadHelper.isValidFile(file)) {
                console.log("SubmitFile");
                this.domandaIntegrativaService.readByteFile(file).then(
                    (fileBase64: string) => {
                        this.fileRicevutaDI = fileBase64;
                        console.log("Upload ok " + fileBase64);
                        this.persistiDomanda(this.idDomandaUnica, file);
                    },
                    (err) => {
                        this.handleError(err)
                    }
                );
            }
            else {
                console.log("File non valido");
                uploadHelper.errors.forEach((item, index) => {
                    this.messageService.add({ key: 'tst', severity: 'error', summary: 'Errore!', detail: item, life: A4gMessages.TOAST_LIFE });
                })
            }
        }
    }

    persistiDomanda(idDomanda, file) {
        console.log('Inzio POST');
        this.istruttoriaService.inserisciDomanda(idDomanda, file)
            .subscribe(
                x => {
                    this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
                    this.uploadOk = true;
                    this.icon = "ui-icon-done";
                    this.fileInput.nativeElement.value = '';
                },
                error => {
                    console.error('Errore in inserisciDomanda: ' + error),
                        A4gMessages.handleError(this.messageService, error, A4gMessages.SALVATAGGIO_RICHIESTA_UTENTE)
                }
            );
    }

    handleError(error: any) {
        console.log("Error " + error + " msg " + error.error + " error.code " + error.status);
        if (error && error.error && error.status == 500) {
            let errMsg = error.error;
            this.messageService.add({ key: 'tst', severity: 'error', summary: 'Errore!', detail: errMsg, life: A4gMessages.TOAST_LIFE });
        }
        else
            this.messageService.add({ key: 'tst', severity: 'error', summary: 'Errore!', detail: 'Errore generico', life: A4gMessages.TOAST_LIFE });
    }

    private blobToFile = (theBlob: Blob, fileName: string): File => {
        var b: any = theBlob;
        b.lastModifiedDate = new Date();
        b.name = fileName;
        return <File>theBlob;
    };

    onRejectCheckModificaDomanda() {
        this.messageService.clear('checkModificaDomanda');
    }

    onConfirmCheckModificaDomanda() {
        this.btnPresentaDomanda = true;
        this.btnModificaDomanda = true;
        this.btnStampa = false;
        this.uploadOk = false;
        this.messageService.clear('checkModificaDomanda');
        this.istruttoriaService.deleteUploadedFile(this.idDomandaUnica).subscribe(
            data => {
                this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
                this.icon = "ui-icon-file-upload";
            },
            err => {
                this.handleError(err);
            }
        )
    }

    checkFinestraPresentazione() {
        let dataChiusura: Date = new Date(this.domandaIntegrativaService.getDataChiusura());
        let sysdate: Date = new Date();
        sysdate.setDate(sysdate.getDate());
        if (sysdate > dataChiusura) {
            this.finestraChiusa = true;
        }
    }

    verificaPresenzaRicevutaDomandaIntegrativa() {
        const baseRequest = { idDomanda: this.idDomandaUnica };
        const request = encodeURIComponent(JSON.stringify(baseRequest));
        this.istruttoriaService.getRicevutaDomandaIntegrativa(request)
            .subscribe(
                result => {
                    this.uploadOk = true;
                    this.icon = "ui-icon-done";
                },
                err => {
                    this.icon = "ui-icon-file-upload";
                }
            );
    }

    downloadRicevutaDomandaIntegrativa() {
        const baseRequest = { idDomanda: this.idDomandaUnica };
        const request = encodeURIComponent(JSON.stringify(baseRequest));
        this.istruttoriaService.getRicevutaDomandaIntegrativa(request)
            .subscribe(
                result => {
                    FileSaver.saveAs(result, this.idDomandaUnica.toString().concat('.pdf'));
                },
                err => {
                    this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.stampaRicevutaDomandaIntegrativa));
                }
            );
    }

}
