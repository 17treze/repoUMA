import { StoreModule } from '@ngrx/store';
import { SplitButtonModule } from 'primeng-lts/splitbutton';
import { GestioneCampagnaService } from './services/gestione-campagna.service';
import { CaricaLayerStoricoComponent } from './components/mappa/control/carica-layer-storico/carica-layer-storico.component';
import { AggiungiNoteDialogComponent } from './components/mappa/gisTools/aggiungi-note/aggiungi-note-dialog/aggiungi-note-dialog.component';
import { AggiungiNoteComponent } from './components/mappa/gisTools/aggiungi-note/aggiungi-note.component';
import { SuoloVigenteDaAdlComponent } from './components/gis/suolo-vigente-da-adl/suolo-vigente-da-adl.component';
import { DeleteAdlComponent } from './components/creazione-lavorazione/selezione-suolo-area/delete-adl/delete-adl.component';
import { ModifyAdlComponent } from './components/creazione-lavorazione/selezione-suolo-area/modify-adl/modify-adl.component';
import { SelezioneSuoloComponent } from './components/mappa/gisTools/selezione-suolo/selezione-suolo.component';
import { SelezioneSuoloAreaComponent } from './components/creazione-lavorazione/selezione-suolo-area/selezione-suolo-area.component';
import { EventiCalcoloAnomalie } from './shared/EventiCalcoloAnomalie';
import { MisuraUtils } from './components/mappa/gisTools/misura.utils';
import { CaricaVettorialiCostants } from './components/mappa/gisTools/carica-vettoriali/carica-vettorialicostants';
import { ModalCaricaVettorialiComponent } from './components/mappa/gisTools/carica-vettoriali/modal-carica-vettoriali/modal-carica-vettoriali.component';
import { CaricaVettorialiComponent } from './components/mappa/gisTools/carica-vettoriali/carica-vettoriali.component';
import { FileUploadModule } from 'primeng-lts/fileupload';
import { DragPanComponent } from './components/mappa/control/dragPan/dragPan.component';
import { RightToolBarEvent } from './shared/RightToolBar/RightToolBarEvent';
import { SpegniVettorialiComponent } from './components/mappa/gisTools/spegni-vettoriali/spegni-vettoriali.component';
import { MisuraComponent } from './components/mappa/gisTools/misura/misura.component';
import { ScalaCostants } from './components/mappa/control/scala/scala.costants';
import { DeleteComponent } from './components/mappa/gisTools/delete/delete.component';
import { TrasformaComponent } from './components/mappa/gisTools/trasforma/trasforma.component';
import { ToolbarLavorazioneComponent } from './components/toolbar-lavorazione/toolbar-lavorazione.component';
import { PopoutService } from './services/PopoutService.service';
import { MapService } from './components/mappa/map.service';
import { WindowsEvent } from './shared/WindowsEvent';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GisRoutingModule } from './gis-routing.module';
import { GisComponent } from './components/gis/gis.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { RightToolBarComponent } from './components/mappa/gisTools/rightToolBar/rightToolBar.component';
import { ToastGisComponent } from './components/toast-gis/toast-gis.component';
import { ConfirmDialogModule } from 'primeng-lts/confirmdialog';
import { DialogModule } from 'primeng-lts/dialog';
import { ToastModule } from 'primeng-lts/toast';
import { DropdownModule } from 'primeng-lts/dropdown';
import { InputTextModule } from 'primeng-lts/inputtext';
import { ButtonModule } from 'primeng-lts/button';
import { CalendarModule } from 'primeng-lts/calendar';
import { SidebarModule } from 'primeng-lts/sidebar';
import { AutoCompleteModule } from 'primeng-lts/autocomplete';
import { GisCostants } from './shared/gis.constants';
import { ProfiloUtente } from './shared/profilo-utente';
import { EsitoValidazioneLavorazioneComponent } from './components/esito-validazione-lavorazione/esito-validazione-lavorazione.component';
import { EsitoValidazioneLavorazioneButtonComponent } from './components/esito-validazione-lavorazione/esito-validazione-lavorazione-button/esito-validazione-lavorazione-button.component';
import { AllegatiEvent } from './shared/AllegatiEvent';
import { LavorazioniEvent } from './shared/LavorazioniEvent';
import { SearchGisComponent } from './components/search-gis/search-gis.component';
import { ResultsGisComponent } from './components/results-gis/results-gis.component';
import { AllegatiGisComponent } from './components/allegati-gis/allegati-gis.component';
import { DialogsGisComponent } from './components/dialogs-gis/dialogs-gis.component';
import { ShowResults } from './shared/show-results';
import { showDettaglioRichiesta } from './shared/showDettaglioRichiesta';
import { RichiestaModificaSuoloService } from './services/richiesta-modifica-suolo.service';
import { AllegatiGisService } from './services/allegati-gis.service';
import { CreazioneLavorazioneService } from './services/creazione-lavorazione.service';
import { DetailGisComponent } from './components/detail-gis/detail-gis.component';
import { MessaggiGisComponent } from './components/messaggi-gis/messaggi-gis.component';
import { UploadAttachmentsComponent } from './components/upload-attachments/upload-attachments.component';
import { UploadAttachmentsProgressComponent } from './components/upload-attachments-progress/upload-attachments-progress.component';
import { CreazioneLavorazioneComponent } from './components/creazione-lavorazione/creazione-lavorazione.component';
import { WizardComponentComponent } from './components/wizard-component/wizard-component.component';
import { OverlayPanelModule } from 'primeng-lts/overlaypanel';
import { TableModule } from 'primeng-lts/table';
import { InputSwitchModule } from 'primeng-lts/inputswitch';
import { TooltipModule } from 'primeng-lts/tooltip';
import { AccordionModule } from 'primeng-lts/accordion';
import { TreeTableModule } from 'primeng-lts/treetable';
import { ReactiveFormsModule } from '@angular/forms';
import { OnlynumberDirective } from './directives/onlynumber.directive';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { CancellazioneLavorazioneComponent } from './components/cancellazione-lavorazione/cancellazione-lavorazione/cancellazione-lavorazione.component';
import { DettaglioLavorazioneComponent } from './components/dettaglio-lavorazione/dettaglio-lavorazione.component';
import { LeMieLavorazioniComponent } from './components/le-mie-lavorazioni/le-mie-lavorazioni.component';
import { PoligoniSuoloTableComponent } from './components/poligoni-suolo-table/poligoni-suolo-table.component';
import { MappaComponent } from './components/mappa/mappa.component';
import { LayerComponent } from './components/mappa/layer/layer.component';
import { ControlComponent } from './components/mappa/control/control.component';
import { MousePositionComponent } from './components/mappa/control/mouse-position.components';
import { EditToolBarComponent } from './components/mappa/gisTools/editToolBar/editToolBar.component';
import { DrawComponent } from './components/mappa/gisTools/draw/draw.component';
import { DrawHoleComponent } from './components/mappa/gisTools/drawHole/drawHole.component';
import { DrawRegularComponent } from './components/mappa/gisTools/drawRegular/drawRegular.component';
import { IdentifyComponent } from './components/mappa/gisTools/identify/identify.component';
import { SaveComponent } from './components/mappa/gisTools/save/save.component';
import { SelectComponent } from './components/mappa/gisTools/select/select.component';
import { LineSplitComponent } from './components/mappa/gisTools/lineSplit/lineSplit.component';
import { ModifyComponent } from './components/mappa/gisTools/modify/modify.component';
import { DrawTraceComponent } from './components/mappa/gisTools/drawTrace/drawTrace.component';
import { UndoRedoComponent } from './components/mappa/gisTools/undoRedo/undoRedo.component';
import { UnionComponent } from './components/mappa/gisTools/union/union.component';
import { CloseHoleComponent } from './components/mappa/gisTools/closeHole/closeHole.component';
import { MapidService } from './components/mappa/mapid.service';
import { MapdataService } from './components/mappa/mapdata.service';
import { FeatureService } from './components/mappa/feature.service';
import { TreeTableComponent } from './components/mappa/tree-table/tree-table.component';
import { LavorazioneWorkspaceService } from './services/LavorazioneWorkspace.service';
import { SuoloDichiaratoComponent } from './components/suolo-dichiarato/suolo-dichiarato.component';
import { MenuContestualeComponent } from './components/mappa/gisTools/menuContestuale/menuContestuale.component';
import { SuoloVigenteComponent } from './components/suolo-vigente/suolo-vigente.component';
import { LayerGisService } from './services/layer-gis.service';
import { LayerAttivi } from './shared/LayerAttivi';
import { LavorazioneWorkspaceComponent } from './components/lavorazioneWorkspace/lavorazioneWorkspace.component';
import { ZoomBarComponent } from './components/mappa/gisTools/zoomBar/zoom-bar.component';
import { LayerSwitcherComponent } from './components/mappa/control/layerSwitcher/layerSwitcher.component';
import { OpacitySwitcherComponent } from './components/mappa/control/layerSwitcher/opacitySwitcher/opacitySwitcher.component';
import { CurrentSession } from './shared/CurrentSession';
import { PanelEvent } from './shared/PanelEvent';
import { Stepper } from './shared/Stepper';
import { ConfirmDialogGisComponent } from './components/confirm-dialog-gis/confirm-dialog-gis.component';
import { CalcolaPoligoniDialogGisComponent } from './components/calcola-poligoni-dialog-gis/calcola-poligoni-dialog-gis.component';
import { AllegatiDialogGisComponent } from './components/allegati-dialog-gis/allegati-dialog-gis.component';
import { AreaFormatPipe } from './shared/AreaFormatPipe.pipe';
import { MapEvent } from './shared/MapEvent';
import { EsitoValidazioneEvent } from './shared/EsitoValidazioneEvent';
import { TabViewModule } from 'primeng-lts/tabview';
import { DecodeEnumPipe } from './shared/DecodeEnumPipe.pipe';
import { ProgressSpinnerModule } from 'primeng-lts/progressspinner';
import { ExternalWindowComponent } from './components/gis/external-window/external-window.component';
import { PortalModule } from '@angular/cdk/portal';
import { SelezioneDichiaratoComponent } from './components/mappa/gisTools/selezione-dichiarato/selezione-dichiarato.component';
import { GisStyles } from './shared/GisStyles';
import { DrawLineBufferComponent } from './components/mappa/gisTools/drawLineBuffer/drawLineBuffer.component';
import { RitagliaADLComponent } from './components/mappa/gisTools/ritagliaADL/ritagliaADL.component';
import { ToolBarEvent } from './shared/ToolBar/ToolBarEvent';
import { ToolBarService } from './shared/ToolBar/toolBar.service';
import { ScalaComponent } from './components/mappa/control/scala/scala.component';
import { BottomBarComponent } from './components/bottombar/bottombar.component';
import { MapNavigatorComponent } from './components/mappa/gisTools/map-navigator/map-navigator.component';
import { MapNavigatorDialogComponent } from './components/mappa/gisTools/map-navigator/map-navigator-dialog/map-navigator-dialog.component';
import { PoligoniDichiaratiTableComponent } from './components/poligoni-dichiarati-table/poligoni-dichiarati-table.component';
import { PoligoniDichiaratiEvent } from './shared/Poligoni-dichiarati/poligoni-dichiarati-event';
import { CheckboxModule } from 'primeng-lts/checkbox';
import { GisMessaggiToastCostants } from './shared/messaggi-toast.constants';
import { PoligoniDichiaratiDialogComponent } from './components/poligoni-dichiarati-dialog/poligoni-dichiarati-dialog.component';
import { PoligoniDichiaratiTableEstesaComponent } from './components/poligoni-dichiarati-table/poligoni-dichiarati-table-estesa/poligoni-dichiarati-table-estesa.component';
import { DragZoomComponent } from './components/mappa/control/dragZoom/dragZoom.component';
import { SettingsComponent } from "./components/mappa/control/settings/settings.component";
import { PanelModule } from "primeng-lts/panel";
import { MultiSelectModule } from "primeng-lts/multiselect";
import { FileSizePipe } from './shared/file-size.pipe';
import { AllegatiRichiestaGisService } from './services/allegati-richiesta-gis.service';
import { AllegatiDichiaratoGisService } from './services/allegati-dichiarato-gis.service';
import { TableValidazioneSenzaAttributiComponent } from './components/table-validazione-senza-attributi/table-validazione-senza-attributi.component';
import { AnnoCampagnaComponent } from './components/mappa/control/anno-campagna/anno-campagna.component';
import { AnnoCampagnaLavorazioneComponent } from './components/anno-campagna-lavorazione/anno-campagna-lavorazione.component';


@NgModule({
    imports: [
        GisRoutingModule,
        ConfirmDialogModule,
        DialogModule,
        ToastModule,
        DropdownModule,
        InputTextModule,
        ButtonModule,
        CalendarModule,
        SidebarModule,
        AutoCompleteModule,
        CommonModule,
        FormsModule,
        OverlayPanelModule,
        TableModule,
        InputSwitchModule,
        TooltipModule,
        AccordionModule,
        TreeTableModule,
        ReactiveFormsModule,
        InfiniteScrollModule,
        TabViewModule,
        ProgressSpinnerModule,
        PortalModule,
        FileUploadModule,
        CheckboxModule,
        PanelModule,
        MultiSelectModule,
        SplitButtonModule
    ],
    providers: [ShowResults, showDettaglioRichiesta, ToastGisComponent, RichiestaModificaSuoloService, AllegatiRichiestaGisService, AllegatiDichiaratoGisService, DetailGisComponent,
        CreazioneLavorazioneService, MapidService, MapdataService, FeatureService, GisCostants, ProfiloUtente, ScalaCostants, MisuraUtils,
        CaricaVettorialiCostants, LavorazioneWorkspaceService, ToolBarService, LayerGisService, LayerAttivi, CurrentSession, PanelEvent,
        Stepper, MapService, LavorazioniEvent, AllegatiEvent, EventiCalcoloAnomalie, EsitoValidazioneEvent, WindowsEvent, ToolBarEvent,
        LavorazioneWorkspaceComponent, PopoutService, GisStyles, ExternalWindowComponent, RightToolBarEvent, PoligoniDichiaratiEvent, MapEvent,
        GisMessaggiToastCostants, PoligoniDichiaratiTableComponent, GestioneCampagnaService],
    declarations: [
        GisComponent,
        ToastGisComponent,
        SidebarComponent,
        RightToolBarComponent,
        SearchGisComponent,
        ResultsGisComponent,
        AllegatiGisComponent,
        DialogsGisComponent,
        AllegatiDialogGisComponent,
        ConfirmDialogGisComponent,
        CalcolaPoligoniDialogGisComponent,
        DetailGisComponent,
        MessaggiGisComponent,
        UploadAttachmentsComponent,
        UploadAttachmentsProgressComponent,
        CreazioneLavorazioneComponent,
        WizardComponentComponent,
        CancellazioneLavorazioneComponent,
        DettaglioLavorazioneComponent,
        LeMieLavorazioniComponent,
        PoligoniSuoloTableComponent,
        OnlynumberDirective,
        MappaComponent,
        LayerComponent,
        ControlComponent,
        MousePositionComponent,
        TreeTableComponent,
        EditToolBarComponent,
        ModifyComponent,
        ModifyAdlComponent,
        DeleteAdlComponent,
        DrawComponent,
        DeleteComponent,
        TrasformaComponent,
        DrawHoleComponent,
        DrawRegularComponent,
        AggiungiNoteComponent,
        MisuraComponent,
        UndoRedoComponent,
        IdentifyComponent,
        SpegniVettorialiComponent,
        SaveComponent,
        UnionComponent,
        CloseHoleComponent,
        SelectComponent,
        DrawTraceComponent,
        LineSplitComponent,
        SuoloDichiaratoComponent,
        MenuContestualeComponent,
        ExternalWindowComponent,
        SuoloVigenteComponent,
        SuoloVigenteDaAdlComponent,
        ZoomBarComponent,
        LayerSwitcherComponent,
        OpacitySwitcherComponent,
        LavorazioneWorkspaceComponent,
        EsitoValidazioneLavorazioneButtonComponent,
        EsitoValidazioneLavorazioneComponent,
        AreaFormatPipe,
        DecodeEnumPipe,
        SelezioneSuoloComponent,
        SelezioneDichiaratoComponent,
        ToolbarLavorazioneComponent,
        DrawLineBufferComponent,
        ScalaComponent,
        RitagliaADLComponent,
        BottomBarComponent,
        DragPanComponent,
        DragZoomComponent,
        CaricaVettorialiComponent,
        ModalCaricaVettorialiComponent,
        MapNavigatorComponent,
        MapNavigatorDialogComponent,
        PoligoniDichiaratiTableComponent,
        PoligoniDichiaratiTableEstesaComponent,
        PoligoniDichiaratiDialogComponent,
        SettingsComponent,
        FileSizePipe,
        TableValidazioneSenzaAttributiComponent,
        SelezioneSuoloAreaComponent,
        AnnoCampagnaComponent,
        AggiungiNoteDialogComponent,
        CaricaLayerStoricoComponent,
        AnnoCampagnaLavorazioneComponent
    ]
})
export class GisModule { }
