import { Directive, Input } from '@angular/core';
import { AbstractControl, NG_VALIDATORS, ValidationErrors, Validator } from '@angular/forms';

@Directive({
  selector: '[a4gIban]',
  providers: [{ provide: NG_VALIDATORS, useExisting: Iban, multi: true }]
})
export class Iban implements Validator {

  @Input('a4gIban') data: { inputRef: any };

  constructor() { }

  validate(control: AbstractControl): ValidationErrors {
    const inputValue = control.value as string;
    const ITA_PREFIX = 'IT';

    if (control.value == null || control.value == '') {
      this.data.inputRef.validity.iban = false;  // reset prime ng error
      return null;
    }

    const isValid = inputValue.trim().toUpperCase().startsWith(ITA_PREFIX) ? true : false;  // check se comincia con IT
    this.data.inputRef.validity.iban = isValid ? false : true;
    return !isValid ? { 'iban': true } : null;                                              // form error iban: false = error, null = no error
  }

}
