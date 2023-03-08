import { Component, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'gis-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css'],
  encapsulation: ViewEncapsulation.None

})
export class FooterComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
