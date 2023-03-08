import { GestioneCampagnaService } from './../../../../services/gestione-campagna.service';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { SelectItem } from 'primeng/api';
import { GisUtilsService } from '../../../../shared/gis-utils.service';
import { GisCostants } from '../../../../shared/gis.constants';
import { PropertyLayer } from '../../../../shared/PropertyLayer.enum';
import { MapService } from '../../map.service';

@Component({
  selector: 'gis-anno-campagna',
  templateUrl: './anno-campagna.component.html',
  styleUrls: ['./anno-campagna.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AnnoCampagnaComponent implements OnInit {
  private _annoCampagna: number;
  anno_campagna: SelectItem[];

  constructor(private mapService: MapService, private gisUtils: GisUtilsService, private gestioneCampagna: GestioneCampagnaService) {
    this.anno_campagna = this.gisUtils.getComboAnniCampagna(false);
  }

  ngOnInit() {
    this._annoCampagna = this.mapService.annoCampagna;
  }

  get annoCampagnaSelezionato() {
    return this._annoCampagna;
  }

  set annoCampagnaSelezionato(value) {
    console.log('Set annoCampagna ', value);
    let anno = value;
    if (!anno) {
      anno = this.gestioneCampagna.inputSelectAnnoCampagna();
    }
    this._annoCampagna = anno;
    this.mapService.annoCampagna = anno;
    this.mapService.refreshWmsLayerCampagna();
  }
}
