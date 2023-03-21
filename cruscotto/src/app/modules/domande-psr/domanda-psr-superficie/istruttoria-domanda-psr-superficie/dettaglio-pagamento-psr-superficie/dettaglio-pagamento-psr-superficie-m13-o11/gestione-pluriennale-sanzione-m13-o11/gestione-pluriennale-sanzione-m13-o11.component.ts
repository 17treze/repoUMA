import { Component, Input } from '@angular/core';
import { GestionePluriennaleSanzioni } from '../../../../../models/dettaglio-pagamento-psr';

@Component({
  selector: 'app-gestione-pluriennale-sanzione-m13-o11',
  templateUrl: './gestione-pluriennale-sanzione-m13-o11.component.html',
  styleUrls: ['./gestione-pluriennale-sanzione-m13-o11.component.css']
})
export class GestionePluriennaleSanzioneM13O11Component {

  @Input()
  gestionePluriennaleSanzioni: GestionePluriennaleSanzioni;
}
