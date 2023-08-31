import { Component, OnInit, SimpleChange, NgModule } from '@angular/core';

import { NgForm } from '@angular/forms';
import { validaInput } from '../../../a4g-common/validazione/validaInput';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../auth/auth.service';
import { Utente } from '../../../auth/user';
import { HomeService } from '../../../home/home.service';
import { Fascicolo } from '../../../a4g-common/classi/Fascicolo';
import { InputFascicolo } from '../../../a4g-common/classi/InputFascicolo';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { Labels } from 'src/app/app.labels';
import { FascicoloService } from 'src/app/fascicolo/fascicolo.service';

@Component({
  selector: 'app-ricerca-fascicoli',
  templateUrl: './ricerca-fascicoli.component.html',
  styleUrls: ['./ricerca-fascicoli.component.css']
})
export class RicercaFascicoliComponent implements OnInit {
  public fascicoli: Array<Fascicolo> = new Array<Fascicolo>();
  public inputRicerca: InputFascicolo = new InputFascicolo();;
  public searchvisible: boolean = false;
  public resultvisible: boolean = false;
  title = Labels.ricercaFascicolo;
  showSuggest: boolean;
  cuaa = '';
  isLoggedIn: boolean;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private homeService: HomeService,
    private authService: AuthService,
    private messageService: MessageService,
    private fascicoloService: FascicoloService) {
  }

  ngOnInit() {

    this.isLoggedIn = this.authService.isLoggedIn();
    if (this.isLoggedIn) {
      let user = this.authService.getUser();
      if (user) {
        // ...
        if (this.authService.isUserInRole(AuthService.roleCaa) ||
          // this.authService.isUserInRole(AuthService.roleAppag) ||
          // this.authService.isUserInRole(AuthService.roleGestoreUtenti) ||
          this.authService.isUserInRole(AuthService.roleAdmin) ||
          this.authService.isUserInRole(AuthService.roleIstruttoreUMA)) {
          this.inputRicerca.cuaa = '';
          this.searchvisible = true;
          this.resultvisible = false;
        }
        else {
          this.inputRicerca.cuaa = user.codiceFiscale;
          this.fascicoloService.ricercaFascicoli(this.inputRicerca).subscribe((next) => {
            this.fascicoli = next,
            this.caricFascicoloUtente()
          });
        }
      }
    }
  }
  
  valuechange(textVal: string) {
    console.log(textVal);
    if (this.resultvisible)
      this.resultvisible = false;
    if (this.fascicoli)
      this.fascicoli.splice(0);
  }

  caricFascicoloUtente() {
    console.log('fai');
    if (this.fascicoli && this.fascicoli.length == 1) {
      this.router.navigate(['../' + this.fascicoli[0].idFascicolo], { relativeTo: this.route });
    } else {
      this.resultvisible = true;
    }
  }

  onSubmit(f: NgForm) {
    if (f.invalid) {
      // alert('CUAA e/o Denominazione non validi!');
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_DATI));
      return;
    }

    if (!(validaInput.campoNonValorizzato(this.inputRicerca.cuaa) && validaInput.campoNonValorizzato(this.inputRicerca.denominazione))) {
      // if (validaInput.validaCuaaIntero(this.inputRicerca.cuaa, true) && validaInput.validaDenominazione(this.inputRicerca.denominazione, true)) {
      if (validaInput.validaCuaaParziale(this.inputRicerca.cuaa, true) && validaInput.validaDenominazione(this.inputRicerca.denominazione, true)) {
        this.resultvisible = true
        this.fascicoloService.ricercaFascicoli(this.inputRicerca)
          .subscribe((next) => this.fascicoli = next);
      } else {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_DATI));
      }
    } else {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_DATI));
    }

  }
}
