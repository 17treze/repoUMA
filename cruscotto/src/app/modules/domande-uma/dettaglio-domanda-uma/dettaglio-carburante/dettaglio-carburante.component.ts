import { Component, Input, OnInit } from '@angular/core';
import { DettaglioCarburante } from '../../models/carburante-richiesto copy';

@Component({
  selector: 'app-dettaglio-carburante',
  templateUrl: './dettaglio-carburante.component.html',
  styleUrls: ['./dettaglio-carburante.component.css'],
})
export class DettaglioCarburanteComponent implements OnInit {
  @Input()
  carburante: DettaglioCarburante; // TODO: make an interface.. itz free

  constructor() {}

  ngOnInit() {}

  get disponibile() {
    if (!this.carburante) return 0;

    return (
      this.carburante.assegnato -
      this.carburante.prelevato -
      this.carburante.ricevuto
    );
  }
}
