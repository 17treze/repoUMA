import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'decodeEnumPipe'
})
export class DecodeEnumPipe implements PipeTransform {

  transform(valueString: any, enumList: any[]): string {
    if (valueString !== undefined && valueString !== null) {
      if (valueString && enumList) {
        const mappingStato = enumList.filter(x => x.value === valueString);
        if (mappingStato && mappingStato[0]) {
          return mappingStato[0].name;
        }
      }
    }
  }
}