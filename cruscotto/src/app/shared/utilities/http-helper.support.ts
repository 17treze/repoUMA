export class HttpHelperSupport {
  
  public static buildQueryStringFromObject(obj: any): URLSearchParams {
    const params = new URLSearchParams();
    for (const prop in obj) {
      if (obj[prop] != null) {
        if ((typeof obj[prop] === 'string') ||
          (typeof obj[prop] === 'number') ||
          (typeof obj[prop] === 'boolean')) {
          params.append(prop, obj[prop]);
        } else if (typeof obj[prop] === 'object' && Array.isArray(obj[prop]) && obj[prop]) { // caso di array (key, val1) (key, val2) ecc.
          obj[prop].forEach(element => {
            params.append(prop, element);
          });
        }
      }
    }
    return params;
  }
}
