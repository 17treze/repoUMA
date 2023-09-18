import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { EMPTY, Subject } from 'rxjs';
import { catchError, takeUntil } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { PaginatorService } from 'src/app/a4g-common/services/paginator.service';
import { PaginatorEvent, Paginazione, SortDirection } from 'src/app/a4g-common/utility/paginazione';
import { Labels } from 'src/app/app.labels';
import { FascicoloService } from '../fascicolo.service';
import { ValidazioneFascicoloDto } from '../shared/fascicolo.model';
import { FascicoliValidatiFilterDto } from './filtro-fascicoli-validati/filtro-fascicoli-validati.component';

@Component({
  selector: 'app-fascicoli-validati',
  templateUrl: './fascicoli-validati.component.html',
  styleUrls: ['./fascicoli-validati.component.scss']
})
export class FascicoliValidatiComponent implements OnInit, OnDestroy {
  public validazioniFascicoloList: ValidazioneFascicoloDto[] = [];
  public elementiPerPagina: number = 0;
  public elementiTotali: number = 0;
  public selectedValidazione: ValidazioneFascicoloDto;
  protected componentDestroyed$: Subject<boolean> = new Subject();
  private lastEvent: PaginatorEvent = undefined;

  constructor(
    protected route: ActivatedRoute,
    protected fascicoloService: FascicoloService,
    private paginatorService: PaginatorService,
    private messageService: MessageService,
    protected router: Router
  ) {}

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  ngOnInit() {
    this.elementiPerPagina = Labels.defaultStartIndexPaginator;

  }

  public goToValidazione(cuaa: string, idValidazione: number) {
    this.router.navigate(
      ['../dettaglio'],
      {
        relativeTo: this.route.parent,
        replaceUrl: false,
        queryParams: { 'id-validazione': idValidazione},
        queryParamsHandling: 'merge'
      });
  }

  loadList(event: PaginatorEvent) {
    /*
    let sortOrder;
    const sortBy: string = event.sortField || 'dataValidazione';
    if (event.sortField) {
      sortOrder = event.sortOrder === 1 ? SortDirection.ASC : SortDirection.DESC;
    }
    const pagination: Paginazione = this.paginatorService.getPagination(
      Math.round(event.first / this.elementiPerPagina),
      this.elementiPerPagina,
      sortBy,
      sortOrder || SortDirection.DESC
    );
    const cuaa = this.route.snapshot.paramMap.get('cuaa');
    this.fascicoloService.getFascicoliValidati(cuaa, event.filters, pagination).pipe(
        takeUntil(this.componentDestroyed$),
        catchError(e => {
          this.messageService.add(
            A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
          this.validazioniFascicoloList = [];
          return EMPTY;
        })
      ).subscribe(res => {
        this.validazioniFascicoloList = res.risultati;
        this.elementiTotali = res.count;
      });
    */
  }

  public lazyLoadData(event: PaginatorEvent) {
    if (this.lastEvent) {
      event.filters = this.lastEvent.filters;
    }
    this.lastEvent = event;
    if (event.rows) {
      this.elementiPerPagina = event.rows;
    }
    this.loadList(event);
  }

  public onSearch(fascicoliValidatiFilterDto: FascicoliValidatiFilterDto) {
    const pagEvent: PaginatorEvent = this.lastEvent;
    pagEvent.filters = fascicoliValidatiFilterDto;
    this.loadList(pagEvent);
  }
}
