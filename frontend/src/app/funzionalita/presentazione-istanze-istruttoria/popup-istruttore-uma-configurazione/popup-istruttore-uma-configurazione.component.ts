import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ImpostazioniIstruttoreUmaVM } from 'src/app/a4g-common/classi/viewModels/ImpostazioniIstruttoreUmaVM';
import { CalendarService } from 'src/app/a4g-common/services/calendar.service';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { FormService } from 'src/app/a4g-common/services/form.service';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';

@Component({
  selector: 'app-popup-istruttore-uma-configurazione',
  templateUrl: './popup-istruttore-uma-configurazione.component.html',
  styleUrls: ['./popup-istruttore-uma-configurazione.component.scss']
})
export class PopupIstruttoreUmaConfigurazioneComponent implements OnInit {
  @Output() onCloseImpostazioniDialog = new EventEmitter();

  display: boolean;
  minDate: Date;
  maxDate: Date;
  impostazioniForm: FormGroup;
  umaMessages = UMA_MESSAGES;

  constructor(
    public calendarService: CalendarService,
    private dateUtilService: DateUtilService,
    private errorService: ErrorService,
    private formService: FormService
  ) { }

  ngOnInit() {
    this.calendarService.configITA();
    this.minDate = this.setMinDate();
    this.maxDate = this.setMaxDate();
    this.impostazioniForm = new FormGroup({
      dataPrelievoDefault: new FormControl(null, [Validators.required])
    })
  }

  open(impostazioni?: ImpostazioniIstruttoreUmaVM) {
    this.display = true;
    if (!impostazioni) {
      this.setDataDefaultPrelievi();
    } else {
      this.impostazioniForm.get('dataPrelievoDefault').setValue(impostazioni.dataDefaultPrelievo);
    }
  }

  annulla() {
    this.display = false;
  }

  salva() {
    if (this.valida()) {
      const impostazioni: ImpostazioniIstruttoreUmaVM = {} as ImpostazioniIstruttoreUmaVM;
      impostazioni.dataDefaultPrelievo = this.dateUtilService.toDateWithNoOffset(new Date(this.impostazioniForm.get('dataPrelievoDefault').value));
      this.onCloseImpostazioniDialog.emit(impostazioni);
      this.display = false;
    }
  }

  private setDataDefaultPrelievi() {
    const defaultDate = this.dateUtilService.toDateFrom(1, 11, this.dateUtilService.getCurrentYear());
    this.impostazioniForm.get('dataPrelievoDefault').setValue(defaultDate);
  }

  private setMinDate(): Date {
    return this.dateUtilService.toDateFrom(1, 1, this.dateUtilService.getCurrentYear());
  }

  private setMaxDate(): Date {
    return this.dateUtilService.toDateFrom(31, 12, this.dateUtilService.getCurrentYear());
  }

  private valida() {
    this.formService.validateForm(this.impostazioniForm);
    if (!this.impostazioniForm.valid) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.datiNonValidi, 'tst-impostazioni');
      return false;
    }
    return true;
  }
}
