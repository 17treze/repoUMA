import { Component, OnInit } from '@angular/core';
import { MenuItem, MessageService } from 'primeng/api';
import { ActivatedRoute, Router } from '@angular/router';
import { Costanti } from '../../Costanti';
import { AppComponent } from 'src/app/app.component';
import { IstruttoriaService } from '../../istruttoria.service';
import { SharedService } from '../../shared.service';
import { DomandaIstruttoriaDettaglio } from '../../domain/domandaIstruttoriaDettaglio';
import { DomandaIstruttoriacorrente } from '../domandaIstruttoriacorrente';
import { SostegnoDu } from '../../classi/SostegnoDu';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { switchMap, map } from 'rxjs/operators';
import { of } from 'rxjs';
import { DatiErede } from '../../domain/datiErede';
import { Istruttoria } from 'src/app/a4g-common/classi/Istruttoria';
import { IstruttoriaCorrente } from '../../istruttoriaCorrente';

@Component({
  selector: 'app-dettaglio-domanda-old',
  templateUrl: './dettaglio-domanda.component.html',
  styleUrls: ['./dettaglio-domanda.component.css']
})

export class DettaglioDomandaComponent implements OnInit {
  menu1: Array<MenuItem>;
  title: String;
  idDomandaCorrente: String;
  numeroDomanda: number;
  domandaCorrente: DomandaIstruttoriaDettaglio;
  datiIstruttoriaCorrente: Istruttoria;
  isRTL = true;
  label1: string;
  labelStatoZootecnia: string;
  labelStatoSuperficie: string;
  annulloRitardoPresentazione: boolean = false;
  showCheckboxAnnulloRitardoPresentazione: boolean = false;
  titolareDeceduto: boolean = false;
  displayDetails: boolean = false;
  a4gMessages = A4gMessages;
  infoDomanda: any;
  datiEredeDto: DatiErede;
  datiErediOk: boolean = false;
  messaggioErroreDatiErede: string;
  istruttoriaCorrente: Istruttoria;

  constructor(private route: ActivatedRoute, public app: AppComponent, private istruttoriaService: IstruttoriaService,
    private sharedService: SharedService, private domandaIstruttoriacorrente: DomandaIstruttoriacorrente,
    private messages: MessageService, private router: Router, private istruttoria: IstruttoriaCorrente) {
  }

  ngOnInit() {
    this.domandaCorrente = this.route.snapshot.data['domandaIstruttoria'];
    this.datiIstruttoriaCorrente = this.route.snapshot.data['istruttoria'];
    this.infoDomanda = this.route.snapshot.data['infoDomanda'];
    const idIStruttoria = this.route.snapshot.paramMap.get('idIstruttoria');
    this.istruttoriaService.getIstruttoria(+idIStruttoria).toPromise().then(istruttoria => {
      this.istruttoriaCorrente = istruttoria;
      this.istruttoria.istruttoria = istruttoria;
      let dtScadenzaDomande: Date = null;
      istruttoria.dtScadenzaDomande ? dtScadenzaDomande = istruttoria.dtScadenzaDomande : null;
      // controlla che la domanda sia di modifica
      if (String(this.infoDomanda['codModuloDomanda']).includes('BPS_ART_15')) {
        // e che sia stata presentata dopo la data di scadenza
        if (this.domandaCorrente.infoGeneraliDomanda.dataPresentazione > dtScadenzaDomande) {
          this.showCheckboxAnnulloRitardoPresentazione = true;
          this.annulloRitardoPresentazione = this.infoDomanda.annulloRiduzione ? true : false;
        }
      }
    });

    this.infoDomanda['lavorazioneSostegno'].forEach(lavorazione => {
      if (lavorazione.tipoSostegno === SostegnoDu.ZOOTECNIA) {
        this.labelStatoZootecnia = ' - ' + lavorazione.statoSotegno.replace(/_/g, ' ');
      }
      if (lavorazione.tipoSostegno === SostegnoDu.SUPERFICIE) {
        this.labelStatoSuperficie = ' - ' + lavorazione.statoSotegno.replace(/_/g, ' ');
      }
    });
    this.domandaIstruttoriacorrente.domanda = this.domandaCorrente;
    this.sharedService.domandSettata.emit(this.domandaIstruttoriacorrente.domanda);
    this.idDomandaCorrente = this.domandaCorrente.id.toString();
    this.numeroDomanda = this.domandaCorrente.infoGeneraliDomanda.numeroDomanda;
    this.label1 = this.domandaCorrente.statoSostegno;


    switch (this.domandaCorrente.statoSostegno) {
      case 'RICHIESTO':
        this.label1 = 'SOSTEGNO DISACCOPPIATO - RICHIESTO';
        break;
      case 'CONTROLLI_CALCOLO_OK':
        this.label1 = 'SOSTEGNO DISACCOPPIATO - CONTROLLI SUPERATI';
        break;
      case 'CONTROLLI_CALCOLO_KO':
        this.label1 = 'SOSTEGNO DISACCOPPIATO - CONTROLLI NON SUPERATI';
        break;
      case 'NON_AMMISSIBILE':
        this.label1 = 'SOSTEGNO DISACCOPPIATO - NON AMMISSIBILE';
        break;
      case 'LIQUIDABILE':
        this.label1 = 'SOSTEGNO DISACCOPPIATO - LIQUIDABILE';
        break;
      case 'CONTROLLI_LIQUIDABILE_KO':
        this.label1 = 'SOSTEGNO DISACCOPPIATO - NON LIQUIDABILE';
        break;
      case 'PAGAMENTO_NON_AUTORIZZATO':
        this.label1 = 'SOSTEGNO DISACCOPPIATO - PAGAMENTO NON AUTORIZZATO';
        break;
      case 'NON_LIQUIDABILE':
        this.label1 = 'SOSTEGNO DISACCOPPIATO - NON LIQUIDABILE';
        break;
      case 'PAGAMENTO_AUTORIZZATO':
        this.label1 = 'SOSTEGNO DISACCOPPIATO - PAGAMENTO AUTORIZZATO';
        break;
      case 'CONTROLLI_INTERSOSTEGNO_OK':
        this.label1 = 'SOSTEGNO DISACCOPPIATO - CONTROLLI INTERSOSTEGNO SUPERATI';
        break;
      case 'INTEGRATO':
        this.label1 = 'SOSTEGNO DISACCOPPIATO - INTEGRATO';
        break;
    }

    //this.title = this.route.snapshot.data[A4gCostanti.ROUTE_DATA_BREADCRUMB].toUpperCase();
    this.title = this.datiIstruttoriaCorrente.annoRiferimento + ' - ' + this.datiIstruttoriaCorrente.tipoIstruttoria;
    const labelZootecnia = 'Sostegno Accoppiato Zootecnia ' + this.labelStatoZootecnia;
    const labelSuperfici = 'Sostegno Accoppiato Superfici ' + this.labelStatoSuperficie;
    if (this.domandaCorrente.richieste.sintesiRichieste.richiestaDisaccoppiato === true &&
      this.domandaCorrente.richieste.sintesiRichieste.richiestaSuperfici === true &&
      this.domandaCorrente.richieste.sintesiRichieste.richiestaZootecnia === true) {
      this.menu1 = new Array<MenuItem>(
        { routerLink: Costanti.disaccoppiatoControlliSuperati, label: this.label1 },
        { routerLink: Costanti.accoppiatoZootecniaRichiesto, label: labelZootecnia },
        { routerLink: Costanti.accoppiatoSuperficiRichiesto, label: labelSuperfici }
      );
    } else if (this.domandaCorrente.richieste.sintesiRichieste.richiestaDisaccoppiato === true &&
      this.domandaCorrente.richieste.sintesiRichieste.richiestaSuperfici === false &&
      this.domandaCorrente.richieste.sintesiRichieste.richiestaZootecnia === false) {
      this.menu1 = new Array<MenuItem>(
        { routerLink: Costanti.disaccoppiatoControlliSuperati, label: this.label1 }
      );
    } else if (this.domandaCorrente.richieste.sintesiRichieste.richiestaDisaccoppiato === true &&
      this.domandaCorrente.richieste.sintesiRichieste.richiestaSuperfici === true &&
      this.domandaCorrente.richieste.sintesiRichieste.richiestaZootecnia === false) {
      this.menu1 = new Array<MenuItem>(
        { routerLink: Costanti.disaccoppiatoControlliSuperati, label: this.label1 },
        { routerLink: Costanti.accoppiatoSuperficiRichiesto, label: labelSuperfici }
      );
    } else if (this.domandaCorrente.richieste.sintesiRichieste.richiestaDisaccoppiato === true &&
      this.domandaCorrente.richieste.sintesiRichieste.richiestaSuperfici === false &&
      this.domandaCorrente.richieste.sintesiRichieste.richiestaZootecnia === true) {
      this.menu1 = new Array<MenuItem>(
        { routerLink: Costanti.disaccoppiatoControlliSuperati, label: this.label1 },
        { routerLink: Costanti.accoppiatoZootecniaRichiesto, label: labelZootecnia }
      );
    } else if (this.domandaCorrente.richieste.sintesiRichieste.richiestaDisaccoppiato === false &&
      this.domandaCorrente.richieste.sintesiRichieste.richiestaSuperfici === true &&
      this.domandaCorrente.richieste.sintesiRichieste.richiestaZootecnia === false) {
      this.menu1 = new Array<MenuItem>(
        { routerLink: Costanti.accoppiatoZootecniaRichiesto, label: labelZootecnia },
        { routerLink: Costanti.accoppiatoSuperficiRichiesto, label: labelSuperfici }
      );
    }

  }

  showDialog() {
    this.displayDetails = true;


    const retrieveErede = this.istruttoriaService.getInfoDomanda(+this.route.snapshot.paramMap.get('idDomanda'), ['EREDE']).pipe(
      switchMap(domanda => {
        //se esiste la domanda nel DB e l'erede è stato già certificato 
        if (domanda.erede) {
          this.titolareDeceduto = true;
          return of(domanda.erede);
        }
        //altrimenti faccio la chiamata ad AGS
        return this.istruttoriaService.getInfoLiquidabilita(this.infoDomanda.numeroDomanda).pipe(
          map((info: Array<any>) => {
            if (!this.a4gMessages.isUndefinedOrNull(info) && info.length > 0) {
              this.titolareDeceduto = info[0].infoLiquidabilita.titolareDeceduto;
              return info[0].infoLiquidabilita.datiErede;
            }
            return null;
          })
        );
      })
    );

    retrieveErede.subscribe(erede => {
      if (this.titolareDeceduto) {
        this.datiEredeDto = this.creaDatiErediDto(erede);
        if (this.datiEredeDto
          && this.datiEredeDto.cognome && this.datiEredeDto.nome
          && this.datiEredeDto.codiceFiscale && this.datiEredeDto.iban
          && this.datiEredeDto.indirizzoResidenza && this.datiEredeDto.provResidenza && this.datiEredeDto.codiceIstat) {
          this.datiErediOk = true;
        } else {
          this.datiErediOk = false;
          this.messaggioErroreDatiErede = "Non sono presenti tutti i dati dell’erede";
          this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.messaggioErroreDatiErede));
        }
      }
    },
      error => {
        console.log('Error', error);
        A4gMessages.handleError(this.messages, error, A4gMessages.SERVIZIO_NON_DISPONIBILE);
      }
    );
  }

  onCheckboxChange(checked: boolean) {
    this.infoDomanda.annulloRiduzione = checked;
    this.istruttoriaService.aggiornaDomanda(this.infoDomanda).subscribe();
  }

  onCheckboxChangeDatiErede(checked: boolean) {
    //creazione DTO da passare al servizio backend
    //this.datiEredeDto = this.creaDatiErediDto();
    this.datiEredeDto.certificato = checked;
    if (this.datiEredeDto.id) {
      //PUT aggiorna
      this.datiEredeDto.certificato = checked;
      this.istruttoriaService.aggiornaDatiErede(this.domandaCorrente.id, this.datiEredeDto).subscribe();
    } else {
      //POST inserisci
      this.istruttoriaService.creaDatiErede(this.domandaCorrente.id, this.datiEredeDto).subscribe(eredeResponse => {
        this.datiEredeDto.id = eredeResponse.id;
      });
    }
  }


  private creaDatiErediDto(datiErede) {
    const datiEredeDto = new DatiErede();
    if (datiErede) {
      Object.keys(datiErede).forEach(key => datiEredeDto[key] = datiErede[key]);
      if (!datiEredeDto.codIstatNascita) {
        datiEredeDto.codIstatNascita = datiErede.codiceIstatNascita;
      }
      return datiEredeDto;
    }
    return datiEredeDto;
  }

  confermaAnnullamento() {
    this.messages.add(A4gMessages.getToast("warn-annullaIstruttoria", A4gSeverityMessage.warn, A4gMessages.CONFERMA_ANNULLA_ISTRUTTORIA));
  };

  onRejectAnnullaIstruttoria() {
    this.messages.clear('warn-annullaIstruttoria');
  }

  annullaIstruttoria() {
    this.istruttoriaService
      .annullaIstruttoria(this.domandaCorrente.id)
      .subscribe(
        annulla => {
          this.router.navigate(['./'], { relativeTo: this.route.parent });
        },
        error => {
          console.log(error.error);
          this.messages.clear('warn-annullaIstruttoria');
          if (error.error.message == 'ANNULLA_ISTRUTTORIA_DOMANDA_IN_PAGAMENTO')
            this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ANNULLA_ISTRUTTORIA_DOMANDA_IN_PAGAMENTO));
          else if (error.error.message == 'ANNULLA_ISTRUTTORIA_ELIMINAZIONE_DATI_ISTRUTTORIA')
            this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ANNULLA_ISTRUTTORIA_KO));
          else if (error.error.message == 'ANNULLA_ISTRUTTORIA_MOVIMENTAZIONE_AGS')
            this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ANNULLA_ISTRUTTORIA_KO));
          else
            this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ANNULLA_ISTRUTTORIA_KO));
        });
  }

  getIstruttoriaCorrente(): Istruttoria {
    return this.istruttoriaCorrente;
  }
}
