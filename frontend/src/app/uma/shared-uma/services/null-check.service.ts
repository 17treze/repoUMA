import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NullCheckService {

  constructor() { }

  /** empty number */
  isEmpty(el: number): boolean | null {
    return el == null || (el != null && el.toString().trim() == null) || (el != null && el.toString().trim() == '');
  }

  /** empty string */
  isEmptyStr(el: string): boolean | null {
    const str = el.toString();
    return str == null || (str != null && str.trim() == '');
  }

  /** empty object */
  isEmptyObject(obj: any): boolean {
    // null and undefined check || no properties || properties null
    return !obj || (obj && Object.keys(obj).length === 0 && typeof obj === 'object') || this.hasPropertiesNull(obj);
  }

  private hasPropertiesNull(obj: any): boolean {
    if (!obj) {
      return true;
    }
    return !Object.keys(obj).filter((prop: string) => obj[prop] != null).length;
  }
}
