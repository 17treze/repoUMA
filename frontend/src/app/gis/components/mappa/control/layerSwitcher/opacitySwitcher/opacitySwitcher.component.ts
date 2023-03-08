import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'gis-opacitySwitcher',
  templateUrl: './opacitySwitcher.component.html',
  styleUrls: ['./opacitySwitcher.component.css'],
  encapsulation: ViewEncapsulation.None

})
export class OpacitySwitcherComponent implements OnInit {
@Input() rootMap:any;
opacityBarVisible = false;

  constructor() { }

  ngOnInit() {
  }

  // Rimuove opacity bar
  removeOpacityBar(visible) {
    setTimeout(() => {
      this.rootMap = document.getElementsByTagName('body')[0];
      if (visible) {
          this.rootMap.classList.remove('hideOpacity');
      } else {
        this.rootMap.setAttribute( 'class', 'hideOpacity' );
      }
    }, 300);
  }
}
