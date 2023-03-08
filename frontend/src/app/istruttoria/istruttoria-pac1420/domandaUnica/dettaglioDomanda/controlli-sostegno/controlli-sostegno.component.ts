import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IstruttoriaService } from '../../istruttoria.service';
import { Controllo } from '../../domain/controllo';
import { DomandaIstruttoriaDettaglio } from '../../domain/domandaIstruttoriaDettaglio';
import { DomandaIstruttoriacorrente } from '../domandaIstruttoriacorrente';
import { SharedService } from '../../shared.service';

@Component({
  selector: 'app-controlli-sostegno',
  templateUrl: './controlli-sostegno.component.html',
  styleUrls: ['./controlli-sostegno.component.css']
})
export class ControlliSostegnoComponent implements OnInit {
  public cols: any[];
  public controlli: Array<Controllo>;
  public passiLavorazione: Array<Controllo>;
  public tipiControllo: Array<Controllo>;
  domandaCorrenteDettaglio: DomandaIstruttoriaDettaglio;

  private _serviceSubscription;

  constructor(private route: ActivatedRoute, private istruttoriaService: IstruttoriaService,
    private domandaCorrente: DomandaIstruttoriacorrente,
    private sharedService: SharedService, private domandaIstruttoriacorrente: DomandaIstruttoriacorrente) {
    this._serviceSubscription = this.sharedService.domandSettata.subscribe(
      (data: any) => {
        this.caricaDatiDettaglio();
      });
  }

  ngOnInit() {
    this.cols = [
      // { field: 'a', header: 'a', width: '20%' },
      { field: 'b', header: '', width: '100%' },
      //{ field: 'c', header: '', width: '20%' }
    ];

    if (this.domandaIstruttoriacorrente.domanda != null && this.domandaCorrenteDettaglio == null) {
      this.caricaDatiDettaglio();
    }
  }

  private caricaDatiDettaglio() {
    this.domandaCorrenteDettaglio = this.domandaIstruttoriacorrente.domanda;
    this.controlli = this.domandaCorrenteDettaglio.controlliSostegno;

    // tslint:disable-next-line:max-line-length
    const passiLavorazioneTmp: Array<Controllo> = this.controlli.map(x => {
      let newControllo = new Controllo();
      newControllo.codice2 = x.codice2;
      newControllo.descrizione2 = x.descrizione2;
      newControllo.codice1 = x.codice1;
      newControllo.descrizione1 = x.descrizione1;
      newControllo.bold = 0;
      return newControllo;
    });

    this.passiLavorazione = this.mapDistinct(passiLavorazioneTmp);

    console.log('this.passiLavorazione ' + this.passiLavorazione.length);

    const tipiControlloTmp: Array<Controllo> = this.controlli.map(x => {
      let newControllo = new Controllo();
      newControllo.codice1 = x.codice1;
      newControllo.descrizione1 = x.descrizione1;
      newControllo.bold = 0;
      return newControllo;
    });

    this.tipiControllo = this.mapDistinct(tipiControlloTmp);

    console.log('this.tipiControllo ' + this.tipiControllo.length);
  }

  private mapDistinct(tmp: Array<Controllo>): Array<Controllo> {
    const def = new Array<Controllo>();
    tmp.forEach(element => {
      let found = false;
      def.forEach(element2 => {
        if (element.codice2 === element2.codice2 &&
          element.codice1 === element2.codice1) {
          found = true;
        }
      });
      if (!found) {
        def.push(element);
      }
    });

    console.log('def ' + def.length);
    console.log(def);
    return def;
  }

  public consume() {
    this.cleanup();
  }

  private cleanup() {
    this._serviceSubscription.unsubscribe();
  }
}
