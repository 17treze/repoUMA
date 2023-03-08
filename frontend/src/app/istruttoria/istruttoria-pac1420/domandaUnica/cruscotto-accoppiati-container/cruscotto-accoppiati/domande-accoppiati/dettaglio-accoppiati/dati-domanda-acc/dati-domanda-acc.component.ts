import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { MessageService } from 'primeng/api';
import { Costanti } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/Costanti';
import { SostegnoDu } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/SostegnoDu';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { DettaglioDomandaAccoppiati, DatiDomandaACS, DettagliInterventiACS, DatiDomandaACZ, DettagliInterventiACZ, DatiInterventi } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/dettaglioDomandaAccoppiati';

@Component({
  selector: 'app-dati-domanda-acc',
  templateUrl: './dati-domanda-acc.component.html',
  styleUrls: ['./dati-domanda-acc.component.css']
})
export class DatiDomandaAccComponent implements OnInit {

  selectedSostegno: string;
  idDomandaCorrente: number;
  tipoSostegno: string;
  cols: any[];
  colsInput: any[];
  colsOutput: any[];
  dettaglioDomandaAccoppiati = new DettaglioDomandaAccoppiati;
  propertyName: string;
  interventi: any[];
  datiDomanda: string;

  constructor(
    private route: ActivatedRoute,
    private istruttoriaService: IstruttoriaService,
    private messageService: MessageService,
    private router: Router) { }

  ngOnInit() {
    this.setSostegno();
    this.setIdDomandaCorrente();
    this.setCols();
    this.getDatiIstruttoria();
  }

  private setSostegno() {
    if (this.router.url.split('/').filter(url => url === Costanti.accoppiatoZootecniaRichiesto).length > 0) {
      this.selectedSostegno = SostegnoDu.ZOOTECNIA;
    } else {
      this.selectedSostegno = SostegnoDu.SUPERFICIE;
    }
    if (this.selectedSostegno == SostegnoDu.ZOOTECNIA) {
      this.tipoSostegno = Costanti.acz;
      this.datiDomanda = 'datiDomandaACZ';
      this.dettaglioDomandaAccoppiati[this.datiDomanda] = new DatiDomandaACZ;
      this.dettaglioDomandaAccoppiati[this.datiDomanda].dettaglioCalcolo = new DettagliInterventiACZ;
      this.interventi = ['int310', 'int311', 'int313', 'int315', 'int320', 'int321', 'int316', 'int318', 'int322'];
    }
    else if (this.selectedSostegno == SostegnoDu.SUPERFICIE) {
      this.tipoSostegno = Costanti.acs
      this.datiDomanda = 'datiDomandaACS';
      this.dettaglioDomandaAccoppiati[this.datiDomanda] = new DatiDomandaACS;
      this.dettaglioDomandaAccoppiati[this.datiDomanda].dettaglioCalcolo = new DettagliInterventiACS;
      this.interventi = ['m8', 'm9', 'm10', 'm11', 'm14', 'm15', 'm16', 'm17'];
    }
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
    this.istruttoriaService.getDettaglioDomandaAccoppiati(this.idDomandaCorrente.toString(), this.tipoSostegno, datiIstruttoria)
      .subscribe(
        (dati) => {
          if (dati) {
            console.log(dati);
            this.dettaglioDomandaAccoppiati.idDomanda = dati[0].idDomanda;
            if (dati[0][this.datiDomanda]) {
              this.dettaglioDomandaAccoppiati[this.datiDomanda].sintesiCalcolo = [];
              for (let key in dati[0][this.datiDomanda].sintesiCalcolo) {
                this.dettaglioDomandaAccoppiati[this.datiDomanda].sintesiCalcolo
                  .push(
                    { codice: key, valore: dati[0][this.datiDomanda].sintesiCalcolo[key] }
                  );
              }
              this.interventi.forEach(
                (element) => {
                  this.dettaglioDomandaAccoppiati[this.datiDomanda].dettaglioCalcolo[element] = [];
                  this.dettaglioDomandaAccoppiati[this.datiDomanda].dettaglioCalcolo[element].input = [];
                  this.dettaglioDomandaAccoppiati[this.datiDomanda].dettaglioCalcolo[element].output = [];
                  for (let key in dati[0][this.datiDomanda].dettaglioCalcolo[element]) {
                    if (key.search("_INPUT") > -1) {
                      this.dettaglioDomandaAccoppiati[this.datiDomanda].dettaglioCalcolo[element].input
                        .push(
                          { codice: key, valore: dati[0][this.datiDomanda].dettaglioCalcolo[element][key] }
                        );
                    }
                    else if (key.search("_OUTPUT") > -1) {
                      this.dettaglioDomandaAccoppiati[this.datiDomanda].dettaglioCalcolo[element].output
                        .push(
                          { codice: key, valore: dati[0][this.datiDomanda].dettaglioCalcolo[element][key] }
                        );
                    }
                  }
                })
            }
            if (dati[0].datiDisciplinaFinanziaria) {
              this.dettaglioDomandaAccoppiati.datiDisciplinaFinanziaria = new DatiInterventi;
              this.dettaglioDomandaAccoppiati.datiDisciplinaFinanziaria.input = [];
              this.dettaglioDomandaAccoppiati.datiDisciplinaFinanziaria.output = [];
              for (let key in dati[0].datiDisciplinaFinanziaria) {
                if (key.search("_INPUT") > -1) {
                  if(dati[0].datiDisciplinaFinanziaria[key] != "0 euro"){
                    this.dettaglioDomandaAccoppiati.datiDisciplinaFinanziaria.input
                      .push(
                        { codice: key, valore: dati[0].datiDisciplinaFinanziaria[key] }
                      );
                  }
                }
                else if (key.search("_OUTPUT") > -1) {
                  if(dati[0].datiDisciplinaFinanziaria[key] != "0 euro"){
                    this.dettaglioDomandaAccoppiati.datiDisciplinaFinanziaria.output
                      .push(
                        { codice: key, valore: dati[0].datiDisciplinaFinanziaria[key] }
                      );
                  }
                }
              }
            }
            console.log(this.dettaglioDomandaAccoppiati[this.datiDomanda])
          }
        },
        (error) => {
          if (error.error.message == "NO_CALCOLO_ACS")
            console.log("Non ci sono passi lavorazione ACS validi per la domanda id: " + this.idDomandaCorrente)
          else if (error.error.message == "NO_CALCOLO_ACZ")
            console.log("Non ci sono passi lavorazione ACZ validi per la domanda id: " + this.idDomandaCorrente)
          else {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
            console.log(error)
          }
        }
      );
  }

  setHeader(codice) {
    var indexInput = codice.indexOf("_INPUT");
    var indexOutput = codice.indexOf("_OUTPUT");
    if (indexInput > -1)
      var codice = codice.substring(0, indexInput);
    else if (indexOutput > -1)
      var codice = codice.substring(0, indexOutput);
    this.propertyName = Costanti[codice];
    return this.propertyName;
  }

}
