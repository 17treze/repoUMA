import { Component, OnInit } from '@angular/core';
import { TreeNode } from 'primeng/api';
import { IstruttoriaDomandaUnica } from '../../../classi/IstruttoriaDomandaUnica';
import { ActivatedRoute } from '@angular/router';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';
import { Controllo } from '../../../domain/controllo';

@Component({
  selector: 'app-dati-domanda',
  templateUrl: './dati-domanda.component.html',
  styleUrls: ['./dati-domanda.component.css']
})
export class DatiDomandaComponent implements OnInit {

  public dataSintesi: TreeNode[];
  public cols: any[];
  public controlli: Array<Controllo>;
  public tipiControllo: any;
  public tabelle: Array<Controllo>;
  istruttoriaDUCorrente: IstruttoriaDomandaUnica;
  sintesi = 'SINTESI';
  
  constructor(
    private route: ActivatedRoute, 
    private istruttoriaService: IstruttoriaDettaglioService
  ) { }

  ngOnInit() {
    this.cols = [
      // { field: 'a', header: 'a', width: '20%' },
      { field: 'controlloDescrizione', header: 'b', width: '70%' },
      { field: 'valore', header: 'ss', width: '30%' }
    ];
    this.istruttoriaDUCorrente = this.route.snapshot.data['domandaIstruttoria'];
    this.caricaDatiDettaglio();
  }

  private caricaDatiDettaglio() {

    this.istruttoriaService
      .getDatiDomandaIstruttoriaDU(this.istruttoriaDUCorrente.id.toString())
      .subscribe(resp => {
        if (!resp) return;
        this.controlli = resp
          .sort((a, b) => (a.ordineControllo < b.ordineControllo) ? -1 : 1)
          .sort((a, b) => (a.ordine1 < b.ordine1) ? -1 : 1);
        const tipiControlloTmp: Array<Controllo> = this.controlli.filter(x => x.codice1 !== this.sintesi).map(x => {
          let newControllo = new Controllo();
          newControllo.codice1 = x.codice1;
          newControllo.descrizione1 = x.descrizione1;
          newControllo.bold = 0;
          newControllo.ordineControllo = 0;
          return newControllo;
        });
    
        this.tipiControllo = this.mapDistinct(tipiControlloTmp);
    
        const datiTmp = this.controlli
          .map(x => {
            let newControllo = new Controllo();
            newControllo.codice2 = x.codice2;
            newControllo.descrizione2 = x.descrizione2;
            newControllo.codice1 = x.codice1;
            newControllo.descrizione1 = x.descrizione1;
            newControllo.bold = 0;
            newControllo.ordineControllo = 0;
            return newControllo;
          });
    
        this.tabelle = this.mapDistinct(datiTmp);
    
        const data: any[] = this.controlli.filter(x => x.codice1 === this.sintesi && x.codice2 === x.controllo).
          map(x => this.controlloToTreeNode(x,
            this.controlli.filter(y => y.codice1 === this.sintesi && y.codice2 === x.controllo && y.controllo !== x.controllo)
              .map(z => this.controlloToTreeNode(z, null))));
    
        this.dataSintesi = data;
    });
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
    return def;
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

}
