import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { FascicoloDettaglioService } from 'src/app/fascicolo/fascicolo-dettaglio-container/fascicolo-dettaglio.service';
import { FascicoloDettaglio } from 'src/app/fascicolo/shared/fascicolo.model';
import { AllevamentoDto } from '../../../creazione-fascicolo/dto/AllevamentoDto';
import { ZootecniaService } from '../zootecnia.service';
import { DatePipe } from '@angular/common'
import { catchError, switchMap, takeUntil, tap } from 'rxjs/operators';
import {BehaviorSubject, EMPTY, Observable, of, Subject} from 'rxjs';
import { AnagraficaFascicoloService } from 'src/app/fascicolo/creazione-fascicolo/anagrafica-fascicolo.service';
import { FascicoloDaCuaa } from 'src/app/a4g-common/classi/FascicoloCuaa';
import { StatoFascicoloEnum } from './../../../creazione-fascicolo/dto/DatiAperturaFascicoloDto';
import {MediatorService} from "../../../mediator.service";

@Component({
  selector: 'app-allevamenti',
  templateUrl: './allevamenti.component.html',
  styleUrls: ['./allevamenti.component.css']
})
export class AllevamentiComponent implements OnInit, OnDestroy {
  public cols: any[];
  public cols2: any[];
  public allevamenti: AllevamentoDto[];
  private cuaa = '';
  protected componentDestroyed$: Subject<boolean> = new Subject();
  private _allevamentoCorrente$ = new BehaviorSubject<AllevamentoDto[]>(null);
  private idValidazione: number = undefined;
  private fascicoloCorrente: FascicoloDaCuaa;

  constructor(
    private route: ActivatedRoute,
    private fascicoloDettaglioService: FascicoloDettaglioService,
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private zootecniaService: ZootecniaService,
    private messageService: MessageService,
    private datepipe: DatePipe,
    private mediatorService: MediatorService
  ) {}

  private subscribeStatoFascicolo() {
    this.fascicoloDettaglioService.fascicoloCorrente.pipe(
      takeUntil(this.componentDestroyed$))
      .subscribe((fascicoloCorrente: FascicoloDaCuaa) => {
        if (fascicoloCorrente) {
          this.fascicoloCorrente = fascicoloCorrente;
        }
      });
  }

  ngOnInit() {
    this.setCols();
    this.cuaa = this.route.snapshot.paramMap.get('cuaa');
    this.subscribeStatoFascicolo();
    const paramIdVal = this.route.snapshot.queryParams['id-validazione'];
    if (paramIdVal) {
      this.idValidazione = Number.parseInt(paramIdVal);
    } else {
      this.idValidazione = 0;
    }
    this.getAllevamentiObservable(this.cuaa).subscribe();
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  private setCols() {
    this.cols = [
      // { field: 'identificativoFiscale', header: 'Identificativo fiscale' },
      { field: 'specie', header: 'Specie' },
      { field: 'struttura.identificativo', header: 'Identificativo della struttura' },
      { field: 'struttura.comune', header: 'Comune della struttura' },
      { field: 'cfProprietario', header: 'Codice fiscale proprietario' },
      { field: 'cfDetentore', header: 'Codice fiscale detentore' },
      { field: 'tipologiaAllevamento', header: 'Tipologia di allevamento' },
      { field: 'tipoProduzione', header: 'Tipo produzione' },
      { field: 'orientamentoProduttivo', header: 'Orientamento produttivo' }
    ];
  }

  public handleClick() {
    this.fascicoloDettaglioService.mostraDettaglioSezione.next(FascicoloDettaglio.DATI);
  }

  private getAllevamentiObservable(cuaa: string): Observable<AllevamentoDto[]> {
    return this.zootecniaService.getAllevamenti(cuaa).pipe(
      takeUntil(this.componentDestroyed$),
      catchError(err => {
        this.messageService.add(
          A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.errorGetAllevamenti));
        return EMPTY;
      }),
      tap(res => this.allevamenti = res)
    );
  }

  public fascicoloEditable(): boolean {
    return this.fascicoloCorrente && this.idValidazione === 0 && (
      this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_AGGIORNAMENTO
      || this.fascicoloCorrente.stato === StatoFascicoloEnum.VALIDATO
      || this.fascicoloCorrente.stato === StatoFascicoloEnum.CONTROLLATO_OK
      || this.fascicoloCorrente.stato === StatoFascicoloEnum.IN_CHIUSURA);
  }
  
  public updateAllevamenti() {
    const dataRichiesta = this.datepipe.transform(new Date(), 'yyyy-MM-dd');
    this.zootecniaService.putUpdateAllevamenti(this.cuaa, dataRichiesta).pipe(
      takeUntil(this.componentDestroyed$),
      switchMap(() => this.anagraficaFascicoloService.getFascicolo(this.cuaa, 0)),
      switchMap(res => {
        this.fascicoloDettaglioService.fascicoloCorrente.next(res);
        return this.getAllevamentiObservable(this.cuaa);
      }),
      switchMap( res => {
        if (res) {
          this.setInAggiornamento(this.fascicoloCorrente.cuaa);
        }
        return of(this.cuaa);
      }),
      catchError(error => {
        A4gMessages.handleError(this.messageService, error, A4gMessages.ERRORE_GENERICO);
        return EMPTY;
      }),
      switchMap ( (cuaa: string) =>{
        return this.mediatorService.deleteEsitiControlloCompletezza(cuaa);
      }),
      catchError(error => {
        A4gMessages.handleError(this.messageService, error, A4gMessages.ERRORE_GENERICO);
        return EMPTY;
      })
    ).subscribe(res => {
      this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
    });
  }

  public setInAggiornamento(cuaa: string) {
    this.anagraficaFascicoloService.putSetStatusFascicoloInAggiornamento(cuaa)
      .subscribe(
        () => {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.success, A4gMessages.changeStatusInAggiornamentoOk));
          this.fascicoloCorrente.stato = StatoFascicoloEnum.IN_AGGIORNAMENTO;
          this.reloadDataFascicolo();
        }, err => {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, A4gMessages.changeStatusInAggiornamentoKo));
        });
  }

  public reloadDataFascicolo() {
    this.fascicoloDettaglioService.fascicoloCorrente.next(this.fascicoloCorrente);
  }
}
