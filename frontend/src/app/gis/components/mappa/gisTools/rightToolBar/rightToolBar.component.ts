import { RightToolBarEvent } from './../../../../shared/RightToolBar/RightToolBarEvent';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'gis-rightToolBar',
  templateUrl: './rightToolBar.component.html',
  styleUrls: ['./rightToolBar.component.css'],
  encapsulation: ViewEncapsulation.None

})
export class RightToolBarComponent implements OnInit {
  activedTools: any;

  constructor(public rightToolbarEvent: RightToolBarEvent) { }

  ngOnInit() {
  }


}
