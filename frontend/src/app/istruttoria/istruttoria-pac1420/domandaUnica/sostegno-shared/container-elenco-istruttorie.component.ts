import { MenuItem } from 'primeng/api';
import { ActivatedRoute } from '@angular/router';
import { Labels } from 'src/app/app.labels';
import { Costanti } from '../Costanti';
import { SostegnoDu } from '../classi/SostegnoDu';

export abstract class ContainerElencoIstruttorieComponent {

  public menu: Array<MenuItem>;
  public annoCampagna: string;
  public tipoIstruttoria: string;
  
  constructor(
    public activatedRoute: ActivatedRoute,
    public sostegno: SostegnoDu) {
    this.annoCampagna = this.activatedRoute.snapshot.paramMap.get('annoCampagna');
    this.tipoIstruttoria = this.activatedRoute.snapshot.paramMap.get('tipo');
    this.menu = new Array<MenuItem>(
      {
        routerLink: Costanti.PREMIO,
        label: Labels.CALCOLO_PREMIO
      },
      {
        routerLink: Costanti.LIQUIDABILITA,
        label: Labels.CONTROLLI_LIQUIDABILITA
      },
      {
        routerLink: Costanti.LIQUIDAZIONE,
        label: Labels.LIQUIDAZIONE
      }
    );
  }
}
