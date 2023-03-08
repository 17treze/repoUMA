import { Component, OnInit, OnDestroy, OnChanges } from '@angular/core';
import { IstruttoriaCorrente } from '../istruttoriaCorrente';
import { Istruttoria } from '../../../../a4g-common/classi/Istruttoria';
import { ActivatedRoute } from '@angular/router';
import { ElencoDomandeService } from '../services/elenco-domande.service';
import { PaginaDomande } from '../domain/paginaDomande';
import { IstruttoriaService } from '../istruttoria.service';
import { StatoDomandaEnum } from '../dettaglio-istruttoria/statoDomanda';

@Component({
  selector: 'app-elenco-domande',
  templateUrl: './elenco-domande.component.html',
  styleUrls: ['./elenco-domande.component.css']
})
export class ElencoDomandeComponent implements OnInit, OnDestroy {

  isAlive = true;
  paginaDomande: PaginaDomande;
  statoDomande: string;
  idIstruttoria: number;
  public subParams;
  public subQueryParams;
  titoloPagina: string;
  elementiPerPagina = 20;
  numeroPagina = 0;


  constructor(private istruttoriaCorrente: IstruttoriaCorrente,
    private route: ActivatedRoute, private elencoDomandeService: ElencoDomandeService, private istruttoriaService: IstruttoriaService) { }

  ngOnInit() {
    console.log('Elenco domande init ');
    this.paginaDomande = new PaginaDomande();

    this.subQueryParams = this.route.queryParams.subscribe(params => {
      this.statoDomande = params['stato'];

      if (this.statoDomande === StatoDomandaEnum.RICEVIBILE) {
        this.titoloPagina = 'Domande Ricevibili';
      } else if (this.statoDomande === StatoDomandaEnum.NON_RICEVIBILE) {
        this.titoloPagina = 'Domande Non Ricevibili';
      } else if (this.statoDomande === StatoDomandaEnum.IN_ISTRUTTORIA) {
        this.titoloPagina = 'Domande In Istruttoria';
      }
    });

    this.subParams = this.route.paramMap.subscribe(params => {
      this.idIstruttoria = Number(params.get('idIstruttoria'));
      console.log('Elenco domande param ' + this.idIstruttoria);
      if (this.getIstruttoriaCorrente() == null) {
        this.istruttoriaCorrente = new IstruttoriaCorrente();
        this.istruttoriaService.getIstruttoria(this.idIstruttoria)
          .subscribe((next) => {
            console.log(next, 'next');
            console.log(this.istruttoriaCorrente, 'istr.');
            this.istruttoriaCorrente.istruttoria = next;
          });
      }
    });
  }

  getIstruttoriaCorrente(): Istruttoria {
    return this.istruttoriaCorrente.istruttoria;
  }

  ngOnDestroy(): void {
    this.isAlive = false;
    this.subParams.unsubscribe();
    this.subQueryParams.unsubscribe();
  }

  changePage(event) {
    this.numeroPagina = Math.floor(event.first / this.elementiPerPagina);
    const jsonParametri = '{ "idDatiSettore": ' + this.idIstruttoria + ', "statoDomanda": "' + this.statoDomande + '"}';
    const jsonPaginazione = '{ "numeroElementiPagina": ' + this.elementiPerPagina + ', "pagina": ' + this.numeroPagina + '}';
    const jsonOrdinamento = '[{"proprieta": "cuaaIntestatario" , "ordine": "ASC"}]';

    this.elencoDomandeService.getPaginaDomandeAttuale(encodeURIComponent(jsonParametri),
      encodeURIComponent(jsonPaginazione), encodeURIComponent(jsonOrdinamento))
      .subscribe((dati => {
        if (dati != null) {
          this.paginaDomande = dati;
        } else {
          this.paginaDomande.risultati = [];
          this.paginaDomande.elementiTotali = 0;
        }
      }));
  }
  toUpperCase() {
    if (this.statoDomande === StatoDomandaEnum.NON_RICEVIBILE) {
      return 'Non Ricevibile';
    }
    if (this.statoDomande === StatoDomandaEnum.RICEVIBILE) {
      return 'Ricevibile';
    }
    if (this.statoDomande === StatoDomandaEnum.IN_ISTRUTTORIA) {
      return 'In Istruttoria';
    }
  }
}
