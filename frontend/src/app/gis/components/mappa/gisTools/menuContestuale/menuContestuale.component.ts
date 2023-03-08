import { MapEvent } from './../../../../shared/MapEvent';
import {  Component, OnInit, ViewEncapsulation } from '@angular/core';
import ContextMenu from 'ol-contextmenu';
import { MapService } from '../../map.service';
import { MapidService } from '../../mapid.service';
import { GisTransform } from '../gisTransform';
import { transform as trans } from 'ol/proj';
import { format } from 'ol/coordinate';
import { Clipboard } from '@angular/cdk/clipboard';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { PropertyLayer } from '../../../../shared/PropertyLayer.enum';
import { enumTool } from '../enumTools';
import { EsitoLavorazioneDichiarato, EsitoLavorazioneDichiaratoDecode } from '../../../../shared/EsitoLavorazioneDichiarato.enum';
import { CampoFeature } from '../../../../shared/CampoFeature';
import { CreazioneLavorazioneService } from '../../../../services/creazione-lavorazione.service';
import { ToastGisComponent } from '../../../toast-gis/toast-gis.component';
import { StatiRichiesta } from '../../../../shared/StatiRichiesta.enum';
import { enumControls } from '../enumControls';

@Component({
  selector: 'gis-menuContestuale',
  templateUrl: './menuContestuale.component.html',
  styleUrls: ['./menuContestuale.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class MenuContestualeComponent implements OnInit {
  // contextmenu: any;

  constructor(
    private mapService: MapService,
    private mapidService: MapidService,
    private toolBarService: ToolBarService,
    private creazioneLavorazioneService: CreazioneLavorazioneService,
    private toastComponent: ToastGisComponent,
    public mapEvent: MapEvent,
    private clipboard: Clipboard
  ) {}

  ngOnInit() {
    const map = this.mapEvent.map;
    const _self = this;

    // tool trasforma
    const transform = new GisTransform(this.mapService, this.toolBarService);
    let textTransform = '';
    let visualizzaTransform = transform.canUseTool;
    let features = [];

    function center(obj) {
      console.log(obj.coordinate);
      map.getView().setCenter(obj.coordinate);
    }

    function copiaCoordinate(obj: any, to: string, precision: number) {
      let from = map.getView().getProjection();
      let coordinates = trans(obj.coordinate, from, to);
      let string = format(coordinates, '{x}, {y}', precision);

      _self.clipboard.copy(string);
    }

    function setEsito(obj, esito) {
      console.log(obj.coordinate, esito);
      if (features.length === 1 && features[0].get(CampoFeature.ESITO) && features[0].get(CampoFeature.ESITO) !== esito) {
        console.log(features);
        _self.creazioneLavorazioneService.putAssociaEsitoSuoloDichiaratoLavorazione
          (features[0].get(CampoFeature.ID), esito).subscribe((response: any) => {
            console.log(response);
            if (response['status'] === 200) {
              _self.toastComponent.showSuccess();
              features[0].set(CampoFeature.ESITO, esito);
              _self.mapService.reloadPoligoniDichiaratiEmitter.emit('');
            } else {
              _self.toastComponent.showError();
            }
          },
            err => { _self.toastComponent.showError(); });
      }
    }

    function setEsitoDaLavorare(obj) {
      setEsito(obj, EsitoLavorazioneDichiarato.DA_LAVORARE);
    }

    function setEsitoApprovato(obj) {
      setEsito(obj, EsitoLavorazioneDichiarato.APPROVATO);
    }

    function setEsitoRespinto(obj) {
      setEsito(obj, EsitoLavorazioneDichiarato.RESPINTO);
    }

    function setEsitoParziale(obj) {
      setEsito(obj, EsitoLavorazioneDichiarato.PARZIALE);
    }

    function selezionaElemento(obj) {
      console.log(obj.coordinate);
      console.log('pixel', map.getPixelFromCoordinate(obj.coordinate));
      const select = ToolBarService.getInteractionFomName(map, enumTool.selectFeatures);

      map.forEachFeatureAtPixel(map.getPixelFromCoordinate(obj.coordinate), function (feature) {
        select.getFeatures().extend(feature);
      } /*, {
        layerFilter: function (layer) {
          if (mapService.editLayer) {
            return layer.get(PropertyLayer.CODICE) === mapService.editLayer.get(PropertyLayer.CODICE);
          }
        }
      }*/);
    }

    function enableTransform() {
      transform.enableTool();
    }

    const contextmenu = new ContextMenu({
      width: 190,
      defaultItems: false, // defaultItems are (for now) Zoom In/Zoom Out
      items: []
    });

    /*let currentWorkspaceLayersource = this.mapService.workspaceLayersource;

    function deleteFeature(feature: any) {
      console.log('delete');
      console.log(feature);
      currentWorkspaceLayersource.removeFeature(feature);

    }*/
    contextmenu.set('name', enumControls.contextMenu);
    map.addControl(contextmenu);

    function reloadMenu(): boolean {
      // eventualmente aggiungere altre condizioni per ricaricare il menu
      let reload = false;
      const caption = ToolBarService.getInteractionCaptionFomName(map, transform.toolName, 'Trasforma');
      if (caption !== textTransform) {
        textTransform = caption;
        reload = true;
      }
      if (visualizzaTransform !== transform.canUseTool) {
        visualizzaTransform = transform.canUseTool;
        reload = true;
      }
      return reload;
    }

    function getFunzioneTrasforma(): any {
      if (transform.canUseTool) {
        return ({
          text: textTransform,
          callback: enableTransform
        });
      } else {
        return null;
      }
    }

    /*contextmenu.on('open', function (evt) {
      var features = [];
      map.forEachFeatureAtPixel(evt.pixel, function (feature) {
        features.push(feature);
        console.log('feature');
        console.log(feature);
        //return feature;
      }, {
        layerFilter: function (layer) {
          if (mapService.editLayer) {
            return layer.get(PropertyLayer.CODICE) === mapService.editLayer.get(PropertyLayer.CODICE);
          }
        }
      });
      console.log(features);
    });*/

    const lastFeatureLegth = 0;

    document.addEventListener('keydown', ({ key }) => {
      if (key === 'Escape' && contextmenu.isOpen()) {
        contextmenu.close();
      }
    });

    contextmenu.on('open', function (evt) {
      features = [];

      map.forEachFeatureAtPixel(evt.pixel, function (feature) {
        if (feature && feature.get(CampoFeature.STATO) && feature.get(CampoFeature.STATO) === StatiRichiesta.IN_LAVORAZIONE) {
          features.push(feature);
        }
      }, {
        layerFilter: function (layer) {
          if (_self.mapService.editLayer) {
            return layer.get(PropertyLayer.CONTENUTO_INFORMATIVO) === PropertyLayer.CONTENUTO_INFORMATIVO_ISTANZA_DI_RIESAME &&
              layer.get(PropertyLayer.FORMATO) === PropertyLayer.FORMATO_WFS;
          }
        }
      }
      );

      let execReloadMenu = reloadMenu();
      if (!execReloadMenu && lastFeatureLegth !== features.length) {
        execReloadMenu = true;
      }

      if (reloadMenu) {
        contextmenu.clear();
        const toolEditing = new Array<any>();
        const funzioneTrasforma = getFunzioneTrasforma();
        if (funzioneTrasforma) {
          toolEditing.push(funzioneTrasforma);
        }

        toolEditing.push({
          text: 'Test tool',
          callback: center
        });

        const all_items = [
          /*{
            // da sistemare
            text: 'Seleziona elemento',
            callback: selezionaElemento
          },*/
          // '-' // this is a separator
        ];
        contextmenu.extend(all_items);
        contextmenu.extend(contextmenu.getDefaultItems());
        contextmenu.extend(
          [{
            text: 'Centra qui',
            // icon: 'img/marker.png',
            callback: center
          }]
        );
        contextmenu.extend([
          {
            text: 'Copia Coordinate',
            width: 400,
            items: [
              {
                text: '25832 - ETRS89 UTM 32N',
                callback: obj => copiaCoordinate(obj, 'EPSG:25832', 4)
              },
              {
                text: '3003 - ROMA40 GB O TN-AA',
                callback: obj => copiaCoordinate(obj, 'EPSG:3003', 4)
              },
              {
                text: '4326 - WGS84',
                callback: obj => copiaCoordinate(obj, 'EPSG:4326', 7)
              }
            ]
          }
        ]);


        if (features.length === 1) {

          const esitoDichiarato = new Array<any>();
          const esitoAttuale = EsitoLavorazioneDichiarato[features[0].get(CampoFeature.ESITO)];

          esitoDichiarato.push({
            text: EsitoLavorazioneDichiaratoDecode.decode(EsitoLavorazioneDichiarato.DA_LAVORARE),
            icon: esitoAttuale === EsitoLavorazioneDichiarato.DA_LAVORARE ? 'assets/img/icons/down-arrow.png' : null,
            callback: setEsitoDaLavorare
          });
          esitoDichiarato.push({
            text: EsitoLavorazioneDichiaratoDecode.decode(EsitoLavorazioneDichiarato.APPROVATO),
            icon: esitoAttuale === EsitoLavorazioneDichiarato.APPROVATO ? 'assets/img/icons/down-arrow.png' : null,
            callback: setEsitoApprovato
          });
          esitoDichiarato.push({
            text: EsitoLavorazioneDichiaratoDecode.decode(EsitoLavorazioneDichiarato.RESPINTO),
            icon: esitoAttuale === EsitoLavorazioneDichiarato.RESPINTO ? 'assets/img/icons/down-arrow.png' : null,
            callback: setEsitoRespinto
          });
          esitoDichiarato.push({
            text: EsitoLavorazioneDichiaratoDecode.decode(EsitoLavorazioneDichiarato.PARZIALE),
            icon: esitoAttuale === EsitoLavorazioneDichiarato.PARZIALE ? 'assets/img/icons/down-arrow.png' : null,
            callback: setEsitoParziale
          });

          const newItem = [
            '-', // this is a separator
            {
              // Setta Esito con sotto Menu Da Lavorare/ Approvato/ Respinto/ Parziale
              text: 'Setta Esito',
              items: esitoDichiarato
            }
          ];
          contextmenu.extend(newItem);
        }
      }
    });
  }
}
