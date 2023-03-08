import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { of } from 'rxjs';
import { catchError, switchMap, takeWhile } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { DatiDomandaACZ, DatiInterventi, DettagliInterventiACZ, DettaglioDomandaAccoppiati } from '../../../classi/dettaglioDomandaAccoppiati';
import { IstruttoriaDomandaUnica } from '../../../classi/IstruttoriaDomandaUnica';
import { SostegnoDu } from '../../../classi/SostegnoDu';
import { Costanti } from '../../../Costanti';
import { IstruttoriaService } from '../../../istruttoria.service';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';

@Component({
  selector: 'app-esiti-calcoli',
  templateUrl: './esiti-calcoli.component.html',
  styleUrls: ['./esiti-calcoli.component.css']
})
export class EsitiCalcoliComponent implements OnInit, OnDestroy {

  selectedSostegno: string;
  idDomandaCorrente: number;
  tipoSostegno: string;
  cols: any[];
  colsInput: any[];
  colsOutput: any[];
  dettaglioDomandaAccoppiati = new DettaglioDomandaAccoppiati;
  propertyName: string;
  interventi: any[];
  private interventiRichiesti: any[];
  datiDomanda: string;
  istruttoriaDUCorrente: IstruttoriaDomandaUnica;
  vedereAncheSeUgualiAZero: string[] = ['DFFRAPPDIS_INPUT', 'DFFRAPPACZ_INPUT', 'DFFRAPPACS_INPUT', 'DFIMPLIQACZLORDO_OUTPUT'];
  private componentDestroyed = true;

  constructor(
    private route: ActivatedRoute,
    private istruttoriaService: IstruttoriaService,
    private istruttoriaDettaglioService: IstruttoriaDettaglioService,
    private messageService: MessageService,
    private router: Router) { }

  ngOnInit() {
    this.istruttoriaDUCorrente = this.route.snapshot.data['domandaIstruttoria'];
    this.setSostegno();
    this.setIdDomandaCorrente();
    this.setCols();
    this.getDatiIstruttoria();
    console.log(this.dettaglioDomandaAccoppiati);
  }

  private setSostegno() {
    this.selectedSostegno = SostegnoDu.ZOOTECNIA;
    this.tipoSostegno = Costanti.acz;
    this.datiDomanda = 'datiDomandaACZ';
    this.dettaglioDomandaAccoppiati[this.datiDomanda] = new DatiDomandaACZ;
    this.dettaglioDomandaAccoppiati[this.datiDomanda].dettaglioCalcolo = new DettagliInterventiACZ;
    this.interventi = ['int310', 'int311', 'int313', 'int315', 'int320', 'int321', 'int316', 'int318', 'int322'];
    this.interventiRichiesti = [];
  }

  private setIdDomandaCorrente() {
    this.route.params
      .subscribe(params => {
        this.idDomandaCorrente = params['idDomanda'];
      });
  }

  private setCols() {
    this.cols = [
      { field: 'codice', header: 'Descrizione Controllo', width: '70%' },
      { field: 'valore', header: 'Valore', width: '30%' }
    ];
    this.colsInput = [
      { field: 'codice', header: 'DATI IN INGRESSO', width: '70%' },
      { field: 'valore', header: '', width: '30%' }
    ];
    this.colsOutput = [
      { field: 'codice', header: 'DATI IN USCITA', width: '70%' },
      { field: 'valore', header: '', width: '30%' }
    ];
  }

  private getDatiIstruttoria() {
    const datiIstruttoria = Costanti.tabDatiDomanda + ',' + Costanti.datiDisciplinaFinanziaria;
    this.istruttoriaDettaglioService.getEsitiCalcoloIstruttoriaAcz(this.istruttoriaDUCorrente.id.toString()).pipe(
      switchMap((dati) => {
        if (dati) {
          console.log(dati);
          this.dettaglioDomandaAccoppiati.idDomanda = this.istruttoriaDUCorrente.id.toString();
          this.dettaglioDomandaAccoppiati[this.datiDomanda].sintesiCalcolo = [];
          for (const key in dati.sintesiCalcolo) {
            if (dati.sintesiCalcolo.hasOwnProperty(key)) {
              this.dettaglioDomandaAccoppiati[this.datiDomanda].sintesiCalcolo
                .push(
                  { codice: key, valore: dati.sintesiCalcolo[key] }
                );
            }
          }
          this.interventi.forEach(
            (element) => {
              this.dettaglioDomandaAccoppiati[this.datiDomanda].dettaglioCalcolo[element] = [];
              this.dettaglioDomandaAccoppiati[this.datiDomanda].dettaglioCalcolo[element].input = [];
              this.dettaglioDomandaAccoppiati[this.datiDomanda].dettaglioCalcolo[element].output = [];
              for (const key in dati.dettaglioCalcolo[element]) {
                if (key.search('_INPUT') > -1) {
                  this.dettaglioDomandaAccoppiati[this.datiDomanda].dettaglioCalcolo[element].input
                    .push(
                      { codice: key, valore: dati.dettaglioCalcolo[element][key] }
                    );
                } else if (key.search('_OUTPUT') > -1) {
                  this.dettaglioDomandaAccoppiati[this.datiDomanda].dettaglioCalcolo[element].output
                    .push(
                      { codice: key, valore: dati.dettaglioCalcolo[element][key] }
                    );
                }
              }
              if ((this.dettaglioDomandaAccoppiati[this.datiDomanda].dettaglioCalcolo[element].input.length > 0)
                || (this.dettaglioDomandaAccoppiati[this.datiDomanda].dettaglioCalcolo[element].output.length > 0)) {
                this.interventiRichiesti.push(element.replace('int', '_'));
              }
            });
          console.log(this.dettaglioDomandaAccoppiati[this.datiDomanda]);
        }
        return this.istruttoriaDettaglioService.getDisciplinaIstruttoriaAcz(this.istruttoriaDUCorrente.id.toString());
      }),
      catchError((error) => {
        if (error.error.message === 'NO_CALCOLO_ACZ') {
          console.log('Non ci sono passi lavorazione ACZ validi per l\'istruttoria con id: ' + this.istruttoriaDUCorrente.id);
        } else {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
          console.log(error);
        }
        return of(null);
      }),
      takeWhile(() => this.componentDestroyed)
    ).subscribe(
      (datiDisciplinaFinanziaria) => {
        if (datiDisciplinaFinanziaria) {
          this.dettaglioDomandaAccoppiati.datiDisciplinaFinanziaria = new DatiInterventi;
          this.dettaglioDomandaAccoppiati.datiDisciplinaFinanziaria.input = [];
          this.dettaglioDomandaAccoppiati.datiDisciplinaFinanziaria.output = [];
          for (const key in datiDisciplinaFinanziaria) {
            if (key.search('_INPUT') > -1) {
              if (this.vedereAncheSeUgualiAZero.includes(key)) {
                this.dettaglioDomandaAccoppiati.datiDisciplinaFinanziaria.input.push(
                  { codice: key, valore: datiDisciplinaFinanziaria[key] || '0 euro' }
                );
              } else if (this.isInterventoRichiesto(key)) {
                this.dettaglioDomandaAccoppiati.datiDisciplinaFinanziaria.input.push(
                  { codice: key, valore: datiDisciplinaFinanziaria[key] || '0 euro' }
                );
              } else if (datiDisciplinaFinanziaria[key] !== '0 euro') {
                this.dettaglioDomandaAccoppiati.datiDisciplinaFinanziaria.input.push(
                  { codice: key, valore: datiDisciplinaFinanziaria[key] }
                );
              }
            } else if (key.search('_OUTPUT') > -1) {
              if (this.vedereAncheSeUgualiAZero.includes(key)) {
                this.dettaglioDomandaAccoppiati.datiDisciplinaFinanziaria.output.push(
                  { codice: key, valore: datiDisciplinaFinanziaria[key] || '0 euro' }
                );
              } else if (this.isInterventoRichiesto(key)) {
                this.dettaglioDomandaAccoppiati.datiDisciplinaFinanziaria.output.push(
                  { codice: key, valore: datiDisciplinaFinanziaria[key] || '0 euro' }
                );
              } else if (datiDisciplinaFinanziaria[key] !== '0 euro') {
                this.dettaglioDomandaAccoppiati.datiDisciplinaFinanziaria.output.push(
                  { codice: key, valore: datiDisciplinaFinanziaria[key] }
                );
              }
            }
          }
          console.log(this.dettaglioDomandaAccoppiati[this.datiDomanda]);
        }
      },
      (error) => {
        if (error.error.message === 'NO_CALCOLO_ACZ') {
          console.log('Non ci sono passi lavorazione ACZ validi per l\'istruttoria con id: ' + this.istruttoriaDUCorrente.id);
        } else {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
          console.log(error);
        }
      }
    );
  }

  private isInterventoRichiesto(key: string): boolean {
    return this.interventiRichiesti.find(intervento => key.search(intervento) > -1) ? true : false;
  }

  setHeader(codice) {
    const indexInput = codice.indexOf('_INPUT');
    const indexOutput = codice.indexOf('_OUTPUT');
    if (indexInput > -1) {
      codice = codice.substring(0, indexInput);
    } else if (indexOutput > -1) {
      codice = codice.substring(0, indexOutput);
    }
    this.propertyName = Costanti[codice];
    return this.propertyName;
  }

  ngOnDestroy() {
    this.componentDestroyed = false;
  }

}
