import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng-lts';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from '../../shared/a4g-messages';
import { AnagraficaFascicoloService } from '../../shared/services/anagrafica-fascicolo.service';
import { DatiAperturaFascicoloDto } from '../fascicolo-dettaglio/models/anagrafica-fascicolo';
import { TIPO_CREAZIONE_UTENTE_FASCICOLO } from '../register/models/register.models';

@Component({
  selector: 'app-crea-fascicolo',
  templateUrl: './crea-fascicolo.component.html',
  styleUrls: ['./crea-fascicolo.component.scss']
})
export class CreaFascicoloComponent implements OnInit, OnDestroy {
  public cuaa: string = "";
  public datiFascicolo: DatiAperturaFascicoloDto = undefined;
  public filtersFormGroup: FormGroup;
  public indirizzoAzienda: string;
  public indirizzoLR: string;
  protected componentDestroyed$: Subject<boolean> = new Subject();
  private ctx: TIPO_CREAZIONE_UTENTE_FASCICOLO;

  @Output() public datiFascicoloOutput = new EventEmitter();

  constructor(
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private messageService: MessageService,
    private translateService: TranslateService,
    protected route: ActivatedRoute,
    private router: Router) { }

  ngOnInit() {
    this.getCtx();
    this.route.params.pipe(
      takeUntil(this.componentDestroyed$)).subscribe(urlParams => {
        this.cuaa = urlParams['cuaa'];
        if (this.cuaa) {
          this.loadData(this.cuaa);
        }
      });
    this.filtersFormGroup = new FormGroup({
      'cuaaFormControl': new FormControl(this.cuaa, [Validators.required]),
    });
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  private getCtx() {
    if (this.router.url.indexOf('/register-crea-fascicolo') > -1) {
      this.ctx = TIPO_CREAZIONE_UTENTE_FASCICOLO.CREA_UTENTE_FASCICOLO;
    } else {
      this.ctx = TIPO_CREAZIONE_UTENTE_FASCICOLO.CREA_UTENTE;
    }
  }

  private formatIndirizzoAzienda(datiAperturaFascicoloDto: DatiAperturaFascicoloDto) {
    if (datiAperturaFascicoloDto.ubicazioneDitta && datiAperturaFascicoloDto.ubicazioneDitta.toponimo) {
      return datiAperturaFascicoloDto.ubicazioneDitta.toponimo;
    }
    return "";
  }

  private formatIndirizzoLegaleRappresentante(datiAperturaFascicoloDto: DatiAperturaFascicoloDto) {
    if (datiAperturaFascicoloDto.domicilioFiscaleRappresentante
      && datiAperturaFascicoloDto.domicilioFiscaleRappresentante.toponimo) {
      return datiAperturaFascicoloDto.domicilioFiscaleRappresentante.toponimo;
    }
    return "";
  }

  public search() {
    const codiceFiscale: string = this.filtersFormGroup.get("cuaaFormControl").value;
    if (this.ctx === TIPO_CREAZIONE_UTENTE_FASCICOLO.CREA_UTENTE) {
      if (this.cuaa) {
        this.router.navigate([codiceFiscale], { relativeTo: this.route.parent });
      } else {
        this.router.navigate([codiceFiscale], { relativeTo: this.route });
      }
    } else {
      this.loadData(codiceFiscale);
    }
  }

  private loadData(codiceFiscale: string) {
    this.anagraficaFascicoloService.getVerificaAperturaFascicoloDetenzioneAutonoma(codiceFiscale)
      .pipe(takeUntil(this.componentDestroyed$))
      .subscribe(response => {
        this.datiFascicolo = response;
        this.indirizzoAzienda = this.formatIndirizzoLegaleRappresentante(response);
        this.indirizzoLR = this.formatIndirizzoAzienda(response);
        this.datiFascicoloOutput.emit(this.datiFascicolo);
      }, error => {
        if (error.error.message && this.translateService.instant(
          'EXC_APRI_FASCICOLO.' + error.error.message) != 'EXC_APRI_FASCICOLO.' + error.error.message) {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error,
            this.translateService.instant('EXC_APRI_FASCICOLO.' + error.error.message)));
        } else {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        }
      });
  }

  public apri() {
    const codiceFiscale: string = this.filtersFormGroup.get("cuaaFormControl").value;
    console.log('Apertura fascicolo: ' + codiceFiscale);
    this.anagraficaFascicoloService.postUrlAperturaFascicoloDetenzioneAutonoma(codiceFiscale)
      .subscribe(
        resp => {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.success, this.translateService.instant('common.creazione_fascicolo_ok')
          ));
          this.router.navigate(['../fascicolo-dettaglio/' + codiceFiscale], { relativeTo: this.route.parent });
        });
  }

  public isCtxCreaUtente() {
    return this.ctx === TIPO_CREAZIONE_UTENTE_FASCICOLO.CREA_UTENTE;
  }

}
