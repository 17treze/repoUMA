import { TranslateService } from '@ngx-translate/core';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
import { Component, EventEmitter, Input, Output, OnInit, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SelectItem, MessageService } from 'primeng/api';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { CalendarService } from 'src/app/a4g-common/services/calendar.service';
import { AnagraficaFascicoloService } from '../creazione-fascicolo/anagrafica-fascicolo.service';
import { PersonaFisicaConCaricaDto } from '../creazione-fascicolo/dto/PersonaDto';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { TipoDocumentoIdentita } from '../creazione-fascicolo/dto/FirmatarioDto';
import { EredeDto } from '../creazione-fascicolo/dto/EredeDto';
import { FascicoloDettaglioService } from '../fascicolo-dettaglio-container/fascicolo-dettaglio.service';
import { FascicoloDaCuaa } from 'src/app/a4g-common/classi/FascicoloCuaa';
import { StatoFascicoloEnum } from '../creazione-fascicolo/dto/DatiAperturaFascicoloDto';

@Component({
  selector: 'app-popup-modifica-firmatario',
  templateUrl: './popup-modifica-firmatario.component.html',
  styleUrls: ['./popup-modifica-firmatario.component.scss']
})
export class PopupModificaFirmatarioComponent implements OnInit, OnDestroy {

  @Input() public popupModificaFirmatarioOpen: boolean;
  @Input() public cuaa: string;
  @Input() public idValidazione: number;
  @Output() public saveFirmatario = new EventEmitter<string>();
  @Output() public chiudiPopup = new EventEmitter();
  public selectedItem: string;
  public firmatariList: SelectItem[];
  public firmatarioFormGroup: FormGroup;
  public fileExt = '.pdf';
  public documentoIdentita = TipoDocumentoIdentita;
  protected componentDestroyed$: Subject<boolean> = new Subject();
  protected maxSize = 2;
  private uploadHelper = new UploadHelper(this.fileExt, this.maxSize);
  private eredi: EredeDto[];
  private fascicoloCorrente: FascicoloDaCuaa;
  private firmatario: PersonaFisicaConCaricaDto;

  constructor(
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    public calendarService: CalendarService,
    private dateUtilService: DateUtilService,
    private messageService: MessageService,
    private translateService: TranslateService,
    private fascicoloDettaglioService: FascicoloDettaglioService
  ) { }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  ngOnInit() {
    this.anagraficaFascicoloService.getDocumentoIdentitaFirmatario(this.cuaa, this.idValidazione)
      .subscribe(res => {
        this.firmatarioFormGroup = new FormGroup({
          codiceFiscale: new FormControl(
            { value: (res.codiceFiscale ? res.codiceFiscale : ''), disabled: !this.canEditFirmatario() },
            [Validators.required]
          ),
          tipoDocumento: new FormControl(
            { value: (res.tipoDocumento ? res.tipoDocumento : ''), disabled: !this.canEditFirmatario() },
            [Validators.required]
          ),
          numeroDocumento: new FormControl(
            { value: (res.numeroDocumento ? res.numeroDocumento : ''), disabled: !this.canEditFirmatario() },
            [Validators.required]
          ),
          dataRilascio: new FormControl(
            { value: (res.dataRilascio ? new Date(res.dataRilascio) : ''), disabled: !this.canEditFirmatario() },
            [Validators.required]
          ),
          dataScadenza: new FormControl(
            { value: (res.dataScadenza ? new Date(res.dataScadenza) : ''), disabled: !this.canEditFirmatario() },
            [Validators.required]
          ),
          documento: new FormControl(
            res.documento ? this.uploadHelper.stringToFile(res.documento, 'Documento_identità', 'application/pdf') : null,
            [Validators.required]
          )
        });
      });
    this.calendarService.configITA();
    this.getList();
  }

  private getList() {
    this.firmatariList = [];
    // se il titolare è una persona fisica deceduta allora carico la lista eredi
    if (this.titolarePersonaFisicaDeceduta()) {
      for (const erede of this.eredi) {
        const selectItem: SelectItem = { label: `${erede.nome} ${erede.cognome}`, value: erede.cfErede };
        if (this.firmatario.codiceFiscale === selectItem.value) {
          this.selectedItem = selectItem.value;
        }
        this.firmatariList.push(selectItem);
      }
    } else { // persona giuridica o persona fisica non deceduta
      // se la lista è vuota devo prendere il rappresentante legale di AT
      this.anagraficaFascicoloService.getPossibiliRappresentantiLegali(this.cuaa).pipe(
        takeUntil(this.componentDestroyed$))
        .subscribe((possibiliRappresentatiLegali: PersonaFisicaConCaricaDto[]) => {
          if (possibiliRappresentatiLegali && possibiliRappresentatiLegali.length > 0) {
            for (const el of possibiliRappresentatiLegali) {
              const selectItem: SelectItem = { label: `${el.nome} ${el.cognome}`, value: el.codiceFiscale };
              let hasFirmatario = false;
              if (el.cariche) {
                hasFirmatario = el.cariche.some(c => c.firmatario);
              }
              if (hasFirmatario) {
                this.selectedItem = selectItem.value;
              }
              //caso di persona fisica non iscritta in parix
              if (!hasFirmatario && (el.cariche == null || el.cariche.length === 0)) {
                this.selectedItem = selectItem.value;
              }
              this.firmatariList.push(selectItem);
            }
          }
        });
    }
  }

  public onSaveFirmatario() {
    if (this.verificaValiditaDocumento()) {
      this.saveFirmatario.emit(this.firmatarioFormGroup.value);
    }
  }

  private verificaValiditaDocumento() {
    if (this.dateUtilService.isAfterDates(
      this.firmatarioFormGroup.controls.dataRilascio.value, this.firmatarioFormGroup.controls.dataScadenza.value
    )) {
      this.messageService.add(A4gMessages.getToast(
        'tst-firmatario', A4gSeverityMessage.warn, this.translateService.instant('FIRMATARIO.DATA_RILASCIO_AFTER_SCADENZA')));
      return false;
    }
    if (this.dateUtilService.isAfterDates(
      this.firmatarioFormGroup.controls.dataRilascio.value, this.dateUtilService.getToday()
    )) {
      this.messageService.add(A4gMessages.getToast(
        'tst-firmatario', A4gSeverityMessage.warn, this.translateService.instant('FIRMATARIO.DATA_RILASCIO_AFTER_TODAY')));
      return false;
    }
    if (this.dateUtilService.isBeforeDates(
      this.firmatarioFormGroup.controls.dataScadenza.value, this.dateUtilService.getToday()
    )) {
      this.messageService.add(A4gMessages.getToast(
        'tst-firmatario', A4gSeverityMessage.warn, this.translateService.instant('FIRMATARIO.DATA_SCADENZA_BEFORE_TODAY')));
      return false;
    }
    return true;
  }

  public closePopup() {
    this.chiudiPopup.emit(false);
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

  public canEditFirmatario(): boolean {
    return this.idValidazione === 0 && !(localStorage.getItem('selectedRole') === 'responsabile_fascicolo_pat');
  }

  public titolarePersonaFisicaDeceduta() {
    this.subscribeFascicolo();
    this.subscribeEredi();
    this.subscribeInformazioniFirmatario();
    return this.isPersonaFisica() &&
      (this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_CHIUSURA || (this.fascicoloCorrente.stato === StatoFascicoloEnum.CHIUSO && this.eredi && this.eredi.length > 0));
  }

  private subscribeFascicolo() {
    this.fascicoloDettaglioService.fascicoloCorrente.pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe((fascicoloCorrente: FascicoloDaCuaa) => {
      this.fascicoloCorrente = fascicoloCorrente;
    });
  }

  private subscribeEredi() {
    this.fascicoloDettaglioService.eredi.pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe((eredi: EredeDto[]) => {
      this.eredi = eredi;
    });
  }

  private isPersonaFisica(): boolean {
    return this.cuaa.length === 16;
  }

  private subscribeInformazioniFirmatario() {
    this.fascicoloDettaglioService.firmatario.pipe(
      takeUntil(this.componentDestroyed$))
      .subscribe((firmatario: PersonaFisicaConCaricaDto) => {
        this.firmatario = firmatario;
      });
  }

}
