import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { HomeService } from '../home/home.service';
import { validaInput } from '../a4g-common/validazione/validaInput';
import { FascicoloService } from '../fascicolo/fascicolo.service';

@Injectable()
export class RoleGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private homeService: HomeService,
    private fascicoloService: FascicoloService) { }

  public canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Promise<boolean> {
    const promiseNoFascicoliAziendaUtente = new Promise((resolve) => {
      if (!this.authService.isUserInRole(AuthService.rolePrivate)) {
        resolve(true);
      } else {
        let ruoliSession = this.homeService.getFascicoliAziendaUtente();
        if (ruoliSession) {
          if (ruoliSession && ruoliSession.length > 0) {
            resolve(false);
          } else {
            resolve(true);
          }
        } else {
          console.log("promiseNoFascicoliAziendaUtente");
          this.fascicoloService.ricercaFascicoliAziendaUtente().subscribe(next => {
            console.log("promiseNoFascicoliAziendaUtente trovati");
            console.log(next);
            let ruoli = next;
            this.homeService.salvaFascicoliAziendaUtente(ruoli);
            if (ruoli && ruoli.length > 0) {
              resolve(false);
            } else {
              resolve(true);
            }
          }, err => {
            console.log("promiseNoFascicoliAziendaUtente error");
            resolve(true);
            throw err;
          });
        }
      }
    });

    const promiseNoRuoliAgs = new Promise((resolve) => {
      let ruoliSession = this.homeService.getUtenzeAgsPerUtente();
      if (ruoliSession) {
        if (ruoliSession && ruoliSession.length > 0) {
          resolve(false);
        } else {
          resolve(true);
        }
      } else {
        console.log("promiseNoRuoliAgs");
        /*
	      this.homeService.ricercaUtenzeAgsPerUtente().subscribe(next => {
	        console.log("promiseNoRuoliAgs trovati");
	        console.log(next);
	        let ruoli = next;
	        this.homeService.salvaUtenzeAgsPerUtente(ruoli);
	        if (ruoli && ruoli.length > 0) {
	          resolve(false);
	        } else {
	          resolve(true);
	        }
	      }, (err) => {
	        console.log("promiseNoRuoliAgs error");
	        resolve(true);
	        throw err;
	      });
        */
        resolve(true);
      }
    });

    const promiseNoRuoliSrt = new Promise((resolve) => {
      let ruoliSession = this.homeService.getSrtRuoliPerUtente();
      if (ruoliSession) {
        if (ruoliSession && ruoliSession.length > 0) {
          resolve(false);
        } else {
          resolve(true);
        }
      } else {
        console.log("promiseNoRuoliSrt");
        /*
        if (utente && utente.codiceFiscale && validaInput.validaCf(utente.codiceFiscale, false)) {
          this.homeService.ricercaSrtRuoliPerUtente(utente.codiceFiscale).subscribe(next => {
            console.log("promiseNoRuoliSrt trovati");
            console.log(next);
            let ruoli = next;
            this.homeService.salvaSrtRuoliPerUtente(ruoli);
            if (ruoli && ruoli.length > 0)
              resolve(false);
            else
              resolve(true);
          }, (err) => {
              console.log("promiseNoRuoliSrt error");
              resolve(true);
              throw err;
            }
          )
        } else {
          console.log("No cf srt");
          resolve(true);
        }
        */
        resolve(true);
      }
    });
    const promises = [
      promiseNoFascicoliAziendaUtente,
      promiseNoRuoliAgs,
      promiseNoRuoliSrt
    ];
    return Promise
      .all(promises)
      .then(function(value)  {
        console.log("value: " + value);
        //SUCCESS
        if ((value[0] && value[1] && value[2]) == true) {
          console.log("fail");
          return Promise.resolve(false);
        } else {
          //FAILURE
          console.log("SUCCESS");
          return Promise.resolve(true);
        }
      });
  }
}
