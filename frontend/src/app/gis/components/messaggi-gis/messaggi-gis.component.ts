import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { RichiestaModificaSuoloService } from './../../services/richiesta-modifica-suolo.service';
import { GisCostants } from './../../shared/gis.constants';
import { ToastGisComponent } from './../toast-gis/toast-gis.component';
import * as moment from 'moment';
import { Utente } from '../../../auth/user';

@Component({
  selector: 'messaggi-gis',
  templateUrl: './messaggi-gis.component.html',
  styleUrls: ['./messaggi-gis.component.css']
})
export class MessaggiGisComponent implements OnInit {
  @Input() detailResults: any;
  @Input() showDetail: boolean;
  @Input() utenteCollegato: string;
  @Input() profiloUtente: any;
  @Input() context: string;
  @Input() idDichiarato: any;
  @Output() public loadPoligoniDichiarati = new EventEmitter();

  utenteLoggato: Utente = JSON.parse(sessionStorage.getItem('user'));
  listaMessaggi: any;
  private numeroElementiPagina: number;
  private noOfItemsToShowInitially: number;
  public itemsToShow: any;
  public isFullListDisplayed = false;
  private itemsToLoad: number;
  formParams: any;
  countMessaggi: number;
  response: any;
  actived: boolean; // variabile che gestisce il pannello di inserimento messaggio
  testoMsg: string;
  constructor(private richiestaModificaSuoloService: RichiestaModificaSuoloService, private gisCostants: GisCostants,
    private toastComponent: ToastGisComponent) {
    this.numeroElementiPagina = this.gisCostants.numMessaggi;
    this.noOfItemsToShowInitially = this.numeroElementiPagina;
    this.itemsToLoad = this.gisCostants.numMessaggi;
  }

  ngOnInit() {
  }

  ngOnChanges() {
  setTimeout(() => {
      if (this.showDetail) {
        this.formParams = {};
        this.formParams.pagina = 0;
        this.actived = false;
        this.noOfItemsToShowInitially = this.numeroElementiPagina;
        this.loadMessaggi(this.formParams, null, null);
      }
  });
  }

  loadMessaggi(val, listaOld, from) {
    const messageBody = document.querySelector('.msg');
    const ordine = 'DESC';
    const params = {'pagina': val.pagina, 'numeroElementiPagina': this.numeroElementiPagina };

    /* AS 30/03: Rimosso perchè mi sembra inutile e impedisce il corretto caricamento
                 dei messaggi dopo inserimento se richiesta aperta da lavorazione*/
    /*if (from === 'fromInsert') {
      params.numeroElementiPagina = val.numeroElementiPagina;
      params.pagina = 0;
      ordine = 'ASC';
    }*/

    let getMessaggiMethod = null;
    let id = null;
    if (this.context === 'dettaglio') {
      id = this.idDichiarato;
      getMessaggiMethod = this.richiestaModificaSuoloService.getMessaggiDichiarati(id);
    } else {
      id = this.detailResults.id;
      getMessaggiMethod = this.richiestaModificaSuoloService.getMessaggi(params, id, ordine);
    }

    if (this.detailResults) {
      getMessaggiMethod.then(res => {
        if (this.context === 'dettaglio') {
          this.listaMessaggi = res;
          this.countMessaggi =  res ? res.length : 0;
        } else {
          this.listaMessaggi = res ? res['risultati'] : [];
          this.countMessaggi = res ? res['count'] : 0;
        }
        if (from !== 'fromScroll' || from === 'fromInsert') {
          setTimeout(() => {
            messageBody.scrollTop = messageBody.scrollHeight - messageBody.clientHeight;
          });
          // ASC
          if (this.listaMessaggi) {
            this.listaMessaggi.sort((a, b) => (a.dataInserimento < b.dataInserimento ? -1 : 1));
          }
        } else {
          if (this.listaMessaggi) {
            this.listaMessaggi = this.listaMessaggi.concat(listaOld);
            this.listaMessaggi = this.listaMessaggi.slice(0, this.noOfItemsToShowInitially);
            // ASC
            this.listaMessaggi.sort((a, b) => (a.dataInserimento < b.dataInserimento ? -1 : 1));
          }
        }
        // Truncate text per formattazione testo html
        if (this.listaMessaggi) {
          for (let i = 0; i < this.listaMessaggi.length; i++) {
            const values =  this.listaMessaggi[i].testo.split(' ');
            for (let index = 0; index < values.length; index++) {
            this.testoMsg = values[index];
            if ( this.testoMsg.length >= 26) {
              const txtToReplace = values[index];
              this.testoMsg = this.testoMsg.replace(/\W/gi, '').replace(/(.{26})/g, '$1 ');
              this.listaMessaggi[i].testo = this.listaMessaggi[i].testo.replace(txtToReplace, this.testoMsg);
            }
            }
          }
        }
      });
    }
  }

  onUp(ev) {
    if (this.noOfItemsToShowInitially <= this.countMessaggi && this.context !== 'dettaglio') {
      this.noOfItemsToShowInitially += this.itemsToLoad;
      this.itemsToShow = this.listaMessaggi.slice(0, this.noOfItemsToShowInitially);
      console.log('scrolled up!');
      this.formParams.pagina += 1;
      this.loadMessaggi(this.formParams, this.listaMessaggi, 'fromScroll');
    } else {
        this.isFullListDisplayed = true;
    }
  }

  insertMsg(text) {
    const dataInserimento = moment().format('YYYY-MM-DDTHH:mm:ss.SSS');
    const idRichiesta = this.detailResults.id;
    let body = [];
    const params = {'pagina': 0, 'numeroElementiPagina': 1 };

    let insertMessaggioMethod = null;
    let getMessaggiMethod = null;
    let id = null;
    if (this.context === 'dettaglio') {
      id = this.idDichiarato;
      body = [{
        'id': '',
        'utente':  this.profiloUtente.profilo.toUpperCase(),
        'profiloUtente': this.profiloUtente.profilo.toUpperCase(),
        'dataInserimento': dataInserimento,
        'testo': text,
        'idPoligonoDichiarato': id
        }];
      insertMessaggioMethod = this.richiestaModificaSuoloService.insertMessaggioDichiarati(body, id);
      getMessaggiMethod = this.richiestaModificaSuoloService.getMessaggiDichiarati(id, );

    } else {
      id = this.detailResults.id;
      body = [{
          'profiloUtente': this.profiloUtente.profilo.toUpperCase(),
          'dataInserimento': dataInserimento,
          'testo': text
      }];

      const params = {'pagina': 0, 'numeroElementiPagina': 1 };
      insertMessaggioMethod = this.richiestaModificaSuoloService.insertMessaggio(body, id);
      getMessaggiMethod = this.richiestaModificaSuoloService.getMessaggi(params, id, 'ASC');
    }

    getMessaggiMethod.then(res => {
      let countMsg = null;
      if (this.context === 'dettaglio') {
        this.listaMessaggi = res;
        countMsg = res ? res.length : 0;
      } else {
        this.listaMessaggi = res ? res['risultati'] : [];
        countMsg = res ? res['count'] : 0;
      }
      insertMessaggioMethod.subscribe((respone: any) => {
        this.response = respone;
        if (this.response['status'] === 200 || this.response['status'] === 201 || this.response['status'] === 204) {
          this.toastComponent.showSuccess();
          this.formParams.pagina = 0;
          this.formParams.numeroElementiPagina = countMsg + 1;
          setTimeout(() => {
            this.loadMessaggi(this.formParams, this.listaMessaggi, 'fromInsert');
          });
          this.isFullListDisplayed = true;
          this.noOfItemsToShowInitially = countMsg + 1;
          this.ricaricaDati();
        } else {
          this.toastComponent.showError();
        }
      });
    });
  }

  ricaricaDati() {
    this.loadPoligoniDichiarati.emit();
  }
}
