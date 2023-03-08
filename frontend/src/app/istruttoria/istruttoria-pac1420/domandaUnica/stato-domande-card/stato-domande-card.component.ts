import { Component, OnInit, Input } from '@angular/core';
import { RiepilogoStatoDomande } from '../dettaglio-istruttoria/RiepilogoStatoDomande';
import { DettaglioIstruttoriaComponent } from '../dettaglio-istruttoria/dettaglio-istruttoria.component';

@Component({
  selector: 'app-stato-domande-card',
  templateUrl: './stato-domande-card.component.html',
  styleUrls: ['./stato-domande-card.component.css']
})
export class StatoDomandeCardComponent implements OnInit {
  @Input() riepilogoStatoDomande: RiepilogoStatoDomande;
  @Input() dettaglioIstruttoria: DettaglioIstruttoriaComponent;
  constructor() { }
  displayInIstruttoriaDialog = false;
  ngOnInit() {
    this.riepilogoStatoDomande;
    this.dettaglioIstruttoria;
  }

  onIstruttoriaDialogClose(event) {
    this.displayInIstruttoriaDialog = event;
  }
  displayDialog() {
    this.displayInIstruttoriaDialog = true;
  }

}
