import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'areaFormatPipe'
})
export class AreaFormatPipe implements PipeTransform {

  transform(val: number): string {
    if (val !== undefined && val !== null) {

      const with1Decimals = val.toString().match(/^-?\d+(?:\.\d{0,1})?/)[0]
      return with1Decimals.replace('.', ',').replace(/\B(?=(\d{3})+(?!\d))/g, '.');
    } else {
      return '-';
    }
  }
}
