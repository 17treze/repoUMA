import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Utente } from './user';
import { AuthService } from './auth.service';
import { Configuration } from '../app.constants';
import { AuthGuard } from './auth.guard'
import { RoleGuard } from './role.guard';
import { MessageService, ConfirmationService } from 'primeng/api';
import { A4gMessages } from '../a4g-common/a4g-messages';
import { ProtocollataGuard } from './protocollata-guard';

@Injectable({
  providedIn: 'root'
})
export class UserGuard implements CanActivate {

  utente: Utente;

  constructor(private authGuard: AuthGuard, private protocollataGuard: ProtocollataGuard, private roleGuard: RoleGuard, private authService: AuthService,
    private configuration: Configuration, private messageService: MessageService, private router: Router, private confirmationService: ConfirmationService) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    console.log("canActivate UserGuard");

    return this.authGuard.canActivate(route, state).then((auth) => {
      if (!auth) {
        return Promise.resolve(false);
      } else {
        this.utente = JSON.parse(sessionStorage.getItem("user"));
        console.log(this.utente);
        if (this.utente?.ruoli &&
          this.utente?.ruoli.length > 0 &&
          this.authService.isUserInRole(AuthService.roleCaa, this.utente) ||
          this.authService.isUserInRole(AuthService.rolePrivate, this.utente) ||
          this.authService.isUserInRole(AuthService.roleAdmin, this.utente) ||
          this.authService.isUserInRole(AuthService.roleIstruttoreUMA, this.utente) || 
          this.authService.isUserInRole(AuthService.roleDistributore, this.utente)
        ) {
          return this.roleGuard.canActivate(route, state).then((auth) => {
            if (!auth) {
              console.log("Ok nessun profilo");
              // controlla se protocollata
              return this.protocollataGuard.canActivate(route, state).then((isRegistrabile) => {
                if (isRegistrabile) {
                  return Promise.resolve(true);
                } else {
                  this.confirmationService.confirm({
                    message: A4gMessages.DOMANDA_PROTOCOLLATA(this.utente.codiceFiscale),
                    accept: () => {
                      window.location.href = this.configuration.IndexPage;;
                    },
                    reject: () => { }
                  });
                }
              });
            } else {
              console.log("Trovato un profilo");
              this.confirmationService.confirm({
                message: A4gMessages.PROFILO_ASSOCIATO(this.utente.codiceFiscale),
                accept: () => {
                  this.router.navigate(["home"]);
                },
                reject: () => { }
              });
              return Promise.resolve(false);
            }
          });
        }
        else {
          console.log("Fail esistono profili");
          this.confirmationService.confirm({
            message: A4gMessages.PROFILO_ASSOCIATO(this.utente.codiceFiscale),
            accept: () => {
              this.router.navigate(["home"]);
            },
            reject: () => { }
          });
          return Promise.resolve(false);
        }
      }
    });
  }
}
