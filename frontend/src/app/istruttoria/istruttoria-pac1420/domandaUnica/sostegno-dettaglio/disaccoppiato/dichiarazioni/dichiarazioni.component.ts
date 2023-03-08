import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DichiarazioneDu } from '../../../domain/dichiarazioneDu';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';

@Component({
  selector: 'app-dichiarazioni',
  templateUrl: './dichiarazioni.component.html',
  styleUrls: ['./dichiarazioni.component.css']
})
export class DichiarazioniComponent implements OnInit {

  cols: any[];
  idDomanda: number;
  dichiarazioni: Array<DichiarazioneDu>;
  codiciTipo: String[];

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
      { field: 'a', header: '', width: '90%' },
      { field: 'b', header: '', width: '10%' }
    ];
  }

  private getIdDomanda() {
    this.idDomanda = this.route.snapshot.data['domandaIstruttoria'].domanda.id;
  }

  private getDettaglio() {
    if (this.idDomanda) {
      this.istruttoriaService.getDichiarazioniDU(this.idDomanda.toString())
        .subscribe(
          resp => {
            this.dichiarazioni = resp;
            this.codiciTipo = this.dichiarazioni.map(x => x.quadro).filter((v, i, a) => a.indexOf(v) === i);
          },
          err => {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
          }
        )
    }
  }

}
