import { TranslateService } from '@ngx-translate/core';
import { StatoFascicoloEnum } from '../../../creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { FascicoloDaCuaa } from 'src/app/a4g-common/classi/FascicoloCuaa';
import { AnagraficaFascicoloService } from '../../../creazione-fascicolo/anagrafica-fascicolo.service';
import { EredeDto } from '../../../creazione-fascicolo/dto/EredeDto';
import { FascicoloDettaglioService } from '../../../fascicolo-dettaglio-container/fascicolo-dettaglio.service';
import { FascicoloDettaglio } from '../../../shared/fascicolo.model';
import { catchError, switchMap, takeUntil } from 'rxjs/operators';
import { EMPTY, Subject } from 'rxjs';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-eredi',
  templateUrl: './eredi.component.html',
  styleUrls: ['./eredi.component.scss']
})

export class ErediComponent implements OnInit, OnDestroy {
  public cuaa = '';
  public listaEredi: EredeDto[];
  public erede: EredeDto;
  public selectedErede: any;
  private idDaCancellare = 0;
  private fascicoloCorrente: FascicoloDaCuaa;
  public eredeFormGroup: FormGroup;
  protected componentDestroyed$: Subject<boolean> = new Subject();
  private idValidazione: number = undefined;
  public popupEredeOpen = false;

  constructor(
    protected anagraficaFascicoloService: AnagraficaFascicoloService,
    protected route: ActivatedRoute,
    protected messageService: MessageService,
    private fascicoloDettaglioService: FascicoloDettaglioService,
    private translateService: TranslateService
  ) { }

  ngOnInit() {
    this.cuaa = this.route.snapshot.paramMap.get('cuaa');
    this.route.queryParams.pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(queryParams => {
      const paramIdVal: string = queryParams['id-validazione'];
      if (paramIdVal) {
        this.idValidazione = Number.parseInt(paramIdVal);
      } else {
        this.idValidazione = 0;
      }
      this.getEredi(this.cuaa, this.idValidazione);
    });
    this.subscribeStatoFascicolo();
  }

  private getEredi(cuaa: string, idValidazione: number) {
    this.anagraficaFascicoloService.getEredi(cuaa, idValidazione).pipe(
      takeUntil(this.componentDestroyed$),
      catchError(err => {
        this.listaEredi = [];
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        return EMPTY;
      })
    ).subscribe((response) => {
      this.listaEredi = response;
      if (!this.listaEredi) {
        this.listaEredi = [];
      }
      this.fascicoloDettaglioService.eredi.next(this.listaEredi);
    });
  }

  public aggiungiErede() {
    this.erede = null;
    this.popupEredeOpen = true;
  }

  public cancellaErede(id: number, cfErede: string) {
    this.idDaCancellare = id;
    this.messageService.add(
      A4gMessages.getToast(
        'cancellaErede',
        A4gSeverityMessage.warn,
        A4gMessages.checkACancellaErede + cfErede + '?'
      )
    );
  }

  public AnnullaCancellaErede() {
    this.messageService.clear('cancellaErede');
  }

  public confermaCancellaErede() {
    this.messageService.clear('cancellaErede');
    if (!this.idDaCancellare) {
      this.idDaCancellare = 0;
    }
    console.log('Cancella record id: ' + this.idDaCancellare);
    this.anagraficaFascicoloService.cancellaErede(this.cuaa, this.idDaCancellare).pipe(
      takeUntil(this.componentDestroyed$),
      catchError(err => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, A4gMessages.salvataggioDatiKo));
        return EMPTY;
      }),
      switchMap((response) => {
        // find index in your array
        const index = this.listaEredi.findIndex(d => d.id === this.idDaCancellare);
        this.listaEredi.splice(index, 1); // remove element from array
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        this.idDaCancellare = 0;
        return this.anagraficaFascicoloService.getFascicolo(this.cuaa, this.idValidazione);
      })
    ).subscribe(fascicoloCorrente => {
      this.fascicoloDettaglioService.fascicoloCorrente.next(fascicoloCorrente);
    });
  }

  private subscribeStatoFascicolo() {
    this.fascicoloDettaglioService.fascicoloCorrente.pipe(
      takeUntil(this.componentDestroyed$))
      .subscribe((fascicoloCorrente: FascicoloDaCuaa) => {
        if (fascicoloCorrente) {
          this.fascicoloCorrente = fascicoloCorrente;
        }
      });
  }

  public eredeEditable(): boolean {
    return !this.eredeFormGroup.invalid && this.idValidazione === 0;
  }

  public fascicoloEditable(): boolean {
    return this.fascicoloCorrente && this.idValidazione === 0 && this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_CHIUSURA;
  }

  public handleClick() {
    this.fascicoloDettaglioService.mostraDettaglioSezione.next(FascicoloDettaglio.DATI);
  }

  public footerTabella(): string {
    let ret = '';
    if (this.listaEredi && this.listaEredi.length === 0) {
      ret = 'Non sono presenti eredi';
    } else if (this.listaEredi && this.listaEredi.length === 1) {
      ret = 'È presente 1 erede';
    } else if (this.listaEredi) {
      ret = 'Sono presenti ' + this.listaEredi.length + ' eredi.';
    }
    return ret;
  }

  public saveErede(erede: EredeDto) {
    this.anagraficaFascicoloService.salvaErede(erede, this.cuaa).pipe(
      takeUntil(this.componentDestroyed$),
      catchError(error => {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, error.error.message));
        return EMPTY;
      })).subscribe(
        () => {
          this.closePopupErede();
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.success,
            this.translateService.instant('EREDE.SALVATAGGIO_OK')));
          this.getEredi(this.cuaa, this.idValidazione);
        });
  }

  public closePopupErede() {
    this.popupEredeOpen = false;
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }
}
