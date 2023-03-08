import { Component, OnInit, Injectable } from '@angular/core';
import { DatiIstruttoria } from '../../domain/datiIstruttoria';
import { IstruttoriaService } from '../../istruttoria.service';
import { MessageService } from 'primeng/api';
import { ActivatedRoute } from '@angular/router';
import { DomandaIstruttoriaDettaglio } from '../../domain/domandaIstruttoriaDettaglio';
import { DomandaIstruttoriacorrente } from '../domandaIstruttoriacorrente';
import { SharedService } from '../../shared.service';
import { DichiarazioneDu } from '../../domain/dichiarazioneDu';

@Component({
  selector: 'app-dichiarazioni',
  templateUrl: './dichiarazioni.component.html',
  styleUrls: ['./dichiarazioni.component.css']
})
export class DichiarazioniComponent implements OnInit {
  dichiarazioni: Array<DichiarazioneDu>;
  idDomanda: number;
  domandaCorrenteDettaglio: DomandaIstruttoriaDettaglio;
  codiciTipo: String[];
  cols: any[];

  private _serviceSubscription;

  constructor(private route: ActivatedRoute, private istruttoriaService: IstruttoriaService,
    private messageService: MessageService, private domandaCorrente: DomandaIstruttoriacorrente,
    private sharedService: SharedService, private domandaIstruttoriacorrente: DomandaIstruttoriacorrente) {
    this._serviceSubscription = this.sharedService.domandSettata.subscribe(
      (data: any) => {
        this.caricaDatiDettaglio();
      });
  }

  ngOnInit() {
    this.cols = [
      { field: 'a', header: '', width: '90%' },
      { field: 'b', header: '', width: '10%' }];

    if (this.domandaIstruttoriacorrente.domanda != null && this.domandaCorrenteDettaglio == null) {
      this.caricaDatiDettaglio();
    }
  }

  private caricaDatiDettaglio() {
    this.domandaCorrenteDettaglio = this.domandaIstruttoriacorrente.domanda;
    this.dichiarazioni = this.domandaCorrenteDettaglio.dichiarazioni;
    this.codiciTipo = this.dichiarazioni.map(x => x.quadro).filter((v, i, a) => a.indexOf(v) === i);
  }

  public consume() {
    this.cleanup();
  }

  private cleanup() {
    this._serviceSubscription.unsubscribe();
  }
}
