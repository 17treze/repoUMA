import { Injectable } from '@angular/core';
import { AbstractControl, FormGroup } from '@angular/forms';
import { FormFieldMap } from '../../uma/core-uma/models/viewModels/FormFieldMap';

@Injectable({
  providedIn: 'root'
})
export class FormService {

  constructor() { }

  /** mark errors for each field, to show messages */
  public validateFormByErrors(form: FormGroup, errorsToCheck: Array<string>) {
    Object.keys(form.controls).forEach((field: string) => {
      const control = form.controls[field];
      if (control.errors != null && !control.disabled && errorsToCheck && errorsToCheck.length) {
        errorsToCheck.forEach((err: string) => {
          if (control.errors[err]) {
            control.markAsTouched({ onlySelf: true });
            control.markAsDirty();
          }
        });
      }
    });
  }
  /** mark errors for each field, to show messages */
  public validateForm(form: FormGroup) {
    Object.keys(form.controls).forEach((field: string) => {
      const control = form.controls[field];
      control.updateValueAndValidity();
      if (control.errors != null && !control.disabled) {
        control.markAsTouched({ onlySelf: true });
        control.markAsDirty();
      }
    });
  }

  public resetForm(form: FormGroup) {
    Object.keys(form.controls).forEach((field: string) => {
      const control = form.controls[field];
      control.updateValueAndValidity();
      if (control.errors != null && !control.disabled) {
        control.markAsPristine({ onlySelf: true });
        control.markAsUntouched();
        control.reset();
        control.setErrors({ required: null });
      }
    });
  }

  public resetControl(control: AbstractControl, form: FormGroup) {
    setTimeout(() => {
      control.clearValidators();
      control.markAsPristine();
      control.markAsUntouched();
      control.reset();
      form.updateValueAndValidity();
    });
  }

  /** mark errors for each field, without skipping array */
  public validateFormWithSkip(form: FormGroup, skippingArray: Array<string>) {
    Object.keys(form.controls).forEach((field: string) => {
      if (skippingArray.indexOf(field) > -1) {
        console.log('Reset FormGroup: ', field);
        const control = form.controls[field];
        this.resetForm(control as FormGroup);
      } else {
        const control = form.controls[field];
        control.updateValueAndValidity();
        if (control.errors != null && !control.disabled) {
          control.markAsTouched({ onlySelf: true });
          control.markAsDirty();
        }
      }
    });
  }

  public getInvalids(form: FormGroup): Array<FormFieldMap> {
    const invalids: Array<FormFieldMap> = [];
    for (let controlName in form.controls) {
      if (form.get(`${controlName}`).status === 'INVALID') {
        invalids.push({ name: controlName, control: form.get(`${controlName}`) });
      }
    }
    return invalids;
  }

  public getInvalidFields(form: FormGroup): Array<AbstractControl> {
    const invalids: Array<AbstractControl> = [];
    for (let controlName in form.controls) {
      if (form.get(`${controlName}`).status === 'INVALID') {
        invalids.push(form.get(`${controlName}`));
      }
    }
    return invalids;
  }

  public getInvalidNames(form: FormGroup): Array<string> {
    const invalids: Array<string> = [];
    for (let controlName in form.controls) {
      if (form.get(`${controlName}`).status === 'INVALID') {
        invalids.push(controlName);
      }
    }
    return invalids;
  }

  public getValids(form: FormGroup) {
    const valids = [];
    for (let controlName in form.controls) {
      if (form.get(`${controlName}`).status === 'VALID') {
        valids.push(controlName);
      }
    }
    return valids;
  }

  public getDisabled(form: FormGroup) {
    const disableds = [];
    for (let controlName in form.controls) {
      if (form.get(`${controlName}`).status === 'DISABLED') {
        disableds.push(controlName);
      }
    }
    return disableds;
  }
}
