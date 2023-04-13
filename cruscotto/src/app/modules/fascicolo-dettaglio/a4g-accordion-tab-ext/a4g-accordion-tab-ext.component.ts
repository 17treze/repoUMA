import { A4gAccordionFieldExt } from './../a4g-accordion-tab/a4g-accordion.model';
import { Component, OnInit, Input, ViewEncapsulation } from '@angular/core';


@Component({
  selector: 'app-a4g-accordion-tab-ext',
  templateUrl: './a4g-accordion-tab-ext.component.html',
  styleUrls: ['./a4g-accordion-tab-ext.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class A4gAccordionTabExtComponent implements OnInit {

  @Input() accordionModels: Array<A4gAccordionFieldExt>; /** Accordion fields */
  @Input() columns: Array<any>;                       /** Names of the table columns  */

  constructor() { }

  ngOnInit() {
  }

}
