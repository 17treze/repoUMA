import { Component, EventEmitter, Input, Output, OnInit, OnDestroy } from '@angular/core';
import { EMPTY, Subject } from 'rxjs';
import { AnagraficaFascicoloService } from '../creazione-fascicolo/anagrafica-fascicolo.service';
import { CalendarService } from 'src/app/a4g-common/services/calendar.service';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
import { catchError, takeUntil } from 'rxjs/operators';
import { HttpErrorResponse } from '@angular/common/http';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { MessageService } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-popup-completa-trasferimento-da-appag',
  templateUrl: './popup-completa-trasferimento-da-appag.component.html',
  styleUrls: ['./popup-completa-trasferimento-da-appag.component.scss']
})
export class PopupCompletaTrasferimentoDaAppagComponent implements OnInit, OnDestroy {

  @Input() public popupVisible = true;
  @Input() public cuaa: string;
  @Output() public chiudiPopupEmitter = new EventEmitter<string>();
  public dataChiusura: Date = new Date();
  public maxDate: Date = new Date();
  protected componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    public calendarService: CalendarService,
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private dateUtilService: DateUtilService,
    private messageService: MessageService,
    private translateService: TranslateService
  ) { }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  ngOnInit() { }

  public onCompletaTrasferimento() {
    if (this.dateUtilService.isAfterDates(this.dataChiusura, this.dateUtilService.getToday())) {
      this.messageService.add(
        A4gMessages.getToast('fdc-toast', A4gSeverityMessage.error, this.translateService.instant('DATA_FUTURA_NON_AMMESSA')));
      return;
    }
    this.anagraficaFascicoloService.chiudiIterTrasferimentoAltroOp(this.cuaa, this.dataChiusura)
      .pipe(
        takeUntil(this.componentDestroyed$),
        catchError((error: HttpErrorResponse) => {
          const errMsg = error.error!.message ? this.translateService.instant('EXC_APRI_FASCICOLO.' + error.error.message) : error.error;
          this.messageService.add(A4gMessages.getToast('fdc-toast', A4gSeverityMessage.error, errMsg));
          return EMPTY;
        }))
      .subscribe(() => {
        this.closePopup();
        this.messageService.add(A4gMessages.getToast(
          'fdc-toast', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
      });
  }

  public closePopup() {
    this.popupVisible = false;
    this.chiudiPopupEmitter.emit();
  }
}
