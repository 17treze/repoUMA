import { MessageService } from 'primeng-lts';
import { TranslateService } from '@ngx-translate/core';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DichiarazioneAssociativaDto, OrganizzazioneDto } from '../models/organizzazione';
import { A4gMessages, A4gSeverityMessage } from '../../../../app/shared/a4g-messages';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-dichiarazioni-associative-dialog',
  templateUrl: './dichiarazioni-associative-dialog.component.html'
})
export class DichiarazioniAssociativeDialogComponent implements OnInit {

  @Input() displayDialog: boolean;
  @Input() dichiarazione: DichiarazioneAssociativaDto;
  @Input() isNewDichiarazione: boolean;
  @Input() organizzazioniList: OrganizzazioneDto[];

  @Output() public saveDichiarazione = new EventEmitter<DichiarazioneAssociativaDto>();
  @Output() public closeDialog = new EventEmitter<boolean>();

  public dichiarazioneFormGroup: FormGroup;

  constructor(
    protected translateService: TranslateService,
    protected messageService: MessageService
  ) { }

  ngOnInit() {
    if (this.isNewDichiarazione) {
      this.dichiarazioneFormGroup = new FormGroup({
        organizzazione: new FormControl(this.organizzazioniList[0], [Validators.required]),
        dataInizioAssociazione: new FormControl('')
      });
    } else {
      this.dichiarazioneFormGroup = new FormGroup({
        dataFineAssociazione: new FormControl('')
      });
    }
  }

  public save() {
    if (this.isNewDichiarazione) {
      // nuova dichiarazione
      if (this.dichiarazioneFormGroup.invalid) {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.warn, this.translateService.instant('DICHIARAZIONE_ASSOCIATIVA.MSG_TOAST.INSERT_DATI_MANCANTI')
        ));
        return;
      }
      this.dichiarazione.organizzazione = new OrganizzazioneDto();
      this.dichiarazione.organizzazione = this.dichiarazioneFormGroup.controls.organizzazione.value;
      if (this.dichiarazioneFormGroup.controls.dataInizioAssociazione.value) {
        this.dichiarazione.dataInizioAssociazione = this.setDate(new Date(this.dichiarazioneFormGroup.controls.dataInizioAssociazione.value));
      }
    } else if (this.dichiarazioneFormGroup.controls.dataFineAssociazione.value) {
      // chiusura dichiarazione
      this.dichiarazione.dataFineAssociazione = this.setDate(new Date(this.dichiarazioneFormGroup.controls.dataFineAssociazione.value));
    }
    const event: any = { dichiarazione: this.dichiarazione, isNewDichiarazione: this.isNewDichiarazione };
    this.saveDichiarazione.emit(event);
  }

  public closeDialogEvent() {
    this.closeDialog.emit(false);
  }

  public getLabelButton() {
    if (this.isNewDichiarazione) {
      return this.translateService.instant('DICHIARAZIONE_ASSOCIATIVA.BUTTON.SAVE');
    } else {
      return this.translateService.instant('DICHIARAZIONE_ASSOCIATIVA.BUTTON.CHIUSURA');
    }
  }

  public getHeaderDialog() {
    if (this.isNewDichiarazione) {
      return this.translateService.instant('DICHIARAZIONE_ASSOCIATIVA.NEW_DICHIARAZIONE');
    } else {
      return this.translateService.instant('DICHIARAZIONE_ASSOCIATIVA.CLOSE_ISCRIZIONE');
    }
  }

  private setDate(date) {
    const timeZoneDifference = (date.getTimezoneOffset() / 60) * -1;
    date.setTime(date.getTime() + (timeZoneDifference * 60) * 60 * 1000);
    return date.toISOString();
  }

}
