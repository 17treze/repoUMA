import { Component, OnInit } from '@angular/core';
import { SelectItem, MessageService } from 'primeng-lts';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { GruppoLavorazioneDto, LavorazioneDto } from 'src/app/uma/core-uma/models/dto/ConfigurazioneDto';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { EMPTY, of, Subscription } from 'rxjs';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { HttpClientConfigurazioneUmaService } from 'src/app/uma/core-uma/services/http-client-configurazione-uma.service';
import { PaginatorA4G, PaginatorEvent } from 'src/app/a4g-common/interfaces/paginator.model';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { Paginazione } from 'src/app/a4g-common/utility/paginazione';
import { catchError, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-gruppi-lavorazione',
  templateUrl: './gruppi-lavorazione.component.html',
  styleUrls: ['./gruppi-lavorazione.component.css']
})
export class GruppiLavorazioneComponent implements OnInit {

  clonedGruppiLavorazione: { [s: string]: GruppoLavorazioneDto; } = {};

  gruppiLavorazione: PaginatorA4G<Array<GruppoLavorazioneDto>>;
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

  constructor(private messageService: MessageService,
    private errorService: ErrorService,
    private httpClientConfigurazioneUmaService: HttpClientConfigurazioneUmaService
  ) { }

  ngOnInit() {
    this.setCols();
    this.display = true;
    this.gruppiLavorazione = {} as PaginatorA4G<Array<GruppoLavorazioneDto>>;
    this.gruppiLavorazione.count = 0;
    this.gruppiLavorazione.risultati = [];
    this.initPaginator();
    this.getGruppiLavorazioni(this.buildPaginatorEvent());
  }

  private setCols() {
    this.cols = [
      { field: 'indice', header: 'Indice' },
      { field: 'nome', header: 'Nome' },
      { field: 'ambito', header: 'Ambito' },
      { field: 'annoInizio', header: 'Anno inizio' },
      { field: 'annoFine', header: 'Anno fine' }
    ];
  }

  onRowEditInit(gruppoLavorazione: GruppoLavorazioneDto) {
    this.clonedGruppiLavorazione[gruppoLavorazione.id] = { ...gruppoLavorazione };
  }

  onRowEditSave(gruppoLavorazione: GruppoLavorazioneDto) {
    if (this.canSave(gruppoLavorazione)) {
      // TODO: aggiungere chiamata per salvataggio
      delete this.clonedGruppiLavorazione[gruppoLavorazione.id];
      this.messageService.add(A4gMessages.getToast('tst-gruppi-lav', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOK));
    }
    else {
      this.messageService.add(A4gMessages.getToast('tst-gruppi-lav', A4gSeverityMessage.warn, UMA_MESSAGES.mandatoryAll));
    }
  }

  onRowEditCancel(gruppoLavorazione: GruppoLavorazioneDto, index: number) {
    this.gruppiLavorazione[index] = this.clonedGruppiLavorazione[gruppoLavorazione.id];
    delete this.clonedGruppiLavorazione[gruppoLavorazione.id];
  }

  canSave(lavorazione: GruppoLavorazioneDto) {
    return lavorazione.ambito && lavorazione.annoFine && lavorazione.annoInizio && lavorazione.indice && lavorazione.nome;
  }

  private getGruppiLavorazioni(event: PaginatorEvent) {
    this.numeroPagina = Math.floor(event.first / this.elementiPerPagina);
    if (this.numeroPagina == null) {
      this.numeroPagina = 0;
    }
    this.paginazione = Paginazione.of(this.numeroPagina, this.elementiPerPagina, event.sortField, Paginazione.getOrdine(event.sortOrder));
    this.paginazione.numeroElementiPagina = this.elementiPerPagina;

    this.getPagedGruppiSubscription = this.httpClientConfigurazioneUmaService.getGruppiLavorazioni(this.paginazione)
      .pipe(switchMap((res: PaginatorA4G<Array<GruppoLavorazioneDto>>) => {
        return of(res);
      }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err, 'tst-gruppi-lav');
          return EMPTY;
        })
      )
      .subscribe((result: PaginatorA4G<Array<GruppoLavorazioneDto>>) => {
        this.gruppiLavorazione = result;
        if (this.gruppiLavorazione != null) {
          this.totalElements = this.gruppiLavorazione.count;
        }
      }, error => this.errorService.showError(error, 'tst-gruppi-lav'));
  }

  private initPaginator() {
    this.first = 0;
    this.totalElements = 0;
    this.numeroPagina = 0;
    this.elementiPerPagina = 10;
    this.defaultPropertySort = 'ambito';
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
    this.getGruppiLavorazioni(event);
  }
}
