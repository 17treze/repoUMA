import { Injectable } from '@angular/core';
import { SelectItem } from 'primeng/api';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GisUtilsService {
  constructor() { }

  public getComboAnniCampagna( elementoNullo: boolean): Array<SelectItem> {
    const anniCampagna = new Array<SelectItem>();

    if (elementoNullo) {
      anniCampagna.push({label: '', value: ''});
    }

    environment.anniGis.forEach(element => {
      anniCampagna.push({label: element.toString(), value: element});
    });

    return anniCampagna;
  }
}
