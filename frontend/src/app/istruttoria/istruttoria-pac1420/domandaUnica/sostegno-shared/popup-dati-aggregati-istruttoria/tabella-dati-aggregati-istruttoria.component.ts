import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import * as moment from 'moment';
import { MessageService } from 'primeng/api';
import { forkJoin, Observable, of, Subject } from 'rxjs';
import { catchError, concatMap, mergeMap, takeUntil, tap } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { IstruttoriaDomandaUnicaFilter } from '../../classi/IstruttoriaDomandaUnicaFilter';
import { SostegnoDu } from '../../classi/SostegnoDu';
import { TipoIstruttoriaEnum } from '../../classi/TipoIstruttoriaEnum';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { NettoLordoEnum, PremioPagamentiStatoIstruttoriaNettoLordoDto } from '../../domain/PremioPagamentiStatoIstruttoriaNettoLordoDto';
import { IstruttoriaService } from '../../istruttoria.service';
import { DatiAggregatiIstruttoriaModel } from './models/dati-aggregati-istruttoria.model';



@Component({
    selector: 'app-tabella-dati-aggregati-istruttoria',
    templateUrl: 'tabella-dati-aggregati-istruttoria.component.html',
    styleUrls: ['tabella-dati-aggregati-istruttoria.component.scss']
  })
  export class TabellaDatiAggregatiIstruttoriaComponent implements OnInit, OnDestroy {

    public columnsDescription: any[];
    public tipoSostegno: SostegnoDu;
    public annoCampagna: number;
    public datiAggregatiModel: DatiAggregatiIstruttoriaModel[] = [];
    public isShowPagamentoAnticipoAsZero: boolean = false;
    public rowGroupMetadata: any = {};
    
    @Input('tipoSostegno') set setTipoSostegno(tipoSostegno: string) {
      this.tipoSostegno = SostegnoDu[tipoSostegno];
      this.setupTableIntegratoRow(this.tipoSostegno);
      this.setupTableGrouping();
    }
    @Input() public tipoIstruttoria: TipoIstruttoriaEnum;
    @Input('annoCampagna') set setAnnoCampagna(annoCampagna: number) {
      this.annoCampagna = annoCampagna;
      let showBeforeDate = moment(`${annoCampagna}1201`, "YYYYMMDD").hours(0).minutes(0).seconds(0);
      let nowDate = moment(moment.now());
      this.isShowPagamentoAnticipoAsZero = nowDate.isSameOrAfter(showBeforeDate);
    }
    @Output() public datiAggregatiModelEmitter = new EventEmitter();
    
    private componentDestroyed$: Subject<boolean> = new Subject();

    private countersRequestParams = [
      StatoIstruttoriaEnum.RICHIESTO,
      StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK,
      StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO,
      StatoIstruttoriaEnum.NON_AMMISSIBILE,
      StatoIstruttoriaEnum.LIQUIDABILE,
      StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO,
      StatoIstruttoriaEnum.DEBITI,
      StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO,
      StatoIstruttoriaEnum.CONTROLLI_INTERSOSTEGNO_OK,
      StatoIstruttoriaEnum.NON_LIQUIDABILE,
      StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO
    ];
    
    constructor(
      private istruttoriaService: IstruttoriaService,
      private messageService: MessageService,
      private translateService: TranslateService) {
        this.columnsDescription = [
          { field: 'stato', header: translateService.instant('IstruttorieDatiAggregati.stato')},
          { field: 'numeroDomande', header: translateService.instant('IstruttorieDatiAggregati.numeroDomande')},
          { field: 'valoreLordo', header: translateService.instant("IstruttorieDatiAggregati.valoreLordo")},
          { field: 'valoreNetto', header: translateService.instant("IstruttorieDatiAggregati.valoreNetto")},
        ];
    }

    private setupTableIntegratoRow(currentSostegno: SostegnoDu) {
      if (currentSostegno === SostegnoDu.ZOOTECNIA) {
        this.countersRequestParams.splice(1, 0, StatoIstruttoriaEnum.INTEGRATO);
      }
    }

    private setupTableGrouping() {
      let richiestoPos: number =  this.countersRequestParams.findIndex(e => e === StatoIstruttoriaEnum.RICHIESTO);
      let liquidabiliPos: number =  this.countersRequestParams.findIndex(e => e === StatoIstruttoriaEnum.LIQUIDABILE);
      let liquidabiliSost: number =  this.countersRequestParams.findIndex(e => e === StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO);
      let totPos: number = this.countersRequestParams.length;
      this.rowGroupMetadata[richiestoPos] = {title: "CONTROLLI_AMMISSIBILITA"};
      this.rowGroupMetadata[liquidabiliPos] = {title: "CONTROLLI_LIQUIDABILITA"};
      this.rowGroupMetadata[liquidabiliSost] = {title: "CONTROLLI_INTERSOSTEGNO"};
      this.rowGroupMetadata[totPos] = {title: "TOTALI"};
    }

    private loadCounter(annoCampagna: number, sostegno: SostegnoDu, tipo: TipoIstruttoriaEnum, stato: StatoIstruttoriaEnum): Observable<number> {
      let istruttoriaDomandaUnicaFilter = new IstruttoriaDomandaUnicaFilter();
      istruttoriaDomandaUnicaFilter.campagna = annoCampagna;
      istruttoriaDomandaUnicaFilter.sostegno = sostegno;
      istruttoriaDomandaUnicaFilter.tipo = tipo;
      istruttoriaDomandaUnicaFilter.stato = stato;
      return this.istruttoriaService.countIstruttorieDU(istruttoriaDomandaUnicaFilter);
    }

    private handleResultPremi(collectToArray: DatiAggregatiIstruttoriaModel[], results: PremioPagamentiStatoIstruttoriaNettoLordoDto[]) {
      let totValoreNetto = 0;
      let totValoreLordo = 0;
      if (results) {
        for (let state of this.countersRequestParams) {
          let riga = collectToArray.find(e => e.keyDesc === state);
          if (!riga) {
            riga = new DatiAggregatiIstruttoriaModel();
            riga.keyDesc = state;
            riga.numeroDomande = 0;
            collectToArray.push(riga);
          }
          if (state === StatoIstruttoriaEnum.RICHIESTO
              || state === StatoIstruttoriaEnum.NON_AMMISSIBILE
              || state === StatoIstruttoriaEnum.INTEGRATO
              || state === StatoIstruttoriaEnum.NON_LIQUIDABILE) {
              riga.valoreNetto = 0;
              riga.valoreLordo = 0;
          } else if (this.tipoSostegno === SostegnoDu.DISACCOPPIATO 
            && this.tipoIstruttoria === TipoIstruttoriaEnum.ANTICIPO 
            && state !== StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO 
            && this.isShowPagamentoAnticipoAsZero) {
              riga.valoreNetto = 0;
              riga.valoreLordo = 0;
          } else {
            if (this.tipoSostegno === SostegnoDu.SUPERFICIE 
              && state === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO) {
              let res = results.find(e => e.statoIstruttoria === state && e.tipoPremio === NettoLordoEnum.LORDO);
              riga.valoreLordo = res ? res.valorePremio : 0;
              riga.valoreNetto = 0;
            } else {
              let res = results.find(e => e.statoIstruttoria === state && e.tipoPremio === NettoLordoEnum.NETTO);
              riga.valoreNetto = res ? res.valorePremio : 0;
              res = results.find(e => e.statoIstruttoria === state && e.tipoPremio === NettoLordoEnum.LORDO);
              riga.valoreLordo = res ? res.valorePremio : 0;
            }
          }
          totValoreNetto+=riga.valoreNetto;
          totValoreLordo+=riga.valoreLordo;
        }
      } else {
        for (let row of collectToArray) {
          row.valoreNetto = 0;
          row.valoreLordo = 0;
        }
      }
      for (let state of this.countersRequestParams) {
        let riga = collectToArray.find(e => e.keyDesc === state);
        riga.stato = this.translateService.instant(`STATO_LAVORAZIONE_SOSTEGNO.${riga.keyDesc}`);
      }
      let totLine: DatiAggregatiIstruttoriaModel = collectToArray[collectToArray.length - 1];
      totLine.valoreLordo = totValoreLordo;
      totLine.valoreNetto = totValoreNetto;
      this.datiAggregatiModelEmitter.emit(collectToArray);
      return collectToArray;
    }
    
    private handleResultCounters(results: number[]) {
      let collectToArray: DatiAggregatiIstruttoriaModel[] = [];
      let totNumeroDomande = 0;
      results.forEach((res, index) => {
        let dato = new DatiAggregatiIstruttoriaModel();
        dato.numeroDomande = Number(results[index]);
        totNumeroDomande += dato.numeroDomande;
        dato.keyDesc = this.countersRequestParams[index];
        collectToArray.push(dato);
      });
      let totLine = new DatiAggregatiIstruttoriaModel();
      totLine.numeroDomande = totNumeroDomande;
      totLine.keyDesc = `TOT_${this.tipoIstruttoria}_${this.tipoSostegno}`;
      totLine.stato = this.translateService.instant(`IstruttorieDatiAggregati.${totLine.keyDesc}`);
      collectToArray.push(totLine);
      return collectToArray;
    }

    public exportPdf(pTableElement) {
        const doc = new jsPDF();
        autoTable(doc, { html: pTableElement.tableViewChild.nativeElement });
        doc.save('tabella-dati-aggregati-istruttoria.pdf');
    }

    ngOnInit() {
      const loadCounterCall = val => this.loadCounter(this.annoCampagna, this.tipoSostegno, this.tipoIstruttoria, val);
      let collectToArray: DatiAggregatiIstruttoriaModel[];
      of(this.countersRequestParams).pipe(
        mergeMap(pTupla => forkJoin(...pTupla.map(loadCounterCall))),
        tap(results => {
          collectToArray = this.handleResultCounters(results);
        }),
        concatMap(results => this.istruttoriaService.getDatiAggregatiPagamenti(this.tipoSostegno, this.tipoIstruttoria, this.annoCampagna)),
        catchError(e => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
          throw e;
        }),
        tap(results => {
          this.datiAggregatiModel = this.handleResultPremi(collectToArray, results);
        }),
        catchError(e => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
          throw e;
        }),
        takeUntil(this.componentDestroyed$)
      ).subscribe();
    }
  
    ngOnDestroy() {
      this.componentDestroyed$.next(true);
      this.componentDestroyed$.complete();
    }
}
