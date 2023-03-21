import { Directive, forwardRef } from '@angular/core';
import { FormControl, ValidationErrors, NG_VALIDATORS } from '@angular/forms';
import { validaInput } from './validaInput';
import { A4gMessages } from '../a4g-messages';

@Directive({
  selector: '[appValidaEmail]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: ValidaEmailDirective, multi: true }
  ]
})
export class ValidaEmailDirective {

  constructor() { }

  validate(c: FormControl): ValidationErrors | null {
    if(!validaInput.validaEmail(c.value, true))
      return {emailErrata : A4gMessages.emailErrata}
    else
      return null;
  }
}
