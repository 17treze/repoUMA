import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import jsPDF from 'jspdf'
import autoTable from 'jspdf-autotable'

import { DomandaAccoppiatoSuperficieService } from 'src/app/istruttoria/istruttoria-pac1420/domanda-accopiato-superficie/domanda-accoppiato-superficie.service';
import { InterventoSuperficieDto } from 'src/app/istruttoria/istruttoria-pac1420/domanda-accopiato-superficie/classi/interventoSuperficieDto';

@Component({
  selector: 'app-popup-dati-aggregati-superficie-intervento',
  templateUrl: 'popup-dati-aggregati-superficie-intervento.component.html',
  styleUrls: ['popup-dati-aggregati-superficie-intervento.component.css']
})
export class PopupDatiAggregatiSuperficieInterventoComponent implements OnInit {

  public annoCampagna: number = undefined;
  public datiAggregati: Array<InterventoSuperficieDto> = [];
  public cols: any[];
  
  @Input() public popupVisibile = false;
  
  @Input('annoCampagna') public set setAnnoCampagna(val: number) {
    this.annoCampagna = val;
    this.domandaAccoppiatoSuperficieService.getTotaleSuperficiePerIntervento(val).subscribe(res => {
      let dati: Array<InterventoSuperficieDto> = [];
      if (!res) {
        return;
      }
      for (let riga of res) {
        let dato = new InterventoSuperficieDto();
        dato.intervento = this.translateService.instant('DescrizioneCodiceAgeaPerChiaveJson.' + riga.intervento);
        dato.superficie = riga.superficie;
        dati.push(dato);
      }
      this.datiAggregati = dati;
    });
  }

  @Output() public chiudiPopup = new EventEmitter();

  constructor(
    public domandaAccoppiatoSuperficieService: DomandaAccoppiatoSuperficieService,
    private translateService: TranslateService) {
    this.cols = [
      { field: 'intervento', header: 'SOSTEGNO.DESCRIZIONE_INTERVENTO' },
      { field: 'superficie', header: 'SOSTEGNO.TOTALE_SUPERFICIE_RICHIESTA' },
    ];
  }

  public exportPdf(pTableElement) {
    const doc = new jsPDF();
    autoTable(doc, { html: pTableElement.tableViewChild.nativeElement });
    doc.save('tabella-dati-aggregati-superfici-ammissibili.pdf');
  }

  ngOnInit() {
  }

  public closePopup() {
    this.chiudiPopup.emit();
  }
}
