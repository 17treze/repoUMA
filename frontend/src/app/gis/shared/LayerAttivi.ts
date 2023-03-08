import { Injectable } from '@angular/core';

@Injectable()
export class LayerAttivi {
  layerIspezionabili: {
    titolo: string;
    identify: boolean;
    abilitato: boolean;
    displayInLayerSwitcher: boolean;
    codice: string;
    workspace: string;
    campoIdentify: string }[];
}

