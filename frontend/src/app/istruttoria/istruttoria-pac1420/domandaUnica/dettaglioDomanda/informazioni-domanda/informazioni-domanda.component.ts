import { Component, OnInit } from '@angular/core';
import { DomandaIstruttoriaRiepilogo } from '../../domain/domandaIstruttoriaRiepilogo';
import { KeyValue } from 'src/app/a4g-common/classi/KeyValue';
import { IstruttoriaService } from '../../istruttoria.service';
import { DomandaIstruttoriaDettaglio } from '../../domain/domandaIstruttoriaDettaglio';
import { DomandaIstruttoriacorrente } from '../domandaIstruttoriacorrente';
import { MessageService } from 'primeng/api';
import { SharedService } from '../../shared.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-informazioni-domanda',
  templateUrl: './informazioni-domanda.component.html',
  styleUrls: ['./informazioni-domanda.component.css']
})
export class InformazioniDomandaComponent implements OnInit {
  dati: Array<KeyValue> = new Array<KeyValue>();
  cols: any[];
  idDomandaCorrente: number;
  idDomanda: number;
  domandaCorrenteDettaglio: DomandaIstruttoriaDettaglio;

  private _serviceSubscription;

  constructor(private route: ActivatedRoute, private istruttoriaService: IstruttoriaService,
    private messageService: MessageService, private domandaIstruttoriacorrente: DomandaIstruttoriacorrente,
    private sharedService: SharedService) {
    this._serviceSubscription = this.sharedService.domandSettata.subscribe(
      (data: any) => {
        console.log('sharedService subscribe');
        console.log(data);
        this.domandaCorrenteDettaglio = data;
        this.dati = this.domandaCorrenteDettaglio.informazioniDomanda;
      });
  }

  ngOnInit() {
    if (this.domandaIstruttoriacorrente.domanda != null && this.domandaCorrenteDettaglio == null) {
      this.domandaCorrenteDettaglio = this.domandaIstruttoriacorrente.domanda;
      this.dati = this.domandaCorrenteDettaglio.informazioniDomanda;
    }

    this.cols = [
      { field: 'a', header: '', width: '80%' },
      { field: 'b', header: '', width: '20%' }];
  }

  public consume() {
    this.cleanup();
  }

  private cleanup() {
    this._serviceSubscription.unsubscribe();
  }
}
