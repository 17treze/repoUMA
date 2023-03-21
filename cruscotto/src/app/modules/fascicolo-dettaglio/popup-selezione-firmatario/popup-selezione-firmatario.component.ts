import { TipoDocumentoIdentita } from './../models/FirmatarioDto';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { MessageService, SelectItem } from 'primeng-lts';
import { EMPTY, Subject } from 'rxjs';
import { catchError, takeUntil } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { PersonaFisicaConCaricaDto } from 'src/app/shared/models/persona';
import { AnagraficaFascicoloService } from 'src/app/shared/services/anagrafica-fascicolo.service';
import { CalendarService } from 'src/app/shared/services/calendar.service';
import { DateService } from 'src/app/shared/utilities/date.service';
import { UploadHelper } from 'src/app/shared/utilities/uploadHelper';
import { FascicoloDaCuaa } from '../models/FascicoloCuaa';
import { validaInput } from '../../../shared/validazione/validaInput';

@Component({
  selector: 'app-popup-selezione-firmatario',
  templateUrl: './popup-selezione-firmatario.component.html',
  styleUrls: ['./popup-selezione-firmatario.component.scss']
})
export class PopupSelezioneFirmatarioComponent implements OnInit, OnDestroy {
  protected componentDestroyed$: Subject<boolean> = new Subject();
  @Output() public chiudiPopup = new EventEmitter();
  @Input() public popupSelezioneFirmatarioOpen: boolean;
  @Input() public fascicoloCorrente: FascicoloDaCuaa;
  @Input() public currentFirmatario: PersonaFisicaConCaricaDto;
  @Input() public idValidazione: number;

  public selectedItem: string;
  public possibiliFirmatariSelectItem: SelectItem[] = [];
  private listaPossibiliFirmatari: PersonaFisicaConCaricaDto[];
  public firmatarioFormGroup: FormGroup;
  public documentoIdentita = TipoDocumentoIdentita;
  public fileExt = '.pdf';
  private maxSize = 2;
  private uploadHelper = new UploadHelper(this.fileExt, this.maxSize);

  constructor(
    private messageService: MessageService,
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    public calendarService: CalendarService,
    private translateService: TranslateService,
    private dateService: DateService) {
  }

  ngOnInit() {
    this.anagraficaFascicoloService.getDocumentoIdentitaFirmatario(this.fascicoloCorrente.cuaa, this.idValidazione)
      .subscribe(res => {
        this.firmatarioFormGroup = new FormGroup({
          codiceFiscale: new FormControl({ value: res.codiceFiscale ? res.codiceFiscale : '', disabled: !this.isFascicoloAttuale() }, [Validators.required]),
          tipoDocumento: new FormControl({ value: res.tipoDocumento ? res.tipoDocumento : '', disabled: !this.isFascicoloAttuale() }, [Validators.required]),
          numeroDocumento: new FormControl({ value: res.numeroDocumento ? res.numeroDocumento : '', disabled: !this.isFascicoloAttuale() }, [Validators.required]),
          dataRilascio: new FormControl({ value: res.dataRilascio ? new Date(res.dataRilascio) : '', disabled: !this.isFascicoloAttuale() }, [Validators.required]),
          dataScadenza: new FormControl({ value: res.dataScadenza ? new Date(res.dataScadenza) : '', disabled: !this.isFascicoloAttuale() }, [Validators.required]),
          documento: new FormControl(res.documento ? this.uploadHelper.stringToFile(res.documento, 'Documento_identità', 'application/pdf') : null, [Validators.required])
        });
        console.log(this.firmatarioFormGroup);
      });

    this.calendarService.configITA();
    this.getList();
  }

  private getList() {
    this.possibiliFirmatariSelectItem = [];
    this.anagraficaFascicoloService.getPossibiliRappresentantiLegali(this.fascicoloCorrente.cuaa)
      .pipe(takeUntil(this.componentDestroyed$))
      .subscribe(resp => {
        this.listaPossibiliFirmatari = resp;
        if (resp) {
          resp.forEach(el => {
            const selectItem: SelectItem = { label: `${el.nome} ${el.cognome}`, value: el.codiceFiscale };
            let hasFirmatario = false;
            if (el.cariche) {
              hasFirmatario = el.cariche.some(c => c.firmatario);
            }
            if (hasFirmatario) {
              this.selectedItem = selectItem.value;
            }
            //caso di persona fisica non iscritta in parix
            if (!hasFirmatario && ( el.cariche == null || el.cariche.length === 0)) {
              this.selectedItem = selectItem.value;
            }
            this.possibiliFirmatariSelectItem.push(selectItem);
          });
        }
      }, err => {
        this.messageService.add(A4gMessages.getToast('tst-firmatario', A4gSeverityMessage.error, err.error));
      });
  }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  private handleErrorOnPutFirmatario = (err: HttpErrorResponse) => {
    let messaggioErrore: string;
    if (err.error.status === 400) {
      messaggioErrore = this.translateService.instant('FIRMATARIO.IBAN_NON_VALIDO');
    } else {
      messaggioErrore = this.translateService.instant('FIRMATARIO.SALVATAGGIO_KO');
    }
    this.messageService.add(A4gMessages.getToast(
      'tst-firmatario', A4gSeverityMessage.error, messaggioErrore));
    return EMPTY;
  }

  public conferma() {
    if (this.verificaValiditaDocumento()) {
      const newCodiceFiscale: string = this.firmatarioFormGroup.get('codiceFiscale').value;
      const newFirmatario: PersonaFisicaConCaricaDto = this.listaPossibiliFirmatari.find(
        el => el.codiceFiscale === newCodiceFiscale);
      this.anagraficaFascicoloService.postSalvaFirmatario(this.firmatarioFormGroup.value, this.fascicoloCorrente.cuaa).pipe(
        takeUntil(this.componentDestroyed$),
        catchError(this.handleErrorOnPutFirmatario)
      ).subscribe(
        () => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success,
              this.translateService.instant('FIRMATARIO.SALVATAGGIO_OK')));
          this.closePopup(newFirmatario);
        });
    }
  }

  public closePopup(selectedFirmatario: PersonaFisicaConCaricaDto) {
    this.chiudiPopup.emit(selectedFirmatario);
  }

  private verificaValiditaDocumento() {
    if (this.dateService.isAfterDates(
      this.firmatarioFormGroup.controls.dataRilascio.value, this.firmatarioFormGroup.controls.dataScadenza.value
    )) {
      this.messageService.add(A4gMessages.getToast(
        'tst-firmatario', A4gSeverityMessage.warn, this.translateService.instant('FIRMATARIO.DATA_RILASCIO_AFTER_SCADENZA')));
      return false;
    }
    if (this.dateService.isAfterDates(
      this.firmatarioFormGroup.controls.dataRilascio.value, this.dateService.getToday()
    )) {
      this.messageService.add(A4gMessages.getToast(
        'tst-firmatario', A4gSeverityMessage.warn, this.translateService.instant('FIRMATARIO.DATA_RILASCIO_AFTER_TODAY')));
      return false;
    }
    if (this.dateService.isBeforeDates(
      this.firmatarioFormGroup.controls.dataScadenza.value, this.dateService.getToday()
    )) {
      this.messageService.add(A4gMessages.getToast(
        'tst-firmatario', A4gSeverityMessage.warn, this.translateService.instant('FIRMATARIO.DATA_SCADENZA_BEFORE_TODAY')));
      return false;
    }
    return true;
  }

  public uploadFile(tipoFile: string) {
    this.openWindowsSelectFile(tipoFile);
  }

  private openWindowsSelectFile(tipoFile) {
    document.getElementById(tipoFile).click();
  }

  public onFileChange(event) {
    if (event.target.files && event.target.files.length > 0) {
      const file: File = event.target.files[0];
      const maxSize = 2;
      const uploadHelper = new UploadHelper(this.fileExt, maxSize);
      if (uploadHelper.isValidFileExtension(file)) {
        if (uploadHelper.isValidFileSize(file)) {
          this.firmatarioFormGroup.get('documento').setValue(file);
          this.messageService.add(A4gMessages.getToast('tst-firmatario', A4gSeverityMessage.success, A4gMessages.UPLOAD_FILE_OK));
        } else {
          this.messageService.add(A4gMessages.getToast('tst-firmatario', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_SIZE));
        }
      } else {
        this.messageService.add(A4gMessages.getToast('tst-firmatario', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_EXT));
      }
    }
  }

  public downloadFile() {
    const fileURL = URL.createObjectURL(this.firmatarioFormGroup.controls.documento.value);
    window.open(fileURL);
  }

  public onChangeFirmatario() {
    this.firmatarioFormGroup = new FormGroup({
      codiceFiscale: new FormControl(this.selectedItem, [Validators.required]),
      tipoDocumento: new FormControl('', [Validators.required]),
      numeroDocumento: new FormControl('', [Validators.required]),
      dataRilascio: new FormControl('', [Validators.required]),
      dataScadenza: new FormControl('', [Validators.required]),
      documento: new FormControl(null, [Validators.required])
    });
  }

  public isFascicoloAttuale(): boolean {
    return this.idValidazione === 0;
  }

}
