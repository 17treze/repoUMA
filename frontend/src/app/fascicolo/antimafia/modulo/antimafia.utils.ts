
import { SoggettiImpresa } from '../classi/datiDichiarazione';
import { Labels } from 'src/app/app.labels';
import { isObject } from 'util';

export class Utils {
  //Inizio Carica (se non presente = data creazione aziende)
  static setInizioCarica(soggettiImpresa: SoggettiImpresa[], dataIscrizione: string) {
    soggettiImpresa.forEach(sogg => sogg.carica.forEach(
      carica => carica.dataInizio ? carica.dataInizio : carica.dataInizio = dataIscrizione
    )
    );
  }
  static isImpresaDittaIndividuale(formaGiuridicaCodice: string): boolean {
    if (
      formaGiuridicaCodice === 'DI'
    ) {
      return true;
    }
    return false;
  }

  static setAllNullValuesToND(object: any): void {
    const keys = Object.keys(object);
    keys.forEach(x => {
      if (isObject(object[x])) {
        this.setAllNullValuesToND(object[x]);
      } else {
        if (!object[x]) {
          object[x] = Labels.nd;
        }
      }
    });
  }

  static isObject(x: any): x is object {
    console.log('is object : ' + x);
    return typeof x === 'object';
  }

  //per una corretta visualizzazione vengono i sono soggetti con più cariche vengono splittati
  //in soggetti con singola carica
  static soggettiAppiattito(soggettiImpresa: SoggettiImpresa[]): SoggettiImpresa[] {
    // nel caso di più cariche duplico l'elemento per poterlo visualizzare correttamente
    let soggettiCloned = soggettiImpresa.map(x => Object.assign({}, x));
    const soggettiResultFlat: SoggettiImpresa[] = [];
    soggettiCloned.forEach(sogg => {
      sogg.carica.forEach(carica => {
        // clona soggetto per evitare problemi di referenza
        const sg: SoggettiImpresa = Object.assign({}, sogg);
        sg.carica = [carica];
        soggettiResultFlat.push(sg);
      });
    });
    return soggettiResultFlat;
  }
}
