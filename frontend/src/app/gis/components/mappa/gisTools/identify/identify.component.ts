import { MapService } from './../../map.service';
import { ToastGisComponent } from './../../../toast-gis/toast-gis.component';
import { getLength } from 'ol/sphere';
import { PanelEvent } from './../../../../shared/PanelEvent';
import { MapEvent } from './../../../../shared/MapEvent';
import { Component, Input, ElementRef, OnInit, Host, Optional, ChangeDetectorRef } from '@angular/core';
import { TreeNode } from 'primeng-lts/api';
import TileWMS from 'ol/source/TileWMS';
import { FeatureService } from './../../feature.service';
import { LayerAttivi } from '../../../../shared/LayerAttivi';
import LayerGroup from 'ol/layer/Group';
import Layer from 'ol/layer/Layer';
import { PropertyLayer } from '../../../../shared/PropertyLayer.enum';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { GisListener } from '../gisListener';
import { enumListener } from '../enumListeners';

@Component({
  selector: 'gis-identify',
  templateUrl: './identify.component.html',
  styleUrls: ['./identify.component.css']
})
export class IdentifyComponent extends GisListener implements OnInit {
  /** Map id
   */
  //  @Input() mapId: string;
  dismissible: false;
  blockScroll: true;

  rootMap = document.getElementsByTagName('body')[0];
  identifyTableContent: any;
  elem: any;
  wmsSource: TileWMS;
  opacityBarVisible = false;
  getLayersConfig = [];
  map: any;
  wfsFound: any;
  constructor(
    public panelEvent: PanelEvent,
    public elementRef: ElementRef,
    private featureService: FeatureService,
    public layerAttivi: LayerAttivi,
    public mapEvent: MapEvent,
    private toastComponent: ToastGisComponent,
    protected toolBarService1: ToolBarService
  ) {
    super(enumListener.identify, toolBarService1, null);
  }

  ngOnInit() {
    this.panelEvent.showWmsResults = false;
    // Get the current map or get map by id
    this.map = this.mapEvent.map;
    const map = this.map;
    const _self = this;

    if (this.rootMap) {
      // All'apertura imposta opacity bar come nascosta
      this.rootMap.setAttribute('class', 'hideOpacity');
    }
    const identyBtn = (document.getElementById('identify') as HTMLElement);
    map.on('singleclick', (evt) => {
      this.panelEvent.identifyTableContent = [];
      this.wfsFound = [];
      // console.log(evt)
      //  if (identyBtn.classList.contains('active-tool')) {
      if (this.isActive) {
        const queryLayers = new Array<string>();
        _self.getLayersConfig = [];
        const featureWfs = []; // feature di layer workspace
        const infoLayersIspezionabili = this.layerAttivi.layerIspezionabili;
        this.map.getLayers().forEach(function (el1) {
          identifyLayerInLayerGroup(el1);

          function identifyLayerInLayerGroup(el) {
            if (el instanceof LayerGroup) {
              const groupLayer = el.get('layers').getArray();
              if (el.state_.visible) {
                for (let i = 0; i < groupLayer.length; i++) {
                  identifyLayerInLayerGroup(groupLayer[i]);
                }
              }
            } else if (el instanceof Layer) {
              // recupero i layer singoli
              identifyLayer(el);
            }
          }

          function identifyLayer(layerInMappa) {
            if (layerInMappa.get(PropertyLayer.ISPEZIONABILE) === true) {
              const infoLayerIspezionabile = infoLayersIspezionabili.filter(x => x.codice === layerInMappa.get(PropertyLayer.CODICE) &&
                layerInMappa.values_.visible === true);
              if (layerInMappa.get(PropertyLayer.FORMATO) === PropertyLayer.FORMATO_WFS) {
                if (infoLayerIspezionabile && infoLayerIspezionabile.length > 0) {
                  // recupero le properties del workspace
                  map.forEachFeatureAtPixel(evt.pixel, function (feature) {
                    // console.log(feature);
                    feature['nomeToc'] = infoLayerIspezionabile[0].titolo;
                    featureWfs.push(feature);
                  },
                    {
                      layerFilter: function (layer) {
                        return layer.get(PropertyLayer.CODICE) === layerInMappa.get(PropertyLayer.CODICE);
                      }
                    });
                }
              } else {
                if (infoLayerIspezionabile && infoLayerIspezionabile.length > 0) {
                  console.log(infoLayerIspezionabile);
                  const nomeLayerInMap: string = layerInMappa.get(PropertyLayer.NOMELAYER);
                  let nomeToc = '';
                  let cqlFilter = '';
                  if (layerInMappa.getSource() && layerInMappa.getSource().getParams()) {
                    cqlFilter = layerInMappa.getSource().getParams().CQL_FILTER;
                  }
                  queryLayers.push(nomeLayerInMap);
                  _self.getLayersConfig[nomeLayerInMap] = [];
                  if (nomeLayerInMap === PropertyLayer.NOME_LAYER_SUOLO_STORICO) {
                    nomeToc = infoLayerIspezionabile[0].titolo + _self.panelEvent.labelStoricoIdentify;
                  } else {
                    nomeToc = infoLayerIspezionabile[0].titolo;
                  }
                  _self.getLayersConfig[nomeLayerInMap].push({
                    'cqlFilter': cqlFilter,
                    'nomeToc': nomeToc,
                    'workspace': infoLayerIspezionabile[0].workspace,
                    'campoIdentify': infoLayerIspezionabile[0].campoIdentify
                  });
                }
              }
            }
          }
        });


        if (featureWfs && featureWfs.length > 0) {
          this.panelEvent.identifyTableContent = this.featureService.mapWfsProperties(featureWfs);
          this.wfsFound = this.panelEvent.identifyTableContent;
          this.expandCollapseWfs(this.panelEvent.identifyTableContent, true);
        }

        // invio a getFeatureInfo
        if (queryLayers && queryLayers.length > 0) {
          queryLayers.forEach(queryLayerName => {
            this.featureService.getFeatures(evt, queryLayers, _self.getLayersConfig, queryLayerName)
              .then(featureContent => {
                if (featureContent && featureContent.length > 0) {
                  if (this.panelEvent.identifyTableContent && this.panelEvent.identifyTableContent.length > 0) {
                    this.panelEvent.identifyTableContent = this.panelEvent.identifyTableContent.concat(featureContent);
                  } else {
                    this.panelEvent.identifyTableContent = featureContent;
                  }
                  this.expandCollapse(this.panelEvent.identifyTableContent, true);
                  this.panelEvent.showWmsResults = true;
                  this.panelEvent.panelIdentifyVisible = true;

                } else {
                  if (this.panelEvent.identifyTableContent && this.panelEvent.identifyTableContent.length > 0) {
                    this.panelEvent.showWmsResults = true;
                    this.panelEvent.panelIdentifyVisible = true;
                  }
                }
              });
          });
        } else {
          if (this.panelEvent.identifyTableContent && this.panelEvent.identifyTableContent.length > 0) {
            this.panelEvent.showWmsResults = true;
            this.panelEvent.panelIdentifyVisible = true;
          } else {
            this.toastComponent.showNoWmsIdentify();
          }
        }

      }
    });

    /* AS 31/03: Commentato in quanto inutile */
    /*map.on('pointermove', function (evt) {
      if (evt.dragging) {
        return;
      }
      const pixel = map.getEventPixel(evt.originalEvent);
      const hit = map.forEachLayerAtPixel(pixel, function () {
        return true;
      });
      map.getTargetElement().style.cursor = hit ? 'pointer' : '';
    });*/

  }

  public get canUseListener(): boolean {
    return true;
  }


  expandCollapse(node, isExpand: boolean): void {
    node.expanded = isExpand;
    for (let i = 0; i < node.length; i++) {
      // Se ho più di un gruppo ma con un solo children mostro solo il campo ID collassato
      if (node.length > 1 && !node[0].data.name.includes(PropertyLayer.WORKSPACE)) {
        if (node[i].children && node[i].children.length === 1) {
          node[i].children.forEach(childNode => {
            this.panelEvent.identifyTableContent[i].expanded = isExpand;
          });
        }
        // Se ho un solo gruppo con un solo children mostro tutti i campi
      } else if (node.length === 1 && !node[0].data.name.includes(PropertyLayer.WORKSPACE)) {
        if (node[i].children && node[i].children.length === 1) {
          this.panelEvent.identifyTableContent[i].expanded = isExpand;
          this.panelEvent.identifyTableContent[i].children[0].expanded = isExpand;
        }
      } else if (node.length > 1 && node[0].data.name.includes(PropertyLayer.WORKSPACE)) {
        if (node[i].children && node[i].children.length === 1) {
          this.panelEvent.identifyTableContent[i].expanded = isExpand;
          this.panelEvent.identifyTableContent[i].children[0].expanded = false;
        }
      }
    }

    this.panelEvent.identifyTableContent = this.removeDuplicates(this.panelEvent.identifyTableContent);

  }

  expandCollapseWfs(node, isExpand: boolean): void {
    node.expanded = isExpand;
    for (let i = 0; i < node.length; i++) {
      if (node.length > 0) {
        if (node.length === 1) {
          if (node[i].children && node[i].children.length === 1) {
            this.panelEvent.identifyTableContent[i].expanded = isExpand;
            this.panelEvent.identifyTableContent[i].children[0].expanded = isExpand;
          }
        } else {
          if (node[i].children && node[i].children.length === 1) {
            node[i].children.forEach(childNode => {
              this.panelEvent.identifyTableContent[i].expanded = isExpand;
            });
          }
        }
      }
    }
  }


  // Elimino i duplicati
  removeDuplicates(node): any {
    const treeNodes = node.reduce((unique, o) => {
      if (!unique.some(obj => obj.data.name === o.data.name)) {
        unique.push(o);
      }
      return unique;
    }, []);
    return treeNodes;
  }
}


