import { Injectable, Output, EventEmitter } from '@angular/core';
import { DomandaIstruttoriaDettaglio } from './domain/domandaIstruttoriaDettaglio';

@Injectable()
export class SharedService {
  domandSettata = new EventEmitter<any>();
  constructor() { }
}
