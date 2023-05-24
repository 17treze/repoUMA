import { Component, OnInit } from '@angular/core';
import { AppComponent } from './app.component';
import { AuthService } from './auth/auth.service';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { Configuration } from './app.constants';
import { MenuItem } from 'primeng-lts/api/menuitem';
import { environment } from 'src/environments/environment';
import { Utente } from './auth/user';

@Component({
    selector: 'app-topbar',
    templateUrl: './app.topbar.component.html'
})

export class AppTopBarComponent implements OnInit {
    userId: string;
    user: Utente;
    public menuVisibile: boolean = false;
    items: MenuItem[];
    isGis: boolean;

    private _serviceSubscription;

    constructor(public app: AppComponent, private authService: AuthService, private router: Router, private configuration: Configuration,    private route: ActivatedRoute) {
        this._serviceSubscription = this.authService.onUserChange.subscribe(
            (next) => {
                this.userId = next.identificativo;
            }
        )
        this.router.events.subscribe((event: any) => {
            if (event instanceof NavigationEnd) {
              if (event.url === '/gis' || event.url === '/funzioniPat/gis' || event.url === '/funzioniCaa/gis') {
                this.isGis = true;
              } else {
                this.isGis = false;
              }
            }
          });
    }

    ngOnInit(): void {
        /*
        this.user = this.authService.getUserFromSession();
        if (this.user instanceof Utente) {
            this.userId = this.user.identificativo;
        }
        */
        this.items = [
            {
                label:'Richiedi modifica profilo', command: () => {
                    this.redirectModificaUtente();
                }
            },
            {
                label:'Logout', 
                command: () => {
                    this.redirectLogoutCittadino();
                }, 
                url: `${environment.backendUrl}sso/Shibboleth.sso/Logout`
            }
        ];
        if (environment.tipoLogin == 'dipendente') {
            this.items[1].command = () => {
                this.redirectLogoutDipendente();
            };
            this.items[1].disabled = true;
        }
    }

    redirectModificaUtente() {
        this.router.navigate(["/utenti/modificaUtente"]);
    }

    redirectLogoutCittadino() {
        // this.router.navigate(["/sso/Shibboleth.sso/Logout"], { relativeTo: this.route.parent.parent });
    }

    redirectLogoutDipendente() {
        this.router.navigate(["//"]);
    }

    // logout() {
    //     this.userId = null;
    //     this.router.navigate(['..']);
    // }
}
