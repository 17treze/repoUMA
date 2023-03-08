import { ConfigurazioneIstruttoriaService } from './../../../sostegno-shared/configurazione-istruttoria/shared/configurazione.istruttoria.service';
import { Subject } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { MenuItem, MessageService } from 'primeng/api';
import { SostegnoDu } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/SostegnoDu';
import { ActivatedRoute, Router } from '@angular/router';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { Costanti } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/Costanti';
import { Istruttoria } from 'src/app/a4g-common/classi/Istruttoria';
import { IstruttoriaCorrente } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoriaCorrente';
import { IstruttoriaDomandaUnica } from '../../../classi/IstruttoriaDomandaUnica';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';
import { DatiIstruttoriaAccoppiati } from '../../../domain/datiIstruttoriaAccoppiati';

@Component({
  selector: 'app-dati-istruttoria',
  templateUrl: './dati-istruttoria.component.html',
  styleUrls: ['./dati-istruttoria.component.css']
})
export class DatiIstruttoriaComponent implements OnInit {

  selectedSostegno: string;
  dati: MenuItem = null;
  tipoSostegno: string;
  datiIstruttoriaDaInserire: DatiIstruttoriaAccoppiati;
  tipoDettaglio: string;
  service: string;
  statoSostegno$: Subject<string> = new Subject();
  istruttoriaDUCorrente: IstruttoriaDomandaUnica;

  public showCheckboxAnnulloRitardoPresentazione = false;
  
  constructor(
    private route: ActivatedRoute,
    private istruttoriaService: IstruttoriaService,
    private istruttoriaDettaglioService: IstruttoriaDettaglioService,
    private messageService: MessageService,
    private router: Router,
    private istruttoriaCorrente: IstruttoriaCorrente,
    private confService: ConfigurazioneIstruttoriaService
  ) { }

  ngOnInit() {
    this.istruttoriaDUCorrente = this.route.snapshot.data['domandaIstruttoria'];
    this.setSostegno();
    this.getDatiIstruttoria();
    this.controllaRitardo();
  }

  private setSostegno() {
    this.selectedSostegno = SostegnoDu.SUPERFICIE;
    this.tipoSostegno = Costanti.acs
    this.tipoDettaglio = 'datiIstruttoriaACS';
    this.service = 'putDettaglioDatiIstruttoriaACS';
    this.setDatiDaInserire();
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


  private getDatiIstruttoria() {
    this.istruttoriaDettaglioService.getDatiIstruttoriaDuAcs(this.istruttoriaDUCorrente.id.toString())
      .subscribe(
        (dati) => {
          if (dati) {
            this.datiIstruttoriaDaInserire = dati;
          } else {
            this.datiIstruttoriaDaInserire = new DatiIstruttoriaAccoppiati();
          }
          this.statoSostegno$.next(this.istruttoriaDUCorrente.stato);//dati[0].statoLavorazioneSostegno)
        },
        (error) => {
          console.error(error);
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
        }
      );
  }

  onSubmit() {

    this.istruttoriaDettaglioService.saveOrUpdateDatiIstruttoriaDuAcs(this.istruttoriaDUCorrente.id.toString(), this.datiIstruttoriaDaInserire)
      .subscribe(
        x => {
          this.datiIstruttoriaDaInserire=x;
          console.log('Salvataggio dati Istruttoria avvenuto con successo'),
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        },
        error => {
          console.error('Errore in salvataggio dati Istruttoria: ' + error),
            console.error(error),
            A4gMessages.handleError(this.messageService, error, A4gMessages.ERRORE_SALVATAGGIO_DATI_ISTRUTTORIA);
        }
      );
  }

  getIstruttoriaCorrente(): Istruttoria {
    return this.istruttoriaCorrente.istruttoria;
  }

  private controllaRitardo() {
    this.confService.getConfigurazioneIstruttorie(this.istruttoriaDUCorrente.domanda.campagna)
      .subscribe(conf => {
        // controlla che la domanda sia di modifica
        if (String(this.istruttoriaDUCorrente.domanda['codModuloDomanda']).includes('BPS_ART_15')) {
          // e che sia stata presentata dopo la data di scadenza
          if (this.istruttoriaDUCorrente.domanda.dtPresentazione > conf.dtScadenzaDomandeIniziali) {
            this.showCheckboxAnnulloRitardoPresentazione = true;
          }
        }
      });
  }

}