import { Directive } from '@angular/core';
import { FormControl, ValidationErrors, NG_VALIDATORS } from '@angular/forms';
import { validaInput } from './validaInput';
import { A4gMessages } from '../a4g-messages';

@Directive({
  selector: '[appValidaCognome]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: ValidaCognomeDirective, multi: true }
  ]
})

export class ValidaCognomeDirective {

  constructor() { }

  validate(c: FormControl): ValidationErrors | null {
    // Here we call our static validator function 
    if (!validaInput.validaCognome(c.value, true))
      return { cognomeErrato: A4gMessages.cognomeErrato };
    else
      return null;
  }
}