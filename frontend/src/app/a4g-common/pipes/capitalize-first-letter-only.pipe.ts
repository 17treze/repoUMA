import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'capitalizeFirstLetterOnly',
  pure: false
})
export class CapitalizeFirstLetterOnlyPipe implements PipeTransform {

  transform(value: string): string {
    if (!value) {
      return '';
    }
    return value.charAt(0).toUpperCase() + value.toLowerCase().slice(1);
  }

}
