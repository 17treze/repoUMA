import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'squareMetersToHectars'
})
export class SquareMetersToHectarsPipe implements PipeTransform {

  transform(value: number): string {
    return (value / 10000).toFixed(2);
  }

}
