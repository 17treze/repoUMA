import { DettaglioCapi, CapoRichiesto } from '../../../../../domain/dettaglioCapi';
import { Capo } from './../../../../../domain/dettaglioAllevamenti';
import { Component, OnInit, ViewChild } from '@angular/core';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { Costanti } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/Costanti';
import { ActivatedRoute } from '@angular/router';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { MessageService } from 'primeng/api';
import { Labels } from 'src/app/app.labels';
import { LoaderService } from 'src/app/loader.service';

@Component({
  selector: 'app-capi-richiesti',
  templateUrl: './capi-richiesti.component.html',
  styleUrls: ['./capi-richiesti.component.css']
})
export class CapiRichiestiComponent implements OnInit {

  allevamenti: DettaglioCapi[] = [];
  cols: any[] = [];
  idDomanda: string;
  intestazioni = Labels;
  elementiPagina: number = 10;
  nessunCapoPresentato: boolean = true;
  @ViewChild('table') table;

  constructor(
    private istruttoriaService: IstruttoriaService,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private loaderService: LoaderService,
  ) { }

  ngOnInit() {
    this.loaderService.setTimeout(480000);
    this.setCols();
    this.setIdDomanda();
    this.changePage();
  }

  ngOnDestroy(): void {
    this.loaderService.resetTimeout();
  }

  private setCols() {
    this.cols = [
      { field: 'codiceCapo', header: this.intestazioni.marcaAuricolare },
      { field: 'codiceSpecie', header: this.intestazioni.codiceSpecie },
      { field: 'esito', header: this.intestazioni.ESITO_CONTROLLO },
      { field: 'messaggio', header: this.intestazioni.messaggioEsito },
      { field: 'duplicato', header: this.intestazioni.duplicato },
      { field: 'controlloSuperato', header: this.intestazioni.controlloNonSuperato },
    ];
  }

  private setIdDomanda() {
    this.route.paramMap.subscribe(params => {
      this.idDomanda = params.get(Costanti.dettaglioDomandaParam);
    });
  }

  changePage() {
    let params: any = { tipo: "RICHIESTO" }
    this.istruttoriaService.getCapi(this.idDomanda, JSON.stringify(params))
      .subscribe(
        (dati => {
          if (dati) {
            dati.forEach(element => {
              element.datiAllevamento = JSON.parse(element.datiAllevamento);
              element.datiDetentore = JSON.parse(element.datiDetentore);
              element.datiProprietario = JSON.parse(element.datiProprietario);
              this.allevamenti.push(element);
            });

            this.allevamenti.forEach(element => {
              let capiRichiesti: Array<CapoRichiesto> = new Array<CapoRichiesto>();
              element.richiesteAllevamentoDuEsito.forEach(result => {
                if ('PRESENTATA' == result.stato) {
                  result.esito = result.esito.split('_').join(' ');
                  capiRichiesti.push(result);
                  this.nessunCapoPresentato = false;
                }

              })
              element.richiesteAllevamentoDuEsito = capiRichiesti;
            })
            this.allevamenti.sort(function (a, b) {
              return a.codiceIntervento - b.codiceIntervento;
            });
          }
          console.log(this.allevamenti);
        }
        ),
        (error => {
          console.log(error);
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        })
      );
  }

  updateControlloSuperato(capo: CapoRichiesto) {
    capo.controlloSuperato = !capo.controlloSuperato;
    this.updateCapo(capo);
  }

  updateCapo(capo: CapoRichiesto) {
    this.istruttoriaService.aggiornaCapo(this.idDomanda, capo).subscribe(
      data => {
        console.log(data);
        capo = data;
      },
      error => {
        console.log(error);
        // this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
      }
    );
  }
}