import { Directive } from '@angular/core';
import { NG_VALIDATORS, ValidationErrors, FormControl } from '@angular/forms';
import { A4gMessages } from 'src/app/a4g-common/a4g-messages';

@Directive({
  selector: '[appValidaNumero2dec]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: ValidaNumero2decDirective, multi: true }
  ]
})
export class ValidaNumero2decDirective {
  constructor() { }

  validate(c: FormControl): ValidationErrors | null {
    if (!this.validaNumero(c.value, true)) {
      return { erroreNumero: A4gMessages.ERRORE_NUMERO };
    } else {
      return null;
    }
  }

  validaNumero(val: string, accettaNoVal: boolean) {
    if (!val) {
      return accettaNoVal;
    }

    const myregexp1 = /^-?\d*\.?\d{0,2}$/;

    if (myregexp1.test(val) === true) {
      return true;
    } else {
      return false;
    }
  }
}
