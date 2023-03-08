import { Directive, Input } from "@angular/core";
import {
  AbstractControl,
  NG_VALIDATORS,
  ValidationErrors,
  Validator,
} from "@angular/forms";

@Directive({
  selector: "[a4gLimitSup]",
  providers: [{ provide: NG_VALIDATORS, useExisting: LimitSup, multi: true }],
})
export class LimitSup implements Validator {

  @Input('a4gLimitSup') data: { limit: string };

  constructor() { }

  validate(control: AbstractControl): ValidationErrors {
    const inputValue = Number(control.value);
    const limitField = control.parent.get(`${this.data.limit}`);
    const limitValue = limitField && Number(limitField.value);

    if (control.value == null || this.data == null || this.data.limit == null) {
      return null;
    }
    if (control && isNaN(inputValue)) {
      return null;
    }
    if (control && isNaN(limitValue)) {
      return null;
    }

    const isValid = inputValue <= limitValue ? true : false;
    return !isValid ? { max: { value: control.value } } : null;                // form error max: false = error, null = no error
  }
}
