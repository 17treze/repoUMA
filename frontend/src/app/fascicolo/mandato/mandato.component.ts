import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng/api';
import { EMPTY, forkJoin, Subject } from 'rxjs';
import { catchError, switchMap, takeUntil } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from '../../a4g-common/a4g-messages';
import { DatiAperturaFascicoloDto } from '../creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import { FascicoloService } from '../fascicolo.service';
import { FascicoloCorrente } from '../fascicoloCorrente';
import { RevocaImmediataDialogComponent } from './revoca-immediata-dialog/revoca-immediata-dialog.component';
import { MandatoService } from '../mandato.service';
import * as moment from 'moment';
import { Labels } from 'src/app/app.labels';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-mandato',
  templateUrl: './mandato.component.html',
  styleUrls: ['./mandato.component.css']
})
export class MandatoComponent implements OnInit {

  public dtAperturaRevocaImmediata: Date = environment.dtAperturaRevocaImmediata;
  public dtChiusuraRevocaImmediata: Date = environment.dtChiusuraRevocaImmediata;
  public datiFascicolo: DatiAperturaFascicoloDto;
  public datiMandato: any;
  public dataSottoscrizione;
  public dataInizioDecorrenza;
  public dataScadenza: string | number;
  public indirizzoAzienda: string;
  public indirizzoLR: string;
  public labels = Labels;
  private cuaa: string;
  private idValidazione: number = undefined;

  @ViewChild('revocaImmediataDialog', { static: true })
  public revocaImmediataDialog: RevocaImmediataDialogComponent;

  protected componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    protected router: Router,
    protected route: ActivatedRoute,
    protected messageService: MessageService,
    protected translateService: TranslateService,
    protected fascicoloService: FascicoloService,
    protected mandatoService: MandatoService,
    private fascicoloCorrente: FascicoloCorrente
  ) { }

  ngOnInit() {
    this.cuaa = this.fascicoloCorrente.fascicolo.cuaa;
    this.getDatiFascicoloEMandato();
    const paramIdVal = this.route.snapshot.queryParams['id-validazione'];
    if (paramIdVal) {
      this.idValidazione = Number.parseInt(paramIdVal);
    } else {
      this.idValidazione = 0;
    }
  }

  private getDatiFascicoloEMandato() {
    const mandatiByCuaa$ = this.mandatoService.fascicoloPerMandato(this.cuaa, this.idValidazione);
    const fascicoloByCuaa$ = this.mandatoService.getMandati(this.cuaa, this.idValidazione);
    forkJoin(mandatiByCuaa$, fascicoloByCuaa$).pipe(
      catchError(e => {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDatiFascicoloMandato));
        return undefined;
      })
    ).subscribe(resp => {
      this.datiFascicolo = { ...resp[0] };
      this.datiMandato = this.getMandatoAttuale(resp[1]);
      this.setIndirizzoAzienda();
      this.setIndirizzoLegaleRappresentante();
    });
  }

  private getMandatoAttuale(mandatiList) {
    const dataAttuale = moment(moment.now());
    for (const element of mandatiList) {
      const dataInizio = moment(element.dataInizio);
      const dataFine = moment(element.dataFine);
      if (dataAttuale.isSameOrAfter(dataInizio) && (!element.dataFine || dataAttuale.isSameOrBefore(dataFine))) {
        return element;
      }
    }
  }

  private setIndirizzoAzienda() {
    if (this.datiFascicolo.ubicazioneDitta && this.datiFascicolo.ubicazioneDitta.toponimo) {
      this.indirizzoAzienda = this.datiFascicolo.ubicazioneDitta.toponimo;
    }
  }

  private setIndirizzoLegaleRappresentante() {
    if (this.datiFascicolo.domicilioFiscaleRappresentante
      && this.datiFascicolo.domicilioFiscaleRappresentante.toponimo) {
      this.indirizzoLR = this.datiFascicolo.domicilioFiscaleRappresentante.toponimo;
    }
  }

  public isRevocabileToday() {
    const dataAttuale = moment(moment.now());
    const dtAperturaRevocaImmediata = moment(this.dtAperturaRevocaImmediata);
    const dtChiusuraRevocaImmediata = moment(this.dtChiusuraRevocaImmediata);
    if (dataAttuale.isSameOrAfter(dtAperturaRevocaImmediata) && dataAttuale.isSameOrBefore(dtChiusuraRevocaImmediata)) {
      return true;
    }
    return false;
  }

  public revocaImmediata() {
    let checkVerificaInserimento;
    this.mandatoService.verificaInserimentoRevocaImmediata(this.cuaa)
      .pipe(
        takeUntil(this.componentDestroyed$),
        catchError(e => {
          switch (e.error.message) {
            case 'DATA_NON_VALIDA':
              this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant("EXC_REVOCA_MANDATO.FINESTRA_TEMPORALE_NON_VALIDA")));
              break;
            case 'MANDATO_MANCANTE':
              this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant("EXC_REVOCA_MANDATO.MANDATO_MANCANTE")));
              break;
            case 'RICHIESTA_REVOCA_IMMEDIATA_PRESENTE_E_DA_VALUTARE':
              this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant("EXC_REVOCA_MANDATO.RICHIESTA_REVOCA_IMMEDIATA_PRESENTE_E_DA_VALUTARE")));
              break;
            default:
              this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreVerificaInserimentoRevocaImmediata));
              break;
          }
          return EMPTY;
        }),
        switchMap(resp => {
          checkVerificaInserimento = resp;
          return this.mandatoService.verificaPresenzaRevocaOrdinaria(this.cuaa);
        }))
      .subscribe(resp2 => {
        if (checkVerificaInserimento) {
          this.verificaPresenzaRevocaOrdinaria(resp2);
        } else {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreVerificaInserimentoRevocaImmediata));
        }
      },
        err => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreVerificaPresenzaRevocaOrdinaria));
        });
  }

  private verificaPresenzaRevocaOrdinaria(resp) {
    if (resp) {
      this.messageService.add(
        {
          key: 'checkPresenzaRevocaOrdinaria',
          sticky: true,
          severity: 'warn',
          summary: A4gMessages.checkVerificaPresenzaRevocaOrdinariaSummary,
          detail: A4gMessages.checkVerificaPresenzaRevocaOrdinariaDetail
        });
    } else {
      this.revocaImmediataDialog.onOpen(this.datiMandato);
    }
  }

  public confirm() {
    this.messageService.clear('checkPresenzaRevocaOrdinaria');
    this.revocaImmediataDialog.onOpen(this.datiMandato);
  }

  public reject() {
    this.messageService.clear('checkPresenzaRevocaOrdinaria');
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  public onDialogClose(evento) {
  }

  public isPersonaFisica(): boolean {
    return this.cuaa.length === 16;
  }

}
