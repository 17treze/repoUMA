import { Directive, forwardRef } from '@angular/core';
import { FormControl, ValidationErrors, NG_VALIDATORS } from '@angular/forms';
import { validaInput } from './validaInput';
import { A4gMessages } from '../a4g-messages';

@Directive({
  selector: '[appValidaCuaaCompleto]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: ValidaCuaaCompletoDirective, multi: true }
  ]
})
export class ValidaCuaaCompletoDirective {

  constructor() { }

  validate(c: FormControl): ValidationErrors | null {
    if(!validaInput.validaCuaaIntero(c.value, false))
      return {cuaaErratoCompleto : A4gMessages.cuaaErratoCompleto};
    else
      return null;
  }

}
