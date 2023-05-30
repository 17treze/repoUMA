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
          if (ruoliSession.length > 0) {
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

    const promises = [
      promiseNoFascicoliAziendaUtente,
      // promiseNoRuoliAgs,
      // promiseNoRuoliSrt
    ];
    return Promise
      .all(promises)
      .then(function(value)  {
        console.log("Value: " + value[0]);
        if (!value[0]) {
          console.log("FAILURE");
          return Promise.resolve(false);
        } else {
          console.log("SUCCESS");
          return Promise.resolve(true);
        }
      });
  }
}
