import { Directive  } from '@angular/core';
import { FormControl, ValidationErrors, NG_VALIDATORS } from '@angular/forms';
import { validaInput } from './validaInput';
import { A4gMessages } from '../a4g-messages';

@Directive({
  selector: '[appValidaTelefono]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: ValidaTelefonoDirective, multi: true }
  ]
})
export class ValidaTelefonoDirective {

  constructor() { }

  validate(c: FormControl): ValidationErrors | null {
    if(!validaInput.validaTelefono(c.value, true))
      return {telefonoErrato : A4gMessages.telefonoErrato};
    else
      return null;
  }
}
