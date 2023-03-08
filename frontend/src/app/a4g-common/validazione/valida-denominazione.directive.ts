import { Directive, forwardRef, NgModule } from '@angular/core';
import { FormControl, ValidationErrors, NG_VALIDATORS } from '@angular/forms';
import { validaInput } from './validaInput';
import { A4gMessages } from '../a4g-messages';

@Directive({
  selector: '[appValidaDenominazione]',
  providers: [ 
    { provide: NG_VALIDATORS, useExisting: ValidaDenominazioneDirective, multi: true }
  ]
})

export class ValidaDenominazioneDirective {

  constructor() { }

  validate(c: FormControl): ValidationErrors | null {
    // Here we call our static validator function 
    if(!validaInput.validaDenominazione(c.value, true))
      return {denominazioneErrata : A4gMessages.denominazioneErrata};
    else
      return null;
  }
}