export interface ComnuniCatastali {
  count: Count;
  risultati: Risultati;
}

export interface Comune {
  codiceFiscale: 'string';
  codiceIstat: 'string';
  denominazione: 'string';
}

export interface Provincia {
  codiceFiscale: 'string';
  codiceIstat: 'string';
  denominazione: 'string';
}

export interface Risultati {
  length: number;
  codice: 'string';
  denominazione: 'string';
  comune: Comune;
  provincia: Provincia;
}

export interface Count {
  count: number;
}

export interface RicercaLocalita{
  id : number;
  geom_st_box2d : 'string';
  label : 'string';
  featureId : number;
  layerBodId : 'string';

}
/*
 {
            "id": 1,
            "weight": 29,
            "attrs": {
                "num": 1,
                "origin": "top010",
                "layerBodId": "Toponomastica",
                "lon": "0",
                "label": "ALA (ALA)",
                "geom_st_box2d": "BOX(655911.0816 5069019.319,655911.0816 5069019.319)",
                "x": 655911.0816,
                "geom_quadindex": "0",
                "y": 5069019.319,
                "rank": 1,
                "detail": "ALA (ALA)",
                "featureId": "1402",
                "lat": "0"
            }
        },
*/