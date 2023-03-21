import { Component, Input, OnInit } from '@angular/core';
import { ImpegnoRichiestoPsr } from '../../../models/impegno-richiesto-psr';

@Component({
  selector: 'app-card-dettaglio-sostegno-m10-o14',
  templateUrl: './card-dettaglio-sostegno-m10-o14.component.html',
  styleUrls: ['./card-dettaglio-sostegno-m10-o14.component.css']
})
export class CardDettaglioSostegnoM10O14Component implements OnInit {

  @Input()
  impegniRichiestiPsr: Array<ImpegnoRichiestoPsr>;

  constructor() { }

  ngOnInit() {
  }

}
