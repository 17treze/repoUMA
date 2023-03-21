import { Component, Input, OnInit } from '@angular/core';
import { DettaglioRazza } from '../../../../../models/dettaglio-pagamento-psr';

@Component({
  selector: 'app-razza-di-bestiame-domanda-psr',
  templateUrl: './razza-di-bestiame-domanda-psr.component.html',
  styleUrls: ['./razza-di-bestiame-domanda-psr.component.css']
})
export class RazzaDiBestiameDomandaPsrComponent implements OnInit {

  @Input()
  dettaglioRazza: DettaglioRazza;

  constructor() {
  }

  ngOnInit() {
  }


}
