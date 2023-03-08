import { LavorazioniEvent } from './../../shared/LavorazioniEvent';
import { Component, Input, OnInit } from '@angular/core';
import { PanelEvent } from '../../shared/PanelEvent';

@Component({
  selector: 'calcola-poligoni-dialog-gis',
  templateUrl: './calcola-poligoni-dialog-gis.component.html',
  styleUrls: ['./calcola-poligoni-dialog-gis.component.css']
})
export class CalcolaPoligoniDialogGisComponent implements OnInit {
  titoloDialog =  'POLIGONI SUOLO NON ASSOCIABILI';
  //titoloDialogPoligoniSuolo = 'POLIGONI SUOLO NON ASSOCIATI';
  //tiloloDialogPoligoniSuoloPrenotati = 'POLIGONI SUOLO PRENOTATI';
  //titoloDialog: string;

  constructor(public panelEvent: PanelEvent, public lavorazioniEvent: LavorazioniEvent) { 
  
  }
  

  

  ngOnInit() {
  }

}
