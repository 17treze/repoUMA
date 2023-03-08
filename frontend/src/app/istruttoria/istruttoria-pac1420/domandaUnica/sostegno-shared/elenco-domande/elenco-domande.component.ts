import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IstruttoriaService } from '../../istruttoria.service';
import { StatoDomandaEnum } from '../../dettaglio-istruttoria/statoDomanda';
import { Paginazione } from 'src/app/a4g-common/utility/paginazione';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { MessageService } from 'primeng/api';
import { DomandaUnicaFilter } from '../../classi/DomandaUnicaFilter';
import { DomandaUnica } from '../dto/DomandaUnica';

@Component({
  selector: 'app-elenco-domande',
  templateUrl: './elenco-domande.component.html',
  styleUrls: ['./elenco-domande.component.css']
})
export class ElencoDomandeComponent implements OnInit {

  public paginazione: Paginazione = Paginazione.of(0, 10, 'id', 'ASC');
  public paginaDomande: DomandaUnica = new DomandaUnica();
  public title: string;
  public cols: any;
  public domandaUnicaFilter: DomandaUnicaFilter;
  public statoDomanda: string;
  private componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    private route: ActivatedRoute,
    private istruttoriaService: IstruttoriaService,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.setCols();
    this.setTitleAndState();
    this.initDefaultFilter();
  }

  private setCols() {
    this.cols = [
      { field: 'cuaaIntestatario', header: 'CUAA' },
      { field: 'ragioneSociale', header: 'Ragione sociale' },
      { field: 'numeroDomanda', header: 'Numero Domanda' },
      { field: 'descModuloDomanda', header: 'Modulo' },
      { field: 'dtPresentazione', header: 'Data pres.' },
      { field: 'numDomandaRettificata', header: 'Numero Domanda rett.' },
      { field: 'dtProtocollazOriginaria', header: 'Data pres. iniz.' },
      { field: 'descEnteCompilatore', header: 'Ente' }
    ];
  }

  private setTitleAndState() {
    switch (this.route.snapshot.params.statoDomanda) {
      case 'ricevibili': {
        this.title = 'Domande Ricevibili';
        this.statoDomanda = StatoDomandaEnum.RICEVIBILE;
        break;
      }
      case 'nonRicevibili': {
        this.title = 'Domande Non Ricevibili';
        this.statoDomanda = StatoDomandaEnum.NON_RICEVIBILE;
        break;
      }
      case 'inIstruttoria': {
        this.title = 'Domande In Istruttoria';
        this.statoDomanda = StatoDomandaEnum.IN_ISTRUTTORIA;
        break;
      }
    }
  }

  private initDefaultFilter() {
    this.domandaUnicaFilter = new DomandaUnicaFilter();
    this.domandaUnicaFilter.annoCampagna = this.route.snapshot.params.annoCampagna;
    this.domandaUnicaFilter.statoDomanda = this.statoDomanda;
  }

  public loadData(event): void {
    if (event) {
      this.paginazione.pagina = Math.floor(event.first / this.paginazione.numeroElementiPagina);
      if (event.sortField != null) {
        this.paginazione.proprieta = event.sortField;
        this.paginazione.ordine = this.istruttoriaService.getOrdine(event.sortOrder);
      } else {
        this.paginazione.proprieta = "id";
        this.paginazione.ordine = "ASC";
      }
    }
    this.getElencoDomande(this.domandaUnicaFilter, this.paginazione);
  }

  private getElencoDomande(domandaUnicaFilter: DomandaUnicaFilter, paginazione: Paginazione) {
    this.istruttoriaService.getElencoDomande(domandaUnicaFilter, paginazione)
      .pipe(
        takeUntil(this.componentDestroyed$)
      )
      .subscribe(
        result => {
          this.paginaDomande = result;
        },
        error => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        }
      );
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

}
