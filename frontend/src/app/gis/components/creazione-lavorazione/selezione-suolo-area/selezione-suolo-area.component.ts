import { never } from 'ol/events/condition';
import { LayerGisService } from 'src/app/gis/services/layer-gis.service';
import GeometryType from 'ol/geom/GeometryType';
import Modify from 'ol/interaction/Modify';
import Select from 'ol/interaction/Select';
import { Tile as TileLayer, Vector as VectorLayer } from 'ol/layer';
import { MapService } from './../../mappa/map.service';
import { enumTool } from './../../mappa/gisTools/enumTools';
import { GisCostants } from './../../../shared/gis.constants';
import { GisStyles } from './../../../shared/GisStyles';
import { MisuraUtils } from './../../mappa/gisTools/misura.utils';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { PanelEvent } from '../../../shared/PanelEvent';
import { Stepper } from '../../../shared/Stepper';
import { LavorazioniEvent } from '../../../shared/LavorazioniEvent';
import { EsitoValidazioneEvent } from '../../../shared/EsitoValidazioneEvent';
import { MapEvent } from '../../../shared/MapEvent';
import { ToolBarService } from '../../../shared/ToolBar/toolBar.service';
import VectorSource from 'ol/source/Vector';
import { Draw } from 'ol/interaction';
import { GisTool } from '../../mappa/gisTools/gisTool';
import { condition } from '../../mappa/gisTools/drawUtils';

@Component({
  selector: 'selezione-suolo-area',
  templateUrl: './selezione-suolo-area.component.html',
  styleUrls: ['./selezione-suolo-area.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class SelezioneSuoloAreaComponent extends GisTool implements OnInit {
  activeButton;
  drawAdl: Draw;
  modifyAdl: Modify;
  select: Select;
  

  constructor(public panelEvent: PanelEvent, public stepper: Stepper, public lavorazioniEvent: LavorazioniEvent,
    public esitoValidazioneEvent: EsitoValidazioneEvent, public mapEvent: MapEvent, private misuraUtils: MisuraUtils,
    public gisCostants: GisCostants, private gisStyles: GisStyles,
    private mapService: MapService, public toolBarService1: ToolBarService, private layerGisService: LayerGisService) {
      super([enumTool.drawAdl], [], false, true, true, toolBarService1.editToolGroup, toolBarService1);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  declareTool(): any[] {
    this.lavorazioniEvent.activeDrawAdl = !this.lavorazioniEvent.activeDrawAdl;
    const self = this;
    if (!self.lavorazioniEvent.featureAdl) {
      this.drawAdl = new Draw({
        source: new VectorSource(),
        type: GeometryType.POLYGON,
        style: function (feature) {
          return self.misuraUtils.styleFunction(feature, true, GeometryType.POLYGON,
          self.toolBarService1.misuraStyle, self.gisStyles.newPolygonStyle);
        },
        stopClick: true,
        clickTolerance: this.gisCostants.drawClickTolerance,
        snapTolerance: this.gisCostants.endDrawSnapTolerance,
        condition: condition(self.gisCostants.endDrawCreateVertex),
        finishCondition: never
      });

      this.drawAdl.on('drawend', async function (event) {
        document.addEventListener('contextmenu', function (e){
          e.preventDefault();
        }, false);
        self.lavorazioniEvent.stackAdl = true;
        self.layerGisService.getLayerByCode('ADL').then((layerAdl: VectorLayer) => {
          layerAdl.getSource().addFeature(event.feature);
          self.lavorazioniEvent.featureAdl = layerAdl;
          self.mapEvent.map.removeInteraction(self.drawAdl);
        });
        self.lavorazioniEvent.calcoloAdlActived = true;
      });
    }
    return [this.drawAdl];
  }

  public get canUseTool(): boolean {
    return this.toolBarService1.canUseTool(this.mainToolName[0], true);
  }
}
