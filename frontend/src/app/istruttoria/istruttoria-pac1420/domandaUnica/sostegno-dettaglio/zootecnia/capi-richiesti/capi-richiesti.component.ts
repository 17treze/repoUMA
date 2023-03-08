import { Component, OnInit, ViewChild } from '@angular/core';
import { DettaglioCapi, CapoRichiesto } from '../../../domain/dettaglioCapi';
import { Labels } from 'src/app/app.labels';
import { IstruttoriaService } from '../../../istruttoria.service';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';
import { Paginazione, SortDirection } from 'src/app/a4g-common/utility/paginazione';
import { EsitoCapiFilter } from '../../../classi/esitoCapiFilter';
import { DettaglioAllevamenti } from '../../../domain/dettaglioAllevamenti';
import { IstruttoriaDomandaUnica } from '../../../classi/IstruttoriaDomandaUnica';

@Component({
  selector: 'app-capi-richiesti',
  templateUrl: './capi-richiesti.component.html',
  styleUrls: ['./capi-richiesti.component.css']
})
export class CapiRichiestiComponent implements OnInit {

  public allevamenti: DettaglioCapi[] = [];
  public cols: any[] = [];
  public elementiPagina: number = 10;
  public istruttoriaDU: IstruttoriaDomandaUnica;

  private idIstruttoria: string;
  private intestazioni = Labels;
  private sortBy: string;
  private sortDirection: SortDirection;

  @ViewChild('table') table;

  constructor(
    private istruttoriaService: IstruttoriaService,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private istruttoriaDettaglioService: IstruttoriaDettaglioService
  ) { }

  ngOnInit() {
    this.istruttoriaDU = this.route.snapshot.data['domandaIstruttoria'];
    this.setCols();
    this.setIdIstruttoria();
    this.getAllevamentiRichiesti();
  }

  private setCols() {
    this.cols = [
      { field: 'codiceCapo', header: this.intestazioni.marcaAuricolare },
      { field: null, header: this.intestazioni.codiceSpecie },
      { field: null, header: this.intestazioni.razza },
      { field: 'esito', header: this.intestazioni.ESITO_CONTROLLO },
      { field: 'messaggio', header: this.intestazioni.messaggioEsito },
      { field: 'duplicato', header: this.intestazioni.duplicato },
      { field: 'controlloNonSuperato', header: this.intestazioni.controlloNonSuperato }
    ];
  }

  private setIdIstruttoria() {
    this.route.paramMap.subscribe(params => {
      this.idIstruttoria = params.get('idIstruttoria');
    });
  }

  private getAllevamentiRichiesti() {
    this.istruttoriaDettaglioService.getAllevamentiRichiesti(this.idIstruttoria)
      .subscribe(
        dati => {
          if (dati) {
            dati.forEach(element => {
              element.datiAllevamento = JSON.parse(element.datiAllevamento);
              element.datiDetentore = JSON.parse(element.datiDetentore);
              element.datiProprietario = JSON.parse(element.datiProprietario);
              if (element.count>0)
                this.allevamenti.push(element);
            })
            this.allevamenti.sort(function (a, b) {
              return a.codiceIntervento - b.codiceIntervento;
            });
          }
          console.log(this.allevamenti);
        },
        error => {
          console.log(error);
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        }
      );
  }

  public changePage(event: any, allevamento: DettaglioAllevamenti, start: number = 0, codice: any) {
    if (event != null) {
      this.sortDirection = event.sortOrder === 1 ? SortDirection.ASC : SortDirection.DESC;
      this.sortBy = event.sortField || 'id';
      start = event.first;
    }
    let filter = new EsitoCapiFilter();
    filter.idAllevamento = allevamento.id;
    filter.richiesto = true;
    if (codice != null) {
      filter.codiceCapo = codice;
    }
    let paginazione: Paginazione = Paginazione.of(
      Math.round(start / this.elementiPagina), this.elementiPagina, this.sortBy, this.sortDirection || SortDirection.ASC
    );
    this.istruttoriaDettaglioService.ricercaEsiticapi(filter, paginazione)
      .subscribe(
        dati => {
          if (dati) {
            allevamento.count = dati.count;
            allevamento.richiesteAllevamentoDuEsito = dati.risultati;
            allevamento.richiesteAllevamentoDuEsito.forEach(result => {
              result.esito = result.esito.split('_').join(' ');
            })
          }
          console.log(this.allevamenti);
        },
        error => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        }
      );
  }

  public onTabOpen(event, allevamento) {
    allevamento.tableVisible = true;
  }

  public aggiornaCapo(capo: CapoRichiesto) {
    this.istruttoriaService.aggiornaCapo(this.idIstruttoria, capo).subscribe(
      data => {
        capo = data;
      },
      error => {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.salvataggioDatiKo));
      }
    );
  }

  public cerca(codice, allevamento: DettaglioAllevamenti) {
    let event = null;
    let start = 0;
    this.changePage(event, allevamento, start, codice);
  }

}