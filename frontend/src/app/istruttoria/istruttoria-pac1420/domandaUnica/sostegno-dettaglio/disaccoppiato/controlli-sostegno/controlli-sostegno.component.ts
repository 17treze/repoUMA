import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IstruttoriaDomandaUnica } from '../../../classi/IstruttoriaDomandaUnica';
import { Controllo } from '../../../domain/controllo';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';

@Component({
  selector: 'app-controlli-sostegno',
  templateUrl: './controlli-sostegno.component.html',
  styleUrls: ['./controlli-sostegno.component.css']
})
export class ControlliSostegnoComponent implements OnInit {
  cols: any[];
  public controlli: Array<Controllo>;
  public passiLavorazione: Array<Controllo>;
  public tipiControllo: Array<Controllo>;
  public istruttoriaDUCorrente: IstruttoriaDomandaUnica;

  constructor(private route: ActivatedRoute, private istruttoriaService: IstruttoriaDettaglioService) {
  }

  ngOnInit() {
    this.cols = [
      // { field: 'a', header: 'a', width: '20%' },
      { field: 'b', header: '', width: '100%' },
      //{ field: 'c', header: '', width: '20%' }
    ];
    this.istruttoriaDUCorrente = this.route.snapshot.data['domandaIstruttoria'];
    this.caricaDatiDettaglio();
  }

  private caricaDatiDettaglio() {
    //call service
    this.istruttoriaService.getControlliSostegnoIstruttoriaDU(this.istruttoriaDUCorrente.id.toString()).subscribe( resp => {
      if (!resp) return;
      this.controlli = resp;
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
      const tipiControlloTmp: Array<Controllo> = this.controlli.map(x => {
        let newControllo = new Controllo();
        newControllo.codice1 = x.codice1;
        newControllo.descrizione1 = x.descrizione1;
        newControllo.bold = 0;
        return newControllo;
      });
      this.tipiControllo = this.mapDistinct(tipiControlloTmp);
      console.log('this.tipiControllo ' + this.tipiControllo.length);
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

    console.log('def ' + def.length);
    console.log(def);
    return def;
  }
}
