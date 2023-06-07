import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Utente } from './user';
import { AuthService } from './auth.service';
import { HomeService } from '../home/home.service';
import { Configuration } from '../app.constants';
import { AuthGuard } from './auth.guard'
import { RoleGuard } from './role.guard';
import { MessageService, ConfirmationService } from 'primeng/api';
import { A4gSeverityMessage, A4gMessages, A4gUserMessages } from '../a4g-common/a4g-messages';
import { ProtocollataGuard } from './protocollata-guard';
import { stringify } from '@angular/compiler/src/util';

@Injectable({
  providedIn: 'root'
})
export class HomeGuard implements CanActivate {

  utente: Utente;

  constructor(private authGuard: AuthGuard, private roleGuard: RoleGuard, private authService: AuthService,
    private router: Router, private protocollataGuard: ProtocollataGuard, private configuration: Configuration, private confirmationService: ConfirmationService) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    console.log(route);

    console.log("canActivate HomeGuard");

    return this.authGuard.canActivate(route, state).then((auth) => {
      if (!auth) {
        return Promise.resolve(false);
      } 
      else {
        this.utente = this.authService.getUser();
        console.log("CaricaUtente HomeGuard " + this.utente?.codiceFiscale);
        if (this.utente?.profili?.length > 0 &&
          this.authService.isUserInRole(AuthService.roleCaa, this.utente) ||
          this.authService.isUserInRole(AuthService.roleAppag, this.utente) ||
          this.authService.isUserInRole(AuthService.roleGestoreUtenti, this.utente) ||
          this.authService.isUserInRole(AuthService.roleAdmin, this.utente) ||
          this.authService.isUserInRole(AuthService.roleIstruttoreAMF, this.utente) ||
          this.authService.isUserInRole(AuthService.roleIstruttoreDomandaUnica, this.utente) ||
          this.authService.isUserInRole(AuthService.roleAltroEnte, this.utente) ||
          this.authService.isUserInRole(AuthService.roleViewerPAT, this.utente) ||
          this.authService.isUserInRole(AuthService.roleBackOffice, this.utente) ||
          this.authService.isUserInRole(AuthService.roleViticolo, this.utente) ||
          this.authService.isUserInRole(AuthService.roleIstruttoreUMA, this.utente) || 
          this.authService.isUserInRole(AuthService.roleDistributore, this.utente) ||
          this.authService.isUserInRole(AuthService.roleDogane, this.utente) ||
          this.authService.isUserInRole(AuthService.roleResponsabileFascicoloPat, this.utente)
        ) {
          console.log("SUCCESS (profili)");
          return Promise.resolve(true);
        }
        return this.roleGuard.canActivate(route, state).then((auth) => {
          if (!auth) {
            this.protocollataGuard.canActivate(route, state).then((isRegistrabile) => {
              if (isRegistrabile) {
                this.confirmationService.confirm({
                  message: A4gMessages.NESSUN_PROFILO(this.utente.codiceFiscale),
                  accept: () => {
                    this.router.navigate([this.configuration.UrlRedirectUtenti]);
                  },
                  reject: () => { }
                });
              }
              else {
                this.confirmationService.confirm({
                  message: A4gMessages.DOMANDA_PROTOCOLLATA(this.utente.codiceFiscale),
                  accept: () => {
                    window.location.href = this.configuration.IndexPage;
                  },
                  reject: () => { }
                });
              }
            })
            return Promise.resolve(false);
          }
          else {
            return Promise.resolve(true);
          }
        })
      }
    })
  }
}
