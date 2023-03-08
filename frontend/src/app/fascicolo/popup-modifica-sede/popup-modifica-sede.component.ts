import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { FascicoloDaCuaa } from 'src/app/a4g-common/classi/FascicoloCuaa';
import { CalendarService } from 'src/app/a4g-common/services/calendar.service';
import { DateSupport } from 'src/app/a4g-common/utility/date-support';
import { Localization } from 'src/app/a4g-common/utility/localization';
import { AnagraficaFascicoloService } from '../creazione-fascicolo/anagrafica-fascicolo.service';
import { DatiCAA, SportelloCAA } from '../creazione-fascicolo/dto/DatiSportelloCAA';
import { FascicoloService } from '../fascicolo.service';

@Component({
  selector: 'app-popup-modifica-sede',
  templateUrl: './popup-modifica-sede.component.html',
  styleUrls: ['./popup-modifica-sede.component.scss']
})
export class PopupModificaSedeComponent implements OnInit {
  @Input() popupVisibile: boolean;
  @Input() fascicolo: FascicoloDaCuaa;
  @Output() public chiudiPopup = new EventEmitter();

  public modificaSedeForm: FormGroup;
  public caa: DatiCAA;
  public sportelloOptions: SportelloCAA[];
  // public language: any = Localization.itCalendar();

  constructor(
    private fb: FormBuilder,
    private fascicoloService: FascicoloService,
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private messages: MessageService,
    public calendarService: CalendarService) { }

  ngOnInit() {
    this.calendarService.configITA();
    this.anagraficaFascicoloService.getDatiSportelloCAA().subscribe(caa => {
      this.caa = caa;
      this.sportelloOptions = caa.sportelli && caa.sportelli.filter((sportello: SportelloCAA) => sportello.id != this.fascicolo.mandatoDto.sportello.id);
    });
    this.setForm();
  }

  private setForm() {
    this.modificaSedeForm = this.fb.group({
      sedeSportello: [this.fascicolo.mandatoDto.sportello, Validators.required],
      data: [null, Validators.required],
      motivazione: [null, Validators.required]
    });
  }

  public annulla() {
    this.popupVisibile = false;
    this.chiudiPopup.emit(false);
    this.setForm();
  }

  public conferma() {
    // PUT
    this.fascicoloService.putCambioSportello(
      this.fascicolo.cuaa,
      this.fascicolo.mandatoDto.sportello.id,
      {
        idNuovoSportello: this.modificaSedeForm.value.sedeSportello.id,
        dataCambio: DateSupport.convertToPatternDate(this.modificaSedeForm.value.data),
        motivazione: this.modificaSedeForm.value.motivazione
      }
    ).subscribe(response => {
      this.popupVisibile = false;
      this.chiudiPopup.emit(true);
      this.modificaSedeForm.reset();
      this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
    }, error => {
      console.log('Error', error);
      A4gMessages.handleError(this.messages, error, A4gMessages.ERRORE_GENERICO);
    });
  }

  public onHide() {
    this.modificaSedeForm.reset();
  }

}
