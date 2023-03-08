import { EnumBase } from "./enumBase";

export enum CodiciErroreClipAdl {
    VALIDAZIONE_ADL = 'VALIDAZIONE_ADL',
    ADL_CLIP_ERRATA = 'ADL_CLIP_ERRATA',
    NO_SUOLO_IN_ADL = 'NO_SUOLO_IN_ADL',
    SUOLO_PRENOTATO = 'SUOLO_PRENOTATO'
}

export class CodiciErroreClipAdlDecode extends EnumBase {
    static lista = [
    { name: "Area Di Lavoro con  geometria non valida", value: CodiciErroreClipAdl.VALIDAZIONE_ADL },
    { name: "Ci sono stati errori nell' elaborazione dell Area Di Lavoro", value: CodiciErroreClipAdl.ADL_CLIP_ERRATA },
    { name: "L'Area Di Lavoro disegnata non è coperta da suolo", value: CodiciErroreClipAdl.NO_SUOLO_IN_ADL },
    { name: "L'Area Di Lavoro disegnata coinvolge poligoni di suolo prenotati in altre lavorazioni", value: CodiciErroreClipAdl.SUOLO_PRENOTATO }
    
  ];

  static decode(input): string {
    return super.innerDecode(input, CodiciErroreClipAdlDecode.lista);
  }
}