import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ProfiloUtente } from '../../shared/profilo-utente';
import { MapdataService } from '../mappa/mapdata.service';
import { PanelEvent } from '../../shared/PanelEvent';

@Component({
  selector: 'app-gis',
  templateUrl: './gis.component.html',
  styleUrls: ['./gis.component.css'],
  encapsulation: ViewEncapsulation.None

})
export class GisComponent implements OnInit {

  router: any;
  topbarMenuClick: false;
  geoObj: string;
  gisResize: number;
  dynamicHeight: any;
  getHeight: string;

  constructor(
    private mapdataService: MapdataService,
    public profiloUtente: ProfiloUtente,
    public panelEvent: PanelEvent
  ) {
    this.profiloUtente.profilo = localStorage.getItem('selectedRole');
  }

  ngOnInit() {
    this.panelEvent.bufferSpinnerLoading = [];
    this.mapdataService.dataChanged
      .subscribe({
        next: (v) => {
          console.log(`Geo Changed: ${v}`);
          this.geoObj = v.toString();
        }

      });
  }
  ngAfterViewInit() {
    if (this.getZoomFromBrowser() > 100) {
      // setTimeout(() => {
      //   this.setBrowserResolution();
      //   this.setOuterHeight();
      // }, 1000);
    }
  }

  getZoomFromBrowser() {
    return Math.round(window.devicePixelRatio * 100);
  }

  setBrowserResolution() {
    // window.innerWidth: Risoluzione larghezza finestra
    // window.outerWidth: Risoluzione larghezza schermo
    this.gisResize = (window.innerWidth / window.outerWidth) + 0.2;
  }

setOuterHeight() {
    // window.outerHeight: Risoluzione altezza schermo
    this.getHeight = 'calc(100vh + ' + window.outerHeight + 'px' + ')';
    const sidebar = document.querySelectorAll('sidebar');
    const sidebarGis = document.getElementById('sidebar-gis');
    const layout = document.getElementById('gis-layout');
    const rightMenu = document.getElementById('right-menu-gis');
    const breadcrumb = document.querySelector('.cont-breadcrumb');
    const editTolbar = document.querySelector('.editTolbar');
    const scalaMappa = document.querySelector('.ol-scale-line-inner');

    for (let i = 0; i < sidebar.length; i++ ) {
      sidebar[i]['style']['zoom'] = this.gisResize.toString();
    }
    sidebarGis['style'].zoom = this.gisResize.toString();
    layout['style'].zoom = this.gisResize.toString();
    rightMenu['style'].zoom = this.gisResize.toString();
    breadcrumb['style'].zoom = this.gisResize.toString();
    editTolbar['style'].zoom = this.gisResize.toString();
    scalaMappa['style'].zoom = this.gisResize.toString();
  }
  onMapReady(event) {
    console.log('Map Ready');
  }
  onMouseDown(event) {
    return event.button !== 1;
  }
}
