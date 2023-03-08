import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SostegnoDu } from '../../classi/SostegnoDu';
import { ContainerElencoIstruttorieComponent } from '../../sostegno-shared/container-elenco-istruttorie.component';

@Component({
  selector: 'app-container-zootecnia',
  templateUrl: '../../sostegno-shared/container-elenco-istruttorie.component.html',
  styleUrls: ['../../sostegno-shared/container-elenco-istruttorie.component.scss']
})
export class ContainerZootecniaComponent extends ContainerElencoIstruttorieComponent {

  constructor(activatedRoute: ActivatedRoute) {
    super(activatedRoute, SostegnoDu.ZOOTECNIA);
    this.tipoIstruttoria = 'saldo';
  }
}
