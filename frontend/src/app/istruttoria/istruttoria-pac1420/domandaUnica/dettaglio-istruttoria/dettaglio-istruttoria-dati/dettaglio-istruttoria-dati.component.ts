import { Component, OnInit, Output, EventEmitter } from "@angular/core";
import { ConfirmationService } from "primeng/api";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { IstruttoriaCorrente } from "../../istruttoriaCorrente";
import { ActivatedRoute, Router } from "@angular/router";
import { IstruttoriaService } from "../../istruttoria.service";
import { Configuration } from "src/app/app.constants";
import { Istruttoria } from "src/app/a4g-common/classi/Istruttoria";
import { IstruttoriaEdit } from "../../classi/istruttoria-edit";

@Component({
    selector: 'app-dettaglio-istruttoria-dati',
    templateUrl: './dettaglio-istruttoria-dati.component.html',
    styleUrls: ['./dettaglio-istruttoria-dati.component.css'],
    providers: [ConfirmationService]
})
export class DettaglioIstruttoriaDatiComponent implements OnInit {


    @Output() aggiornaDomandeCounters = new EventEmitter();
    production: boolean;
    controlloRicev = false;
    showSostegno = false;
    controllo: any;
    it: any;
    updateForm: FormGroup;
    editable = false; // variabile che rende editabile i dati di dettaglio istruttoria
    constructor(private istruttoriaCorrente: IstruttoriaCorrente, private fb: FormBuilder,
        private route: ActivatedRoute, private istruttoriaService: IstruttoriaService, private confirmationService: ConfirmationService,
        private router: Router, private conf: Configuration) {
    }

    ngOnInit() {
        this.it = {// imposto i valori del calendario
            firstDayOfWeek: 1,
            dayNames: ['domenica', 'lunedi', 'martedi', 'mercoledi', 'giovedi', 'venerdi', 'sabato'],
            dayNamesShort: ['dom', 'lun', 'mar', 'mer', 'gio', 'ven', 'sab'],
            dayNamesMin: ['Dom', 'Lun', 'Mar', 'Mer', 'Gio', 'Ven', 'Sab'],
            monthNames: ['Gennaio', 'Febbraio', 'Marzo', 'Aprile', 'Maggio', 'Giugno', 'Luglio', 'Agosto', 'Settembre',
                'Ottobre', 'Novembre', 'Dicembre'],
            monthNamesShort: ['Gen', 'Feb', 'Mar', 'Apr', 'Mag', 'Giu', 'Lug', 'Ago', 'Sep', 'Ott', 'Nov', 'Dic'],
            today: 'Oggi',
            clear: 'Svuota'
        };
    }

    getIstruttoriaCorrente(): Istruttoria {
        return this.istruttoriaCorrente.istruttoria;
    }


    modifica(): void {
        this.confirmationService.confirm({
            message: 'Attenzione! Sei davvero sicuro di voler modificare questi dati?',
            acceptLabel: 'Sì',
            rejectLabel: 'No',
            rejectVisible: true,
            accept: () => {
                this.editable = true;

                this.updateForm = this.fb.group({
                    percIncrementoGreening: [(this.getIstruttoriaCorrente().percIncrementoGreening * 100).toFixed(2), [Validators.required,
                    Validators.pattern(/^\d{0,3}(\.\d{0,2})?$/)]],
                    percIncrementoGiovane: [(this.getIstruttoriaCorrente().percIncrementoGiovane * 100).toFixed(2),
                    [Validators.required, Validators.pattern(/^-?\d{0,3}(\.?\d{0,2})$/)]],
                    limiteGiovane: [this.getIstruttoriaCorrente().limiteGiovane, +[Validators.required, Validators.pattern(/^-?\d{0,3}(\.?\d{0,4})$/)]],
                    percRiduzioneLineare1: [(this.getIstruttoriaCorrente().percRiduzioneLineare1 * 100).toFixed(2),
                    [Validators.required, Validators.pattern(/^-?\d{0,3}(\.?\d{0,2})$/)]],
                    percRiduzioneLineare2: [(this.getIstruttoriaCorrente().percRiduzioneLineare2 * 100).toFixed(2),
                    [Validators.required, Validators.pattern(/^-?\d{0,3}\.?(\d{0,2})$/)]],
                    percRiduzioneLineare3: [(this.getIstruttoriaCorrente().percRiduzioneLineare3 * 100).toFixed(2),
                    [Validators.required, Validators.pattern(/^-?\d{0,3}\.?(\d{0,2})$/)]],
                    percRiduzioneTitoli: [(this.getIstruttoriaCorrente().percRiduzioneTitoli * 100).toFixed(2),
                    [Validators.required, Validators.pattern(/^-?\d{0,3}\.?(\d{0,2})$/)]],
                    percDisciplinaFinanziaria: [(this.getIstruttoriaCorrente().percDisciplinaFinanziaria * 100).toFixed(6),
                    [Validators.required, Validators.pattern(/^-?\d{0,5}(\.?\d{0,6})$/)]],
                    dtRicevibilita: [new Date(this.getIstruttoriaCorrente().dtRicevibilita), Validators.required],
                    dtScadenzaDomande: [new Date(this.getIstruttoriaCorrente().dtScadenzaDomande), Validators.required],
                    dtScadenzaDomandeRitardo: [new Date(this.getIstruttoriaCorrente().dtScadenzaDomandeRitardo), Validators.required],
                    dtScadenzaDomandeRitiroparz: [new Date(this.getIstruttoriaCorrente().dtScadenzaDomandeRitiroparz), Validators.required],
                    percPagamento: [(this.getIstruttoriaCorrente().percPagamento * 100).toFixed(2),
                    [Validators.required, Validators.pattern(/^-?\d{0,3}\.?(\d{0,2})$/)]],

                });
            }
        });

    }
    annullaModifica(): void {
        this.editable = false;
    }

    salva(): void {
        const idIstruttoria = this.getIstruttoriaCorrente().id;
        const formV = this.updateForm.getRawValue();
        const istruttoriaEdit = new IstruttoriaEdit((this.fixDati(formV.percIncrementoGreening))
            , this.fixDati(formV.percIncrementoGiovane),
            // tslint:disable-next-line:max-line-length
            Number(formV.limiteGiovane), this.fixDati(formV.percRiduzioneLineare1), this.fixDati(formV.percRiduzioneLineare2), this.fixDati(formV.percRiduzioneLineare3), this.fixDati(formV.percRiduzioneTitoli), this.fixDati(formV.percPagamento), Number(formV.percDisciplinaFinanziaria) / 100,
            formV.dtRicevibilita, formV.dtScadenzaDomande, formV.dtScadenzaDomandeRitardo, formV.dtScadenzaDomandeRitiroparz);
        // tslint:disable-next-line:max-line-length
        if (formV.percDisciplinaFinanziaria === '' || formV.limiteGiovane === '' || formV.percRiduzioneLineare1 === '' || formV.percRiduzioneLineare2 === '' || formV.percRiduzioneLineare3 === '' || formV.percRiduzioneTitoli === '' || formV.dtRicevibilita === '' || formV.dtScadenzaDomande === '' || formV.dtScadenzaDomandeRitardo === ''
            || formV.dtScadenzaDomandeRitiroparz === '' || formV.percPagamento === '') {
            this.confirmationService.confirm({
                message: 'Attenzione! I valori inseriti non risultano corretti.',
                acceptLabel: 'Annulla',
                rejectVisible: false,
                accept: () => {
                    this.editable = true;
                }
            });
        } else {
            this.istruttoriaService.putIstruttoria(idIstruttoria, istruttoriaEdit).subscribe((dati) => {
                this.aggiornaDomandeCounters.emit(idIstruttoria);
                this.editable = false;
            });
        }
    }

    //Metodo di utilità per gestire il corretto troncamento delle cifre decimali durante la /100 per le variabili del cruscotto di istruttoria
    fixDati(valore: any): number {
        return Number(Number((valore) / 100).toFixed(4));
    }

}
