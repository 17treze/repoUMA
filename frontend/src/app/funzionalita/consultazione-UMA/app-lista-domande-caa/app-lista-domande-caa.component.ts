import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Dropdown, SelectItem, Table } from 'primeng-lts';
import { TipoIntestazioneUma } from 'src/app/a4g-common/classi/enums/uma/TipoDocumentoUma.enum';
import { PaginatorEvent } from 'src/app/a4g-common/interfaces/paginator.model';
import { TipologicheService } from 'src/app/a4g-common/services/tipologiche.service';
import { Labels } from 'src/app/app.labels';
import { ConsultazioneUMA } from 'src/app/uma/core-uma/models/dto/ConsultazioneUMA';

@Component({
  selector: 'app-lista-domande-caa',
  templateUrl: './app-lista-domande-caa.component.html',
  styleUrls: ['./app-lista-domande-caa.component.scss']
})
export class ListaDomandeCaaComponent implements OnInit {

  @ViewChild('table', { static: true }) table: Table;
  @ViewChild('denominazioneDomandaFilter', { static: false }) denominazioneFilter: ElementRef;
  @ViewChild('cuaaDomandaFilter', { static: false }) cuaaFilter: ElementRef;
  @ViewChild('statoDomandaFilter', { static: false }) statoFilter: ElementRef;
  @ViewChild('tipiDomandaFilter', { static: false }) tipiFilter: ElementRef;
  @ViewChild('nrDomandaFilter', { static: false }) nrDomandaFilter: ElementRef;

  @Input() listaDomande: Array<ConsultazioneUMA>;
  @Input() nrDomande: number;
  @Input() elementiPerPagina: number;
  @Input() reset: boolean;
  @Output() aggiornaLista = new EventEmitter<any>();

  numeroDomandeTotali: number;
  header: string;
  footer: string;
  cols: any[];
  intestazioni = Labels;
  stati: Array<SelectItem>;
  tipiDomande: Array<SelectItem>;
  consultazioneAsCaa: boolean; /** true, se sono nella pagina come caa */

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private tipologicheService: TipologicheService
  ) { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes && changes.reset && changes.reset.currentValue) {
      this.table.reset();
      // resetto anche i filtri di ricerca
      this.denominazioneFilter && this.denominazioneFilter.nativeElement ? this.denominazioneFilter.nativeElement.value = null : null;
      this.cuaaFilter && this.cuaaFilter.nativeElement ? this.cuaaFilter.nativeElement.value = null : null;
      this.nrDomandaFilter && this.nrDomandaFilter.nativeElement ? this.nrDomandaFilter.nativeElement.value = null : null;
      (this.statoFilter as any).selectedOption = { label: "Tutti gli stati", value: null };
      (this.tipiFilter as any).selectedOption = { label: "Tutte le domande", value: null };
    }
  }

  ngOnInit() {
    this.consultazioneAsCaa = false;
    if (this.router.url && this.router.url.indexOf('funzioniCaa') > -1) {
      this.consultazioneAsCaa = true;
    }
    // costruzione lista tipiDomande
    this.tipiDomande = [
      { label: 'RICHIESTA', value: 'RICHIESTA' },
      { label: 'RETTIFICA', value: 'RETTIFICA' },
      { label: 'DICHIARAZIONE CONSUMI', value: 'DICHIARAZIONE CONSUMI' }
    ];
    this.tipiDomande.unshift({ label: 'Tutte le domande', value: null });

    // costruzione lista stati
    this.stati = this.tipologicheService.statiRichiestaEDichiarazioneUma.slice();
    this.stati.unshift({ label: 'Tutti gli stati', value: null });

    this.header = "Richieste/Rettifiche/Dichiarazioni";
    this.footer = "richiesta/rettifica/dichiarazione";

    this.cols = [
      { field: 'anno', header: this.intestazioni.anno, sortable: false, fieldSort: '', width: "6%" },
      { field: 'nrDomanda', header: this.intestazioni.domanda, sortable: true, fieldSort: 'id', width: "13%" },
      { field: 'tipo', header: this.intestazioni.tipo, sortable: true, fieldSort: 'tipo', width: "10%" },
      { field: 'stato', header: this.intestazioni.stato, sortable: true, fieldSort: 'stato', width: "10%" },
      { field: 'cuaa', header: this.intestazioni.cuaaSigla, sortable: true, fieldSort: 'cuaa', width: "14%" },
      { field: 'denominazione', header: this.intestazioni.denominazioneImpresa, sortable: true, fieldSort: 'denominazione', width: "16%" },
      { field: 'protocollo', header: this.intestazioni.nrProtocollo, sortable: false, fieldSort: '', width: "30%" },
      { field: '', header: 'Visualizza', sortable: false, fieldSort: '', width: "2%" }
    ];
  }

  ngOnDestroy(): void {
  }

  visualizzaDomanda(domanda: ConsultazioneUMA) {
    if (domanda.tipo == TipoIntestazioneUma.RICHIESTA) {
      this.visualizzaRichiesta(domanda.nrDomanda);
    } else if (domanda.tipo.split(' ').join('_') == TipoIntestazioneUma.DICHIARAZIONE_CONSUMI) {
      this.visualizzaDichiarazione(domanda.nrDomanda);
    } else if (domanda.tipo == TipoIntestazioneUma.RETTIFICA) {
      this.visualizzaRettifiche(domanda.nrDomanda);
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
      tipo: 'FULL'
    });
  }
}
