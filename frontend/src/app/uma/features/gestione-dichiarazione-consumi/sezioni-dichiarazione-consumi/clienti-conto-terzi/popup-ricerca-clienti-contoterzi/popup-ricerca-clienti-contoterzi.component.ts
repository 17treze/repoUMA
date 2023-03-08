import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { PaginatorA4G, PaginatorEvent } from './../../../../../../a4g-common/interfaces/paginator.model';
import { HttpClientClienteUmaService } from 'src/app/uma/core-uma/services/http-client-cliente-uma.service';
import { Component, EventEmitter, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Table } from 'primeng/table';
import { Dialog } from 'primeng/dialog';
import { MessageService } from 'primeng/api';
import { FascicoloService } from 'src/app/fascicolo/fascicolo.service';
import { EMPTY, of, Subscription } from 'rxjs';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { Paginazione } from 'src/app/a4g-common/utility/paginazione';
import { ClienteDto } from 'src/app/uma/core-uma/models/dto/ClienteDto';
import { catchError, switchMap } from 'rxjs/operators';
import { RisultatiRicercaClientiDto } from 'src/app/uma/core-uma/models/dto/RisultatiRicercaClientiDto';

@Component({
  selector: 'app-popup-ricerca-clienti-contoterzi',
  templateUrl: './popup-ricerca-clienti-contoterzi.component.html',
  styleUrls: ['./popup-ricerca-clienti-contoterzi.component.scss']
})
export class PopupRicercaClientiContoterziComponent implements OnInit, OnDestroy {
  datasource: PaginatorA4G<Array<RisultatiRicercaClientiDto>>;
  display: boolean;
  idDichiarazione: number;
  testoRicerca: string;
  numeroPagina: number;
  elementiPerPagina: number;
  totalElements: number;
  first: number;
  paginazione: Paginazione;
  defaultPropertySort: string;
  clienteSelezionato: ClienteDto = {} as ClienteDto;

  @ViewChild('dialog', { static: true })
  dialogElement: Dialog;
  @ViewChild('table') table: Table;
  @Output() chiudiPopup = new EventEmitter<any>();

  postClienteSubscription: Subscription;
  routerSubscription: Subscription;
  pagedListSubscription: Subscription;
  validaClientiSubscription: Subscription;

  constructor(
    private messageService: MessageService,
    private route: ActivatedRoute,
    private fascicoloService: FascicoloService,
    private httpClientClienteUmaService: HttpClientClienteUmaService,
    private errorService: ErrorService
  ) { }

  ngOnInit() {
    this.routerSubscription = this.route.params
      .subscribe(params => {
        this.idDichiarazione = params['id'];
      });
  }

  ngOnDestroy() {
    if (this.postClienteSubscription) {
      this.postClienteSubscription.unsubscribe();
    }
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
    if (this.pagedListSubscription) {
      this.pagedListSubscription.unsubscribe();
    }
    if (this.validaClientiSubscription) {
      this.validaClientiSubscription.unsubscribe();
    }
  }

  open() {
    this.display = true;
    this.datasource = {} as PaginatorA4G<Array<RisultatiRicercaClientiDto>>;
    this.datasource.count = 0;
    this.datasource.risultati = [];
    this.testoRicerca = null;
    this.initPaginator();
  }

  onHideDialog() {
  }

  onCloseToastRicercaClienti() {
  }

  chiudiDialog($event?: Event) {
    this.display = false;
    this.dialogElement._style.width = 'unset'; // reset width dialog
  }

  onClickButton(tipo: String, valore: string, id: number) {
    switch (tipo) {
      case "CERCA": {
        // richiamo servizio cerca clienti e popola tabella
        this.reset();
        this.cercaClienti(valore, this.buildPaginatorEvent());
        break;
      }
      case "AGGIUNGI": {
        // richiamo servizio put e chiudo popup
        this.validaClientiSubscription = this.httpClientClienteUmaService.validaClientiContoterzi(this.idDichiarazione, id).subscribe(() => {
          this.clienteSelezionato.cuaa = valore;
          this.clienteSelezionato.idFascicolo = id;
          this.addCliente(this.clienteSelezionato);
        }, error => this.errorService.showError(error, 'tst-ricerca-clienti'));
        break;
      }
      default: {
        break;
      }
    }
  }

  public changePage(event: PaginatorEvent, testoDaCercare: string) {
    this.cercaClienti(testoDaCercare, event);
  }

  reset() {
    if (this.table) {
      this.table.reset();
    }
  }

  cercaClienti(testoDaCercare: string, event: PaginatorEvent) {
    this.numeroPagina = Math.floor(event.first / this.elementiPerPagina);
    if (this.numeroPagina == null) {
      this.numeroPagina = 0;
    }
    this.paginazione = Paginazione.of(this.numeroPagina, this.elementiPerPagina, event.sortField, Paginazione.getOrdine(event.sortOrder));
    this.paginazione.numeroElementiPagina = this.elementiPerPagina;

    this.pagedListSubscription = this.fascicoloService.getListaPaged(testoDaCercare, this.paginazione)
      .pipe(switchMap((res: PaginatorA4G<Array<RisultatiRicercaClientiDto>>) => {
        this.dialogElement.center();
        this.dialogElement._style.width = 1200;
        return of(res);
      }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err, 'tst-ricerca-clienti');
          return EMPTY;
        })
      )
      .subscribe((result: PaginatorA4G<Array<RisultatiRicercaClientiDto>>) => {
        this.datasource = result;
        if (this.datasource != null) {
          this.totalElements = this.datasource.count;
        }
      }, error => this.errorService.showError(error, 'tst-ricerca-clienti'));
  }

  isNotValidText(testoRicerca: string): boolean {
    if (testoRicerca && testoRicerca.length > 2) return false;
    return true;
  }

  addCliente(clienteSelezionato: ClienteDto) {
    this.chiudiDialog();
    this.chiudiPopup.emit(clienteSelezionato);
  }

  private initPaginator() {
    this.first = 0;
    this.totalElements = 0;
    this.numeroPagina = 0;
    this.elementiPerPagina = 5;
    this.defaultPropertySort = 'denominazione';
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

}
