import { Directive, Input } from '@angular/core';
import { AbstractControl, NG_VALIDATORS, ValidationErrors, Validator } from '@angular/forms';

@Directive({
  selector: '[a4gNotZero]',
  providers: [{ provide: NG_VALIDATORS, useExisting: NotZero, multi: true }]
})
export class NotZero implements Validator {

  constructor() { }

  @Input('a4gNotZero') data: { list: Array<any>, active: { on: boolean, field: string }};

  validate(control: AbstractControl): ValidationErrors {
    const inputValue = parseInt(control.value);

    if (control == null || control.value == null || this.data == null) {
      return null;
    }

    if (this.data.active && !this.data.active.on) {
      return null;
    }

    if (this.data.active && this.data.active.on && parseInt(control.value) == parseInt(control.parent.get(`${this.data.active.field}`).value)) {
      return null;
    }

    const isInvalid = this.data.list && this.data.list.length && (inputValue == 0 || control.value.toString().trim() === '') ? true : false;
    return isInvalid ? { notZero: { value: control.value } } : null;
  }

}
