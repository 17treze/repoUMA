import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import jsPDF from 'jspdf'
import autoTable from 'jspdf-autotable'

import { StatisticaZootecniaDto } from 'src/app/istruttoria/istruttoria-pac1420/domanda-integrativa/classi/StatisticaZootecniaDto';
import { DomandaIntegrativaService } from 'src/app/istruttoria/istruttoria-pac1420/domanda-integrativa/domanda-integrativa.service';

@Component({
  selector: 'app-popup-dati-aggregati-zootecnia',
  templateUrl: './popup-dati-aggregati-zootecnia.component.html',
  styleUrls: ['./popup-dati-aggregati-zootecnia.component.scss']
})
export class PopupDatiAggregatiZootecniaComponent implements OnInit {

  public cols: any[];
  public datiAggregati: StatisticaZootecniaDto[];

  @Input() popupVisibile: boolean;
  @Output() chiudiPopup = new EventEmitter();

  constructor(private route: ActivatedRoute,
    private domandaIntegrativaService: DomandaIntegrativaService) {
    this.cols = [
      { field: 'codiceAgea', header: 'Codice intervento AGEA' },
      { field: 'descrizioneBreve', header: 'Descrizione intervento' },
      { field: '', header: 'Numero Capi Richiesti' },
      { field: '', header: 'Di cui ammissibili' },
      { field: '', header: 'Di cui ammissibili con sanzione' }
    ];
  }

  public exportPdf(pTableElement) {
    const doc = new jsPDF();
    autoTable(doc, { html: pTableElement.tableViewChild.nativeElement });
    doc.save('tabella-istruttoria-accoppiato-zootecnia.pdf');
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      let annoCampagna: number = Number(params['annoCampagna']);
      this.domandaIntegrativaService.getStatisticheZootecnia(annoCampagna).subscribe(result => {
        this.datiAggregati = result;
      });
    });
  }

  closePopup() {
    this.chiudiPopup.emit();
  }
}
