import { DecimalPipe } from '@angular/common';

export class RoundingSupport {
  public static   round(value, format): number {
    const formattedValue = new DecimalPipe('it').transform(value, format);
    return  parseFloat(formattedValue);
  }
}

