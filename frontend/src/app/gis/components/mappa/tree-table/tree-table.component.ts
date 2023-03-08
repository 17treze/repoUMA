import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'gis-tree-table',
  templateUrl: './tree-table.component.html',
  styleUrls: ['./tree-table.component.css'],
  encapsulation: ViewEncapsulation.None

})
export class TreeTableComponent implements OnInit {
  @Input() data: any;
  @Input() showWmsResults: boolean;

  constructor() { }

  ngOnInit() {
  }

}
