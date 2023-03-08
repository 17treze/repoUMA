import { TranslateService } from '@ngx-translate/core';
import { StatoFascicoloEnum } from './../creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import { AnagraficaFascicoloService } from './../creazione-fascicolo/anagrafica-fascicolo.service';
import { CalendarService } from './../../a4g-common/services/calendar.service';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng-lts';
import { Subject } from 'rxjs';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { FascicoloDaCuaa } from 'src/app/a4g-common/classi/FascicoloCuaa';
import { SospensioneFascicolo } from 'src/app/a4g-common/classi/Fascicolo';

@Component({
  selector: 'app-popup-sospensione-fascicolo',
  templateUrl: './popup-sospensione-fascicolo.component.html',
  styleUrls: ['./popup-sospensione-fascicolo.component.css']
})
export class PopupSospensioneFascicoloComponent implements OnInit {

  @Input() display: boolean;
  @Input() fascicoloCorrente: FascicoloDaCuaa;
  @Output() displayChange = new EventEmitter();
  @Output() sospendiFascicolo = new EventEmitter<SospensioneFascicolo>();

  public motivazione: string;
  public sospensioneFormGroup: FormGroup;

  protected componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    private messages: MessageService,
    protected router: Router,
    protected calendarService: CalendarService,
    protected anagraficaFascicoloService: AnagraficaFascicoloService,
    protected translateService: TranslateService
  ) { }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  ngOnInit() {
    this.sospensioneFormGroup = new FormGroup({
      data: new FormControl('', [Validators.required]),
      motivazione: new FormControl('', [Validators.required])
    });
    this.calendarService.configITA();
  }

  public onClose() {
    this.display = false;
    this.displayChange.emit();
  }

  public sospendi() {
    const sospensioneFascicolo = new SospensioneFascicolo();
    sospensioneFascicolo.dataSospensione = this.sospensioneFormGroup.controls.data.value;
    sospensioneFascicolo.motivazioneSospensione = this.sospensioneFormGroup.controls.motivazione.value;
    if (sospensioneFascicolo.motivazioneSospensione.trim().length > 0) {
      this.sospendiFascicolo.emit(sospensioneFascicolo);
    } else {
      this.messages.add(A4gMessages.getToast('psf-toast', A4gSeverityMessage.warn, this.getMessageInputRequiredInvalid()));
    }
  }

  public isStatoSospeso() {
    return this.fascicoloCorrente.stato === StatoFascicoloEnum.SOSPESO;
  }

  public getMessageInputRequiredInvalid() {
    if (this.isStatoSospeso()) {
      return this.translateService.instant('SOSPENSIONE.INPUT_REQUIRED_INVALID_RIMOZIONE');
    }
    return this.translateService.instant('SOSPENSIONE.INPUT_REQUIRED_INVALID_INSERIMENTO');
  }

}
