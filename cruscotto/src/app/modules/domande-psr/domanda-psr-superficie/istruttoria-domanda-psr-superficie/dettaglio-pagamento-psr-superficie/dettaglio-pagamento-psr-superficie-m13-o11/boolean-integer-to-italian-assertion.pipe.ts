import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'booleanIntegerToItalianAssertion'
})
export class BooleanIntegerToItalianAssertionPipe implements PipeTransform {

  transform(value: string): any {
    if (value === '0') {
      return 'NO';
    } else {
      return 'SI';
    }
  }

}
