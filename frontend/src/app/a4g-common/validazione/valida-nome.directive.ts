import { Directive } from '@angular/core';
import { FormControl, ValidationErrors, NG_VALIDATORS } from '@angular/forms';
import { validaInput } from './validaInput';
import { A4gMessages } from '../a4g-messages';

@Directive({
  selector: '[appValidaNome]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: ValidaNomeDirective, multi: true }
  ]
})

export class ValidaNomeDirective {

  constructor() { }

  validate(c: FormControl): ValidationErrors | null {
    // Here we call our static validator function 
    if (!validaInput.validaNome(c.value, true))
      return { nomeErrato: A4gMessages.nomeErrato };
    else
      return null;
  }
}