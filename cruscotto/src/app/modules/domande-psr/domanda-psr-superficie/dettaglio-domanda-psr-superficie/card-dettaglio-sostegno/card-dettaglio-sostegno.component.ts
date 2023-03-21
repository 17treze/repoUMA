import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-card-dettaglio-sostegno',
  templateUrl: './card-dettaglio-sostegno.component.html',
  styleUrls: ['./card-dettaglio-sostegno.component.css']
})
export class CardDettaglioSostegnoComponent implements OnInit {
  @Input()
  codiceIntervento: string;

  constructor() {
  }

  ngOnInit() {
  }

}
