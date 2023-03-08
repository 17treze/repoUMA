import { Injectable } from '@angular/core';
import { SortEvent } from 'primeng-lts';
import * as moment from 'moment';

@Injectable({
  providedIn: 'root'
})
export class A4gMultiTableService {

  constructor() { }

  /** ordinamento custom con differenziazione su campo data */
  customSortFn($event: SortEvent) {
    $event.data.sort((data1: any, data2: any) => {
      let value1 = data1[$event.field];
      let value2 = data2[$event.field];
      let result = null;

      if (value1 == null && value2 != null)
        result = -1;
      else if (value1 != null && value2 == null)
        result = 1;
      else if (value1 == null && value2 == null)
        result = 0;
      else if (!moment(value1, 'DD/MM/YYYY', true).isValid() && moment(value2, 'DD/MM/YYYY', true).isValid())
        result = -1;
      else if (moment(value1, 'DD/MM/YYYY', true).isValid() && !moment(value2, 'DD/MM/YYYY', true).isValid())
        result = 1;
      else if (moment(value1, 'DD/MM/YYYY', true).isValid() && moment(value2, 'DD/MM/YYYY', true).isValid())
        result = (moment(value1, 'DD/MM/YYYY').isBefore(moment(value2, 'DD/MM/YYYY'))) ? -1 : (moment(value1, 'DD/MM/YYYY').isAfter(moment(value2, 'DD/MM/YYYY'))) ? 1 : 0;
      else if (typeof value1 === 'string' && typeof value2 === 'string')
        result = value1.localeCompare(value2);
      else
        result = (value1 < value2) ? -1 : (value1 > value2) ? 1 : 0;

      return ($event.order * result);
    });

  }
}
