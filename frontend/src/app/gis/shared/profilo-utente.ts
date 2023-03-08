import { Injectable } from '@angular/core';

@Injectable()
export class ProfiloUtente {
  profilo: string;
}

export enum NameProfilo {
  CAA = 'caa',
  BACKOFFICE = 'backoffice'
}


