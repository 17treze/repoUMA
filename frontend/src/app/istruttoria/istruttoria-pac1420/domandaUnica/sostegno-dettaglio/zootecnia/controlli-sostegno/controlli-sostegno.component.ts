import { Component, OnInit } from '@angular/core';
import { DettaglioDomandaAccoppiati, DettaglioControlliSostegno } from '../../../classi/dettaglioDomandaAccoppiati';
import { MenuItem, MessageService } from 'primeng/api';
import { IstruttoriaService } from '../../../istruttoria.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Costanti } from '../../../Costanti';
import { SostegnoDu } from '../../../classi/SostegnoDu';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';

@Component({
  selector: 'app-controlli-sostegno',
  templateUrl: './controlli-sostegno.component.html',
  styleUrls: ['./controlli-sostegno.component.css']
})
export class ControlliSostegnoComponent implements OnInit {
  cols: any[];
  idDomandaCorrente: number;
  selectedSostegno: string;
  tipoSostegno: string;
  dettaglioDomandaAccoppiati = new DettaglioDomandaAccoppiati();
  controlliSostegnoErrors: MenuItem[] = [];
  controlliSostegnoWarnings: MenuItem[] = [];
  controlliSostegnoInfos: MenuItem[] = [];
  controlliSostegnoSuccesses: MenuItem[] = [];

  constructor(
    private istruttoriaService: IstruttoriaService,
    private istruttoriaDettaglioService: IstruttoriaDettaglioService,
    private messageService: MessageService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.setSostegno();
    this.setCols();
    this.setIdDomandaCorrente();
    this.getControlliSostegno(this.idDomandaCorrente.toString());
  }

  private setSostegno() {
    this.selectedSostegno = SostegnoDu.ZOOTECNIA;
    this.tipoSostegno = Costanti.acz
  }

  private setCols() {
    this.cols = [
      { field: 'b', header: '', width: '100%' }
    ];
  }

  private setIdDomandaCorrente() {
    this.route.params
      .subscribe(params => {
        this.idDomandaCorrente = params['idIstruttoria'];
      });
  }

  private getControlliSostegno(idDomanda) {
    this.istruttoriaDettaglioService.getControlliSostegno(idDomanda)
      .subscribe(
        (dati) => {
          if (dati) {
            let ctrlSostegno = new DettaglioControlliSostegno();
            this.dettaglioDomandaAccoppiati.controlliSostegno = ctrlSostegno;
            this.dettaglioDomandaAccoppiati.controlliSostegno.errors = dati.errors;
            this.dettaglioDomandaAccoppiati.controlliSostegno.infos = dati.infos;
            this.dettaglioDomandaAccoppiati.controlliSostegno.warnings = dati.warnings;
            this.dettaglioDomandaAccoppiati.controlliSostegno.successes = dati.successes;
            console.log(Costanti.tabControlliSostego + ' ' + this.selectedSostegno + ":");
            console.log(this.dettaglioDomandaAccoppiati)
            if (this.dettaglioDomandaAccoppiati.controlliSostegno) {
              if (this.dettaglioDomandaAccoppiati.controlliSostegno.errors) {
                this.dettaglioDomandaAccoppiati.controlliSostegno.errors.forEach(
                  (element) => {
                    if (Costanti[element] != null) {
                      this.controlliSostegnoErrors.push(
                        {
                          label: element,
                          title: Costanti[element]
                        }
                      )
                    }
                    console.log(this.controlliSostegnoErrors);
                  })
                  this.controlliSostegnoErrors = this.distinctControlli(this.controlliSostegnoErrors);
              }
              if (this.dettaglioDomandaAccoppiati.controlliSostegno.warnings) {
                this.dettaglioDomandaAccoppiati.controlliSostegno.warnings.forEach(
                  (element) => {
                    if (Costanti[element] != null) {
                      this.controlliSostegnoWarnings.push(
                        {
                          label: element,
                          title: Costanti[element]
                        }
                      )
                    }
                    console.log(this.controlliSostegnoWarnings);
                  })
              }
              if (this.dettaglioDomandaAccoppiati.controlliSostegno.infos) {
                this.dettaglioDomandaAccoppiati.controlliSostegno.infos.forEach(
                  (element) => {
                    if (Costanti[element] != null) {
                      this.controlliSostegnoInfos.push(
                        {
                          label: element,
                          title: Costanti[element]
                        }
                      )
                    }
                    console.log(this.controlliSostegnoInfos);
                  })
                  this.controlliSostegnoInfos = this.distinctControlli(this.controlliSostegnoInfos);
              }
              if (this.dettaglioDomandaAccoppiati.controlliSostegno.successes) {
                this.dettaglioDomandaAccoppiati.controlliSostegno.successes.forEach(
                  (element) => {
                    if (Costanti[element] != null) {
                      this.controlliSostegnoSuccesses.push(
                        {
                          label: element,
                          title: Costanti[element]
                        }
                      )
                    }
                    console.log(this.controlliSostegnoSuccesses);
                  })
                  this.controlliSostegnoSuccesses = this.distinctControlli(this.controlliSostegnoSuccesses);
              }
            }
          }
        },
        (error) => {
          if (error.error.message == "NO_CALCOLO_ACZ")
            console.log("Non ci sono passi lavorazione ACZ validi per la domanda id: " + this.idDomandaCorrente)
          else {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
            console.log(error)
          }
        }
      )
  }

  private distinctControlli(controlli: MenuItem[]){
    for (let x = 0; x < controlli.length; x++) {
      const element = controlli[x];
      if(controlli[x + 1] != null && element.title === controlli[x + 1].title){
          controlli = controlli.splice(x,1);
        }
    }
    return controlli;
  }
}
