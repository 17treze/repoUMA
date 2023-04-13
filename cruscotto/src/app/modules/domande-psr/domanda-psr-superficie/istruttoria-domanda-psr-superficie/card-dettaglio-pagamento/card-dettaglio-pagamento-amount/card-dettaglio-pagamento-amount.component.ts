import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-card-dettaglio-pagamento-amount',
  templateUrl: './card-dettaglio-pagamento-amount.component.html',
  styleUrls: ['./card-dettaglio-pagamento-amount.component.css']
})
export class CardDettaglioPagamentoAmountComponent implements OnInit {

  constructor() { }

  @Input()
  titolo: string;

  @Input()
  amount: number;

  @Input()
  subPrezzi?: Map<string, number>;

  ngOnInit() {
  }

}
