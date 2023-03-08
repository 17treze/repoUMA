import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TreeNode } from 'primeng/api';
import { Istruttoria } from 'src/app/a4g-common/classi/Istruttoria';
import { Configuration } from 'src/app/app.constants';
import { StatoDomandaEnum } from '../dettaglio-istruttoria/statoDomanda';
import { IstruttoriaService } from '../istruttoria.service';
import { IstruttoriaCorrente } from '../istruttoriaCorrente';
import { StatiLavorazione } from './statiLavorazione';
import { StatoIstruttoriaEnum } from './StatoIstruttoriaEnum';


@Component({
  selector: 'app-cruscotto-sostegno',
  templateUrl: './cruscotto-sostegno.component.html',
  styleUrls: ['./cruscotto-sostegno.component.css']
})
export class CruscottoSostegnoComponent implements OnInit {

  listaStatiLavorazione: Array<StatiLavorazione>;
  public subIstruttoria;
  files: TreeNode[];
  cols: any[];
  domandeDisaccoppiato: number;
  a: number;
  stato: string;
  numeroDomandeGiovaneAgricoltore: number;
  checked = false;

  constructor(private istruttoriaCorrente: IstruttoriaCorrente,
    private route: ActivatedRoute, private istruttoriaService: IstruttoriaService,
    private router: Router, private conf: Configuration) {
  }

  ngOnInit() {


    this.listaStatiLavorazione = new Array<StatiLavorazione>();
    this.listaStatiLavorazione.push(new StatiLavorazione(1, StatoIstruttoriaEnum.RICHIESTO, 0));

    this.listaStatiLavorazione.sort((a, b): number => {
      if (a.order > b.order) { return 1; }
      if (a.order < b.order) { return -1; }
      return 0;
    });

    this.subIstruttoria = this.route
      .params
      .subscribe(params => {
        this.getIstruttoria(params['idIstruttoria']);
      });

    this.cols = [
      { field: 'sostegno', header: 'SOSTEGNO' },
      { field: 'interventi', header: 'INTERVENTI' },
      { field: 'numeroDomande', header: 'NUMERO DOMANDE' }
    ];
    this.numeroDomandeGiovaneAgricoltore = 0;
  }

  getIstruttoria(idIstruttoria: number): void {
    this.istruttoriaService.getIstruttoria(idIstruttoria)
      .subscribe((next) => this.istruttoriaCorrente.istruttoria = next);
    this.countDomandeGiovaneAgricoltore(idIstruttoria);
    this.countDomandeSostegno(idIstruttoria);
    this.countStatoLavorazione();
  }

  getIstruttoriaCorrente(): Istruttoria {
    return this.istruttoriaCorrente.istruttoria;
  }

  addDomandePerSostegno(numeroDomande: number) {
    this.listaStatiLavorazione.map(a => { a.counter = numeroDomande; });
  }

  countStatoLavorazione() {
    const baseRequest = '{"sostegno": "DISACCOPPIATO" , "statoSostegno":"RICHIESTO"}';
    const requestJSON = encodeURIComponent(baseRequest);
    this.istruttoriaService.countDomandeFiltered(requestJSON).subscribe((next) => this.addDomandePerSostegno(next));
  }

  countDomandeSostegno(idIstruttoria: number): void {
    const richiestaSostegno = 'richiestaDisaccoppiato';
    const baseRequest = '{"idDatiSettore": ' + idIstruttoria +
      ',"statoDomanda": "' + StatoDomandaEnum.IN_ISTRUTTORIA + '","sostegno": "' + richiestaSostegno + '"}';
    const requestJSON = encodeURIComponent(baseRequest);
    this.istruttoriaService.countDomandeFiltered(requestJSON)
    .subscribe((next) => this.initTreeNode(next, this.numeroDomandeGiovaneAgricoltore));

  }

  countDomandeGiovaneAgricoltore(idDatiSettore: number) {
    const baseRequest = '{"dichiarazione": "GIOVANE_AGRICOLTORE", "idDatiSettore": ' + idDatiSettore + '}';
    const requestJSON = encodeURIComponent(baseRequest);
    this.istruttoriaService.countDomandeFiltered(requestJSON)
    .subscribe((res) => this.numeroDomandeGiovaneAgricoltore = res);
  }

  initTreeNode(countDisaccoppiato: number , countDomandeGiovaneAgricoltore: number) {
    this.files = [{

      data: {
        sostegno: 'DISACCOPPIATO',
        interventi: '',
        numeroDomande: countDisaccoppiato
      },
      children: [
        {
           data: {
            sostegno: '',
            interventi: 'REGIME PAGAMENTO BASE',
            numeroDomande: countDisaccoppiato
          }
        },
        {
          data: {
           sostegno: '',
           interventi: 'PAGAMENTO GREENING',
           numeroDomande: countDisaccoppiato
         }
       },
       {
        data: {
         sostegno: '',
         interventi: 'PAGAMENTO GIOVANE AGRICOLTORE',
         numeroDomande: countDomandeGiovaneAgricoltore
       }
     }
    ]
  }
  ];
  }


}
