import { ToolBarService } from './../../shared/ToolBar/toolBar.service';
import { GisStyles } from './../../shared/GisStyles';
import { WindowsEvent } from './../../shared/WindowsEvent';
import { MapEvent } from './../../shared/MapEvent';
import { Component, ElementRef, EventEmitter, HostListener, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges, ViewChild, ViewEncapsulation } from '@angular/core';
import { CodUso, FeatureWorkspace, StatoColt } from '../../shared/featureWorkspace';
import { CampoFeature } from '../../shared/CampoFeature';
import { MapService } from '../mappa/map.service';
import { SortEvent } from 'primeng-lts/api';
import { enumTool } from '../mappa/gisTools/enumTools';
import { LavorazioneWorkspaceService } from '../../services/LavorazioneWorkspace.service';
import Style from 'ol/style/Style';
import Fill from 'ol/style/Fill';
import Stroke from 'ol/style/Stroke';
import { PropertyLayer } from '../../shared/PropertyLayer.enum';
import Select from 'ol/interaction/Select';
import { LavorazioniEvent } from '../../shared/LavorazioniEvent';
import * as cloneDeep from 'lodash/cloneDeep';


@Component({
  selector: 'gis-lavorazioneWorkspace',
  templateUrl: './lavorazioneWorkspace.component.html',
  styleUrls: ['./lavorazioneWorkspace.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class LavorazioneWorkspaceComponent implements OnInit, OnDestroy, OnChanges {

  columns: any[];
  filterPoligoniWorkspace: any[];
  listaCodUso: string;
  listaStatoColt: string;
  selected = null;
  @Output() totalsPoligoniWorkspaceOutput = new EventEmitter<number>();
  @Output() poligoniWorkspaceOutput = new EventEmitter<any>();
  @Input() idLavorazione: any;
  @Input() contextView: string;
  @Input() elementiWorkspaceInput: any[];
  @ViewChild('zoomBtn') zoomBtn: any;

  selectInteraction: Select;
  flagModificaMultiFeature = false;
  codUsoMultiFeature: CodUso;
  statoColtMultiFeature: StatoColt;
  elementiWorkspace: FeatureWorkspace[];
  reloadedElementiWorkspace: FeatureWorkspace[];
  subscriptionModifyFeature: any;
  subscriptionHighLigthFeature: any;
  subscriptionManageHighLigthFeature: any;
  subscriptionSelectFeature: any;
  subscriptionDragSelect: any;
  subscriptionFeatureChange: any;
  loading: boolean;
  codUsoSuoloList = sessionStorage.getItem('codUsoSuoloList');
  statoColtList = sessionStorage.getItem('statoColtList');
  firstLoad = true;
  @Input() tabAccordionWorkspaceStep4: number;
  totalsPoligoniWorkspace = -1;
  showPortal = false;
  windowColumns: { field: string; header: string; width: string; }[];
  selectedFeatures: any;
  triggerSelect: HTMLElement = document.getElementById('selectFeaturesTool') as HTMLElement;
  multiClickActived = false;
  editaCelle: boolean;
  constructor(private mapService: MapService, public mapEvent: MapEvent, private toolBarService: ToolBarService,
    private lavorazioneWorkspaceService: LavorazioneWorkspaceService, public windowsEvent: WindowsEvent, private gisStyles: GisStyles,
    public lavorazioniEvent: LavorazioniEvent) {
    this.subscribeMapSelect();
  }
  @HostListener('document:click', ['$event'])
  onClickEvent(event: MouseEvent) {
    const target = event.target || event.srcElement;
    if ((target['parentElement'] && target['parentElement'].classList[0] === 'select') ||
      (target['offsetParent'] && target['offsetParent'].classList[0] === 'extWindow')) {
      this.cleanAllHighLigtStyles();
    }
  }
  ngOnInit() {
    if (this.windowsEvent.fromWindow) {
      this.elementiWorkspace = JSON.parse(sessionStorage.getItem('elementiWorkspace'));
      this.disabilitaModificaPoligoniWorkspace();
      this.setElementiWorkspace();
      this.loading = false;
    } else {
      this.editaCelle = this.lavorazioniEvent.editaCelleWorkSpace;
      this.setElementiWorkspace();
    }

    // Columns tabella
    this.columns = [
      { field: 'id', header: 'ID', width: '10%' },
      { field: 'codUso', header: 'USO', width: '22%' },
      { field: 'statoColt', header: 'STATO', width: '20%' },
      { field: 'area', header: 'AREA (MQ)', width: '40%' },
      { field: 'multiSelectedOrd', class: 'fa fa-hand-pointer ui-table-thead-lav-ord-multi-sel' },
    ];

    // Columns tabella in finestra
    this.windowColumns = [
      { field: 'zoom', header: '', width: '5%' },
      { field: 'id', header: 'ID', width: '10%' },
      { field: 'codUso', header: 'USO', width: '10%' },
      { field: 'codUsoDesc', header: 'DESCRIZIONE USO', width: '25%' },
      { field: 'statoColt', header: 'STATO', width: '10%' },
      { field: 'statoColtDesc', header: 'DESCRIZIONE STATO', width: '25%' },
      { field: 'area', header: 'AREA (MQ)', width: '15%' },
    ];

    this.getCodificheSuolo();

    if (this.firstLoad && this.mapService.layerConfig.find(x => x.codice === PropertyLayer.CODICE_LAYER_WORKSPACE).loaded) {
      // console.log('Workspace firstload');
      this.realoadElementi(PropertyLayer.CODICE_LAYER_WORKSPACE);
    }

    const _self = this;
    this.subscriptionSelectFeature = this.toolBarService.getSelectChangeEmitter()
      .subscribe(() => {
        // console.log('getSelectChangeEmitter');
        _self.selectInteraction = _self.toolBarService.getInteractionFomName(enumTool.selectFeatures);

        if (_self.selectInteraction) {
          _self.cleanAllHighLigtStyles();
          return;
        }
      });
  }

  private realoadElementi(tipoOperazione: any) {
    if (tipoOperazione === PropertyLayer.CODICE_LAYER_WORKSPACE) {
      this.loading = true;
      this.firstLoad = false;
      this.realoadElementiWorkspaceDaWfs().then(value => {
        // console.log('subscriptionModifyFeature get');
        this.totalsPoligoniWorkspaceOutput.emit(this.elementiWorkspace.length);
        this.loading = false;
      }).catch(error => {
        this.loading = false;
      });
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.cleanHighLigtStyle();
    this.cleanAllHighLigtStyles();
    this.cleanHighLigtStyleNoMultiSelected();
    this.selectInteraction = this.toolBarService.getInteractionFomName(enumTool.selectFeatures);
    if (this.selectInteraction) {
      this.selectInteraction.values_.active = false;
    }
    this.toolBarService.removeFeaturesFromSelect();
    if (this.subscriptionHighLigthFeature) {
      this.subscriptionHighLigthFeature.unsubscribe();
    }
  }

  ngOnDestroy() {
    if (this.contextView === undefined || this.contextView !== 'readOnly') {
      this.subscriptionModifyFeature.unsubscribe();
      if (this.subscriptionHighLigthFeature) {
        this.subscriptionHighLigthFeature.unsubscribe();
      }
      this.subscriptionManageHighLigthFeature.unsubscribe();
      this.subscriptionSelectFeature.unsubscribe();
      this.subscriptionFeatureChange.unsubscribe();
    }
  }

  reloadSelectedFeatures(reload) {
    this.toolBarService.mapElementiWorkspace(reload).then(reloadedEl => {
      this.toolBarService.sendFeatures(reloadedEl);
    });
  }

  subscribeMapSelect() {
    this.subscriptionFeatureChange = this.toolBarService.getSelectedFeatures().subscribe(features => {
      if (this.elementiWorkspace) {
        this.elementiWorkspace.forEach(element => {
          element.multiSelected = false;
        });
        if (features && features.length > 0) {
          for (let i = 0; i < this.elementiWorkspace.length; i++) {
            const featureSelected = features.filter(x => x.id === this.elementiWorkspace[i].id);
            if (featureSelected && featureSelected.length > 0) {
              this.elementiWorkspace[i].multiSelected = featureSelected[0].multiSelected;
            }
          }
        }
      }
    });
  }
  // Se il contesto è readOnly i dati da visualizzare vengono passati in input
  // altrimenti viene attivato un ascoltatore di eventi sui poligoni letti dalla mappa
  // e viene aggiunto addInteractionHighLightFeature
  setElementiWorkspace() {
    return new Promise(((resolve, reject) => {
      if (this.contextView && this.contextView === 'readOnly') {
        this.loading = true;
        // Se in readOnly i dati vengono passati in input
        if (this.elementiWorkspaceInput && this.elementiWorkspaceInput.length > 0) {
          this.elementiWorkspace = [];
          for (const feat of this.elementiWorkspaceInput) {
            const newEl = new FeatureWorkspace(
              feat.id,
              feat.id,
              new CodUso(feat.codUsoSuoloWorkspaceLavSuolo.codUsoSuolo, feat.codUsoSuoloWorkspaceLavSuolo.descrizione),
              new StatoColt(feat.statoColtWorkspaceLavSuolo.statoColt, feat.statoColtWorkspaceLavSuolo.descrizione),
              true,
              feat.area,
              false,
              feat.extent,
              false,
              false,
              feat.note);
            this.elementiWorkspace.push(newEl);
          }
          this.loading = false;
        }
      } else {
        this.subscriptionModifyFeature = this.mapService.getModifyFeatureEmitter()
          .subscribe((tipoOperazione) => {
            this.realoadElementiWorkspaceDaWfs().then(value => {
              this.totalsPoligoniWorkspaceOutput.emit(this.elementiWorkspace.length);
              this.poligoniWorkspaceOutput.emit(this.elementiWorkspace);

              this.loading = false;
            }).catch(error => {
              this.loading = false;
            });
          });

        // Abilita pointermove sopra le feature
        this.toolBarService.addInteractionHighLightFeature();

        // Ascoltatore eventi
        this.subscriptionManageHighLigthFeature = this.toolBarService.getManageActivateEditToolEmitter().subscribe((activate) => {
          // se c'è un tool di edit attivo spengo l'interaction pointermove
          if (activate) {
            this.toolBarService.toolEditActive = true;
            if (this.subscriptionHighLigthFeature) {

              if (this.toolBarService.isEditToolActive) {
                this.cleanHighLigtStyle();
              }
              this.subscriptionHighLigthFeature.unsubscribe();
            }
          } else {
            this.toolBarService.toolEditActive = false;
            this.subscriptionManageHighLigthFeature.unsubscribe();
          }
        });
      }
      resolve(this.elementiWorkspace);
    }));
  }

  private getCodificheSuolo() {
    if (!this.codUsoSuoloList || !this.statoColtList) {
      return new Promise(((resolve, reject) => {
        this.lavorazioneWorkspaceService.getCodificheSuolo().subscribe(
          (res) => {
            if (!this.codUsoSuoloList) {
              const listaCodUso = [];
              this.customSort({ data: res.codUsoSuolo, field: 'codice', mode: 'single', order: 1 });

              for (let i = 0; i < res.codUsoSuolo.length; i++) {
                listaCodUso.push({ 'codUso': res.codUsoSuolo[i].codice, 'codUsoDesc': res.codUsoSuolo[i].descrizione });
              }
              sessionStorage.setItem('codUsoSuoloList', JSON.stringify(listaCodUso));
            }
            if (!this.statoColtList) {
              const listaStatoColt = [];
              this.customSort({ data: res.statoColtSuolo, field: 'codice', mode: 'single', order: 1 });
              for (let i = 0; i < res.statoColtSuolo.length; i++) {
                listaStatoColt.push({ 'statoColt': res.statoColtSuolo[i].codice, 'statoColtDesc': res.statoColtSuolo[i].descrizione });
              }

              sessionStorage.setItem('statoColtList', JSON.stringify(listaStatoColt));
            }
            resolve(true);
          },
          (error) => {
            console.log(error);
            reject(error);
          });
      }));
    } else {
      this.listaCodUso = this.codUsoSuoloList;
      this.listaStatoColt = this.statoColtList;
    }

  }
  realoadElementiWorkspaceDaWfs() {
    return new Promise(((resolve, reject) => {
      try {
        const features = this.mapService.getFeaturesFromWfs(PropertyLayer.CODICE_LAYER_WORKSPACE);
        this.elementiWorkspace = [];
        for (const feat of features) {
          if (feat.get(CampoFeature.ID)) {
            const newEl = new FeatureWorkspace(
              feat.getId(),
              feat.get(CampoFeature.ID),
              new CodUso(feat.get(CampoFeature.COD_USO_SUOLO), feat.get(CampoFeature.USO_SUOLO_DES)),
              new StatoColt(feat.get(CampoFeature.STATO_COLT).toString(), feat.get(CampoFeature.STATO_COLT_DES)),
              true,
              feat.get(CampoFeature.AREA),
              false,
              null,
              false,
              false,
              feat.get(CampoFeature.NOTE)
            );
            this.elementiWorkspace.push(newEl);
          }
        }
        this.customSort({ data: this.elementiWorkspace, field: 'id', mode: 'single', order: 1 });
        resolve(this.elementiWorkspace);
      } catch (error) {
        console.log('error in realoadElementiWorkspace', error);
        reject(error);
      }
    }));
  }

  centerMap(poligono) {
    this.mapService.centerMapArea(poligono.extent, poligono.area);
  }

  zoomElementoInMappa(featureID, event) {
    this.mapService.centerOnFeature(PropertyLayer.CODICE_LAYER_WORKSPACE, featureID);
  }

  abilitaModificaPoligoniWorkspace() {
    this.lavorazioniEvent.editaCelleWorkSpace = true;
    this.elementiWorkspace.map(x => {
      if (x.statoColt && x.statoColt.statoColt) {
        x.statoColt.statoColt = x.statoColt.statoColt.toString();
      }
    });
    this.selectedFeatures = this.toolBarService.getInteractionFomName(enumTool.selectFeatures);
    if (this.selectedFeatures && !this.selectedFeatures.values_.active) {
      this.cleanAllHighLigtStyles();
    }
  }

  disabilitaModificaPoligoniWorkspace() {
    this.lavorazioniEvent.editaCelleWorkSpace = false;
    this.reloadedElementiWorkspace = cloneDeep(this.elementiWorkspace);
    this.elementiWorkspace = [];
    this.loading = true;
    //
    // ricarica tutti gli elementi del workspace senza gli stili acquisiti
    this.realoadElementiWorkspaceDaWfs().then(value => {
      console.log('disabilitaModificaPoligoniWorkspace');
      this.totalsPoligoniWorkspaceOutput.emit(this.elementiWorkspace.length);
      this.loading = false;
    });

    for (let k = 0; k < this.elementiWorkspace.length; k++) {
      if (this.reloadedElementiWorkspace && this.reloadedElementiWorkspace.length > 0) {
        for (let i = 0; i < this.reloadedElementiWorkspace.length; i++) {
          if (this.elementiWorkspace[k].id === this.reloadedElementiWorkspace[i].id) {
            if (this.reloadedElementiWorkspace[i].multiSelected) {
              this.elementiWorkspace[k].multiSelected = true;
            } else {
              this.elementiWorkspace[k].multiSelected = false;
            }
          }
        }
      }
    }

    this.loading = false;
    this.selectedFeatures = this.toolBarService.getInteractionFomName(enumTool.selectFeatures);
    if (this.selectedFeatures && !this.selectedFeatures.values_.active) {
      this.cleanAllHighLigtStyles();
    }
  }

  applicaModifichePoligoniWorkspace() {
    this.loading = true;
    const undoInteraction = this.toolBarService.getInteractionFomName(enumTool.undoRedoInteraction);

    undoInteraction.blockStart();
    this.elementiWorkspace.forEach(elem => {
      if (this.flagModificaMultiFeature) {
        if (elem.selectedFeature === true || elem['multiSelected'] === true) {
          if (this.statoColtMultiFeature) {
            elem.statoColt = this.statoColtMultiFeature;
          }
          if (this.codUsoMultiFeature) {
            elem.codUso = this.codUsoMultiFeature;
          }
          elem.selectedFeature = false;
          elem.sincronizzatoInMappa = false;
        }

      }

      this.salvaSuFeatureMappa(elem);
    });

    undoInteraction.blockEnd();

    this.loading = false;
    this.refreshExternalWindow();
  }

  refreshExternalWindow() {
    // Refresh finestra
    if (this.windowsEvent.fromWindow && this.windowsEvent.element['windowInstance']
      && !this.windowsEvent.element['windowInstance'].closed) {
      const triggerWindow: HTMLElement = document.getElementById('triggerNewWin') as HTMLElement;
      triggerWindow.click();
    }
  }
  salvaSuFeatureMappa(featureWorkspace: FeatureWorkspace): boolean {
    const oriSincronizzato = featureWorkspace.sincronizzatoInMappa;
    const fillAttr = this.toolBarService.getInteractionFomName(enumTool.fillAttributeInteraction);
    try {
      if (MapService.getLayerCode(this.mapService.editLayer) === PropertyLayer.CODICE_LAYER_WORKSPACE && !oriSincronizzato) {
        const feat = this.mapService.getWfsFeatureFromId(PropertyLayer.CODICE_LAYER_WORKSPACE, featureWorkspace.featureID);
        if (feat) {

          const attr = {};
          let mod = 0;

          if (attr[CampoFeature.COD_USO_SUOLO] !== featureWorkspace.codUso.codUso) {
            attr[CampoFeature.COD_USO_SUOLO] = featureWorkspace.codUso.codUso;
            attr[CampoFeature.USO_SUOLO_DES] = featureWorkspace.codUso.codUsoDesc;
            mod++;
          }

          if (attr[CampoFeature.STATO_COLT] !== featureWorkspace.statoColt.statoColt) {
            attr[CampoFeature.STATO_COLT] = featureWorkspace.statoColt.statoColt;
            attr[CampoFeature.STATO_COLT_DES] = featureWorkspace.statoColt.statoColtDesc;
            mod++;
          }

          if (mod > 0) {
            // si può forse usare per il multiattribute
            fillAttr.fill([feat], attr);
          }

          return true;
        }
      } else {
        return oriSincronizzato;
      }
    } catch (error) {
      return false;
    }
  }

  filterAutoCompleteCodUso(event) {
    const query = event.query;
    this.filterPoligoniWorkspace = [];
    this.listaCodUso = JSON.parse(this.codUsoSuoloList = sessionStorage.getItem('codUsoSuoloList'));
    for (let i = 0; i < this.listaCodUso.length; i++) {
      const itemAutoComplete = this.listaCodUso[i];
      if (itemAutoComplete['codUso'].includes(query)) {
        this.filterPoligoniWorkspace.push(itemAutoComplete);
      } else if (itemAutoComplete['codUsoDesc'].toLowerCase().includes(query.toLowerCase())) {
        this.filterPoligoniWorkspace.push(itemAutoComplete);
      }
    }
  }

  filterAutoCompleteStatoColt(event) {
    const query = event.query;
    this.filterPoligoniWorkspace = [];
    this.listaStatoColt = JSON.parse(this.codUsoSuoloList = sessionStorage.getItem('statoColtList'));
    for (let i = 0; i < this.listaStatoColt.length; i++) {
      const itemAutoComplete = this.listaStatoColt[i];
      if (itemAutoComplete['statoColt'].includes(query)) {
        this.filterPoligoniWorkspace.push(itemAutoComplete);
      } else if (itemAutoComplete['statoColtDesc'].toLowerCase().includes(query.toLowerCase())) {
        this.filterPoligoniWorkspace.push(itemAutoComplete);
      }
    }
  }


  changeRow(id) {
    const indexChanged = this.elementiWorkspace.findIndex(element => element.id === id);
    this.elementiWorkspace[indexChanged].sincronizzatoInMappa = false;
    this.applicaModifichePoligoniWorkspace();
  }

  customSort(event: SortEvent) {
    event.data.sort((data1, data2) => {
      const value1 = data1[event.field];
      const value2 = data2[event.field];
      if (event.field !== 'multiSelectedOrd') {
        return (event.order * this.customSortFunct(value1, value2));
      } else if (event.field === 'multiSelectedOrd') {
        // Ordina per poligoni multiselezionati
        return (-1 * this.customSortFunct(data1.multiSelected ? true : null, data2.multiSelected ? true : null));
      }
    });

  }
  private customSortFunct(value1, value2) {
    let result = null;
    if (value1 == null && value2 != null) {
      result = -1;
    } else if (value1 != null && value2 == null) {
      result = 1;
    } else if (value1 == null && value2 == null) {
      result = 0;
    } else if (typeof value1 === 'string' && typeof value2 === 'string') {
      result = value1.localeCompare(value2);
    } else if (typeof value1 === 'string' && typeof value2 !== 'string') {
      result = -1;
    } else if (typeof value1 === 'object' && typeof value2 === 'object') {
      if (value1.codUso && value2.codUso) {
        return this.customSortFunct(value1.codUso, value2.codUso);
      } else if (value1.statoColt && value2.statoColt) {
        return this.customSortFunct(value1.statoColt, value2.statoColt);
      }
    } else {
      result = (value1 < value2) ? -1 : (value1 > value2) ? 1 : 0;
    }
    return result;
  }

  getHighLightFeatureStyle() {
    const highlightStyle = new Style({
      fill: new Fill({
        color: 'rgba(255,255,255,0.7)',
      }),
      stroke: new Stroke({
        color: '#3399CC',
        width: 3,
      }),
    });
    return highlightStyle;
  }

  getHighLightMultiSelection() {
    const highlightStyle = new Style({
      stroke: new Stroke({
        color: this.gisStyles.selectTematismoBordoColore,
        width: this.gisStyles.selectTematismoDimensioneBordo,
      }),
      fill: new Fill({
        color: this.gisStyles.selectTematismoFillColore,
      }),
    });
    return highlightStyle;
  }

  onRowSelect(event, poligono) {
    if (event.originalEvent) {
      // Attiva Select
      this.multiClickActived = true;
      this.onMultiRowSelect(event, null, poligono);
      return;
    }
  }

  onRowHover(event, poligono) {
    this.selectInteraction = this.toolBarService.getInteractionFomName(enumTool.selectFeatures);
    if (this.contextView === undefined || this.contextView !== 'readOnly') {
      const feat = this.mapService.getWfsFeatureFromId(PropertyLayer.CODICE_LAYER_WORKSPACE, poligono.featureID);
      if (this.elementiWorkspace) {
        const indexChanged = this.elementiWorkspace.findIndex(element => element.featureID === poligono.featureID);
        if (indexChanged >= 0) {
          if (this.selectInteraction != null && !this.selectInteraction.values_.active && !this.elementiWorkspace[indexChanged]['multiSelected']) {
            this.cleanHighLigtStyle();
          } else if (this.elementiWorkspace[indexChanged].multiSelected) {
            this.elementiWorkspace[indexChanged].selectedAndMultiselected = true;
            this.elementiWorkspace[indexChanged].multiSelected = false;
          }
          this.elementiWorkspace[indexChanged].selectedFeature = true;
          this.selected = feat;
          this.selected.setStyle()
          this.selected.setStyle(this.getHighLightFeatureStyle());
        }
      }
    }
  }

  onMultiRowSelect(event, context, poligono) {
    let setStyle = this.getHighLightFeatureStyle();
    setStyle = this.getHighLightMultiSelection();
    if (!context || context === '') {
      // Attiva Select
      this.checkSelectButton(this.triggerSelect, this.selectInteraction).then(value => {
        setTimeout(() => {
          if (this.contextView === undefined || this.contextView !== 'readOnly') {
            const feat = this.mapService.getWfsFeatureFromId(PropertyLayer.CODICE_LAYER_WORKSPACE, poligono.featureID);
            this.selected = feat;
            if (this.elementiWorkspace) {
              const indexChanged = this.elementiWorkspace.findIndex(element => element.featureID === poligono.featureID);
              if (indexChanged >= 0) {
                if (!poligono.multiSelected) {
                  poligono.multiSelected = true;
                } else {
                  poligono.multiSelected = false;
                }
                this.selected = feat;
                this.selectedFeatures = this.selectInteraction.getFeatures().getArray();
                let featureId = this.selectedFeatures.filter(element => element.id_ === this.elementiWorkspace[indexChanged].featureID);
                if (featureId.length === 0) {
                  this.selected.setStyle(setStyle);
                  this.selectedFeatures.push(this.selected);
                  this.reloadSelectedFeatures(this.selectedFeatures);
                } else if (featureId.length > 0) {
                  this.selected.setStyle();
                  let indexToRemove = this.selectedFeatures.findIndex(element => element.id_ === this.selected.id_);
                  this.selectedFeatures.splice(indexToRemove, 1);
                  this.reloadSelectedFeatures(this.selectedFeatures);
                }
              }
            }
          }
        }, 100);

      });
    }
  }

  checkSelectButton(triggerSelect, select) {
    return new Promise(((resolve, reject) => {
      try {
        if (!select || !select.get('active')) {
          setTimeout(() => {
            this.cleanAllHighLigtStyles();
            triggerSelect.click();
            this.selectInteraction = this.toolBarService.getInteractionFomName(enumTool.selectFeatures);
            this.selectInteraction.values_.active = true;
          }, 100);
        }
        resolve(true);
      } catch (error) {
        reject(error);
      }
    }));
  }

  cleanHighLigtStyle() {
    if (this.elementiWorkspace) {
      this.elementiWorkspace.map(element => {
        element.selectedFeature = false;
      });
    }
    if (this.selected !== null) {
      if (this.elementiWorkspace.some(element => element.featureID === this.selected.getId())) {
        this.selected.setStyle();
        this.selected = null;
      }
    }
  }

  cleanHighLigtStyleNoMultiSelected() {
    if (this.elementiWorkspace) {
      this.elementiWorkspace.map(element => {
        if (element.multiSelected === false) {
          element.selectedFeature = false;
        }
        if (element.selectedAndMultiselected) {
          element.multiSelected = true;
          element.selectedAndMultiselected = false;
        }
      });
    }
    if (this.selected !== null) {
      if (this.elementiWorkspace.some(element => element.featureID === this.selected.getId() && !element.multiSelected)) {
        this.selected.setStyle();
        this.selected = null;
      } else if (this.elementiWorkspace.some(element => element.featureID === this.selected.getId() && element.multiSelected)) {
        this.selected.setStyle(this.getHighLightMultiSelection());
      }
    }
  }

  cleanAllHighLigtStyles() {
    this.selectedFeatures = this.toolBarService.getInteractionFomName(enumTool.selectFeatures);
    if (this.selectedFeatures !== null) {
      if (this.elementiWorkspace && (!this.selectedFeatures.values_.active || this.selectedFeatures.values_)) {
        this.elementiWorkspace.map(element => {
          element.multiSelected = false;
          element.selectedFeature = false;
        });
      }
      if (this.selectedFeatures && !this.selectedFeatures.values_.active) {
        const features = this.selectedFeatures.getFeatures().getArray();
        features.forEach((element, index) => {
          element.setStyle();
          this.selectedFeatures.getFeatures().array_[index].setStyle();
        });
        this.selectedFeatures.getFeatures().array_ = [];
      } else if (this.selected) {
        if (this.elementiWorkspace.some(element => element.featureID === this.selected.getId())) {
          this.selected.setStyle();
          this.selected = null;
        }
      }
    } else {
      this.cleanHighLigtStyle();
    }
  }

  receiveTotalsPoligoniWorkspace($event) {
    this.totalsPoligoniWorkspace = $event;
  }

  onChangeModificaMultipla(e) {
    this.flagModificaMultiFeature = e.checked;
  }
}
