import LayerGroup from 'ol/layer/Group';
import { MisuraUtils } from './../components/mappa/gisTools/misura.utils';
import { GeometryType } from 'ol/geom/GeometryType';
import { MapEvent } from './../shared/MapEvent';
import { ProfiloUtente } from './../shared/profilo-utente';
import { LayerAttivi } from './../shared/LayerAttivi';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { GisCostants } from '../shared/gis.constants';
import { PropertyLayer } from '../shared/PropertyLayer.enum';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import TileLayer from 'ol/layer/Tile';
import { LayerConfig } from '../shared/LayerConfig';
import { MapService } from '../components/mappa/map.service';
import { CaricaVettorialiCostants } from '../components/mappa/gisTools/carica-vettoriali/carica-vettorialicostants';
import { catchError } from 'rxjs/operators';
import { ToolBarService } from '../shared/ToolBar/toolBar.service';
import { GisStyles } from '../shared/GisStyles';

@Injectable()
export class LayerGisService {

  pathControllerLayer: string;

  constructor(private http: HttpClient, private gisConstants: GisCostants, public layerAttivi: LayerAttivi, public mapEvent: MapEvent,
    private mapService: MapService, public profiloUtente: ProfiloUtente, public caricaVettorialiCostants: CaricaVettorialiCostants,
    public gisCostants: GisCostants, public misuraUtils: MisuraUtils, private toolBarService: ToolBarService, public gisStyles: GisStyles) {
    this.pathControllerLayer = this.gisConstants.pathLayerController;
    this.profiloUtente.profilo = localStorage.getItem('selectedRole');
  }

  getLayerConfigProfile(params): Observable<any> {
    const URL = this.pathControllerLayer;
    return this.http.get<any[]>(URL, { params });
  }

  /** Add layer to the map
   */
  getLayerConfig(layerCaricati) {
    const params = new HttpParams().set('profilo_utente', this.profiloUtente.profilo.toUpperCase());
    return new Promise(((resolve, reject) => {
      this.getLayerConfigProfile(params)
        .subscribe(
          (results) => {
            results.forEach(element => {
              const layer = new LayerConfig(element['id'], element['codice'], element['url'],
                element['nomeLayer'], element['workspace'], element['proprieta'], element['attributi']);

              this.mapService.layerConfig.push(layer);
            });
            this.setIdentifyProperty(results);
            resolve(this.mapService.layerConfig);
          },
          (error) => {
            console.log(error);
            reject(error);
          });
    }));
  }

  layerProperties(layer: TileLayer | VectorLayer, currentLayer: LayerConfig) {
    layer.set(PropertyLayer.TITLE, currentLayer.mapProperty.get(PropertyLayer.TITOLO));
    layer.set(PropertyLayer.CODICE, currentLayer.codice);
    layer.set(PropertyLayer.NOMELAYER, currentLayer.getNomeLayer(this.mapService.annoCampagna));
    layer.set(PropertyLayer.NOSWITCHERDELETE, currentLayer.mapProperty.get(PropertyLayer.CANCELLABILE) === 'no' ? true : false);
    layer.set(PropertyLayer.ALLWAYSONTOP, true);
    layer.set(PropertyLayer.VISIBLE, currentLayer.mapProperty.get(PropertyLayer.ABILITATODEFAULT) === 'si' ? true : false);
    layer.set(PropertyLayer.EDITABILE, currentLayer.mapProperty.get(PropertyLayer.EDITABILE) === 'si' ? true : false);
    layer.set(PropertyLayer.ISPEZIONABILE, currentLayer.mapProperty.get(PropertyLayer.ISPEZIONABILE) === 'si' ? true : false);
    layer.set(PropertyLayer.FORMATO, currentLayer.mapProperty.get(PropertyLayer.FORMATO));
    layer.set(PropertyLayer.CONTENUTO_INFORMATIVO, currentLayer.mapProperty.get(PropertyLayer.CONTENUTO_INFORMATIVO));
    layer.set(PropertyLayer.CAMPO_IDENTIFY, currentLayer.mapProperty.get(PropertyLayer.CAMPO_IDENTIFY));
    layer.set(PropertyLayer.SNAP, currentLayer.mapProperty.get(PropertyLayer.SNAP) === 'si' ? true : false);
    layer.set(PropertyLayer.CARICAMENTO_STORICO, currentLayer.mapProperty.get(PropertyLayer.CARICAMENTO_STORICO) === 'si' ? true : false);
    layer.set(PropertyLayer.GRUPPO, currentLayer.mapProperty.get(PropertyLayer.GRUPPO));
    layer.set(PropertyLayer.DISPLAYINLAYERSWITCHER, currentLayer.mapProperty.get(PropertyLayer.DISPLAYINLAYERSWITCHER) === 'si' ? true : false);
    layer.set(PropertyLayer.RELAZIONECONLAYER, currentLayer.mapProperty.get(PropertyLayer.RELAZIONECONLAYER) ? currentLayer.mapProperty.get(PropertyLayer.RELAZIONECONLAYER) : 0);
  }

  layerCaricatiProperties(layer: TileLayer | VectorLayer, title, id) {
    return new Promise(((resolve, reject) => {
      try {
        layer.set(PropertyLayer.TITLE, title);
        layer.set(PropertyLayer.CODICE, 'LayerCaricati' + id);
        layer.set(PropertyLayer.NOMELAYER, title);
        layer.set(PropertyLayer.EDITABILE, true);
        layer.set(PropertyLayer.ISPEZIONABILE, true);
        layer.set(PropertyLayer.ALLWAYSONTOP, true);
        layer.set(PropertyLayer.SNAP, true);
        layer.set(PropertyLayer.FORMATO, PropertyLayer.FORMATO_WFS);
        layer.set(PropertyLayer.CAMPO_IDENTIFY, title);
        layer.set(PropertyLayer.CONTESTO, PropertyLayer.CONTESTO_LAVORAZIONE_EDITING);
        resolve(layer);
      } catch (error) {
        reject(error);
      }
    }));
  }

  setIdentifyProperty(listaLayers) {
    // console.log(listaLayers);
    const lista = listaLayers.filter(x => x.codice !== 'ortofoto');
    const layer = [];
    for (let i = 0; i < lista.length; i++) {
      layer.push({
        'name': lista[i].nomeLayer,
        'codice': lista[i].codice,
        'workspace': lista[i].workspace,
        'properties': lista[i].proprieta
      });
      const properties = layer[i]['properties'];
      // recupero e mappo relazioneConLayer
      const relazioneConLayer = properties.filter(x => x.nome === 'relazioneConLayer');
      if (relazioneConLayer && relazioneConLayer.length == 1) {
        layer[i].relazioneConLayer = relazioneConLayer[0].valore;
      } else {
        layer[i].relazioneConLayer = '0';
      }
      // recupero e mappo displayInLayerSwitcher
      const displayInLayerSwitcher = properties.filter(x => x.nome === 'displayInLayerSwitcher');
      if (displayInLayerSwitcher && displayInLayerSwitcher[0].valore === 'si') {
        layer[i].displayInLayerSwitcher = true;
      } else {
        layer[i].displayInLayerSwitcher = false;
      }
      // recupero e mappo identify
      const ispezionabile = properties.filter(x => x.nome === 'Ispezionabile');
      if (ispezionabile && ispezionabile[0].valore === 'si') {
        layer[i].identify = true;
      } else {
        layer[i].identify = false;
      }
      // recupero e mappo abiltiato
      const abilitato = properties.filter(x => x.nome === 'AbilitatoDefault');
      if (abilitato && abilitato[0].valore === 'si') {
        layer[i].abilitato = true;
      } else {
        layer[i].abilitato = false;
      }
      // recupero titolo
      const titolo = properties.filter(x => x.nome === 'titolo');
      layer[i].titolo = titolo[0].valore;
      // recupero campo_identify
      const campoIdentify = properties.filter(x => x.nome === 'CAMPO_IDENTIFY');
      if (campoIdentify && campoIdentify.length > 0) {
        layer[i].campoIdentify = campoIdentify[0].valore;
      }
      // recupero snap
      const snap = properties.filter(x => x.nome === PropertyLayer.SNAP);
      if (snap && snap.length > 0) {
        layer[i].snap = snap[0].valore;
      }
      delete layer[i]['properties'];
      this.layerAttivi.layerIspezionabili = layer;
    }

    let rels = (listaLayers.map(props => props['proprieta'])
      .filter(element => element
        .find(element => element.nome === 'relazioneConLayer' && element.valore !== '0')));

  }

  addLayerCaricatiToIdentify(layer) {
    const layerProperties = {
      name: '', titolo: '', identify: true, displayInLayerSwitcher: true, abilitato: true, codice: '', workspace: '',
      campoIdentify: null
    };
    layerProperties.name = layer.get(PropertyLayer.TITLE);
    layerProperties.titolo = layer.get(PropertyLayer.TITLE);
    layerProperties.identify = true;
    layerProperties.displayInLayerSwitcher = true;
    layerProperties.abilitato = true;
    layerProperties.codice = layer.get(PropertyLayer.CODICE);
    layerProperties.workspace = '/app_a4s';
    layerProperties.campoIdentify = null;
    this.layerAttivi.layerIspezionabili.push(layerProperties);
  }

  // getCapabilitiesWmts(currentLayer): Observable<any> {
  //   let URL = this.gisConstants.hostRichiesta + currentLayer.url + '/gwc/service/wmts?request=getCapabilities' ;
  //   return this.http.get<any[]>(URL, {});
  // }

  getCapabilitiesWmts(currentLayer): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'text/xml' });
    const urlWmts = this.gisConstants.hostRichiesta + currentLayer.url + '/gwc/service/wmts?request=getCapabilities';
    return this.http.get(urlWmts, { responseType: 'text', observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError('Error');
        })
      );
  }

  getCapabilities(url): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'text/xml' });
    return this.http.get(url, { responseType: 'text', observe: 'response' }).
      pipe(
        ((data: any) => {
          return data;
        }), catchError(error => {
          return throwError('Error');
        })
      );
  }

  addLayerGroup(groupInput) {
    // Order layer for position
    const layersGroupSort = groupInput.layers.sort((a, b) => (a.position > b.position) ? 1 : -1);

    const localLayer = [];
    layersGroupSort.forEach(x => {
      localLayer.push(x.layers);
    });
    const layerGroup = new LayerGroup({
      layers: localLayer,
      noSwitchDelete: true
    });
    layerGroup.set(PropertyLayer.TITLE, groupInput.title);
    layerGroup.set(PropertyLayer.VISIBLE, true);
    layerGroup.set(PropertyLayer.OPENINLAYERSWITCHER, groupInput.collassaGruppo === 'si' ? false : true); // TRUE il gruppo non è collassato
    layerGroup.set(PropertyLayer.NOSWITCHERDELETE, true);

    return layerGroup;
  }
  getLayerByCode(code: string) {
    return new Promise(((resolve, reject) => {
      try {
        const getVectorLayers = this.mapEvent.map.getLayers().getArray().filter(layer => layer instanceof VectorLayer);
        getVectorLayers.forEach(layer => {
          if (layer.get(PropertyLayer.CODICE) === code) {
            resolve(layer);
          }
        });
      } catch (error) {
        reject(error);
      }
    }));
  }
  /************
  // isVisible:
  // TRUE, per settare un layer come visibile
  // FALSE. per settare un layer come non visibile
  ************/
  toggleLayerSingolo(codiceLayer: string, isVisible: boolean) {
    const layers = this.mapEvent.map.getLayers().getArray();
    // recupera i gruppi di layer
    for (let i = 0; i < layers.length; i++) {
      if (layers[i].get('layers')) {
        // recupera i layer singoli all'interno del gruppo
        layers[i].getLayers().getArray().forEach((childLayer) => {
          if (childLayer.get(PropertyLayer.CODICE) === codiceLayer && isVisible) {
            // se il gruppo è spento viene acceso
            if (!layers[i].getVisible()) {
              layers[i].setVisible(true);
            }
            // se il layer è spento viene acceso
            if (!childLayer.getVisible()) {
              childLayer.setVisible(true);
            }
          } else if (childLayer.get(PropertyLayer.CODICE) === codiceLayer && !isVisible) {
            // spegne il layer
            childLayer.setVisible(false);
          }
        });
      }
    }
  }

  // Crea un vectorLayer dai parametri che riceve
  createVectorLayer(geometryType: GeometryType, misuraStyle, otherStyle) {
    const self = this;
    const vectorLayer = new VectorLayer({
      source: new VectorSource(),
      projection: this.gisCostants.defaultProjection,
      style: function (feature) {
        return self.misuraUtils.styleFunction(feature, true, geometryType,
          misuraStyle, otherStyle);
      },
    });
    return vectorLayer;
  }
}
