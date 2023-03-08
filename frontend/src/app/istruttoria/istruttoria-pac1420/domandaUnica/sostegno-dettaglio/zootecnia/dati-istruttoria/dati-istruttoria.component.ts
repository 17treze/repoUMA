import { ConfigurazioneIstruttoriaService } from './../../../sostegno-shared/configurazione-istruttoria/shared/configurazione.istruttoria.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MenuItem, MessageService } from 'primeng/api';
import { Subject } from 'rxjs';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { SostegnoDu } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/SostegnoDu';
import { Costanti } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/Costanti';
import { IstruttoriaDomandaUnica } from '../../../classi/IstruttoriaDomandaUnica';
import { DatiIstruttoriaAccoppiati } from '../../../domain/datiIstruttoriaAccoppiati';
import { IstruttoriaDettaglioService } from '../../shared/istruttoria-dettaglio.service';

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
    private istruttoriaDettaglioService: IstruttoriaDettaglioService,
    private messageService: MessageService,
    private confService: ConfigurazioneIstruttoriaService
  ) { }

  ngOnInit() {
    this.istruttoriaDUCorrente = this.route.snapshot.data['domandaIstruttoria'];
    this.setSostegno();
    this.getDatiIstruttoria();
    this.controllaRitardo();
  }

  private setSostegno() {

    this.selectedSostegno = SostegnoDu.ZOOTECNIA;
    this.tipoSostegno = Costanti.acz
    this.tipoDettaglio = 'datiIstruttoriaACZ';
    this.service = 'putDettaglioDatiIstruttoriaACZ';
  }


  private getDatiIstruttoria() {
    this.istruttoriaDettaglioService.getDatiIstruttoriaDuAcz(this.istruttoriaDUCorrente.id.toString())
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

    this.istruttoriaDettaglioService.saveOrUpdateDatiIstruttoriaDuAcz(this.istruttoriaDUCorrente.id.toString(), this.datiIstruttoriaDaInserire)
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