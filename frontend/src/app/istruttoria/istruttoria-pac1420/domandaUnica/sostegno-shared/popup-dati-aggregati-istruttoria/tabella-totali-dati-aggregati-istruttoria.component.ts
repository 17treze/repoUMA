import { Component, Input } from "@angular/core";
import { DatiAggregatiIstruttoriaTotaliModel } from "./models/dati-aggregati-istruttoria.model";
import jsPDF from 'jspdf'
import autoTable from 'jspdf-autotable'

@Component({
    selector: 'app-tabella-totali-dati-aggregati-istruttoria',
    templateUrl: 'tabella-totali-dati-aggregati-istruttoria.component.html'
  })
  export class TabellaTotaliDatiAggregatiIstruttoriaComponent {
    
    public tableModel: any[];
    public columnsModel: any[];
    
    @Input('datiTotali') public set setDatiTotali(datiTotali: DatiAggregatiIstruttoriaTotaliModel) {
      
      this.tableModel = [{
        pagamentoAutorizzato: datiTotali.pagamentoAutorizzato,
        totaleCalcolato: datiTotali.totaleCalcolato,
        percentualePagamento: datiTotali.percentualePagamento
      }];
    };

    constructor() {
      this.columnsModel = [
        'IstruttorieDatiAggregati.totalePagamentoAutorizzatoDisaccoppiato',
        'IstruttorieDatiAggregati.totaleCalcolatoDisaccoppiato',
        'IstruttorieDatiAggregati.percentualePagamentoDisaccoppiato'
      ];
    }

    public exportPdf(pTableElement) {
      const doc = new jsPDF();
      autoTable(doc, { html: pTableElement.tableViewChild.nativeElement });
      doc.save('tabella-dati-aggregati-istruttoria.pdf');
    }
  }