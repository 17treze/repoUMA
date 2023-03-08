import { Subject } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { MenuItem, MessageService } from 'primeng/api';
import { SostegnoDu } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/SostegnoDu';
import { DettaglioDomandaAccoppiati, DettaglioDatiIstruttoriaACS, DettaglioDatiIstruttoriaACZ } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/dettaglioDomandaAccoppiati';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { Costanti } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/Costanti';
import { Istruttoria } from 'src/app/a4g-common/classi/Istruttoria';
import { IstruttoriaCorrente } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoriaCorrente';

@Component({
  selector: 'app-dati-istruttoria',
  templateUrl: './dati-istruttoria.component.html',
  styleUrls: ['./dati-istruttoria.component.css']
})
export class DatiIstruttoriaComponent implements OnInit {

  selectedSostegno: string;
  dati: MenuItem = null;
  idDomandaCorrente: number;
  tipoSostegno: string;
  dettaglioDomandaAccoppiati = new DettaglioDomandaAccoppiati;
  tipoDettaglio: string;
  service: string;
  statoSostegno$: Subject<string> = new Subject();

  constructor(
    private route: ActivatedRoute,
    private istruttoriaService: IstruttoriaService,
    private messageService: MessageService,
    private router: Router,
    private istruttoriaCorrente: IstruttoriaCorrente
  ) { }

  ngOnInit() {
    this.setSostegno();
    this.setIdDomandaCorrente();
    this.getDatiIstruttoria();
  }

  private setSostegno() {
    if (this.router.url.split('/').filter(url => url === Costanti.accoppiatoZootecniaRichiesto).length > 0) {
      this.selectedSostegno = SostegnoDu.ZOOTECNIA;
    } else {
      this.selectedSostegno = SostegnoDu.SUPERFICIE;
    }
    if (this.selectedSostegno == SostegnoDu.ZOOTECNIA) {
      this.tipoSostegno = Costanti.acz
      this.dettaglioDomandaAccoppiati.datiIstruttoriaACZ = new DettaglioDatiIstruttoriaACZ;
      this.tipoDettaglio = 'datiIstruttoriaACZ';
      this.service = 'putDettaglioDatiIstruttoriaACZ';
    }
    else if (this.selectedSostegno == SostegnoDu.SUPERFICIE) {
      this.tipoSostegno = Costanti.acs
      this.dettaglioDomandaAccoppiati.datiIstruttoriaACS = new DettaglioDatiIstruttoriaACS;
      this.tipoDettaglio = 'datiIstruttoriaACS';
      this.service = 'putDettaglioDatiIstruttoriaACS';
      this.setDatiDaInserire();
    }
  }

  private setDatiDaInserire() {
    this.dati = {
      items: [
        { label: 'ACS - Superficie determinata dall’istruttore Soia', id: 'superficieDeterminataSoiaM8', target: 'Ha' },
        { label: 'ACS - Superficie determinata dall’istruttore Frumento', id: 'superficieDeterminataFrumentoM9', target: 'Ha' },
        { label: 'ACS - Superficie determinata dall’istruttore Proteaginose', id: 'superficieDeterminataProteaginoseM10', target: 'Ha' },
        { label: 'ACS - Superficie determinata dall’istruttore Leguminose', id: 'superficieDeterminataLeguminoseM11', target: 'Ha' },
        { label: 'ACS - Superficie determinata dall’istruttore Pomodoro', id: 'superficieDeterminataPomodoroM14', target: 'Ha' },
        { label: 'ACS - Superficie determinata dall’istruttore Olivo Standard', id: 'superficieDeterminataOlivoStandardM15', target: 'Ha' },
        { label: 'ACS - Superficie determinata dall’istruttore Olivo  7,5%', id: 'superficieDeterminataOlivoPendenzaM16', target: 'Ha' },
        { label: 'ACS - Superficie determinata dall’istruttore Olivo Qualità', id: 'superficieDeterminataOlivoQualitaM17', target: 'Ha' }
      ]
    };
  }

  private setIdDomandaCorrente() {
    this.route.params
      .subscribe(params => {
        this.idDomandaCorrente = params['idDomanda'];
      });
  }

  private getDatiIstruttoria() {
    this.istruttoriaService.getDettaglioDomandaAccoppiati(this.idDomandaCorrente.toString(), this.tipoSostegno, Costanti.tabDatiIstruttoria)
      .subscribe(
        (dati) => {
          if (dati) {
            console.log(dati);
            this.statoSostegno$.next(dati[0].statoLavorazioneSostegno)
            this.dettaglioDomandaAccoppiati.idDomanda = dati[0].idDomanda;
            if (dati[0][this.tipoDettaglio]) {
              this.dettaglioDomandaAccoppiati[this.tipoDettaglio] = dati[0][this.tipoDettaglio];
              console.log(Costanti.tabControlliSostego + ' ' + this.selectedSostegno + ":");
              console.log(this.dettaglioDomandaAccoppiati);
            }
          }
        },
        (error) => {
          console.error(error);
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        }
      );
  }

  onSubmit() {
    console.log(this.dettaglioDomandaAccoppiati);
    this.istruttoriaService[this.service](this.idDomandaCorrente, this.dettaglioDomandaAccoppiati[this.tipoDettaglio])
      .subscribe(
        (success) => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        },
        (error) => {
          console.error(error);
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_SALVATAGGIO_DATI_ISTRUTTORIA));
        }
      );
  }

  getIstruttoriaCorrente(): Istruttoria {
    return this.istruttoriaCorrente.istruttoria;
  }
}