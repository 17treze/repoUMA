import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DichiarazioneDu } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/domain/dichiarazioneDu';
import { IstruttoriaDomandaUnica } from '../../../classi/IstruttoriaDomandaUnica';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';

@Component({
  selector: 'app-dichiarazioni-acc',
  templateUrl: './dichiarazioni-acc.component.html',
  styleUrls: ['./dichiarazioni-acc.component.css']
})
export class DichiarazioniAccComponent implements OnInit {

  dichiarazioni: Array<DichiarazioneDu>;
  codiciTipo: String[];
  selectedSostegno: string;
  tipoSostegno: string;
  istruttoriaDUCorrente: IstruttoriaDomandaUnica;

  constructor(
    private route: ActivatedRoute,
    private istruttoriaDettaglioService: IstruttoriaDettaglioService,
  ) { }

  ngOnInit() {
    this.istruttoriaDUCorrente = this.route.snapshot.data['domandaIstruttoria'];
    this.caricaDichiarazioni();
  }

  private caricaDichiarazioni() {
    this.istruttoriaDettaglioService.getDichiarazioniAcs(this.istruttoriaDUCorrente.domanda.id.toString())
      .subscribe(
        (dichiarazioni) => {
          this.dichiarazioni = dichiarazioni;
          this.codiciTipo = this.dichiarazioni.map(x => x.quadro).filter((v, i, a) => a.indexOf(v) === i);
        }
      );
  }

}
