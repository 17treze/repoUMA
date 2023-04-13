import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { MessageService } from 'primeng-lts';
import { Subject } from 'rxjs';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { UploadHelper } from 'src/app/shared/utilities/uploadHelper';
import { StatoFascicoloEnum } from '../models/FascicoloCuaa';

@Component({
  selector: 'app-popup-scheda-validazione-in-proprio',
  templateUrl: './popup-scheda-validazione-in-proprio.component.html'
})
export class PopupSchedaValidazioneInProprioComponent implements OnInit, OnDestroy {

  @Input() public popupSchedaValidazioneInProprioOpen: boolean;
  @Input() public statoFascicolo: StatoFascicoloEnum;

  @Output() public chiudiPopup = new EventEmitter();
  @Output() public getSchedaValidazioneDetenzioneAutonoma = new EventEmitter();
  @Output() public putSchedaValidazioneDetenzioneAutonoma = new EventEmitter();
  @Output() public schedaValidazioneInProprioChange = new EventEmitter();

  public fileExt = '.p7m, .pdf';
  public file: File;
  public statoFascicoloAllaFirmaAzienda = StatoFascicoloEnum.ALLA_FIRMA_AZIENDA;
  private maxSize = 2;
  private tipoFile = 'schedaValidazioneFile';
  protected componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    private messageService: MessageService
  ) { }

  ngOnInit() { }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  public closePopup() {
    this.chiudiPopup.emit(false);
  }

  public downloadSchedaValidazione() {
    this.getSchedaValidazioneDetenzioneAutonoma.emit();
  }

  public uploadModulo() {
    this.messageService.add(
      A4gMessages.getToast('checkSchedaValidazione', A4gSeverityMessage.warn, A4gMessages.CONFERMA_FASCICOLO_VALIDATO));
  }

  public onFileChange(event) {
    if (event.target.files && event.target.files.length > 0) {
      this.file = event.target.files[0];
      const uploadHelper = new UploadHelper(this.fileExt, this.maxSize);
      if (uploadHelper.isValidFileExtension(this.file)) {
        if (uploadHelper.isValidFileSize(this.file)) {
          console.log("schedaValidazioneInProprioChange.emit");
          this.schedaValidazioneInProprioChange.emit(this.file);
        } else {
          this.messageService.add(A4gMessages.getToast(
            'tstSchedaValidazione', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_SIZE
          ));
        }
      } else {
        this.messageService.add(A4gMessages.getToast(
          'tstSchedaValidazione', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_EXT
        ));
      }
    }
  }

  public confirm() {
    this.messageService.clear('checkSchedaValidazione');
    this.putSchedaValidazioneDetenzioneAutonoma.emit(this.tipoFile);
  }

  public reject() {
    this.messageService.clear('checkSchedaValidazione');
  }
}
