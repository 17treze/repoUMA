import { Component, OnInit } from '@angular/core';
import { DichiarazioneDu } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/domain/dichiarazioneDu';
import { ActivatedRoute, Router } from '@angular/router';
import { MenuItem, MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { Costanti } from '../../../../../Costanti';
import { SostegnoDu } from '../../../../../classi/SostegnoDu';

@Component({
  selector: 'app-dichiarazioni-acc',
  templateUrl: './dichiarazioni-acc.component.html',
  styleUrls: ['./dichiarazioni-acc.component.css']
})
export class DichiarazioniAccComponent implements OnInit {

  dichiarazioni: Array<DichiarazioneDu>;
  idDomanda: number;
  codiciTipo: String[];
  selectedSostegno: string;
  tipoSostegno: string;

  constructor(
    private istruttoriaService: IstruttoriaService,
    private messageService: MessageService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.setSostegno();
    this.setIdDomandaCorrente();
    this.caricaDichiarazioni();
  }

  private caricaDichiarazioni() {
    this.istruttoriaService.getDettaglioDomandaAccoppiati(this.idDomanda.toString(), this.tipoSostegno, Costanti.tabDichiarazioni)
      .subscribe(
        (datiAccoppiato) => {
          this.dichiarazioni = datiAccoppiato[0].dichiarazioni;
          this.codiciTipo = this.dichiarazioni.map(x => x.quadro).filter((v, i, a) => a.indexOf(v) === i);
        }
      );
  }

  private setIdDomandaCorrente() {
    this.route.params
      .subscribe(params => {
        this.idDomanda = params['idDomanda'];
      });
  }

  private setSostegno() {
    if (this.router.url.split('/').filter(url => url === Costanti.accoppiatoZootecniaRichiesto).length > 0) {
      this.selectedSostegno = SostegnoDu.ZOOTECNIA;
    } else {
      this.selectedSostegno = SostegnoDu.SUPERFICIE;
    }
    if (this.selectedSostegno === SostegnoDu.ZOOTECNIA) {
      this.tipoSostegno = Costanti.acz;
    } else if (this.selectedSostegno === SostegnoDu.SUPERFICIE) {
      this.tipoSostegno = Costanti.acs;
    }
  }
}
