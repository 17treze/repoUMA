import { Component, OnInit, Input, Output, EventEmitter, ViewChild, OnChanges, SimpleChanges, OnDestroy } from '@angular/core';
import * as _ from 'lodash';
import { ConsultazioneUMA } from 'src/app/uma/core-uma/models/dto/ConsultazioneUMA';
import { Labels } from "src/app/app.labels";
import { ActivatedRoute, Router } from "@angular/router";
import { TipoIntestazioneUma } from 'src/app/a4g-common/classi/enums/uma/TipoDocumentoUma.enum';
import { PaginatorEvent } from 'src/app/a4g-common/interfaces/paginator.model';
import { Table } from 'primeng/table';

@Component({
  selector: 'app-lista-domande',
  templateUrl: './app-lista-domande.component.html',
  styleUrls: ['./app-lista-domande.component.scss']
})
export class ListaDomandeComponent implements OnInit, OnChanges, OnDestroy {

  @ViewChild('table', { static: true }) table: Table;

  @Input() listaDomande: Array<ConsultazioneUMA>;
  @Input() tipoDomande: TipoIntestazioneUma;
  @Input() nrDomande: number;
  @Input() reset: boolean;
  @Output() aggiornaLista = new EventEmitter<any>();

  elementiPerPagina: number = 5;
  header: string;
  footer: string;
  cols: any[];
  intestazioni = Labels;

  consultazioneAsCaa: boolean; /** true, se sono nella pagina come caa */

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes && changes.reset && changes.reset.currentValue) {
      setTimeout(() => { //  // evita l'AfterChecked Error visto che questo componente è invocato contemporaneamente sia dalla lista richieste che dalla lista consumi 
        this.table.reset();
      });
    }
  }

  ngOnInit() {
    if (this.tipoDomande == TipoIntestazioneUma.DICHIARAZIONE_CONSUMI) {
      this.header = "Dichiarazioni"; this.footer = "dichiarazione";
    }

    if (this.tipoDomande == TipoIntestazioneUma.RICHIESTA) {
      this.header = "Richieste/Rettifiche di carburante";
      this.footer = "richiesta/rettifica di carburante";
    }

    this.cols = [
      { field: 'nrDomanda', header: this.intestazioni.domanda, sortable: true, fieldSort: 'id', width: "12%" },
      { field: 'anno', header: this.intestazioni.anno, sortable: false, fieldSort: '', width: "9%" },
      { field: 'stato', header: this.intestazioni.stato, sortable: true, fieldSort: 'stato', width: "13%" },
      { field: 'cuaa', header: this.intestazioni.cuaaSigla, sortable: true, fieldSort: 'cuaa', width: "13%" },
      { field: 'denominazione', header: this.intestazioni.denominazioneImpresa, sortable: true, fieldSort: 'denominazione', width: "15%" },
      { field: 'protocollo', header: this.intestazioni.nrProtocollo, sortable: false, fieldSort: '', width: "35%" },
      { field: '', header: 'Visualizza', sortable: false, fieldSort: '', width: "3%" }
    ];
  }

  ngOnDestroy(): void {
  }

  visualizzaDomanda(nrDomanda: number) {
    if (this.tipoDomande == TipoIntestazioneUma.RICHIESTA) {
      this.visualizzaRichiesta(nrDomanda);
    }
    if (this.tipoDomande == TipoIntestazioneUma.DICHIARAZIONE_CONSUMI) {
      this.visualizzaDichiarazione(nrDomanda);
    }
    if (this.tipoDomande == TipoIntestazioneUma.RETTIFICA) {
      this.visualizzaRettifiche(nrDomanda);
    }
  }

  visualizzaDichiarazione(nrDomanda: number) {
    this.router.navigate([`./dichiarazione-consumi/${nrDomanda}/sezioni`], { relativeTo: this.route });
  }

  visualizzaRichiesta(nrDomanda: number) {
    this.router.navigate([`./richiesta/${nrDomanda}/indice`], { relativeTo: this.route });
  }

  visualizzaRettifiche(nrDomanda: number) {
    this.router.navigate([`./richiesta/${nrDomanda}/indice`], { relativeTo: this.route });
  }

  changePage(event: PaginatorEvent) {
    this.aggiornaLista.emit({
      paginator: event,
      tipo: this.tipoDomande
    });
  }
}
