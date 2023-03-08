import { Component, OnInit, Input, ViewEncapsulation } from '@angular/core';
import { A4gAccordionField } from './a4g-accordion.model';

@Component({
  selector: 'app-a4g-accordion-tab',
  templateUrl: './a4g-accordion-tab.component.html',
  styleUrls: ['./a4g-accordion-tab.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class A4gAccordionTabComponent implements OnInit {

  @Input() accordionModels: Array<A4gAccordionField>; /** Accordion fields */
  @Input() columns: Array<any>;                       /** Names of the table columns  */

  constructor() { }

  ngOnInit() {
  }

}
