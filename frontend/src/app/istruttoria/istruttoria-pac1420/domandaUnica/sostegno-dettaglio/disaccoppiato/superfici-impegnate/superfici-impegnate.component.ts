import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { PaginaSuperfici, PaginaRichiestaSuperficie, RichiestaSuperficie } from '../../../domain/richiestaSuperficie';
import { Paginazione } from 'src/app/a4g-common/utility/paginazione';

@Component({
  selector: 'app-superfici-impegnate',
  templateUrl: './superfici-impegnate.component.html',
  styleUrls: ['./superfici-impegnate.component.css']
})
export class SuperficiImpegnateComponent implements OnInit {

  cols: any[];
  idDomanda: number;
  listaParticelleTable: PaginaSuperfici = new PaginaSuperfici();
  numeroPagina = 1;
  elementiPerPagina = 10;
  paginazione: Paginazione;
  first: boolean = true;

  @ViewChild('tableSup', { static: true }) tableSup;

  constructor(
    private route: ActivatedRoute,
    private istruttoriaService: IstruttoriaDettaglioService,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.initializeObject();
    this.setCols();
    this.getIdDomanda();
    this.getDettaglio(this.tableSup);
  }

  private initializeObject() {
    this.listaParticelleTable = new PaginaSuperfici();
    this.listaParticelleTable.paginaSuperfici = new PaginaRichiestaSuperficie();
    this.listaParticelleTable.paginaSuperfici.risultati = new Array<RichiestaSuperficie>();
  }

  private setCols() {
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
      { field: 'supRichiestaNetta', header: 'Sup Imp. Netta' }
    ];
  }

  private getIdDomanda() {
    this.idDomanda = this.route.snapshot.data['domandaIstruttoria'].domanda.id;
  }

  private getDettaglio(event) {
    if (this.idDomanda) {
      this.numeroPagina = Math.floor(event.first / this.elementiPerPagina);
      if (event != null && event.sortField != null)
        this.paginazione = Paginazione.of(this.numeroPagina, this.elementiPerPagina, event.sortField, this.istruttoriaService.getOrdine(event.sortOrder));
      else
        this.paginazione = Paginazione.of(this.numeroPagina, this.elementiPerPagina, 'id', 'ASC');
      this.istruttoriaService.getSuperficiImpegnateDU(this.idDomanda.toString(), this.paginazione)
        .subscribe(
          resp => {
            if (resp) {
              this.listaParticelleTable = resp;
            }
          },
          err => {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
          }
        )
    }
  }

  changePage(event) {
    if (this.first) {
      this.first = false;
    } else {
      this.getDettaglio(event);
    }
  }

}
