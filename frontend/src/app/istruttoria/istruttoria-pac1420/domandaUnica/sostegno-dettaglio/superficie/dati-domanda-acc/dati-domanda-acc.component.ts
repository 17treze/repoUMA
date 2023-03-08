import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { of } from 'rxjs';
import { catchError, switchMap, takeWhile } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { DatiDomandaACS, DatiInterventi, DettagliInterventiACS, DettaglioDomandaAccoppiati } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/dettaglioDomandaAccoppiati';
import { SostegnoDu } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/SostegnoDu';
import { Costanti } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/Costanti';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { IstruttoriaDomandaUnica } from '../../../classi/IstruttoriaDomandaUnica';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';


@Component({
  selector: 'app-dati-domanda-acc',
  templateUrl: './dati-domanda-acc.component.html',
  styleUrls: ['./dati-domanda-acc.component.css']
})
export class DatiDomandaAccComponent implements OnInit, OnDestroy {

  selectedSostegno: string;
  tipoSostegno: string;
  cols: any[];
  colsInput: any[];
  colsOutput: any[];
  dettaglioDomandaAccoppiati = new DettaglioDomandaAccoppiati;
  propertyName: string;
  interventi: any[];
  datiDomanda: string;
  istruttoriaDUCorrente: IstruttoriaDomandaUnica;
  vedereAncheSeUgualiAZero: string[] = ['DFFRAPPDIS_INPUT', 'DFFRAPPACZ_INPUT', 'DFFRAPPACS_INPUT', 'DFIMPLIQACSLORDO_OUTPUT'];
  private interventiRichiesti: any[];
  private componentDestroyed = true;

  constructor(
    private route: ActivatedRoute,
    private istruttoriaService: IstruttoriaService,
    private messageService: MessageService,
    private istruttoriaDettaglioService: IstruttoriaDettaglioService,
    private router: Router) { }

  ngOnInit() {
    this.istruttoriaDUCorrente = this.route.snapshot.data['domandaIstruttoria'];
    this.setSostegno();
    this.setCols();
    this.getDatiIstruttoria();
  }

  private setSostegno() {
    this.selectedSostegno = SostegnoDu.SUPERFICIE;
    this.tipoSostegno = Costanti.acs;
    this.datiDomanda = 'datiDomandaACS';
    this.dettaglioDomandaAccoppiati[this.datiDomanda] = new DatiDomandaACS;
    this.dettaglioDomandaAccoppiati[this.datiDomanda].dettaglioCalcolo = new DettagliInterventiACS;
    this.interventi = ['m8', 'm9', 'm10', 'm11', 'm14', 'm15', 'm16', 'm17'];
    this.interventiRichiesti = [];
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
    this.istruttoriaDettaglioService.getEsitiCalcoloIstruttoriaAcs(this.istruttoriaDUCorrente.id.toString()).pipe(
      switchMap((dati) => {
        if (dati) {
          console.log(dati);
          this.dettaglioDomandaAccoppiati.idDomanda = this.istruttoriaDUCorrente.id.toString();
          if (true) {
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
                  this.interventiRichiesti.push('_' + element.toUpperCase());
                }
              });
          }
          console.log(this.dettaglioDomandaAccoppiati[this.datiDomanda]);
        }

        return this.istruttoriaDettaglioService.getDisciplinaIstruttoriaAcs(this.istruttoriaDUCorrente.id.toString());
      }),
      catchError((error) => {
        if (error.error.message === 'NO_CALCOLO_ACS') {
          console.log('Non ci sono passi lavorazione ACS validi per l\'istruttoria con id: ' + this.istruttoriaDUCorrente.id);
        } else if (error.error.message === 'NO_CALCOLO_ACZ') {
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
        if (error.error.message === 'NO_CALCOLO_ACS') {
          console.log('Non ci sono passi lavorazione ACS validi per l\'istruttoria con id: ' + this.istruttoriaDUCorrente.id);
        } else if (error.error.message === 'NO_CALCOLO_ACZ') {
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
