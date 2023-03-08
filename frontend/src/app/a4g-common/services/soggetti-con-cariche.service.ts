import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { PersonaAgsDto } from 'src/app/uma/core-uma/models/dto/PersonaAgsDto';

@Injectable({
  providedIn: 'root'
})
export class SoggettiConCaricheService {
  public soggettiConCariche$ = new BehaviorSubject<Array<PersonaAgsDto>>([]);
  private _sogg: Array<PersonaAgsDto>;

  constructor() { }

  updatedData(data: Array<PersonaAgsDto>) {
    this.soggettiConCariche$.next(data);
  }

  get soggetti(): Array<any> {
    return this._sogg;
  }

  set soggetti(value: Array<any>) {
    this._sogg = value;
  }

}
