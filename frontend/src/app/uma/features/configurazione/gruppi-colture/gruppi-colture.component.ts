import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EMPTY, of, Subscription } from 'rxjs';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { HttpClientConfigurazioneUmaService } from 'src/app/uma/core-uma/services/http-client-configurazione-uma.service';
import { PaginatorA4G, PaginatorEvent } from 'src/app/a4g-common/interfaces/paginator.model';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { Paginazione } from 'src/app/a4g-common/utility/paginazione';
import { catchError, switchMap } from 'rxjs/operators';
import { GruppoColtureDto } from 'src/app/uma/core-uma/models/dto/ConfigurazioneDto';

@Component({
  selector: 'app-gruppi-colture',
  templateUrl: './gruppi-colture.component.html',
  styleUrls: ['./gruppi-colture.component.scss']
})

export class GruppiColtureComponent implements OnInit, OnDestroy {

  listaGruppi: PaginatorA4G<Array<GruppoColtureDto>>;
  display: boolean;
  testoRicerca: string;
  numeroPagina: number;
  elementiPerPagina: number;
  totalElements: number;
  first: number;
  paginazione: Paginazione;
  defaultPropertySort: string;
  rowgroup: boolean;

  // Subscriptions
  getPagedGruppiSubscription: Subscription;

  cols: any;

  constructor(
    private errorService: ErrorService,
    private httpClientConfigurazioneUmaService: HttpClientConfigurazioneUmaService
  ) { }

  ngOnInit() {
    this.display = true;
    this.rowgroup = true;
    this.listaGruppi = {} as PaginatorA4G<Array<GruppoColtureDto>>;
    this.listaGruppi.count = 0;
    this.listaGruppi.risultati = [];
    this.initPaginator();
    this.getGruppi(this.buildPaginatorEvent());
  }

  ngOnDestroy() {
    if (this.getPagedGruppiSubscription) {
      this.getPagedGruppiSubscription.unsubscribe();
    }
  }

  onCloseToastGetGruppi() {
  }

  private getGruppi(event: PaginatorEvent) {
    this.numeroPagina = Math.floor(event.first / this.elementiPerPagina);
    if (this.numeroPagina == null) {
      this.numeroPagina = 0;
    }
    this.paginazione = Paginazione.of(this.numeroPagina, this.elementiPerPagina, event.sortField, Paginazione.getOrdine(event.sortOrder));
    this.paginazione.numeroElementiPagina = this.elementiPerPagina;

    this.getPagedGruppiSubscription = this.httpClientConfigurazioneUmaService.getGruppiColture(this.paginazione)
      .pipe(switchMap((res: PaginatorA4G<Array<GruppoColtureDto>>) => {
        return of(res);
      }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err, 'tst-gruppi-colture');
          return EMPTY;
        })
      )
      .subscribe((result: PaginatorA4G<Array<GruppoColtureDto>>) => {
        this.listaGruppi = result;
        if (this.listaGruppi != null) {
          this.totalElements = this.listaGruppi.count;
        }
      }, error => this.errorService.showError(error, 'tst-gruppi-colture'));
  }

  private initPaginator() {
    this.first = 0;
    this.totalElements = 0;
    this.numeroPagina = 0;
    this.elementiPerPagina = 10;
    this.defaultPropertySort = 'id_gruppo_lavorazione';
  }

  private setCols() {
    this.cols = [
      { field: 'codiceSuolo', header: 'Suolo' },
      { field: 'codiceDestUso', header: 'Destinazione uso' },
      { field: 'codiceUso', header: 'Uso' },
      { field: 'codiceQualita', header: 'Qualità' },
      { field: 'codiceVarieta', header: 'Varietà' },
      { field: 'gruppoLavorazione', header: 'Gruppo lavorazione' },
      { field: 'annoInizio', header: 'Anno inizio' },
      { field: 'annoFine', header: 'Anno fine' }
    ];
  }

  private buildPaginatorEvent(): PaginatorEvent {
    return {
      filters: {},
      first: 0,
      globalFilter: null,
      multiSortMeta: undefined,
      rows: this.elementiPerPagina,
      sortField: undefined || this.defaultPropertySort,
      sortOrder: 1 // SortDirection.ASC;
    };
  }

  public changePage(event: PaginatorEvent) {
    this.getGruppi(event);
  }

}