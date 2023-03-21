import { Component, OnInit } from '@angular/core';
import { DomandeUnicheService } from '../../domande-uniche.service';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { MessageService } from 'primeng-lts';
import { ActivatedRoute } from '@angular/router';
import { Dati, EsitiIstruttoria, EsitiIstruttoriaView } from '../../models/esiti-istruttoria';
import { Istruttoria } from '../../models/istruttoria';

@Component({
  selector: 'app-esiti-istruttoria',
  templateUrl: './esiti-istruttoria.component.html',
  styleUrls: ['./esiti-istruttoria.component.css']
})
export class EsitiIstruttoriaComponent implements OnInit {

  public datiView: EsitiIstruttoriaView[] = [];
  public listaEsitiTmp: EsitiIstruttoria[] = [];
  public istruttoria: Istruttoria;
  private idIstruttoria: number = Number(this.route.snapshot.paramMap.get('idIstruttoria'));

  constructor(
    private service: DomandeUnicheService,
    private messageService: MessageService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.getIstruttoria(this.idIstruttoria);
    this.setObject();
    this.caricaDatiDettaglio(this.idIstruttoria);
  }

  public scroll(el: HTMLElement) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start', inline: 'nearest' });
  }

  private getIstruttoria(idIstruttoria) {
    this.service.getIstruttoria(idIstruttoria).subscribe(
      result => {
        this.istruttoria = result;
        console.log(this.istruttoria)
      }, error => {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDatiIstruttoria));
      }
    );
  }

  private setObject() {
    this.datiView = [
      new EsitiIstruttoriaView('AMMISSIBILITA', [], []),
      new EsitiIstruttoriaView('RIDUZIONI_BPS', [], []),
      new EsitiIstruttoriaView('SANZIONI_BPS', [], []),
      new EsitiIstruttoriaView('GREENING', [], []),
      new EsitiIstruttoriaView('GIOVANE_AGRICOLTORE', [], []),
      new EsitiIstruttoriaView('RIEPILOGO_SANZIONI', [], []),
      new EsitiIstruttoriaView('CONTROLLI_FINALI', [], []),
      new EsitiIstruttoriaView('DISCIPLINA_FINANZIARIA', [], [])
    ]
  }

  private caricaDatiDettaglio(idIstruttoria) {
    const doNotShowList: string[] = ['ISCAMP', 'DOMSIGECOCHIUSA', 'BPSSUPSIGECO', 'AZCMPBOV', 'AZCMPOVI'];
    this.service.getEsitiIstruttoria(idIstruttoria).subscribe(result => {
      if (!result) { return; }
      this.listaEsitiTmp = result
      .sort((a, b) => (a.ordineControllo < b.ordineControllo) ? -1 : 1)
      .sort((a, b) => (a.ordine1 < b.ordine1) ? -1 : 1);

      this.listaEsitiTmp.forEach(el => {
          for (var i = 0; i < this.datiView.length; i++) {
            if (this.datiView[i].tipoEsito === el.codice1) {
              if (doNotShowList.some(nel => el.controllo === nel)) {
                continue;
              }
              let dati = new Dati(el.controlloDescrizione, el.valore);
              if (el.codice2 === 'Input') {
                this.datiView[i].datiInput.push(dati);
              } else if (el.codice2 === 'Output') {
                this.datiView[i].datiOutput.push(dati);
              }
            }
          }

        }
      );
      console.log(this.datiView);
    }, error => {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoEsitiIstruttoria));
    });
  }
}
