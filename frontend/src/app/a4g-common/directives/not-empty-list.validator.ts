import { Directive, Input } from '@angular/core';
import { AbstractControl, NG_VALIDATORS, ValidationErrors, Validator } from '@angular/forms';

@Directive({
  selector: '[a4gNotEmptyList]',
  providers: [{ provide: NG_VALIDATORS, useExisting: NotEmptyList, multi: true }]
})
export class NotEmptyList implements Validator {

  constructor() { }

  @Input('a4gNotEmptyList') data: { list: Array<any>, active: { on: boolean, field: string } }; /* active on: true/false abilita o disabilita  la direttiva**/ 

  validate(control: AbstractControl): ValidationErrors {
    const inputValue = parseInt(control.value);

    if (control.value == null || this.data == null) {
      return null;
    }
    if (control && isNaN(inputValue)) {
      return null;
    }
    if (control && inputValue === 0) {
      return null;
    }
    if (this.data.active && !this.data.active.on) {
      return null;
    }
    if (this.data.active && this.data.active.on && parseInt(control.value) == parseInt(control.parent.get(`${this.data.active.field}`).value)) {
      return null;
    }

    const isValid = this.data.list && this.data.list.length ? true : false;
    return !isValid ? { emptylist: { value: control.value } } : null;
  }

}
