import { KeyValue, KeyValueExt } from "../models/KeyValue";

export interface A4gAccordion {
    headerTitle: string;
    fields: Array<A4gAccordionField>;
    imgPath: string;
}
export class A4gAccordionField extends KeyValue {
    showButton?: string;
    longText?: boolean;
    colorButton?: string;
}

export interface A4gAccordionExt {
  headerTitle: string;
  fields: Array<A4gAccordionFieldExt>;
  imgPath: string;
}
export class A4gAccordionFieldExt extends KeyValueExt {
  showButton?: string;
  longText?: boolean;
  colorButton?: string;
  noBorder?: boolean;
}
