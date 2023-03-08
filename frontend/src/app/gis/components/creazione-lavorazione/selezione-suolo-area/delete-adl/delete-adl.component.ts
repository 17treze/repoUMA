import { PoligoniSuoloDaAdl, Count } from './../../../../models/poligoniSuolo/poligoniSuoloDaAdl.model';
import { VectorLayer } from 'ol/layer/Vector';
import { LayerGisService } from 'src/app/gis/services/layer-gis.service';
import { LavorazioniEvent } from 'src/app/gis/shared/LavorazioniEvent';
import { MapEvent } from './../../../../shared/MapEvent';
import { MisuraUtils } from './../../../mappa/gisTools/misura.utils';
import { enumTool } from './../../../mappa/gisTools/enumTools';
import { GisCostants } from './../../../../shared/gis.constants';
import { Component, HostListener, Input, OnInit, ViewEncapsulation } from '@angular/core';
import Select from 'ol/interaction/Select';import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { GisStyles } from '../../../../shared/GisStyles';
import GeometryType from 'ol/geom/GeometryType';
import { GisButton } from '../../../mappa/gisTools/gisButton';
import { CreazioneLavorazioneService } from './../../../../services/creazione-lavorazione.service';
import {ToastGisComponent} from './../../../toast-gis/toast-gis.component';
import { GisMessaggiToastCostants } from '../../../../shared/messaggi-toast.constants';
import { MapService } from '../../../mappa/map.service';
import { PropertyLayer } from '../../../../shared/PropertyLayer.enum';
import { CreazioneLavorazioneComponent } from '../../creazione-lavorazione.component';
@Component({
  selector: 'gis-delete-adl',
  templateUrl: './delete-adl.component.html',
  styleUrls: ['./delete-adl.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DeleteAdlComponent implements OnInit {
  select: Select;


  constructor(private layerGisService: LayerGisService, private gisStyles: GisStyles, private toolBarService1: ToolBarService,
    private misuraUtils: MisuraUtils, public gisCostants: GisCostants, public mapEvent: MapEvent,private messaggiGis: GisMessaggiToastCostants, private mapService: MapService,
    public lavorazioniEvent: LavorazioniEvent, private creazioneLavorazioneService : CreazioneLavorazioneService, private toastGisComponent : ToastGisComponent,private creazioneLavorazioneComponent : CreazioneLavorazioneComponent ) {
  }

  ngOnInit(): void {
    const self = this;
     this.select = new Select({
      type: GeometryType.POLYGON,
      layers: [self.lavorazioniEvent.featureAdl]
    });
    this.select.features_.array_.push(self.lavorazioniEvent.featureAdl.getSource().getFeatures()[0]);

  }

  doDelete() {
    const modifyAdl = this.toolBarService1.getInteractionFomName(enumTool.modifyAdl);
    if ((this.select) && this.select.getFeatures()) {
      this.select.getFeatures().clear();
      this.layerGisService.getLayerByCode('ADL').then((layerAdl: VectorLayer) => {
       if (this.lavorazioniEvent.poligoniDaAdl && this.lavorazioniEvent.poligoniDaAdl.length > 0) {
       this.creazioneLavorazioneService.deleteAdl(this.lavorazioniEvent.idLavorazione).subscribe((res) => {
          this.toastGisComponent.showSuccessGenerico('Cancellazione Adl', this.messaggiGis.deleteAdlSuccess);
          this.creazioneLavorazioneService.ricaricaADL(this.lavorazioniEvent.idLavorazione).then((results: PoligoniSuoloDaAdl) => {
            if (results) {
              this.lavorazioniEvent.poligoniDaAdl = results['risultati'];
              this.lavorazioniEvent.totalsPoligoniDaAdl = results['risultati'].length;
          }
          });
        }, (error) => {
          this.toastGisComponent.showErrorGenerico(this.messaggiGis.deleteAdlError);
        });
      }
        layerAdl.getSource().clear();
        this.lavorazioniEvent.featureAdl = null;
        this.lavorazioniEvent.poligoniDaAdl = null;
        this.lavorazioniEvent.calcoloAdlActived = false;
      });
    }
  }
}
