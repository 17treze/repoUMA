import { Directive, Input } from '@angular/core';
import { AbstractControl, NG_VALIDATORS, ValidationErrors, Validator } from '@angular/forms';

@Directive({
  selector: '[a4gLimitSum]',
  providers: [{ provide: NG_VALIDATORS, useExisting: LimitSum, multi: true }]
})
export class LimitSum implements Validator {

  @Input('a4gLimitSum') data: { toSum: string, limit: string };

  constructor() { }

  validate(control: AbstractControl): ValidationErrors {
    const inputValue = Number(control.value);
    const toSumField = control.parent.get(`${this.data.toSum}`);
    const toSumValue = toSumField && Number(toSumField.value);
    const limitField = control.parent.get(`${this.data.limit}`);
    const limitValue = limitField && Number(limitField.value);

    if (control.value == null || this.data == null || this.data.limit == null) {
      return null;
    }
    if (control && isNaN(inputValue)) {
      return null;
    }
    if (control && isNaN(toSumValue)) {
      return null;
    }
    if (control && isNaN(limitValue)) {
      return null;
    }

    const isValid = (inputValue + toSumValue) <= limitValue ? true : false;
    !isValid ? this.setErrorByField(toSumField) : this.unsetErrorByField(toSumField); // set/reset degli errori sul campo da sommare
    return !isValid ? { max: { value: control.value } } : null;                       // form error max: value => error, null => no error
  }

  private setErrorByField(field: AbstractControl) {
    field.markAsTouched();
    field.setErrors({ max: { value: field.value } })
  }

  private unsetErrorByField(field: AbstractControl) {
    field.markAsUntouched();
    field.setErrors(null);
  }

}
