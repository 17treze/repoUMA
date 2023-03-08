import { StatoFascicoloEnum } from './../creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import { MessageService } from 'primeng/api';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Subject } from 'rxjs';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { AnagraficaFascicoloService } from '../creazione-fascicolo/anagrafica-fascicolo.service';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { AntimafiaService } from '../antimafia/antimafia.service';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'app-popup-scheda-validazione',
    templateUrl: './popup-scheda-validazione.component.html',
    styleUrls: ['./popup-scheda-validazione.component.css']
})
export class PopupSchedaValidazioneComponent implements OnInit, OnDestroy {
    @Input() public popupSchedaValidazioneOpen: boolean;
    @Input() public statoFascicolo: StatoFascicoloEnum;
    @Input() public cuaa: string;
    @Input() public idSchedaValidazione: number;
    @Input() public codiceFiscale: string;

    @Output() public chiudiPopup = new EventEmitter();
    @Output() public setValida = new EventEmitter();
    @Output() public setFirma = new EventEmitter();
    @Output() public getSchedaValidazione = new EventEmitter();
    @Output() public putSchedaValidazione = new EventEmitter();
    @Output() public schedaValidazioneChange = new EventEmitter();

    public fileExt = '.p7m, .pdf';
    public file: File;
    public statoFascicoloAllaFirmaCaa = StatoFascicoloEnum.ALLA_FIRMA_CAA;
    private maxSize = 2;
    private tipoFile = 'schedaValidazioneFile';
    protected componentDestroyed$: Subject<boolean> = new Subject();
    public verificaScelta: boolean = false;
    public verificaUploadFile: boolean = false;
    public testoPulsanteAllega: string = "";


    constructor(
        private errorService : ErrorService,
        private anagraficaFascicoloService : AnagraficaFascicoloService,
        private messageService: MessageService,
        private antimafiaService : AntimafiaService
    ) { }

    ngOnInit() { }

    ngOnDestroy(): void {
        this.componentDestroyed$.next(true);
        this.componentDestroyed$.complete();
    }

    public closePopup() {
        this.chiudiPopup.emit(false);
    }

    public onFileChange(event) {
        if (event.target.files && event.target.files.length > 0) {
            this.file = event.target.files[0];
            const uploadHelper = new UploadHelper(this.fileExt, this.maxSize);
            if (uploadHelper.isValidFileExtension(this.file)) {
                if (uploadHelper.isValidFileSize(this.file)) {
                    if(this.statoFascicoloAllaFirmaCaa == StatoFascicoloEnum.IN_VALIDAZIONE){
                        //Parte due della verifica - Da inserire il codice fiscale
                        this.antimafiaService.verificaFirma(this.file).subscribe(esito => {
                            if (esito.dettaglioFirmaDigitale.datiFirmatari.length == 0){
                                this.messageService.add(A4gMessages.getToast(
                                    'tstSchedaValidazione', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_NOT_SIGNED));
                                    this.file = null;
                            } else {
                                if (environment.verificaFirmaFascicolo){
                                    if (esito.dettaglioFirmaDigitale.datiFirmatari.length  == 1){
                                        this.messageService.add(A4gMessages.getToast(
                                            'tstSchedaValidazione', A4gSeverityMessage.error, A4gMessages.ERRORE_FIRMA_SINGOLA));
                                        this.file = null;    
                                    } else {
                                        const cfFirmatario = esito.dettaglioFirmaDigitale.datiFirmatari.find(x => x.firmatario.codiceFiscale == this.codiceFiscale);
                                        if (!cfFirmatario) {
                                            this.messageService.add(A4gMessages.getToast(
                                                'tstSchedaValidazione', A4gSeverityMessage.error, A4gMessages.ERRORE_CONTROLLO_FIRMAMULTIPLA));
                                            this.file = null;

                                        } else {
                                            this.verificaUploadFile = true;
                                        }
                                }
                                }else {
                                    this.verificaUploadFile = true;
                                }    
                            }                                
                            }
                            );

                    }else{ //Prima casistica in cui firma il CAA
                        this.antimafiaService.verificaFirma(this.file).subscribe(esito => {
                            if (esito.dettaglioFirmaDigitale.datiFirmatari.length != 1) {
                                this.messageService.add(A4gMessages.getToast(
                                    'tstSchedaValidazione', A4gSeverityMessage.error, A4gMessages.ERRORE_CONTROLLO_FIRMASINGOLA
                                ));
                                this.file = null;
                            }
                            else{
                                this.verificaUploadFile = true;
                            }
                        });
                    }
                    
                } else {
                    this.messageService.add(A4gMessages.getToast(
                        'tstSchedaValidazione', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_SIZE
                    ));
                    this.file = null;
                }
            } else {
                this.messageService.add(A4gMessages.getToast(
                    'tstSchedaValidazione', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_EXT
                ));
                this.file = null;
            }


        }
    }

    public inviaFile() {
        this.schedaValidazioneChange.emit(this.file);
    }

    public confirm() {
        this.messageService.clear('checkSchedaValidazione');
        if (this.statoFascicoloAllaFirmaCaa == StatoFascicoloEnum.FIRMATO_CAA) {
            this.setFirma.emit(); // true
        }
        if (this.statoFascicoloAllaFirmaCaa == StatoFascicoloEnum.IN_VALIDAZIONE) {
            this.setValida.emit(); // true
        }
        this.putSchedaValidazione.emit(this.tipoFile);
    }

    public reject() {
        this.messageService.clear('checkSchedaValidazione');
    }

    public deleteFile() {
        this.file = null;
        this.verificaUploadFile = false;
    }

    public modificaScelta() {
        this.verificaScelta = true;
        if (this.statoFascicoloAllaFirmaCaa == StatoFascicoloEnum.FIRMATO_CAA) {
            this.testoPulsanteAllega = "Trasmetti al titolare"
        }
        if (this.statoFascicoloAllaFirmaCaa == StatoFascicoloEnum.IN_VALIDAZIONE) {
            this.testoPulsanteAllega = "Protocolla";
        }
    }
}
