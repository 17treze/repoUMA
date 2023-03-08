import { Component, OnInit } from '@angular/core';
import { TipoIntestazioneUma } from 'src/app/a4g-common/classi/enums/uma/TipoDocumentoUma.enum';
@Component({
  selector: 'app-gestione-dichiarazione-consumi',
  templateUrl: './gestione-dichiarazione-consumi.component.html',
  styleUrls: ['./gestione-dichiarazione-consumi.component.scss']
})
export class GestioneDichiarazioneConsumiComponent implements OnInit {

  tipoIntestazioneEnum: typeof TipoIntestazioneUma;

  constructor() { }

  ngOnInit() {
    this.tipoIntestazioneEnum = TipoIntestazioneUma;
  }

}
