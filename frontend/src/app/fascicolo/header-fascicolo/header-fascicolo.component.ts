import { Component, OnInit } from '@angular/core';
import { recordTabella } from 'src/app/a4g-common/classi/recordTabella';

@Component({
  selector: 'app-header-fascicolo',
  templateUrl: './header-fascicolo.component.html',
  styleUrls: ['./header-fascicolo.component.css']
})
export class HeaderFascicoloComponent implements OnInit {
  dati = new Array<recordTabella>();

  constructor() { }

  ngOnInit(): void {
  }
}
