import { Component, Input, OnInit } from '@angular/core';
import { Sanzione } from '../../models/dettaglio-istruttoria-srt';

@Component({
  selector: 'app-sanzione-domande',
  templateUrl: './sanzione-domanda.component.html',
  styleUrls: ['./sanzione-domanda.component.css']
})
export class SanzioneDomandaComponent implements OnInit {

  @Input()
  sanzioni: Sanzione[];

  constructor() {
  }

  ngOnInit() {

  }

}
