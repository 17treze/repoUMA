import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-card-dettaglio-investimento',
  templateUrl: './card-dettaglio-investimento.component.html',
  styleUrls: ['./card-dettaglio-investimento.component.css']
})
export class CardDettaglioInvestimentoComponent implements OnInit {
  @Input()
  titolo: string;

  @Input()
  descrizione: string;

  @Input()
  subDescrizione: string;

  @Input()
  descrizioneSettoriProduttivi: string;

  @Input()
  descrizioneDettaglioInvestimenti: string;

  constructor() { }

  ngOnInit() {
  }

}
