import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot } from "@angular/router";
import { ConfirmationService } from "primeng/api";
import { Observable, Observer } from "rxjs";
import { switchMap } from "rxjs/operators";
import { environment } from 'src/environments/environment';
import { A4gMessages } from "../a4g-common/a4g-messages";
import { HomeService } from "../home/home.service";
import { AuthService } from "./auth.service";

@Injectable({
    providedIn: 'root'
})
export class UtenteGuard implements CanActivate {

    constructor(
        private homeService: HomeService,
        private authService: AuthService,
        private confirmationService: ConfirmationService) { }

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean | any {
            
        let utente = this.authService.getUserFromSession();
        const obDomandaInLavorazione = Observable.create((observer: Observer<boolean>) => {
            this.confirmationService.confirm({
                message: A4gMessages.erroreRichiestaPresente,
                accept: () => {
                    observer.next(false);
                    observer.complete();
                    this.vaiAHome();
                },
                reject: () => {
                    observer.next(false);
                    observer.complete();
                    this.vaiAHome();
                }
            });
        });
        const obDomandaProtocollata = Observable.create((observer: Observer<boolean>) => {
            this.confirmationService.confirm({
                message: A4gMessages.PROFILO_ASSOCIATO(utente.codiceFiscale),
                accept: () => {
                    observer.next(true);
                    observer.complete();
                },
                reject: () => {
                    observer.next(false);
                    observer.complete();
                }
            });
        });
        //Se ho una domanda protocollata e un profilo valido nel caso in cui tenti di modificare il profilo mi deve comparire il messaggio di cortesia
        return this.homeService.verificaUtente().pipe(
            switchMap((isRegistrabile) => {
                if (!isRegistrabile) {
                    return obDomandaInLavorazione;
                } else {
                    return obDomandaProtocollata;
                }
            }));
    }


    public vaiAHome() {
        let urlReindirizzamento: string = "/"
        let user = this.authService.getUserFromSession();  
        if ((user.profili == null) || (user.profili.length == 0)) {
            urlReindirizzamento = environment.indexPage;
        } else {
            urlReindirizzamento = environment.frontendUrl;
        }
        window.location.href = urlReindirizzamento;
    }
}