import { Directive, Input } from '@angular/core';
import { AbstractControl, NG_VALIDATORS, ValidationErrors, Validator } from '@angular/forms';
import { A4gMultiTableColumn } from '../a4g-multi-table/a4g-multi-table-model';

@Directive({
  selector: '[a4gMaxValue]',
  providers: [{ provide: NG_VALIDATORS, useExisting: MaxValue, multi: true }]
})
export class MaxValue implements Validator {

  @Input('a4gMaxValue') data: { max: number, inputRef: any, row: any, col: A4gMultiTableColumn };

  constructor() { }

  validate(control: AbstractControl): ValidationErrors {
    const inputValue = Number(control.value);
    const limitUpValue = Number(this.data.max);

    let sum: number = inputValue;
    if (control.value == null || this.data == null || this.data.max == null) {
      this.data.inputRef.validity.maxVal = false;  // reset error
      return null;
    }
    if (control && isNaN(inputValue)) {
      this.data.inputRef.validity.maxVal = false;  // reset error
      return null;
    }
    if (control && isNaN(limitUpValue)) {
      this.data.inputRef.validity.maxVal = false;  // reset error
      return null;
    }
  
    const isValid = sum <= limitUpValue ? true : false;       // check gasolio+benzina <= sup massima
    this.data.inputRef.validity.maxVal = isValid ? false : true; // validity prime-ng error: true = error, false = no error
    return !isValid ? { 'maxVal': false } : null;                // form error max: false = error, null = no error
  }

}
