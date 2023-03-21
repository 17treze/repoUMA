import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'convertCodiceOperazioneToReadableString'
})
export class ConvertCodiceOperazioneToReadableStringPipe implements PipeTransform {

  transform(value: string, ...args: any[]): string {
    if (value.startsWith('M')) {
      const replaceUnderscoreWithDotRegex = /_/gi;
      return value.replace(replaceUnderscoreWithDotRegex, '.').replace('O', '').substring(1, value.length);
    } else {
      if (value.startsWith('11')) {
        return '11';
      }
      if (value.length === 4) {
        return value.slice(0, 2) + '.' + value[2] + '.' + value[3];
      }
      if (value.length === 2) {
        return value + '.1.1';
      }
    }
  }

}
