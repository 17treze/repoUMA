import { Directive, Input } from '@angular/core';
import { AbstractControl, NG_VALIDATORS, ValidationErrors, Validator } from '@angular/forms';
import { TipoCarburante } from 'src/app/uma/core-uma/models/enums/TipoCarburante.enum';
import { A4gMultiTableColumn } from '../a4g-multi-table/a4g-multi-table-model';

@Directive({
  selector: '[a4gMaxInputSize]',
  providers: [{ provide: NG_VALIDATORS, useExisting: MaxInputSize, multi: true }]
})
export class MaxInputSize implements Validator {

  @Input('a4gMaxInputSize') data: { size: number, inputRef: any, row: any, col: A4gMultiTableColumn };

  constructor() { }

  validate(control: AbstractControl): ValidationErrors {
    const inputValue = Number(control.value);
    const limitValue = Number(this.data.size);

    let sum: number = inputValue;
    if (control.value == null || this.data == null || this.data.size == null) {
      this.data.inputRef.validity.max = false;  // reset error
      return null;
    }
    if (control && isNaN(inputValue)) {
      this.data.inputRef.validity.max = false;  // reset error
      return null;
    }
    if (control && isNaN(limitValue)) {
      this.data.inputRef.validity.max = false;  // reset error
      return null;
    }
    if (this.data.col.field === TipoCarburante.BENZINA && !isNaN(Number(this.data.row.GASOLIO))) {
      sum += Number(this.data.row.GASOLIO);
    }
    if (this.data.col.field === TipoCarburante.GASOLIO && !isNaN(Number(this.data.row.BENZINA))) {
      sum += Number(this.data.row.BENZINA);
    }

    const isValid = sum <= limitValue ? true : false;     // check gasolio+benzina <= sup massima
    this.data.inputRef.validity.max = isValid ? false : true; // validity prime-ng error: true = error, false = no error
    return !isValid ? { 'max': false } : null;                // form error max: false = error, null = no error
  }

}
