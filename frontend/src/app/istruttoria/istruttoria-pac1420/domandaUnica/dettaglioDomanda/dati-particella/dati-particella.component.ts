import { Component, OnInit, ViewChild } from '@angular/core';
import { PaginaDettaglioParticella, DettaglioParticella } from '../../domain/dettaglioParticella';
import { IstruttoriaService } from '../../istruttoria.service';
import { PaginaDettaglioCalcoloParticella } from '../../domain/dettaglioCalcoloParticella';
import { ActivatedRoute } from '@angular/router';
import { Costanti } from '../../Costanti';

@Component({
  selector: 'app-dati-particella',
  templateUrl: './dati-particella.component.html',
  styleUrls: ['./dati-particella.component.css']
})
export class DatiParticellaComponent implements OnInit {
  idDomanda: String;
  listaDettaglioParticelleTable: PaginaDettaglioParticella = new PaginaDettaglioParticella();
  listaDettaglioParticelleTableGreening: PaginaDettaglioParticella = new PaginaDettaglioParticella();
  listaDettaglioParticelleTableMantenimento: PaginaDettaglioParticella = new PaginaDettaglioParticella();
  cols: any[];
  colsGreening: any[];
  colsMantenimento: any[];
  numeroPagina = 1;
  numeroPaginaGreen = 1;
  elementiPerPagina = 10;
  first = 0;

  @ViewChild('tableEle') tableEle;
  @ViewChild('tableGreening') tableGreening;
  @ViewChild('tableMan') tableMan;

  private _subscription;
  private _subscriptionIstruttoriaService;

  constructor(private istruttoriaService: IstruttoriaService,
    private route: ActivatedRoute) {
  }

  ngOnInit() {
    console.log(this.listaDettaglioParticelleTable);
    this.cols = [
      // { field: 'comune', header: 'Comune Catastale' },
      { field: 'codNazionale', header: 'Cod. Comune' },
      { field: 'foglio', header: 'Foglio' },
      { field: 'particella', header: 'Particella' },
      { field: 'sub', header: 'Sub' },
      { field: 'codiceColtura3', header: 'Cod. Coltura' },
      { field: 'descrizioneColtura', header: 'Descrizione Coltura', sortable: false},
      { field: 'superficieImpegnata', header: 'Sup Imp.' },
      { field: 'superficieEleggibile', header: 'Sup Elegg.' },
      { field: 'superficieSigeco', header: 'Sup Controllo in loco' },
      { field: 'anomalieMantenimento', header: 'Anomalie Man' },
      { field: 'anomalieCoordinamento', header: 'Anomalie Coor' },
      { field: 'superficieDeterminata', header: 'Sup Determinata' },
    ];

    this.colsGreening = [
      // { field: 'comune', header: 'Comune Catastale' },
      { field: 'codNazionale', header: 'Cod. Comune' },
      { field: 'foglio', header: 'Foglio' },
      { field: 'particella', header: 'Particella' },
      { field: 'sub', header: 'Sub' },
      { field: 'codiceColtura3', header: 'Cod. Coltura' },
      { field: 'descrizioneColtura', header: 'Descrizione Coltura',  sortable: false },
      { field: 'superficieDeterminata', header: 'Sup Determinata' },
      { field: 'tipoColtura', header: 'Tipo di Coltura' },
      { field: 'tipoSeminativo', header: 'Tipo di Seminativo' },
      { field: 'colturaPrincipale', header: 'Coltura Principale' },
      { field: 'secondaColtura', header: 'Seconda Coltura'},
      { field: 'azotoFissatrice', header: 'Azoto Fissatrice'}
    ];

    this.colsMantenimento = [
      // { field: 'comune', header: 'Comune Catastale' },
      { field: 'codNazionale', header: 'Cod. Comune' },
      { field: 'foglio', header: 'Foglio' },
      { field: 'particella', header: 'Particella' },
      { field: 'sub', header: 'Sub' },
      { field: 'codiceColtura3', header: 'Cod. Coltura' },
      { field: 'descrizioneColtura', header: 'Descrizione Coltura',  sortable: false},
      { field: 'pascolo', header: 'Pascolo' },
      { field: 'superficieImpegnata', header: 'Sup. imp. netta' },
      { field: 'superficieEleggibile', header: 'Sup. eleggibile' },
      { field: 'superficieSigeco', header: 'Sup. controllo in loco' },
      { field: 'anomalieMantenimento', header: 'Anomalie MAN' }
    ];

    this._subscription = this.route.paramMap.subscribe(params => {
      console.log(params);
      this.idDomanda = params.get(Costanti.dettaglioDomandaParam);
      console.log('idDomanda ' + this.idDomanda);
      this.changePage1(this.tableEle, 0);
      this.first = this.first + 1;
    });
  }

  public consume() {
    this.cleanup();
  }

  private cleanup() {
    this._subscriptionIstruttoriaService.unsubscribe();
    this._subscription.unsubscribe();
  }

  changePage(event, update: number) {
    if (this.idDomanda != null) {
      if (this.first === 3) {
        this.changePage1(event, update);
      } else {
        this.first = this.first + 1;
      }
    } else {
      this.first = this.first + 1;
    }
  }

  changePageEle(event) {
    console.log('Recupero dettaglio particella changePageEle');
    this.changePage(event, 1);
  }

  changePageGreen(event) {
    console.log('Recupero dettaglio particella changePageGreen');
    this.changePage(event, 2);
  }
  changePageMantenimento(event) {
    console.log('Recupero dettaglio particella changePageMantenimento');
    this.changePage(event, 3);
  }

  changePage1(event, update: number) {
    let first = 1;
    if (event) {
      first = event.first;
    }
    this.numeroPagina = Math.floor(first / this.elementiPerPagina);
    const jsonPaginazione = '{ "numeroElementiPagina": ' + this.elementiPerPagina + ', "pagina": ' + this.numeroPagina + '}';
    let jsonOrdinamento = '';

    let isPascolo = false;
    if (update === 3) {
      isPascolo = true;
    }

    const jsonParams = '{"identificativoSostegno": "DISACCOPPIATO", "isPascolo": ' + isPascolo + ' }';
    if (event != null && event.sortField != null) {
      console.log(event.sortField);
      // tslint:disable-next-line:max-line-length
      jsonOrdinamento = '[{ "proprieta": "' + event.sortField + '", "ordine": "' + this.istruttoriaService.getOrdine(event.sortOrder) + '"}]';
    }
    console.log('Recupero dettaglio particella');
    this._subscriptionIstruttoriaService = this.istruttoriaService.getPaginaDettaglioParticellaAttuale(this.idDomanda, encodeURIComponent(jsonParams),
      encodeURIComponent(jsonPaginazione), encodeURIComponent(jsonOrdinamento))
      .subscribe((dati => {
        const dettaglioParticelleTable = new PaginaDettaglioParticella();
        if (dati != null) {
          console.log('Dettaglio Particelle trovate');
          const particelle = this.caricaParticelle(dati);
          dettaglioParticelleTable.risultati = particelle;
          dettaglioParticelleTable.elementiTotali = dati.elementiTotali;
        } else {
          dettaglioParticelleTable.risultati = [];
          dettaglioParticelleTable.elementiTotali = 0;
        }
        switch (update) {
          case 0: {
            this.listaDettaglioParticelleTable.elementiTotali = dettaglioParticelleTable.elementiTotali;
            this.listaDettaglioParticelleTable.risultati = dettaglioParticelleTable.risultati;
            this.listaDettaglioParticelleTableGreening.elementiTotali = dettaglioParticelleTable.elementiTotali;
            this.listaDettaglioParticelleTableGreening.risultati = dettaglioParticelleTable.risultati;
            break;
          }
          case 1: {
            this.listaDettaglioParticelleTable.elementiTotali = dettaglioParticelleTable.elementiTotali;
            this.listaDettaglioParticelleTable.risultati = dettaglioParticelleTable.risultati;
            break;
          }
          case 2: {
            this.listaDettaglioParticelleTableGreening.elementiTotali = dettaglioParticelleTable.elementiTotali;
            this.listaDettaglioParticelleTableGreening.risultati = dettaglioParticelleTable.risultati;
            break;
          }
          case 3: {
            this.listaDettaglioParticelleTableMantenimento.elementiTotali = dettaglioParticelleTable.elementiTotali;
            this.listaDettaglioParticelleTableMantenimento.risultati = dettaglioParticelleTable.risultati;
            break;
          }
        }
      }));
  }

  caricaParticelle(res: PaginaDettaglioCalcoloParticella): Array<DettaglioParticella> {
    const particelle = new Array<DettaglioParticella>();
    res.risultati.forEach(element => {
      // tslint:disable-next-line:prefer-const
      let particella = new DettaglioParticella();
      particella.comuneCatastale = element.infoCatastali.comune;
      particella.codNazionale = element.infoCatastali.codNazionale;
      particella.foglio = element.infoCatastali.foglio;
      particella.particella = element.infoCatastali.particella;
      particella.sub = element.infoCatastali.sub;
      particella.codColtura = element.codiceColtura3;
      particella.descrizioneColtura = element.descrizioneColtura;
      particella.supImpegnata = element.variabiliCalcoloParticella.superficieImpegnata;
      particella.supEleggibile = element.variabiliCalcoloParticella.superficieEleggibile;
      particella.supSigeco = element.variabiliCalcoloParticella.superficieSigeco;
      particella.supDeterminata = element.variabiliCalcoloParticella.superficieDeterminata;
      particella.anomalieMantenimento = this.boolToString(element.variabiliCalcoloParticella.anomalieMantenimento);
      particella.anomalieCoordinamento = this.boolToString(element.variabiliCalcoloParticella.anomalieCoordinamento);
      particella.tipoColtura = element.variabiliCalcoloParticella.tipoColtura;
      particella.tipoSeminativo = element.variabiliCalcoloParticella.tipoSeminativo;
      particella.colturaPrincipale = this.boolToString(element.variabiliCalcoloParticella.colturaPrincipale);
      particella.secondaColtura = this.boolToString(element.variabiliCalcoloParticella.secondaColtura);
      particella.azotoFissatrice = this.boolToString(element.variabiliCalcoloParticella.azotoFissatrice);
      particella.pascolo = element.variabiliCalcoloParticella.pascolo;

      particelle.push(particella);
    });
    return particelle;
  }

  boolToString(val: Boolean): string {
    if (val == null) {
      return '';
    }
    if (val === true) {
      return 'SI';
    }
    if (val === false) {
      return 'NO';
    }
  }
}
