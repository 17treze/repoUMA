import { EredeDto } from './../../../creazione-fascicolo/dto/EredeDto';
import { HttpErrorResponse } from '@angular/common/http';
import { TranslateService } from '@ngx-translate/core';
import { StatoFascicoloEnum } from './../../../creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { FascicoloDaCuaa } from 'src/app/a4g-common/classi/FascicoloCuaa';
import { AnagraficaFascicoloService } from '../../../creazione-fascicolo/anagrafica-fascicolo.service';
import { ModalitaPagamentoDto } from '../../../creazione-fascicolo/dto/ModalitaPagamento';
import { FascicoloDettaglioService } from '../../../fascicolo-dettaglio-container/fascicolo-dettaglio.service';
import { FascicoloDettaglio } from '../../../shared/fascicolo.model';
import {catchError, switchMap, takeUntil} from 'rxjs/operators';
import { EMPTY, Subject } from 'rxjs';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-modalita-pagamento',
  templateUrl: './modalita-pagamento.component.html',
  styleUrls: ['./modalita-pagamento.component.scss']
})
export class ModalitaPagamentoComponent implements OnInit {
  public cuaa = '';
  public listaModalitaPagamento: ModalitaPagamentoDto[];
  public displayDialog: boolean;
  public modalitaPagamento: ModalitaPagamentoDto;
  public selectedModalitaPagamento: any;
  public cols: any[];
  private idDaCancellare = 0;
  private fascicoloCorrente: FascicoloDaCuaa;
  private newModalitaPagamento = false;
  public modalitaPagamentoFormGroup: FormGroup;
  protected componentDestroyed$: Subject<boolean> = new Subject();
  private idValidazione: number = undefined;
  private eredi: EredeDto[] = [];

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
      this.getModalitaDiPagamento(this.cuaa, this.idValidazione);
    });
    this.subscribeStatoFascicolo();
    this.setCols();
    this.subscribeEredi();
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
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        return EMPTY;
      })
    ).subscribe((response) => {
      this.listaModalitaPagamento = response;
      if (!this.listaModalitaPagamento) {
        this.listaModalitaPagamento = [];
      }
    });
  }

  public showDialogToAdd() {
    this.displayDialog = true;
    this.newModalitaPagamento = true;
    this.modalitaPagamentoFormGroup = new FormGroup({
      iban: new FormControl('', [Validators.required])
    });
  }

  public annulla() {
    console.log('annulla cancellazione');
    this.messageService.clear('cancellaModalitaPagamento');
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

  public deleteRow() {
    this.messageService.clear('cancellaModalitaPagamento');
    if (!this.idDaCancellare) {
      this.idDaCancellare = 0;
    }
    console.log('Cancella record id: ' + this.idDaCancellare);
    this.anagraficaFascicoloService.cancellaModalitaPagamento(this.cuaa, this.idDaCancellare).pipe(
      takeUntil(this.componentDestroyed$),
      catchError(err => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, A4gMessages.salvataggioDatiKo));
        return EMPTY;
      }),
      switchMap((response) => {
        // find index in your array
        const index = this.listaModalitaPagamento.findIndex(d => d.id === this.idDaCancellare);
        this.listaModalitaPagamento.splice(index, 1); // remove element from array
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

  public modalitaPagamentoEditable(): boolean {
    return !this.modalitaPagamentoFormGroup.invalid && this.idValidazione === 0;
  }

  public fascicoloEditable(): boolean {
    return this.fascicoloCorrente && this.idValidazione === 0 && (
      this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_AGGIORNAMENTO
      || this.fascicoloCorrente.stato === StatoFascicoloEnum.VALIDATO
      || this.fascicoloCorrente.stato === StatoFascicoloEnum.CONTROLLATO_OK
      || (this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_CHIUSURA && this.eredi.length > 0));
  }

  public handleClick() {
    this.fascicoloDettaglioService.mostraDettaglioSezione.next(FascicoloDettaglio.DATI);
  }

  private handleErrorOnPutModalitaPagamento = (err: HttpErrorResponse) => {
    let messaggioErrore: string;
    let errorMessage = err.error.message;
    console.log("errore: " + err.error.status);
    if (err.error.status === 400) {
      messaggioErrore = this.translateService.instant('MODALITA_PAGAMENTO.IBAN_NON_VALIDO');
    }
    else if (err.error.status === 406) {
      messaggioErrore = this.translateService.instant('MODALITA_PAGAMENTO.IBAN_NON_VALIDO');
    } 
    else if (err.error.status === 401 || err.error.status === 403 || err.error.status === 404 ) {// || err.error.status === 500) { 
      messaggioErrore = this.translateService.instant('MODALITA_PAGAMENTO.PROBLEMI_SERVER_PAGOPA');
      messaggioErrore = messaggioErrore.concat( err.error.status + ": " + "Riprovare più tardi");     
    }  
   
    else if (err.error.status === 502 || err.error.status === 503 || err.error.status === 504) { 
      messaggioErrore = this.translateService.instant('MODALITA_PAGAMENTO.MANCATA_RISPOSTA');
      messaggioErrore = messaggioErrore.concat( err.error.status + ": " + " Riprovare più tardi");     
     }     
    else if (err.error.status === 510) {
      messaggioErrore = this.translateService.instant('MODALITA_PAGAMENTO.TITOLARE_CONTO_MISMATCH_INTESTATARIO_FASCICOLO');
    }         
    else if(errorMessage.match("510 Not Extended")){ 
      messaggioErrore = this.translateService.instant('MODALITA_PAGAMENTO.TITOLARE_CONTO_MISMATCH_INTESTATARIO_FASCICOLO');      
    }   
    
    else {
      messaggioErrore = this.translateService.instant('MODALITA_PAGAMENTO.SALVATAGGIO_KO');
      //se non c'e' un erede firmatario il messaggio di errore in err.errore. inizia per "Deve essere specificato uno ed un solo erede firmatario."
      
      if (errorMessage.startsWith( 'Deve essere specificato uno ed un solo erede firmatario')) {
        messaggioErrore = this.translateService.instant('MODALITA_PAGAMENTO.EREDE_FIRMATARIO_ASSENTE');
      }
      //se IBAN gia inserito
      if (errorMessage.match("gia' presente per il cuaa")){ 
        messaggioErrore = this.translateService.instant('MODALITA_PAGAMENTO.IBAN_GIA_INSERITO');        
      }    

    }
    console.log(messaggioErrore);
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
        console.log(this.modalitaPagamento); 
        
        
        if (this.modalitaPagamento.bic == null && this.modalitaPagamento.cittaFiliale == null && this.modalitaPagamento.denominazioneFiliale == null ){
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.warn, this.translateService.instant('MODALITA_PAGAMENTO.IBAN_NON_CERTIFICATO')));
        } else {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.success, this.translateService.instant('MODALITA_PAGAMENTO.INSERT_OK')));        
           
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.success, this.translateService.instant('MODALITA_PAGAMENTO.IBAN_CERTIFICATO')));
        }

        this.listaModalitaPagamento = listaModalitaPagamento;
        this.modalitaPagamento = null;
        this.displayDialog = false;
        
        
        
        return this.anagraficaFascicoloService.getFascicolo(this.cuaa, this.idValidazione);
      })
      ).subscribe(fascicoloCorrente => {
        this.fascicoloDettaglioService.fascicoloCorrente.next(fascicoloCorrente);
        this.getModalitaDiPagamento(this.cuaa, this.idValidazione);
      });
  }

  public footerTabella(): string {
    let ret = '';
    if (this.listaModalitaPagamento && this.listaModalitaPagamento.length === 0) {
      ret = 'Non sono presenti modalità di pagamento';
    } else if (this.listaModalitaPagamento && this.listaModalitaPagamento.length === 1) {
      ret = 'È presente 1 modalità di pagamento';
    } else if (this.listaModalitaPagamento) {
      ret = 'Sono presenti ' + this.listaModalitaPagamento.length + ' modalità di pagamento.';
    }
    return ret;
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  private subscribeEredi() {
    this.fascicoloDettaglioService.eredi.pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe((eredi: EredeDto[]) => {
        this.eredi = eredi;
    });
  }

}
