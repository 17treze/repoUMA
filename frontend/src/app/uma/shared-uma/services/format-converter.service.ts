import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FormatConverterService {

  constructor() { }

  toNumber(value: string | number) {
    return !isNaN(+value) ? +value : 0;
  }

  toNumberOrNull(value: string) {
    return !isNaN(parseInt(value)) ? parseInt(value) : null;
  }

  /** < 0 include anche spazi e caratteri non numerici */
  isLessThanZero(value: string | number): boolean {
    const stringified = value && value.toString() || '';
    const toNumber = parseInt(stringified);
    return !isNaN(toNumber) ? toNumber < 0 : true;
  }

  isEmptyString(value: string | number) {
    return value == null || value.toString().trim() == '';
  }

  isMultipleBy(value: number, multiple: number) {
    return value % multiple == 0;
  }
}
