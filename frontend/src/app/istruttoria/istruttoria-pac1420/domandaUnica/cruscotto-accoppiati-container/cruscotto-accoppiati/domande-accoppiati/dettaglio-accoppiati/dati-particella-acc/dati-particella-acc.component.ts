import { Component, OnInit } from '@angular/core';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { MessageService } from 'primeng/api';
import { ActivatedRoute } from '@angular/router';
import { DatiParticella, DettaglioDomandaAccoppiati } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/dettaglioDomandaAccoppiati';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { Costanti } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/Costanti';

@Component({
  selector: 'app-dati-particella-acc',
  templateUrl: './dati-particella-acc.component.html',
  styleUrls: ['./dati-particella-acc.component.css']
})
export class DatiParticellaAccComponent implements OnInit {

  cols: any[];
  elementiPagina: number = 10;
  idDomandaCorrente: number;
  dettaglioDomandaAccoppiati = new DettaglioDomandaAccoppiati;
  propertyName: string;
  interventi: any[];

  constructor(
    private messageService: MessageService,
    private route: ActivatedRoute,
    private istruttoriaService: IstruttoriaService
  ) { }

  ngOnInit() {
    this.dettaglioDomandaAccoppiati.datiParticellaACS = new DatiParticella;
    this.setInterventi();
    this.setCols();
    this.setIdDomandaCorrente();
    this.changePage();
  }

  private setInterventi() {
    this.interventi = ['m8', 'm9', 'm10', 'm11', 'm14', 'm15', 'm16', 'm17'];
  }

  private setCols() {
    this.cols = [
      { field: 'codComune', header: 'Cod. Comune' },
      { field: 'foglio', header: 'Foglio' },
      { field: 'particella', header: 'Particella' },
      { field: 'sub', header: 'Sub' },
      { field: 'codColtura', header: 'Cod. Coltura' },
      { field: 'descColtura', header: 'Descrizione Coltura' },
      { field: 'controlloRegioni', header: 'Ctrl Regioni.' },
      { field: 'controlloColture', header: 'Ctrl Colture.' },
      { field: 'controlloAnomCoord', header: 'Anomalie Coor' },
      { field: 'supImpegnata', header: 'Sup Imp.' },
      { field: 'supRichiesta', header: 'Sup Rich.' },
      { field: 'supControlliLoco', header: 'Sup Controllo in loco' },
      { field: 'supEleggibileGis', header: 'Sup Eleggibile Gis' },
      { field: 'supDeterminata', header: 'Sup Determinata' },
    ];
  }

  private setIdDomandaCorrente() {
    this.route.params
      .subscribe(params => {
        this.idDomandaCorrente = params['idDomanda'];
      });
  }

  changePage() {
    this.istruttoriaService.getDettaglioDomandaAccoppiati(this.idDomandaCorrente.toString(), Costanti.acs, Costanti.tabDatiParticella)
      .subscribe(
        (dati) => {
          if (dati) {
            console.log(dati)
            this.dettaglioDomandaAccoppiati.idDomanda = dati[0].idDomanda;
            if (dati[0].datiParticellaACS) {
              this.dettaglioDomandaAccoppiati.datiParticellaACS = dati[0].datiParticellaACS;
              this.interventi.forEach(
                (element) => {
                  this.dettaglioDomandaAccoppiati.datiParticellaACS[element].forEach(
                    (element) => {
                      element.controlloRegioni = this.boolToString((element.controlloRegioni == true || element.controlloRegioni == false) ? element.controlloRegioni : null);
                      element.controlloColture = this.boolToString((element.controlloColture == true || element.controlloColture == false) ? element.controlloColture : null);
                      element.controlloAnomCoord = this.boolToString((element.controlloAnomCoord == true || element.controlloAnomCoord == false) ? !element.controlloAnomCoord : null);
                    }
                  );
                }
              );
            }
            console.log(Costanti.tabDatiParticella + ":");
            console.log(this.dettaglioDomandaAccoppiati);
          }
        },
        (error) => {
          if (error.error.message == "NO_CALCOLO_ACS")
            console.log("Non ci sono passi lavorazione ACS validi per la domanda id: " + this.idDomandaCorrente)
          else {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
            console.log(error)
          }
        }
      );
  }

  setHeader(intervento) {
    this.propertyName = Costanti[intervento];
    return this.propertyName;
  }

  boolToString(val: Boolean): String {
    if (val == null) {
      return '';
    }
    if (val === true) {
      return 'SI';
    }
    if (val === false) {
      return 'NO';
    }
  }

}