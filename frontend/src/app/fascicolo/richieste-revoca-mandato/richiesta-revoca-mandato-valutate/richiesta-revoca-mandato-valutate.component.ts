import {RichiestaRevocaSupport} from './../utility/richiesta-revoca-support';
import {MessageService} from 'primeng/api';
import {Component, EventEmitter, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {A4gPagedTableComponent} from 'src/app/a4g-common/a4g-paged-table/a4g-paged-table.component';
import {PaginatorService} from 'src/app/a4g-common/services/paginator.service';
import {Paginazione, SortDirection} from 'src/app/a4g-common/utility/paginazione';
import {Labels} from 'src/app/app.labels';
import {RichiestaRevocaImmediataDto, RichiesteRevocaImmediata} from '../dto/RichiestaRevocaImmediataDto';
import {A4gMessages, A4gSeverityMessage} from 'src/app/a4g-common/a4g-messages';
import {MandatoService} from '../../mandato.service';
import {RichiestaValutataEventService} from '../richiesta-valutata-event.service';
import {Subject} from 'rxjs';
import {takeUntil} from 'rxjs/operators';
import * as FileSaver from "file-saver";

@Component({
  selector: 'app-richiesta-revoca-mandato-valutate',
  templateUrl: './richiesta-revoca-mandato-valutate.component.html',
  styleUrls: ['./richiesta-revoca-mandato-valutate.component.css']
})
export class RichiestaRevocaMandatoValutateComponent implements OnInit, OnDestroy {
  @Output() updateCounter = new EventEmitter();
  @ViewChild('table', { static: true }) table: A4gPagedTableComponent;

  public cols: any[];
  public richiesteValutate: any[];
  public elementiTotali: number;
  public intestazioni = Labels;
  public numeroPagina = 0;
  public elementiPerPagina = 10;
  public paginazione: Paginazione;
  public first = true;
  public richiesteListTable = new RichiesteRevocaImmediata();
  public richiestaRevocaSupport = RichiestaRevocaSupport;
  private defaultPropertySort = 'dataSottoscrizione';
  private componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    protected paginatorService: PaginatorService,
    protected messageService: MessageService,
    protected mandatoService: MandatoService,
    protected richiestaValutataEventService: RichiestaValutataEventService
  ) {
    this.richiestaValutataEventService.getEventObservable().pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(eventVal => this.getRichiesteValutate(this.table))
  }

  ngOnInit() {
    this.initializeObject();
    this.cols = this.defineTableColumns();
    this.getRichiesteValutate(this.table);
  }
  
  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  public updateList() {
    this.getRichiesteValutate(this.table);
  }

  private initializeObject() {
    this.richiesteListTable = new RichiesteRevocaImmediata();
    this.richiesteListTable.risultati = new Array<RichiestaRevocaImmediataDto>();
  }

  public changePage(event) {
    if (this.first) {
      this.first = false;
    } else {
      this.getRichiesteValutate(event);
    }
  }

  private defineTableColumns() {
    return [
      { field: '', header: '' },
      { field: null, header: Labels.cuaaSigla },
      { field: "mandato.fascicolo.denominazione", header: Labels.denominazioneAzienda },
      { field: "mandato.sportello.denominazione", header: Labels.sedeSportelloCaa },
      { field: "mandato.sportello.comune", header: Labels.localitaSportelloCaa },
      { field: 'dataSottoscrizione', header: Labels.dataSottoscrizione },
      { field: 'stato', header: Labels.esito },
      { field: null, header: Labels.downloadDocumentoRevocaImmediata }
    ];
  }

  private getRichiesteValutate(event) {
    this.numeroPagina = Math.floor(event.first / this.elementiPerPagina);
    if (event != null && event.sortField != null) {
      this.paginazione = Paginazione.of(
        this.numeroPagina, this.elementiPerPagina, event.sortField, Paginazione.getOrdine(event.sortOrder)
      );
    } else {
      this.paginazione = Paginazione.of(this.numeroPagina, this.elementiPerPagina, this.defaultPropertySort, SortDirection.ASC);
    }
    this.mandatoService.richiesteRevocaImmediataValutatePerCaa(this.paginazione)
      .subscribe(
        resp => {
          if (resp) {
            this.richiesteListTable = resp;
            this.updateCounter.emit(this.richiesteListTable.count);
          }
        },
        err => {
          this.messageService.add(
            A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati)
          );
        }
      );
  }

  public onDialogClose(evento) {
  }

  public downloadRichiesta(revoca: RichiestaRevocaImmediataDto) {
    this.mandatoService.getRevocaImmediataFile(revoca.idProtocollo)
        .subscribe(
            resp => {
              FileSaver.saveAs(resp, 'Richiesta_Revoca_Immediata.pdf');
            }, err => {
              this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_STAMPA_REVOCA));
            }
        );
  }

}
