import { Injectable } from '@angular/core';

@Injectable()
export class AllegatiEvent {
   allegati: any;
   allegatiCount: any;
   displayAllegati: boolean;
   params: { numeroElementiPagina: number; pagina: number; triggerFromDetail: boolean };
}

