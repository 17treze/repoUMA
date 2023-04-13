import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng-lts';
import { EMPTY, Subject } from 'rxjs';
import { catchError, switchMap, takeUntil } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { ModalitaPagamentoDto } from 'src/app/shared/models/ModalitaPagamento';
import { AnagraficaFascicoloService } from 'src/app/shared/services/anagrafica-fascicolo.service';
import { FascicoloDettaglioService } from 'src/app/shared/services/fascicolo-dettaglio.service';
import { FascicoloDaCuaa, StatoFascicoloEnum } from '../../models/FascicoloCuaa';

@Component({
  selector: 'app-modalita-pagamento',
  templateUrl: './modalita-pagamento.component.html',
  styleUrls: ['./modalita-pagamento.component.scss']
})
export class ModalitaPagamentoComponent implements OnInit, OnDestroy {

  public cuaa = '';
  public listaModalitaPagamento: ModalitaPagamentoDto[];
  public modalitaPagamento: ModalitaPagamentoDto;
  public selectedModalitaPagamento: any;
  public displayDialog: boolean;
  private newModalitaPagamento = false;
  private idDaCancellare = 0;
  public cols: any[];
  public modalitaPagamentoFormGroup: FormGroup;
  protected componentDestroyed$: Subject<boolean> = new Subject();
  private idValidazione: number = undefined;
  private fascicoloCorrente: FascicoloDaCuaa;

  constructor(
    protected anagraficaFascicoloService: AnagraficaFascicoloService,
    protected route: ActivatedRoute,
    protected messageService: MessageService,
    private translateService: TranslateService,
    private fascicoloDettaglioService: FascicoloDettaglioService
  ) {}

  ngOnInit(): void {
    this.fascicoloDettaglioService.fascicoloCorrente.pipe(
      switchMap(fascicoloCorrente => {
        this.cuaa = fascicoloCorrente.cuaa;
        return this.route.queryParams;
      }),
      takeUntil(this.componentDestroyed$)
    ).subscribe(queryParams => {
      const paramIdVal: string = queryParams['id-validazione'];
      if (paramIdVal) {
        this.idValidazione = Number.parseInt(paramIdVal, 10);
      } else {
        this.idValidazione = 0;
      }
      this.getModalitaDiPagamento(this.cuaa, this.idValidazione);
    });
    this.setCols();
    this.subscribeStatoFascicolo();
  }

  private setCols() {
    this.cols = [
      { field: 'iban', header: 'IBAN' },
      { field: 'bic', header: 'BIC' },
      { field: 'denominazioneIstituto', header: 'Denominazione Istituto Bancario' },
      { field: 'denominazioneFiliale', header: 'Denominazione Filiale' },
      { field: 'cittaFiliale', header: 'Città Filiale' }
    ];
  }

  private getModalitaDiPagamento(cuaa: string, idValidazione: number) {
    this.anagraficaFascicoloService.getModalitaPagamento(cuaa, idValidazione).pipe(
      takeUntil(this.componentDestroyed$),
      catchError(err => {
        this.listaModalitaPagamento = [];
        if (err.status === 403) {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('HttpErrorMessages.403')));
        } else {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        }
        return EMPTY;
      })
    ).subscribe((response) => {
      this.listaModalitaPagamento = response;
      if (!this.listaModalitaPagamento) {
        this.listaModalitaPagamento = [];
      }
    });
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  public showDialogToAdd() {
    this.displayDialog = true;
    this.newModalitaPagamento = true;
    this.modalitaPagamentoFormGroup = new FormGroup({
      iban: new FormControl('', [Validators.required])
    });
  }

  public cancellaIBAN(id: number, iban: string) {
    this.idDaCancellare = id;
    this.messageService.add(
      A4gMessages.getToast(
        'cancellaModalitaPagamento',
        A4gSeverityMessage.warn,
        A4gMessages.checkACancellaModalitaDiPagamento + iban + '?'
      )
    );
  }

  public footerTabella(): string {
    let ret: string = "";
    if (this.listaModalitaPagamento && this.listaModalitaPagamento.length == 0) {
      ret = "Non sono presenti modalità di pagamento";
    } else if (this.listaModalitaPagamento && this.listaModalitaPagamento.length == 1) {
      ret = "E' presente 1 modalità di pagamento";
    } else if (this.listaModalitaPagamento) {
      ret = "Sono presenti " + this.listaModalitaPagamento.length + " modalità di pagamento.";
    }
    return ret;
  }

  private handleErrorOnPutModalitaPagamento = (err: any) => {
    let messaggioErrore: string;
    if (err.error.message) {
      messaggioErrore = err.error.message;
    } else {
      messaggioErrore = A4gMessages.salvataggioDatiKo;
    }
    this.messageService.add(A4gMessages.getToast(
      'tst-dialog', A4gSeverityMessage.error, messaggioErrore));
    return EMPTY;
  }

  public save() {
    const ibanDaInserire: any = {
      'iban': this.modalitaPagamentoFormGroup.controls['iban'].value
    };
    this.anagraficaFascicoloService.putModalitaPagamento(this.cuaa, ibanDaInserire).pipe(
      takeUntil(this.componentDestroyed$),
      catchError(this.handleErrorOnPutModalitaPagamento),
      switchMap((response) => {
        this.modalitaPagamento = response;
        const listaModalitaPagamento = [...this.listaModalitaPagamento];
        if (this.newModalitaPagamento) {
          listaModalitaPagamento.push(this.modalitaPagamento);
        } else {
          listaModalitaPagamento[this.listaModalitaPagamento.indexOf(this.selectedModalitaPagamento)] = this.modalitaPagamento;
        }
        this.listaModalitaPagamento = listaModalitaPagamento;
        this.modalitaPagamento = null;
        this.displayDialog = false;
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.success, this.translateService.instant('MODALITA_PAGAMENTO.INSERT_OK')));
        return this.anagraficaFascicoloService.getFascicoloDaCuaa(this.cuaa, this.idValidazione);
      })
      ).subscribe(fascicoloCorrente => {
        this.fascicoloDettaglioService.fascicoloCorrente.next(fascicoloCorrente);
      });
  }

  public modalitaPagamentoEditable(): boolean {
    return !this.modalitaPagamentoFormGroup.invalid && this.idValidazione === 0;
  }

  public deleteRow() {
    this.messageService.clear('cancellaModalitaPagamento');
    if (!this.idDaCancellare) {
      this.idDaCancellare = 0;
    }
    console.log("Cancella record id: " + this.idDaCancellare);
    this.anagraficaFascicoloService.cancellaModalitaPagamento(this.cuaa, this.idDaCancellare).pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(response => {
        //find index in your array
        let index = this.listaModalitaPagamento.findIndex(d => d.id === this.idDaCancellare);
        this.listaModalitaPagamento.splice(index, 1);//remove element from array
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        this.idDaCancellare = 0;
        this.fascicoloCorrente.stato = StatoFascicoloEnum.IN_AGGIORNAMENTO;
        this.fascicoloDettaglioService.fascicoloCorrente.next(this.fascicoloCorrente);
      },
      catchError(err => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, A4gMessages.salvataggioDatiKo));
        return EMPTY;
      }));
  }

  public annulla() {
    console.log('annulla cancellazione');
    this.messageService.clear('cancellaModalitaPagamento');
  }

  public fascicoloEditable(): boolean {
    return this.fascicoloCorrente && this.idValidazione === 0 && (
      this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_AGGIORNAMENTO
      || this.fascicoloCorrente.stato === StatoFascicoloEnum.VALIDATO);
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
}
