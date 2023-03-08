import { Component, OnInit } from '@angular/core';
import { CreazioneLavorazioneService } from 'src/app/gis/services/creazione-lavorazione.service';
import { LayerConfig } from 'src/app/gis/shared/LayerConfig';
import { MapEvent } from 'src/app/gis/shared/MapEvent';
import { PropertyLayer } from 'src/app/gis/shared/PropertyLayer.enum';
import { ToolBarService } from 'src/app/gis/shared/ToolBar/toolBar.service';
import { ToastGisComponent } from '../../../toast-gis/toast-gis.component';
import { MapService } from '../../map.service';
import { enumTool } from '../enumTools';
import { GisButton } from '../gisButton';

@Component({
  selector: 'gis-ritagliaADL',
  templateUrl: './ritagliaADL.component.html',
  styleUrls: ['./ritagliaADL.component.css']
})
export class RitagliaADLComponent extends GisButton implements OnInit {

  constructor(private creazioneLavorazioneService: CreazioneLavorazioneService,
    private toastComponent: ToastGisComponent, private mapService: MapService,public mapEvent : MapEvent, private toolBarService1: ToolBarService) {
    super(enumTool.deleteEl);
  }
  ngOnInit() {
    /*
        document.addEventListener('keydown', ({ key }) => {
          if (key === 'Delete') {
            this.click();
          }
        });
        */
  }

  click() {
    const editLayer = this.mapService.editLayer;

    if (editLayer) {

      const id = editLayer.get(PropertyLayer.VALOREFILTRO);
      return new Promise((resolve, reject) => {
      this.creazioneLavorazioneService.ritagliaWorkspaceSuAreaDiLavoro(id).subscribe((respone: any) => {
        if (respone['status'] === 201) {
          const layConfig = this.mapService.layerConfig
          .find(x => x.mapProperty.get(PropertyLayer.CONTENUTO_INFORMATIVO) === PropertyLayer.CONTENUTO_INFORMATIVO_WORKSPACE);
          if (layConfig) {
            layConfig.setTmpNomeLayer(layConfig.mapProperty.get(PropertyLayer.NOME_LAYER_TMP));
            const layerContesto: LayerConfig[] = new Array<LayerConfig>();
            layerContesto.push(layConfig);

            const undoInteraction = this.toolBarService1.getInteractionFomName(enumTool.undoRedoInteraction);

            this.mapService.loadLayers(layerContesto, id, true, false, false, undoInteraction);
            layConfig.restoreNomeLayer();
          }
          this.toastComponent.showSuccess();
        } else {
          this.toastComponent.showError();
        }
      },
      (error) => {
        console.log(error);
        this.toastComponent.showError();
        reject(error);
      });
    });
    }
  }

  public get canUse(): boolean {
    return this.toolBarService1.canUseTool(this.mainName, this.mapService.editLayer);
  }
}
