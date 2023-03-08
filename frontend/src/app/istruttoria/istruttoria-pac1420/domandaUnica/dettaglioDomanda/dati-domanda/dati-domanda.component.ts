import { Component, OnInit } from '@angular/core';
import { Controllo } from '../../domain/controllo';
import { KeyValue } from 'src/app/a4g-common/classi/KeyValue';
import { ActivatedRoute } from '@angular/router';
import { IstruttoriaService } from '../../istruttoria.service';
import { SharedService } from '../../shared.service';
import { DomandaIstruttoriaDettaglio } from '../../domain/domandaIstruttoriaDettaglio';
import { DomandaIstruttoriacorrente } from '../domandaIstruttoriacorrente';
import { TreeNode } from 'primeng/api';

@Component({
  selector: 'app-dati-domanda',
  templateUrl: './dati-domanda.component.html',
  styleUrls: ['./dati-domanda.component.css']
})
export class DatiDomandaComponent implements OnInit {
  cols: any[];
  public controlli: Array<Controllo>;
  public tabelle: Array<Controllo>;
  public tipiControllo: Array<Controllo>;
  domandaCorrenteDettaglio: DomandaIstruttoriaDettaglio;
  sintesi = 'SINTESI';

  dataSintesi: TreeNode[];

  private _serviceSubscription;

  constructor(private route: ActivatedRoute,
    private sharedService: SharedService, private domandaIstruttoriacorrente: DomandaIstruttoriacorrente) {
    this._serviceSubscription = this.sharedService.domandSettata.subscribe(
      (data: any) => {
        this.caricaDatiDettaglio();
      });
  }

  ngOnInit() {
    this.cols = [
      // { field: 'a', header: 'a', width: '20%' },
      { field: 'controlloDescrizione', header: 'b', width: '70%' },
      { field: 'valore', header: 'ss', width: '30%' }];

    if (this.domandaIstruttoriacorrente.domanda != null && this.domandaCorrenteDettaglio == null) {
      this.caricaDatiDettaglio();
    }
  }

  private controlloToTreeNode(pcontrollo: Controllo, pchildren: Array<TreeNode>): TreeNode {
    if (pchildren) {
      pcontrollo.bold = 1;
    } else {
      pcontrollo.bold = 0;
    }

    return {
      label: pcontrollo.controllo.toString(),
      data: pcontrollo,
      children: pchildren
    };
  }

  private caricaDatiDettaglio() {
    this.domandaCorrenteDettaglio = this.domandaIstruttoriacorrente.domanda;
    this.controlli = this.domandaCorrenteDettaglio.datiDomanda
      .sort((a, b) => (a.ordineControllo < b.ordineControllo) ? -1 : 1)
      .sort((a, b) => (a.ordine1 < b.ordine1) ? -1 : 1);
    const tipiControlloTmp: Array<Controllo> = this.controlli.filter(x => x.codice1 !== this.sintesi).map(x => {
      let newControllo = new Controllo();
      newControllo.codice1 = x.codice1;
      newControllo.descrizione1 = x.descrizione1;
      newControllo.bold = 0;
      return newControllo;
    });
    
    this.tipiControllo = this.mapDistinct(tipiControlloTmp);
    const datiTmp = this.controlli.map(x => {
      let newControllo = new Controllo();
      newControllo.codice1 = x.codice1;
      newControllo.descrizione1 = x.descrizione1;
      newControllo.bold = 0;
      return newControllo;
    });
    this.tabelle = this.mapDistinct(datiTmp);
    const data: any[] = this.controlli.filter(x => x.codice1 === this.sintesi && x.codice2 === x.controllo).map(x =>
      this.controlloToTreeNode(x, this.controlli
        .filter(y => y.codice1 === this.sintesi && y.codice2 === x.controllo && y.controllo !== x.controllo)
        .map(z => this.controlloToTreeNode(z, null))));
    this.dataSintesi = data;
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
