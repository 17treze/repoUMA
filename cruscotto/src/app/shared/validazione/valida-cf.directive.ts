import { Directive, forwardRef } from '@angular/core';
import { FormControl, ValidationErrors, NG_VALIDATORS } from '@angular/forms';
import { validaInput } from './validaInput';
import { Message } from "primeng-lts"
import { A4gMessages } from '../a4g-messages';

@Directive({
  selector: '[appValidaCf]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: ValidaCfDirective, multi: true }
  ]
})

export class ValidaCfDirective {

  constructor() { }

  validate(c: FormControl): ValidationErrors | null {
    if(!validaInput.validaCf(c.value, false))
      return {cfErrato : A4gMessages.cfErrato};
    else
      return null;
  }

}
