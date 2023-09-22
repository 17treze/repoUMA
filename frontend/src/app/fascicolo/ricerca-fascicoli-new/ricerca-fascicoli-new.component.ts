import { PaginatorService } from './../../a4g-common/services/paginator.service';
import { PaginatorA4G, PaginatorEvent } from './../../a4g-common/interfaces/paginator.model';
import { Component, OnInit, OnDestroy, ViewChild, ViewEncapsulation } from '@angular/core';
import { Labels } from 'src/app/app.labels';
import { A4gPagedTableComponent } from 'src/app/a4g-common/a4g-paged-table/a4g-paged-table.component';
import { FascicoloDTO } from '../shared/fascicolo.model';
import { Paginazione, SortDirection } from 'src/app/a4g-common/utility/paginazione';
import { ActivatedRoute, Router } from '@angular/router';
import { AnagraficaFascicoloService } from '../creazione-fascicolo/anagrafica-fascicolo.service';

@Component({
  selector: 'app-ricerca-fascicoli-new',
  templateUrl: './ricerca-fascicoli-new.component.html',
  styleUrls: ['./ricerca-fascicoli-new.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RicercaFascicoliNewComponent implements OnInit, OnDestroy {
  @ViewChild('table') tabella: A4gPagedTableComponent;

  cols: any[];
  elementiPerPagina: number;
  elementiTotali: number;
  fascicoliList: Array<FascicoloDTO>; // risultati ricerca
  fascicoliListSelezionati: Array<FascicoloDTO>; // selezione tabella
  isSelectedAll: boolean;
  intestazioni = Labels;
  isEmpty: boolean;

  constructor(
    private anagraficaFascicoloService: AnagraficaFascicoloService,
    private paginatorService: PaginatorService,
    private route: ActivatedRoute,
    private router: Router) {
  }

  ngOnInit() {
    this.elementiTotali = this.intestazioni.defaultPageSizePaginator;
    this.elementiPerPagina = this.intestazioni.defaultStartIndexPaginator;
    this.cols = this.defineTableColumns();
  }

  ngOnDestroy() {
  }

  loadData(event: PaginatorEvent) {
    if (event.rows) {
      this.elementiPerPagina = event.rows;
    }
    this.loadList(event);
  }

  loadList(event: PaginatorEvent) {
    let sortOrder;
    const sortBy: string = event.sortField || 'id';
    if (event.sortField) {
      sortOrder = event.sortOrder === 1 ? SortDirection.ASC : SortDirection.DESC;
    }
    
    const pagination: Paginazione = this.paginatorService.getPagination(Math.round(event.first / this.elementiPerPagina), this.elementiPerPagina, sortBy, sortOrder || SortDirection.ASC);
    this.anagraficaFascicoloService.getAnagraficaFascicolo(
      null,
      pagination).subscribe((res: PaginatorA4G<Array<FascicoloDTO>>) => {
        this.elementiTotali = res.count;
        this.fascicoliList = res.risultati;
        this.isEmpty = res.risultati && res.risultati.length ? false : true;
      }, err => {
        console.log('error:', err);
        this.isEmpty = true;
      }
      );
  }

  public onSearch(fascicoli: PaginatorA4G<Array<FascicoloDTO>>) {
    this.elementiTotali = fascicoli.count;
    this.fascicoliList = fascicoli.risultati;
  }

  visualizzaDettaglioFascicolo(selection: FascicoloDTO) {
    this.router.navigate([`./cuaa/${selection.codiCuaa}/dettaglio`], { relativeTo: this.route});
  }

  defineTableColumns() {
    return [
      {
        field: 'cuaa',
        header: Labels.cuaaSigla
      },
      {
        field: 'denominazione',
        header: Labels.denominazioneImpresa
      },
      {
        field: null,
        header: Labels.sportello
      },
      {
        field: 'stato',
        header: Labels.stato
      }
    ];
  }

}
