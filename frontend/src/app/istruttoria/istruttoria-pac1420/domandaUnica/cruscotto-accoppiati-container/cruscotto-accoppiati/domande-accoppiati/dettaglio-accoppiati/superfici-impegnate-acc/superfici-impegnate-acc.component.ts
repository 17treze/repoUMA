import { Component, OnInit } from '@angular/core';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { DettaglioDomandaAccoppiati, SuperficiImpegnateSintesi, SuperficiImpegnateACS, DatiSupImpegnata } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/dettaglioDomandaAccoppiati';
import { Costanti } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/Costanti';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';

@Component({
  selector: 'app-superfici-impegnate-acc',
  templateUrl: './superfici-impegnate-acc.component.html',
  styleUrls: ['./superfici-impegnate-acc.component.css']
})
export class SuperficiImpegnateAccComponent implements OnInit {

  interventi: any[];
  cols: any[];
  elementiPagina: number = 10;
  idDomandaCorrente: number;
  dettaglioDomandaAccoppiati = new DettaglioDomandaAccoppiati;
  propertyName: string;
  superficiImpegnateSintesi: SuperficiImpegnateSintesi[];

  constructor(
    private messageService: MessageService,
    private route: ActivatedRoute,
    private istruttoriaService: IstruttoriaService
  ) { }

  ngOnInit() {
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
      { field: 'idParticella', header: 'Id Particella' },
      { field: 'comune', header: 'Comune Catastale' },
      { field: 'codNazionale', header: 'Cod. Nazionale' },
      { field: 'foglio', header: 'Foglio' },
      { field: 'particella', header: 'Particella' },
      { field: 'sub', header: 'Sub' },
      { field: 'codIsola', header: 'Isola' },
      { field: 'codiceColtura3', header: 'Cod. Coltura' },
      { field: 'descrizioneColtura', header: 'Descr. Coltura' },
      // { field: 'supRichiesta', header: 'Sup. Imp. Lorda' },
      { field: 'supRichiestaNetta', header: 'Sup Imp. Netta' },
    ];
  }

  private setIdDomandaCorrente() {
    this.route.params
      .subscribe(params => {
        this.idDomandaCorrente = params['idDomanda'];
      });
  }

  changePage() {
    this.istruttoriaService.getDettaglioDomandaAccoppiati(this.idDomandaCorrente.toString(), Costanti.acs, Costanti.tabSuperficiImpegnate)
      .subscribe(
        (dati) => {
          if (dati) {
            console.log(dati);
            this.dettaglioDomandaAccoppiati.idDomanda = dati[0].idDomanda;
            if (dati[0].datiSuperficiImpegnate) {
              this.dettaglioDomandaAccoppiati.datiSuperficiImpegnate = new SuperficiImpegnateACS;
              this.dettaglioDomandaAccoppiati.datiSuperficiImpegnate.supRichiesta = dati[0].datiSuperficiImpegnate.supRichiesta;
              this.dettaglioDomandaAccoppiati.datiSuperficiImpegnate.supRichiestaNetta = dati[0].datiSuperficiImpegnate.supRichiestaNetta;
              this.interventi.forEach(
                (element) => {
                  if (dati[0].datiSuperficiImpegnate[element]) {
                    this.dettaglioDomandaAccoppiati.datiSuperficiImpegnate[element] = new DatiSupImpegnata;
                    this.dettaglioDomandaAccoppiati.datiSuperficiImpegnate[element].supRichiesta = dati[0].datiSuperficiImpegnate[element].supRichiesta;
                    this.dettaglioDomandaAccoppiati.datiSuperficiImpegnate[element].supRichiestaNetta = dati[0].datiSuperficiImpegnate[element].supRichiestaNetta;
                    this.dettaglioDomandaAccoppiati.datiSuperficiImpegnate[element].superficiImpegnate = [];
                    dati[0].datiSuperficiImpegnate[element].superficiImpegnate.forEach(element2 => {
                      this.dettaglioDomandaAccoppiati.datiSuperficiImpegnate[element].superficiImpegnate
                        .push({
                          datiCatastali: element2.datiCatastali,
                          datiColtivazione: element2.datiColtivazione,
                          riferimentiCartografici: element2.riferimentiCartografici,
                          supRichiesta: element2.supRichiesta,
                          supRichiestaNetta: element2.supRichiestaNetta
                        })
                    });
                  }
                })
              // Popolo oggetto per dati sintesi
              this.superficiImpegnateSintesi = new Array(1);
              this.superficiImpegnateSintesi[0] = new SuperficiImpegnateSintesi;
              this.superficiImpegnateSintesi[0].supRichiestaTotale = this.dettaglioDomandaAccoppiati.datiSuperficiImpegnate.supRichiesta;
              const supImp = this.superficiImpegnateSintesi[0];
              this.interventi.forEach(
                (intervento) => {
                  if (this.dettaglioDomandaAccoppiati.datiSuperficiImpegnate[intervento] && this.dettaglioDomandaAccoppiati.datiSuperficiImpegnate[intervento].supRichiesta)
                    supImp[intervento] = this.dettaglioDomandaAccoppiati.datiSuperficiImpegnate[intervento].supRichiesta;
                }
              );
              this.superficiImpegnateSintesi[0] = supImp;
            }
            console.log(Costanti.tabSuperficiImpegnate + ":");
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

}
