import { Component, OnInit, ViewChild } from '@angular/core';
import { PaginaParticellaDomanda, ParticellaDomanda } from '../../domain/particellaDomanda';
import { IstruttoriaService } from '../../istruttoria.service';
import { DomandaIstruttoriaDettaglio } from '../../domain/domandaIstruttoriaDettaglio';
import { SharedService } from '../../shared.service';
import { PaginaRichiestaSuperficie } from '../../domain/richiestaSuperficie';
import { DomandaIstruttoriacorrente } from '../domandaIstruttoriacorrente';
import { ActivatedRoute } from '@angular/router';
import { Costanti } from '../../Costanti';


@Component({
  selector: 'app-superfici-impegnate',
  templateUrl: './superfici-impegnate.component.html',
  styleUrls: ['./superfici-impegnate.component.css']
})
export class SuperficiImpegnateComponent implements OnInit {
  domandaCorrenteDettaglio: DomandaIstruttoriaDettaglio;
  listaParticelleTable: PaginaParticellaDomanda = new PaginaParticellaDomanda();
  cols: any[];
  checked: boolean;
  numeroPagina = 1;
  elementiPerPagina = 5;
  first = true;

  @ViewChild('tableSup', { static: true }) tableSup;

  private _serviceSubscription;
  private _subscriptionIstruttoriaService;

  constructor(private istruttoriaService: IstruttoriaService,
    private sharedService: SharedService, private domandaIstruttoriacorrente: DomandaIstruttoriacorrente) {
    this._serviceSubscription = this.sharedService.domandSettata.subscribe(
      (data: any) => {
        this.domandaCorrenteDettaglio = data;
        this.changePage1(this.tableSup);
      });
  }

  ngOnInit() {
    this.cols = [
      { field: 'idParticella', header: 'Id Particella' },
      { field: 'comune', header: 'Comune Catastale' },
      { field: 'codNazionale', header: 'Cod. Nazionale' },
      { field: 'foglio', header: 'Foglio' },
      { field: 'particella', header: 'Particella' },
      { field: 'sub', header: 'Sub' },
      { field: 'codIsola', header: 'Isola' },
      { field: 'codiceColtura3', header: 'Cod. Coltura' },
      { field: 'descrizioneColtura', header: 'Descr. Coltura' },
      { field: 'supRichiesta', header: 'Sup. Imp. Lorda' },
      { field: 'supRichiestaNetta', header: 'Sup Imp. Netta' },
    ];

    if (this.domandaIstruttoriacorrente.domanda != null
      && this.domandaCorrenteDettaglio == null) {
      this.domandaCorrenteDettaglio = this.domandaIstruttoriacorrente.domanda;
      this.changePage1(this.tableSup);
    }
  }

  public consume() {
    this.cleanup();
  }

  private cleanup() {
    this._subscriptionIstruttoriaService.unsubscribe();
    this._serviceSubscription.unsubscribe();
  }

  changePage(event) {
    if (this.domandaCorrenteDettaglio != null) {
      if (this.first) {
        this.first = false;
      } else {
        this.changePage1(event);
      }
    }
  }

  changePage1(event) {
    this.numeroPagina = Math.floor(event.first / this.elementiPerPagina);
    const jsonPaginazione = '{ "numeroElementiPagina": ' + this.elementiPerPagina + ', "pagina": ' + this.numeroPagina + '}';

    let jsonOrdinamento = '';

    if (event != null && event.sortField != null) {
      console.log(event.sortField);
      // tslint:disable-next-line:max-line-length
      jsonOrdinamento = '[{ "proprieta": "' + event.sortField + '", "ordine": "' + this.istruttoriaService.getOrdine(event.sortOrder) + '"}]';
    }

    this._subscriptionIstruttoriaService = this.istruttoriaService.getPaginaParticellaAttuale(this.domandaCorrenteDettaglio.id.toString(),
      encodeURIComponent(jsonPaginazione), encodeURIComponent(jsonOrdinamento))
      .subscribe((dati => {
        if (dati != null) {
          console.log('Particelle trovate');
          this.caricaParticelle(dati);
        } else {
          this.listaParticelleTable.risultati = [];
          this.listaParticelleTable.elementiTotali = 0;
        }
      }));
  }

  caricaParticelle(res: PaginaRichiestaSuperficie) {
    const particelle = new Array<ParticellaDomanda>();

    res.risultati.forEach(element => {
      // tslint:disable-next-line:prefer-const
      let particella = new ParticellaDomanda();
      particella.idParticella = element.infoCatastali.idParticella;
      particella.comuneCatastale = element.infoCatastali.comune;
      particella.codNazionale = element.infoCatastali.codNazionale;
      particella.foglio = element.infoCatastali.foglio;
      particella.particella = element.infoCatastali.particella;
      particella.sub = element.infoCatastali.sub;
      particella.isola = element.riferimentiCartografici.codIsola;
      particella.codColtura = element.codiceColtura3;
      particella.descrColtura = element.infoColtivazione.descrizioneColtura;
      particella.supImpLorda = element.supRichiesta;
      particella.supImpNetta = element.supRichiestaNetta;
      particelle.push(particella);
    });

    this.listaParticelleTable = new PaginaParticellaDomanda();
    this.listaParticelleTable.risultati = particelle;
    this.listaParticelleTable.elementiTotali = res.count;

    console.log(this.listaParticelleTable.risultati);
  }
}
