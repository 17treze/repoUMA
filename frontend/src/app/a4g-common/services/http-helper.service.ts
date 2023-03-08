import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class HttpHelperService {
  constructor() { }

  buildQueryStringFromObject(obj: any): URLSearchParams {
    const params = new URLSearchParams();
    for (const prop in obj) {
      if (obj[prop] != null) {
        if ((typeof obj[prop] === 'string') || // caso banale con valori singoli 
          (typeof obj[prop] === 'number') ||
          (typeof obj[prop] === 'boolean')) {
          params.append(prop, obj[prop]);
        } else if (typeof obj[prop] === 'object' && Array.isArray(obj[prop]) && obj[prop]) { // caso di array (key, value1) (key, value2) - [key=value1&key=value2&...]
          obj[prop].forEach(element => {
            params.append(prop, element);
          });
        }
      }
    }
    return params;
  }

  getParamsFromObject(obj: Object): HttpParams {
    let params = new HttpParams();
    Object.keys(obj).forEach((prop) => {
      if (obj[prop] != null) {
        if ((typeof obj[prop] === 'string') || // caso banale con valori singoli 
          (typeof obj[prop] === 'number') ||
          (typeof obj[prop] === 'boolean')) {
          params = params.append(prop, obj[prop]);
        } else if (typeof obj[prop] === 'object' && Array.isArray(obj[prop]) && obj[prop]) { // caso di array (key, value1) (key, value2) - [key=value1&key=value2&...]
          obj[prop].forEach(element => {
            params = params.append(prop, element);
          });
        }
      }
    });
    return params;
  }
}
