import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable, of } from 'rxjs';
import { environment } from 'src/environments/environment';
import { catchError, switchMap } from 'rxjs/operators';
import {DomandeUnicheService} from "../../modules/domande-uniche/domande-uniche.service";
import {CaricaDto} from "../../modules/domande-uniche/models/facicoloImprese.model";
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class LoggedInGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private domandeUnicheService: DomandeUnicheService
    ) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      return this.authService.getUser(false).pipe(
        switchMap((utente) => {
          if ((utente.profili == null) || (utente.profili.length === 0)) {
            window.location.href = `${environment.mobileUrl}register`;
            return of(false);
          } else {
            let utenteAppag = utente.profili.find(e => e.identificativo === 'appag');
            if (utenteAppag) {
              return of(true);
            }
            let caa = utente.profili.find(e => e.identificativo === 'caa');
            if (caa) {
              return of(true);
            }
            let utenteAzienda = utente.profili.find(e => e.identificativo === 'azienda');
            if (!utenteAzienda) {
              return this.domandeUnicheService.getListaCariche(utente.codiceFiscale);
            }
          }
          return of(true);
        }),
        switchMap(res => {
          if (typeof res === "boolean") {
            return of(res);
          } else if (res instanceof Array && res.length > 0 && res[0] instanceof CaricaDto) {
              window.location.href = `${environment.mobileUrl}register`;
              return of(false);
          }
          return of(false);
        }),
        catchError(err => {
          console.log('caught error, providing fallback value');
          window.location.href = environment.indexPage;
          return of(false);
        })
      );
  }
}
