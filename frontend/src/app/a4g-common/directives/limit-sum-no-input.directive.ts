import { Directive, Input } from '@angular/core';
import { AbstractControl, NG_VALIDATORS, ValidationErrors, Validator } from '@angular/forms';


@Directive({
  selector: '[a4gLimitSumNoInput]',
  providers: [{ provide: NG_VALIDATORS, useExisting: LimitSumNoInput, multi: true }]
})
export class LimitSumNoInput implements Validator {

  @Input('a4gLimitSumNoInput') data: { toSum: Array<string>, limit: string };

  constructor() { }

  validate(control: AbstractControl): ValidationErrors {
    if (this.data.toSum == null || this.data.toSum.length == 0) {
      return null;
    }
    let toSumFieldControl = null;
    let toSumValue = 0;
    this.data.toSum.forEach(fieldName => {
      toSumFieldControl = control.parent.get(`${fieldName}`);
      if (toSumFieldControl) {
        toSumValue += Number(toSumFieldControl.value);
      }
    });

    const limitField = control.parent.get(`${this.data.limit}`);
    const limitValue = limitField && Number(limitField.value);

    if (control.value == null || this.data == null || this.data.limit == null) {
      return null;
    }
    if (control && isNaN(toSumValue)) {
      return null;
    }
    if (control && isNaN(limitValue)) {
      return null;
    }

    const isValid = (toSumValue) <= limitValue ? true : false;
    !isValid ? this.setErrorOnFields(control) : this.unsetErrorOnFields(control); // set/reset degli errori sugli altri campi da sommare
    return !isValid ? { maxArray: { value: control.value } } : null;                                // form error max: value => error, null => no error
  }

  private setErrorOnFields(control: AbstractControl) {
    let fieldControl = null;
    this.data.toSum.forEach(fieldName => {
      fieldControl = control.parent.get(`${fieldName}`);
      fieldControl.markAsTouched();
      fieldControl.setErrors({ maxArray: { value: fieldControl.value } })
    });
  }

  private unsetErrorOnFields(control: AbstractControl) {
    let fieldControl = null;
    this.data.toSum.forEach(fieldName => {
      fieldControl = control.parent.get(`${fieldName}`);
      fieldControl.markAsUntouched();
      fieldControl.setErrors(null)
    });
  }

}

