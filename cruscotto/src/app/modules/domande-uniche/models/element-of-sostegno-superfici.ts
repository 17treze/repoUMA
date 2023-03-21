export class ElementOfSostegnoSuperfici {

  label: string;
  value: number;
  richiesto: boolean;

  public static of(label: string, value: number, richiesto: boolean): ElementOfSostegnoSuperfici {
    let element: ElementOfSostegnoSuperfici = new ElementOfSostegnoSuperfici();
    element.label = label;
    element.value = value;
    element.richiesto = richiesto;
    return element;
  }

}
