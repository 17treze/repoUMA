import { Sostegno } from './sostegno';

export class RiepilogoSostegno {
    counter: number;
    order: number;
    descrizioneSostegno: string;

    constructor(orderIn: number, descrizioneIn: string, counterIn: number) {
        this.order = orderIn;
        this.counter = counterIn;
        this.descrizioneSostegno = descrizioneIn;

    }

   static ritornaSostegno(sostegno: string): string {
        if (sostegno === Sostegno.SOSTEGNO_DISACCOPPIATO) {
            sostegno = 'richiestaDisaccoppiato';
          }
          if (sostegno === Sostegno.SOSTEGNO_SUPERFICI) {
            sostegno = 'richiestaSuperfici';
          }
          if (sostegno === Sostegno.SOSTEGNO_ZOOTECNIA) {
            sostegno = 'richiestaZootecnia';
          }
          return sostegno;
    }

}
