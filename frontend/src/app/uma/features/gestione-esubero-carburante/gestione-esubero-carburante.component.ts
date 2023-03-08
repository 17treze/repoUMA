import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TipoIntestazioneUma } from 'src/app/a4g-common/classi/enums/uma/TipoDocumentoUma.enum';

@Component({
  selector: 'app-gestione-esubero-carburante',
  templateUrl: './gestione-esubero-carburante.component.html',
  styleUrls: ['./gestione-esubero-carburante.component.scss']
})
export class GestioneEsuberoCarburanteComponent implements OnInit {

  tipoIntestazioneEnum: typeof TipoIntestazioneUma;

  constructor(
    private router: Router,
    private activeRoute: ActivatedRoute
  ) { }
  
  ngOnInit() {
    this.tipoIntestazioneEnum = TipoIntestazioneUma;
  }

  onClickTrasferimenti() {
    this.router.navigate(["trasferimenti"], {
      relativeTo: this.activeRoute.parent,
    });
}


  onClickRavvedimentoOperoso() {}
}
