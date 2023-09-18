import { ErrorService } from './../../../a4g-common/services/error.service';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { EMPTY, interval, Subscription } from 'rxjs';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { HomeService } from 'src/app/home/home.service';
import { IstruttoriaAntimafiaService } from 'src/app/istruttoria/istruttoria-antimafia/istruttoria-antimafia.service';
import { LoaderService } from 'src/app/loader.service';
import { AuthService } from '../../../auth/auth.service';
import { FascicoloService } from '../../fascicolo.service';
import { FascicoloCorrente } from '../../fascicoloCorrente';
import { AntimafiaService } from '../antimafia.service';
import { Azienda } from '../classi/azienda';
import { DatiChiusuraExNovoDichiarazioneAntimafia } from '../classi/datiChiusuraExNovoDichiarazioneAntimafia';
import { DatiDichiarazione, Richiedente } from '../classi/datiDichiarazione';
import { DichiarazioneAntimafia } from '../classi/dichiarazioneAntimafia';
import { DichiarazioneAntimafiaService } from '../dichiarazione-antimafia.service';
import { FascicoloAgsDto } from 'src/app/a4g-common/classi/FascicoloAgsDto';
import { catchError, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-antimafia',
  templateUrl: './antimafia.component.html',
  styleUrls: ['./antimafia.component.css']
})

export class AntimafiaComponent implements OnInit, OnDestroy {
  cuaaImpresa: string;
  showLista = false;
  listaDichiarazioni: Array<DichiarazioneAntimafia>;
  currentDichiarazioneAntimafia: DichiarazioneAntimafia;
  errorMessage: string;
  private cfRichiedente: string;

  sub: Subscription;
  getFascicoloAgsByCuaaSub: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private dichiarazioneAntimafiaService: DichiarazioneAntimafiaService,
    private antimafiaService: AntimafiaService,
    private fascicoloCorrente: FascicoloCorrente,
    private messageService: MessageService,
    private authservice: AuthService,
    private homeservice: HomeService,
    private istruttoriaAntimafiaService: IstruttoriaAntimafiaService,
    private loader: LoaderService,
    private fascicoloService: FascicoloService,
    private errorService: ErrorService
  ) { }

  ngOnInit() {
    this.getDichiarazioniAntimafia();
    this.loader.setTimeout(900000); // 15 minuti
  }

  getDichiarazioniAntimafia(): void {
    /*
    if (this.fascicoloCorrente == null || this.fascicoloCorrente.fascicoloLegacy == null || this.fascicoloCorrente.fascicoloLegacy.cuaa == null) {
      this.getFascicoloAgsByCuaaSub = this.route.params
        .pipe(
          switchMap((params: Params) => {
            return this.fascicoloService.getLegacy(params['idFascicolo']);
          }),
          catchError((err) => {
            this.errorService.showError(err);
            return EMPTY;
          }),
          switchMap((response: FascicoloAgsDto) => {
            this.fascicoloCorrente.fascicoloLegacy = response;
            const dichiarazione = new DichiarazioneAntimafia();
            dichiarazione.azienda = new Azienda();
            dichiarazione.azienda.cuaa = this.fascicoloCorrente.fascicoloLegacy.cuaa;
            return this.antimafiaService.getDichiarazioniAntimafia(dichiarazione);
          })
        ).subscribe((dichiarazioni: Array<DichiarazioneAntimafia>) => {
          this.listaDichiarazioni = dichiarazioni || [];
          this.rerouteAntimafia();
        }, err => this.errorService.showError(err));
    } else {
      const dichiarazione = new DichiarazioneAntimafia();
      dichiarazione.azienda = new Azienda();
      dichiarazione.azienda.cuaa = this.fascicoloCorrente.fascicoloLegacy.cuaa;
      this.getFascicoloAgsByCuaaSub = this.antimafiaService.getDichiarazioniAntimafia(dichiarazione)
        .subscribe((dichiarazioni: Array<DichiarazioneAntimafia>) => {
          this.listaDichiarazioni = dichiarazioni || [];
          this.rerouteAntimafia();
        }, err => this.errorService.showError(err));
    }
    */
  }

  selected(value: string) {
    if (value) {
      this.cfRichiedente = value;
      this.rerouteAntimafiaCreaDomanda(value);
    }
  }


  rerouteAntimafiaCreaDomanda(value: string) {
    this.showLista = false;
    // filtro la lista escludendo le dichiarazioni con stato CHIUSA
    this.listaDichiarazioni = this.listaDichiarazioni.filter(x => x.stato.identificativo !== 'CHIUSA');

    let antimafiaBozza = this.listaDichiarazioni.filter(x => x.stato.identificativo === 'BOZZA');
    if (this.listaDichiarazioni.length === 0) {
      const dichiarazione = new DichiarazioneAntimafia();
      dichiarazione.azienda = new Azienda();
      dichiarazione.azienda.cuaa = this.fascicoloCorrente.fascicoloLegacy.cuaa;

      this.newDichiarazione(dichiarazione, this.cfRichiedente);
    } else {
      const statoDichiarazioneNonInCompilazione = this.listaDichiarazioni.filter(x => x.stato.identificativo !== 'BOZZA');

      if (!A4gMessages.isUndefinedOrNull(statoDichiarazioneNonInCompilazione) && statoDichiarazioneNonInCompilazione.length > 0) {
        this.currentDichiarazioneAntimafia = statoDichiarazioneNonInCompilazione[0];
        this.showWarnDomandaProtocollata(this.fascicoloCorrente.fascicoloLegacy.cuaa);
      } else {
        this.messageService.add(
          A4gMessages.getToast(
            'errorCreazioneDichiarazione',
            A4gSeverityMessage.warn,
            'ATTENZIONE: Esistono più domande in stato BOZZA'
          )
        );
        interval(3000).subscribe(n => {
          // redirect alla pagina di presentazione istanze
          this.router.navigate(['./'], { relativeTo: this.route.parent });
        });
      }
    }
  }

  rerouteAntimafia() {
    this.showLista = false;
    // filtro la lista escludendo le dichiarazioni con stato CHIUSA
    this.listaDichiarazioni = this.listaDichiarazioni.filter(x => x.stato.identificativo !== 'CHIUSA');

    const antimafiaBozza = this.listaDichiarazioni.filter(x => x.stato.identificativo === 'BOZZA');
    if (this.listaDichiarazioni.length === 0) {
      if (!(this.authservice.isUserPrivate() && this.homeservice.isAziendaUtente(this.cuaaImpresa))) {
        this.cuaaImpresa = this.fascicoloCorrente.fascicoloLegacy.cuaa;
        console.log('cuaaImpresa rerouteAntimafia' + this.cuaaImpresa);
        return;
      }
    } else if (antimafiaBozza.length === 1) {
      this.router.navigate(['./' + antimafiaBozza[0].id], { relativeTo: this.route });
    } else {
      const statoDichiarazioneNonInCompilazione = this.listaDichiarazioni.filter(x => x.stato.identificativo !== 'BOZZA');

      if (!A4gMessages.isUndefinedOrNull(statoDichiarazioneNonInCompilazione) && statoDichiarazioneNonInCompilazione.length > 0) {
        if (!(this.authservice.isUserPrivate() && this.homeservice.isAziendaUtente(this.cuaaImpresa))) {
          this.cuaaImpresa = this.fascicoloCorrente.fascicoloLegacy.cuaa;
          console.log('cuaaImpresa rerouteAntimafia more bozza' + this.cuaaImpresa);
          return;
        }
      } else {
        this.messageService.add(
          A4gMessages.getToast(
            'errorCreazioneDichiarazione',
            A4gSeverityMessage.warn,
            'ATTENZIONE: Esistono più domande in stato BOZZA'
          )
        );
        interval(3000).subscribe(n => {
          // redirect alla pagina di presentazione istanze
          this.router.navigate(['./'], { relativeTo: this.route.parent.parent });
        });
      }
    }
  }

  newDichiarazione(dichiarazione: DichiarazioneAntimafia, cuaaRichiedente: string) {
    const datiDichiarazione = new DatiDichiarazione();
    datiDichiarazione.richiedente = new Richiedente();
    datiDichiarazione.richiedente.codiceFiscale = cuaaRichiedente;

    console.log('Nuova dichiatazione utente ' + cuaaRichiedente + ' azienda ' + this.fascicoloCorrente.fascicoloLegacy.cuaa);

    dichiarazione.datiDichiarazione = datiDichiarazione;

    this.antimafiaService.creaDichiarazioneAntimafia(dichiarazione)
      .subscribe(
        data => {
          dichiarazione.id = data;
          this.dichiarazioneAntimafiaService.setDichiarazioneAntimafia(dichiarazione);
          this.router.navigate(['./' + dichiarazione.id], { relativeTo: this.route });
        },
        error => {
          console.log('Error', error);
          this.handleError(error);
        }
      );
  }

  private handleError(error: any) {
    const errMsg = error.error.message ? error.error.message : error.error;
    this.messageService.add(A4gMessages.getToast('errorCreazioneDichiarazione', A4gSeverityMessage.error, errMsg));
  }

  onRejectUpdateAndCreateNew() {
    this.messageService.clear('warn-stato-protocollata');
    // redirect alla pagina di presentazione istanze
    this.router.navigate(['./'], { relativeTo: this.route.parent });
  }

  onConfirmUpdateAndCreateNew() {
    this.messageService.clear('warn-stato-protocollata');

    const datiChiusuraExNovo: DatiChiusuraExNovoDichiarazioneAntimafia = new DatiChiusuraExNovoDichiarazioneAntimafia();
    datiChiusuraExNovo.daChiudere = this.currentDichiarazioneAntimafia;

    const dichiarazione: DichiarazioneAntimafia = new DichiarazioneAntimafia();
    dichiarazione.azienda = new Azienda();
    dichiarazione.azienda.cuaa = this.fascicoloCorrente.fascicoloLegacy.cuaa;
    const datiDichiarazione = new DatiDichiarazione();
    datiDichiarazione.richiedente = new Richiedente();
    datiDichiarazione.richiedente.codiceFiscale = this.cfRichiedente;
    dichiarazione.datiDichiarazione = datiDichiarazione;
    datiChiusuraExNovo.exNovo = dichiarazione;

    this.antimafiaService.chiudiAndRicreaDichiarazioneAntimafia(datiChiusuraExNovo)
      .subscribe(
        dichiarazioneData => {
          // se l'id è cambiato ho creato una nuova Dichiarazione
          if (dichiarazioneData.creataNuovaDichiarazione) {
            this.dichiarazioneAntimafiaService.setDichiarazioneAntimafia(dichiarazioneData.dichiarazione);
            this.router.navigate(['./' + dichiarazioneData.dichiarazione.id], { relativeTo: this.route });
          }
        },
        error => {
          console.log('Error', error);
          this.handleError(error);
        }
      );
  }

  private showWarnDomandaProtocollata(cuaa: string) {
    this.messageService.add(
      A4gMessages.getToast(
        'warn-stato-protocollata',
        A4gSeverityMessage.warn,
        A4gMessages.CHECK_DICHIARAZIONE_PROTOCOLLATA(cuaa)
      )
    );
  }

  ngOnDestroy(): void {
    this.loader.resetTimeout();
    if (this.sub) {
      this.sub.unsubscribe();
    }
    if (this.getFascicoloAgsByCuaaSub) {
      this.getFascicoloAgsByCuaaSub.unsubscribe();
    }
  }
}
