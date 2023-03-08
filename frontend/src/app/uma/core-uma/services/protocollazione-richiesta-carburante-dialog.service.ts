import { Injectable } from '@angular/core';
import { OverlayPanel } from 'primeng/overlaypanel';

@Injectable({
  providedIn: 'root'
})
export class ProtocollazioneRichiestaCarburanteDialogService {


  constructor() { }

  openPanel(panel: OverlayPanel, event: any = null, target: any = null) {
    panel.show(event, target);
  }

}
