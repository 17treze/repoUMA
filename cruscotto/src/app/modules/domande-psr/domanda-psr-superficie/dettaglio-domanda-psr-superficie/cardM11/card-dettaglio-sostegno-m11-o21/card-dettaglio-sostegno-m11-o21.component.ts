import { Component, Input, OnInit } from '@angular/core';
import { ImpegnoRichiestoPsr } from '../../../../models/impegno-richiesto-psr';

@Component({
  selector: 'app-card-dettaglio-sostegno-m11-o21',
  templateUrl: './card-dettaglio-sostegno-m11-o21.component.html',
  styleUrls: ['./card-dettaglio-sostegno-m11-o21.component.css']
})
export class CardDettaglioSostegnoM11O21Component implements OnInit {

  @Input()
  impegniRichiestiPsr: Array<ImpegnoRichiestoPsr>;

  constructor() { }

  ngOnInit() {
  }

}
