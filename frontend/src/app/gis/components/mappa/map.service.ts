import { CaricaVettorialiCostants } from './gisTools/carica-vettoriali/carica-vettorialicostants';
import { MapEvent } from './../../shared/MapEvent';
import { EventEmitter, Injectable } from '@angular/core';
import { ToastGisComponent } from './../toast-gis/toast-gis.component';
import Point from 'ol/geom/Point';
import Feature from 'ol/Feature';
import { fromLonLat } from 'ol/proj';
import OlMap from 'ol/Map';
import OlView from 'ol/View';
import { Style, Icon, Fill, Text, Stroke } from 'ol/style';
import proj4 from 'proj4';
import { register } from 'ol/proj/proj4';
import { Extent } from 'ol/extent';
import Projection from 'ol/proj/Projection';
import { get as GetProjection } from 'ol/proj';
import View from 'ol/View';
import { ScaleLine, defaults as DefaultControls } from 'ol/control';
import LayerGroup from 'ol/layer/Group';
import Collection from 'ol/Collection';
import { getDistance } from 'ol/sphere';
import { transform } from 'ol/proj';
import MousePosition from 'ol/control/MousePosition';
import { createStringXY } from 'ol/coordinate';
import { GeoJSON, WFS } from 'ol/format';
import {
  and as andFilter,
  equalTo as equalToFilter,
  not as notFilter,
  like as likeFilter,
} from 'ol/format/filter';
import { Filter } from 'ol/format/filter/Filter';
import { GisCostants } from '../../shared/gis.constants';
import { MapidService } from './mapid.service';
import VectorLayer from 'ol/layer/Vector';
import Layer from 'ol/layer/Layer';
import { HttpClient } from '@angular/common/http';
import { LayerConfig } from '../../shared/LayerConfig';
import { PropertyLayer } from '../../shared/PropertyLayer.enum';
import { CampoFeature } from '../../shared/CampoFeature';
import { getArea } from 'ol/extent';
import TileLayer from 'ol/layer/Tile';
import TileWMS from 'ol/source/TileWMS';
import ImageLayer from 'ol/layer/Image';
import ImageWMS from 'ol/source/ImageWMS';
import { Lavorazione, LavorazioneChangeStatus } from '../../shared/Lavorazione';
import { environment } from '../../../../environments/environment';


/**
 * Openlayers map service to acces maps by id
 * Inject the service in the class that have to use it and access the map with the getMap method.
 * @example
 *
  import { MapService } from '../map.service';
  import OlMap from 'ol/Map';

  constructor(
    private mapService: MapService,
  ) { }
  ngOnInit() {
    // Get the current map
    const map: OlMap = this.mapService.getMap('map');
  }
 */
@Injectable({
  providedIn: 'root'
})
export class MapService {

  /**
   * List of Openlayer map objects [ol.Map](https://openlayers.org/en/latest/apidoc/module-ol_Map-Map.html)
   */
  private map = {};
  rp = [];
  features = [];
  projection: Projection;
  extent: Extent = [-1877994.66, 3932281.56, 836715.13, 9440581.95];
  view: View;
  featureContent: any;
  idNew = 0;
  featureCenterSelected: Feature;
  editLayer: VectorLayer;
  wfsTraceLayer: VectorLayer;
  linearProjection = true;
  annoCampagna: number = environment.annoCorrenteGis;

  newContestoEmitter: EventEmitter<number> = new EventEmitter();
  // modifyFeatureEmitter: EventEmitter<{layerCode: string, action: string}> = new EventEmitter();
  modifyFeatureEmitter: EventEmitter<string> = new EventEmitter();
  reloadPoligoniDichiaratiEmitter: EventEmitter<string> = new EventEmitter();

  private contesto: string;
  layerConfig: LayerConfig[] = [];
  static centerExtentOnMap(extent, map) {
    const ext = MapService.getExt(extent, map);
    if (ext) {
      map.getView().fit(ext, { minResolution: MapService.getResolutionForScale(map, 1000) });
    }
  }

  static centerOnMapArea(extent, area, map) {
    const ext = MapService.getExt(extent, map);
    if (ext) {
      if (area !== undefined && area < 5) {
        map.getView().fit(ext);
      } else {
        map.getView().fit(ext, { minResolution: MapService.getResolutionForScale(map, 1000) });
      }
    }
  }

  static centerFeatureOnMap(geom, map) {
    MapService.centerOnMapArea(geom.getExtent(), geom.getArea(), map);
  }

  static getExt(extent, map): any {
    if (extent.filter(isFinite).length === 4) {
      const ext = Object.assign([], extent);
      const x = ext[2] - ext[0];
      const y = ext[3] - ext[1];
      const extentWindowPixel = 354; // sidebar sinistra + pannello
      const deZoomPixel = 150; // spazio da lasciare 'intorno' a una feature
      const mapX = map.getSize()[0] - (extentWindowPixel - 2 * deZoomPixel);
      const mapY = map.getSize()[1] - 2 * deZoomPixel;

      if (x / mapX > y / mapY) {
        // console.log('x > y');
        ext[0] = ext[0] - extentWindowPixel * x / mapX; // Extent Left
        ext[0] = ext[0] - deZoomPixel * x / mapX;
        ext[2] = ext[2] + deZoomPixel * x / mapX;
      } else {
        // console.log('y> x');
        ext[0] = ext[0] - extentWindowPixel * y / mapY; // Extent Left
        ext[1] = ext[1] - deZoomPixel * y / mapY;
        ext[3] = ext[3] + deZoomPixel * y / mapY;
      }
      return ext;
    }
    return null;
  }

  static getResolutionForScale(map: any, scale: number): number {
    const view = map.getView();
    const projection = view.getProjection();
    const inchesPrMeter = 39.3700787;
    const dpi = MapService.calcScreenDPI();
    const conversionFactor = (projection.getMetersPerUnit() * inchesPrMeter * dpi);
    return scale / conversionFactor;
  }

  static getMapScale(map, linearProjection): number {
    const dpi = MapService.calcScreenDPI();
    const view = map.getView();
    const proj = view.getProjection();
    const center = view.getCenter();
    const px = map.getPixelFromCoordinate(center);

    px[1] += 1;
    const coord = map.getCoordinateFromPixel(px);
    /*
        console.log('x\t', center[0], '\t', coord[0]);
        console.log('y\t', center[1], '\t', coord[1]);

        console.log('xt\t', transform(center, proj, 'EPSG:4326')[0], '\t', transform(coord, proj, 'EPSG:4326')[0]);
        console.log('yt\t', transform(center, proj, 'EPSG:4326')[1], '\t', transform(coord, proj, 'EPSG:4326')[1]);
    */
    let d2 = getDistance(
      transform(center, proj, 'EPSG:4326'),
      transform(coord, proj, 'EPSG:4326'));
    //  console.log('dt\t', d2);
    d2 *= (dpi || 96) / 0.0254; // 1 inch in metri
    //   console.log('dpt\t', d2);

    let d1 = Math.sqrt(Math.pow(coord[0] - center[0], 2) + Math.pow(coord[1] - center[1], 2));
    // console.log('d\t', d1);
    d1 *= (dpi || 96) / 0.0254; // 1 inch in metri
    //  console.log('dt\t ', d1);

    if (linearProjection) {
      return d1;
    } else {
      return d2;
    }
  }

  static setMapScale(map, scale, linearProjection) {
    const dpi = MapService.calcScreenDPI();
    if (map && scale) {
      let fac = scale;
      if (typeof (scale) === 'string') {
        fac = scale.split('/')[1];
        if (!fac) {
          fac = scale;
        }
        fac = fac.replace(/[^\d]/g, '');
        fac = Number(fac);
      }
      if (!fac) {
        return;
      }
      // Calculate new resolution
      const d = this.getMapScale(map, linearProjection);
      map.getView().setResolution(map.getView().getResolution() * fac / d);
      return fac;
    }
  }

  static calcScreenDPI(): number {
    const devicePixelRatio = window.devicePixelRatio || 1;
    const el = document.createElement('div');
    el.style.cssText = 'width: 1in';
    document.body.appendChild(el);
    const ppi = el.offsetWidth * devicePixelRatio;
    document.body.removeChild(el);
    // console.log(ppi);
    return ppi;
  }

  constructor(private gisConstants: GisCostants, public mapEvent: MapEvent, public caricaVettorialiCostants: CaricaVettorialiCostants,
    private toastComponent: ToastGisComponent, private mapidService: MapidService, private http: HttpClient) {
    this.rp = this.generateRandomPoints({ 'lat': 7.0785, 'lng': 51.4614 }, 9999999, 100);
  }

  static getLayerCode(layer): string {
    return layer.get(PropertyLayer.CODICE);
  }

  static getLayerFromLayerCodeProperty(currentMap: OlMap, codiceLayer: string): any {
    return this.getLayer(currentMap.getLayers(), codiceLayer);
  }

  refreshWmsLayerCampagna(): void {
    const map: OlMap = this.getMap(this.mapidService);

    const filter = new Map<PropertyLayer, PropertyLayer>([
      [PropertyLayer.FORMATO, PropertyLayer.FORMATO_WMS],
      [PropertyLayer.SPECIFICO_PER_CAMPAGNA, PropertyLayer.SI],
    ]);

    const filteredLayerConfig = this.getFilteredLayersConfig(filter);
    filteredLayerConfig.forEach(configLayer => {
      const layer = MapService.getLayerFromLayerCodeProperty(map, configLayer.codice);
      const source = layer.getSource();
      if (source instanceof ImageWMS || source instanceof TileWMS) {
        const newNomeLayerInMappa = configLayer.getNomeLayer(this.annoCampagna);
        layer.set(PropertyLayer.NOMELAYER, newNomeLayerInMappa);
        source.updateParams({ 'LAYERS': newNomeLayerInMappa });
        source.updateParams({ 'TIMESTAMP': Date.now() });
        source.refresh();
      }
    });

    const filter2 = new Map<PropertyLayer, any>([
      [PropertyLayer.FORMATO, PropertyLayer.FORMATO_WMS],
      [PropertyLayer.CQL_CAMPAGNA, null],
    ]);

    const filteredLayerConfig2 = this.getFilteredLayersConfig(filter2);
    filteredLayerConfig2.forEach(configLayer => {
      const layer = MapService.getLayerFromLayerCodeProperty(map, configLayer.codice);
      const source = layer.getSource();
      if (source instanceof ImageWMS || source instanceof TileWMS) {
        this.applyConfigFilter(configLayer, layer);
        source.updateParams({ 'TIMESTAMP': Date.now() });
        source.refresh();
      }
    });
  }

  refreshWmsLayer(codici: string[]): void {
    const map: OlMap = this.getMap(this.mapidService);

    codici
      .map(codice => MapService.getLayerFromLayerCodeProperty(map, codice))
      .map(layer => layer.getSource())
      .filter(source => source instanceof ImageWMS || source instanceof TileWMS)
      .forEach(source => {
        source.updateParams({ 'TIMESTAMP': Date.now() });
        source.refresh();
      });
  }

  applyConfigFilter(currentLayer: LayerConfig, tileLayer: any) {
    let currentCQL = '';

    if (currentLayer.mapProperty.get(PropertyLayer.PARAMS_FILTER)) {
      currentCQL = currentLayer.mapProperty.get(PropertyLayer.PARAMS_FILTER);
    }

    if (currentLayer.mapProperty.get(PropertyLayer.CQL_CAMPAGNA)) {
      if (currentCQL && currentCQL.trim()) {
        currentCQL = '(' + currentCQL + ') AND ';
      }
      currentCQL = currentCQL + currentLayer.mapProperty.get(PropertyLayer.CQL_CAMPAGNA) + ' = ' + this.annoCampagna;
    }
    if (currentCQL) {
      const params = tileLayer.getSource().getParams();
      params.CQL_FILTER = currentCQL;
      tileLayer.getSource().updateParams(params);
    }
  }

  getLayerFromCode(codiceLayer: string) {
    const map: OlMap = this.getMap(this.mapidService);

    return MapService.getLayerFromLayerCodeProperty(map, codiceLayer);
  }

  static getLayer(layers: Collection, codiceLayer: string): any {
    for (const layer of layers.getArray()) {
      if (layer instanceof Layer) {
        if (this.getLayerCode(layer) === codiceLayer) {
          return layer;
        }
      }
      if (layer instanceof LayerGroup) {
        const res = this.getLayer(layer.getLayers(), codiceLayer);
        if (res) {
          return res;
        }
      }
    }
  }

  setNewFeatureattributesFromFeature(newFeature: Feature, featTemplate: Feature, editLayer: VectorLayer): Feature {
    const layConfig = this.layerConfig.find(x => x.codice === editLayer.get(PropertyLayer.CODICE));
    const properties = featTemplate.getProperties();
    const id = this.getNewElementId();

    newFeature.setId(this.getNewFeatureId(editLayer, id));

    if (layConfig) {
      for (const prop of layConfig.attributi) {
        let val = prop.valoreDefault;
        if (properties[prop.nome]) {
          val = properties[prop.nome];
        }
        newFeature.set(prop.nome, val);
      }
    }

    const area = newFeature.values_.geometry.getArea();
    newFeature.set(CampoFeature.AREA, area);

    newFeature.set(editLayer.get(PropertyLayer.CAMPOFILTRO), editLayer.get(PropertyLayer.VALOREFILTRO));
    newFeature.set(CampoFeature.ID, id);
    return newFeature;
  }


  setNewFeatureattributes(feature: Feature, editLayer: VectorLayer) {
    const layConfig = this.layerConfig.find(x => x.codice === editLayer.get(PropertyLayer.CODICE));
    const id = this.getNewElementId();
    feature.setId(this.getNewFeatureId(editLayer, id));

    if (layConfig) {
      for (const prop of layConfig.attributi) {
        feature.set(prop.nome, prop.valoreDefault);
      }
    }

    const area = feature.values_.geometry.getArea();
    feature.set(CampoFeature.AREA, area);

    feature.set(editLayer.get(PropertyLayer.CAMPOFILTRO), editLayer.get(PropertyLayer.VALOREFILTRO));
    feature.set(CampoFeature.ID, id);
  }

  newFeaturePromise(mapService, newFeature) {
    return new Promise((resolve, reject) => {
      try {
        mapService.setNewFeatureattributes(newFeature, mapService.editLayer);
        console.log(newFeature);
        mapService.editLayer.getSource().addFeatures(newFeature);
        resolve(true);
      } catch (error) {
        reject(false);
      }
    });
  }

  getWfsFeatureFromId(codiceLayer, featureID) {
    const currentMap = this.getMap(this.mapidService);
    const vectorLayer = MapService.getLayerFromLayerCodeProperty(currentMap, codiceLayer);
    if (vectorLayer) {
      return vectorLayer.getSource().getFeatureById(featureID);
    } else {
      return null;
    }
  }

  getNewElementId(): string {
    this.idNew++;
    return 'New ' + this.idNew.toString().padStart(3, ' ');
  }


  getNewFeatureId(editLayer: VectorLayer, idNew: string): string {
    return editLayer.get(PropertyLayer.NOMELAYER) + '.' + Math.floor(Math.random() * 10000000000) + '-' + idNew;
  }

  centerOnFeature(codiceLayer, featureID): Feature {
    const currentMap = this.getMap(this.mapidService);
    const feat = this.getWfsFeatureFromId(codiceLayer, featureID);
    if (feat) {
      MapService.centerFeatureOnMap(feat.getGeometry(), currentMap);
    }
    return feat;
  }

  getWfsContestoLayers(): any[] {
    const currentMap = this.getMap(this.mapidService);
    const layers = new Array<any>();
    const layerContesto: LayerConfig[] = new Array<LayerConfig>();
    this.layerConfig.forEach(element => {
      if (element.mapProperty.get(PropertyLayer.FORMATO) === PropertyLayer.FORMATO_WFS) {
        layerContesto.push(element);
        layers.push(MapService.getLayerFromLayerCodeProperty(currentMap, element.codice));
        // console.log(this.layerConfig)

      }
    });

    return layers;
  }

  geWfsSnapLayers(layer) {
    return new Promise(((resolve, reject) => {
      try {
        if (layer.get(PropertyLayer.SNAP) === true) {
          resolve(true);
        } else {
          resolve(false);
        }
      } catch (error) {
        reject(error);
      }
    }));
  }

  getFilteredLayersConfig(filtri: Map<PropertyLayer, any>): LayerConfig[] {
    const currentMap = this.getMap(this.mapidService);
    let layersFiltered = this.layerConfig;
    for (const [key, val] of filtri) {
      console.log(key, val);
      if (val) {
        layersFiltered = layersFiltered.filter(x => x.mapProperty.get(key) === val);
      } else {
        layersFiltered = layersFiltered.filter(x => x.mapProperty.get(key));
      }
    }
    return layersFiltered;
  }

  ricaricaLavorazioneLayers(idLavorazione: any, isEditabile: boolean, resetZoom: boolean) {
    // ricarica i layer wfs di una lavorazione
    return new Promise((resolve, reject) => {
      const layerContesto: LayerConfig[] = new Array<LayerConfig>();

      this.layerConfig.forEach(element => {
        if (element.mapProperty.get(PropertyLayer.CONTESTO) === PropertyLayer.CONTESTO_LAVORAZIONE_EDITING &&
          element.mapProperty.get(PropertyLayer.FORMATO) === PropertyLayer.FORMATO_WFS) {
          layerContesto.push(element);
        }
      });

      if (idLavorazione) {
        console.log('ricaricaLavorazioneLayers isEditabile ', isEditabile);
        this.contesto = PropertyLayer.CONTESTO_LAVORAZIONE_EDITING;
        this.loadLayers(layerContesto, idLavorazione, isEditabile, resetZoom);
      } else {
        console.log('ricaricaLavorazioneLayers unloadLayers');
        this.contesto = null;
        this.unloadLayers(layerContesto);
      }
      this.idNew = 0;

      resolve(true);
    });
  }

  public refreshLayer(codiceLayer: any, idFilter: any) {
    const layConfig = this.layerConfig.find(x => x.codice === codiceLayer);
    const layerContesto: LayerConfig[] = new Array<LayerConfig>();
    layerContesto.push(layConfig);

    console.log('refreshLayer ', codiceLayer);

    let inEditing = false;

    if (this.editLayer && this.editLayer.get(PropertyLayer.CODICE)) {
      inEditing = true;
    }

    this.loadLayers(layerContesto, idFilter, inEditing, false);
  }

  public loadLayers(layerContesto: LayerConfig[], idFilter: any, inEditing: boolean,
    resetZoom: boolean, resetUndo = true, undoInteraction: any = null) {

    if (!resetUndo) {
      undoInteraction.blockStart();
    }

    layerContesto.forEach(element => {
      element.loaded = false;
    });

    const currentMap = this.getMap(this.mapidService);
    const newContestoEmitter = this.newContestoEmitter;
    const modifyFeatureEmitter = this.modifyFeatureEmitter;

    let zoomExtent = resetZoom;

    if (!inEditing) {
      this.editLayer = null;
    }

    for (const currentLayerConfig of layerContesto) {
      if (inEditing && currentLayerConfig.mapProperty.get(PropertyLayer.EDITABILE) &&
        currentLayerConfig.mapProperty.get(PropertyLayer.EDITABILE) === PropertyLayer.SI) {
        this.editLayer = MapService.getLayerFromLayerCodeProperty(currentMap, currentLayerConfig.codice);
      } else {
        // TODO crea proprietà
        this.wfsTraceLayer = MapService.getLayerFromLayerCodeProperty(currentMap, currentLayerConfig.codice);
      }

      const newFilterCAMPOFILTRO: Filter = equalToFilter(currentLayerConfig.mapProperty.get(PropertyLayer.CAMPOFILTRO), idFilter);
      let newFilter: Filter = this.applyConfigFilterWfs(newFilterCAMPOFILTRO, currentLayerConfig);

      // generate a GetFeature request
      const wsFeatureRequest = new WFS().writeGetFeature({
        featureNS: this.gisConstants.featureNamespace,
        projection: this.gisConstants.defaultProjection,
        featurePrefix: currentLayerConfig.workspace.replace('/', ''),
        featureTypes: [currentLayerConfig.getNomeLayer(this.annoCampagna)],
        outputFormat: 'application/json',
        filter: newFilter
      });

      const body = new XMLSerializer().serializeToString(wsFeatureRequest);

      const currentLayer = MapService.getLayerFromLayerCodeProperty(currentMap, currentLayerConfig.codice);
      currentLayer.set(PropertyLayer.VALOREFILTRO, idFilter);

      fetch(this.gisConstants.hostRichiesta + currentLayerConfig.url + currentLayerConfig.workspace + '/' + PropertyLayer.FORMATO_WFS, {
        method: 'POST',
        body: body,
      })
        .then(function (response) {
          return response.json();
        })
        .then(function (json) {

          const features = new GeoJSON().readFeatures(json);
          currentLayer.getSource().clear();
          currentLayer.getSource().addFeatures(features);

          if (resetUndo) {
            newContestoEmitter.emit(idFilter);
          }
          //console.log('loadedall');

          //console.log('modifyFeatureEmitter ' + currentLayerConfig.codice);
          modifyFeatureEmitter.emit(currentLayerConfig.codice);

          if (zoomExtent && currentLayer.getSource().getExtent() && currentLayer.getSource().getExtent().filter(isFinite).length === 4) {
            MapService.centerExtentOnMap(currentLayer.getSource().getExtent(), currentMap);
            zoomExtent = false;
          }
          currentLayerConfig.loaded = true;

          if (!resetUndo) {
            if (layerContesto.find(x => x.loaded === false) == null) {
              undoInteraction.blockEnd();
            }
          }
        });
    }
  }

  // non usato da sistemare
  public populateZoomScaleLavorazione(lavorazioneChanged: LavorazioneChangeStatus): LavorazioneChangeStatus {
    const viewCenter = this.mapEvent.map.getView().getCenter();
    const xUltimoZoom = viewCenter[0];
    const yUltimoZoom = viewCenter[1];
    const scalaUltimoZoom = Math.round(MapService.getMapScale(this.mapEvent.map, this.linearProjection));
    if (
      !lavorazioneChanged.lavorazioneSuolo.xUltimoZoom ||
      !lavorazioneChanged.lavorazioneSuolo.yUltimoZoom ||
      !lavorazioneChanged.lavorazioneSuolo.scalaUltimoZoom ||
      xUltimoZoom !== lavorazioneChanged.lavorazioneSuolo.xUltimoZoom ||
      yUltimoZoom !== lavorazioneChanged.lavorazioneSuolo.yUltimoZoom ||
      scalaUltimoZoom !== lavorazioneChanged.lavorazioneSuolo.scalaUltimoZoom) {
      lavorazioneChanged.lavorazioneSuolo.xUltimoZoom = xUltimoZoom;
      lavorazioneChanged.lavorazioneSuolo.yUltimoZoom = yUltimoZoom;
      lavorazioneChanged.lavorazioneSuolo.scalaUltimoZoom = scalaUltimoZoom;
      lavorazioneChanged.changed = true;

      console.log('Aggiorno lavorazione per modifica scala');
      return lavorazioneChanged;
    } else {
      return lavorazioneChanged;
    }
  }

  applyConfigFilterWfs(filter: Filter, currentLayer: LayerConfig): Filter {
    let newFilter = filter;
    let newFilterPARAM: Filter = null;

    if (currentLayer.mapProperty.get(PropertyLayer.PARAMS_FILTER)) {
      const params: String = String(currentLayer.mapProperty.get(PropertyLayer.PARAMS_FILTER));
      // gestiamo solo i filtri singoli
      if (params.includes('=')) {
        const left = params.substring(0, params.indexOf('=')).trim();
        let right = params.substring(params.indexOf('=') + 1).trim();
        right = right.replace(/'/g, ''); // per replace di tutte le occorenze altrimenti fa solo laprima
        newFilterPARAM = equalToFilter(left, right);
      }
      if (params.includes('<>')) {
        const left = params.substring(0, params.indexOf('<>')).trim();
        const right = params.substring(params.indexOf('<>') + 1).trim();
        newFilterPARAM = notFilter(equalToFilter(left, right));
      }
    }

    // non metto CQL_CAMPAGNA perché non è this.annoCampagna e basta il filter normale (su lavorazione) 
    if (newFilterPARAM) {
      newFilter = andFilter(newFilter, newFilterPARAM);
    }

    return newFilter;
  }

  private unloadLayers(layerContesto: LayerConfig[]) {
    const currentMap = this.getMap(this.mapidService);
    const newContestoEmitter = this.newContestoEmitter;
    const modifyFeatureEmitter = this.modifyFeatureEmitter;

    for (const currentLayerConfig of layerContesto) {
      const currentLayer = MapService.getLayerFromLayerCodeProperty(currentMap, currentLayerConfig.codice).getSource();
      currentLayer.set(PropertyLayer.VALOREFILTRO, null);
      currentLayer.clear();
      currentLayer.loaded = false;
      this.editLayer = null;
      this.wfsTraceLayer = null;
      newContestoEmitter.emit();
      modifyFeatureEmitter.emit(currentLayerConfig.codice);
    }
  }

  getNewContestoEmitter() {
    return this.newContestoEmitter;
  }



  /**
   * Create a map
   * @param id map id
   * @returns [ol.Map](https://openlayers.org/en/latest/apidoc/module-ol_Map-Map.html) the map
   */
  private createMap(id): OlMap {

    const mousePositionControl = new MousePosition({
      coordinateFormat: createStringXY(4),
      projection: this.gisConstants.defaultProjection,
      // comment the following two lines to have the mouse position
      // be placed within the map.
      className: 'custom-mouse-position',
      target: document.getElementById('mouse-position'),
    });

    /* const projectionSelect = document.getElementById('projection');
     projectionSelect.addEventListener('change', function (event) {
       mousePositionControl.setProjection((<HTMLInputElement>event.target).value);
     });
 */
    proj4.defs(this.gisConstants.defaultProjection, this.gisConstants.proj4Transformtion);
    proj4.defs('EPSG:3003', '+proj=tmerc +lat_0=0 +lon_0=9 +k=0.9996 +x_0=1500000 +y_0=0 +ellps=intl +towgs84=-117.23193982,-29.243113167,-7.955969586,-0.353472059,-2.843197642,-1.534991981,-17.961969706 +units=m +no_defs');
    register(proj4);

    this.projection = GetProjection(this.gisConstants.defaultProjection);
    this.projection.setExtent(this.extent);
    const map = new OlMap({
      target: 'map',
      layers: [],
      controls: DefaultControls().extend([
        new ScaleLine({}), mousePositionControl
      ]),
      view: new OlView({
        center: [669426.43, 5100596.95],
        zoom: 7,
        projection: this.projection,
        enableRotation: false
      })
    });
    return map;
  }

  /**
   * Get a map. If it doesn't exist it will be created.
   * @param id id of the map or an objet with a getId method (from mapid service), default 'map'
   */
  getMap(id): OlMap {
    id = ((id && id.getId) ? id.getId() : id) || 'map';
    // Create map if not exist
    if (!this.map[id]) {
      this.map[id] = this.createMap(id);
    }
    // return the map
    return this.map[id];
  }

  /** Get all maps
   * NB: to access the complete list of maps you should use the ngAfterViewInit() method to have all maps instanced.
   * @return the list of maps
   */
  getMaps() {
    return this.map;
  }

  /** Get all maps
   * NB: to access the complete list of maps you should use the ngAfterViewInit() method to have all maps instanced.
   * @return array of maps
   */
  getArrayMaps() {
    return Object.values(this.map);
  }


  markerStyle(testo: String) {
    return new Style({
      image: new Icon({
        src: './assets/img/gis-icons/ping.svg',
        scale: 3,
        anchor: [0.5, 1],
      }),
      text: new Text({
        font: '12px Titillium Web,bold,sans-serif',

        fill: new Fill({ color: '#000' }),
        stroke: new Stroke({
          color: '#fff', width: 6
        }),
        textAlign: 'center',
        textBaseline: 'top',
        text: testo,
        offsetY: 8
      })
    });
  }


  public olPtsLayer() {
    const features = [];
    this.rp.forEach(element => {
      const coords = fromLonLat([parseFloat(element.lat), parseFloat(element.lng)]);
      features.push(
        new Feature({
          geometry: new Point(coords),
        })
      );
    });
    return features;
  }


  generateRandomPoints(center, radius, count) {
    const points = [];
    for (let i = 0; i < count; i++) {
      points.push(this.generateRandomPoint(center, radius, i));
    }
    return points;
  }

  generateRandomPoint(center, radius, i) {
    const x0 = center.lng;
    const y0 = center.lat;
    const rd = radius / 111300;

    const u = Math.random();
    const v = Math.random();

    const w = rd * Math.sqrt(u);
    const t = 2 * Math.PI * v;
    const x = w * Math.cos(t);
    const y = w * Math.sin(t);

    const xp = x / Math.cos(y0);
    return { 'lat': y + y0, 'lng': xp + x0, 'id': i };
  }

  centerMap(extent) {
    const map: OlMap = this.getMap(this.mapidService);
    MapService.centerExtentOnMap(extent, map);
  }

  centerMapArea(extent, area) {
    const map: OlMap = this.getMap(this.mapidService);
    MapService.centerOnMapArea(extent, area, map);
  }

  checkWfsExists(codiceLayer: string): boolean {
    const map: OlMap = this.getMap(this.mapidService);
    const currentLayer = MapService.getLayerFromLayerCodeProperty(map, codiceLayer);
    if (currentLayer && currentLayer.getSource()) {
      return true;
    } else {
      return false;
    }
  }

  getFeaturesFromWfs(codiceLayer: string): Feature[] {
    const map: OlMap = this.getMap(this.mapidService);

    const currentLayer = MapService.getLayerFromLayerCodeProperty(map, codiceLayer);

    if (currentLayer && currentLayer.getSource() && currentLayer.getSource().getFeatures()) {
      return currentLayer.getSource().getFeatures();
    } else {
      return new Feature[0]();
    }
  }

  getModifyFeatureEmitter() {
    return this.modifyFeatureEmitter;
  }

  getReloadPoligoniDichiaratiEmitter() {
    return this.reloadPoligoniDichiaratiEmitter;
  }

  removeDuplicateCoordinates(feature) {
    const coordinates = feature.getGeometry().getCoordinates()[0];
    const dictionary = {};
    const filteredPolygonCoordinates = [];
    filteredPolygonCoordinates.push(coordinates[0]);
    for (let i = 1; i < (coordinates.length - 1); i++) {
      const key = coordinates[i][0] + '' + coordinates[i][1];
      if (!dictionary[key]) {
        dictionary[key] = coordinates[i];
        filteredPolygonCoordinates.push(coordinates[i]);
      }
    }
    filteredPolygonCoordinates.push(coordinates[(coordinates.length - 1)]);
    feature.getGeometry().setCoordinates([filteredPolygonCoordinates]);
  }

  addVectorLayerToMap(vectorLayer) {
    // andare ad aggiungere ad una mappa  se l'oggeto map da api
    const map = this.getMap(this.mapidService);
    map.addLayer(vectorLayer);
  }
  removeLayerFromMap(vectorLayer) {
    const map = this.getMap(this.mapidService);
    map.removeLayer(vectorLayer);
  }

  public getRelatedLayers(mapEvent: MapEvent, l) {
    const layers = mapEvent.map.getLayers().getArray().filter(element => element instanceof LayerGroup);

    return layers.map(element => (element.values_.layers.array_).map(element => element.values_)
      .filter(element => element !== null && element.relazioneConLayer === l.values_.codice))
      .filter(element => element.length > 0);
  }


  public getLayerFromContenutoInformativo(contenutoInformativo: PropertyLayer) {
    let outputLayers: TileLayer[] = [];
    const map: OlMap = this.getMap(this.mapidService);

    try {

      for (let i = 0; i < map.getLayers().getArray().length; i++) {
        let element = map.getLayers().getArray()[i];
        if (element instanceof LayerGroup) {

          let listElem = element.getLayers().getArray().filter(l =>
            l.get(PropertyLayer.CONTENUTO_INFORMATIVO) === contenutoInformativo);
          for (let i = 0; i < listElem.length; i++) {
            outputLayers.push(listElem[i]);
          }
        } else {
          if (element.get(PropertyLayer.CONTENUTO_INFORMATIVO) == contenutoInformativo) {
            outputLayers.push(element);
          }
        }
      }
      return outputLayers;
    } catch (error) {
      console.log(error);
    } finally {
      console.log('finally');
    }
  }

  createPoint(coordinate, scala) {
    const point = new Point(coordinate, {
      minResolution: MapService.getResolutionForScale(this.mapEvent.map, scala)
    });
    return point;
  }
}


