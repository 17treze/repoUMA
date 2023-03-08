import {
    Component,
    ElementRef,
    Host,
    Input,
    OnInit,
    Optional,
    Renderer2,
    ViewChild,
    ViewEncapsulation
} from "@angular/core";
import {MapService} from "../../map.service";
import {MapidService} from "../../mapid.service";
import {PanelEvent} from "../../../../shared/PanelEvent";
import {RightToolBarEvent} from "../../../../shared/RightToolBar/RightToolBarEvent";
import {MapEvent} from "../../../../shared/MapEvent";
import {SelectItem} from "primeng-lts";
import {ToolBarService} from "../../../../shared/ToolBar/toolBar.service";
import {MisuraUtils} from "../../gisTools/misura.utils";
import {GisListener} from "../../gisTools/gisListener";
import {enumListener} from "../../gisTools/enumListeners";
import {InputNumberModule} from 'primeng/inputnumber';
import { GisCostants } from "src/app/gis/shared/gis.constants";
import { EditSettingsService } from "src/app/gis/shared/EditSettings";


@Component({
    selector: 'gis-settings',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.css'],
    encapsulation: ViewEncapsulation.None
})

export class SettingsComponent extends GisListener implements OnInit {

    @Input() mapId: string;
    @ViewChild('settings') settings: ElementRef;
    dismissible: false;
    blockScroll: true;
    elem: any;
    showLSettings = false;
    rootMap = document.getElementsByTagName('body')[0];
    map: any;
    clickHandlerLoaded = false;

    tipologiaMisura: SelectItem[];
    selectedTipologiaMisura: String;
    selectedModalita: String;
    valoreSnap: number;
    tipologiaModalita: { label: string; value: string; }[];
    editSettings : EditSettingsService;

    constructor(

        private mapService: MapService,
        private misuraUtils: MisuraUtils,
        @Host()
        @Optional()
        private mapidService: MapidService,
        public panelEvent: PanelEvent,
        public rightToolbarEvent: RightToolBarEvent,
        private toolBarService1: ToolBarService,
        public mapEvent: MapEvent,
        public renderer: Renderer2,
        public gisCostants: GisCostants
        
    ) {
       
        super(enumListener.identify, toolBarService1, null);
        this.tipologiaMisura = [
            {label: 'Solo Area', value: 'Area'},
            {label: 'Solo lunghezza', value: 'Lunghezza'},
            {label: 'Entrambe', value: 'Entrambe'}
            
        ];
        this.tipologiaModalita = [
            {label: 'Entrambi', value: 'Entrambi'},
            {label: 'Solo Vertice', value: 'Vertice'},
            {label: 'Solo Segmento', value: 'Segmento'}
        ];
        this.selectedModalita = 'Entrambi';
        this.selectedTipologiaMisura = 'Area';
        this.valoreSnap = this.gisCostants.snapTolerance;
        

    }

    ngOnInit(): void {
        this.misuraUtils.enableLabelArea = true;
        this.misuraUtils.enableLabelLine = false;
    }
    OnChanges() {
        console.log('snapTolerance');
    }
    openSettings() {
        this.panelEvent.showSettings = !this.panelEvent.showSettings;
        this.panelEvent.showLayerSwitcher = false;
        this.panelEvent.showmapNavigator = false;
        this.panelEvent.identifyTableContent = [];

    }

    onChangeTipologiaMisura(event) {

        if (event.originalEvent && event.value === this.selectedTipologiaMisura) {
            switch (this.selectedTipologiaMisura) {
                case "Area":
                    this.misuraUtils.enableLabelArea = true;
                    this.misuraUtils.enableLabelLine = false;
                    break;
                case "Lunghezza":
                    this.misuraUtils.enableLabelArea = false;
                    this.misuraUtils.enableLabelLine = true;
                    break;
                case "Entrambe":
                    this.misuraUtils.enableLabelArea = true;
                    this.misuraUtils.enableLabelLine = true;
                    break;
            }
            this.toolBarService1.refreshStyle();
        }
    }

    get canUseListener(): boolean {
        return false;
    }

    /*
    onChangeTolleranzaSnap(event){

    }
     */
    onChangeSnap(event) {
        if(this.valoreSnap && this.valoreSnap > 0){
        this.toolBarService1.snapTolerance = this.valoreSnap;
        //this.toolBarService1.modifyPixelTolerance = this.valoreSnap;
        console.log('this.gisCostants.snapTolerance', this.toolBarService1.snapTolerance);
        //console.log('this.gisContanst.modifyPixelTolerance',this.toolBarService1.modifyPixelTolerance);
        this.toolBarService1.refreshSnapOnAllLayers(false);
    }else{ this.valoreSnap=0;}

    }
    onChangeModalitaSnap(event) {

        if (event.originalEvent && event.value === this.selectedModalita) {

            this.mapService.getLayerFromCode('MISURA_LAYER');
            switch (this.selectedModalita) {
                case "Entrambi":
                    this.toolBarService1.snapEdge = true;
                    this.toolBarService1.snapVertex = true;
                    //console.log('this.toolBarService1.snapEdge', this.toolBarService1.snapEdge);
                    //console.log('this.toolBarService1.snapVertex', this.toolBarService1.snapVertex);
                    break;
                case "Vertice":
                    this.toolBarService1.snapEdge = false;
                    this.toolBarService1.snapVertex = true;
                    //console.log('this.toolBarService1.snapEdge', this.toolBarService1.snapEdge);
                    //console.log('this.toolBarService1.snapVertex', this.toolBarService1.snapVertex);
                    break;
                case "Segmento":
                    this.toolBarService1.snapEdge = true;
                    this.toolBarService1.snapVertex = false;
                    //console.log('this.toolBarService1.snapEdge', this.toolBarService1.snapEdge);
                    //console.log('this.toolBarService1.snapVertex',this.toolBarService1.snapVertex);
                    break;
            }
            this.toolBarService1.refreshSnapOnAllLayers(false);
        }
    }
}
