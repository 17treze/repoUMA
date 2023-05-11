import { Component, OnInit, OnDestroy } from '@angular/core';
import { EMPTY, of, Subscription } from 'rxjs';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { HttpClientConfigurazioneUmaService } from 'src/app/uma/core-uma/services/http-client-configurazione-uma.service';
import { PaginatorA4G, PaginatorEvent } from 'src/app/a4g-common/interfaces/paginator.model';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { Paginazione, SortDirection } from 'src/app/a4g-common/utility/paginazione';
import { catchError, switchMap } from 'rxjs/operators';
import { GruppoColtureDto } from 'src/app/uma/core-uma/models/dto/ConfigurazioneDto';

@Component({
  selector: 'app-gruppi-colture',
  templateUrl: './gruppi-colture.component.html',
  styleUrls: ['./gruppi-colture.component.scss']
})

export class GruppiColtureComponent implements OnInit, OnDestroy {

  listaGruppi: PaginatorA4G<Array<GruppoColtureDto>>;
  totalElements: number;
  cols: any;
  displayDialog: boolean;
  newGruppoLavorazione: boolean;
  selectedGruppoLavorazione: GruppoColtureDto;
  elementiPagina = 10;
  sortBy: string = 'id_gruppo_lavorazione';
  sortDirection: SortDirection;

  // Subscriptions
  getPagedGruppiSubscription: Subscription;

  constructor(
    private errorService: ErrorService,
    private httpClientConfigurazioneUmaService: HttpClientConfigurazioneUmaService
  ) { }

  ngOnInit() {
    this.setCols();
    this.listaGruppi = {} as PaginatorA4G<Array<GruppoColtureDto>>;
    this.listaGruppi.count = 0;
    this.listaGruppi.risultati = [];
  }

  private setCols() {
    this.cols = [
      { field: 'id_gruppo_lavorazione', header: 'Gruppo lavorazione' },
      { field: 'codice_suolo', header: 'Suolo' },
      { field: 'codice_dest_uso', header: 'Destinazione uso' },
      { field: 'codice_uso', header: 'Uso' },
      { field: 'codice_qualita', header: 'Qualità' },
      { field: 'codice_varieta', header: 'Varietà' },
      { field: 'anno_inizio', header: 'Anno inizio' },
      { field: 'anno_fine', header: 'Anno fine' }
    ];
  }

  ngOnDestroy() {
    if (this.getPagedGruppiSubscription) {
      this.getPagedGruppiSubscription.unsubscribe();
    }
  }

  changePage(event: PaginatorEvent) {
    if (event != null) {
      this.sortDirection = event.sortOrder === 1 ? SortDirection.ASC : SortDirection.DESC;
      this.sortBy = event.sortField || this.sortBy;
    }
    let paginazione: Paginazione = Paginazione.of(
      event.first / this.elementiPagina, this.elementiPagina, this.sortBy, this.sortDirection || SortDirection.ASC
    );

    this.getPagedGruppiSubscription = this.httpClientConfigurazioneUmaService.getGruppiColture(paginazione)
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

}