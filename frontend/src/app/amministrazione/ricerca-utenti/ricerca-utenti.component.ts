import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import * as FileSaver from "file-saver";
import { MessageService } from 'primeng/api';
import { empty, forkJoin, Subject } from 'rxjs';
import { catchError, takeUntil } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { DatiAnagrafici } from 'src/app/utenti/classi/datiAnagrafici';
import { UtentiService } from 'src/app/utenti/utenti.service';
import { DatiUtente, ProfiliAgs, ProfiliSrt } from './dto/Profili';
import { ModificaConfigurazioneUtenteDialogComponent } from './modifica-configurazione-utente-dialog/modifica-configurazione-utente-dialog.component';

class UtenteProfiloPresentationValue {
  id: number;
  denominazione: string
  profiliA4g: ProfileA4gPresentation[];
  profiliAgs: string[];
  ruoloSrt: string;

  public addA4gProfile(prf: string, lock: boolean) {
    if (!this.profiliA4g)
      this.profiliA4g = new Array<ProfileA4gPresentation>();
    if (!this.profiliA4g.some(p => (p.descrizione === prf))) {
      this.profiliA4g.push({ descrizione: prf, disabled: lock });
    }
  }

  public addAgsProfile(prf: string) {
    if (!this.profiliAgs) {
      this.profiliAgs = [];
    }
    if (!this.profiliAgs.includes(prf)) {
      this.profiliAgs.push(prf);
    }
  }
}

class UserPresentation {
  cf: string;
  profili: UtenteProfiloPresentationValue;
}

class ProfileA4gPresentation {
  descrizione: string;
  disabled: boolean;
}

@Component({
  selector: 'app-ricerca-utenti',
  templateUrl: './ricerca-utenti.component.html',
  styleUrls: ['./ricerca-utenti.component.css']
})
export class RicercaUtentiComponent implements OnInit, OnDestroy {
  nome: string;
  cognome: string;
  codiceFiscale: string;
  datiUtente: DatiAnagrafici;
  private componentDestroyed$: Subject<boolean> = new Subject();
  utenti = [];
  idDettaglioUtente: number;

  @ViewChild("configurazioneUtenteDialog", { static: true })
  public configurazioneUtenteDialog: ModificaConfigurazioneUtenteDialogComponent;

  constructor(
    private utentiService: UtentiService,
    private route: ActivatedRoute,
    private messages: MessageService,
    private router: Router
  ) {}

  ngOnInit() {
    this.datiUtente = new DatiAnagrafici();
    this.route.queryParams.pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(params => {
      let ricercaNome: string = params['ricerca-nome'];
      let ricercaCognome: string = params['ricerca-cognome'];
      let ricercaCF: string = params['ricerca-cf'];
      if (ricercaNome || ricercaCognome || ricercaCF) {
        this.nome = ricercaNome;
        this.cognome = ricercaCognome;
        this.codiceFiscale = ricercaCF;
        this.eseguiRicerca(ricercaNome, ricercaCognome, ricercaCF);
      }
    });
  }

  private eseguiRicerca(nome: string, cognome: string, codiceFiscale: string) {
    this.utenti = [];
    this.datiUtente.nome = nome;
    this.datiUtente.cognome = cognome;
    this.datiUtente.codiceFiscale = codiceFiscale;
    // effettuo le chiamate ai 3 sistemi di SRT AGS e A4G
    // solo dopo la conslusione delle chiamate effettuo il merge dei profili per codice fiscale (this.creaUtenteCompleto)
    forkJoin([
      this.utentiService.getProfiliSrt(this.datiUtente).pipe(
        catchError(e => {
          this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDatiSrt));
          return empty();
        })),
      this.utentiService.getProfiliAgs(this.datiUtente).pipe(
        catchError(e => {
          this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDatiAgs));
          return empty();
        })),
      this.utentiService.getProfiliA4g(this.datiUtente).pipe(
        catchError(e => {
          this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDatiA4g));
          return empty();
        }))
    ]).pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(result => {
      let profiliSrt: ProfiliSrt[] = this.getProfilesArray<ProfiliSrt>(result[0]);
      let profiliAgs: ProfiliAgs[] = this.getProfilesArray<ProfiliAgs>(result[1]);
      let profiliA4g: DatiUtente[] = this.getProfilesArray<DatiUtente>(result[2]);
      this.creaUtenteCompleto(profiliSrt, profiliAgs, profiliA4g);
    }, err => {
      console.log(err);
    });
  }

  public onSubmit(f: NgForm) {
    let queryParams = {};
    if (this.nome) {
      queryParams['ricerca-nome'] = this.nome;
    }
    if (this.cognome) {
      queryParams['ricerca-cognome'] = this.cognome;
    }
    if (this.codiceFiscale) {
      queryParams['ricerca-cf'] = this.codiceFiscale;
    }
    console.log('onSubmit routre navigaate');
    this.router.navigate(['./'], { relativeTo: this.route, queryParams: queryParams });
  }

  private getProfilesArray<T>(result) {
    let pArray: T[] = [];
    if (result) {
      result.forEach(element => {
        pArray.push(element);
      });
    }
    return pArray;
  }

  openConfigura(isEnabled: boolean, cf: string): boolean {
    if (isEnabled) {
      this.configurazioneUtenteDialog.onOpenOnUserID(cf);
    }
    return false;
  }

  public onDialogClose(evento) {
  }

  creaUtenteCompleto(
    profiliSrt: ProfiliSrt[],
    profiliAgs: ProfiliAgs[],
    profiliA4g: DatiUtente[]) {
    let utenteProfilo: Map<string, UtenteProfiloPresentationValue> = new Map<string, UtenteProfiloPresentationValue>();
    /*
    if (profiliSrt) {
      profiliSrt.forEach(element => {
        if (element) {
          let elementProfiloUtente: UtenteProfiloPresentationValue = utenteProfilo[this.toUpperCaseAndTrim(element.codiceFiscale)];
          if (elementProfiloUtente) {
            elementProfiloUtente.ruoloSrt = elementProfiloUtente.ruoloSrt + ', ' + element.ruolo;
          } else {
            elementProfiloUtente = new UtenteProfiloPresentationValue();
            elementProfiloUtente.denominazione = this.toUpperCaseAndTrim(element.nome) + ' ' + this.toUpperCaseAndTrim(element.cognome);
            elementProfiloUtente.ruoloSrt = element.ruolo;
            utenteProfilo[this.toUpperCaseAndTrim(element.codiceFiscale)] = elementProfiloUtente;
          }
        }
      });
    }
    if (profiliAgs) {
      profiliAgs.forEach(element => {
        if (element) {
          let elementProfiloUtente: UtenteProfiloPresentationValue = utenteProfilo[this.toUpperCaseAndTrim(element.cf)];
          if (!elementProfiloUtente) {
            elementProfiloUtente = new UtenteProfiloPresentationValue();
            elementProfiloUtente.denominazione = this.toUpperCaseAndTrim(element.nome) + ' ' + this.toUpperCaseAndTrim(element.cognome);
            elementProfiloUtente.addAgsProfile(element.utenza);
            utenteProfilo[this.toUpperCaseAndTrim(element.cf)] = elementProfiloUtente;
          } else {
            elementProfiloUtente.addAgsProfile(element.utenza);
          }
        }
      });
    }
    */
    /*
    if (profiliA4g) {
      profiliA4g.forEach(element => {
        if (element) {
          let elementProfiloUtente: UtenteProfiloPresentationValue = utenteProfilo[this.toUpperCaseAndTrim(element.codiceFiscale)];
          if (!elementProfiloUtente) {
            elementProfiloUtente = new UtenteProfiloPresentationValue();
            elementProfiloUtente.id = element.id;
            elementProfiloUtente.denominazione = this.toUpperCaseAndTrim(element.nome) + ' ' + this.toUpperCaseAndTrim(element.cognome);
            utenteProfilo[this.toUpperCaseAndTrim(element.codiceFiscale)] = elementProfiloUtente;
            element.profili.forEach(el => {
              if (el.responsabilita != 'UTENZA_TECNICA') {
                elementProfiloUtente.addA4gProfile(el.descrizione, el.disabled);
              }});
          } else {
            element.profili.forEach(el => {
              if (el.responsabilita != 'UTENZA_TECNICA') {
                elementProfiloUtente.addA4gProfile(el.descrizione, el.disabled);
              }
            });
            elementProfiloUtente.id = element.id;
          }
        }
      });
    }
    */
    if (Object.keys(utenteProfilo).length === 0) {
      this.messages.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.utenteNonTrovato));
    } else {
      let utentiList: UserPresentation[] = [];
      Object.keys(utenteProfilo).map(function(key) {
        let userPresentation: UserPresentation = new UserPresentation();
        userPresentation.cf = key;
        userPresentation.profili = utenteProfilo[key];
        utentiList.push(userPresentation);
        return utentiList;
      });
      utentiList.sort(function (a, b) {
        return a.profili.denominazione > b.profili.denominazione ? 1
             : a.profili.denominazione < b.profili.denominazione ? -1
             : a.cf > b.cf ? 1
             : a.cf < b.cf ? -1
             : 0
      })
      this.utenti = utentiList;
    }
  }


  public downloadUtentiA4gCsv() {
    this.utentiService.getEsportaUtentiA4gCsv().pipe(
    takeUntil(this.componentDestroyed$))
    .subscribe(csv => {
      const fileName = "utentiA4g.csv"; //TODO recuprare nome file da BE 
      FileSaver.saveAs(csv.body, fileName);
    }, err => {
      this.messages.add(A4gMessages.getToast("tst", A4gSeverityMessage.error, A4gMessages.ERRORE_STAMPA_UTENTI_A4G));
    });
  }




  public toUpperCaseAndTrim(str: string): string {
    if (str) {
      return str.toUpperCase().trim();
    } else {
      return "";
    }
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  vaiAlloStorico(utente) {
    this.router.navigate(
      ['./ricercaUtenti/:idUtenza/storicoUtenza'
        .replace(':idUtenza', utente.profili.id)
      ],
      { relativeTo: this.route.parent.parent }
    );
  }
}
