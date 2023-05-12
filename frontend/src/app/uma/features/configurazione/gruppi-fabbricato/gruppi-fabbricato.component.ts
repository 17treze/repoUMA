import { Component, OnInit } from '@angular/core';
import { EMPTY, of } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { PaginatorA4G, PaginatorEvent } from 'src/app/a4g-common/interfaces/paginator.model';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { Paginazione, SortDirection } from 'src/app/a4g-common/utility/paginazione';
import { GruppoFabbricatoDto } from 'src/app/uma/core-uma/models/dto/ConfigurazioneDto';
import { HttpClientConfigurazioneUmaService } from 'src/app/uma/core-uma/services/http-client-configurazione-uma.service';

@Component({
  selector: 'app-gruppi-fabbricato',
  templateUrl: './gruppi-fabbricato.component.html',
  styleUrls: ['./gruppi-fabbricato.component.css']
})
export class GruppiFabbricatoComponent implements OnInit {

  gruppiFabbricato: PaginatorA4G<Array<GruppoFabbricatoDto>>;
  cols: any;
  elementiPagina = 10;
  sortBy: string = 'codiceFabbricato';
  sortDirection: SortDirection;

  constructor(
    private httpClientConfigurazioneUmaService: HttpClientConfigurazioneUmaService,
    private errorService: ErrorService
  ) { }

  ngOnInit() {
    this.setCols();
    this.gruppiFabbricato = {} as PaginatorA4G<Array<GruppoFabbricatoDto>>;
    this.gruppiFabbricato.count = 0;
    this.gruppiFabbricato.risultati = [];
  }

  private setCols() {
    this.cols = [
      { field: 'codiceFabbricato', header: 'Codice fabbricato' },
      { field: 'tipoFabbricato', header: 'Tipo Fabbricato' },
      { field: 'gruppoLavorazione', header: 'Gruppo lavorazione' }
    ];
  }

  changePage(event: PaginatorEvent) {
    if (event != null) {
      this.sortDirection = event.sortOrder === 1 ? SortDirection.ASC : SortDirection.DESC;
      this.sortBy = event.sortField || this.sortBy;
    }
    let paginazione: Paginazione = Paginazione.of(
      0, this.elementiPagina, this.sortBy, this.sortDirection || SortDirection.ASC
    );

    this.httpClientConfigurazioneUmaService.getGruppiFabbricato(paginazione)
      .pipe(switchMap((res: PaginatorA4G<Array<GruppoFabbricatoDto>>) => {
        return of(res);
      }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err, 'tst-gruppi-lav');
          return EMPTY;
        })
      )
      .subscribe((result: PaginatorA4G<Array<GruppoFabbricatoDto>>) => {
        this.gruppiFabbricato = result;
      }, error => this.errorService.showError(error, 'tst-gruppi-lav'));
  }


}
