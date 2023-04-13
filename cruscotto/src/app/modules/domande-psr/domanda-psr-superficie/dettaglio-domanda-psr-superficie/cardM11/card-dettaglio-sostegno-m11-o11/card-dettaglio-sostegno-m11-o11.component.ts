import { Component, Input, OnInit } from '@angular/core';
import { ImpegnoRichiestoPsr } from '../../../../models/impegno-richiesto-psr';

@Component({
  selector: 'app-card-dettaglio-sostegno-m11-o11',
  templateUrl: './card-dettaglio-sostegno-m11-o11.component.html',
  styleUrls: ['./card-dettaglio-sostegno-m11-o11.component.css']
})
export class CardDettaglioSostegnoM11O11Component implements OnInit {

  @Input()
  impegniRichiestiPsr: Array<ImpegnoRichiestoPsr>;

  constructor() { }

  ngOnInit() {
  }

}
