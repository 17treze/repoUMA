import { Directive, forwardRef } from '@angular/core';
import { FormControl, ValidationErrors, NG_VALIDATORS } from '@angular/forms';
import { validaInput } from './validaInput';
import { A4gMessages } from '../a4g-messages';

@Directive({
  selector: '[appValidaCuaa]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: ValidaCuaaDirective, multi: true }
  ]
})
export class ValidaCuaaDirective {

  constructor() { }

  validate(c: FormControl): ValidationErrors | null {
    // Here we call our static validator function 
    // if(!validaInput.validaCuaaIntero(c.value, true))
    if(!validaInput.validaCuaaParziale(c.value, true))
      return {cuaaErrato : A4gMessages.cuaaErrato};
    else
      return null;
  }
}
