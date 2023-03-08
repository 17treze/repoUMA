import { MapEvent } from 'src/app/gis/shared/MapEvent';
import { ToolBarService } from 'src/app/gis/shared/ToolBar/toolBar.service';
import { RightToolBarEvent } from './../../../../shared/RightToolBar/RightToolBarEvent';
import { PanelEvent } from './../../../../shared/PanelEvent';
import { Component, Input, ElementRef, OnInit, Host, Optional, ViewEncapsulation, AfterViewChecked, ViewChild, Renderer2, HostListener, AfterViewInit, AfterContentInit, OnChanges } from '@angular/core';
import { MapService } from './../../map.service';
import { MapidService } from './../../mapid.service';
import OlMap from 'ol/Map';
import LayerSwitcher from '../../../../../../assets/pages/LayerSwitcher/LayerSwitcher_v3.2.1';
import ol_layer_Vector from 'ol/layer/Vector';
import ol_source_Vector from 'ol/source/Vector';
import GeoJSON from 'ol/format/GeoJSON';
import { MapdataService } from '../../mapdata.service';
import { LayerConfig } from '../../../../shared/LayerConfig';
import * as cloneDeep from 'lodash/cloneDeep';
import { enumTool } from '../../gisTools/enumTools';
import { PropertyLayer } from '../../../../shared/PropertyLayer.enum';

@Component({
  selector: 'gis-layerSwitcher',
  templateUrl: './layerSwitcher.component.html',
  styleUrls: ['./layerSwitcher.component.css'],
  encapsulation: ViewEncapsulation.None

})
export class LayerSwitcherComponent implements OnInit {

  /** Map id
   */
  @Input() mapId: string;
  @ViewChild('layerSwitcher') layerSwitcher: ElementRef;
  checkboxLayer: HTMLElement = document.querySelector('.ol-layerswitcher .panel li label') as HTMLElement;
  dismissible: false;
  blockScroll: true;
  elem: any;
  showLayerSwitcher = false;
  rootMap = document.getElementsByTagName('body')[0];
  layerConfig: LayerConfig[] = [];
  map: any;
  identifyPanelVisibility: boolean;
  clickHandlerLoaded = false;
  constructor(
    private mapService: MapService,
    @Host()
    @Optional()
    private mapidService: MapidService,
    private mapdataService: MapdataService,
    private elementRef: ElementRef,
    public panelEvent: PanelEvent,
    private toolBarService: ToolBarService,
    public rightToolbarEvent: RightToolBarEvent,
    public mapEvent: MapEvent,
    public renderer: Renderer2
  ) {
  }

  ngOnInit() {

    if (this.rootMap) {
      // All'apertura imposta opacity bar come nascosta
      this.rootMap.setAttribute('class', 'hideOpacity');
    }
    // Get the current map or get map by id
    const map: OlMap = this.mapService.getMap(this.mapidService || this.mapId);
    this.map = map;
    // Get the mapdataService
    const mapdataService: MapdataService = this.mapdataService;

    // Get the target if outside the map
    const target = this.elementRef.nativeElement.parentElement ? this.elementRef.nativeElement : null;

    //  Vector layer
    const vector = new ol_layer_Vector({ source: new ol_source_Vector() });
    // map.addLayer(vector);

    // Vector callback
    vector.on('change', function (e) {
      console.log('VECTOR change', e);
      console.log('VECTOR change - features', vector.getSource().getFeatures());
      const geoJsonValue = new GeoJSON().writeFeatures(vector.getSource().getFeatures());
    });

    const _self = this;

    const external = (<HTMLElement> document.querySelector('.layerSwitcher'));
    const switcher = new LayerSwitcher({
      source: vector.getSource(),
      target: external,
      show_progress: true,
      extent: true,
      reordering: true,
      trash: true,
      refresh: true,
      onchangeCheck: function (l) {
        const listaInteractionSnap =  _self.mapEvent.map.interactions.getArray()
        .filter(interaction1 => interaction1.values_.name && interaction1.values_.name.search(enumTool.snapEl) >= 0 );
        const storedLayers = cloneDeep(l);

        const currentRelatedLayers = _self.mapService.getRelatedLayers(_self.mapEvent, l);
        currentRelatedLayers.forEach(element => {
          element.forEach(layer => {
              layer.visible = l.values_.visible;
          });
        });

        // se il gruppo è disattivato disattivo i layer per lo snap
        if (l.values_.layers && l.getVisible() === false) {
          for (let i = 0; i < l.getLayers().getArray().length; i++) {
            const layer = l.getLayers().getArray();
            layer[i].setVisible(false);
          }
        }
        if (l.values_.layers !== undefined && l.values_.layers.getLength() > 0) {
          // Layer Group
          l.values_.layers.getArray().forEach(element => {
            _self.toolBarService.setSnapOnLayer(element, listaInteractionSnap);
          });
        } else {
          // Layer
          _self.toolBarService.setSnapOnLayer(l, listaInteractionSnap);
        }
        // riattivo i layer del gruppo per non perdere la selezione
        if (l.values_.layers && l.getVisible() === false) {
          for (let i = 0; i < l.getLayers().getArray().length; i++) {
            const layer = l.getLayers().getArray();
            layer[i].setVisible(storedLayers.getLayers().getArray()[i].getVisible());
          }
        }
      }
    });
    map.addControl(switcher);

    map.on('pointermove', function (evt) {
      if (evt.dragging) {
        return;
      }
      const pixel = map.getEventPixel(evt.originalEvent);
      const hit = map.forEachLayerAtPixel(pixel, function () {
        return true;
      });
      map.getTargetElement().style.cursor = hit ? 'pointer' : '';
    });

  }

  checkIfIdentifyIsHidden() {
    const identifyPanel = (<HTMLElement> document.querySelector('.identify-results'));
    if (identifyPanel.hidden === true) {
      this.identifyPanelVisibility = false;
    } else {
      this.identifyPanelVisibility = true;
    }
  }

  // Attiva Layer switcher
  openLayerSwitcher() {
    // console.log(this.layerAttivi)
    // console.log(this.layerConfig)
    this.checkIfIdentifyIsHidden();
    this.panelEvent.showLayerSwitcher = !this.panelEvent.showLayerSwitcher;
    this.panelEvent.showSettings = false;
    const typecast = (<HTMLElement> document.querySelector('#switcher-opacity'));
    if (this.panelEvent.showLayerSwitcher) {
      typecast.style.display = 'block';
    } else {
      typecast.style.display = 'none';
    }


    /*if (!this.clickHandlerLoaded) {
      const _self = this;
      function clickHandlerGroup(this: HTMLElement, ev: Event) {
        //_self.toolBarService.getSnapStatus(_self.mapEvent.map.getLayers().getArray());
        console.log(this.offsetParent.childNodes[0]);
      }
      function clickHandlerLayer(this: HTMLElement, ev: Event) {
        //_self.toolBarService.getSnapStatus(_self.mapEvent.map.getLayers().getArray());
        console.log(this.offsetParent.childNodes[0]);
      }

      function clickExpandHandler(this: HTMLElement, ev: Event) {
        const innerElements = this.querySelectorAll('.ol-layerswitcher .panel li label');
        if (innerElements.length > 0) {
          elements.forEach(element => {
            element.addEventListener('click', clickHandlerGroup);
          });
        }
      }

      const elements = document.querySelectorAll('.ol-layerswitcher .panel li label');
      if (elements.length > 0) {
        this.clickHandlerLoaded = true;
        elements.forEach(element => {
          element.addEventListener('click', clickHandlerGroup);
        });
      }

      const expandElements = document.querySelectorAll('.ol-layerswitcher .panel div expend-layers');
      if (expandElements.length > 0) {
        expandElements.forEach(el => {
          el.addEventListener('click', clickExpandHandler);
        });
      }

    }*/

  }

  checkLayerVisibility(event) {
    // console.log(this.map.getLayers());
  }
}
