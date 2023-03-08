import { getLength } from 'ol/sphere';
import { Feature } from 'ol/Feature';
import { MisuraUtils } from './../../components/mappa/gisTools/misura.utils';
import { GisStyles } from './../GisStyles';
import { PropertyLayer } from 'src/app/gis/shared/PropertyLayer.enum';
import { EventEmitter, HostListener, Injectable } from '@angular/core';
import { Vector as VectorLayer } from 'ol/layer';
import Select from 'ol/interaction/Select';
import Snap from 'ol/interaction/Snap';
import { enumListener } from '../../components/mappa/gisTools/enumListeners';
import { enumTool } from '../../components/mappa/gisTools/enumTools';
import { MapService } from '../../components/mappa/map.service';
import { MapidService } from '../../components/mappa/mapid.service';
import { MapEvent } from '../MapEvent';
import { ToolBarEvent } from './ToolBarEvent';
import { Interaction } from 'ol/interaction';
import { OlMap } from 'ol/Map';
import { Observable, Subject } from 'rxjs';
import { CampoFeature } from '../../shared/CampoFeature';
import { CodUso, FeatureWorkspace, StatoColt } from '../featureWorkspace';
import Control from 'ol/control/Control';
import { GisCostants } from '../gis.constants';
import GeometryType from 'ol/geom/GeometryType';

@Injectable()
export class ToolBarService extends ToolBarEvent {
  selected = null;
  canIdentify = false;
  private _editToolGroup: enumTool[];
  public listenerGroup: enumListener[];
  public activeListener: enumListener;
  public activeTool: enumTool;
  manageActivateEditToolEmitter: EventEmitter<boolean> = new EventEmitter();
  isEditToolActive = false;
  highLightToolEmitter: EventEmitter<any> = new EventEmitter();
  rows: Subject<boolean> = new Subject<boolean>();
  rowsSelected: Subject<any> = new Subject<any>();
  misuraStyle = false;
  snapTolerance = this.gisCostants.snapTolerance;
  snapActive = true;
  snapEdge: boolean = true;
  snapVertex: boolean = true;

  constructor(private mapService: MapService, public toolbarEvent: ToolBarEvent, public mapEvent: MapEvent,
    public gisCostants: GisCostants, public gisStyles: GisStyles,
    public misuraUtils: MisuraUtils) {
    super();
  }

  private static getIProperty(tool: enumTool, i: number) {
    return tool + '_' + i;
  }

  private static getToolFromIProperty(toolProperty: string): enumTool {
    if (toolProperty.includes('_')) {
      toolProperty = toolProperty.substr(0, toolProperty.lastIndexOf('_'));
    }
    return <enumTool>toolProperty;
  }

  public get editToolGroup(): enumTool[] {
    if (!this._editToolGroup) {
      this._editToolGroup = new Array<enumTool>();
    }
    return this._editToolGroup;
  }

  static getInteractionName(interaction: Interaction): string {
    const name = interaction.get('name');
    return name;
  }

  static getControlName(control: Control): string {
    const name = control.get('name');
    return name;
  }

  static setInteractionProperties(interaction: Interaction, interactionName: string, interactionCaption: string = null) {
    interaction.set('name', interactionName);

    if (!interactionCaption) {
      interactionCaption = 'Attiva Tool ' || interactionName;
    }
  }

  static getInteractionCaptionFomName(map: OlMap, toolName: enumTool, toolCaption: string): string {
    const interactionsFound = ToolBarService.getInteractionFomName(map, toolName);
    if (interactionsFound) {
      return 'Disattiva Tool ' + toolCaption;
    } else {
      return 'Attiva Tool ' + toolCaption;
    }
  }

  static getInteractionsFomName(map: OlMap, toolName: string): any {
    return map.getInteractions().getArray().filter(interaction => ToolBarService.getInteractionName(interaction) === toolName);
  }

  static getInteractionFomName(map: OlMap, toolName: string): any {
    const res = ToolBarService.getInteractionsFomName(map, toolName);
    if (res && res.length > 0) {
      return res[0];
    } else {
      return null;
    }
  }

  getInteractionFomName(toolName: enumTool): any {
    const map = this.mapEvent.map;
    // console.log('test numero invocazioni')
    return ToolBarService.getInteractionFomName(map, toolName);
  }


  getControlFromName(controlName: String): any {
    const map = this.mapEvent.map;
    return map.getControls().getArray().filter(control => ToolBarService.getControlName(control) === controlName);
  }

  public isInInteraction(toolName: enumTool): boolean {
    return (this.getInteractionFomName(toolName)) ? true : false;
  }

  public isInControl(controlName: String): boolean {
    return (this.getControlFromName(controlName)) ? true : false;
  }

  rimuoviListaInteraction(toolsToRemove: string[], clearSelection, diabilitaToolEditing): boolean {
    try {
      const map = this.mapEvent.map;
      if (toolsToRemove) {
        console.log('toolsToRemove', toolsToRemove);
        const interactionToRemove = [];
        for (const interaction of map.getInteractions().getArray()) {
          const toolName = ToolBarService.getInteractionName(interaction);
          if (toolName) {
            if (toolsToRemove.includes(toolName) ||
              (toolName.startsWith(enumTool.snapEl) && toolsToRemove.includes(enumTool.snapEl))) {
              interactionToRemove.push(interaction);
            }
          }
        }

        for (const interaction of interactionToRemove) {
          this.rimuoviInteraction(map, interaction, clearSelection);
        }
      } else {
        if (clearSelection) {
          const select = this.getInteractionFomName(enumTool.selectFeatures);
          if (select && select.getFeatures()) {
            select.getFeatures().clear();
            this.getSelectChangeEmitter().emit();
          }
          const selectEl = this.getInteractionFomName(enumTool.selectEl);
          if (selectEl && selectEl.getFeatures()) {
            selectEl.getFeatures().clear();
            // no perché non è un 'vero'  this.getChangeSelectEmitter().emit();
          }
        }
      }

      if (diabilitaToolEditing) {
        this.isEditToolActive = false;
        console.log('manageActivateEditToolEmitter.emit ', this.isEditToolActive);
        this.manageActivateEditToolEmitter.emit(this.isEditToolActive);
      }

      return true;
    } catch (error) {
      return false;
    }
  }

  private rimuoviInteraction(map: any, interaction: any, clearSelection: boolean) {
    const name = ToolBarService.getInteractionName(interaction);
    // if (name !== 'snapEl_LayerCaricati') {
    const toolToRemoveEnum = ToolBarService.getToolFromIProperty(name);
    // Se misuraEl viene rimosso bisogna eliminare il layer di riferimento
    if (name === enumTool.misuraEl) {
      this.removeLayerMisura();
    }
    console.log('Rimuovi el ' + name);

    switch (toolToRemoveEnum) {
      case enumTool.selectFeatures: {
        interaction.setActive(false);
        if (clearSelection) {
          console.log('clearselection');
          interaction.getFeatures().clear();
          this.getSelectChangeEmitter().emit();
        }
        break;
      }
      case enumTool.selectFeaturesBox:
        interaction.setActive(false);
        break;
      case enumTool.selectEl: {
        interaction.getFeatures().clear();
        // no perché non è un 'vero' select this.getSelectChangeEmitter().emit();
        map.removeInteraction(interaction);
        break;
      }
      default: {
        map.removeInteraction(interaction);
        break;
      }
    }

    if (this.activeTool === toolToRemoveEnum) {
      this.activeTool = null;
    }
    // }
  }

  addInteraction(interactions: any[], toolIsExclusive: boolean, addSnap: boolean, clearSelection: boolean, toolbar: enumTool[]): boolean {
    try {
      if (toolIsExclusive) {
        this.rimuoviListaInteraction(toolbar, clearSelection, false);
        console.log('Disattivo activeListener per interaction attiva', this.activeListener, interactions);
        this.activeListener = null;
      } else {
        for (const interaction of interactions) {
          this.rimuoviListaInteraction([ToolBarService.getInteractionName(interaction)], clearSelection, false);
        }
      }

      const map = this.mapEvent.map;
      if (interactions) {
        for (const interaction of interactions) {
          const name = ToolBarService.getInteractionName(interaction);
          const toolEnum = ToolBarService.getToolFromIProperty(name);
          if (this.isInInteraction(toolEnum) && toolEnum === enumTool.selectFeatures) {
            interaction.setActive(true);
          } else {
            map.addInteraction(interaction);
            if (toolIsExclusive) {
              this.activeTool = toolEnum;
            }
          }
          if (!this.isEditToolActive && toolIsExclusive) {
            this.isEditToolActive = true;
            console.log('manageActivateEditToolEmitter.emit ', this.isEditToolActive);
            this.manageActivateEditToolEmitter.emit(this.isEditToolActive);
          }
        }
      }
      if (addSnap) {
        this.addSnap(map);
      }
      return true;
    } catch (error) {
      return false;
    }
  }

  getManageActivateEditToolEmitter() {
    return this.manageActivateEditToolEmitter;
  }

  addInteractionHighLightFeature() {
    const _self = this;
    const map = this.mapEvent.map;
    map.on('pointermove', function (e) {
      _self.highLightToolEmitter.emit(e.pixel);
    });
  }

  dragBoxFeature(features, elementiWorkspace) {
    const _self = this;
    return new Promise(((resolve, reject) => {
      const map = this.mapEvent.map;
      map.on('pointermove', function (e) {
        if (features && features.array_ && features.array_.length > 0) {
          features.getArray().forEach(elem => {
            const indexChanged = elementiWorkspace.findIndex(element => element.id === elem.get(CampoFeature.ID));
            if (indexChanged >= 0) {
              if (elementiWorkspace[indexChanged] && !elementiWorkspace[indexChanged]['multiSelected']) {
                elementiWorkspace[indexChanged]['multiSelected'] = true;
              }
            }
          });
        } else {
          elementiWorkspace.forEach(elem => {
            elem['multiSelected'] = false;
          });
          resolve(elementiWorkspace);
        }
      });
    }));
  }

  getHighLigthToolEmitter() {
    return this.highLightToolEmitter;
  }

  getSelectChangeEmitter() {
    return this.selectChangeEmitter;
  }

  canUseTool(currentTool: enumTool, customCanUse: boolean = true) {
    return customCanUse && this.mapService.editLayer &&
      (!this.exclusiveActiveTool || this.exclusiveActiveTool === currentTool);
  }

  getSelectedFeatures(): Observable<any> {
    return this.rowsSelected.asObservable();
  }

  getSelectedFeaturesLength() {
    const selected = this.getInteractionFomName(enumTool.selectFeatures);
    if (selected && selected.getFeatures() && selected.getFeatures().getArray()
      && selected.getFeatures().getArray().length > 0) {
      return selected.getFeatures().getArray().length;
    } else {
      return 0;
    }
  }

  getNotesFromFeature(featureList: Feature) {
    if (featureList.length === 1) {
      return featureList[0].get(CampoFeature.NOTE);
    } else {
      return '';
    }
  }

  removeFeaturesFromSelect() {
    const selected = this.getInteractionFomName(enumTool.selectFeatures);
    if (selected && selected.getFeatures() && selected.getFeatures().getArray()
      && selected.getFeatures().getArray().length > 0) {
      selected.getFeatures().array_ = [];
      return selected;
    }
  }
  sendFeatures(data) {
    this.rowsSelected.next(data);
  }

  mapElementiWorkspace(features) {
    return new Promise(((resolve, reject) => {
      const elementiWorkspace = [];
      for (const feat of features) {
        const newEl = new FeatureWorkspace(
          feat.getId(),
          feat.get(CampoFeature.ID),
          new CodUso(feat.get(CampoFeature.COD_USO_SUOLO), feat.get(CampoFeature.USO_SUOLO_DES)),
          new StatoColt(feat.get(CampoFeature.STATO_COLT).toString(), feat.get(CampoFeature.STATO_COLT_DES)),
          true,
          feat.get(CampoFeature.AREA),
          false,
          null,
          true,
          false,
          feat.get(CampoFeature.NOTE),
        );
        elementiWorkspace.push(newEl);
      }
      resolve(elementiWorkspace);
    }));
  }

  mapSingleElementWorkspace(feature): any {
    const newEl = new FeatureWorkspace(
      feature.getId(),
      feature.get(CampoFeature.ID),
      new CodUso(feature.get(CampoFeature.COD_USO_SUOLO), feature.get(CampoFeature.USO_SUOLO_DES)),
      new StatoColt(feature.get(CampoFeature.STATO_COLT).toString(), feature.get(CampoFeature.STATO_COLT_DES)),
      true,
      feature.get(CampoFeature.AREA),
      false,
      null,
      true,
      false,
      '');
    return newEl;
  }

  private addSnap(map: OlMap) {
    const self = this;
    for (const layer of this.mapService.getWfsContestoLayers()) {
      if (layer.get(PropertyLayer.SNAP) === true) {
        const interaction = new Snap({
          source: layer.getSource(),
          pixelTolerance: this.snapTolerance,
          edge: this.snapEdge,
          vertex: this.snapVertex,
        });
        const snapLayerName = enumTool.snapEl + '_' + layer.get(PropertyLayer.CODICE);
        ToolBarService.setInteractionProperties(interaction, snapLayerName);
        console.log('addInteraction snap ', snapLayerName);
        interaction.values_.active = false;
        map.addInteraction(interaction);
      }

      /*
      this.mapService.geWfsSnapLayers(layer).then(isSnappable => {
        if (isSnappable === true) {
          const snapLayerName = enumTool.snapEl + '_' + layer.get(PropertyLayer.CODICE);
          ToolBarService.setInteractionProperties(interaction, snapLayerName);
          console.log('addInteraction snap ', snapLayerName);
          interaction.values_.active =
          map.addInteraction(interaction);
          self.refreshSnapOnLayer(layer.get(PropertyLayer.CODICE)); // TMP fa un sacco di chiamate, ma se messo in fondo viene eseguito prima
        }
      });*/
    }

    if (self.mapEvent.checkLayerCaricati.length > 0) {
      self.mapEvent.checkLayerCaricati[0].getLayers().getArray().forEach(element => {
        self.addSnapInteraction(element, this.mapEvent.map);
      });
    }

    // attiva snap
    // gestisci
    this.refreshSnapOnAllLayers();

    /*self.mapEvent.checkLayerCaricati[0].getLayers().getArray().forEach(element => {
      self.setSnapOnLayer1(element, this.mapEvent.map.interactions.getArray(), true);
    });*/
  }

  refreshSnapOnAllLayers(changeActivation = false) {
    if (!this.mapEvent.map.interactions.getArray().some(interaction => interaction.values_.name
      && interaction.values_.name.search(enumTool.snapEl) >= 0)) {
      return;
    }
    if (changeActivation) {
      this.snapActive = !this.snapActive;
    }
    const listaInteractionSnap = this.mapEvent.map.interactions.getArray()
      .filter(interaction1 => interaction1.values_.name && interaction1.values_.name.search(enumTool.snapEl) >= 0);
    console.log('refreshSnapOnAllLayers', listaInteractionSnap);
    this.setSnapOnAllLayers(this.mapEvent.map.getLayers().getArray(), listaInteractionSnap);
  }

  setSnapOnAllLayers(layerList, listaInteraction) { // copiato da occhialini
    // console.log('setSnapOnAllLayers', layerList);
    for (let i = 0; i < layerList.length; i++) {
      if (layerList[i].values_.layers) {
        this.setSnapOnAllLayers(layerList[i].getLayers().getArray(), listaInteraction);
      } else {
        if (layerList[i].get(PropertyLayer.CODICE) && layerList[i].get(PropertyLayer.SNAP) === true) {
          this.setSnapOnLayer(layerList[i], listaInteraction);
        }
      }
    }
  }

  public addSnapInteraction(layer: any, map: OlMap) {
    if (layer.get(PropertyLayer.SNAP) === true) {
      const interaction = new Snap({
        source: layer.getSource(),
        pixelTolerance: this.snapTolerance,
        edge: this.snapEdge,
        vertex: this.snapVertex,
      });
      const snapLayerName = enumTool.snapEl + '_' + layer.get(PropertyLayer.CODICE);
      ToolBarService.setInteractionProperties(interaction, snapLayerName);
      console.log('addInteraction snap1 ', snapLayerName);
      map.addInteraction(interaction);
      interaction.values_.active = false;
      this.setSnapActivationOnLayer(layer, interaction);
    }
  }

  removeLayerMisura() {
    const getVectorLayers = this.mapEvent.map.getLayers().getArray().filter(layer => layer instanceof VectorLayer);
    getVectorLayers.forEach(layer => {
      // se esiste viene rimosso
      if (layer.get(PropertyLayer.CODICE) === 'MISURA_LAYER') {
        this.mapEvent.map.removeLayer(layer);
      }
    });
  }

  addLayerMisura() {

  }


  /**
   * Metodo per l'abilitazione in mappa dello snap sul layer passato come parametro.
   * Nel caso in cui listaInteraction non contenga un'interaction di snap per il layer indicato,
   * aggiunge l'interaction sulla base della proprietà "SNAP" definita sul Layer.
   * @param layer: layer sul quale valutare lo snap
   * @param listaInteraction: lista delle interaction da valutare
  */
  setSnapOnLayer(layer, listaInteraction) {
    listaInteraction.forEach(interaction => {
      // CASO 1. Esiste un'interaction di snap in mappa relativa al layer
      if (interaction.values_.name.search(layer.get(PropertyLayer.CODICE)) >= 0) {
        console.log('layer snap2 ', layer.get(PropertyLayer.CODICE), layer.get(PropertyLayer.VISIBLE));
        this.setSnapActivationOnLayer(layer, interaction);
        this.refreshSnapParameters(interaction);
        // verifice meglio e poi scommenta return;
      } /*else {
          // CASO 2. Non esiste un'interaction di snap in mappa relativa al layer
          if (l.values_.snap) {
            // CASO 2.1 Il layer è configurato come snappabile
            if (l.values_.visible) {
              // CASO 2.1.2 Il Layer è attivo in mappa
              const newInteraction = new Snap({
                source: l.getSource(),
                pixelTolerance: this.snapTolerance
              });
              const snapToolName = enumTool.snapEl;
              const snapLayerName = snapToolName + '_' + l.values_.codice;
              newInteraction.set('name', snapLayerName);
              if (isCaricati) {
                console.log('setSnap name', snapLayerName, interaction.values_.name);
                newInteraction.values_.active = this.snapActive;
                this.mapEvent.map.addInteraction(newInteraction);
              }
            } else {
              interaction.value = false;
            }
          }
        }*/
    });
  }

  private setSnapActivationOnLayer(layer: any, interaction: any) {
    if (!layer.get(PropertyLayer.VISIBLE)) {
      // CASO 1.1. Il layer non è attivo in mappa
      console.log('interaction1 snap ', false, interaction.values_.name);
      interaction.values_.active = false;
    } else {
      // CASO 2.2. Il layer è attivo in mappa
      console.log('interaction2 snap ', this.snapActive, interaction.values_.name);
      interaction.values_.active = this.snapActive;
    }
    console.log('layer snap ', layer.get(PropertyLayer.CODICE), layer.get(PropertyLayer.VISIBLE), interaction.values_.active);
  }

  private refreshSnapParameters(interaction: any) {
    console.log('refreshSnapParameters1', interaction);
    interaction.pixelTolerance_ = this.snapTolerance;
    interaction.edge_ = this.snapEdge;
    interaction.vertex_ = this.snapVertex;
    console.log('refreshSnapParameters2', interaction);
  }

  // ritorna true se si sta disegnando una feature con un tool di Draw
  // includeDrawRegular ritorna true se si sta disegnando con qualsiasi tool incluso drawRegular
  // includeDrawBuffer ritorna true se si sta disegnando con qualsiasi tool incluso drawBuffer
  // includeMisura ritorna true se si sta disegnando con qualsiasi tool incluso drawBuffer
  isInDrawing(includeDrawRegular: boolean, includeDrawBuffer: boolean, includeMisura: boolean): boolean {
    if (this.activeTool) {
      if (this.activeTool === enumTool.misuraEl && !includeMisura) {
        return false;
      }
      if (this.activeTool === enumTool.drawBuffer && !includeDrawBuffer) {
        return false;
      }
      if (this.activeTool === enumTool.drawRegularEl && !includeDrawRegular) {
        return false;
      }

      const activeTool = this.getInteractionFomName(this.activeTool);
      // diverso da null se tool è di tipo Draw e sto disegnando
      if (activeTool.sketchFeature_) {
        return true;
      }

      // diverso da null se tool è di tipo DrawRegular e sto disegnando
      if (this.activeTool === enumTool.drawRegularEl
        && includeDrawRegular
        && activeTool.feature_
        && !activeTool.feature_.getProperties().ID) {
        return true;
      }

      return false;
    }
  }

  refreshStyle() {
    const editLayer: VectorLayer = this.mapService.editLayer;
    const misuraLayer: VectorLayer = this.mapService.getLayerFromCode('MISURA_LAYER');
    if (editLayer) {
      editLayer.setStyle(editLayer.getStyle());
    }
    if (misuraLayer) {
      misuraLayer.setStyle(misuraLayer.getStyle())
    }

    if (this.activeTool) {
      const activeTool = this.getInteractionFomName(this.activeTool);
      if (activeTool.sketchFeature_) {
        activeTool.sketchFeature_.setStyle(activeTool.sketchFeature_.getStyle());
      }
      if (activeTool.feature_) {
        activeTool.feature_.setStyle(activeTool.feature_.getStyle());
      }
    }
  }

  getSelectedAdl(layer) {
    let self = this;
    let select = new Select({
      type: GeometryType.POLYGON,
      style: function (feature) {
        return self.misuraUtils.styleFunction(feature, true, GeometryType.POLYGON,
          self.misuraStyle, self.gisStyles.modifyPolygonStyle);
      },
      layers: [layer]
    });
    return select;
  }
  /**
   * intercetta gli eventi e nè determina le condizioni
   * @param e: evento al click
   * @param originalEvent: lista degli eventi
   * buttons === 1 Tasto Sinistro
   * buttons === 0 Tasto Destro
  **/
  conditionClick(e) {
    if (e.originalEvent.buttons === 1) {
      return true;
    } else {
      return false;
    }
  }

}
