import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { KeyValue } from 'src/app/a4g-common/classi/KeyValue';
import { InformazioniDu } from '../../../domain/informazioniDU';

@Component({
  selector: 'app-informazioni-domanda',
  templateUrl: './informazioni-domanda.component.html',
  styleUrls: ['./informazioni-domanda.component.css']
})
export class InformazioniDomandaComponent implements OnInit {

  cols: any[];
  idDomanda: number;
  dati: Array<KeyValue> = new Array<KeyValue>();

  constructor(
    private route: ActivatedRoute,
    private istruttoriaService: IstruttoriaDettaglioService,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.setCols();
    this.getIdDomanda();
    this.getDettaglio();
  }

  private setCols() {
    this.cols = [
      { field: 'a', header: '', width: '80%' },
      { field: 'b', header: '', width: '20%' }
    ];
  }

  private getIdDomanda() {
    this.idDomanda = this.route.snapshot.data['domandaIstruttoria'].domanda.id;
  }

  private getDettaglio() {
    if (this.idDomanda) {
      this.istruttoriaService.getInformazioniDU(this.idDomanda.toString())
        .subscribe(
          resp => {
            this.getUserPresentation(resp);
          },
          err => {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
          }
        )
    }
  }

  private getUserPresentation(resp: InformazioniDu) {
    let array: KeyValue[] = [];
    Object.keys(resp).map(function (key) {
      let userPresentation: KeyValue = new KeyValue();
      userPresentation.mkey = key.toUpperCase();
      userPresentation.mvalue = resp[key] === true ? 'SI' : resp[key] === false ? 'NO' : resp[key];
      if (resp[key]===null){userPresentation.mvalue='NON RICHIESTO'}
      array.push(userPresentation);
      return array;
    });
    this.dati = array;
  }

}
