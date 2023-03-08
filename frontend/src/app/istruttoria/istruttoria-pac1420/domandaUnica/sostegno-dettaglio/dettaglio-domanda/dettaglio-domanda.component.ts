import { Component, OnInit, OnDestroy } from '@angular/core';
import { MenuItem, MessageService } from 'primeng/api';
import { ActivatedRoute, Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import { IstruttoriaService } from '../../istruttoria.service';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { switchMap, map } from 'rxjs/operators';
import { of } from 'rxjs';
import { DatiErede } from '../../domain/datiErede';
import { IstruttoriaDomandaUnica } from '../../classi/IstruttoriaDomandaUnica';
import { TranslateService } from '@ngx-translate/core';
import { IstruttoriaDomandaUnicaFilter } from '../../classi/IstruttoriaDomandaUnicaFilter';
import { Paginazione } from 'src/app/a4g-common/utility/paginazione';
import { IstruttoriaPagina } from '../../classi/IstruttoriaPagina';
import { LoaderService } from 'src/app/loader.service';
import { SostegnoDu } from '../../classi/SostegnoDu';
import * as _ from 'lodash';

@Component({
  selector: 'app-dettaglio-domanda',
  templateUrl: './dettaglio-domanda.component.html',
  styleUrls: ['./dettaglio-domanda.component.css']
})

export class DettaglioDomandaComponent implements OnInit, OnDestroy {
  menu1: Array<MenuItem>;
  isRTL = true;
  titolareDeceduto: boolean = false;
  displayDetails: boolean = false;
  a4gMessages = A4gMessages;
  infoDomanda: any;
  datiEredeDto: DatiErede;
  datiErediOk: boolean = false;
  messaggioErroreDatiErede: string;
  istruttoriaDomandaUnicaFilter: IstruttoriaDomandaUnicaFilter = new IstruttoriaDomandaUnicaFilter();
  paginazione: Paginazione = Paginazione.of(0, 10, 'id', 'ASC');
  paginaIstruttorie: IstruttoriaPagina;
  istruttoriaDUCorrente: IstruttoriaDomandaUnica;


  constructor(private route: ActivatedRoute, public app: AppComponent, private istruttoriaService: IstruttoriaService,
      private messages: MessageService, private router: Router,
      private translateService: TranslateService,
      private loader: LoaderService
    ) {
  }

  ngOnInit() {
    this.istruttoriaDUCorrente = this.route.snapshot.data['domandaIstruttoria'];
    this.infoDomanda = this.istruttoriaDUCorrente.domanda;
    console.log(this.route.snapshot.data['sostegno']);
    this.buildMenu();
    this.loader.setTimeout(600000); //10 minuti
    //recupero dati settori / configurazione per controllo dtScadenzaDomande
  }

  ngOnDestroy(): void {
    this.loader.resetTimeout();
  }

  buildMenu() {
    this.istruttoriaDomandaUnicaFilter.tipo = this.istruttoriaDUCorrente.tipo;
    this.istruttoriaDomandaUnicaFilter.campagna = this.istruttoriaDUCorrente.domanda.campagna;
    this.istruttoriaDomandaUnicaFilter.numeroDomanda = this.istruttoriaDUCorrente.domanda.numeroDomanda;

    this.istruttoriaService.ricercaIstruttorieDU(this.istruttoriaDomandaUnicaFilter, this.paginazione).subscribe(
      result => {
        this.paginaIstruttorie = result;
        let nrIstruttorie: number = this.paginaIstruttorie.count;
        this.setOrder(this.paginaIstruttorie.risultati);
        this.paginaIstruttorie.risultati = _.sortBy(this.paginaIstruttorie.risultati, "order");
        let index: number = 0;
        let labelTab: Array<MenuItem> = [];
        let menuNew: MenuItem;
        for (index = 0; index < nrIstruttorie; index++) {
          menuNew = { routerLink: '/funzioniPat/istruttoriaPac1420/' + this.paginaIstruttorie.risultati[index].domanda.campagna + '/' + this.paginaIstruttorie.risultati[index].sostegno.toLowerCase() + '/saldo/istruttoria/' + this.paginaIstruttorie.risultati[index].id, 
                      label:  this.translateService.instant("SOSTEGNO." + this.paginaIstruttorie.risultati[index].sostegno) +" - "+ this.translateService.instant("STATO_LAVORAZIONE_SOSTEGNO." + this.paginaIstruttorie.risultati[index].stato)};
          labelTab.push(menuNew);
        }
        this.menu1 = labelTab;
    }, error => {
        this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
      });
  }

  private setOrder(istruttorieList: Array<IstruttoriaDomandaUnica>) {
    istruttorieList.forEach(istruttoria => {
      if (istruttoria.sostegno === SostegnoDu.DISACCOPPIATO) {
        istruttoria.order = 1;
      } else if (istruttoria.sostegno === SostegnoDu.ZOOTECNIA) {
        istruttoria.order = 2;
      } else if (istruttoria.sostegno === SostegnoDu.SUPERFICIE) {
        istruttoria.order = 3;
      }
    });
  }

showDialog() {
    this.displayDetails = true;

    const retrieveErede = this.istruttoriaService.getInfoDomanda(+this.istruttoriaDUCorrente.domanda.id, ['EREDE']).pipe(
      switchMap(domanda => {
        //se esiste la domanda nel DB e l'erede è stato già certificato 
        if (domanda.erede) {
          this.titolareDeceduto = true;
          return of(domanda.erede);
        }
        //altrimenti faccio la chiamata ad AGS
        return this.istruttoriaService.getInfoLiquidabilita(this.istruttoriaDUCorrente.domanda.numeroDomanda).pipe(
          map((info) => {
            if (!this.a4gMessages.isUndefinedOrNull(info)) {
              this.titolareDeceduto = info.infoLiquidabilita.titolareDeceduto;
              return info.infoLiquidabilita.datiErede;
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
        this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, 'Errore nel recupero dei dati dell\'erede'));
      }
    );
  }

  onCheckboxChange(checked: boolean) {
    this.infoDomanda.annulloRiduzione = checked;
    this.istruttoriaService.aggiornaDomanda(this.infoDomanda).subscribe();
  }

  onCheckboxChangeDatiErede(checked: any) {
    //creazione DTO da passare al servizio backend
    //this.datiEredeDto = this.creaDatiErediDto();
    this.datiEredeDto.certificato = checked.checked;
    if (this.datiEredeDto.id) {
      //PUT aggiorna
      this.datiEredeDto.certificato = checked.checked;
      this.istruttoriaService.aggiornaDatiErede(this.istruttoriaDUCorrente.domanda.id, this.datiEredeDto).subscribe();
    } else {
      //POST inserisci
      this.istruttoriaService.creaDatiErede(this.istruttoriaDUCorrente.domanda.id, this.datiEredeDto).subscribe(eredeResponse => {
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
      .annullaIstruttoria(this.istruttoriaDUCorrente.domanda.id)
      .subscribe(
        annulla => {
          this.router.navigate(['/funzioniPat/istruttoriaPac1420']);
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

  getIstruttoriaCorrente(): IstruttoriaDomandaUnica {
    return this.istruttoriaDUCorrente;
  }
}
