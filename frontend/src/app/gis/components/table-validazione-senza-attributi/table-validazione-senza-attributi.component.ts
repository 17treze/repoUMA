import { Component, OnInit, OnDestroy, OnChanges, Input, ViewChild, SimpleChanges } from "@angular/core";
import { CodUso, StatoColt, FeatureWorkspace } from "../../shared/featureWorkspace";
import { MapService } from "../mappa/map.service";
import Select from 'ol/interaction/Select';




@Component({
  selector: 'app-table-validazione-senza-attributi',
  templateUrl: './table-validazione-senza-attributi.component.html',
  styleUrls: ['./table-validazione-senza-attributi.component.css']
  //encapsulation: ViewEncapsulation.None
})
export class TableValidazioneSenzaAttributiComponent implements OnInit, OnDestroy, OnChanges {
  editaCelle = false;
  columns: any[];
  filterPoligoniWorkspace: any[];
  listaCodUso: string;
  listaStatoColt: string;
  selected = null;
  @Input() idLavorazione: any;
  @Input() contextView: string;
  @Input() TableValidazioneSenzaAttributiInput: any[];
  @ViewChild('zoomBtn') zoomBtn: any;

  selectInteraction: Select;
  flagModificaMultiFeature = false;
  codUsoMultiFeature: CodUso;
  statoColtMultiFeature: StatoColt;
  TableValidazioneSenzaAttributi: FeatureWorkspace[];
  reloadedTableValidazioneSenzaAttributi: FeatureWorkspace[];
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
  constructor(private mapService: MapService) {
    
  }

  ngOnInit() {
    console.log(this.TableValidazioneSenzaAttributiInput);

    // Columns tabella
    this.columns = [
      { field: 'id', header: 'ID', width: '10%' },
      { field: 'codUso', header: 'USO', width: '25%' },
      { field: 'statoColt', header: 'STATO', width: '25%' },
      { field: 'area', header: 'AREA (MQ)', width: '40%' },
    ];



  }



  ngOnChanges(changes: SimpleChanges): void {

  }

  ngOnDestroy() {
 
  }

  centerMap(poligono) {
    this.mapService.centerMapArea(poligono.extent, poligono.area);
  }

}

