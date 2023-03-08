import { Component, OnInit } from '@angular/core';
import { TipoIntestazioneUma } from 'src/app/a4g-common/classi/enums/uma/TipoDocumentoUma.enum';
@Component({
  selector: 'app-gestione-domanda-uma',
  templateUrl: './gestione-domanda-uma.component.html',
  styleUrls: ['./gestione-domanda-uma.component.scss']
})
export class GestioneDomandaUmaComponent implements OnInit {

  tipoIntestazioneEnum: typeof TipoIntestazioneUma;

  constructor(
  ) { }

  ngOnInit() {
    this.tipoIntestazioneEnum = TipoIntestazioneUma;
  }
}
