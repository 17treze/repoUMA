import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'descrizioneByCodice'
})
export class DescrizioneByCodicePipe implements PipeTransform {

  transform(value: string): string {
    switch (value) {
      case '10.1.1': {
        return 'Gestione aree prative miglioramento della biodiversità legata ai prati permanenti';
      }
      case
      '10.1.2': {
        return 'Gestione delle superfici a pascolo: aiuti a favore dell’alpeggio';
      }
      case
      '10.1.3': {
        return 'Allevamento di razze animali minacciate di estinzione';
      }
      case
      '10.1.4': {
        return 'Coltivazione di specie vegetali minacciate di erosione genetica';
      }
      case
      '11.1.1': {
        return 'Sostegno all’introduzione del metodo biologico';
      }
      case
      '11.2.1': {
        return 'Mantenimento del metodo biologico';
      }
      case
      '13.1.1': {
        return 'Indennità compensativa per gli agricoltori delle zone montane';
      }
      case
      '11': {
        return 'Metodo biologico';
      }
    }
  }
}
