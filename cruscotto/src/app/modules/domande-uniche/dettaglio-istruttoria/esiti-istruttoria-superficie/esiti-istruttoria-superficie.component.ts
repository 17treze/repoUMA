import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng-lts';
import { of } from 'rxjs';
import { catchError, switchMap, takeWhile } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { EsitiIstruttoriaView } from '../../models/esiti-istruttoria';
import { Istruttoria } from '../../models/istruttoria';
import { DettaglioIstruttoriaService } from '../dettaglio-istruttoria.service';
import { DatiInterventi } from '../models/dettaglioDomandaAccoppiati';


@Component({
  selector: 'app-esiti-istruttoria-superficie',
  templateUrl: './esiti-istruttoria-superficie.component.html',
  styleUrls: ['../esiti-istruttoria/esiti-istruttoria.component.css']
})
export class EsitiIstruttoriaSuperficieComponent implements OnInit, OnDestroy {

  public datiView: Array<EsitiIstruttoriaView> = new Array<EsitiIstruttoriaView>();
  public datiDisciplina: DatiInterventi = new DatiInterventi();
  public istruttoria: Istruttoria;
  private idIstruttoria: number = Number(this.route.snapshot.paramMap.get('idIstruttoria'));
  private interventi: string[] = ['m8', 'm9', 'm10', 'm11', 'm14', 'm15', 'm16', 'm17'];
  private vedereAncheSeUgualiAZero: string[] = ['DFFRAPPDIS_INPUT', 'DFFRAPPACZ_INPUT', 'DFFRAPPACS_INPUT', 'DFIMPLIQACSLORDO_OUTPUT'];
  private interventiRichiesti: any[];
  private componentDestroyed = true;

  constructor(private service: DettaglioIstruttoriaService,
    private messageService: MessageService,
    private route: ActivatedRoute) { }

  ngOnInit() {
    this.getIstruttoria(this.idIstruttoria);
    this.setObject();
    this.getDatiIstruttoria();
    console.log(this.istruttoria);
  }

  private getIstruttoria(idIstruttoria) {
    this.service.getIstruttoria(idIstruttoria).subscribe(
      (result) => {
        this.istruttoria = result;
        console.log(this.istruttoria);
      },
      error => {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDatiIstruttoria));
      }
    );
  }

  public scroll(el: HTMLElement) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start', inline: 'nearest' });
  }

  private setObject() {
    this.datiView = [
      { tipoEsito: this.interventi[0], datiInput: [], datiOutput: [] },
      { tipoEsito: this.interventi[1], datiInput: [], datiOutput: [] },
      { tipoEsito: this.interventi[2], datiInput: [], datiOutput: [] },
      { tipoEsito: this.interventi[3], datiInput: [], datiOutput: [] },
      { tipoEsito: this.interventi[4], datiInput: [], datiOutput: [] },
      { tipoEsito: this.interventi[5], datiInput: [], datiOutput: [] },
      { tipoEsito: this.interventi[6], datiInput: [], datiOutput: [] },
      { tipoEsito: this.interventi[7], datiInput: [], datiOutput: [] },
    ];
    this.interventiRichiesti = [];
  }


  private getDatiIstruttoria() {
    const doNotShowList: string[] = ['ISCAMP', 'DOMSIGECOCHIUSA', 'BPSSUPSIGECO', 'AZCMPBOV', 'AZCMPOVI'];
    this.service.getEsitiCalcoloIstruttoriaACS(this.idIstruttoria.toString()).pipe(
      switchMap((dati) => {
        if (dati) {
          this.interventi.forEach(
            (element) => {
              for (const key in dati.dettaglioCalcolo[element]) {
                if (doNotShowList.some(nel => key.startsWith(nel))) {
                  continue;
                }
                if (key.search('_INPUT') > -1) {
                  this.datiView.find(dato => dato.tipoEsito === element).datiInput.push(
                    { descrizione: key, valore: dati.dettaglioCalcolo[element][key] }
                  );
                } else if (key.search('_OUTPUT') > -1) {
                  this.datiView.find(dato => dato.tipoEsito === element).datiOutput.push(
                    { descrizione: key, valore: dati.dettaglioCalcolo[element][key] }
                  );
                }
              }
              if ((this.datiView.find(dato => dato.tipoEsito === element).datiInput.length > 0)
                || (this.datiView.find(dato => dato.tipoEsito === element).datiOutput.length > 0)) {
                this.interventiRichiesti.push(element.replace('m', '_M'));
              }
            });
        }
        return this.service.getDisciplinaIstruttoriaACS(this.idIstruttoria.toString());
      }),
      catchError((error) => {
        if (error.error.message === 'NO_CALCOLO_ACS') {
          console.log('Non ci sono passi lavorazione ACS validi per l\'istruttoria con id: ' + this.idIstruttoria.toString());
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
          this.datiDisciplina.input = [];
          this.datiDisciplina.output = [];
          for (const key in datiDisciplinaFinanziaria) {
            if (key.search('_INPUT') > -1) {
              if (this.vedereAncheSeUgualiAZero.includes(key)) {
                this.datiDisciplina.input.push(
                  { codice: key, valore: datiDisciplinaFinanziaria[key] || '0 euro' }
                );
              } else if (this.isInterventoRichiesto(key)) {
                this.datiDisciplina.input.push(
                  { codice: key, valore: datiDisciplinaFinanziaria[key] || '0 euro' }
                );
              } else if (datiDisciplinaFinanziaria[key] !== '0 euro') {
                this.datiDisciplina.input.push(
                  { codice: key, valore: datiDisciplinaFinanziaria[key] }
                );
              }
            } else if (key.search('_OUTPUT') > -1) {
              if (this.vedereAncheSeUgualiAZero.includes(key)) {
                this.datiDisciplina.output.push(
                  { codice: key, valore: datiDisciplinaFinanziaria[key] || '0 euro' }
                );
              } else if (this.isInterventoRichiesto(key)) {
                this.datiDisciplina.output.push(
                  { codice: key, valore: datiDisciplinaFinanziaria[key] || '0 euro' }
                );
              } else if (datiDisciplinaFinanziaria[key] !== '0 euro') {
                this.datiDisciplina.output.push(
                  { codice: key, valore: datiDisciplinaFinanziaria[key] }
                );
              }
            }
          }
        }
      },
      (error) => {
        if (error.error.message === 'NO_CALCOLO_ACS') {
          console.log('Non ci sono passi lavorazione ACS validi per l\'istruttoria con id: ' + this.idIstruttoria.toString());
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
    const translate = 'DETTAGLIO_ISTRUTTORIA.' + codice;
    return translate;
  }

  ngOnDestroy() {
    this.componentDestroyed = false;
  }

  isHidden(datiView: EsitiIstruttoriaView) {
    return !(datiView.datiInput.length > 0) && !(datiView.datiOutput.length > 0);
  }

}
