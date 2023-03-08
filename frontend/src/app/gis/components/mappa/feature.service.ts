import { EventEmitter, Injectable } from '@angular/core';
import { ToastGisComponent } from './../toast-gis/toast-gis.component';
import OlMap from 'ol/Map';
import View from 'ol/View';
import TileWMS from 'ol/source/TileWMS';
import { GisCostants } from '../../shared/gis.constants';
import { MapidService } from './mapid.service';
import { HttpClient } from '@angular/common/http';
import { LayerConfig } from '../../shared/LayerConfig';
import { PropertyLayer } from '../../shared/PropertyLayer.enum';
import { MapService } from './map.service';
import * as moment from 'moment';
import { MapEvent } from '../../shared/MapEvent';



@Injectable({
  providedIn: 'root'
})
export class FeatureService {

  features = [];
  view: View;

  newContestoEmitter: EventEmitter<number> = new EventEmitter();
  private contesto: string;
  layerConfig: LayerConfig[] = [];
  nomeToc: any;
  workSpaceList: any;


  constructor(private gisConstants: GisCostants, private mapService: MapService,
    private mapEvent: MapEvent, private mapidService: MapidService, private http: HttpClient) {

  }


  getFeatures(evt, queryLayers, config, queryLayerName): OlMap {

    let featureContent = [];
    let paramsIdentify;

    if (config[queryLayerName][0].cqlFilter) {
      paramsIdentify = { 'LAYERS': [queryLayerName], 'CQL_FILTER': config[queryLayerName][0].cqlFilter };
    } else {
      paramsIdentify = { 'LAYERS': [queryLayerName] };
    }

    const wmsSource = new TileWMS({
      url: this.gisConstants.geoserverUrl + config[queryLayerName][0].workspace + '/' + PropertyLayer.FORMATO_WMS,
      params: paramsIdentify,
      serverType: 'geoserver',
      crossOrigin: 'anonymous',
    });

    const viewResolution = this.mapEvent.map.getView().getResolution();
    const coordinate = evt.coordinate;

    // var url = wmsSource.getGetFeatureInfoUrl(
    let url = wmsSource.getFeatureInfoUrl(
      coordinate,
      viewResolution,
      this.gisConstants.defaultProjection,
      {
        'INFO_FORMAT': 'application/json',
        'exceptions': 'application/vnd.ogc.se_inimage',
        'BBOX': coordinate,
        'FEATURE_COUNT': 50
      }
    );

    if (url) {
      url = this.http.get<any>(url);
      return url
        .toPromise()
        .then((html) => {
          const wmsFeatures = html['features'];
          // const grouped = [];
          let arrLayers = [];
          queryLayers.forEach(function (value) {
            arrLayers[value] = [];
          });

          arrLayers = this.buildIdentifyTableContent(wmsFeatures, arrLayers, config, 'features');

          // tslint:disable-next-line: forin
          for (const key in arrLayers) {
            if (arrLayers[key] && arrLayers[key].length > 0) {
              featureContent.push(arrLayers[key][0]);
            }
          }
          if (wmsFeatures.length === 0 /*&& featureWfs.length === 0*/) {
            // this.toastComponent.showNoWmsIdentify();
            featureContent = [];
          }
        }).then(res => featureContent);
    }

  }

  mapWfsProperties(workSpaceList): any {

    const featureList = [];
    const featureContent = [];
    let arrLayers = [];
    const _self = this;
    for (let i = 0; i < workSpaceList.length; i++) {
      if (!workSpaceList[i].id_) {
        workSpaceList[i].id_ = 'LAYER_CARICATI';
      }
      const featureId = workSpaceList[i].id_;
      const splitted = featureId.split('.');
      const nomeLayer = splitted[0];
      arrLayers[nomeLayer] = [];
      _self.nomeToc = workSpaceList[i].nomeToc;
    }

    for (let k = 0; k < workSpaceList.length; k++) {
      const wmsFeature = {
        id: '',
        nomeToc: '',
        properties: {}
      };
      wmsFeature.id = workSpaceList[k].id_;
      wmsFeature.nomeToc = workSpaceList[k].nomeToc;
      const properties = workSpaceList[k]['values_'];
      // wmsFeature.properties = properties;
      wmsFeature.properties = Object.assign([], properties);
      wmsFeature.properties['AREA'] = Math.round((wmsFeature.properties['AREA'] + Number.EPSILON) * 100) / 100;
      delete wmsFeature.properties['geometry'];
      featureList.push(wmsFeature);
    }
    this.workSpaceList = workSpaceList;
    arrLayers = this.buildIdentifyTableContent(featureList, arrLayers, null, 'wfs');

    for (const key in arrLayers) {
      if (arrLayers[key] && arrLayers[key].length > 0) {
        featureContent.push(arrLayers[key][0]);
      }
    }

    return featureContent;
  }

  buildIdentifyTableContent(featureList, arrLayers, config, type): any {
    const grouped = [];
    let nomeToc = '';

    for (let i = 0; i < featureList.length; i++) {
      for (let arr = 0; arr < Object.keys(arrLayers).length; arr++) {
        // tslint:disable-next-line: forin
        for (const key in arrLayers) {
          const groupLayers = featureList.filter(x => x.id.includes(key));
          if (groupLayers && groupLayers[0]) {
            grouped[key] = groupLayers;
          }
        }
      }
    }
    // tslint:disable-next-line: forin
    for (const key in arrLayers) {
      if (type === 'features' && config) {
        nomeToc = config[key][0].nomeToc;
      }
      let nomeLayer = nomeToc;
      if (grouped[key] && grouped[key].length > 0) {
        // tslint:disable-next-line: forin
        for (let y = 0; y < grouped[key].length; y++) {
          if (type === 'wfs') {
            nomeLayer = grouped[key][y].nomeToc;
          }
          arrLayers[key].push({
            'data': { 'name': nomeLayer + ' (' + grouped[key].length + ')', 'size': '', 'type': 'main' },
            'children': []
          });
          let nomeTabPadre = '';
          // controllo se è presente e che valore ha campoIdentify
          if (config && config[key][0].campoIdentify === 'ID') {
            // scelgo objectid se ID non è presente (es. catasto)
            if (grouped[key][y]['properties'].objectid) {
              nomeTabPadre = grouped[key][y]['properties'].objectid;
            } else {
              nomeTabPadre = grouped[key][y]['properties']['ID'];
            }
          } else if (config && !!config[key][0].campoIdentify) {
            nomeTabPadre = grouped[key][y]['properties'][config[key][0].campoIdentify];
          } else {
            // scelgo objectid se ID non è presente (catasto)
            if (grouped[key][y]['properties'].objectid) {
              nomeTabPadre = grouped[key][y]['properties'].objectid;
              // se nemmeno objectid è presente mostro il primo campo disponibile (es. vettoriali esterni)
            } else if (!grouped[key][y]['properties'].objectid && grouped[key][y].nomeToc === 'Layer Caricati') {
              let prop = grouped[key][y]['properties'].sort();
              nomeTabPadre = prop[Object.keys(prop)[0]];
            } else {
              nomeTabPadre = grouped[key][y]['properties']['ID'];
            }
          }
          arrLayers[key][0]['children'].push({ 'data': { 'name': nomeTabPadre, 'size': '' }, 'children': [] });
          const properties = grouped[key][y]['properties'];
          // tslint:disable-next-line: forin
          for (const prop in properties) {
            if (typeof (properties[prop]) === 'string' && Date.parse(properties[prop]) && properties[prop].includes('-')) {
              // console.log(prop);
              let dateFormat = 'DD-MM-YYYY';
              properties[prop] = moment((new Date(properties[prop]).toISOString()));
              properties[prop] = moment(properties[prop]).format(dateFormat);
            }
            arrLayers[key][0]['children'][y]['children'].push({ 'data': { 'name': prop, 'size': grouped[key][y]['properties'][prop] } });
          }
        }
      }
    }

    return arrLayers;
  }
}
