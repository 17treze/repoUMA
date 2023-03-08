import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  pure: false,
  name: 'replaceWithSpaceBy'
})
export class ReplaceWithSpaceByPipe implements PipeTransform {

  transform(value: string, toReplace: string): any {
    if (!value || value.indexOf(toReplace) <= -1) {
      return value;
    }
    const replaced = value.split(toReplace).join(' ');
    return replaced;
  }

}
