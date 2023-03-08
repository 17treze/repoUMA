import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { MapEvent } from '../../../../shared/MapEvent';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { enumListener } from '../enumListeners';
import { GisListener } from '../gisListener';
import OlMap from 'ol/Map';
import Point from 'ol/geom/Point';
import Feature from 'ol/Feature';
import { GeoJSON } from 'ol/format';
import { CreazioneLavorazioneService } from '../../../../services/creazione-lavorazione.service';
import { ToastGisComponent } from '../../../toast-gis/toast-gis.component';

@Component({
  selector: 'gis-selezione-suolo',
  templateUrl: './selezione-suolo.component.html',
  styleUrls: ['./selezione-suolo.component.css']
})
export class SelezioneSuoloComponent extends GisListener implements OnInit, OnDestroy {
  @Input()
  idLavorazione: number;
  func: any;
  map: any;

  @Output() ricaricaSuoloEmitter = new EventEmitter<any>();

  constructor(public creazioneLavorazioneService: CreazioneLavorazioneService, private toastComponent: ToastGisComponent,
    public mapEvent: MapEvent, protected toolBarService1: ToolBarService
  ) {
    super(enumListener.selectSuolo, toolBarService1, null);
  }

  ngOnInit() {
    super.ngOnInit();
    console.log('ngOnInit SelezioneSuoloComponent ');
    // Get the current map or get map by id
    this.map = this.mapEvent.map;

    const _self = this;

    this.func = function singleClickSelectSuolo(evt) {
      if (_self.isActive) {
        console.log('singleclick ');
        _self.cercaSuoloVigenteDaClickInMappa(new GeoJSON().writeFeatureObject(new Feature(new Point(evt.coordinate))),
        _self.idLavorazione).then(value => {
          _self.callParentRicaricaSuolo();
          }).catch(error => {
            console.log('errore singleClickSelectSuolo', error);
            if (error.status === 204) {
              _self.toastComponent.showErrorPoligonoNonTrovato();
            } else {
              _self.toastComponent.showErrorPoligonoCoinvoltoInAltraLavorazione();
            }
          });
      }
    };

    this.map.on('singleclick', this.func);
  }

  ngOnDestroy() {
    super.ngOnDestroy();
    console.log('OnDestroy SelezioneSuoloComponent ');
    this.map.un('singleclick', this.func);
  }

  cercaSuoloVigenteDaClickInMappa(point, idLavorazione) {
    return new Promise((resolve, reject) => {
      this.creazioneLavorazioneService.cercaSuoloVigenteDaClickInMappa(idLavorazione, point)
        .subscribe(response => {
          if (response['status'] === 200 || response['status'] === 201) {
            resolve(response['status']);
          } else {
            reject(response);
          }
        },
          err => { reject(err); }
        );
    });
  }

  callParentRicaricaSuolo(): void {
    this.ricaricaSuoloEmitter.emit();
  }

  get canUseListener(): boolean {
    return true;
  }
}
