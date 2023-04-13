import { FascicoliValidatiFilterDto } from './fascicoli-validati-filter/fascicoli-validati-filter.component';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng-lts';
import { EMPTY, Subject } from 'rxjs';
import { catchError, takeUntil } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { PaginatorEvent, Paginazione, SortDirection } from 'src/app/shared/models/paginazione';
import { AnagraficaFascicoloService } from 'src/app/shared/services/anagrafica-fascicolo.service';
import { PaginatorService } from 'src/app/shared/services/paginator.service';
import { ValidazioneFascicoloDto } from '../models/anagrafica-fascicolo';
import { FascicoloDettaglioService } from 'src/app/shared/services/fascicolo-dettaglio.service';

@Component({
  selector: 'app-fascicoli-validati',
  templateUrl: './fascicoli-validati.component.html',
  styleUrls: ['./fascicoli-validati.component.scss']
})
export class FascicoliValidatiComponent implements OnInit, OnDestroy {
  public validazioniFascicoloList: ValidazioneFascicoloDto[] = [];
  public elementiPerPagina = 0;
  public elementiTotali = 0;
  public selectedValidazione: ValidazioneFascicoloDto;
  protected componentDestroyed$: Subject<boolean> = new Subject();
  private lastEvent: PaginatorEvent = undefined;

  constructor(
    protected route: ActivatedRoute,
    protected anagraficaFascicoloService: AnagraficaFascicoloService,
    private paginatorService: PaginatorService,
    private messageService: MessageService,
    protected router: Router,
    private fascicoloDettaglioService: FascicoloDettaglioService
  ) { }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  ngOnInit() { }

  public goToValidazione(cuaa: string, idValidazione: number) {
    this.anagraficaFascicoloService.getFascicoloDaCuaa(cuaa, idValidazione)
      .subscribe(fascicoloCorrente => {
        this.fascicoloDettaglioService.fascicoloCorrente.next(fascicoloCorrente);
      });

    this.router.navigate(
      ['.'],
      {
        relativeTo: this.route.parent,
        replaceUrl: false,
        queryParams: { 'id-validazione': idValidazione },
        queryParamsHandling: 'merge'
      });
  }

  loadList(event: PaginatorEvent) {
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
    const cuaa = this.route.snapshot.parent.paramMap.get('cuaa');
    this.anagraficaFascicoloService.getFascicoliValidati(cuaa, event.filters, pagination).pipe(
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
