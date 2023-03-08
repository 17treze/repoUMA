import { environment } from "../../../environments/environment";
import { AttributiLayer } from "./AttributiLayer";
import { PropertyLayer } from "./PropertyLayer.enum";

export class LayerConfig {

    id: number;
    codice: string;
    url: string;
    private nomeLayerMappa: string;
    private nomeLayerMappaOrigin: string;
    private nomeLayerDb: string;
    workspace: string;
    mapProperty: Map<string, string>;
    attributi: AttributiLayer[];
    loaded: boolean;

    constructor(id, codice, url, nomeLayer, workspace, proprieta, attributi) {
        this.id = id;
        this.codice = codice;
        this.url = url;
        this.nomeLayerMappa = nomeLayer;
        this.nomeLayerMappaOrigin = nomeLayer;
        this.nomeLayerDb = nomeLayer;
        this.workspace = workspace;
        this.mapProperty = new Map(proprieta.map(key => [key.nome, key.valore] as [string, string]));
        this.attributi = attributi;
        this.loaded = false;
    }

    setTmpNomeLayer(newTmpaName) {
        this.nomeLayerDb = newTmpaName;
    }

    restoreNomeLayer() {
        this.nomeLayerDb = this.nomeLayerMappaOrigin;
    }

    getNomeLayer(annoCampagna) {
        let nomeLayerDb = this.nomeLayerDb;
        if (this.mapProperty.get(PropertyLayer.SPECIFICO_PER_CAMPAGNA) &&
            this.mapProperty.get(PropertyLayer.SPECIFICO_PER_CAMPAGNA) === PropertyLayer.SI) {
            nomeLayerDb = nomeLayerDb + '_' + annoCampagna;
        }
        return nomeLayerDb;
    }
}
