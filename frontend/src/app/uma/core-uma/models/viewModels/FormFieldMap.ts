import { AbstractControl } from '@angular/forms';

export interface FormFieldMap {
    name: string;
    control: AbstractControl;
}