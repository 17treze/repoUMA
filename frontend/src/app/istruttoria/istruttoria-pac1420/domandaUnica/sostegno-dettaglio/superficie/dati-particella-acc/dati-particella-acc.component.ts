import { Component, OnInit } from '@angular/core';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { MessageService } from 'primeng/api';
import { ActivatedRoute } from '@angular/router';
import { DatiParticella, DettaglioDomandaAccoppiati } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/dettaglioDomandaAccoppiati';
import { Costanti } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/Costanti';
import { IstruttoriaDomandaUnica } from '../../../classi/IstruttoriaDomandaUnica';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';

@Component({
  selector: 'app-dati-particella-acc',
  templateUrl: './dati-particella-acc.component.html',
  styleUrls: ['./dati-particella-acc.component.css']
})
export class DatiParticellaAccComponent implements OnInit {

  cols: any[];
  elementiPagina: number = 10;
  dettaglioDomandaAccoppiati = new DettaglioDomandaAccoppiati;
  propertyName: string;
  interventi: any[];
  istruttoriaDUCorrente: IstruttoriaDomandaUnica;

  constructor(
    private messageService: MessageService,
    private route: ActivatedRoute,
    private istruttoriaDettaglioService: IstruttoriaDettaglioService,
  ) { }

  ngOnInit() {
    this.istruttoriaDUCorrente = this.route.snapshot.data['domandaIstruttoria'];
    this.dettaglioDomandaAccoppiati.datiParticellaACS = new DatiParticella;
    this.setInterventi();
    this.setCols();
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
      { field: 'superficieAnomaliaCoor', header: 'Sup An. Coord.' },
      { field: 'supImpegnata', header: 'Sup Imp.' },
      { field: 'supRichiesta', header: 'Sup Rich.' },
      { field: 'supControlliLoco', header: 'Sup Controllo in loco' },
      { field: 'supEleggibileGis', header: 'Sup Eleggibile Gis' },
      { field: 'supDeterminata', header: 'Sup Determinata' },
    ];
  }

  changePage() {
    this.istruttoriaDettaglioService.getEsitiParticelle(this.istruttoriaDUCorrente.id.toString())
      .subscribe(
        (datiParticellaACS) => {
          if (datiParticellaACS) {
            console.log(datiParticellaACS)
              this.dettaglioDomandaAccoppiati.datiParticellaACS = datiParticellaACS;
              this.interventi.forEach(
                (element) => {
                  this.dettaglioDomandaAccoppiati.datiParticellaACS[element].forEach(
                    (element) => {
                      element.controlloRegioni = this.boolToString((element.controlloRegioni == true || element.controlloRegioni == false) ? element.controlloRegioni : null);
                      element.controlloColture = this.boolToString((element.controlloColture == true || element.controlloColture == false) ? element.controlloColture : null);
                      element.controlloAnomCoord = this.boolToString((element.controlloAnomCoord == true || element.controlloAnomCoord == false) ? element.controlloAnomCoord : null);
                    }
                  );
                }
              );
            console.log(Costanti.tabDatiParticella + ":");
            console.log(this.dettaglioDomandaAccoppiati);
          }
        },
        (error) => {
          if (error.error.message == "NO_CALCOLO_ACS")
            console.log("Non ci sono passi lavorazione ACS validi per la domanda id: " + this.istruttoriaDUCorrente.id)
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