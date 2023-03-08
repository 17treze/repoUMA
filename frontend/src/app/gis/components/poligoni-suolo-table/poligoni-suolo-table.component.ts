import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { CreazioneLavorazioneService } from '../../services/creazione-lavorazione.service';
import { saveAs } from 'file-saver/FileSaver';
import { PanelEvent } from '../../shared/PanelEvent';
@Component({
  selector: 'gis-poligoni-suolo-table',
  templateUrl: './poligoni-suolo-table.component.html',
  styleUrls: ['./poligoni-suolo-table.component.css'],
  encapsulation: ViewEncapsulation.None

})
export class PoligoniSuoloTableComponent implements OnInit {
  @Input() titoloDialog: string;
  @Input() tableValue: any;

  visible: boolean;
  suoloVigente: any;
  constructor(public panelEvent: PanelEvent) {

  }

  ngOnInit() {
    this.visible = this.panelEvent.showPoligoniSuoloTable;
  }

  ngOnChanges(): void {
    if (this.tableValue) {
      this.suoloVigente = this.tableValue;
      setTimeout(() => {
        // rimuove l'overlay di primeNg
        // if (document.querySelector(".ui-widget-overlay.ui-dialog-mask")){
        //   document.querySelector(".ui-widget-overlay.ui-dialog-mask").remove();
        // }
      }, 400);
    }
  }

  downloadFileCsv(data: any) {
    const replacer = (key, value) => value === null ? '' : value;
    const header = Object.keys(data[0]);
    let csv = data.map(row => header.map(fieldName => JSON.stringify(row[fieldName], replacer)).join(','));
    csv.unshift(header.join(','));
    let csvArray = csv.join('\r\n');

    var blob = new Blob([csvArray], {type: 'text/csv' })
    saveAs(blob, 'poligoniSuoloNonAssociati.csv');
  }

}
