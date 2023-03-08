import { Labels } from "src/app/app.labels";
import { Component, OnInit } from '@angular/core';
import * as FileSaver from "file-saver";
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { DatiDomandaIbanErrato } from '../../istruttoria/istruttoria-pac1420/domandaUnica/classi/DatiDomandaIbanErrato';
import { IstruttoriaService } from '../../istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';

@Component({
  selector: 'app-iban-errati-du',
  templateUrl: './iban-errati-du.component.html',
  styleUrls: ['./iban-errati-du.component.css']
})

export class IbanErratiDuComponent implements OnInit {
  public labels = Labels;
  cols: any[];
  intestazioni = Labels;
  datiDomanda: Array<DatiDomandaIbanErrato> = [];

  constructor(private istruttoria: IstruttoriaService, private messages: MessageService) {
  }

  ngOnInit() {
    this.cols = [
      { field: 'cuaa', header: this.intestazioni.cuaaSigla },
      { field: 'descrizioneImpresa', header: this.intestazioni.descrizioneImpresa },
      { field: 'tipoDomanda', header: this.intestazioni.tipoDomanda },
      { field: 'numeroDomanda', header: this.intestazioni.NUMERO_DOMANDA },
      { field: 'annoCampagna', header: this.intestazioni.campagna },
      { field: 'ibanDomanda', header: this.intestazioni.ibanDomanda },
      { field: 'stato', header: this.intestazioni.stato }
    ];
    this.getDomande();
  }

  private getDomande() {
    this.istruttoria.getDomandeIbanErrato()
      .subscribe(result => {
        if (result) {
          result.forEach(element => {
            this.datiDomanda.push({ cuaa: element.cuaa, descrizioneImpresa: element.descrizioneImpresa, ibanDomanda: element.ibanDomanda, numeroDomanda: element.numeroDomanda, tipoDomanda: element.tipoDomanda, annoCampagna: element.annoCampagna, ibanValido: element.ibanValido });
          })
        }
      })
  }

  downloadDomandeIbanErrato(data) {
    if (data.length > 0) {
      const replacer = (key, value) => value === null ? '' : value;
      const header = Object.keys(data[0]);
      let csv = data.map(row => header.map(fieldName => JSON.stringify(row[fieldName], replacer)).join(','));
      csv.unshift(header.join(','));
      let csvArray = csv.join('\r\n');
      var blob = new Blob([csvArray], { type: 'text/csv' })
      FileSaver.saveAs(blob, "domandeNonLiquidabili.csv");
    }
    else
      this.messages.add(A4gMessages.getToast('tst-csv', A4gSeverityMessage.warn, A4gMessages.ELENCO_VUOTO))
  }

}
