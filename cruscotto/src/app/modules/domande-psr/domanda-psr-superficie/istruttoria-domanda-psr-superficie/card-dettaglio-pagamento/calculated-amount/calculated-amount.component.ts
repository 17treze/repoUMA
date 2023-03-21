import { Component, Input, OnInit } from '@angular/core';
import { StatoDomandaPsr } from '../../../../models/stato-domanda-psr';

@Component({
  selector: 'app-calculated-amount',
  templateUrl: './calculated-amount.component.html',
  styleUrls: ['./calculated-amount.component.css']
})
export class CalculatedAmountComponent implements OnInit {

  @Input()
  statoDomandaPsr?: StatoDomandaPsr;

  @Input()
  importoCalcolato?: number;

  constructor() {
  }

  ngOnInit() {
  }

  showAmount(): boolean {
      return this.importoCalcolato !== null;
  }
}
