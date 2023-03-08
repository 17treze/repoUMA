import { KeyValue } from "../classi/KeyValue";

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
