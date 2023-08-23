import { Component, OnInit, Input, Output, EventEmitter, ViewChild, OnChanges, SimpleChanges } from '@angular/core';
import * as _ from 'lodash';
import { ConsultazioneUMA } from 'src/app/uma/core-uma/models/dto/ConsultazioneUMA';
import { Labels } from "src/app/app.labels";
import { ActivatedRoute, Router } from "@angular/router";
import { TipoIntestazioneUma } from 'src/app/a4g-common/classi/enums/uma/TipoDocumentoUma.enum';
import { PaginatorEvent } from 'src/app/a4g-common/interfaces/paginator.model';
import { Table } from 'primeng/table';
import { FascicoloCorrente } from 'src/app/fascicolo/fascicoloCorrente';
import { A4gMultiTableService } from 'src/app/a4g-common/a4g-multi-table/a4g-multi-table.service';

@Component({
  selector: 'app-lista-rettifiche-domande',
  templateUrl: './app-lista-rettifiche-domande.component.html',
  styleUrls: ['./app-lista-rettifiche-domande.component.scss']
})
export class ListaRettificheDomandeComponent implements OnInit, OnChanges {

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

  constructor(
    private fascicoloCorrente: FascicoloCorrente,
    private route: ActivatedRoute,
    private router: Router,
    public a4gMultiTableService: A4gMultiTableService
  ) { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes &&
      changes.reset &&
      changes.reset.currentValue) {
      this.table.reset();
    }
  }

  ngOnInit() {
    if (this.tipoDomande == TipoIntestazioneUma.DICHIARAZIONE_CONSUMI) { this.header = "Dichiarazioni"; this.footer = "dichiarazione"; }
    if (this.tipoDomande == TipoIntestazioneUma.RICHIESTA) { this.header = "Richieste/Rettifiche di carburante"; this.footer = "richiesta/rettifica di carburante"; }

    this.cols = [
      { field: 'nrDomanda', header: this.intestazioni.idDichRettifica, sortable: false, fieldSort: 'nrDomanda', width: "6%" },
      { field: 'dataPresentazione', header: this.intestazioni.dataPresentazione, sortable: true, fieldSort: 'dataPresentazione', width: "20%" },
      { field: 'anno', header: this.intestazioni.anno, sortable: false, fieldSort: 'anno', width: "8%" },
      // { field: 'tipo', header: this.intestazioni.tipo, sortable: false },
      { field: 'stato', header: this.intestazioni.stato, sortable: false, fieldSort: 'stato', width: "14%" },
      { field: 'cuaa', header: this.intestazioni.cuaaSigla, sortable: false, fieldSort: 'cuaa', width: "12%" },
      // { field: 'denominazione', header: this.intestazioni.denominazioneImpresa, sortable: true, fieldSort: 'denominazione', width: "15%" },
      { field: 'protocollo', header: this.intestazioni.nrProtocollo, sortable: false, fieldSort: 'protocollo', width: "30%" },
      { field: '', header: 'Modifica', sortable: false, fieldSort: '', width: "5%" },
      { field: '', header: 'Visualizza', sortable: false, fieldSort: '', width: "5%" }
    ];
  }

  visualizzaDomanda(nrDomanda: number, readonly: boolean) {
    if (this.tipoDomande == TipoIntestazioneUma.RICHIESTA) {
      this.visualizzaRichiesta(nrDomanda);
    }
    if (this.tipoDomande == TipoIntestazioneUma.RETTIFICA) {
      this.visualizzaRichiesta(nrDomanda);
    }
  }

  rettificaDomanda(nrDomanda: number) {

  }

  visualizzaRichiesta(nrDomanda: number) {
    this.router.navigate([`uma/${this.fascicoloCorrente.fascicoloLazio.data.codiCuaa}/richiesta/${nrDomanda}/indice`]);
  }


  changePage(event: PaginatorEvent) {
    this.aggiornaLista.emit({
      paginator: event,
      tipo: this.tipoDomande
    });
  }
}
