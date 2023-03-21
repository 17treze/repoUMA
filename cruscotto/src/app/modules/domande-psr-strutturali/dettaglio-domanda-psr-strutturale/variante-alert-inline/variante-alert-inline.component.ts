import { Component, Input } from '@angular/core';
import { Variante } from '../../models/variante';

@Component({
  selector: 'app-variante-alert-inline',
  templateUrl: './variante-alert-inline.component.html',
  styleUrls: ['./variante-alert-inline.component.css']
})
export class VarianteAlertInlineComponent {

  @Input()
  varianti: Variante[];

}
