import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  pure: false,
  name: 'clearString'
})
export class ClearStringPipe implements PipeTransform {

  transform(value: string, toExclude: string): any {
    if (value !== toExclude){
      return value;
    }
    return '';
  }

}
