import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { Router } from '@angular/router';
import * as FileSaver from 'file-saver';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { MessageService } from 'primeng/api';
import { DatiAperturaFascicoloDto } from '../../creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import { MandatoDTO } from '../../shared/fascicolo.model';
import { formatDate } from '@angular/common';
import { AnagraficaFascicoloService } from '../../creazione-fascicolo/anagrafica-fascicolo.service';
import { TranslateService } from '@ngx-translate/core';
import { FascicoloCorrente } from '../../fascicoloCorrente';
import { MandatoService } from '../../mandato.service';
import { CentroAssistenzaAgricolaService } from '../../centro-assistenza-agricola.service';
import { DatiCAA, SportelloCAA } from '../../creazione-fascicolo/dto/DatiSportelloCAA';

@Component({
  selector: 'app-revoca-immediata-dialog',
  templateUrl: './revoca-immediata-dialog.component.html',
  styleUrls: ['./revoca-immediata-dialog.component.css']
})
export class RevocaImmediataDialogComponent implements OnInit {
  public display: boolean;
  public testoMail = '';
  public causaRichiesta: string;
  public displayApprovazioneDialog: boolean;
  public fileExt = '.p7m, .pdf';
  public datiRevocaImmediata: any;
  public revocaFile: File;
  public sportelloList: SportelloCAA[] = [];
  public selectedSportello: SportelloCAA;

  private mandatoAttuale: any;
  private toLoadRevocaFile: string;

  protected maxSize = 2;

  @Output() displayChange = new EventEmitter();
  @Input() datiFascicolo: DatiAperturaFascicoloDto;
  @Input() datiMandato: MandatoDTO;

  constructor(
    protected mandatoService: MandatoService,
    protected messageService: MessageService,
    protected router: Router,
    protected anagraficaFascicoloService: AnagraficaFascicoloService,
    protected translateService: TranslateService,
    protected fascicoloCorrente: FascicoloCorrente,
    protected caaService: CentroAssistenzaAgricolaService
  ) {
    this.displayApprovazioneDialog = false;
  }

  ngOnInit() {
  }

  private getSportelliList() {
    this.caaService.getListaCaaConSportelli()
      .subscribe(
        resp => {
          if (resp)
            this.filtraSportelli(resp);
        },
        err => {
          this.messageService.add(A4gMessages.getToast('tst-dialog', A4gSeverityMessage.error, A4gMessages.errorGetListSportelli));
        }
      )
  }

  private filtraSportelli(datiCaa: DatiCAA[]) {
    this.sportelloList = datiCaa
      .filter(pilot => pilot.codiceFiscale != this.mandatoAttuale.codiceFiscale)
      .map(jedi => jedi.sportelli)
      .reduce((acc, val) => acc.concat(val), []);
  }

  public onOpen(datiMandato) {
    this.display = true;
    this.displayChange.emit(true);
    this.causaRichiesta = null;
    this.selectedSportello = null;
    this.mandatoAttuale = datiMandato;
    this.getSportelliList();
  }

  public onClose() {
    this.display = false;
    this.displayChange.emit(false);
  }

  public revocaMandato() {
    this.messageService.add(A4gMessages.getToast('checkRichiestaRevocaImmediata', A4gSeverityMessage.warn, A4gMessages.checkVerificaPresenzaRevocaOrdinariaDetail));
  }

  public confirmRichiestaRevoca() {
    this.messageService.clear('checkRichiestaRevocaImmediata');

    if (this.selectedSportello) {
      if (this.revocaFile) {
        const datiRevoca = this.getDatiRevoca();
        if (datiRevoca) {
          this.postRevoca(datiRevoca);
        }
      } else {
        this.messageService.add(A4gMessages.getToast('tst-dialog', A4gSeverityMessage.warn, A4gMessages.notFoundRevocaFile));
      }
    } else {
      this.messageService.add(A4gMessages.getToast('tst-dialog', A4gSeverityMessage.warn, A4gMessages.sportelloNonSelezionato));
    }
  }

  public rejectRichiestaRevoca() {
    this.messageService.clear('checkRichiestaRevocaImmediata');
  }

  private getDatiRevoca() {
    return {
      cuaa: this.fascicoloCorrente.fascicolo.cuaa,
      codiceFiscaleRappresentante: this.datiFascicolo.datiAnagraficiRappresentante.codiceFiscale,
      revocaFile: this.revocaFile,
      causaRichiesta: this.causaRichiesta,
      sportello: this.selectedSportello
    };
  }

  private postRevoca(datiRevoca) {
    this.mandatoService.revocaImmediata(datiRevoca)
      .subscribe(
        esito => {
          if (esito) {
            this.display = false;
            this.displayChange.emit(false);
            this.messageService.add(A4gMessages.getToast('tst-dialog', A4gSeverityMessage.success, A4gMessages.successRevocaImmediata));
          }
        },
        error => {
          if (error.error.includes('RICHIESTA_REVOCA_IMMEDIATA_PRESENTE_E_DA_VALUTARE')) {
            this.messageService.add(
              A4gMessages.getToast(
                'tst-dialog',
                A4gSeverityMessage.error,
                this.translateService.instant('EXC_REVOCA_MANDATO.RICHIESTA_REVOCA_IMMEDIATA_PRESENTE_E_DA_VALUTARE')
              )
            );
          } else {
            this.messageService.add(
              A4gMessages.getToast(
                'tst-dialog',
                A4gSeverityMessage.error,
                A4gMessages.SERVIZIO_NON_DISPONIBILE
              )
            );
          }
        });
  }

  private preparaStampaRevocaImmediata() {
    if (this.datiFascicolo.datiAnagraficiRappresentante.dataNascita) {
      let newDate = new Date(this.datiFascicolo.datiAnagraficiRappresentante.dataNascita);
      if (isNaN(newDate.getTime()) == false) { this.datiFascicolo.datiAnagraficiRappresentante.dataNascita = formatDate(newDate, 'dd/MM/yyyy', 'it-IT') };
    }
    this.datiRevocaImmediata = {
      denominazioneSportello: this.datiMandato.sportello.denominazione,
      sedeSportello: this.datiMandato.sportello.comune,
      indirizzoSportello: this.datiMandato.sportello.indirizzo,
      emailPECSportello: this.datiMandato.sportello.email,
      datiAnagraficiRappresentante: this.datiFascicolo.datiAnagraficiRappresentante,
      domicilioFiscaleRappresentante: this.datiFascicolo.domicilioFiscaleRappresentante,
      comuneDitta: this.datiFascicolo.ubicazioneDitta.comune,
      provinciaDitta: this.datiFascicolo.ubicazioneDitta.provincia,
      capDitta: this.datiFascicolo.ubicazioneDitta.cap,
      indirizzoDitta: this.datiFascicolo.ubicazioneDitta.toponimo,
      denominazioneAzienda: this.datiFascicolo.denominazione,
      codiceFiscaleeAzienda: this.datiFascicolo.codiceFiscale,
      indirizzoPEC: this.datiFascicolo.datiAnagraficiRappresentante.pec
    };
  }

  private effettuaStampaRevocaImmediata() {
    const uploadHelper = new UploadHelper(this.fileExt, this.maxSize);
    this.mandatoService.getTemplateRevocaImmediata()
      .subscribe(response => {
        const template: File = uploadHelper.blobToFile(response, 'templateRevocaImmediata.docx');
        return this.mandatoService.stampaRevocaImmediata(template, this.datiRevocaImmediata)
          .subscribe(
            result => {
              FileSaver.saveAs(result, 'RevocaImmediata.pdf');
            }, err => {
              this.messageService.add(A4gMessages.getToast('tst-dialog', A4gSeverityMessage.error, A4gMessages.ERRORE_STAMPA_REVOCA));
              console.error('Errore nella stampa del modulo revoca immadiata:', err);
            }
          );
      });
  }

  public downloadModulo() {
    this.preparaStampaRevocaImmediata();
    this.effettuaStampaRevocaImmediata();
    console.log('Scarica modulo revoca immediata');
  }

  public uploadModulo(tipoFile: string) {
    this.toLoadRevocaFile = tipoFile;
    this.verificaPresenzaRevocaOrdinaria();
  }

  private openWindowsSelectFile() {
    document.getElementById(this.toLoadRevocaFile).click();
  }

  public onFileChange(event) {
    if (event.target.files && event.target.files.length > 0) {
      const file: File = event.target.files[0];
      const uploadHelper = new UploadHelper(this.fileExt, this.maxSize);
      if (uploadHelper.isValidFileExtension(file)) {
        if (uploadHelper.isValidFileSize(file)) {
          this.verificaFirmaSingola(file);
        } else {
          this.messageService.add(A4gMessages.getToast('tst-dialog', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_SIZE));
        }
      } else {
        this.messageService.add(A4gMessages.getToast('tst-dialog', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_EXT));
      }
    }
  }

  private verificaFirmaSingola(file: File) {
    this.anagraficaFascicoloService.verificaFirmaSingola(file, this.datiFascicolo.datiAnagraficiRappresentante.codiceFiscale)
      .subscribe(
        esito => {
          if (esito) {
            this.revocaFile = file;
            this.messageService.add(A4gMessages.getToast('tst-dialog', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
          }
        },
        error => {
          this.messageService.add(A4gMessages.getToast(
            'tst-dialog', A4gSeverityMessage.error, this.translateService.instant('EXC_VERIFICA_FIRMA.' + error.error.message)
          ));
        });
  }

  private verificaPresenzaRevocaOrdinaria() {
    if (this.revocaFile) {
      this.messageService.add(A4gMessages.getToast('checkPresenzaRevocaFile', A4gSeverityMessage.warn, A4gMessages.FILE_ALREADY_UPLOADED));
    } else {
      this.openWindowsSelectFile();
    }
  }

  public confirmPresenzaRevocaFile() {
    this.messageService.clear('checkPresenzaRevocaFile');
    this.openWindowsSelectFile();
  }

  public rejectPresenzaRevocaFile() {
    this.messageService.clear('checkPresenzaRevocaFile');
  }

}
